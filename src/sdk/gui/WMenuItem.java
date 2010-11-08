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

	public ClickHandler OnClick;

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
		OnClick = handler;
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
			RS.Draw(MENU_SL, new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(MENU_SM, new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(MENU_SR, new RectD(offx+right()-16, offy+Y, 16,   16), 1);
		}
		else if (reg == MT_TOP)
		{
			RS.Draw(MENU_TL, new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(MENU_TM, new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(MENU_TR, new RectD(offx+right()-16, offy+Y, 16,   16), 1);
			Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);
		}
		else if (reg == MT_MIDDLE)
		{
			RS.Draw(MENU_ML, new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(MENU_MM, new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(MENU_MR, new RectD(offx+right()-16, offy+Y, 16,   16), 1);
			Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);
		}
		else if (reg == MT_BOTTOM)
		{
			RS.Draw(MENU_BL, new RectD(offx+X,    offy+Y, 16,   16), 1);
			RS.Draw(MENU_BM, new RectD(offx+X+16, offy+Y, Width-32, 16), 1);
			RS.Draw(MENU_BR, new RectD(offx+right()-16, offy+Y, 16,   16), 1);
			Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);
		}
		else Engine.intMain.DrawText(Text, offx + X + 16, offy + Y);

		if (Checked) RS.Draw(CHECK_ON, new RectD(offx+X, offy+Y, 16, 16), 1);
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
	public ClickResponse MouseDown(Object sender, int xp, int yp, int buttons)
	{
		if (yp > 16) //Clicked on subitem
		{
			ClickResponse ret = ((WMenuItem)Items.get((yp-16) / 16)).MouseDown(this, xp, yp%16, buttons);
			Collapse();
			return ret;
		}
		else Toggle(); //Clicked on actual menu.

		if (OnClick != null)
		{
			OnClick.onClick(this, true);
			return ClickResponse.Close;
		}
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

	/** Menu Types */
	public static final int MT_TOP = 0;
	public static final int MT_MIDDLE = 1;
	public static final int MT_BOTTOM = 2;
	public static final int MT_MAIN = 3;
}
