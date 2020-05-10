using Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Networking.ProtocolBuffer;

namespace Networking.Utils
{
    public class SerialServer :ConcurrentServer
    {
        private IServices server;
        private Worker worker;

        public SerialServer(string host, int port, IServices server1): base(host, port)
        {
            this.server = server1;
            Console.WriteLine("Serial Server....");
        }
        protected override Thread createWorker(TcpClient client)
        {
            worker = new Worker(server, client);
            return new Thread(new ThreadStart(worker.run));
        }
    }
}
