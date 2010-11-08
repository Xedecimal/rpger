package sdk.gui;

import java.util.ArrayList;
import java.util.Iterator;
import sdk.Engine;
import sdk.types.Rect;
import sdk.types.RectD;
import sdk.types.RegionSet;

/**
 *
 * @author Xedecimal
 */
public class WGroupBox extends TextWidget
{
	public ArrayList<Widget> Widgets;
	private Widget m_focus;

	//public int xoff { get { return X; } }
	//public int yoff { get { return Y; } }

	public WGroupBox(Rect r, String text)
	{
		super(r, text);
		Widgets = new ArrayList<Widget>();
	}

	@Override
	public ClickResponse MouseDown(Object sender, int xp, int yp, int buttons)
	{
		Iterator<Widget> i = Widgets.iterator();
		while (i.hasNext())
		{
			Widget wig = i.next();
			if (wig.Contains(xp-X, yp-Y))
			{
				m_focus = wig;
				return wig.MouseDown(this, xp-X, yp-Y, buttons);
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
		RS.Draw(GROUP_TL, new RectD(offx+X,        offy+Y+16,      16,       16), 1);
		RS.Draw(GROUP_TM, new RectD(offx+X+16,     offy+Y+16,      Width-32, 16), 1);
		RS.Draw(GROUP_TR, new RectD(offx+right()-16, offy+Y+16,      16,       16), 1);

		RS.Draw(GROUP_ML, new RectD(offx+X,        offy+Y+32,      16,       Height-48), 1);
		RS.Draw(GROUP_MM, new RectD(offx+X+16,     offy+Y+32,      Width-32, Height-48), 1);
		RS.Draw(GROUP_MR, new RectD(offx+right()-16, offy+Y+32,      16,       Height-48), 1);

		RS.Draw(GROUP_BL, new RectD(offx+X,        offy+bottom()-16, 16,       16), 1);
		RS.Draw(GROUP_BM, new RectD(offx+X+16,     offy+bottom()-16, Width-32, 16), 1);
		RS.Draw(GROUP_BR, new RectD(offx+right()-16, offy+bottom()-16, 16,       16), 1);

		Engine.intMain.DrawText(Text, offx + X + 10, offy + Y);
		Iterator<Widget> i = Widgets.iterator();
		while (i.hasNext()) i.next().Render(offx+X, offy+Y, RS);
	}

	@Override
	public boolean KeyPress(char key)
	{
		if (m_focus != null) return m_focus.KeyPress(key);
		return false;
	}
}
