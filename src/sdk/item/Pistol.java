/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.item;

import java.awt.Point;
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
				// TODO: Fix me
				/*Space.ThrowEntity((Bullet)Activator.CreateInstance(
					m_clip.bullet.GetType(), new object[] { Space }),
					source, target, 1000);*/
			}
		}

		@Override
		public void Use(Item target)
		{
			if (target.getClass().getName().compareTo("Clip") == 0)
				m_clip = (Clip)target;
			super.Use(target);
		}
	}