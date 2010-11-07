package sdk.types.particle;

import sdk.types.RPointD;

/**
 *
 * @author Xedecimal
 */
public class Particle
{
	public int Reg, Life;
	public double X, Y;
	public RPointD Vel;
	public float A, R, G, B;

	public Particle(ParticleEmitter parent, float x, float y, byte life, RPointD vel, float a, float r, float g, float b)
	{
		A = a;
		R = r;
		G = g;
		B = b;
		X = x;
		Y = y;
		Life = life;
		Vel = vel;
	}
}
