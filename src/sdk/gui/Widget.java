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
	public ClickResponse MouseDown(Object sender, int xp, int yp, int buttons)
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
	public ClickResponse MouseUp(Object sender, int xp, int yp, int buttons)
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

	/** <summary> Titlebar top left window region. </summary> */
	public static final int WINDOW_TTL = 0;
	/** <summary> Titlebar top middle window region. </summary> */
	public static final int WINDOW_TTM = 1;
	/** <summary> Titlebar top right window region. </summary> */
	public static final int WINDOW_TTR = 2;
	/** <summary> Titlebar close caption window region. </summary> */
	public static final int WINDOW_TCC = 3;
	/** Border top left window region. */
	public static final int WINDOW_BTL = 4;
	/** <summary> Border top middle window region. */
	public static final int WINDOW_BTM = 5;
	/** Border top right window region. */
	public static final int WINDOW_BTR = 6;
	/** Border middle left window region. */
	public static final int WINDOW_BML = 7;
	/** Body window region. */
	public static final int WINDOW_B = 8;
	/** Border middle right window region. */
	public static final int WINDOW_BMR = 9;
	/** Border bottom left window region. */
	public static final int WINDOW_BBL = 10;
	/** Border bottom middle window region. */
	public static final int WINDOW_BBM = 11;
	/** Border bottom right window region. */
	public static final int WINDOW_BBR = 12;
	/** Left button region. */
	public static final int BUTTON_L = 13;
	/** Body button region. */
	public static final int BUTTON_B = 14;
	/** Right button region. */
	public static final int BUTTON_R = 15;
	public static final int PBUTTON_L = 16;
	public static final int PBUTTON_B = 17;
	public static final int PBUTTON_R = 18;
	public static final int EBOX_L = 19;
	public static final int EBOX_M = 20;
	public static final int EBOX_R = 21;
	public static final int MENU_TL = 22;
	public static final int MENU_TM = 23;
	public static final int MENU_TR = 24;
	public static final int MENU_ML = 25;
	public static final int MENU_MM = 26;
	public static final int MENU_MR = 27;
	public static final int MENU_SL = 28;
	public static final int MENU_SM = 29;
	public static final int MENU_SR = 30;
	public static final int MENU_BL = 31;
	public static final int MENU_BM = 32;
	public static final int MENU_BR = 33;
	public static final int CHECK_OFF = 34;
	public static final int CHECK_ON = 35;
	public static final int RADIO_OFF = 36;
	public static final int RADIO_ON = 37;
	public static final int GROUP_TL = 38;
	public static final int GROUP_TM = 39;
	public static final int GROUP_TR = 40;
	public static final int GROUP_ML = 41;
	public static final int GROUP_MM = 42;
	public static final int GROUP_MR = 43;
	public static final int GROUP_BL = 44;
	public static final int GROUP_BM = 45;
	public static final int GROUP_BR = 46;
}