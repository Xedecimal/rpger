package sdk.types.particle;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Xedecimal
 */
public class ParticleEmitterCollection extends ArrayList<ParticleEmitter>
{
	public void Render(int offx, int offy)
	{
		for (int ix = size() - 1; ix >= 0; ix--)
		{
			if (get(ix).Duration > 0 && get(ix).Age > get(ix).Duration)
				remove(ix);
			else get(ix).Render(offx, offy);
		}
	}

	public void KillAll()
	{
		Iterator<ParticleEmitter> i = this.iterator();
		while (i.hasNext()) i.next().Dead = true;
	}
}
