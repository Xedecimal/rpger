package sdk.player;

import java.util.EnumSet;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Entity;
import sdk.types.RPoint;
import sdk.types.RectD;
import sdk.types.RegionSet;
import sdk.types.Space;
import sdk.types.Vertex;
import sdk.item.Item;

/**
 *
 * @author Xedecimal
 */
public class Player extends Entity
{
	private enum Regions
	{
		PLAYER_NN_0, PLAYER_NN_1, PLAYER_NN_2, PLAYER_NN_3, PLAYER_NN_4, PLAYER_NN_5, PLAYER_NN_6, PLAYER_NN_7,
		PLAYER_NE_0, PLAYER_NE_1, PLAYER_NE_2, PLAYER_NE_3, PLAYER_NE_4, PLAYER_NE_5, PLAYER_NE_6, PLAYER_NE_7,
		PLAYER_EE_0, PLAYER_EE_1, PLAYER_EE_2, PLAYER_EE_3, PLAYER_EE_4, PLAYER_EE_5, PLAYER_EE_6, PLAYER_EE_7,
		PLAYER_SE_0, PLAYER_SE_1, PLAYER_SE_2, PLAYER_SE_3, PLAYER_SE_4, PLAYER_SE_5, PLAYER_SE_6, PLAYER_SE_7,
		PLAYER_SS_0, PLAYER_SS_1, PLAYER_SS_2, PLAYER_SS_3, PLAYER_SS_4, PLAYER_SS_5, PLAYER_SS_6, PLAYER_SS_7,
		PLAYER_SW_0, PLAYER_SW_1, PLAYER_SW_2, PLAYER_SW_3, PLAYER_SW_4, PLAYER_SW_5, PLAYER_SW_6, PLAYER_SW_7,
		PLAYER_WW_0, PLAYER_WW_1, PLAYER_WW_2, PLAYER_WW_3, PLAYER_WW_4, PLAYER_WW_5, PLAYER_WW_6, PLAYER_WW_7,
		PLAYER_NW_0, PLAYER_NW_1, PLAYER_NW_2, PLAYER_NW_3, PLAYER_NW_4, PLAYER_NW_5, PLAYER_NW_6, PLAYER_NW_7
	}

	private String m_regname;
	private int m_visibledir;
	private RegionSet m_rs;
	public float m_frame, m_animspeed;
	public String Name;
	public PlayerType Type;
	public RPoint Destination;
	public int ID, HP, HPMax, ST, STMax;
	private Space m_space;

	//Inventory

	/** Item in left hand. */
	private Item m_left;
	/** Item in right hand. */
	private Item m_right;

	public Item getLeft() { return m_left; }
	public Item getRight() { return m_right; }
	public Item setLeft(Item i) { m_left = i; i.owner = this; return i; }
	public Item setRight(Item i) { m_right = i; i.owner = this; return i; }

	//public event DirChangeHandler OnDirChange;

	public Player(Space space)
	{
		super(space);
		m_rs = Engine.regMain.GetRegions("player");
		m_space = space;
	}

	public Player(String name, PlayerType type, boolean load, Space parent)
	{
		this(parent);
		x = y = 0;
		//z = 1f;
		w = 64;
		h = 64;
		m_animspeed = 13;
		vel = 140;
		//vel = 1;
		m_frame = 0;

		ID = 0;
		Name = name;
		Type = type;
		m_regname = "player";
		//m_pos = new Core.RPointF();
		Destination = new RPoint();
		Dir = Entity.DIR_NONE;
	}

	/*public Player(BinaryReader r, Space parent) : this(parent)
	{
		m_animspeed = 40f;
		vel = 140;
		m_frame = 0;

		ID = r.ReadInt32();
		Name = r.ReadString();
		Type = (PlayerType)r.ReadInt32();
		m_regname = r.ReadString();
		x = r.ReadSingle();
		y = r.ReadSingle();
		Destination = new Core.RPoint(r.ReadInt32(), r.ReadInt32());
		Dir = (Direction)r.ReadInt32();
		HP = r.ReadInt32();
		HPMax = r.ReadInt32();
		ST = r.ReadInt32();
		STMax = r.ReadInt32();
		System.out.println("[PLAYER] New player: " + Name);
	}*/

