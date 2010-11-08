importPackage(Packages.sdk);
importPackage(Packages.sdk.gui);
importPackage(Packages.sdk.player);
importPackage(Packages.sdk.types);
importPackage(Packages.sdk.item);
importPackage(Packages.sdk.world);

var ch_file_sp = new Packages.sdk.gui.ClickHandler() {
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

function autoexec()
{
	m_menuMain = new WMenu();

	m_miFile = new WMenuItem("File");

	m_miFile.AddItem(new WMenuItem("Single Player", ch_file_sp));

	m_miFile.AddItem(new WMenuItem("Multiplayer"));
	m_miFile.AddItem(new WMenuItem("-"));
	m_miFile.AddItem(new WMenuItem("Save Current Map"));
	m_miFile.AddItem(new WMenuItem("Load Current Map"));
	m_miFile.AddItem(new WMenuItem("-"));
	m_miFile.AddItem(new WMenuItem("Exit"));

	m_miEditors = new WMenuItem("Editors");
	m_miEditorsMode = new WMenuItem("Editor Mode");
	m_miEditorsPart = new WMenuItem("Particle Editor");
	m_miEditors.AddItem(m_miEditorsMode);
	m_miEditors.AddItem(m_miEditorsPart);

	m_miOptions = new WMenuItem("Options");
	m_miOptions.AddItem(new WMenuItem("Input Editor"));

	m_menuMain.AddItem(m_miFile);
	m_menuMain.AddItem(m_miEditors);
	m_menuMain.AddItem(m_miOptions);
	Engine.guiMain.add(m_menuMain);

	//Packages.sdk.Engine.netMain.OnDataRead += new DataReadHandler(client_read);
}

