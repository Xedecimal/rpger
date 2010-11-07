package sdk.types.particle;

/**
 *
 * @author Xedecimal
 */
public class ParticleFilterFade implements IParticleFilter
{
	private float m_a, m_r, m_g, m_b;
	public ParticleFilterFade(float astep, float rstep, float gstep, float bstep)
	{
		m_a = astep;
		m_r = rstep;
		m_g = gstep;
		m_b = bstep;
	}

	public void Apply(Particle p)
	{
		p.A += m_a;
		p.R += m_r;
		p.G += m_g;
		p.B += m_b;
	}
}
