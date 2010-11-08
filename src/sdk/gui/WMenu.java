package sdk.gui;

import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Interface;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 *
 * @author Xedecimal
 */
public class WMenu extends Widget
{
	private ArrayList<WMenuItem>	m_items;
	private Widget			m_parent;
	private int				m_curoffset;
	/// <summary> Currently expanded item. </summary>
	private WMenuItem		m_active;
	private WMenuItem		m_hovered;

	public WMenuItem get(int x) { return m_items.get(x); }

	public WMenu()
	{
		super(new Rect(0, 0, Engine.intMain.Width, 16));
		m_items = new ArrayList<WMenuItem>();
		m_curoffset = 0;
		if (m_parent != null) Width = m_parent.Width;
		else Width = Engine.intMain.Width;
	}

	public WMenu(Widget parent)
	{
		this();
		m_parent = parent;
	}

	public WMenu(int height)
	{
		this();
		Height = height;
	}

	public void AddItem(WMenuItem item)
	{
		item.X = m_curoffset;
		m_items.add(item);
		m_curoffset += item.Text.length() * 12 + 20;
	}

	public void Collapse()
	{
		Iterator<WMenuItem> i = m_items.iterator();
		while (i.hasNext()) i.next().Collapse();
	}

	@Override
	public ClickResponse MouseDown(Object sender, int xp, int yp, int buttons)
	{
		// Left Button
		if (buttons == 0)
		{
			Iterator<WMenuItem> i = m_items.iterator();
			while (i.hasNext())
			{
				WMenuItem mi = i.next();
				if (mi.Contains(xp, yp))
				{
					if (m_active != null && m_active != mi) m_active.Collapse();
					m_active = mi;
					ClickResponse ret = mi.MouseDown(this, xp, yp, buttons);
					if (ret == ClickResponse.Focus) Height = mi.Height;
					else Height = 16;
				}
			}
		}
		return ClickResponse.Focus;
	}

	@Override
	public boolean MouseMove(Object sender, int xp, int yp)
	{
		Iterator<WMenuItem> i = m_items.iterator();
		while (i.hasNext())
		{
			WMenuItem mi = i.next();
			if (mi.Contains(xp, yp))
			{
				if (mi.MouseMove(this, xp, yp))
				{
					if (m_hovered != null && m_hovered != mi) m_hovered.Hover = false;
					m_hovered = mi;
					return true;
				}
			}
		}
		return super.MouseEnter(sender, xp, yp);
	}

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0.5f, 0.5f, 0.5f, 1);
		Interface.DrawRect(new Rect(X, Y, Width, 16));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 1, 1, 1);
		Iterator<WMenuItem> i = m_items.iterator();
		while (i.hasNext()) i.next().Render(0, 0, MenuType.MAIN.ordinal(), RS);
	}

	@Override
	public ClickResponse Blur()
	{
		Iterator<WMenuItem> i = m_items.iterator();
		while (i.hasNext()) i.next().Collapse();
		Height = 16;
		if (m_hovered != null) m_hovered.Hover = false;
		return ClickResponse.None;
	}
}
