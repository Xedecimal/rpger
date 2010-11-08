package sdk.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdk.Engine;

/**
 *
 * @author Xedecimal
 */
public class RPNetServerCollection extends ArrayList<RPNetNode>
{
	private ServerSocketChannel m_ssc;
	private Selector m_sel;

	public void Send(String text)
	{
		Iterator<RPNetNode> it = iterator();
		while (it.hasNext())
		{
			RPNetNode rnn = it.next();
			rnn.SendText(null, text);
		}
	}

	public RPNetServerCollection()
	{
		try {
			m_ssc = ServerSocketChannel.open();
			m_ssc.configureBlocking(false);
			m_ssc.socket().bind(new InetSocketAddress(29838));
			m_ssc.register(m_sel, SelectionKey.OP_ACCEPT);
		} catch (IOException ex) {
			Logger.getLogger(RPNetServerCollection.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void Update(int offx, int offy) throws IOException
	{
		m_sel.select();
		Iterator<SelectionKey> it = m_sel.selectedKeys().iterator();

		while (it.hasNext())
		{
			SelectionKey sk = it.next();
			it.remove();
			if (sk.isAcceptable())
			{
				ServerSocketChannel ssc = (ServerSocketChannel)sk.channel();
				SocketChannel sc = ssc.accept();
				onAccept(sc);
			}
		}

		Iterator<RPNetNode> it2 = iterator();
		while (it2.hasNext())
		{
			RPNetNode rnn = it2.next();
			rnn.Update(offx, offy);
		}
	}

	public void onAccept(SocketChannel sc)
	{
		System.out.println("Browse from: " +
			sc.socket().getInetAddress().getHostAddress());
		CharsetEncoder ce = Engine.cs.newEncoder();
		try
		{
			ByteBuffer bb = ce.encode(CharBuffer.wrap("rpger://" + m_ssc.socket().getInetAddress().getHostName() + ":" + m_ssc.socket().getLocalPort() + "\n"));
			sc.write(bb);
		}
		catch (Exception ex)
		{
			Logger.getLogger(RPNetServerCollection.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}