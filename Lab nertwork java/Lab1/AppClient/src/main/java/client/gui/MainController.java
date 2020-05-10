package client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.CursaDTO;
import models.DTOAngajat;
import models.DTOInfoSubmit;
import models.ParticipantCursaDTO;
import services.IObserver;
import services.IServices;
import services.ServerException;
import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable, IObserver {
    private IServices server;
    private DTOAngajat crtAngajat;
    private ObservableList<CursaDTO> modelCursa= FXCollections.observableArrayList();
    private ObservableList<ParticipantCursaDTO> modelParticipant=FXCollections.observableArrayList();
    ObservableList<DTOAngajat> others=FXCollections.observableArrayList();

    @FXML
    ComboBox<Integer> cbCapacitateMotor;
    @FXML
    TableView<CursaDTO> tabelCurse;
    @FXML
    TableColumn<CursaDTO,Integer> columnIdCurse;
    @FXML
    TableColumn<CursaDTO,Integer> columnCapacitateMotorCurse;
    @FXML
    TableColumn<CursaDTO,Integer> columnNrParticipantiCurse;
    @FXML
    TableView<ParticipantCursaDTO> tabelParticipanti;
    @FXML
    TableColumn<ParticipantCursaDTO,Integer> columnIdParticipanti;
    @FXML
    TableColumn<ParticipantCursaDTO,String> columnNumeParticipanti;
    @FXML
    TableColumn<ParticipantCursaDTO,Integer> columnCapacitateMotorParticipanti;
    @FXML
    TextField tfCautaParticipant;
    @FXML
    TextField tfNume;
    @FXML
    TextField tfEchipa;
    @FXML
    Button buttonClear;
    @FXML
    Button buttonLogout;
    @FXML
    Button buttonSave;
    @FXML
    Button buttonSearch;
    @FXML
    Button buttonClearSearch;

    public void setServer(IServices server1){
        this.server=server1;
    }

    public void setUser(DTOAngajat crtAngajat){
        this.crtAngajat=crtAngajat;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
    public void initialiazeTabels(){
        columnIdCurse.setCellValueFactory(new PropertyValueFactory<CursaDTO,Integer>("id"));
        columnCapacitateMotorCurse.setCellValueFactory(new PropertyValueFactory<CursaDTO,Integer>("capMotor"));
        columnNrParticipantiCurse.setCellValueFactory(new PropertyValueFactory<CursaDTO,Integer>("nrParticipanti"));
        //tabelCurse.setItems(modelCursa);

        columnIdParticipanti.setCellValueFactory(new PropertyValueFactory<ParticipantCursaDTO,Integer>("id"));
        columnNumeParticipanti.setCellValueFactory(new PropertyValueFactory<ParticipantCursaDTO,String>("numeParticipant"));
        columnCapacitateMotorParticipanti.setCellValueFactory(new PropertyValueFactory<ParticipantCursaDTO,Integer>("capCursa"));
        tabelParticipanti.setItems(modelParticipant);
    }

    public void setCurseTabel() {


        try{
            this.tabelCurse.getItems().clear();
            CursaDTO[] curse=this.server.getCurseDisp();
            for(CursaDTO c:curse){
                modelCursa.add(c);
            }
            tabelCurse.setItems(modelCursa);
        }catch (ServerException e){
            System.out.println("Error when setting CurseTabel"+e);
        }
    }

    public void setComboBox() {
        ObservableList<Integer> tipuriCurse=FXCollections.observableArrayList();
        for(CursaDTO c : this.modelCursa){
            tipuriCurse.add(c.getCapMotor());
        }
        cbCapacitateMotor.setItems(tipuriCurse);
    }

//    @Override
//    public void AngajatLoggedIn(DTOAngajat employee) throws ServerException {
//        Platform.runLater(() -> {
//            others.add(employee);
//            //OthersEmp.setItems(others);
//            System.out.println("Employee logged in"+employee);
//            System.out.println(others.size());
//        });
//
//    }
//
//    @Override
//    public void AngajatLoggedOut(DTOAngajat employee) throws ServerException {
//        Platform.runLater(() -> {
//            others.remove(employee);
//            //OthersEmp.setItems(others);
//            System.out.println("Employee logged out"+employee);
//        });
//
//    }

    public void AngajatSubmitted(CursaDTO[] result) throws ServerException {
        Platform.runLater(() -> {
            System.out.println("S-a apelat AngajatSubmitted din MainCtrl");
            AddNewDataCurse(result);
            //setCurseTabel();
        });
    }

    public void AddNewDataCurse(CursaDTO [] source){
        this.modelCursa.clear();
        for(CursaDTO c:source){
            this.modelCursa.add(c);
        }
        this.tabelCurse.setItems(this.modelCursa);
    }

    @FXML
    public void handleClear() {
        tfNume.setText("");
        tfEchipa.setText("");
    }

    @FXML
    public void handleClearSearch(){
        tfCautaParticipant.setText("");
        tabelParticipanti.getItems().clear();
    }

    @FXML
    public void handleSearch(){
        try{
            this.tabelParticipanti.getItems().clear();
            String team=tfCautaParticipant.getText();
            ParticipantCursaDTO[] result=this.server.searchByTeam(team);
            for(ParticipantCursaDTO part : result){
                modelParticipant.add(part);
            }
            this.tabelParticipanti.setItems(modelParticipant);
        }catch (ServerException e){
            System.out.println("Error when searching "+e);
        }
    }

    @FXML
    public void handleSubmit(){
        System.out.println("Se apeleaza Inscriere");
        try{
            System.out.println("se apeleaza Inscriere");
            String numePart=tfNume.getText();
            String numeEchipa=tfEchipa.getText();
            int capacitate= cbCapacitateMotor.getValue();
            System.out.println(numePart+" "+numeEchipa+" "+capacitate);
            DTOInfoSubmit submit=new DTOInfoSubmit(crtAngajat,capacitate,numePart,numeEchipa);
            this.server.submitInscriere(submit);
        }catch (ServerException e){
            System.out.println("Error when submitting from MainCtrl"+e);
        }
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent){
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


    }

    public void logout() {
        try {
            server.logout(crtAngajat, this);
        } catch (ServerException e) {
            System.out.println("Logout error " + e);
        }
    }

////////////////


/*

    public void initialize(){
        columnIdCurse.setCellValueFactory(new PropertyValueFactory<CursaDTO,Integer>("id"));
        columnCapacitateMotorCurse.setCellValueFactory(new PropertyValueFactory<CursaDTO,Integer>("capMotor"));
        columnNrParticipantiCurse.setCellValueFactory(new PropertyValueFactory<CursaDTO,Integer>("nrParticipanti"));
        tabelCurse.setItems(modelCursa);

        columnIdParticipanti.setCellValueFactory(new PropertyValueFactory<ParticipantCursaDTO,Integer>("id"));
        columnNumeParticipanti.setCellValueFactory(new PropertyValueFactory<ParticipantCursaDTO,String>("numeParticipant"));
        columnCapacitateMotorParticipanti.setCellValueFactory(new PropertyValueFactory<ParticipantCursaDTO,Integer>("capCursa"));
        tabelParticipanti.setItems(modelParticipant);

    }

    public void initModel(){

        Iterable<CursaDTO> curseDto=service.findlAllCursaDto();
        List<CursaDTO> listCurse=
                StreamSupport.stream(curseDto.spliterator(),false)
                .collect(Collectors.toList());
        modelCursa.setAll(listCurse);


        ObservableList<Integer> tipuriCursa=FXCollections.observableArrayList();
        //List<Cursa> curse= (List<Cursa>) service.findAllCursa();
        for (Cursa c:service.findAllCursa()){
            tipuriCursa.add(c.getCapacitateMotor());
        }
        cbCapacitateMotor.getSelectionModel().selectFirst();
        cbCapacitateMotor.setItems(tipuriCursa);

    }

    public void setService(Service service,Stage stage){
        this.service=service;
        this.stage=stage;
        initModel();

    }





    public void handleSave(){
        try {
            String nume = tfNume.getText();
            String echipa = tfEchipa.getText();
            Integer capacitate = Integer.valueOf(cbCapacitateMotor.getValue());
            service.addInscriereParticipant(nume, echipa, capacitate);
            modelCursa.setAll((List<CursaDTO>) StreamSupport.stream(service.findlAllCursaDto().spliterator(), false).collect(Collectors.toList()));

            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.setContentText("Participant inscris cu succes!");
            message.showAndWait();
        }catch (Exception e) {
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setHeaderText("Eroare insciere");
            message.setContentText("Participantul e inscris deja.");
            message.showAndWait();
        }
    }

    public void handleLogout(){
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/loginView.fxml"));
            AnchorPane root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("LOGIN");
            loginStage.initModality(Modality.WINDOW_MODAL);
            Scene loginScene = new Scene(root);
            loginStage.setScene(loginScene);
            LoginController logCtrl = loader.getController();
            logCtrl.setService(getService(), loginStage);
            loginStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    static Service getService(){
        ApplicationContext context=new ClassPathXmlApplicationContext("BeanXML.xml");
        Service service=context.getBean(Service.class);
        return service;
    }



 */

}
