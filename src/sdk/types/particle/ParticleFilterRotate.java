package sdk.types.particle;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author Xedecimal
 */
public class ParticleFilterRotate implements IParticleFilter
{
	public void Apply(Particle p)
	{
		GL11.glRotatef((float)p.Life, 0.0f, 0.0f, 1.0f);
		GL11.glScaled((double)p.Life/1000+0.5, (double)p.Life/1000+0.5, 0);
	}
}
