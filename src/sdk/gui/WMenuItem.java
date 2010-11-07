/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.gui;

import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.RectD;
import sdk.types.RegionSet;

/**
 * A menu item which can hold sub items and more.
 * @author Xedecimal
 */
public class WMenuItem extends TextWidget
{
	private ArrayList<WMenuItem> Items;
	private boolean Expanded;
	public boolean Checked;
	public boolean Hover;
	private WMenuItem Hovered;
	/// <summary>
	/// Where the next menu item will be added.
	/// </summary>
	private int m_off;

	public WMenuItem get(int ix) { return Items.get(ix); }

	//public event ClickHandler OnClick;

	public WMenuItem(String text)
	{
		super();

		//Y offset to place sub menus.
		m_off = 16;
		Items = new ArrayList<WMenuItem>();
		Text = text;
		Width = (text.length()*12+16);
		Height = 16;
	}

	public WMenuItem(String text, ClickHandler handler)
	{
		this(text);
		//OnClick += handler;
	}

	public void AddItem(WMenuItem m)
	{
		m.Y = X+m_off;
		m_off += 16;
		Items.add(m);

		int maxwidth = 0;
		Iterator<WMenuItem> i = Items.iterator();
		while (i.hasNext())
		{
			WMenuItem mi = i.next();
			if (mi.Width > maxwidth) maxwidth = mi.Width;
		}
		i = Items.iterator();
		while (i.hasNext()) i.next().Width = maxwidth;
	}

	public void Expand()
	{
		Expanded = true;
		Height = (Items.size()+1) * 16;
		if (Items.size() > 0) Width = Items.get(0).Width;
	}

	public void Collapse()
	{
		if (Items.size() > 0) Width = (Text.length()*12)+16;
		Iterator<WMenuItem> i = Items.iterator();
		while (i.hasNext()) i.next().Collapse();
		Expanded = false;
	}

	public void Toggle()
	{
		if (Expanded) Collapse();
		else Expand();
	}

	public void Render(int offx, int offy, int reg, RegionSet RS)
	{
		if (!Hover) { GL11.glColor4d(1, 1, 1, 0.5); }
		if (Text.equals("-"))
		{
			RS.Draw(GUITiles.MENU_SL.ordinal(), new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(GUITiles.MENU_SM.ordinal(), new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(GUITiles.MENU_SR.ordinal(), new RectD(offx+right()-16, offy+Y, 16,   16), 1);
		}
		else if (reg == MenuType.TOP.ordinal())
		{
			RS.Draw(GUITiles.MENU_TL.ordinal(), new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(GUITiles.MENU_TM.ordinal(), new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(GUITiles.MENU_TR.ordinal(), new RectD(offx+right()-16, offy+Y, 16,   16), 1);
			Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);
		}
		else if (reg == MenuType.MIDDLE.ordinal())
		{
			RS.Draw(GUITiles.MENU_ML.ordinal(), new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(GUITiles.MENU_MM.ordinal(), new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(GUITiles.MENU_MR.ordinal(), new RectD(offx+right()-16, offy+Y, 16,   16), 1);
			Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);
		}
		else if (reg == MenuType.BOTTOM.ordinal())
		{
			RS.Draw(GUITiles.MENU_BL.ordinal(), new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(GUITiles.MENU_BM.ordinal(), new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(GUITiles.MENU_BR.ordinal(), new RectD(offx+right()-16, offy+Y, 16,   16), 1);
			Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);
		}
		else Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);

		if (Checked) RS.Draw(GUITiles.CHECK_ON.ordinal(), new RectD(offx+X, offy+Y, 16, 16), 1);
		if (!Hover) { GL11.glColor4d(1, 1, 1, 1); }
		if (Expanded)
		{
			for (int ix = 0; ix < Items.size(); ix++)
			{
				int r = 1;
				if (ix == 0) r = 0;
				if (ix == Items.size()-1) r = 2;
				this.get(ix).Render(X, offy, r, RS);
			}
		}
		super.Render(RS);
	}

	@Override
	public ClickResponse Blur()
	{
		Hovered.Hover = false;
		Hovered = null;
		Expanded = false;
		return ClickResponse.None;
	}

	@Override
	public ClickResponse MouseDown(Object sender, int xp, int yp, byte buttons)
	{
		if (yp > 16) //Clicked on subitem
		{
			ClickResponse ret = ((WMenuItem)Items.get((yp-16) / 16)).MouseDown(this, xp, yp%16, buttons);
			Collapse();
			return ret;
		}
		else Toggle(); //Clicked on actual menu.
		//@TODO Fix me
		/*if (OnClick != null)
		{
			OnClick(this, true);
			return ClickResponse.Close;
		}*/
		return ClickResponse.Focus;
	}

	@Override
	public boolean MouseMove(Object sender, int xp, int yp)
	{
		super.MouseMove(sender, xp, yp);
		//Hovered on subitem
		if (yp > 16)
		{
			Hover = false;
			WMenuItem mi = Items.get((yp - 16) / 16);
			if (mi.MouseMove(this, xp, yp % 16))
			{
				if (Hovered != null && Hovered != mi) Hovered.Hover = false;
				Hovered = mi;
				return true;
			}
		}
		else Hover = true;
		return true;
	}
}
