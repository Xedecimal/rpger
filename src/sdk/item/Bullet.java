package sdk.item;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import sdk.Engine;
import sdk.types.Interface;
import sdk.types.RectD;
import sdk.types.Space;

/**
 *
 * @author Xedecimal
 */
public class Bullet extends Item implements Cloneable
{
	private Texture m_tex;

	public Bullet(Space parent)
	{
		super(parent);
		m_tex = Engine.intMain.GetSurface("img/bullet.png");
		weight = 0.01f;
	}

	@Override
	public void Update(int offx, int offy)
	{
		super.Update(offx, offy);
		GL11.glLoadIdentity();
		GL11.glTranslated(x-offx, y-offy, 0);
		float degs = (float)(angle * (180 / Math.PI));
		GL11.glRotatef(degs, 0, 0, 1);
		Interface.Draw(m_tex, new RectD(0, 0, 1, 1), new RectD(-8, -8, 16, 16), 1);
		GL11.glLoadIdentity();
	}

	@Override
	public void Break()
	{
		super.Break();
		Space.Objects.remove(this);
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
