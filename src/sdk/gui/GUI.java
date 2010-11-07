package sdk.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import sdk.types.Rect;
import sdk.types.RegionSet;

/**
 * The main GUI handler, managing widgets and all it's derived objects.
 * @author Xedecimal
 * @todo Don't modify the Rect when using -1 and -2, allowing constant dynamic
 * resizing.
 */
public class GUI extends ArrayList<Widget> implements ClickHandler {
	private boolean m_dragging;
	private Widget m_focus;
	private int lx, ly;

	/** All the GUI regions from GUISet. */
	public RegionSet RS;

	public boolean Dragging;
	public Widget Focus;

	/// <summary>
	/// Use this to remove a specific widget, if you already have the widget
	/// initialized and in your posession this is usually the easiest way to
	/// go.
	/// </summary>
	/// <param name="w">The widget to remove.</param>
	public void RemoveWidget(Widget w)
	{
		if (m_focus == w) Blur();
		remove(w);
	}

	/// <summary>
	/// Create a brand new GUI (aka window manager)
	/// </summary>
	public GUI()
	{
		m_dragging = false;
		m_focus = null;
		RS = new RegionSet("GUISet");
	}

	/**
	 * Draw all widgets that have been added to the GUI in backwards order.
	 */
	public void Render()
	{
		ListIterator<Widget> li = listIterator(size());
		while (li.hasPrevious()) 
			li.previous().Render(RS);
	}

	/// <summary>
	/// Called by the engine when the mouse is pressed.
	/// </summary>
	/// <param name="x"></param>
	/// <param name="y"></param>
	/// <param name="buttons"></param>
	/// <returns></returns>
	public boolean MouseDown(int x, int y, byte buttons)
	{
		if (Focus != null && !Focus.Contains(x, y)) Focus.Blur();
		
		Iterator<Widget> i = this.iterator();
		while (i.hasNext())
		{
			Widget w = i.next();
			if (w.Contains(x, y))
			{
				if (indexOf(w) > 0)
				{
					remove(w);
					add(0, w);
				}
				ClickResponse cr = w.MouseDown(this, x, y, buttons);
				if (cr == ClickResponse.Close) { RemoveWidget(w); return true; }
				else if (cr == ClickResponse.Drag) { Focus = w; m_dragging = true; }
				else if (cr == ClickResponse.Focus) { Focus = w; }
				return true;
			}
		}
		return false;
	}

	/// <summary>
	/// Called by the engine when the mouse is depressed.
	/// </summary>
	/// <param name="xp">Horizontal position of mouse cursor.</param>
	/// <param name="yp">Vertical position of mouse cursor.</param>
	/// <param name="buttons">Current pressed buttons.</param>
	/// <returns>True if this widget took the input.</returns>
	public boolean MouseUp(int xp, int yp, byte buttons)
	{
		m_dragging = false;
		Iterator<Widget> i = iterator();
		while (i.hasNext())
		{
			Widget wig = i.next();
			if (wig.Contains(xp, yp))
			{
				ClickResponse cr = wig.MouseUp(this, xp, yp, buttons);
				if (cr == ClickResponse.Focus) Focus = wig;
				return true;
			}
		}
		return false;
	}

	/// <summary>
	/// Call the focused widget's blur method and set focus
	/// to null.
	/// </summary>
	public void Blur()
	{
		if (m_focus != null) m_focus.Blur();
		m_focus = null;
	}

	/// <summary>
	/// Handle general mouse movements.
	/// </summary>
	/// <param name="xp">Current horizontal point of the mouse.</param>
	/// <param name="yp">Current vertical point of the mouse.</param>
	/// <returns></returns>
	public boolean MouseMove(int xp, int yp)
	{
		if (m_dragging) { m_focus.Offset(xp-lx, yp-ly); lx = xp; ly = yp; return true; }
		lx = xp; ly = yp;
		Iterator<Widget> i = iterator();
		while (i.hasNext())
		{
			Widget w = i.next();
			if (w.Contains(xp, yp)) if (w.MouseMove(this, xp, yp)) return true;
		}
		return false;
	}

	/// <summary>
	/// When a key is pressed, if a widget is focused, and can take input, then take it.
	/// </summary>
	/// <param name="key">The ASCII code of the key that was pressed</param>
	/// <returns>True if the focused widget took the input.</returns>
	public boolean KeyPress(int key)
	{
		if (m_focus != null) return m_focus.KeyPress(key);
		return false;
	}

	/**
	 * Display a custom little GUI messagebox.
	 * @param text The text to display inside the messagebox.
	 * @param title The title of the messagebox.
	 * @return False always for now.
	 * @todo Make this return true if positive, false if negative hopefully.
	 */
	public boolean MessageBox(String text, String title)
	{
		WWindow w = new WWindow(new Rect(-1, -1, 200, 200), "MessageBox");
		w.Add(new WLabel(new Rect(0, 0, 100, 100), text));
		w.Add(new WButton(new Rect(0, 125, 50, 16), "OK", this));
		add(0, w);
		return false;
	}

	/// <summary>
	/// Display a custom little GUI messagebox.
	/// </summary>
	/// <param name="text">The text to display inside the messagebox.</param>
	/// <returns>False always for now.</returns>
	/// <todo>Make this return true if positive, false if negative hopefully.</todo>
	public boolean MessageBox(String text)
	{
		return MessageBox(text, "MessageBox");
	}

	/**
	 * Callback for OK button on message box.
	 * @param sender Message box.
	 * @param state Unknown.
	 */
	public void onClick(Object sender, boolean state) {
		remove(m_focus);
		m_focus = null;
	}
}
