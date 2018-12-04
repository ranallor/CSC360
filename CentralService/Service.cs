using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;

namespace CentralService
{
    class Service
    {
        Socket serviceSocket;
        int port = 100;
        int backLog = 5;

        public Service()
        {
            serviceSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Udp);
        }

        public void start()
        {
            serviceSocket.Bind(new IPEndPoint(IPAddress.Any, port));
            serviceSocket.Listen(backLog);
        }
    }
}
