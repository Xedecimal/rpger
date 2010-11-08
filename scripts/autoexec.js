function autoexec()
{
	m_menuMain = new Packages.sdk.gui.WMenu();

	m_miFile = new Packages.sdk.gui.WMenuItem("File");

	m_miFile.AddItem(new Packages.sdk.gui.WMenuItem("Single Player", new Packages.sdk.gui.ClickHandler() {
	onClick: function () {
		print("Test.");
	}
}));

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

