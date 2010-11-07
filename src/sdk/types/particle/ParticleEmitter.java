package sdk.types.particle;

import java.io.IOException;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Entity;
import sdk.types.RPointD;
import sdk.types.RectD;
import sdk.types.RegionSet;

/**
 *
 * @author Xedecimal
 */
public class ParticleEmitter
{
	RegionSet m_rs;
	RPointD m_vel;
	public float X, Y, Life;
	public RPointD Spread;
	Particle[] m_parts;
	public ArrayList<IParticleFilter> Filters;
	public boolean Dead;
	public float a, r, g, b;
	public int Duration;
	public float Age;
	public Entity Parent;

	public ParticleEmitter(float x, float y, int count, int life, RPointD velocity, String rs)
	{
		Filters = new ArrayList<IParticleFilter>();
		m_rs = new RegionSet(rs);
		m_parts = new Particle[count];
		X = x;
		Y = y;
		Life = life;
		Spread = new RPointD();
		m_vel = velocity;
		Dead = false;

		int offx = (int)(Engine.intMain.CameraX + X);
		int offy = (int)(Engine.intMain.CameraY + Y);
		for (int ix = 0; ix < m_parts.length; ix++)
		{
			m_parts[ix] = new Particle(this,
				(float)(Engine.r.nextFloat() * (X - Spread.x) + (X + Spread.x)),
				(float)(Engine.r.nextFloat() * (Y - Spread.y) + (Y + Spread.y)),
				(byte)Engine.r.nextInt((int)Life),
				m_vel, a, r, g, b);
		}
	}

	public ParticleEmitter(int x, int y, int count, String rs) throws IOException
	{
		this(x, y, count, 100, new RPointD(100, 100), rs);
	}

	public void Render(int offx, int offy)
	{
		if (Parent != null) { X = (int)Parent.x + 32; Y = (int)Parent.y + 32; }
		GL11.glLoadIdentity();
		Age += Engine.Delta;
		RectD rd = new RectD(0, 0, 64, 64);
		for (int ix = 0; ix < m_parts.length; ix++)
		{
			Particle p = m_parts[ix];
			if (p.Life-- < 1)
			{
				p.X = Engine.Random((long)(X - Spread.x), (long)(X + Spread.x));
				p.Y = Engine.Random((long)(Y - Spread.y), (long)(Y + Spread.y));
				p.Life = Engine.r.nextInt((int)Life);
				p.Vel = m_vel;
				p.Reg = Engine.r.nextInt(m_rs.size());
				p.A = (byte)a;
				p.R = (byte)r;
				p.G = (byte)g;
				p.B = (byte)b;
				continue;
			}
			GL11.glPushMatrix();
			p.Y += p.Vel.y;
			p.X += p.Vel.x;
			GL11.glTranslated(p.X-offx, p.Y-offy, 1f);
			for (int fcount = 0; fcount < Filters.size(); fcount++)
				Filters.get(fcount).Apply(p);
			GL11.glColor4f(p.R, p.G, p.B, p.A);
			m_rs.Draw(p.Reg, rd, 0);
			GL11.glPopMatrix();
		}
	}

	public String ToString()
	{
		return "Emitter";
	}
}
