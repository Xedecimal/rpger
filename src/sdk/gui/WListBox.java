/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.gui;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Interface;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 * An editbox, to collect information from the user.
 * @author Xedecimal
 */
public class WListBox extends Widget
{
	public ArrayList<String> Items = new ArrayList<String>();
	private int m_selected;

	public int Selected;

	public WListBox() { super(); Selected = 0; }
	public WListBox(Rect r) { super(r); Selected = 0; }
	public void Add(String text) { Items.add(text); }
	public boolean Contains(String text) { return Items.contains(text); }

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		GL11.glLoadIdentity();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glColor3f(0.25f, 0.25f, 0.25f);
		GL11.glVertex2d(offx+X, offy+bottom());
		GL11.glVertex2d(offx+X, offy+Y);
		GL11.glVertex2d(offx+right(), offy+Y);
		GL11.glColor3f(0.50f, 0.50f, 0.50f);
		GL11.glVertex2d(offx+right(), offy+bottom());
		GL11.glVertex2d(offx+X, offy+bottom());
		GL11.glEnd();

		if (Items.size() > 0)
		{
			if (m_selected > -1)
			{
				GL11.glColor3f(0.0f, 0.0f, 0.5f);
				Interface.DrawRect(new Rect(offx+X, offy+Y+(m_selected*16), Width, 16));
			}
			for (int iy = 0; iy < Items.size(); iy++)
			{
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
				Engine.intMain.DrawText(Items.get(iy), offx + X, offy + Y + (iy * 16));
			}
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}

	@Override
	public ClickResponse MouseDown(Object sender, int xp, int yp, byte buttons)
	{
		if (buttons == 1)
		{
			int item = yp / 16;
			if (item < Items.size()) m_selected = item;
		}
		return ClickResponse.Focus;
	}
}
