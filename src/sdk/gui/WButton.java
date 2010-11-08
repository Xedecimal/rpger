package sdk.gui;

import java.util.ArrayList;
import java.util.Iterator;
import sdk.Engine;
import sdk.types.Rect;
import sdk.types.RectD;
import sdk.types.RegionSet;

/**
 * A clickable button with an convenient event for the owner.
 * @author Xedecimal
 */
public class WButton extends TextWidget
{
	private boolean m_pressed;

	public ArrayList<ClickHandler> OnClick;

	/**
	 * Create a TextWidget out of a rect (r), setting the initial text to the
	 * parameter 'text' and a callback (e).
	 * @param r The initial position and size of this widget.
	 * @param text The initial text for this TextWidget.
	 * @param e A callback for when the button is depressed.
	 */
	public WButton(Rect r, String text, ClickHandler e)
	{
		super(r, text);
		OnClick = new ArrayList<ClickHandler>();
		OnClick.add(e);
	}

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		if (m_pressed)
		{
			RS.Draw(PBUTTON_L, new RectD(offx+X,     offy+Y, 16,   Height), 1);
			RS.Draw(PBUTTON_B, new RectD(offx+X+16,  offy+Y, Width-32, Height), 1);
			RS.Draw(PBUTTON_R, new RectD(offx+right()-16,  offy+Y, 16,   Height), 1);
			Engine.intMain.DrawText(Text, offx+X+3, offy+Y+1);
		}
		else
		{
			RS.Draw(BUTTON_L, new RectD(offx+X,     offy+Y, 16,   Height), 1);
			RS.Draw(BUTTON_B, new RectD(offx+X+16,  offy+Y, Width-32, Height), 1);
			RS.Draw(BUTTON_R, new RectD(offx+right()-16,  offy+Y, 16,   Height), 1);
			Engine.intMain.DrawText(Text, offx+X+2, offy+Y);
		}
	}

	@Override
	public ClickResponse MouseDown(Object sender, int x, int y, int buttons)
	{
		if (buttons == 1)
		{
			m_pressed = true;
			return ClickResponse.Focus;
		}
		return ClickResponse.None;
	}

	@Override
	public ClickResponse MouseUp(Object sender, int x, int y, int buttons)
	{
		if (buttons == 1 && this.ScreenRect().Contains(x, y))
		{
			if (OnClick != null)
			{
				Iterator<ClickHandler> it = OnClick.iterator();
				while (it.hasNext()) it.next().onClick(sender, true);
			}
			m_pressed = false;
			return ClickResponse.Focus;
		}
		return ClickResponse.None;
	}
}
