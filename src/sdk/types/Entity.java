package sdk.types;

import java.util.EnumSet;
import sdk.Engine;

/**
 * An entity, affected by physics and global environments.
 * @author Xedecimal
 */
public class Entity extends RectD
{
	public int vel;
	public EnumSet<Direction> Dir;
	public double angle;
	public float weight, damage, tolerance;
	public boolean remove;
	public Space Space;
	public double z;

	public Entity(Space parent)
	{
		Space = parent;
		vel = 1;
		Dir = EnumSet.of(Direction.None);
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

			if (Engine.araMain != null)
			{
				if (Dir.contains(Direction.North)) y -= m_deltaposy;
				if (Dir.contains(Direction.East)) x += m_deltaposx;
				if (Dir.contains(Direction.South)) y += m_deltaposy;
				if (Dir.contains(Direction.West)) x -= m_deltaposx;
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
