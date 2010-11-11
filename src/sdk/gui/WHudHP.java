/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.gui;

import org.lwjgl.opengl.GL11;
import sdk.types.Interface;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 *
 * @author Xedecimal
 */
public class WHudHP extends Widget
{
	public WHudHP(Rect r)
	{
		super(r);
	}

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Interface.DrawRect(this);
		GL11.glColor3d(0, 0, 0);
		Interface.DrawRect(new Rect(X+1, Y+1, Width-2, Height-2));
		GL11.glPopAttrib();
	}
}
