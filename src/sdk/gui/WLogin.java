package sdk.gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdk.Engine;
import sdk.net.Client.BrowseResultHandler;
import sdk.types.Rect;

/**
 * An easy to use, pre-built login window.
 * @author Xedecimal
 * @todo <todo>Need this lbServers to be a listview with multiple sortable columns.</todo>
 * @todo <todo>Move this all to an external script.</todo>
 */
public class WLogin extends WWindow
{
	private WEditBox ebHost;
	private WEditBox ebUser;
	private WEditBox ebPass;
	private WListBox lbServ;

	public WLogin()
	{
		super(new Rect(-1, -1, 350, 380), "Login");
		ebHost = new WEditBox(new Rect(120,   0,   200, 20), "192.168.1.150");
		ebUser = new WEditBox(new Rect(120,   25,  200, 20), "Blank");
		ebPass = new WEditBox(new Rect(120,   50,  200, 20), "", true);
		lbServ = new WListBox(new Rect(0,     125, 300, 180));

		Add(new WLabel  (new Rect(0,   0,   80,  20), "Host: "));
		Add(ebHost);
		Add(new WLabel  (new Rect(0,   25,  80,  20), "Login: "));
		Add(ebUser);
		Add(new WLabel  (new Rect(0,   50,  80,  20), "Password: "));
		Add(ebPass);
		Add(new WButton (new Rect(0,   75,  90,  16), "Search", new ButSearch()));
		Add(new WButton (new Rect(95,  75,  90,  16), "Cancel", new ButCancel()));
		Add(new WLabel  (new Rect(0,   100, 90,  18), "Servers"));
		Add(lbServ);
		Add(new WButton (new Rect(0,   316, 90,  16), "Connect", new ButConnect()));

		//Engine.netMain.OnBrowseResult = new BrowseResult();
	}

	private class ButSearch implements ClickHandler
	{
		public void onClick(Object sender, boolean state)
		{
			/*try
			{
				if (!Engine.netMain.Browse(ebHost.Text, 29838))
				{
					Engine.guiMain.MessageBox("Error connecting to browse server." + "\ncheck the host.");
				}
			}
			catch (IOException ex)
			{
				Logger.getLogger(WLogin.class.getName()).log(Level.SEVERE, null, ex);
			}*/
		}
	}

	private class ButCancel implements ClickHandler
	{
		public void onClick(Object sender, boolean state) {
			Engine.guiMain.RemoveWidget((WWindow)sender);
		}
	}

	private class ButConnect implements ClickHandler
	{
		public void onClick(Object sender, boolean state) {
			/*String item = (String)lbServ.Items.get(lbServ.Selected);

			System.out.println("Connecting to: " + item);

			if (!Engine.netMain.Connect(item, ebUser.Text, ebPass.Text))
			{
				Engine.guiMain.MessageBox("Error connecting,\ncheck the host.");
				return;
			}
			Engine.guiMain.RemoveWidget((Widget)sender);*/
		}
	}

	private class BrowseResult implements BrowseResultHandler
	{
		public void onResult(String url) {
			lbServ.Add(url);
		}
	}
}