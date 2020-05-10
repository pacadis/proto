package repos.repositories;

import models.Echipa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repos.IEchipaRepository;

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

public class EchipaRepository implements IEchipaRepository {
    private JdbcUtils utils;
    //private static final Logger logger= LogManager.getLogger();
    private static final Logger logger = LogManager.getLogger(EchipaRepository.class);

    public EchipaRepository(Properties props){
        logger.info("Initializing EchipaRepositpry with properties: {} ",props);
        utils=new JdbcUtils(props);
    }


    @Override
    public int size() {
        logger.traceEntry();
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Echipa")) {
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
    public void save(Echipa entity) {
        logger.traceEntry("se salveaza echipa {} ",entity);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Echipa values (?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            int rez=preStmt.executeUpdate();

        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.info("s a salvat echipa {} "+entity);
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("se sterge echipa cu id-ul {}",integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Echipa where id=?")){
            preStmt.setInt(1,integer);
            int rez=preStmt.executeUpdate();
        }
        catch (SQLException ex){
            logger.error(ex);
        }

        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Echipa entity) {
        logger.traceEntry("se actualizeaza echipa cu id-ul {}",integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Echipa set id=?,name=? where id=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setInt(3,integer);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Echipa findOne(Integer integer) {
        logger.traceEntry("se cauta echipa cu id-ul {}",integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Echipa where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet rez=preStmt.executeQuery()){
                if (rez.next()){
                    Integer id=rez.getInt("id") ;
                    String nume=rez.getString("nume");
                    Echipa E=new Echipa(id,nume);
                    logger.traceExit(E);
                    return E;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
        }
        return null;
    }

    @Override
    public Iterable<Echipa> findAll() {
        Connection con=utils.getConnection();
        List<Echipa> echipe=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Echipa")) {
            try(ResultSet rez=preStmt.executeQuery()) {
                while (rez.next()) {
                    Integer id=rez.getInt("id") ;
                    String nume=rez.getString("nume");
                    Echipa e=new Echipa(id,nume);
                    echipe.add(e);
                }
            }
        } catch (SQLException e) {
            logger.error(e);

            System.out.println("Error DB "+e);
        }
        logger.traceExit(echipe);
        return echipe;
    }

    public int findIdEchipa(String echipa){
        Connection connection=utils.getConnection();
        try(PreparedStatement preStmt=connection.prepareStatement("select e.id from Echipa e where e.nume=?")){
            preStmt.setString(1,echipa);
            try(ResultSet rez=preStmt.executeQuery()) {
                if (rez.next()) {
                    int id = rez.getInt("id");
                    //int idEchipa = id;
                    logger.traceExit(id);
                    return id;
                }
            }
        }catch (SQLException e){
            //e.printStackTrace();
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        return 0;

    }
}
