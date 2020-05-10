package client;

import RPCProtocol.ClientServicesProxy;
import client.gui.LoginController;
import client.gui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {
    private static int defaultChatPort = 55556;
    private static String defaultServer = "127.0.0.1";

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/Client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find Client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IServices server = new ClientServicesProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginView.fxml"));
        Parent root=loader.load();

        LoginController ctrl = loader.<LoginController>getController();
        ctrl.setServer(server);

        FXMLLoader cloader = new FXMLLoader();
        cloader.setLocation(getClass().getResource("/view/mainView.fxml"));
        Parent croot=cloader.load();

        MainController mainCtrl = cloader.<MainController>getController();
        mainCtrl.setServer(server);

        ctrl.setMainCtrl(mainCtrl);
        ctrl.setMainParent(croot);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
