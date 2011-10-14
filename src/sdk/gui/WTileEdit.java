package sdk.gui;

import sdk.Engine;
import sdk.types.Rect;
import sdk.types.RegionSet;
import sdk.world.Area;
import sdk.world.Tile;

/**
 *
 * @author Xedecimal
 */
public class WTileEdit extends WWindow
{
	//private Point m_coord;
	private WImage imgTile;
	private WLabel lblHeight;
	private Tile m_tile;
	private RegionSet m_rs;

	public WTileEdit(int x, int y)
	{
		super(new Rect(-1, -1, 350, 380), "Tile Editor");
		//m_coord = new Point(p.X, p.Y);
		if (Engine.araMain == null) return;
		m_rs = Engine.regMain.GetRegions("tiles");
		Area a = Engine.araMain;
		m_tile = Engine.intMain.ScreenToAreaTile(x, y);
		imgTile = new WImage(new Rect(16, 16, 64, 64), m_rs.Texture, m_rs.get(m_tile.ID));
		Add(imgTile);

		lblHeight = new WLabel(new Rect(16, 70, 100, 18));
		Add(lblHeight);
		updateHeight();

		Add(new WButton(new Rect(Width-150,  0, 90, 16), "Next", new ButAdd()));
		Add(new WButton(new Rect(Width-150, 20, 90, 16), "Previous", new ButSub()));
		Add(new WButton(new Rect(Width-150, 40, 90, 16), "Up", new ButUp()));
		Add(new WButton(new Rect(Width-150, 60, 90, 16), "Down", new ButDown()));
	}

	private void updateHeight()
	{
		lblHeight.Text = "Count FIXME!";
	}

	private class ButAdd implements ClickHandler
	{
		public void onClick(Object sender, boolean state)
		{
			if (++m_tile.ID > m_rs.size()-1) m_tile.ID = 0;
			imgTile.Reg = m_rs.get(m_tile.ID);
		}
	}

	private class ButSub implements ClickHandler
	{
		public void onClick(Object sender, boolean state)
		{
			if (m_tile.ID == 0) m_tile.ID = (short)(m_rs.size()-1);
			else m_tile.ID--;
			imgTile.Reg = m_rs.get(m_tile.ID);
		}
	}

	private class ButUp implements ClickHandler
	{
		public void onClick(Object sender, boolean state) {
			m_tile.Push(new Tile(Engine.araMain, m_tile.ID, false));
			updateHeight();
		}
	}

	private class ButDown implements ClickHandler
	{
		public void onClick(Object sender, boolean state) {
			m_tile.Pop();
			updateHeight();
		}
	}
}
