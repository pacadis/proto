package repos.repositories;

import models.Participant;
import models.ParticipantCursaDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repos.IParticipantRepository;

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

public class ParticipantRepository implements IParticipantRepository {
    private JdbcUtils utils;
    //private static final Logger logger= LogManager.getLogger();
    private static final Logger logger = LogManager.getLogger(ParticipantRepository.class);


    public ParticipantRepository(Properties props){
        logger.info("Initializig ParticipantRepository with proprieties: {} ",props);
        utils=new JdbcUtils(props);
    }


    @Override
    public int size() {
        logger.traceEntry();
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Participanti")){
            try(ResultSet rez=preStmt.executeQuery()){
                if(rez.next()){
                    logger.traceExit(rez.getInt("SIZE"));
                    return rez.getInt("SIZE");
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
    return 0;
    }

    @Override
    public void save(Participant entity) {
        logger.traceEntry("Se salveaza participantul {} ",entity);
        Connection con =utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Participanti values (?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setInt(3,entity.getIdEchipa());
            int rez=preStmt.executeUpdate();
        }catch (SQLException e){
            logger.error(e);
        }
        logger.info("S-a salvat participantul {}"+entity);
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Se sterge participantul cu id-ul {}"+integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Participanti where id=?")){
            preStmt.setInt(1,integer);
            int rez=preStmt.executeUpdate();
        }catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Participant entity) {
        logger.traceEntry("Se actualizeaza participantul cu id-ul {}"+integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Participanti set id=?,nume=?,idEchipa=? where id=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setInt(3,entity.getIdEchipa());
            preStmt.setInt(4,integer);
        }catch (SQLException e){
            logger.error(e);
        }
        logger.traceExit();
    }

    @Override
    public Participant findOne(Integer integer) {
        logger.traceEntry("se cauta participantul cu id-ul {}", integer);

        Connection con = utils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Participanti where id=?")) {
            preStmt.setInt(1, integer);
            try (ResultSet rez = preStmt.executeQuery()) {
                if (rez.next()) {
                    Integer id = rez.getInt("id");
                    String nume = rez.getString("nume");
                    Integer idEchipa = rez.getInt("idEchipa");
                    Participant p = new Participant(id, nume, idEchipa);
                    logger.traceExit();
                    return p;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    public Iterable<Participant> findAll() {
        Connection con=utils.getConnection();
        List<Participant> participanti=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Participanti")){
            try(ResultSet rez=preStmt.executeQuery()){
                while(rez.next()){
                    int id=rez.getInt("id");
                    String nume=rez.getString("nume");
                    int idEchipa=rez.getInt("idEchipa");
                    Participant p=new Participant(id,nume,idEchipa);
                    participanti.add(p);
                }
            }
        }catch (SQLException e){
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(participanti);
        return participanti;
    }

    //filtreaza participantii dupa numele echipei din care fac parte
    public Iterable<ParticipantCursaDTO> findByTeam(String team){
        Connection connection=utils.getConnection();
        List<ParticipantCursaDTO> participanti=new ArrayList<>();
        try(PreparedStatement preStmt=connection.prepareStatement("select p.id,p.nume,c.capacitateMotor from Participanti p inner join Echipa e on p.idEchipa = E.id inner join Inscriere I on p.id = I.idParticipant inner join Cursa C on I.idCursa = C.id where e.nume=?")){
            preStmt.setString(1,team);
            try(ResultSet resultSet=preStmt.executeQuery()) {
                while (resultSet.next()) {
                    int id=resultSet.getInt("id");
                    String nume = resultSet.getString("nume");
                    int capMotor = resultSet.getInt("capacitateMotor");
                    ParticipantCursaDTO pcDto = new ParticipantCursaDTO(id,nume, capMotor);
                    participanti.add(pcDto);
                }
            }
        }catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error db"+ex);
            //ex.printStackTrace();
            }
        logger.traceExit(participanti);
        return participanti;
    }

    public int getNewIdParticipant() {
        Connection connection = utils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) AS maxim FROM Participanti")) {
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

}
