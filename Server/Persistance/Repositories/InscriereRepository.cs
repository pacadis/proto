using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using Persistance.DBUtils;
using Problem11.Model;

namespace Problem11.Repositories
{
    public class InscriereRepository : IInscriereRepository
    {
        public static readonly ILog logger = LogManager.GetLogger("InscriereRepository");


        public InscriereRepository()
        {
            logger.Info("Creating the InscriereRepository");
        }

        public void delete(int id)
        {
            throw new NotImplementedException();
        }

        public List<Inscriere> findAll()
        {
            IDbConnection connection = DBUtils.getConnection();
            List<Inscriere> inscriereList = new List<Inscriere>();

            using (var command = connection.CreateCommand())
            {
                command.CommandText = "select idInscriere, idParticipant, idCursa from Inscriere";

                using (var result = command.ExecuteReader())
                {
                    while (result.Read())
                    {
                        int idInscriere = result.GetInt32(0);
                        int idParticpant = result.GetInt32(2);
                        int idCursa = result.GetInt32(2);
                        Inscriere inscriere= new Inscriere(idInscriere, idParticpant, idCursa);
                        inscriereList.Add(inscriere);
                    }
                }
            }

            return inscriereList;
        }

        public int FindMaxId()
        {

            List<int> lista = new List<int>();
            foreach (Inscriere p in findAll())
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

        public Inscriere findOne(int id)
        {
            throw new NotImplementedException();
        }

        public void save(Inscriere entity)
        {
            logger.InfoFormat("Se salveaza Inscrierea cu id-il {0}", entity.Id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "insert into Inscriere values (@idInscriere,@idParticipant,@idCursa)";

                IDbDataParameter paramIdAngajat = com.CreateParameter();
                paramIdAngajat.ParameterName = "@idInscriere";
                paramIdAngajat.Value = entity.Id;
                com.Parameters.Add(paramIdAngajat);

                IDbDataParameter paramidPart = com.CreateParameter();
                paramidPart.ParameterName = "@idParticipant";
                paramidPart.Value = entity.IdPart;
                com.Parameters.Add(paramidPart);

                IDbDataParameter paramidCursa = com.CreateParameter();
                paramidCursa.ParameterName = "@idCursa";
                paramidCursa.Value = entity.IdCursa;
                com.Parameters.Add(paramidCursa);

                var result = com.ExecuteNonQuery();
                if (result == 0)
                {
                    logger.Info("Error while adding");
                    throw new Exception("Nici o inscriere adaugata!");
                }

            }
            logger.InfoFormat("A fost adaugat inscrierea cu id-ul {0}", entity.Id);
        }
    }
}
