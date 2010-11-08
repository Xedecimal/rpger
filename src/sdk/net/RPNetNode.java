package sdk.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sdk.Config;

/**
 *
 * @author Xedecimal
 */
public class RPNetNode
{
	private ArrayList<SocketChannel> m_sock_nodes;
	private SocketChannel m_sc_gateway;
	byte[] bin, cin;
	String m_user, m_pass;
	public int Port;

	DataInputStream input;
	DataOutputStream output;

	public ArrayList<RPNetGateway> Gateways;

	public boolean Connected = false;

	public BrowseResultHandler OnBrowseResult;

	//public event DataReadHandler OnDataRead;

	public RPNetNode(Config c)
	{
		//msout = new MemoryStream();
		//bwout = new BinaryWriter(msout);
		Gateways = new ArrayList<RPNetGateway>();
		NodeList nl = c.get("//network/gateway");
		for (int ix = 0; ix < nl.getLength(); ix++)
		{
			Node n = nl.item(ix);
			String host = n.getAttributes().getNamedItem("host").getNodeValue();
			int port = Integer.parseInt(n.getAttributes().getNamedItem("port").getNodeValue());
			Gateways.add(new RPNetGateway(host, port));
		}
	}

	//Browsing

	private Selector m_browse_sel;

	public boolean Browse(String host, int port) throws IOException
	{
		bin = new byte[4096];

		m_browse_sel = Selector.open();
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		sc.register(m_browse_sel, sc.validOps());

		if (!sc.connect(new InetSocketAddress(host, port)))
			return false;

		//m_sc_gateway = SocketChannel.open();
		//m_sc_gateway.configureBlocking(false);
		//m_sc_gateway.connect(new InetSOcketAddress(host, port));

		//m_sock_gateway = new Socket(host, port);
		//input = new DataInputStream(m_sock_gateway.getInputStream());
		//output = new DataOutputStream(m_sock_gateway.getOutputStream());

		return true;
	}

	/*private void m_browser_read(IAsyncResult ar)
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
	}*/

	//Client
	public boolean Connect(String url, String user, String pass)
	{
		m_user = user; m_pass = pass;
		cin = new byte[8];
		Pattern p = Pattern.compile("rpger://(.*?):(.*?)/(.*)");
		Matcher m = p.matcher(url);
		//Match m = Regex.Match(url, , RegexOptions.IgnoreCase);
		String host = m.group(1);
		int port = Integer.parseInt(m.group(2));
		String name = m.group(3);
		try {
			//@TODO: Finish this.
			m_sc_gateway = SocketChannel.open();
			//DataInputStream dis = new DataInputStream(m_sock_gateway.getInputStream());
		} catch (UnknownHostException ex) {
			Logger.getLogger(RPNetNode.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(RPNetNode.class.getName()).log(Level.SEVERE, null, ex);
		}

		return true;
	}

	/*private void m_client_readhead(IAsyncResult ar)
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
	}*/

	/*public void m_client_readchunk(IAsyncResult ar)
	{
		int read = m_sock_gateway.EndReceive(ar);
		System.out.println("chunk size: " + read);

		//MemoryStream ms = new MemoryStream(cin);
		//BinaryReader br = new BinaryReader(ms);

		//PacketType t = (PacketType)br.ReadInt32();
		System.out.println("type: " + t);
		//if (OnDataRead != null) OnDataRead(t, ms);

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
	}*/

	public void Send()
	{
		//@TODO: Fix me.
		//m_sock_gateway.Send(BitConverter.GetBytes(msout.Length));
		//m_sock_gateway.Send(msout.GetBuffer());
	}

	public void SendText(Socket sock, String text)
	{
		/*if (sock == null)
		{
			Iterator<Socket> i = m_sock_nodes.iterator();
			while (i.hasNext())
			{
				Socket sockix = i.next();
				//sockix.SendText(sockix, text);
			}
		}
		else
		{
			byte[] data = Encoding.ASCII.GetBytes(text);
			if (sock != null) sock.Send(data, 0, data.Length, SocketFlags.None);
		}*/
	}

	public void Update(int offx, int offy)
	{
		if (m_browse_sel != null)
		{
			try {
				m_browse_sel.select();
			} catch (IOException ex) {
				Logger.getLogger(RPNetNode.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		Iterator<SelectionKey> it = m_browse_sel.selectedKeys().iterator();
		while (it.hasNext())
		{
			SelectionKey sk = it.next();
			if (sk.isReadable())
			{
				System.out.println("Data available!");
			}
		}
	}

	public interface BrowseResultHandler
	{
		public void onResult(String url);
	}
}