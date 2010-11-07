/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.net;

/**
 *
 * @author Xedecimal
 */
	/*public class RPNetNode2
	{
		private Socket m_sock;
		private byte[] m_datin;
		public int Port;
		private Area m_area;

		private Dictionary<int, RPNetServerClient> m_clients = new Dictionary<int, RPNetServerClient>();
		private int cid = 0;

		MemoryStream msinp;
		BinaryReader brinp;
		MemoryStream msout;
		BinaryWriter bwout;

		public RPNetNode2(Area a, int port, Uri gateway)
		{
			msinp = new MemoryStream();
			brinp = new BinaryReader(msinp);
			msout = new MemoryStream();
			bwout = new BinaryWriter(msout);

			m_area = new World.Area(50, 50);
			m_area.Map[2, 2].ID = 2;
			for (int ix = 0; ix < 20; ix++)
			{
				Player p = new Player("Jack " + ix, PlayerType.Enemy, false, m_area);
				p.OnDirChange += new DirChangeHandler(p_OnDirChange);
				m_area.AddObject(p);
			}
			m_datin = new byte[4096];
			m_sock = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
			IPEndPoint ipe = new IPEndPoint(IPAddress.Any, port);
			m_sock.Bind(ipe);
			m_sock.Listen(5);
			m_sock.BeginAccept(new AsyncCallback(m_sock_accept), this);
		}

		private void m_sock_accept(IAsyncResult ar)
		{
			Socket stmp = m_sock.EndAccept(ar);
			System.out.println("Connection from: " + stmp.RemoteEndPoint + " (" + cid + ")");

			RPNetServerClient c = new RPNetServerClient(cid, stmp);
			m_clients.Add(cid++, c);

			bwout.Write((int)PacketType.ProvideUsername);
			stmp.Send(BitConverter.GetBytes(msout.Length));
			stmp.Send(msout.GetBuffer(), (int)msout.Length, SocketFlags.None);
			msout.SetLength(0);

			stmp.BeginReceive(m_datin, 0, m_datin.Length, SocketFlags.None, new AsyncCallback(m_sock_read), c);
			m_sock.BeginAccept(new AsyncCallback(m_sock_accept), this);
		}

		private void m_sock_read(IAsyncResult ar)
		{
			RPNetServerClient c = (RPNetServerClient)ar.AsyncState;
			int read = 0;

			try { read = c.Sock.EndReceive(ar); }
			catch (SocketException)
			{
				System.out.println("Client " + cid + " threw an error, removing.");
				m_clients.Remove(cid);
				return;
			}
			if (read < 1)
			{
				System.out.println("Client " + cid + " quit.");
				m_clients.Remove(cid);
				return;
			}

			msinp = new MemoryStream(m_datin, 0, read);
			brinp = new BinaryReader(msinp);
			PacketType t = (PacketType)brinp.ReadInt32();

			switch (t)
			{
				case PacketType.Username:
					string user = brinp.ReadString();
					System.out.println("[" + cid + "] Username: " + user);
					bwout.Write((int)PacketType.ProvidePassword);
					c.Name = user;
					c.Sock.Send(BitConverter.GetBytes(msout.Length));
					c.Sock.Send(msout.GetBuffer(), (int)msout.Length, SocketFlags.None);
					break;
				case PacketType.Password:
					string pass = brinp.ReadString();
					System.out.println("[" + cid + "] Password: " + pass);
					m_area.AddObject(new Player(c.Name, PlayerType.Controlled, false, m_area));
					bwout.Write((int)PacketType.Area);
					m_area.Serialize(bwout);
					c.Sock.Send(BitConverter.GetBytes(msout.Length));
					c.Sock.Send(msout.GetBuffer(), (int)msout.Length, SocketFlags.None);
					break;
				case PacketType.GotArea:
					System.out.println("[" + cid + "] Got area.");
					bwout.Write((int)PacketType.ID);
					bwout.Write(c.ID);
					c.Sock.Send(BitConverter.GetBytes(msout.Length));
					c.Sock.Send(msout.GetBuffer(), (int)msout.Length, SocketFlags.None);
					c.State = ClientState.Authenticated;
					break;
			}

			msout.SetLength(0);
			c.Sock.BeginReceive(m_datin, 0, m_datin.Length, SocketFlags.None, new AsyncCallback(m_sock_read), ar.AsyncState);
		}



		/// <summary>
		/// Send the data in ms to all clients that have the ClientState of state
		/// and truncate ms to zero.
		/// </summary>
		/// <param name="state">Clients with this state receieve the data.</param>
		/// <param name="ms">Stream with data to send.</param>
		public void Broadcast(ClientState state, MemoryStream ms)
		{
			byte[] size = BitConverter.GetBytes(ms.Length);
			byte[] buf = msout.GetBuffer();

			foreach (RPNetServerClient c in m_clients.Values)
			{
				try
				{
					c.Sock.Send(size);
					c.Sock.Send(buf, (int)ms.Length, SocketFlags.None);
				}
				catch (SocketException)
				{
					System.out.println("[NET] Client " + c.ID + " threw error on send, destroying.");
					c.Sock.Close();
					m_clients.Remove(c.ID);
					continue;
				}
				System.out.println("[NET] Broadcasted " + ms.Length + " bytes");
			}
			ms.SetLength(0);
		}

		private void p_OnDirChange(Player p, Direction d)
		{
			bwout.Write((int)PacketType.MoveState);
			bwout.Write(p.ID);
			bwout.Write((int)p.Dir);
			bwout.Write(p.x);
			bwout.Write(p.y);
			Broadcast(ClientState.Authenticated, msout);
		}
	}*/