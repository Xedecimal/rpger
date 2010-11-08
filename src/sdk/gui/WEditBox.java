package sdk.gui;

import sdk.Engine;
import sdk.types.Rect;
import sdk.types.RectD;
import sdk.types.RegionSet;

/**
 * An edit box, to collect information from the user.
 * @author Xedecimal
 */
public class WEditBox extends TextWidget
{
	public boolean Masked;

	public WEditBox() { super(); }
	public WEditBox(Rect r) { super(r); }
	public WEditBox(Rect r, String text) { this(r); Text = text; }
	public WEditBox(Rect r, String text, boolean masked)
	{
		this(r, text);
		Masked = true;
	}

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		RS.Draw(GUITiles.EBOX_L.ordinal(), new RectD(offx+X,     offy+Y, 16,   Height), 1);
		RS.Draw(GUITiles.EBOX_M.ordinal(), new RectD(offx+X+16,  offy+Y, Width-32, Height), 1);
		RS.Draw(GUITiles.EBOX_R.ordinal(), new RectD(offx+right()-16,  offy+Y, 16,   Height), 1);

		if (Masked) Engine.intMain.DrawText(String.format(String.format(
			"%%0%dd", Text.length()), 0).replace("0","*"),
			offx + X, offy + Y + 2);
		else Engine.intMain.DrawText(Text, offx + X, offy + Y + 2);
	}

	@Override
	public ClickResponse MouseDown(Object sender, int xp, int yp, int buttons)
	{
		if (buttons == 1) return ClickResponse.Focus;
		return ClickResponse.None;
	}

	@Override
	public boolean KeyPress(int key)
	{
		switch (key)
		{
			case 8: //Backspace
				if (Text.length() > 0) Text = Text.substring(0, Text.length() - 1);
				break;
			case 13: //Enter
				break;
			default: //Readable
				Text += (char)key;
				break;
		}
		return true;
	}
}
