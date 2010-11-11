package sdk.input;

import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sdk.ActionKey;
import sdk.Engine;
import sdk.gui.EnterHandler;
import sdk.gui.WEditBox;
import sdk.gui.WTileEdit;
import sdk.item.Item;
import sdk.types.Entity;
import sdk.types.Interface;
import sdk.types.Rect;
import sdk.world.Tile;

/**
 *
 * @author Xedecimal
 */
public class InputManager
{
	public static final int MB_NONE = -1;
	public static final int MB_LEFT = 0;
	public static final int MB_RIGHT = 1;
	public static final int MB_MIDDLE = 2;

	public ArrayList<Integer> PressedKeys;
	private HashMap<Integer, ActionKey> Actions;
	/**
	 * The mouse buttons that are currently pressed, a 6 element boolean
	 * array.
	 */
	public boolean[] Buts;
	public int MouseX;
	public int MouseY;

	public InputManager()
	{
		Actions = new HashMap<Integer, ActionKey>();
		PressedKeys = new ArrayList<Integer>();
		Buts = new boolean[6];

		NodeList nl = Engine.config.get("/configuration/input/bind");
		for (int ix = 0; ix < nl.getLength(); ix++)
		{
			Node n = nl.item(ix);

			boolean hold = false;
			String atrkey = n.getAttributes().getNamedItem("key").getNodeValue();
			String atrhold = n.getAttributes().getNamedItem("hold").getNodeValue();
			String atract = n.getAttributes().getNamedItem("action").getNodeValue();

			int key = 0;
			try
			{
				key = Integer.parseInt(atrkey);
			}
			catch (NumberFormatException ex)
			{
				if (atrkey.length() == 1) key = (int)atrkey.charAt(0);
				else
				{
					System.out.println("Couldn't convert INT key (" + atrkey + ") to an actual key.");
					System.out.println("TODO: Convert ascii keys int numerical equivalants. (eg a = 65)");
					continue;
				}
			}

			hold = Boolean.parseBoolean(atrhold);
			//{
			//	System.out.println("Couldn't convert (" + atrhold + ") to either True or False.");
			//	continue;
			//}

			ActionTypes at = ActionTypes.valueOf(atract);
			if (at == null)
			{
				System.out.println("Couldn't convert action type (" + atract + ") to a valid action.");
				continue;
			}
			switch (at)
			{
				case MOVE_UP:
					Actions.put(key, new ActionKey(new ActionUp(), hold));
					break;
				case MOVE_LEFT:
					Actions.put(key, new ActionKey(new ActionLeft(), hold));
					break;
				case MOVE_DOWN:
					Actions.put(key, new ActionKey(new ActionDown(), hold));
					break;
				case MOVE_RIGHT:
					Actions.put(key, new ActionKey(new ActionRight(), hold));
					break;
				case CONSOLE:
					Actions.put(key, new ActionKey(new ActionConsole(), hold));
					break;
			}
		}
	}

	public void Update()
	{
		while (Keyboard.next())
		{
			int key = Keyboard.getEventKey();
			if (Keyboard.getEventKeyState())
			{
				KeyDown(key);
				KeyPress(Keyboard.getEventKey(), Keyboard.getEventCharacter());
			}
			else
				KeyUp(key);
		}

		while (Mouse.next())
		{
			// Mouse Moved
			if (Mouse.getDX() != 0 || Mouse.getDY() != 0)
				OnMouseMove(Mouse.getX(), Engine.intMain.Height-Mouse.getY());

			int d = 0;
			if ((d = Mouse.getDWheel()) != 0)
				OnMouseWheel(Mouse.getX(), Engine.intMain.Height-Mouse.getY(), d);

			// Button pressed.
			if (Mouse.getEventButton() != -1)
			{
				if (Mouse.getEventButtonState())
					OnMouseDown(Mouse.getX(),
							Engine.intMain.Height-Mouse.getY(),
							Mouse.getEventButton());
				else
					OnMouseUp(Mouse.getX(),
							Engine.intMain.Height-Mouse.getY(),
							Mouse.getEventButton());
			}
		}

		for (int ix = PressedKeys.size() - 1; ix >= 0; ix--)
		{
			if (Actions.containsKey(PressedKeys.get(ix)))
			{
				if (Actions.get(PressedKeys.get(ix)).Hold)
					Actions.get(PressedKeys.get(ix)).Handler.keyDown();
			}
		}
	}

