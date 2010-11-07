/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.types.item;

import sdk.types.Entity;
import sdk.types.Space;
import sdk.types.player.Player;

/**
 *
 * @author Xedecimal
 */
public class Item extends Entity
{
	public Player owner;

	public Item(Space parent) { super(parent); }

	public void Remove() { }

	public void Use(int x, int y) { }
	public void Use(Item target) { }
}
