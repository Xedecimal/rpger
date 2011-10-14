package sdk.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import sdk.Engine;
import sdk.player.Player;
import sdk.player.PlayerType;

/**
 *
 * @author Xedecimal
 */
public class ServerClient {
	private int m_id;
	public SocketChannel SC;
	private Selector m_sel;
	private int m_state;
	private String m_name;

	public ServerClient(int id, SocketChannel sc, int state) throws IOException
	{
		m_id = id;
		SC = sc;
		m_state = state;

		m_sel = Selector.open();
		sc.register(m_sel, sc.validOps());
	}

	public void Update() throws IOException
	{
		if (m_sel != null)
		{
			m_sel.selectNow();

			Iterator<SelectionKey> it = m_sel.selectedKeys().iterator();

			while (it.hasNext())
			{
				SelectionKey sk = (SelectionKey)it.next();
				it.remove();

				if (sk.isReadable())
				{
					SocketChannel sc = (SocketChannel)sk.channel();
					onRead(sc);
				}
			}
		}
	}

	public void onRead(SocketChannel sc) throws IOException
	{
		ByteBuffer bbIn = ByteBuffer.allocateDirect(1024);

		int read = sc.read(bbIn);
		bbIn.flip();

		int size = bbIn.getInt();
		int type = bbIn.getInt();

		ByteBuffer bbOut = ByteBuffer.allocateDirect(64*64*64*2);

		switch (type)
		{
			case NetManager.PS_USERNAME:
				byte[] arr = new byte[bbIn.remaining()];
				System.out.println("Username Length: "+arr.length);
				bbIn.get(arr);
				m_name = new String(arr);
				System.out.println("[" + m_id + "] Username: " + m_name);
				bbOut.putInt(NetManager.PS_ProvidePassword);
				bbOut.flip();
				NetManager.Send(SC, bbOut);
				break;
			case NetManager.PS_Password:
				byte[] bpass = new byte[bbIn.remaining()];
				bbIn.get(bpass);
				String pass = new String(bpass);
				System.out.println("[" + m_id + "] Password: " + pass);
				Engine.araMain.AddObject(new Player(m_name, PlayerType.Controlled, false, Engine.araMain));
				bbOut.putInt(NetManager.PS_Area);
				Engine.araMain.Serialize(bbOut);
				bbOut.flip();
				NetManager.Send(SC, bbOut);
				break;
			case NetManager.PS_GotArea:
				System.out.println("[" + m_id + "] Got area.");
				bbOut.putInt(NetManager.PS_ID);
				bbOut.putInt(m_id);
				bbOut.flip();
				NetManager.Send(SC, bbOut);
				m_state = CS_Authenticated;
				break;
		}
	}

	public void SendText(String text) throws IOException
	{
		ByteBuffer bb = Engine.cs.encode(CharBuffer.wrap(text));
		SC.write(bb);
	}

	public static final int CS_Unknown = 0;
	public static final int CS_Connected = 1;
	public static final int CS_Authenticated = 2;
}
