package sdk.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sdk.Engine;
import sdk.player.Player;
import sdk.player.PlayerType;
import sdk.types.GameState;
import sdk.world.Area;

/**
 *
 * @author Xedecimal
 */
public class Client
{
	private SocketChannel m_sc;
	private Selector m_sel;

	// TODO: Resolve these
	byte[] bin, cin;
	String m_user, m_pass;
	public int Port;

	DataInputStream input;
	DataOutputStream output;

	public boolean Connected = false;

	public BrowseResultHandler OnBrowseResult;

	//public event DataReadHandler OnDataRead;

	public Client()
	{
		m_user = "Anonymous";
		m_pass = "";
	}

	public boolean Browse(String host, int port) throws IOException
	{
		return false;
		/*bin = new byte[4096];

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

		return true;*/
	}

	public boolean Connect(String url) throws IOException
	{
		System.out.println("Client::Connect");
		//m_user = user; m_pass = pass;
		cin = new byte[8];
		Pattern p = Pattern.compile("rpger://([^:]+):(\\d*)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(url);
		if (m.find())
		{
			String host = m.group(1);
			int port = Integer.parseInt(m.group(2));
			//String name = m.group(3);

			m_sc = SocketChannel.open();
			m_sc.configureBlocking(false);
			m_sel = Selector.open();
			m_sc.register(m_sel, m_sc.validOps());

			m_sc.connect(new InetSocketAddress(host, port));

			return true;
		}
		return false; // Couldn't process URL.
	}

	/*private void m_client_readhead(IAsyncResult ar)
	{
		int read = 0;
		read = m_sock_gateway.EndReceive(ar);

		//Attach memorystream and binaryreader to the client buffer
		long size = BitConverter.ToInt64(cin, 0);
		System.out.println("--- Packet Head (" + size + ") --------------------------");

		cin = new byte[size];
		m_sock_gateway.BeginReceive(cin, 0, cin.Length, SocketFlags.None,
			new AsyncCallback(m_client_readchunk), this);
	}*/

	/*public void m_client_readchunk(IAsyncResult ar)
	{

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

	public void Update() throws IOException
	{
		if (m_sel != null)
		{
			m_sel.select();

			Iterator<SelectionKey> it = m_sel.selectedKeys().iterator();
			while (it.hasNext())
			{
				SelectionKey sk = it.next();
				it.remove();
				if (sk.isConnectable()) onConnect(sk);
				if (sk.isReadable()) onRead(sk);
			}
		}
	}

	public void onConnect(SelectionKey sk) throws IOException
	{
		System.out.println("Client::onConnect");

		SocketChannel sc = (SocketChannel)sk.channel();
		if (!sc.finishConnect())
		{
			System.out.println("Unable to connect!");
			sk.cancel();
		}
	}

	public void onRead(SelectionKey sk) throws IOException
	{
		SocketChannel sc = (SocketChannel)sk.channel();
		ByteBuffer data = ByteBuffer.allocateDirect(16384);
		int read = sc.read(data);
		if (read < 4)
		{
			Engine.guiMain.MessageBox("Read smaller size than packet head.");
			return;
		}

		System.out.println("chunk size: " + read);

		data.flip();
		int size = data.getInt();
		System.out.println("Total size: "+size);
		int type = data.getInt();
		System.out.println("type: " + type);
		//if (OnDataRead != null) OnDataRead(t, ms);

		ByteBuffer bbOut = ByteBuffer.allocateDirect(16384);

		switch (type)
		{
			case NetManager.PS_PROVIDE_USERNAME:
				bbOut.putInt(NetManager.PS_USERNAME);
				bbOut.put(Engine.cs.encode(m_user));
				bbOut.flip();
				NetManager.Send(sc, bbOut);
				break;
			case NetManager.PS_ProvidePassword:
				bbOut.putInt(NetManager.PS_Password);
				bbOut.put(Engine.cs.encode(m_pass));
				bbOut.flip();
				NetManager.Send(sc, bbOut);
				break;
			case NetManager.PS_Area:
				Engine.araMain = new Area(data);
				Engine.State = GameState.Active;
				Engine.pecMain.clear();

				// Configure Local Player
				Player plrClient = new Player("Local User",
					PlayerType.Controlled, true, Engine.araMain);
				plrClient.HP = 100;
				plrClient.HPMax = 100;
				plrClient.ST = 100;
				plrClient.STMax = 100;
				plrClient.Move(0, 0, 5);
				Engine.Player = plrClient;
				Engine.araMain.AddObject(plrClient);

				bbOut.putInt(NetManager.PS_GotArea);
				bbOut.flip();
				NetManager.Send(sc, bbOut);
				break;
			case NetManager.PS_MoveState:
				int id = data.getInt();
				int dir = data.getInt();
				float xp = data.getFloat();
				float yp = data.getFloat();
				//Engine.mapMain.Players[id].Dir = dir;
				//Engine.araMain.Players[id].Pos.x = xp;
				//Engine.araMain.Players[id].Pos.y = yp;
				System.out.println("id (" + id + ") dir (" + dir + ") x (" + xp + ") y (" + yp + ")");
				break;
		}
	}

	public interface BrowseResultHandler
	{
		public void onResult(String url);
	}
}