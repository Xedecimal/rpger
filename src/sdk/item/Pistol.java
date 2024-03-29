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
	public class Pistol extends Item
	{
		private Clip m_clip;

		public Pistol(Space parent) { super(parent); }

		@Override
		public void Use(int x, int y)
		{
			super.Use(x, y);
			if (m_clip != null)
			{
				Point target = Engine.araMain.ScreenToArea(x, y);
				VertexD source = new VertexD(owner.x, owner.y, owner.z);
			try {
				Space.ThrowEntity((Bullet) m_clip.bullet.clone(), source, target, 1000);
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(Pistol.class.getName()).log(Level.SEVERE, null, ex);
			}
			}
		}

		@Override
		public void Use(Item target)
		{
			if (target.getClass().getName().compareTo("sdk.item.Clip") == 0)
				m_clip = (Clip)target;
			super.Use(target);
		}
	}