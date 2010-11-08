package sdk.item;

import sdk.types.Space;

/**
 *
 * @author Xedecimal
 */
public class Clip extends Item
{
	public Bullet bullet;
	private int count;

	public Clip(Space parent, int count)
	{
		super(parent);
		this.count = count;
		bullet = new Bullet(parent);
	}
}
