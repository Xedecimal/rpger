/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.gui;

import sdk.types.Interface;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 *
 * @author Xedecimal
 */
public class WHudHP extends Widget
{
	public WHudHP(Rect r)
	{
		super(r);
	}

	@Override
	public void Render(int offx, int offy, RegionSet RS)
	{
		Interface.DrawRect(new Rect(offx+X, offy+Y, Width, Height));
	}
}
