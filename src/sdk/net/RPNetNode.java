package sdk.net;

import java.net.Socket;
import java.util.ArrayList;
import javax.swing.text.Document;

/**
 *
 * @author Xedecimal
 */
/*public class RPNetNode
{
	private ArrayList<Socket> m_sock_nodes;
	private Socket m_sock_gateway;
	byte[] bin, cin;
	String m_user, m_pass;
	public int Port;

	//MemoryStream msout;
	//BinaryWriter bwout;

	public ArrayList<RPNetGateway> Gateways;

	public boolean Connected = false;

	//public event BrowseResultHandler OnBrowseResult;
	//public event DataReadHandler OnDataRead;

	public RPNetNode(Document config)
	{
		msout = new MemoryStream();
		bwout = new BinaryWriter(msout);
		Gateways = new List<RPNetGateway>();
		XmlNodeList xnl = config.SelectNodes("//network/gateway");
		foreach (XmlNode xn in xnl)
		{
			Gateways.Add(new RPNetGateway(xn.Attributes["host"].Value, Int32.Parse(xn.Attributes["port"].Value)));
		}
	}

	//Browsing
	public boolean Browse(String host, int port)
	{
		bin = new byte[4096];
		m_sock_gateway = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
		try
		{
			Port = port;
			IPEndPoint ipe = new IPEndPoint(Dns.GetHostEntry(host).AddressList[0], port);
			m_sock_gateway.Connect(ipe);
			m_sock_gateway.BeginReceive(bin, 0, bin.Length, SocketFlags.None, new AsyncCallback(m_browser_read), this);
		}
		catch (SocketException) { return false; }
		return true;
	}

	private void m_browser_read(IAsyncResult ar)
	{
		int read = m_sock_gateway.EndReceive(ar);
		if (read == 0) return;
		string dat = Encoding.ASCII.GetString(bin, 0, read);
		StringReader sr = new StringReader(dat);
		string line = sr.ReadLine();

		while (line != null)
		{
			if (OnBrowseResult != null) OnBrowseResult(line);
			line = sr.ReadLine();
		}
		m_sock_gateway.BeginReceive(bin, 0, bin.Length, SocketFlags.None, new AsyncCallback(m_browser_read), this);
	}

	//Client
	public boolean Connect(String url, String user, String pass)
	{
		m_user = user; m_pass = pass;
		cin = new byte[8];
		Match m = Regex.Match(url, "rpger://(.*?):(.*?)/(.*)", RegexOptions.IgnoreCase);
		string host = m.Groups[1].Value;
		int port = Convert.ToInt32(m.Groups[2].Value);
		string name = m.Groups[3].Value;

		m_sock_gateway = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
		try
		{
			IPEndPoint ipe = new IPEndPoint(Dns.GetHostEntry(host).AddressList[0], port);
			m_sock_gateway.Connect(ipe);
			m_sock_gateway.BeginReceive(cin, 0, cin.Length, SocketFlags.None, new AsyncCallback(m_client_readhead), this);
		}
		catch (SocketException) { return false; }
		return true;
	}

	private void m_client_readhead(IAsyncResult ar)
	{
		int read = 0;
		try
		{
			read = m_sock_gateway.EndReceive(ar);
		}
		catch (SocketException se)
		{
			Engine.guiMain.MessageBox("Connection problem...\n" + se.Message);
			return;
		}
		if (read < 8)
		{
			Engine.guiMain.MessageBox("Read smaller size than packet head.");
			return;
		}

		//Attach memorystream and binaryreader to the client buffer
		long size = BitConverter.ToInt64(cin, 0);
		System.out.println("--- Packet Head (" + size + ") --------------------------");

		cin = new byte[size];
		m_sock_gateway.BeginReceive(cin, 0, cin.Length, SocketFlags.None, new AsyncCallback(m_client_readchunk), this);
	}

	public void m_client_readchunk(IAsyncResult ar)
	{
		int read = m_sock_gateway.EndReceive(ar);
		System.out.println("chunk size: " + read);

		MemoryStream ms = new MemoryStream(cin);
		BinaryReader br = new BinaryReader(ms);

		PacketType t = (PacketType)br.ReadInt32();
		System.out.println("type: " + t);
		if (OnDataRead != null) OnDataRead(t, ms);

		switch (t)
		{
			case PacketType.ProvideUsername:
				bwout.Write((int)PacketType.Username);
				bwout.Write(m_user);
				m_sock_gateway.Send(msout.GetBuffer(), (int)msout.Length, SocketFlags.None);
				msout.SetLength(0);
				break;
			case PacketType.ProvidePassword:
				bwout.Write((int)PacketType.Password);
				bwout.Write(m_pass);
				m_sock_gateway.Send(msout.GetBuffer(), (int)msout.Length, SocketFlags.None);
				msout.SetLength(0);
				break;
			case PacketType.Area:
				Engine.araMain = new World.Area(br);
				Engine.State = Core.GameState.Active;
				bwout.Write((int)PacketType.GotArea);
				m_sock_gateway.Send(msout.GetBuffer(), (int)msout.Length, SocketFlags.None);
				break;
			case PacketType.MoveState:
				int id = br.ReadInt32();
				Direction dir = (Direction)br.ReadInt32();
				float xp = br.ReadSingle();
				float yp = br.ReadSingle();
				//Engine.mapMain.Players[id].Dir = dir;
				//Engine.araMain.Players[id].Pos.x = xp;
				//Engine.araMain.Players[id].Pos.y = yp;
				System.out.println("id (" + id + ") dir (" + dir + ") x (" + xp + ") y (" + yp + ")");
				break;
		}

		cin = new byte[8];
		m_sock_gateway.BeginReceive(cin, 0, cin.Length, SocketFlags.None, new AsyncCallback(m_client_readhead), this);
	}

	public void Send()
	{
		m_sock_gateway.Send(BitConverter.GetBytes(msout.Length));
		m_sock_gateway.Send(msout.GetBuffer());
	}

	public void SendText(Socket sock, String text)
	{
		if (sock == null)
		{
			Iterator<Socket> i = m_sock_nodes.iterator();
			while (i.hasNext()) i.next().SendText(sockix, text);
		}
		else
		{
			byte[] data = Encoding.ASCII.GetBytes(text);
			if (sock != null) sock.Send(data, 0, data.Length, SocketFlags.None);
		}
	}

	public void Update(int offx, int offy)
	{
		//m_area.Update(offx, offy);
	}
}*/