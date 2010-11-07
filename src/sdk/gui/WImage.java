package sdk.gui;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import sdk.types.Interface;
import sdk.types.Rect;
import sdk.types.RectD;
import sdk.types.RegionSet;

/**
 *
 * @author Xedecimal
 */
public class WImage extends Widget
{
	Texture m_tex;
	RectD m_reg;

	public int Texture;
	public RectD Reg;

	public WImage(Rect r) { super(r); }

	public WImage(Rect r, Texture tex, RectD reg)
	{
		super(r);
		m_tex = tex;
		m_reg = reg;
	}

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		GL11.glLoadIdentity();
		Interface.Draw(m_tex, m_reg, new RectD(offx+X, offy+Y, Width, Height), 1);
	}
}
