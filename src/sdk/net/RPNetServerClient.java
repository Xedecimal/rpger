package sdk.net;

import java.net.Socket;

/**
 *
 * @author Xedecimal
 */
	public class RPNetServerClient
	{
		public int ID;
		public Socket Sock;
		public ClientState State;
		public String Name;

		public RPNetServerClient(int id, Socket s)
		{
			ID = id;
			Sock = s;
			State = ClientState.Connected;
		}
	}
