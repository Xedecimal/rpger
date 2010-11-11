package sdk.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Xedecimal
 */
public class Server
{
	private ServerSocketChannel m_ssc;
	private ArrayList<ServerClient> m_clients;
	private Selector m_sel;
	private int m_cid; // Autoincrement client id.

	public Server()
	{
		m_clients = new ArrayList<ServerClient>();
	}

	public void Listen(int port) throws IOException
	{
		m_ssc = ServerSocketChannel.open();
		m_ssc.configureBlocking(false);
		m_ssc.socket().bind(new InetSocketAddress(port));

		m_sel = Selector.open();
		m_ssc.register(m_sel, SelectionKey.OP_ACCEPT);
	}

	public void Update() throws IOException
	{
		m_sel.selectNow();
		Iterator<SelectionKey> it = m_sel.selectedKeys().iterator();

		while (it.hasNext())
		{
			SelectionKey sk = it.next();
			it.remove();
			if (sk.isAcceptable())
			{
				ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				onAccept(sc);
			}
		}

		Iterator<ServerClient> it2 = m_clients.iterator();
		while (it2.hasNext())
		{
			ServerClient sc = it2.next();
			sc.Update();
		}
	}

	public void onAccept(SocketChannel sc) throws IOException
	{
		ServerClient scnew = new ServerClient(m_cid++, sc,
			ServerClient.CS_Connected);
		m_clients.add(scnew);

		NetManager.BBO.putInt(NetManager.PS_PROVIDE_USERNAME);
		NetManager.BBO.flip();
		NetManager.Send(scnew.SC, NetManager.BBO);
	}

	public void Broadcast(String text) throws IOException
	{
		Iterator<ServerClient> it = m_clients.iterator();
		while (it.hasNext())
		{
			ServerClient sc = it.next();
			sc.SendText(text);
		}
	}
}
