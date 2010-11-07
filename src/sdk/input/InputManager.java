/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.input;

import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import sdk.ActionKey;
import sdk.Engine;
import sdk.types.Direction;

/**
 *
 * @author Xedecimal
 */
public class InputManager
{
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
		/*for (int ix = PressedKeys.size() - 1; ix >= 0; ix--)
		{
			if (Actions.containsKey(PressedKeys.get(ix)))
			{
				if (Actions.get(PressedKeys.get(ix)).Hold)
					Actions.get(PressedKeys.get(ix)).Handler(true);
			}
		}*/
	}

	/*public void KeyDown(Sdl.SDL_KeyboardEvent e)
	{
		if (!PressedKeys.Contains(e.keysym.sym))
		{
			KeyPress(e);
			PressedKeys.Add(e.keysym.sym);
		}
	}

	public void KeyUp(Sdl.SDL_KeyboardEvent e)
	{
		if (PressedKeys.Contains(e.keysym.sym))
		{
			PressedKeys.Remove(e.keysym.sym);
			if (Actions.ContainsKey(e.keysym.sym)) Actions[e.keysym.sym].Handler(false);
		}
	}

	protected void KeyPress(Sdl.SDL_KeyboardEvent e)
	{
		if (Engine.guiMain.KeyPress(e.keysym.sym)) return;
		if (Actions.ContainsKey(e.keysym.sym)) Actions[e.keysym.sym].Handler(true);

		//if (e.keysym.sym == 'i') Engine.araMain.Scroll(Direction.North);
		//if (e.keysym.sym == 'k') Engine.araMain.Scroll(Direction.South);
		//if (e.keysym.sym == 'j') Engine.araMain.Scroll(Direction.West);
		//if (e.keysym.sym == 'l') Engine.araMain.Scroll(Direction.East);
	}

	public void OnMouseMove(Sdl.SDL_MouseMotionEvent mme)
	{
		MouseX = mme.x;
		MouseY = mme.y;

		if (Engine.araMain != null)
		{
			Engine.araMain.SetMarker(Engine.araMain.ScreenToArea(mme.x, mme.y));
		}
		if (Engine.guiMain.MouseMove(mme.x, mme.y)) return;
	}

	public void OnMouseDown(Sdl.SDL_MouseButtonEvent mbe)
	{
		if (Buts[mbe.button]) return;
		Buts[mbe.button] = true;

		//GUI has priority.
		if (Engine.guiMain.MouseDown(mbe.x, mbe.y, mbe.button)) return;

		if (Engine.araMain != null)
		{
			//Left Click
			if (mbe.button == 1)
			{
				if (Engine.Player.Left != null) Engine.Player.Left.Use(mbe.x, mbe.y);
			}

			//Right Click
			if (mbe.button == 3)
			{
				if (Interface.EditMode)
				{
					WTileEdit wnd = new WTileEdit(mbe.x, mbe.y);
					Engine.guiMain.Insert(0, wnd);
				}
				else if (Engine.araMain != null)
				{
					if (Engine.Player.Right != null) Engine.Player.Right.Use(mbe.x, mbe.y);
				}
			}

			//Wheel Up
			if (mbe.button == 4)
			{
				Tile t = Engine.intMain.ScreenToAreaTile(mbe.x, mbe.y);
				if (t != null)
					t.Push(new Tile(Engine.araMain, t.ID, false));
			}
			//Wheel Down
			if (mbe.button == 5)
			{
				Tile t = Engine.intMain.ScreenToAreaTile(mbe.x, mbe.y);
				if (t != null)
					t.Pop();
			}
		}
	}

	public void OnMouseUp(Sdl.SDL_MouseButtonEvent mbe)
	{
		if (!Buts[mbe.button]) return;
		Buts[mbe.button] = false;
		if (Engine.guiMain.MouseUp(mbe.x, mbe.y, mbe.button)) return;
	}*/

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