package sdk.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 * @author Xedecimal
 */
public class NetManager
{
	Server m_server;
	Client m_client;

	public static ByteBuffer BBI;
	public static ByteBuffer BBO;

	//private final Peer m_peer;

	public NetManager() throws IOException
	{
		BBI = ByteBuffer.allocateDirect(64*64*64*2);
		BBO = ByteBuffer.allocateDirect(64*64*64*2);

		/*TODO: Make this work some day.

		// Make our multicaster.

		m_peer = new Peer();

		// Multicast for a remote server.

		String rserver = null;
		if ((rserver = m_peer.Search()) != null)
		{
			System.out.println("Found one!");
		}
		*/

		// Make our own server.
		m_server = new Server();
		m_server.Listen(29838);

		m_client = new Client();
		//m_client.Connect("rpger://localhost:29838");
	}

	public void Update() throws IOException
	{
		//m_peer.Update();
		m_server.Update();
		m_client.Update();
	}

	static void Send(SocketChannel sc, ByteBuffer bb) throws IOException
	{
		ByteBuffer bbOut = ByteBuffer.allocateDirect(bb.limit()+4);
		bbOut.putInt(bb.limit());
		bbOut.put(bb);
		bbOut.flip();
		sc.write(bbOut);
	}

	public static final int PS_NONE = 0;
	/** An error has occured. */
	public static final int PS_ERROR = 1;
	public static final int PS_PROVIDE_USERNAME = 2;
	public static final int PS_USERNAME = 3;
	public static final int PS_ProvidePassword = 4;
	public static final int PS_Password = 5;
	public static final int PS_ID = 6;
	public static final int PS_Area = 7;
	public static final int PS_GotArea = 8;
	public static final int PS_ChatSay = 9;
	public static final int PS_AddPlayer = 10;
	public static final int PS_RemovePlayer = 11;
	public static final int PS_MoveState = 12;
}