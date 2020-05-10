package services;

import models.*;

public interface IObserver {
    //void AngajatLoggedIn(DTOAngajat angajat) throws ServerException;
    //void AngajatLoggedOut(DTOAngajat angajat) throws ServerException;
    void AngajatSubmitted(CursaDTO[] curse) throws ServerException;
}
