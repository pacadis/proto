
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.ServerImplementation;
import service.Service;
import services.IServices;
import services.ServerException;
import utils.AbstractServer;
import utils.RPCConcurentServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartServer {

    private static int defaultport = 55555;
    static Service getService(){
        ApplicationContext context=new ClassPathXmlApplicationContext("/BeanXML.xml");
        Service service = context.getBean(Service.class);
        return service;
    }

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File("E:\\Anul2\\Semestrul 2\\MPP\\ALTELE\\Lab nertwork java\\Lab1\\Server\\src\\main\\resources\\Server.properties"));
            serverProps.load(input);
//            serverProps.load(StartServer.class.getResourceAsStream("./Server.properties"));
            System.out.println("Server properties set");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.out.println("Could not find Server.properties");
            return;
        }
        IServices ServiceImplementation = new ServerImplementation(getService());
        int ServerPort = defaultport;
        try {
            ServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong  Port Number" + e.getMessage());
            System.err.println("Using default port " + defaultport);
        }
        System.out.println("Starting the server on the port " + ServerPort);
        AbstractServer server = new RPCConcurentServer(ServerPort, ServiceImplementation);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }

}