	public void KeyDown(int key)
	{
		//System.out.println("Keydown: "+key);
		if (!PressedKeys.contains(key))
			PressedKeys.add(key);
		if (Actions.containsKey(key)) Actions.get(key).Handler.keyDown();
	}

	public void KeyUp(int key)
	{
		if (PressedKeys.contains(key))
		{
			PressedKeys.remove(PressedKeys.indexOf(key));
			if (Actions.containsKey(key))
				Actions.get(key).Handler.keyUp();
		}
	}

	protected void KeyPress(int key, char chr)
	{
		if (Engine.guiMain.KeyPress(chr)) return;
		if (Actions.containsKey(key))
			Actions.get(key).Handler.keyPress(key, chr);
	}

	public void OnMouseMove(int x, int y)
	{
		MouseX = x;
		MouseY = y;

		if (Engine.araMain != null)
		{
			Engine.araMain.SetMarker(Engine.araMain.ScreenToArea(x, y));
		}
		if (Engine.guiMain.MouseMove(x, y)) return;
	}

	public void OnMouseWheel(int x, int y, int w)
	{
		//Wheel Up
		if (w > 0)
		{
			Tile t = Engine.intMain.ScreenToAreaTile(x, y);
			if (t != null)
				t.Push(new Tile(Engine.araMain, t.ID, false));
		}
		//Wheel Down
		if (w < 0)
		{
			Tile t = Engine.intMain.ScreenToAreaTile(x, y);
			if (t != null)
				t.Pop();
		}
	}

	public void OnMouseDown(int x, int y, int button)
	{
		if (Buts[button]) return;
		Buts[button] = true;

		//GUI has priority.
		if (Engine.guiMain.MouseDown(x, y, button)) return;

		if (Engine.araMain != null)
		{
			//Left Click
			if (button == InputManager.MB_LEFT)
			{
				Item i = Engine.Player.getLeft();
				if (i != null) i.Use(x, y);
			}

			//Right Click
			if (button == InputManager.MB_RIGHT)
			{
				if (Interface.EditMode)
				{
					WTileEdit wnd = new WTileEdit(x, y);
					Engine.guiMain.add(0, wnd);
				}
				else if (Engine.araMain != null)
				{
					Item i = Engine.Player.getRight();
					if (i != null) i.Use(x, y);
				}
			}
		}
	}

	public void OnMouseUp(int x, int y, int button)
	{
		if (!Buts[button]) return;
		Buts[button] = false;
		if (Engine.guiMain.MouseUp(x, y, button)) return;
	}

	private class ActionUp extends KeyHandler
	{
		@Override
		public void keyDown()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_N, true);
		}

		@Override
		public void keyUp()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_N, false);
		}
	}

	private class ActionLeft extends KeyHandler
	{
		@Override
		public void keyDown()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_W, true);
		}

		@Override
		public void keyUp()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_W, false);
		}
	}

	private class ActionDown extends KeyHandler
	{
		@Override
		public void keyDown()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_S, true);
		}

		@Override
		public void keyUp()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_S, false);
		}
	}

	private class ActionRight extends KeyHandler
	{
		@Override
		public void keyDown()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_E, true);
		}

		@Override
		public void keyUp()
		{
			if (Engine.Player != null)
				Engine.Player.AlterDir(Entity.DIR_E, false);
		}
	}

	private class ActionConsole extends KeyHandler
	{
		@Override
		public void keyUp()
		{
			WEditBox eb = new WEditBox(new Rect(50, 50, 100, 16), "Poop!");
			eb.onEnter = new ConsoleEnterHandler();
			Engine.guiMain.add(eb);
		}
	}

	public class ConsoleEnterHandler implements EnterHandler
	{
		public void onEnter(String text)
		{
		}
	}
}