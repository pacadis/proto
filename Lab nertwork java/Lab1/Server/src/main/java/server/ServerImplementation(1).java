package server;

import models.*;
import service.Service;
import services.IObserver;
import services.IServices;
import services.ServerException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImplementation implements IServices {
    private Service service;
    private Iterable<Oficiu> angajati;
    private Iterable<CursaDTO> curse;
    private Map<String, IObserver> loggedEmployees;
    private final int noOfThreads = 5;

    public ServerImplementation(Service service) {
        this.service = service;
        angajati = service.findAllEmployees();
        curse = service.findlAllCursaDto();
        loggedEmployees = new ConcurrentHashMap<>();
    }

    public synchronized void login(DTOAngajat angajat, IObserver client) throws ServerException {
        boolean isEmployee=this.service.login(angajat.getUsername(),angajat.getPassword());
        if(isEmployee){
            if(loggedEmployees.get(angajat.getUsername())!=null){
                throw new ServerException("User is already logged in");
            }
            loggedEmployees.put(angajat.getUsername(),client);
            //notifyEmployeeLoggedIn(angajat);
        }else{
            System.out.println("Authentiocation failed");
            throw new ServerException("Wrong username or password");
        }

    }

    private void notifyEmployeeSubmitted(DTOAngajat angajat) throws ServerException{
        System.out.println("S-a apelat notifyEmployeeSubmitted");
        Iterable<Oficiu> employees=this.service.findAllEmployees();
        //for(DTOAngajat a : employees){
        //  System.out.println(a.getUsername());
        //}
        this.curse=this.service.findlAllCursaDto();
        CursaDTO[] result=convert(this.curse);
        ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
        for(Oficiu ang : employees){

            IObserver client=loggedEmployees.get(ang.getUser());
            System.out.println(client);
            if(client!=null){
                executor.execute(()->{
                    try{
                        System.out.println("Notifiying employee "+ang.getUser()+" employee "+angajat.getUsername()+" submitted");
                        client.AngajatSubmitted(result);
                        System.out.println("Employee "+ang.getUser()+" notified");
                    }catch (ServerException e){
                        System.err.println("Error notifying about submit");
                    }
                });
            }else System.out.println("Error gettting logged in employees");
        }
        executor.shutdown();
    }

    public CursaDTO[] convert(Iterable<CursaDTO> source){
        ArrayList<CursaDTO> result=new ArrayList<>();
        for (CursaDTO c : source){
            result.add(c);
        }
        return result.toArray(new CursaDTO[result.size()]);
    }

    public synchronized void logout(DTOAngajat angajat, IObserver client) throws ServerException {
        IObserver localClient=loggedEmployees.remove(angajat.getUsername());
        if(localClient==null){
            throw new ServerException("User "+angajat.getUsername()+" is not logged in");
        }
       // notifyEmployeeLoggedOut(angajat);

    }

    public synchronized void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException {
        System.out.println("Submitting by "+infoSubmit.getUserWho()+" ....");
        try{
            this.service.addInscriereParticipant(infoSubmit.getNumePart(),infoSubmit.getNumeEchipa(),infoSubmit.getCapacitate());
            System.out.println("New submit saved in database");
            notifyEmployeeSubmitted(infoSubmit.getWho());
        }catch(ServerException e){
            throw new ServerException("Could not submit ..."+e);
        }
    }

    public synchronized CursaDTO[] getCurseDisp() throws ServerException {
        ArrayList<CursaDTO> rez=new ArrayList<>();
        for(CursaDTO c :this.curse){
            rez.add(c);
        }
        return rez.toArray(new CursaDTO[rez.size()]);
    }


    public synchronized ParticipantCursaDTO[] searchByTeam(String team) throws ServerException {
        Iterable<ParticipantCursaDTO> result=this.service.findAllParticipantiCursaDto(team);
        ArrayList<ParticipantCursaDTO> ret=new ArrayList<>();
        for(ParticipantCursaDTO part : result){
            ret.add(part);
        }
        return ret.toArray(new ParticipantCursaDTO[ret.size()]);
    }
}
