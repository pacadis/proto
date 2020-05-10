package repos.repositories;

import models.Inscriere;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repos.IInscriereRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class InscriereRepository implements IInscriereRepository {
    private JdbcUtils utils;
    //private static final Logger logger= LogManager.getLogger();
    private static final Logger logger = LogManager.getLogger(InscriereRepository.class);

    public InscriereRepository(Properties props){
        logger.info("Initializing InscriereRepositpry with properties: {} ",props);
        utils=new JdbcUtils(props);
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Inscriere")) {
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
    public void save(Inscriere entity) {
        logger.traceEntry("se salveaza inscirerea {} ",entity);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Inscriere values (?,?,?)")){
            preStmt.setInt(1, entity.getId());
            preStmt.setInt(2,entity.getIdParticipant());
            preStmt.setInt(3,entity.getIdCursa());
            int rez=preStmt.executeUpdate();

        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.info("s a salvat insrierea {} "+entity);
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Inscriere entity) {

    }

    @Override
    public Inscriere findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Inscriere> findAll() {
        return null;
    }


    public int getNewIdInscriere() {
        Connection connection = utils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(idInscriere) AS maxim FROM Inscriere")) {
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("maxim");
                    //id = id + 1;
                    return id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Integer findIdInscriere(Integer idp,Integer idc){
        Connection connection=utils.getConnection();
        try(PreparedStatement preStmt=connection.prepareStatement("select i.idInscriere from Inscriere i where idParticipant=? and idCursa=?")){
            preStmt.setInt(1,idp);
            preStmt.setInt(2,idc);
            ResultSet rez=preStmt.executeQuery();
            while(rez.next()){
                int id=rez.getInt("id");
                int idInscriere=id;
                logger.traceExit(idInscriere);
                return idInscriere;
            }
        }catch (SQLException e){
            //e.printStackTrace();
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        return null;

    }

}
