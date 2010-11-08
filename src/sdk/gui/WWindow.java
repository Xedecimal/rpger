package sdk.gui;

import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Rect;
import sdk.types.RectD;
import sdk.types.RegionSet;

/**
 * A standard window with a titlebar, that can be opened, closed, moved, and
 * contain widgets.
 * @author Xedecimal
 */
public class WWindow extends TextWidget
{
	public ArrayList<Widget> Widgets;
	private Widget m_focus = null;

	public int xoff() { return X + 10; }
	public int yoff() { return Y + 32; }

	public WWindow(Rect r, String title)
	{
		super(r, title);
		Widgets = new ArrayList<Widget>();
	}

	public WWindow(Rect r, String title, Object obj) { this(r, title); }

	public void Add(Widget w)
	{
		w.Parent = this;
		Widgets.add(w);
	}

	@Override
	public ClickResponse MouseDown(Object sender, int xp, int yp, int buttons)
	{
		//Check for close button.
		if (xp > right()-16 && xp < right() && yp > Y && yp < Y+16) return ClickResponse.Close;
		//Check for titlebar drag
		if (xp > X && xp < right()-20 && yp > Y && yp < Y+16) return ClickResponse.Drag;

		Iterator<Widget> i = Widgets.iterator();
		while (i.hasNext())
		{
			Widget wig = i.next();
			if (wig.Contains(xp-xoff(), yp-yoff()))
			{
				m_focus = wig;
				return wig.MouseDown(this, xp-xoff(), yp-yoff(), buttons);
			}
		}
		return super.MouseDown(sender, xp, yp, buttons);
	}

	@Override
	public ClickResponse MouseUp(Object sender, int xp, int yp, int buttons)
	{
		if (m_focus != null) m_focus.MouseUp(this, xp, yp, buttons);
		return super.MouseUp(sender, X, Y, buttons);
	}

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
		GL11.glLoadIdentity();
		RS.Draw(WINDOW_TTL, new RectD(offx+X,        offy+Y,         16,       16), 1);
		RS.Draw(WINDOW_TTM, new RectD(offx+X+16,     offy+Y,         Width-32, 16), 1);
		RS.Draw(WINDOW_TTR, new RectD(offx+right()-16, offy+Y,         16,       16), 1);
		RS.Draw(WINDOW_TCC, new RectD(offx+right()-16, offy+Y,         16,       16), 1);

		RS.Draw(WINDOW_BTL, new RectD(offx+X,        offy+Y+16,      16,       16), 1);
		RS.Draw(WINDOW_BTM, new RectD(offx+X+16,     offy+Y+16,      Width-32, 16), 1);
		RS.Draw(WINDOW_BTR, new RectD(offx+right()-16, offy+Y+16,      16,       16), 1);

		RS.Draw(WINDOW_BML, new RectD(offx+X,        offy+Y+32,      16,       Height-48), 1);
		RS.Draw(WINDOW_B,   new RectD(offx+X+16,     offy+Y+32,      Width-32, Height-48), 1);
		RS.Draw(WINDOW_BMR, new RectD(offx+right()-16, offy+Y+32,      16,       Height-48), 1);

		RS.Draw(WINDOW_BBL, new RectD(offx+X,        offy+bottom()-16, 16,       16), 1);
		RS.Draw(WINDOW_BBM, new RectD(offx+X+16,     offy+bottom()-16, Width-32, 16), 1);
		RS.Draw(WINDOW_BBR, new RectD(offx+right()-16, offy+bottom()-16, 16,       16), 1);

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Engine.intMain.DrawText(Text, offx + X + 5, offy + Y + 1);

		Iterator<Widget> i = Widgets.iterator();
		while (i.hasNext()) i.next().Render(offx+xoff(), offy+yoff(), RS);
	}

	@Override
	public boolean KeyPress(int key)
	{
		if (m_focus != null) return m_focus.KeyPress(key);
		return false;
	}
}
