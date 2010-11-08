package sdk.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import sdk.types.Interface;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 * @todo Rather incomplete, but coming along ok.
 * @author Xedecimal
 */
public class WListView extends Widget
{
	public ArrayList<WListViewHeader> Headers = new ArrayList<WListViewHeader>();
	public WListViewItemCollection Items = new WListViewItemCollection();
	public int Selected = -1;
	
	public WListView() { super(); }
	public WListView(Rect r) { super(r); }

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		//Border
		GL11.glLoadIdentity();
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

		//Draw selection rectangle.
		if (Selected > -1)
		{
			GL11.glColor3f(0.0f, 0.0f, 0.5f);
			Interface.DrawRect(new Rect(offx+X, offy+Y+((Selected+1)*16), Width, 16));
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		//Headers
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslated(offx+X, offy+Y, 0);

		GL11.glPushMatrix();
		Iterator<WListViewHeader> hi = Headers.iterator();
		while (hi.hasNext())
		{
			WListViewHeader wlvh = hi.next();
			try {
				Interface.DrawText(wlvh.Text);
			} catch (IOException ex) {
				Logger.getLogger(WListView.class.getName()).log(Level.SEVERE, null, ex);
			}
			GL11.glTranslated(wlvh.Width, 0, 0);
		}
		GL11.glPopMatrix();

		//Items
		Iterator<WListViewItem> ii = Items.iterator();
		while (ii.hasNext())
		{
			WListViewItem wlvi = ii.next();
			GL11.glTranslated(0, 16, 0);
			GL11.glPushMatrix();

			for (int ix = 0; ix < wlvi.Cols.length; ix++)
			{
				try {
					Interface.DrawText(wlvi.Cols[ix]);
				} catch (IOException ex) {
					Logger.getLogger(WListView.class.getName()).log(Level.SEVERE, null, ex);
				}
				if (ix < Headers.size())
					GL11.glTranslated(Headers.get(ix).Width, 0, 0);
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

	@Override
	public ClickResponse MouseDown(Object sender, int xp, int yp, int buttons)
	{
		if (buttons == 1)
		{
			if (yp < 16) //Hit a header.
			{
				int ix = 0;
				Iterator<WListViewHeader> i = Headers.iterator();
				while (i.hasNext())
				{
					WListViewHeader head = i.next();
					if (xp < (ix += head.Width))
					{
						Items.Sort(Headers.indexOf(head));
						break;
					}
				}
			}
			else //Hit an item.
			{
				int item = (yp-16) / 16;
				if (item < Items.size()) Selected = item;
			}
		}
		return ClickResponse.Focus;
	}
}
