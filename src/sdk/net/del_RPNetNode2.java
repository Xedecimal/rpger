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