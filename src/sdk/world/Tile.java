package sdk.world;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import sdk.Engine;
import sdk.types.Entity;
import sdk.types.RPointD;
import sdk.types.RectD;
import sdk.types.RegionSet;
import sdk.types.Space;
import sdk.types.particle.ParticleEmitter;
import sdk.types.particle.ParticleFilterFade;
import sdk.types.particle.ParticleFilterRotate;

/**
 *
 * @author Xedecimal
 */
public class Tile extends Entity
{
	public short ID;
	public Tile next;
	public boolean Block, Destroyed;
	public int Tolerance = 30;
	public boolean Destroying = false;
	public double offset = 0;

	public int count() { return next != null ? next.count() + 1 : 0; }

	public Tile(Space parent) { super(parent); }

	public Tile(Space parent, short id, boolean block)
	{
		this(parent);
		this.ID = id;
		this.Block = block;
	}

	public Tile(Space parent, ByteBuffer bb) throws IOException
	{
		this(parent);
		ID = bb.getShort();
		Block = bb.get() == 1;
	}

	public void Serialize(ByteBuffer bb) throws IOException
	{
		bb.putInt(count());
		bb.putShort(ID);
		bb.put((byte)(Block ? 1 : 0));
	}

	public void Draw(RegionSet rs, RectD rdst, double zorder)
	{
		rs.Draw(ID, rdst.GetOffset(0, (int)offset), zorder);
		//Engine.intMain.DrawText(zorder.ToString(), (int)rdst.x, (int)rdst.y);
		if (Destroyed) { damage = 0; Destroying = false; Destroyed = false; offset = 0; }
		else if (Destroying)
		{
			offset += 0.1;
			if (offset >= 16)
			{
				Point p = Engine.araMain.ScreenToArea((int)rdst.x, (int)rdst.y);
				ParticleEmitter pe = new ParticleEmitter(p.x, p.y, 10, 50, new RPointD(0, -2f), "particle_fire");
				pe.Spread = new RPointD(16, 0);
				pe.Duration = 3;
				pe.a = 1;
				pe.r = 1;
				pe.g = 1;
				pe.b = 0;
				pe.Filters.add(new ParticleFilterRotate());
				pe.Filters.add(new ParticleFilterFade(-0.05F, -0.005f, -0.005f, 0));
				Engine.araMain.Emitters.add(pe);
				Destroyed = true;
			}
		}
		if (next != null)
		{
			if (next.Destroyed) { next = null; return; }
			rdst.Offset(0, -32);
			next.Draw(rs, rdst, zorder + 0.001);
			rdst.Offset(0, 32);
		}
	}

	public Tile GetStack(int z)
	{
		if (z == 0) return this;
		if (next != null) return next.GetStack(z - 1);
		return null;
	}

	@Override
	public void Break()
	{
		super.Break();
		if (next != null) next.Damage(30);
		Destroying = true;
	}

	public void Push(Tile t)
	{
		if (next != null) next.Push(t);
		else
		{
			next = t;
			t.x = x;
			t.y = y;
			t.z = z - 16;
		}
	}

	public void Pop()
	{
		if (next == null) return;
		if (next.next == null) next = null;
		else next.Pop();
	}
}