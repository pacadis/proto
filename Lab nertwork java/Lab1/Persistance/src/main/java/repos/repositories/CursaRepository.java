package repos.repositories;

import models.Cursa;
import models.CursaDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repos.ICursaRepository;

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
//import java.util.logging.Logger;

public class CursaRepository implements ICursaRepository {
    private JdbcUtils utils;
    //private static final Logger logger= LogManager.getLogger();
    private static final Logger logger = LogManager.getLogger(CursaRepository.class);


    public CursaRepository(Properties props){
        logger.info("Initializig CursaRepository with proprieties: {} ",props);
        utils=new JdbcUtils(props);
    }


    @Override
    public int size() {
        logger.traceEntry();

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from Cursa")) {
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
    public void save(Cursa entity) {
        logger.traceEntry("Se salveaza Cursa  {} ",entity);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into Cursa values (?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getCapacitateMotor());
            int rez=preStmt.executeUpdate();

        }catch(SQLException ex){
            logger.error(ex);
        }
        logger.info("S a salvat cursa {} "+entity);
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Se sterge cursa cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from Cursa where id=?")){
            preStmt.setInt(1,integer);
            int rez=preStmt.executeUpdate();
        }
        catch (SQLException ex){
            logger.error(ex);
        }

        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Cursa entity) {
        logger.traceEntry("se actualizeaza cursa cu id-ul {}",integer);

        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("update Cursa set id=?,capacitateMotor=? where idCursa=?")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getCapacitateMotor());
            preStmt.setInt(3,integer);
        }catch (SQLException ex){
            logger.error(ex);
        }
        logger.traceExit();
    }

    @Override
    public Cursa findOne(Integer integer) {
        logger.traceEntry("se cauta cursa cu id-ul {}",integer);
        Connection con=utils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cursa where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet rez=preStmt.executeQuery()){
                if (rez.next()){
                    Integer id,capac;
                    id=rez.getInt("id") ;
                    capac=rez.getInt("capacitateMotor");
                    Cursa c=new Cursa(id,capac);
                    logger.traceExit(c);
                    return c;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
        }

        return null;
    }

    @Override
    public Iterable<Cursa> findAll() {
        Connection con=utils.getConnection();
        List<Cursa> curse=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from Cursa")) {
            try(ResultSet rez=preStmt.executeQuery()) {
                while (rez.next()) {
                    int id = rez.getInt("id");
                    Integer cap;
                    cap=rez.getInt("capacitateMotor");
                    Cursa P=new Cursa(id,cap);
                    curse.add(P);
                }
            }
        } catch (SQLException e) {
            logger.error(e);

            System.out.println("Error DB "+e);
        }
        logger.traceExit(curse);

        return curse;
    }

    public int findIdCursa(int cap){
        Connection connection=utils.getConnection();
        try(PreparedStatement preStmt=connection.prepareStatement("select c.id from Cursa c where capacitateMotor=?")){
            preStmt.setInt(1,cap);
            try(ResultSet rez=preStmt.executeQuery()) {
                if (rez.next()) {
                    int id = rez.getInt("id");
                    //logger.traceExit(id);
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


    public Iterable<CursaDTO> findCursaNrParticipanti(){
        Connection connection=utils.getConnection();
        List<CursaDTO> curse=new ArrayList<>();
        try(PreparedStatement preStmt=connection.prepareStatement("select C.id,C.capacitateMotor,count(P.id) as NrInscrisi from Cursa C left join Inscriere I on C.id = I.idCursa left join Participanti P on I.idParticipant = P.id group by C.capacitateMotor")){
            ResultSet rez=preStmt.executeQuery();
            while(rez.next()){
                int id=rez.getInt("id");
                int cap=rez.getInt("capacitateMotor");
                int nr=rez.getInt("NrInscrisi");
                CursaDTO c=new CursaDTO(id,cap,nr);
                curse.add(c);
            }
        }catch (SQLException e){
            //e.printStackTrace();
            logger.error(e);

            System.out.println("Error DB "+e);
        }
        logger.traceExit(curse);
        return curse;

    }

}
