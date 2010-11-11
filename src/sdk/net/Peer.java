package sdk.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 *
 * @author Xedecimal
 */
public class Peer
{
	private DatagramChannel sc_in;
	private Selector sel_in;
	private DatagramChannel sc_out;
	private Selector sel_out;

	private ByteBuffer bbIn;

	// Set up our server

	public Peer() throws IOException
	{
		bbIn = ByteBuffer.allocateDirect(1024);

		MulticastSocket s = new MulticastSocket(9292);
		s.joinGroup(InetAddress.getByName("228.1.2.3"));
		sc_in.configureBlocking(false);
		sel_in = Selector.open();
		sc_in.register(sel_in, sc_in.validOps());
		System.out.println("Listening for DGP");
	}

	public String Search() throws IOException
	{
		byte[] buf = {22, 33, 44};
		DatagramSocket s = new DatagramSocket();
		InetAddress iag = InetAddress.getByName("228.1.2.3");
		DatagramPacket dp = new DatagramPacket(buf, buf.length, iag, 9292);
		s.send(dp);
		System.out.println("Sending DGP");
		return null;
	}

	public void Update() throws IOException
	{
		sel_in.selectNow();

		Iterator<SelectionKey> it = sel_in.selectedKeys().iterator();
		while (it.hasNext())
		{
			SelectionKey sk = it.next();
			if (sk.isReadable()) onRead(sk);
		}
	}

	private void onRead(SelectionKey sk) throws IOException
	{
		DatagramChannel dc = (DatagramChannel)sk.channel();
		int read = dc.read(bbIn);
		
		System.out.println("Multicast server got a message.");
	}
}
