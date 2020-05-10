package client.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.DTOAngajat;
import services.IServices;
import services.ServerException;


public class LoginController {
    private IServices server;
    private MainController mainCtrl;
    private DTOAngajat crtAngajat;
    Parent mainParent;

    @FXML
    TextField tfUser;
    @FXML
    PasswordField tfParola;
    @FXML
    Button buttonLogin;

    public void setServer(IServices server1){
        this.server = server1;
    }

    public void setMainParent(Parent parent1){
        this.mainParent = parent1;
    }

    public void setMainCtrl(MainController ctrl){
        this.mainCtrl = ctrl;
    }

    public void pressCancel(ActionEvent event){
        System.exit(0);
    }

    public void setCrtAngajat(DTOAngajat crtAngajat) {
        this.crtAngajat = crtAngajat;
    }

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        String user = tfUser.getText();
        String parola = tfParola.getText();
        crtAngajat = new DTOAngajat(user, parola);
        try {
            server.login(crtAngajat,mainCtrl);
            Stage stage = new Stage();
            stage.setTitle("Main Window for: "+crtAngajat.getUsername());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainCtrl.logout();
                    System.exit(0);
                }
            });
            stage.show();
            mainCtrl.setUser(crtAngajat);
            //mainCtrl.setLoggedEmployess();
            mainCtrl.initialiazeTabels();
            mainCtrl.setCurseTabel();
            mainCtrl.setComboBox();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        } catch (ServerException e) {
            Alert msg = new Alert(Alert.AlertType.ERROR);
            msg.setHeaderText("Login Failed!!!");
            msg.setContentText("Wrong username or password!!!");
            msg.showAndWait();

            //JOptionPane.showMessageDialog(null, "Login failed. Wrong username or password!");
            //return;
        }

    }

}
