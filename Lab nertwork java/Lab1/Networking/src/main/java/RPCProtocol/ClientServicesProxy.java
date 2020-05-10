package RPCProtocol;

import com.sun.corba.se.pept.transport.ReaderThread;
import models.*;
import services.IObserver;
import services.IServices;
import services.ServerException;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientServicesProxy implements IServices {

    private String host;
    private int port;

    private IObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<Protocol.Response> responses;
    private volatile boolean finished = true;

    public ClientServicesProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<Protocol.Response>();
    }


    @Override
    public void login(DTOAngajat angajat, IObserver client) throws ServerException {
        initializeConnection();
        Protocol.Request request = Master.CreateLoginRequest(angajat);
        sendRequest(request);
        Protocol.Response response = readResponse();
        if (response.getType() == Protocol.Response.Type.OK){
            this.client = client;
            return;
        }
        if (response.getType() == Protocol.Response.Type.ERROR){
            String err = Master.getError(response);
            closeConnection();
            throw new ServerException("Eroare raspuns login "+err);
        }
    }

    @Override
    public void logout(DTOAngajat angajat, IObserver client) throws ServerException {
        Protocol.Request request = Master.CreateLogoutRequest(angajat);
        sendRequest(request);
        Protocol.Response response = readResponse();
        closeConnection();
        if (response.getType() == Protocol.Response.Type.ERROR){
            String err = Master.getError(response);
            throw new ServerException("Eroare raspuns logout "+ err);
        }

    }

    @Override
    public void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException {
        System.out.println("Se apeleaza submitInscriere din ClientServerProxy");
        Protocol.Request request = Master.CreateSubmitInscRequest(infoSubmit);
        System.out.println(infoSubmit.getUserWho()+" submitted");
        System.out.println("Sending submit request "+request);
        sendRequest(request);
        System.out.println("Waiting response");
        Protocol.Response response = readResponse();
        if (response.getType() == Protocol.Response.Type.OK){
            System.out.println("Succesfully submitted.");
        }
        if (response.getType() == Protocol.Response.Type.ERROR){
            String err = Master.getError(response);
            throw new ServerException("Eroare raspuns submit "+err);
        }

    }

    @Override
    public CursaDTO[] getCurseDisp() throws ServerException {
        Protocol.Request request = Master.CreateGetCurrentCurseRequest();
        sendRequest(request);
        Protocol.Response response = readResponse();
        if (response.getType() == Protocol.Response.Type.ERROR){
            String err = Master.getError(response);
            throw new ServerException("Eroare incarcare curse "+err);
        }
        return Master.getCurse(response);
    }

    @Override
    public ParticipantCursaDTO[] searchByTeam(String team) throws ServerException {
        Protocol.Request request = Master.CreateSearchByTeamRequest(team);
        sendRequest(request);
        Protocol.Response response = readResponse();
        if (response.getType() == Protocol.Response.Type.ERROR){
            String err = Master.getError(response);
            throw new ServerException("Eroare cautare dupa echipa" + err);
        }
        return Master.getSearchResult(response);
    }


    private void startReadeer(){
        Thread tr = new Thread(new ReaderThread());
        tr.start();
    }

    private void initializeConnection()throws ServerException{
        try{
            connection = new Socket(host,port);
            output = connection.getOutputStream();
//            output.flush();
            input =connection.getInputStream();
            finished = false;
            startReadeer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Protocol.Response readResponse() throws ServerException{
        Protocol.Response response = null;
        try{
            response = responses.take();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Protocol.Request request) throws ServerException{
        try{
            System.out.println("Sending request ..." + request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request send...");
        }catch (IOException e){
            throw new ServerException("Error sending objeect"+e);
        }
    }

    private void closeConnection(){
        finished = true;
        try{
            output.close();
            input.close();
            connection.close();
            client = null;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean idUpdate(Protocol.Response response){
        return  response.getType() == Protocol.Response.Type.NEW_SUBMIT;
    }

    private class ReaderThread implements Runnable{

        @Override
        public void run() {
            while (!finished){
                try{
                    Protocol.Response response = Protocol.Response.parseDelimitedFrom(input);
                    System.out.println("Response received: "+ response);
                    if(idUpdate(response)){
                        handleUpdate(response);
                    }else{
                        try{
                            responses.put(response);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }catch (IOException e){
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(Protocol.Response response){
        if(response.getType() == Protocol.Response.Type.NEW_SUBMIT){
            CursaDTO[] cursa = Master.getCurse(response);
            System.out.println("Employee submitted from handleUpdate ClientServicesProxy");
            try{
                client.AngajatSubmitted(cursa);
            }catch (ServerException e){
                e.printStackTrace();
            }
        }
    }




}
