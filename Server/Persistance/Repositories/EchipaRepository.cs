using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using Persistance.DBUtils;
using Problem11.Model;

namespace Problem11.Repositories
{
    public class EchipaRepository : IEchipaRepository
    {

        public static readonly ILog logger = LogManager.GetLogger("EchipaRepository");

        public EchipaRepository()
        {
            logger.Info("Se creeaza Repository Echipa");
        }

        public List<DTOBJPart> cautare(string numeEchipa)
        {
            logger.InfoFormat("Se cauta toti participantii care apartin echipei {0}", numeEchipa);

            List<DTOBJPart> rezultat = new List<DTOBJPart>();

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "select P.id,P.nume,C.capacitateMotor from Cursa C INNER JOIN Inscriere I on C.id = I.idCursa INNER JOIN Participanti P on I.idParticipant = P.id INNER JOIN Echipa E on P.idEchipa = E.id WHERE E.nume=@name";

                IDbDataParameter name = com.CreateParameter();
                name.ParameterName = "@name";
                name.Value = numeEchipa;
                com.Parameters.Add(name);


                using (var Data = com.ExecuteReader())
                {
                    while (Data.Read())
                    {
                        int id = Data.GetInt32(0);
                        String nume = Data.GetString(1);
                        int capacitate = Data.GetInt32(2);
                        
                        DTOBJPart obiect = new DTOBJPart(id,nume, capacitate);
                        rezultat.Add(obiect);

                    }
                }
            }
            return rezultat;
        }

        public void delete(int id)
        {
            logger.InfoFormat("Se sterge echipa cu id-ul {0}", id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "delete from Echipa where id=@id";

                IDbDataParameter paramId = com.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                com.Parameters.Add(paramId);
                var result = com.ExecuteNonQuery();
                if (result == 0)
                {
                    logger.Info("Eroare incercand sa se stearga echipa cu id-ul");
                    throw new Exception("Eroare la stergere");
                }
                logger.InfoFormat("S-a sters echipa cu id-ul {0}", id);
            }
        }

        public int FindIdByName(string nume)
        {
            logger.InfoFormat("Se cauta id-ul echipei {0}", nume);

            IDbConnection conn = DBUtils.getConnection();

            using(var com = conn.CreateCommand())
            {
                com.CommandText = "select id from Echipa where nume=@name";

                IDbDataParameter name = com.CreateParameter();
                name.ParameterName = "@name";
                name.Value = nume;
                com.Parameters.Add(name);

                using(var Data = com.ExecuteReader())
                {
                    if (Data.Read())
                    {
                        int id = Data.GetInt32(0);
                        return id;
                    }
                }
            }
            logger.Info("Nu s-a gasit Echipa cu numele cerut");
            return 0;
        }

        public Echipa findOne(int id)
        {
            logger.InfoFormat("Se cauta echipa cu id-ul {0}", id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "select id,nume from Echipa where id=@id";
                IDbDataParameter paramId = com.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                com.Parameters.Add(paramId);

                using (var Data = com.ExecuteReader())
                {
                    if (Data.Read())
                    {
                        int idEchipa = Data.GetInt32(0);
                        string nume = Data.GetString(1);

                        Echipa E = new Echipa(idEchipa, nume);
                        logger.InfoFormat("S-a gasit echipa cu id-ul {0}", E.Id);
                        return E;
                    }
                }
            }
            logger.InfoFormat("Nu s a gasit echipa cu id ul {0}", id);
            return null;
        }

        public void save(Echipa entity)
        {
            logger.InfoFormat("Se salveaza echipa cu id-il {0}", entity.Id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "insert into Echipa values (@idEchipa,@nume)";

                IDbDataParameter paramIdEchipa = com.CreateParameter();
                paramIdEchipa.ParameterName = "@idEchipa";
                paramIdEchipa.Value = entity.Id;
                com.Parameters.Add(paramIdEchipa);

                IDbDataParameter paramNume = com.CreateParameter();
                paramNume.ParameterName = "@nume";
                paramNume.Value = entity.Nume;
                com.Parameters.Add(paramNume);


                var result = com.ExecuteNonQuery();
                if (result == 0)
                {
                    logger.Info("Error while adding");
                    throw new Exception("Nici o echipa adaugata!");
                }

            }
            logger.InfoFormat("A fost adaugata echipa cu id-ul {0}", entity.Id);
        }

        
    }
}
