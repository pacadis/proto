package services;
import models.*;

public interface IServices {
    void login(DTOAngajat angajat, IObserver client) throws ServerException;
    void logout(DTOAngajat angajat, IObserver client) throws ServerException;
    void submitInscriere(DTOInfoSubmit infoSubmit) throws ServerException;
    //Oficiu[] getLoggedEmployees() throws ServerException;
    CursaDTO[] getCurseDisp() throws ServerException;
    ParticipantCursaDTO[] searchByTeam(String team) throws ServerException;

}
