package sdk.item;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdk.Engine;
import sdk.types.Space;
import sdk.types.VertexD;

/**
 *
 * @author Xedecimal
 */
public class Rifle extends Item
{
	private Clip m_clip;

	public Rifle(Space parent) { super(parent); }

	@Override
	public void Use(int x, int y)
	{
		super.Use(x, y);
		if (m_clip != null)
		{
			Point target = Engine.araMain.ScreenToArea(x, y);
			VertexD source = new VertexD(owner.x, owner.y, owner.z);
			try {
				Space.ThrowEntity(m_clip.bullet.getClass().newInstance(),
					source, target, 1000);
			} catch (InstantiationException ex) {
				Logger.getLogger(Rifle.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				Logger.getLogger(Rifle.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public void Use(Item target)
	{
		super.Use(target);
		if (target.getClass().getName().compareTo("Clip") == 0)
			m_clip = (Clip)target;
	}
}