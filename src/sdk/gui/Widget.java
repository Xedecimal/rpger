/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.gui;

import sdk.Engine;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 * A single GUI element, at it's most primitive form.
 * @author Xedecimal
 */
public class Widget extends Rect
{
	public WWindow Parent = null;

	public Rect ScreenRect()
	{
		if (Parent != null)
			return new Rect(Parent.xoff()+X, Parent.yoff()+Y, Width, Height);
		else return this;
	}

	public Widget()
	{
		X = Y = Width = Height = 0;
	}

	/**
	 * Initializes this widget with the coordinates and size of the specified
	 * Rect.
	 * -1 for X or Y coordinates centers this widget independant of resolution.
	 * -2 for X or Y coordinates places this widget on the right or bottom
	 * respectively independant of resolution.
	 * @param r Initial size of this object.
	 */
	public Widget(Rect r)
	{
		super(r);
		if (r.X == -1) X = (Engine.intMain.Width / 2) - (r.Width / 2);
		if (r.X == -2) X = Engine.intMain.Width - r.Width;
		if (r.Y == -1) Y = (Engine.intMain.Height / 2) - (r.Height / 2);
		if (r.Y == -2) Y = Engine.intMain.Height - r.Height;
	}

	/**
	 * Alias for Draw(0,0)
	 * @param rs Regionset used to draw this object.
	 */
	public void Render(RegionSet rs) { Render(0, 0, rs); }

	/**
	 * Draw this widget at the specified coordinates.
	 * @param offx Horizontal coordinate.
	 * @param offy Vertical coordinate.
	 * @param RS Region set used to draw graphics.
	 */
	public void Render(int offx, int offy, RegionSet RS) { }

	public boolean MouseMove(Object sender, int xp, int yp) { return false; }

	/**
	 * Returns how this widget was affected to a mouse click at the specified
	 * coordinates with the specified button(s).
	 * @param sender The object that sent this MouseDown
	 * @param xp Horizontal coordinate of mouse cursor.
	 * @param yp Vertical coordinate of mouse cursor.
	 * @param buttons Mouse button used.
	 * @return A ClickResponse class containing the information of how this
	 * widget reacted.
	 */
	public ClickResponse MouseDown(Object sender, int xp, int yp, byte buttons)
	{
		return ClickResponse.None;
	}

	/**
	 * Returns how this widget was affected to a mouse depress at the specified
	 * coordinates with the specified button(s).
	 * @param sender The object that called this method.
	 * @param xp Horizontal coordinate of course cursor.
	 * @param yp Vertical coordinate of mouse cursor.
	 * @param buttons The button(s) that are currently pressed.
	 * @return An entry from ClickResponse.
	 */
	public ClickResponse MouseUp(Object sender, int xp, int yp, byte buttons)
	{
		return ClickResponse.None;
	}

	/**
	 * Fired when the mouse cursor has entered an object.
	 * @param sender Source object.
	 * @param xp Horizontal position of mouse cursor relative to source object.
	 * @param yp Vertical position of mouse cursor relative to source object.
	 * @return True if we consumed the event.
	 */
	public boolean MouseEnter(Object sender, int xp, int yp) { return false; }

	/**
	 * Fired when the mouse cursor leaves an object.
	 * @param sender Source object.
	 * @param xp Horizontal position of mouse cursor relative to source object.
	 * @param yp Vertical position of mouse cursor relative to source object.
	 * @return True if this widget consumed the event.
	 */
	public boolean MouseLeave(Object sender, int xp, int yp) { return false; }

	/**
	 * Called when a key was pressed in the parent element.
	 * @param key The ascii code of the key that was pressed.
	 * @return Whether or not this widget took the input.
	 */
	public boolean KeyPress(int key) { return false; }

	/**
	 * Responds to how this widget was affected to being blurred, or removed
	 * from focus.
	 * @return ClickResponse
	 */
	public ClickResponse Blur() { return ClickResponse.None; }
}