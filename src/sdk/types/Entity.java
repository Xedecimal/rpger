package sdk.types;

import java.util.EnumSet;
import sdk.Engine;

/**
 * An entity, affected by physics and global environments.
 * @author Xedecimal
 */
public class Entity extends RectD
{
	public static final int DIR_NONE = 0;
	public static final int DIR_N = 1;
	public static final int DIR_E = 2;
	public static final int DIR_S = 4;
	public static final int DIR_W = 8;

	public int vel;
	public int Dir;
	public double angle;
	public float weight, damage, tolerance;
	public boolean remove;
	public Space Space;
	public double z;

	public Entity(Space parent)
	{
		Space = parent;
		vel = 1;
		Dir = DIR_NONE;
	}

	public void Update(int offx, int offy)
	{
		if (angle != 0)
		{
			x += (float)Math.cos(angle) * Engine.Delta * vel;
			y += (float)Math.sin(angle) * Engine.Delta * vel;
			z -= weight;
		}
		else
		{
			float m_deltaposx = vel * Engine.Delta;
			float m_deltaposy = vel * Engine.Delta / 2;

			if (Dir != DIR_NONE && Engine.araMain != null)
			{
				if ((Dir & DIR_N) == DIR_N) y -= m_deltaposy;
				if ((Dir & DIR_E) == DIR_E) x += m_deltaposx;
				if ((Dir & DIR_S) == DIR_S) y += m_deltaposy;
				if ((Dir & DIR_W) == DIR_W) x -= m_deltaposx;
			}
		}
	}

	/**
	 * Sets the position of this entity. (Pixel relative)
	 * @param x X position
	 * @param y Y position
	 * @param z Z position
	 */
	public void Move(double x, double y, double z)
	{
			this.x = x;
			this.y = y;
			this.z = z;
		}

	public void Damage(float damage)
	{
			if ((damage += damage) >= tolerance)
			{
				this.Break();
			}
		}

	public void Break() { }
}
