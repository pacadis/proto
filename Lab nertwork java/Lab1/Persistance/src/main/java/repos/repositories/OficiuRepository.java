package repos.repositories;

import models.DTOAngajat;
import models.Oficiu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repos.IOficiuRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OficiuRepository implements IOficiuRepository {
    private JdbcUtils utils;
    //private static final Logger logger= LogManager.getLogger();
    private static final Logger logger = LogManager.getLogger(OficiuRepository.class);

    public OficiuRepository(Properties props){
        logger.info("Initializing PersoaneREPO with properties: {} ",props);
        utils=new JdbcUtils(props);
    }


    @Override
    public int size() {
        logger.traceEntry();
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Oficiu ")) {
            try(ResultSet rez = preStmt.executeQuery()) {
                if (rez.next()) {
                    logger.traceExit(rez.getInt("SIZE"));
                    return rez.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }

    @Override
    public void save(Oficiu entity) {
        logger.traceEntry("se salveaza angajatul  {} ",entity);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Oficiu values (?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getUser());
            preStmt.setString(3,entity.getParola());
            int rez=preStmt.executeUpdate();

        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.info("s a salvat angajatul {} "+entity);
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("se sterge angajatul cu id-ul {}",integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Oficiu where id=?")){
            preStmt.setInt(1,integer);
            int rez=preStmt.executeUpdate();
        }
        catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Oficiu entity) {
        logger.traceEntry("se updateaza angajatul cu id-ul {}",integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Oficiu set id=?,user=?,parola=?  where id=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getUser());
            preStmt.setString(3,entity.getParola());
            preStmt.setInt(4,integer);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Oficiu findOne(Integer integer) {
        logger.traceEntry("se cauta angajatul cu id-ul {}",integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Oficiu where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet rez=preStmt.executeQuery()){
                if (rez.next()){
                    Integer id=rez.getInt("id");
                    String username,password;
                    username=rez.getString("user");
                    password=rez.getString("parola");
                    Oficiu a=new Oficiu(id,username,password);
                    logger.traceExit(a);
                    return a;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
        }
        return null;
    }

    @Override
    public Iterable<Oficiu> findAll() {
        Connection con=utils.getConnection();
        List<Oficiu> angajati=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Oficiu")) {
            try(ResultSet rez=preStmt.executeQuery()) {
                while (rez.next()) {
                    int id = rez.getInt("id");
                    String username,password;
                    username=rez.getString("user");
                    password=rez.getString("parola");
                    Oficiu a=new Oficiu(id,username,password);
                    angajati.add(a);
                }
            }
        } catch (SQLException e) {
            logger.error(e);

            System.out.println("Error DB "+e);
        }
        logger.traceExit(angajati);

        return angajati;
    }

    @Override
    public boolean localLogin(String username, String parola) {
        Connection con=utils.getConnection();
        logger.traceEntry("Se cauta angajatul cu username-ul cu {}",username);
        try(PreparedStatement preStmt=con.prepareStatement("select * from Oficiu where Oficiu.user=? and Oficiu.parola=?")){
            preStmt.setString(1,username);
            preStmt.setString(2,parola);
            try(ResultSet rez=preStmt.executeQuery()){
                if(rez.next()){
                    logger.traceExit("S-a gasit angajatul cautat!");
                    return true;
                }
            }
        }catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit("Nu s-a gasit angajatul cautat.");
        return false;
    }

    public Iterable<DTOAngajat> findOtherEmployees(DTOAngajat angajat){
        Connection con=utils.getConnection();
        List<DTOAngajat> result=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select username,password from Angajat where username!=? and password!=?")){
            preStmt.setString(1,angajat.getUsername());
            preStmt.setString(2,angajat.getPassword());
            try(ResultSet resultSet=preStmt.executeQuery()){
                while(resultSet.next()){
                    String user=resultSet.getString("username");
                    String pass=resultSet.getString("password");
                    DTOAngajat angajat1=new DTOAngajat(user,pass);
                    result.add(angajat1);
                }
            }
        }catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit(result);
        return result;
    }
}
