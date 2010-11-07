package sdk.types;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xedecimal
 */
public abstract class Space {
	public List<Entity> Objects;

	public int w;
	public int h;
	public int d;
	public abstract ImpactResult CheckImpact(Entity source);

	public Space() { Objects = new ArrayList<Entity>(); }

	public void ThrowEntity(Entity i, VertexD source, Point dest, int velocity)
	{
		i.vel = velocity;
		i.angle = Math.atan2(dest.y - source.y, dest.x - source.x);
		i.Move(source.x, source.y, source.z);
		Objects.add(i);
	}

	void Remove(Entity e)
	{
		Objects.remove(e);
	}
}
