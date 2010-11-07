package sdk.gui;

import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 * To simply display text on a WWindow.
 * @author Xedecimal
 */
public class WLabel extends TextWidget
{
	public WLabel(Rect r) { super(r); }
	public WLabel(Rect r, String text) { super(r, text); }

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		GL11.glLoadIdentity();
		Engine.intMain.DrawText(Text, offx+X, offy+Y);
	}
}
