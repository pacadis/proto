using System;
using System.Collections.Generic;
using System.Data;
using log4net;
using Persistance.DBUtils;

namespace Problem11.Repositories
{
    public class AngajatRepository : IAngajatRepository
    {
        public static readonly ILog logger = LogManager.GetLogger("OficiuRepository");

        public AngajatRepository()
        {
            logger.Info("Creating the OficiuRepository");
        }

        public void Save(Angajat entity)
        {
            logger.InfoFormat("Se salveaza angajatul cu id-ul {0}", entity.Id);
            IDbConnection con = DBUtils.getConnection();

            using (var com = con.CreateCommand())
            {
                com.CommandText = "insert into Oficiu values (@id,@username,@parola)";

                IDbDataParameter paramId = com.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = entity.Id;
                com.Parameters.Add(paramId);

                IDbDataParameter paramUser = com.CreateParameter();
                paramUser.ParameterName = "@username";
                paramUser.Value = entity.User;
                com.Parameters.Add(paramUser);

                IDbDataParameter paramParola = com.CreateParameter();
                paramParola.ParameterName = "@parola";
                paramParola.Value = entity.Pass;
                com.Parameters.Add(paramParola);

                var result = com.ExecuteNonQuery();
                if(result==0)
                {
                    logger.Info("Eroare la adaugare");
                    throw new Exception("Niciun angajat adaugat!");
                }
            }
            logger.InfoFormat("A fost adaugat angajatul cu id-ul {0}", entity.Id);
        }

        public void Delete(int id)
        {
            logger.InfoFormat("Se sterge angajatul cu id-ul {0}", id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "delete from Oficiu where id=@id";

                IDbDataParameter paramId = com.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                com.Parameters.Add(paramId);
                var result = com.ExecuteNonQuery();
                if (result == 0)
                {
                    logger.Info("Eroare incercand sa se stearga angajatul cu id-ul");
                    throw new Exception("Eroare la stergere");
                }
                logger.InfoFormat("S-a sters angajatul cu id-ul {0}", id);
            }
        }
        
        public List<Angajat> findAll()
        {
            logger.Info("Se cauta toti angajatii");
            List<Angajat> result = new List<Angajat>();
            IDbConnection conn = DBUtils.getConnection();
            using(var com = conn.CreateCommand())
            {
                com.CommandText = "select id,user,parola from Oficiu";
                using (var Data = com.ExecuteReader())
                {
                    while (Data.Read())
                    {
                        int idAngajat = Data.GetInt32(0);
                        string user = Data.GetString(1);
                        string pass = Data.GetString(2);
                        Angajat Ang = new Angajat(idAngajat, user, pass);
                        result.Add(Ang);
                    }
                }
            }
            return result;
        }

        public Angajat FindOne(int id)
        {
            logger.InfoFormat("Se cauta angajatul cu id-ul {0}", id);

            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "select id,user,parola from Oficiu where id=@id";
                IDbDataParameter paramId = com.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                com.Parameters.Add(paramId);

                using (var Data = com.ExecuteReader())
                {
                    if (Data.Read())
                    {
                        int idAngajat = Data.GetInt32(0);
                        string user = Data.GetString(1);
                        string pass = Data.GetString(2);
                        Angajat ang = new Angajat(idAngajat, user, pass);
                        logger.InfoFormat("S-a gasit angajatul");
                        return ang;
                    }
                }
            }
            logger.InfoFormat("Nu s a gasit angajatul cu id ul {0}", id);
            return null;
        }

        public bool LocalLogin(string username,string password)
        {
            logger.InfoFormat("Se verifica daca se poate efectua logarea angajatului {0}", username);
            IDbConnection conn = DBUtils.getConnection();

            using (var com = conn.CreateCommand())
            {
                com.CommandText = "select id from Oficiu where user=@username and parola=@parola";
                IDbDataParameter paramUser = com.CreateParameter();
                paramUser.ParameterName = "@username";
                paramUser.Value = username;
                com.Parameters.Add(paramUser);

                IDbDataParameter paramParola = com.CreateParameter();
                paramParola.ParameterName = "@parola";
                paramParola.Value = password;
                com.Parameters.Add(paramParola);

                using (var data = com.ExecuteReader())
                {
                    if(data.Read())
                    {
                        return true;
                    }
                }
            }
            return false;
        }

        public Angajat findOne(int id)
        {
            throw new NotImplementedException();
        }

        public void save(Angajat entity)
        {
            throw new NotImplementedException();
        }

        public void delete(int id)
        {
            throw new NotImplementedException();
        }
    }
}
