using Model.Model;
using Problem11.Model;
using Services;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Threading;
using Google.Protobuf;
using DTOAngajat = Model.Model.DTOAngajat;

namespace Networking.ProtocolBuffer
{
    class Worker : Observer
    {
        private IServices server;

        private TcpClient connection;

        private NetworkStream stream;

        private volatile bool connected;

        public Worker(IServices services, TcpClient conn)
        {
            this.server = services;
            this.connection = conn;
            try
            {
                stream = connection.GetStream();
                connected = true;
            } catch (ServerException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        public virtual void run()
        {
            while (connected)
            {
                try
                {
                    Protocol.Request request = Protocol.Request.Parser.ParseDelimitedFrom(stream);
                    Protocol.Response response = handleRequest(request);
                    if (response != null)
                    {
                        sendResponse(response);
                    }
                }
                catch (ServerException e)
                {
                    Console.WriteLine(e.StackTrace);
                }

                try
                {
                    Thread.Sleep(1000);
                }
                catch (ServerException e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }

            try
            {
                stream.Close();
                connection.Close();
            }
            catch (ServerException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }

        private void sendResponse(Protocol.Response response)
        {
            Console.WriteLine("Sending respons" + response);
            lock (stream)
            {
                response.WriteDelimitedTo(stream);
                stream.Flush();
            }
        }

        private Protocol.Response handleRequest(Protocol.Request request)
        {
            Protocol.Response response = null;
            if (request.Type == Protocol.Request.Types.Type.Login)
            {
                Console.WriteLine("LOGIN Request");
                DTOAngajat angajat = Master.getAngajat(request);
                try
                {
                    lock (server)
                    {
                        server.login(angajat, this);
                    }

                    return Master.CreateOkResponse();
                }
                catch (ServerException e)
                {
                    connected = false;
                    return Master.CreateErrorResponse(e.Message);
                }
            }

            if (request.Type == Protocol.Request.Types.Type.Logout)
            {
                Console.WriteLine("LOGOUT Request ");
                DTOAngajat angajat = Master.getAngajat(request);
                try
                {
                    lock (server)
                    {
                        server.logout(angajat, this);
                    }

                    connected = false;
                    return Master.CreateOkResponse();
                }
                catch (ServerException e)
                {
                    return Master.CreateErrorResponse(e.Message);
                }
            }
            if (request.Type == Protocol.Request.Types.Type.GetCurrentCurse)
            {
                Console.WriteLine("Get current races request");
                try
                {
                    lock (server)
                    {
                        DTOBJCursa[] curse = server.getCurrentRaces();
                        return Master.CreateGetDispCurseResponse(curse);
                    }
                }catch(ServerException e)
                {
                    return Master.CreateErrorResponse(e.Message);
                }
            }
            if (request.Type == Protocol.Request.Types.Type.SearchByTeam)
            {
                Console.WriteLine("Search by team request");
                string team = Master.getTeam(request);
                try
                {
                    lock (server)
                    {
                        DTOBJPart[] dTOBJPart = server.searchbyteam(team);
                        return Master.CreateSearchResultResponse(dTOBJPart);
                    }
                }catch(ServerException e)
                {
                    return Master.CreateErrorResponse(e.Message);
                }
            }
            if (request.Type == Protocol.Request.Types.Type.SubmitInsc)
            {
                Console.WriteLine("Handling submit request");
                DTOInfoSubmit infoSubmit = Master.getInfoSubmit(request);
                try
                {
                    lock (server)
                    {
                        server.submitInsc(infoSubmit);
                        return Master.CreateOkResponse();
                    }
                }catch (ServerException e)
                {
                    return Master.CreateErrorResponse(e.Message);
                }

            }
            return response;
        }
        
        public void AngajatSubmitted(DTOBJCursa[] curse)
        {
            Console.WriteLine("Angajat Submitted invoked from Worker");
            try
            {
                sendResponse(Master.CreateNewSubmitResponse(curse));
            }catch(ServerException e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
    }
}
