importPackage(Packages.sdk);
importPackage(Packages.sdk.gui);
importPackage(Packages.sdk.player);
importPackage(Packages.sdk.types);
importPackage(Packages.sdk.item);
importPackage(Packages.sdk.world);

var ch_file_sp = new ClickHandler() {
	onClick: function () {
		Engine.State = GameState.Active;
		Engine.pecMain.clear();
		Engine.araMain = new Area(50, 100);
		var plrClient = new Player("Local User", PlayerType.Controlled, true, Engine.araMain);
		plrClient.HP = 100;
		plrClient.HPMax = 100;
		plrClient.ST = 100;
		plrClient.STMax = 100;
		plrClient.Move(0, 0, 5);

		//Create a debug weapon.
		var c = new Clip(Engine.araMain, 13);
		var p = new Pistol(Engine.araMain);
		p.Use(c);
		plrClient.setLeft(p);

		Engine.Player = plrClient;
		Engine.araMain.AddObject(plrClient);

		for (var ix = 0; ix < 20; ix = ix+1)
		{
			var plrTmp = new Player("An Enemy " + ix, PlayerType.Enemy, true, Engine.araMain);
			plrTmp.HP = 50; plrTmp.HPMax = 50;
			plrTmp.ST = 50; plrTmp.STMax = 50;
			plrTmp.Move(0, 0, 5);
			plrTmp.Destination = new RPoint(Engine.r.nextInt(Engine.araMain.w()*32), Engine.r.nextInt(Engine.araMain.h()*16));
			Engine.araMain.AddObject(plrTmp);
		}
	}
}

var ch_file_mp = new ClickHandler() {
	onClick: function () {
		var w = new WLogin();
		Engine.guiMain.add(w);
	}
}

var ch_file_save = new ClickHandler() {
	onClick: function () {
		if (Engine.araMain != null)
		{
			var bw = new BinaryWriter(File.OpenWrite("world.dat"), System.Text.Encoding.ASCII);
			Engine.araMain.Serialize(bw);
			bw.Close();
		}
	}
}

var ch_file_load = new ClickHandler() {
	onClick: function () {
		var br = new BinaryReader(File.OpenRead("world.dat"), System.Text.Encoding.ASCII);
		Engine.araMain = new Area(br);
		br.Close();
		Engine.pecMain.Clear();
		Engine.State = GameState.Active;
	}
}

var ch_file_exit = new ClickHandler() {
	onClick: function () {
		Engine.intMain.Quit();
	}
}

var ch_ed_mode = new ClickHandler() {
	onClick: function () {
		m_miEditorsMode.Checked = !m_miEditorsMode.Checked;
		Interface.EditMode = m_miEditorsMode.Checked;
	}
}

var ch_ed_part = new ClickHandler() {
	onClick: function () {
		Engine.guiMain.Add(new WPartEdit());
	}
}

var ch_opt_input = new ClickHandler() {
	onClick: function () {
		Engine.guiMain.Add(new WInput());
	}
}

/*public void client_read(PacketType t, MemoryStream data)
{
	Trace.WriteLine("Read Data, Type (" + t + ") Size: (" + data.Length + ")");
}*/

function autoexec()
{
	m_menuMain = new WMenu();

	m_miFile = new WMenuItem("File");

	m_miFile.AddItem(new WMenuItem("Single Player", ch_file_sp));
	m_miFile.AddItem(new WMenuItem("Multiplayer", ch_file_mp));
	m_miFile.AddItem(new WMenuItem("-"));
	m_miFile.AddItem(new WMenuItem("Save Current Map", ch_file_save));
	m_miFile.AddItem(new WMenuItem("Load Saved Map", ch_file_load));
	m_miFile.AddItem(new WMenuItem("-"));
	m_miFile.AddItem(new WMenuItem("Exit", ch_file_exit));

	m_miEditors = new WMenuItem("Editors");
	m_miEditorsMode = new WMenuItem("Editor Mode", ch_ed_mode);
	m_miEditorsPart = new WMenuItem("Particle Editor", ch_ed_part);
	m_miEditors.AddItem(m_miEditorsMode);
	m_miEditors.AddItem(m_miEditorsPart);

	m_miOptions = new WMenuItem("Options");
	m_miOptions.AddItem(new WMenuItem("Input Editor", ch_opt_input));

	m_menuMain.AddItem(m_miFile);
	m_menuMain.AddItem(m_miEditors);
	m_menuMain.AddItem(m_miOptions);
	Engine.guiMain.add(m_menuMain);

	//Packages.sdk.Engine.netMain.OnDataRead += new DataReadHandler(client_read);
}

var WPartEdit = new WWindow(new Rect(-1, -1, 348, 290), "Particle Editor") {
	WPartEdit2: function () {
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

		for (pe in Engine.pecMain)
		{
			pe = Engine.pecMain[pe];
			lvEmitters.Items.Add([ pe.ToString(), pe.X.ToString(), pe.Y.ToString() ]);
		}
	}
}