function autoexec()
{
	m_menuMain = new Packages.sdk.gui.WMenu();

	m_miFile = new Packages.sdk.gui.WMenuItem("File");
	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("Single Player"));
	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("Multiplayer"));
	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("-"));
	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("Save Current Map"));
	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("Load Current Map"));
	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("-"));
	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("Exit"));

	m_miEditors = new Packages.sdk.gui.WMenuItem("Editors");
	m_miEditorsMode = new Packages.sdk.gui.WMenuItem("Editor Mode");
	m_miEditorsPart = new Packages.sdk.gui.WMenuItem("Particle Editor");
	m_miEditors.AddItem(m_miEditorsMode);
	m_miEditors.AddItem(m_miEditorsPart);

	m_miOptions = new Packages.sdk.gui.WMenuItem("Options");
	m_miOptions.AddItem(new Packages.sdk.gui.WMenuItem("Input Editor"));

	m_menuMain.AddItem(m_miFile);
	m_menuMain.AddItem(m_miEditors);
	m_menuMain.AddItem(m_miOptions);
	Packages.sdk.Engine.guiMain.add(m_menuMain);

	//Packages.sdk.Engine.netMain.OnDataRead += new DataReadHandler(client_read);
}

/*namespace RPGER
{
	class AutoExec : IAutoExec
	{
		//Menus
		private WMenu m_menuMain;
		private WMenuItem m_miFile;
		private WMenuItem m_miEditors;
		private WMenuItem m_miEditorsMode;
		private WMenuItem m_miEditorsPart;
		private WMenuItem m_miOptions;

		public void Exec()
		{

		}

		public void miFileSinglePlayer_Click(object sender, bool state)
		{
			Trace.WriteLine("Single player menu item clicked.", "MenuBar");
			Engine.State = GameState.Active;
			Engine.pecMain.Clear();
			Engine.araMain = new Area(50, 100);
			Player plrClient = new Player("Local User", PlayerType.Controlled, true, Engine.araMain);
			plrClient.HP = 100;
			plrClient.HPMax = 100;
			plrClient.ST = 100;
			plrClient.STMax = 100;
			plrClient.Move(0, 0, 5);

			//Create a debug weapon.
			Clip c = new Clip(Engine.araMain, 13);
			Pistol p = new Pistol(Engine.araMain);
			p.Use(c);
			plrClient.Left = p;

			Engine.Player = plrClient;
			Engine.araMain.AddObject(plrClient);

			for (int ix = 0; ix < 20; ix++)
			{
				Player plrTmp = new Player("An Enemy " + ix, PlayerType.Enemy, true, Engine.araMain);
				plrTmp.HP = 50; plrTmp.HPMax = 50;
				plrTmp.ST = 50; plrTmp.STMax = 50;
				plrTmp.Move(0, 0, 5);
				plrTmp.Destination = new RPoint(Engine.r.Next(0, Engine.araMain.w*32), Engine.r.Next(0, Engine.araMain.h*16));
				Engine.araMain.AddObject(plrTmp);
			}
		}

		public void miFileMultiplayer_Click(object sender, bool state)
		{
			Engine.guiMain.Add(new WLogin());
		}

		public void miFileSaveWorld_Click(object sender, bool state)
		{
			if (Engine.araMain != null)
			{
				BinaryWriter bw = new BinaryWriter(File.OpenWrite("world.dat"), System.Text.Encoding.ASCII);
				Engine.araMain.Serialize(bw);
				bw.Close();
			}
		}

		public void miFileLoadWorld_Click(object sender, bool state)
		{
			BinaryReader br = new BinaryReader(File.OpenRead("world.dat"), System.Text.Encoding.ASCII);
			Engine.araMain = new Area(br);
			br.Close();
			Engine.pecMain.Clear();
			Engine.State = GameState.Active;
		}

		public void miFileExit_Click(object sender, bool state)
		{
			Engine.intMain.Quit();
		}

		public void miEditorsMode_Click(object sender, bool state)
		{
			m_miEditorsMode.Checked = !m_miEditorsMode.Checked;
			Interface.EditMode = m_miEditorsMode.Checked;
		}

		public void miEditorsPart_Click(object sender, bool state)
		{
			Engine.guiMain.Add(new WPartEdit());
		}

		public void miOptionsInput_Click(object sender, bool state)
		{
			Engine.guiMain.Add(new WInput());
		}

		public void client_read(PacketType t, MemoryStream data)
		{
			Trace.WriteLine("Read Data, Type (" + t + ") Size: (" + data.Length + ")");
		}
	}

	public class WPartEdit : WWindow
	{
		private WListView lvEmitters;
		private WEditBox ebName;
		private WLabel lblName;
		private WGroupBox grpEmitters;
		private WGroupBox grpProperties;

		public WPartEdit() : base(new Rect(-1, -1, 348, 290), "Particle Editor")
		{
			ebName = new WEditBox(new Rect(68, 20, 256, 20));
			lblName = new WLabel(new Rect(8, 24, 56, 16), "Name:");

			lvEmitters = new WListView(new Rect(8, 20, 316, 140));
			lvEmitters.Headers.Add(new WListViewHeader("Name"));
			lvEmitters.Headers.Add(new WListViewHeader("X"));
			lvEmitters.Headers.Add(new WListViewHeader("Y"));
			lvEmitters.Headers.Add(new WListViewHeader("Region"));
			lvEmitters.Headers.Add(new WListViewHeader("Amount"));

			grpEmitters = new WGroupBox(new Rect(0, 0, 330, 168), "Particle Emitters");
			grpEmitters.Widgets.Add(lvEmitters);

			grpProperties = new WGroupBox(new Rect(0, 184, 330, 50), "Properties");
			grpProperties.Widgets.Add(lblName);
			grpProperties.Widgets.Add(ebName);

			Add(grpEmitters);
			Add(grpProperties);

			foreach (ParticleEmitter pe in Engine.pecMain)
			{
				lvEmitters.Items.Add(new string[] { pe.ToString(), pe.X.ToString(), pe.Y.ToString() });
			}
		}
	}
}*/
