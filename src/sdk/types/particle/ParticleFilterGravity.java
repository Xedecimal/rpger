/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.types.particle;

import sdk.types.RPointD;

/**
 *
 * @author Xedecimal
 */
public class ParticleFilterGravity implements IParticleFilter
{
	public RPointD m_amount;

	public ParticleFilterGravity(RPointD amount)
	{
		m_amount = amount;
	}

	public void Apply(Particle p)
	{
		p.Vel.y -= m_amount.y;
		p.Vel.x -= m_amount.x;
	}
}