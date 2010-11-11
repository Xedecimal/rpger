package sdk.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sdk.Engine;
import sdk.player.Player;
import sdk.player.Player.DirChangeNotifier;
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

		NetManager.BBI.clear();
		NetManager.BBO.clear();
		int read = sc.read(NetManager.BBI);
		NetManager.BBI.flip();

		if (read < 4)
		{
			Engine.guiMain.MessageBox("Read smaller size than packet head.");
			return;
		}

		System.out.println("chunk size: " + read);

		int size = NetManager.BBI.getInt();
		int type = NetManager.BBI.getInt();

		switch (type)
		{
			case NetManager.PS_PROVIDE_USERNAME:
				NetManager.BBO.putInt(NetManager.PS_USERNAME);
				NetManager.BBO.put(Engine.cs.encode(m_user));
				NetManager.BBO.flip();
				NetManager.Send(sc, NetManager.BBO);
				break;
			case NetManager.PS_ProvidePassword:
				NetManager.BBO.putInt(NetManager.PS_Password);
				NetManager.BBO.put(Engine.cs.encode(m_pass));
				NetManager.BBO.flip();
				NetManager.Send(sc, NetManager.BBO);
				break;
			case NetManager.PS_Area:
				Engine.araMain = new Area(NetManager.BBI);
				Engine.State = GameState.Active;
				Engine.pecMain.clear();

				//TODO: Dont' do this here.
				// Configure Local Player
				Player plrClient = new Player("Local User",
					PlayerType.Controlled, true, Engine.araMain);
				plrClient.HP = 100;
				plrClient.HPMax = 100;
				plrClient.ST = 100;
				plrClient.STMax = 100;
				plrClient.Move(0, 0, 5);
				plrClient.onDirChange = new DirChangeHandler();
				Engine.Player = plrClient;
				Engine.araMain.AddObject(plrClient);

				NetManager.BBO.putInt(NetManager.PS_GotArea);
				NetManager.BBO.flip();
				NetManager.Send(sc, NetManager.BBO);
				break;
			case NetManager.PS_MoveState:
				int id = NetManager.BBI.getInt();
				int dir = NetManager.BBI.getInt();
				float xp = NetManager.BBI.getFloat();
				float yp = NetManager.BBI.getFloat();
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

	private class DirChangeHandler implements DirChangeNotifier
	{
		public void dirChange(Player p, int dir)
		{
			NetManager.BBO.clear();
			NetManager.BBO.putInt(NetManager.PS_MoveState);
			NetManager.BBO.putInt(p.ID);
			NetManager.BBO.putInt(p.Dir);
			NetManager.BBO.putDouble(p.x);
			NetManager.BBO.putDouble(p.y);
			NetManager.BBO.flip();
			try { NetManager.Send(m_sc, NetManager.BBO); }
			catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}