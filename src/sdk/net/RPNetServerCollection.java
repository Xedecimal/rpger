package sdk.net;

import java.net.Socket;

/**
 *
 * @author Xedecimal
 */
/*public class RPNetServerCollection extends ArrayList<RPNetNode>
{
	private Socket m_sock;
	private byte[] m_datin;

	public void Send(string text) { foreach (RPNetNode s in this) s.SendText(null, text); }

	public RPNetServerCollection()
	{
		m_datin = new byte[4096];
		m_sock = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
		IPEndPoint ipe = new IPEndPoint(IPAddress.Any, 29838);
		m_sock.Bind(ipe);
		m_sock.Listen(5);
		m_sock.BeginAccept(new AsyncCallback(m_sock_accept), this);
	}

	public void Update(int offx, int offy)
	{
		foreach (RPNetNode s in this) s.Update(offx, offy);
	}

	public void m_sock_accept(IAsyncResult ar)
	{
		Socket stmp = m_sock.EndAccept(ar);
		IPAddress ip = Dns.GetHostEntry(Dns.GetHostName()).AddressList[0];
		System.out.println("Browse from: " + stmp.RemoteEndPoint);
		foreach (RPNetNode s in this)
		{
			byte[] datout = Encoding.ASCII.GetBytes("rpger://" + ip + ":" + s.Port + "\n");
			stmp.Send(datout, 0, datout.Length, SocketFlags.None);
		}
		stmp.Close();
		m_sock.BeginAccept(new AsyncCallback(m_sock_accept), this);
	}
}*/