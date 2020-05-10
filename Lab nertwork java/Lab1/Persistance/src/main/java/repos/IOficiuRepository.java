package repos;


import models.Oficiu;

public interface IOficiuRepository extends IRepository<Integer, Oficiu> {
    boolean localLogin(String user,String parola);
}