	/*public void Serialize(BinaryWriter w)
	{
		w.Write(ID);
		w.Write(Name);
		w.Write((int)Type);
		w.Write(m_regname);
		w.Write(x);
		w.Write(y);
		w.Write(Destination.x);
		w.Write(Destination.y);
		w.Write((int)Dir);
		w.Write(HP);
		w.Write(HPMax);
		w.Write(ST);
		w.Write(STMax);
	}*/

	public void Think()
	{
		if (Type == PlayerType.Enemy)
		{
			if (
				(int)x < Destination.x + vel &&
				(int)x > Destination.x - vel &&
				(int)y < Destination.y + vel &&
				(int)y > Destination.y - vel)
			{
				Destination.x = Engine.r.nextInt(Space.w() * 32);
				Destination.y = Engine.r.nextInt(Space.h() * 32);
			}
			else MoveTowards(Destination);
		}
	}

	public void AlterDir(int dd, boolean add)
	{
		if (add) Dir |= dd;
		else Dir ^= dd;
		SetVisibleDir();
	}

	private void SetVisibleDir()
	{
		if ((Dir & DIR_N) == DIR_N && (Dir & DIR_E) == DIR_E) m_visibledir = 1;
		else if ((Dir & DIR_S) == DIR_S && (Dir & DIR_E) == DIR_E) m_visibledir = 3;
		else if ((Dir & DIR_S) == DIR_S && (Dir & DIR_W) == DIR_W) m_visibledir = 5;
		else if ((Dir & DIR_N) == DIR_N && (Dir & DIR_W) == DIR_W) m_visibledir = 7;
		else if ((Dir & DIR_N) == DIR_N) m_visibledir = 0;
		else if ((Dir & DIR_E) == DIR_E) m_visibledir = 2;
		else if ((Dir & DIR_S) == DIR_S) m_visibledir = 4;
		else if ((Dir & DIR_W) == DIR_W) m_visibledir = 6;
	}

	public void SetDir(int d)
	{
		//if (Dir != d && OnDirChange != null) OnDirChange(this, d);
		Dir = d;
		SetVisibleDir();
	}

	public void MoveTowards(RPoint pDst)
	{
		/*if (pDst.x < (int)x && pDst.y < (int)y) SetDir(Direction.North.ordinal() | Direction.West.ordinal());
		else if (pDst.x > (int)x && pDst.y < (int)y) SetDir(Direction.North | Direction.East);
		else if (pDst.x > (int)x && pDst.y > (int)y) SetDir(Direction.South | Direction.East);
		else if (pDst.x < (int)x && pDst.y > (int)y) SetDir(Direction.South | Direction.West);
		else if (pDst.x < (int)x) SetDir(Direction.West);
		else if (pDst.x > (int)x) SetDir(Direction.East);
		else if (pDst.y < (int)y) SetDir(Direction.North);
		else if (pDst.y > (int)y) SetDir(Direction.South);*/
	}

	@Override
	public void Update(int offx, int offy)
	{
		super.Update(offx, offy);
		if (Type == PlayerType.Controlled)
		{
			Engine.intMain.CameraX = Engine.Player.x - (Engine.intMain.Width / 2);
			Engine.intMain.CameraY = Engine.Player.y - (Engine.intMain.Height / 2);
			Engine.intMain.CameraX += ((Engine.inpMain.MouseX / (float)Engine.intMain.Width) - 0.5f) * 1600;
			Engine.intMain.CameraY += ((Engine.inpMain.MouseY / (float)Engine.intMain.Height) - 0.5f) * 1200;
			//if (Engine.araMain != null) Engine.araMain.SetMarker((int)x + 32, (int)y + 55);
		}
		else { Think(); }

		if (Dir != DIR_NONE)
		{
			if ((m_frame += Engine.Delta * m_animspeed) > 7)
				m_frame = 0;
		}

		GL11.glLoadIdentity();
		RectD rdst = new RectD((int)x - offx, (int)y - offy - 64, 64, 64);
		Vertex v = Engine.araMain.AreaToTile((int)x, (int)y);
		if (v != null)
		{
			m_rs.Draw((int)m_frame + (int)(m_visibledir) * 8, rdst, v.y * .01);
			Engine.m_debug = ((Double)XMid()).toString();
			Engine.intMain.DrawText(Name, (int)rdst.x - (Name.length() / 2), (int)rdst.y - 16);
		}
	}
}