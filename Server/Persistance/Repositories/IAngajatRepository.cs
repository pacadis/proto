using System;
namespace Problem11.Repositories
{
    interface IAngajatRepository : ICrudRepository<int, Angajat>
    {
        bool LocalLogin(String username, String password);
    }
}
