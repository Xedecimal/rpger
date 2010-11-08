package sdk.input;

import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.w3c.dom.Document;
import sdk.ActionKey;
import sdk.Engine;
import sdk.types.Direction;
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
	public static final int MB_WUP = 3;
	public static final int MB_WDN = 4;

	public ArrayList<Integer> PressedKeys;
	private HashMap<Integer, ActionKey> Actions;
	/**
	 * The mouse buttons that are currently pressed, a 6 element boolean
	 * array.
	 */
	public boolean[] Buts;
	public int MouseX;
	public int MouseY;

	public InputManager(Document config)
	{
		Actions = new HashMap<Integer, ActionKey>();
		PressedKeys = new ArrayList<Integer>();
		Buts = new boolean[6];

		/*XmlNodeList xnl = config.SelectNodes("/configuration/input/bind");
		foreach (XmlNode xn in xnl)
		{
			int key = 0;
			bool hold = false;
			string atrkey = xn.Attributes["key"].Value;
			string atrhold = xn.Attributes["hold"].Value;
			string atract = xn.Attributes["action"].Value;
			if (!int.TryParse(atrkey, out key))
			{
				if (atrkey.Length == 1) key = (int)atrkey[0];
				else
				{
					System.out.println("Couldn't convert INT key (" + atrkey + ") to an actual key.");
					System.out.println("TODO: Convert ascii keys int numerical equivalants. (eg a = 65)");
					continue;
				}
			}
			if (!bool.TryParse(atrhold, out hold))
			{
				System.out.println("Couldn't convert (" + atrhold + ") to either true or false.");
				continue;
			}
			object parsed = Enum.Parse(typeof(ActionTypes), atract, true);
			if (parsed == null)
			{
				System.out.println("Couldn't convert action type (" + atract + ") to a valid action.");
				continue;
			}
			switch ((ActionTypes)parsed)
			{
				case ActionTypes.MOVE_UP:
					Actions[key] = new ActionKey(new KeyHandler(Action_Move_Up), hold);
					break;
				case ActionTypes.MOVE_LEFT:
					Actions[key] = new ActionKey(new KeyHandler(Action_Move_Left), hold);
					break;
				case ActionTypes.MOVE_DOWN:
					Actions[key] = new ActionKey(new KeyHandler(Action_Move_Down), hold);
					break;
				case ActionTypes.MOVE_RIGHT:
					Actions[key] = new ActionKey(new KeyHandler(Action_Move_Right), hold);
					break;
			}
		}*/
	}

	public void Update()
	{
		while (Keyboard.next())
		{
			int key = Keyboard.getEventKey();
			if (Keyboard.getEventKeyState());
		}

		while (Mouse.next())
		{
			// Mouse Moved
			if (Mouse.getDX() != 0 || Mouse.getDY() != 0)
				OnMouseMove(Mouse.getX(), Engine.intMain.Height-Mouse.getY());

			if (Mouse.getDWheel() != 0)
				OnMouseWheel(Mouse.getDWheel());

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

		/*for (int ix = PressedKeys.size() - 1; ix >= 0; ix--)
		{
			if (Actions.containsKey(PressedKeys.get(ix)))
			{
				if (Actions.get(PressedKeys.get(ix)).Hold)
					Actions.get(PressedKeys.get(ix)).Handler(true);
			}
		}*/
	}

	public void KeyDown(int key)
	{
		KeyPress(key);
		PressedKeys.add(key);
	}

	/*public void KeyUp(Sdl.SDL_KeyboardEvent e)
	{
		if (PressedKeys.Contains(e.keysym.sym))
		{
			PressedKeys.Remove(e.keysym.sym);
			if (Actions.ContainsKey(e.keysym.sym))
				Actions[e.keysym.sym].Handler(false);
		}
	}*/

	protected void KeyPress(int key)
	{
		if (Engine.guiMain.KeyPress(key)) return;
		//if (Actions.containsKey(key))
		//	Actions.get(key).Handler(true);
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

	public void OnMouseWheel(int w)
	{
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
				if (Engine.Player.Left != null) Engine.Player.Left.Use(x, y);
			}

			//Right Click
			if (button == InputManager.MB_RIGHT)
			{
				//@TODO: Fix me.
				/*if (Interface.EditMode)
				{
					WTileEdit wnd = new WTileEdit(mbe.x, mbe.y);
					Engine.guiMain.Insert(0, wnd);
				}
				else if (Engine.araMain != null)
				{*/
					if (Engine.Player.Right != null) Engine.Player.Right.Use(x, y);
				//}
			}

			//Wheel Up
			if (button == InputManager.MB_WUP)
			{
				Tile t = Engine.intMain.ScreenToAreaTile(x, y);
				if (t != null)
					t.Push(new Tile(Engine.araMain, t.ID, false));
			}
			//Wheel Down
			if (button == InputManager.MB_WDN)
			{
				Tile t = Engine.intMain.ScreenToAreaTile(x, y);
				if (t != null)
					t.Pop();
			}
		}
	}

	public void OnMouseUp(int x, int y, int button)
	{
		if (!Buts[button]) return;
		Buts[button] = false;
		if (Engine.guiMain.MouseUp(x, y, button)) return;
	}

	private void Action_Move_Up(boolean down)
	{
		Engine.Player.AlterDir(Direction.North, down);
	}

	private void Action_Move_Left(boolean down)
	{
		Engine.Player.AlterDir(Direction.West, down);
	}

	private void Action_Move_Down(boolean down)
	{
		Engine.Player.AlterDir(Direction.South, down);
	}

	private void Action_Move_Right(boolean down)
	{
		Engine.Player.AlterDir(Direction.East, down);
	}
}