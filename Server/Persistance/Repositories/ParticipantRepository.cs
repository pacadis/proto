using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using Persistance.DBUtils;
using Problem11.Model;

namespace Problem11.Repositories
{
    public class ParticipantRepository : IParticipantRepository
    {
        public static readonly ILog logger = LogManager.GetLogger("ParticipantRepository");


        public ParticipantRepository()
        {
            logger.Info("Se creeaza Repository Participanti");
        }

        public void delete(int id)
        {
            logger.InfoFormat("Se sterge participantul cu id-ul {0}", id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "delete from Participanti where id=@id";

                IDbDataParameter paramId = com.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                com.Parameters.Add(paramId);
                var result = com.ExecuteNonQuery();
                if (result == 0)
                {
                    logger.Info("Eroare incercand sa se stearga participantul cu id-ul");
                    throw new Exception("Eroare la stergere");
                }
                logger.InfoFormat("S-a sters participantul cu id-ul {0}", id);
            }
        }

        public List<Participant> findAll()
        {
            IDbConnection connection = DBUtils.getConnection();
            List<Participant> participantList = new List<Participant>();

            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select id, nume, idEchipa from Participanti";

                using (var result = command.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int id = result.GetInt32(0);
                        string nume = result.GetString(1);
                        int idEchipa = result.GetInt32(2);
                        Participant participant = new Participant(id, nume, idEchipa);
                        participantList.Add(participant);
                    }
                }
            }

            return participantList;
        }

        public int FindMaxId()
        {

            List<int> lista = new List<int>();
            foreach (Participant p in findAll())
            {
                lista.Add(p.Id);
            }

            int max = 0;
            foreach (int s in lista)
            {

                int id = s;
                if (id > max)
                {
                    max = id;

                }

            }
            int maximBun = max + 1;

            return maximBun;
        }

        public Participant findOne(int id)
        {
            logger.InfoFormat("Se cauta participantul cu id-ul {0}", id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "select id,nume,idEchipa from Participanti where id=@id";
                IDbDataParameter paramId = com.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                com.Parameters.Add(paramId);

                using (var Data = com.ExecuteReader())
                {
                    if (Data.Read())
                    {
                        int idParticipant = Data.GetInt32(0);
                        string nume = Data.GetString(1);
                        int idEchipa = Data.GetInt32(2);

                        Participant P = new Participant(idParticipant, nume, idEchipa);
                        logger.InfoFormat("S-a gasit participantul cu id-ul {0}", P.Id);
                        return P;
                    }
                }
            }
            logger.InfoFormat("Nu s a gasit participantul cu id ul {0}", id);
            return null;
        }

        public void save(Participant entity)
        {
            logger.InfoFormat("Se salveaza participantul cu id-il {0}", entity.Id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "insert into Participanti values (@idParticipant,@nume,@idEchipa)";

                IDbDataParameter paramIdParticipant = com.CreateParameter();
                paramIdParticipant.ParameterName = "@idParticipant";
                paramIdParticipant.Value = entity.Id;
                com.Parameters.Add(paramIdParticipant);

                IDbDataParameter paramNume = com.CreateParameter();
                paramNume.ParameterName = "@nume";
                paramNume.Value = entity.Nume;
                com.Parameters.Add(paramNume);

                IDbDataParameter paramidEchipa = com.CreateParameter();
                paramidEchipa.ParameterName = "@idEchipa";
                paramidEchipa.Value = entity.IdEchipa;
                com.Parameters.Add(paramidEchipa);

                var result = com.ExecuteNonQuery();
                if (result == 0)
                {
                    logger.Info("Error while adding");
                    throw new Exception("Nici un participant adaugat!");
                }

            }
            logger.InfoFormat("A fost adaugata participantul cu id-ul {0}", entity.Id);
        }

        
    }
}
