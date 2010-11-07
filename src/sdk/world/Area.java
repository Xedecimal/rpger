/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.world;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Entity;
import sdk.types.ImpactResult;
import sdk.types.ImpactType;
import sdk.types.Interface;
import sdk.types.RPoint;
import sdk.types.RegionSet;
import sdk.types.Space;
import sdk.types.Vertex;
import sdk.types.particle.ParticleEmitterCollection;

/**
 * A building block for a world.
 * @author Xedecimal
 */
public class Area extends Space
{
	public MapIsometric Map;
	public ParticleEmitterCollection Emitters;

	public int w() { return Map.Width; }
	public int h() { return Map.Height; }
	public int d() { return 0; }

	/** Width of each individual tile used for scaling the map. */
	private int m_tw = 64;

	/// <summary>
	/// Height of each individual tile used for scaling the map.
	/// </summary>
	private int m_th = 64;

	public boolean Server = false;

	private final int MAX_HEIGHT = 10;
	private Vertex m_marker;
	private BufferedImage m_mousemap;

	//Objects
	private Calendar m_deltaTime;
	private float m_delta;

	/// <summary>
	/// Precalculated viewport position, using m_viewoffset, player position
	/// and any other variables that affect the position of the viewport.
	/// </summary>

	//public Point ViewPort;

	public static int XTO;
	public static int YTO;

	public float RESERVED;

	public Area() throws IOException
	{
		Object o = new Object();
		m_deltaTime = Calendar.getInstance();
		XTO = m_tw / 2;
		//XTO = 32;
		YTO = m_th / 4;
		//YTO = 16;
		//ViewPort = new Point();
		m_marker = new Vertex();
		m_mousemap = ImageIO.read(new File("img/mousemap.png"));
		Emitters = new ParticleEmitterCollection();
	}

	/**
	 * Instantiates a new area as a portion of a world.
	 * @param wt Width of area in tiles.
	 * @param ht Height of area in tiles.
	 */
	public Area(int wt, int ht) throws IOException
	{
		this();
		Map = new MapIsometric(this, 64, 64, wt, ht);
		Server = true;
	}

	public Area(DataInputStream br) throws IOException
	{
		this();
		Map = new MapIsometric(this, br);
	}

	public void Serialize(DataOutputStream bw) throws IOException
	{
		Map.Serialize(bw);
	}

	public void Update(int offx, int offy)
	{
		m_delta = (Calendar.getInstance().getTimeInMillis() - m_deltaTime.getTimeInMillis()) / 1000;
		for (int ix = Objects.size() - 1; ix > -1; ix--)
		{
			Entity e = Objects.get(ix);
			e.Update(offx, offy);
			ImpactResult ir = CheckImpact(e);
			if (ir.type == ImpactType.IMPACT_OOB) Objects.remove(ix);
			else if (ir.type == ImpactType.IMPACT_HIT)
			{
				ir.target.Damage(e.weight);
				e.Damage(ir.target.weight);
			}
		}
		m_deltaTime = Calendar.getInstance();
	}

	public ImpactResult CheckImpact(Entity obj)
	{
		Vertex p = AreaToTile((int)obj.XMid(), (int)obj.y);
		if (p == null) { return new ImpactResult(ImpactType.IMPACT_OOB); }
		if (obj.z < p.z * 16)
		{
			Tile t = Map.get(p.x, p.y, (int)obj.z / 16);
			if (t != null) return new ImpactResult(ImpactType.IMPACT_HIT, t);
		}
		return new ImpactResult(ImpactType.IMPACT_NONE);
	}

	public void Render(int offx, int offy, RegionSet rs)
	{
		GL11.glColor4f(1, 1, 1, 1);
		Map.Render(offx, offy, rs);
		Emitters.Render(offx, offy);
	}

	//public Tile ScreenTile(int ix, int iy)
	//{
	//    Vertex np = ScreenTile(p);
	//    if (np.z < 1) return m_tiles[np.x + (np.y * m_w)];
	//    else return (Tile)m_tiles[np.x + (np.y*m_w)].Stack[np.z-1];
	//}

	/// <summary>
	/// Translates coordinates relative to the area into coordinates relative to tiles on the area.
	/// </summary>
	/// <param name="x">Horizontal pixel location to translate into tile coordinate.</param>
	/// <param name="y">Vertical pixel location to translate into tile coordinate.</param>
	/// <returns>Tile coordinates calculated from pixel coordinate p.</returns>
	public Vertex AreaToTile(int x, int y, int height)
	{
		//Tile point
		Point tp = new Point(x / m_tw, (y / 32) * 2);
		int c = m_mousemap.getRGB(Math.abs(x % m_tw), Math.abs(y % 32));
		int r = (c >>> 16) & 0xFF;
		int g = (c >>> 8) & 0xFF;
		int b = c & 0xFF;

		//Red == Top Left
		if (r == 255 && g == 000 && b == 000 && tp.y > 0) { tp.y--; }
		//Yellow == Top Right
		else if (r == 255 && g == 255 && b == 000 && tp.y > 0) { tp.x++; tp.y--; }
		//Blue == Bottom Left
		else if (r == 000 && g == 000 && b == 255) { tp.y++; }
		//Green == Bottom Right
		else if (r == 000 && g == 255 && b == 000) { tp.x++; tp.y++; }

		//int[] vp = new int[4];
		//Gl.glGetIntegerv(Gl.GL_VIEWPORT, vp);
		//double[] mm = new double[16];
		//Gl.glGetDoublev(Gl.GL_MODELVIEW_MATRIX, mm);
		//double[] pm = new double[16];
		//Gl.glGetDoublev(Gl.GL_PROJECTION_MATRIX, pm);
		//float nz = 0;
		//Gl.glReadPixels(x, vp[1] - y, 1, 1, Gl.GL_DEPTH_COMPONENT, Gl.GL_FLOAT, nz);
		//double ox, oy, oz;
		//int res = Glu.gluUnProject(x, vp[1] - y, nz, mm, pm, vp, out ox, out oy, out oz);

		//if (tp.X >= 0 && tp.Y >= 0 && tp.X < Map.Width && tp.Y < Map.Height)
		if (Map.InMap(tp.x - height, tp.y + height))
		{
			Tile t = Map.get(tp.x, tp.y);
			return new Vertex(tp.x, tp.y, t.Count());
		}

		//Check height
		/*if (Map.InMap(tp.X, tp.Y))
		{
			Vertex v = AreaToTile(x, y + m_th, height + 1);

			//No tiles on top of this one.
			if (v == null)
			{
				Tile t = Map[tp.X, tp.Y, height];
				if (t == null) return null;
				return new Vertex(tp.X, tp.Y, t.Count);
			}

			//Return heighest tile.
			return v;
		}
		else return null;*/
		return null;
	}

	public Vertex AreaToTile(int x, int y) { return AreaToTile(x, y, 0); }

	//public Vertex CoordFromPoint(Point p)
	//{
		//int step = YTO * 2;
		//Calculate actual tile we are on (Tile Point)
		//Vertex tp = new Vertex(p.X / m_tw, (p.Y / 32) * 2, 0);
		//Color c = m_mousemap.GetPixel(p.X % m_tw, p.Y % 32);
		//Yellow == Top Left
		//if (c.R == 255 && c.G == 255 && c.B == 000) { tp.y--; }
		//Green == Top Right
		//if (c.R == 000 && c.G == 255 && c.B == 000) { tp.x++; tp.y--; }
		//Blue == Bottom Left
		//if (c.R == 000 && c.G == 000 && c.B == 255) { tp.y++; }
		//Red == Bottom Right
		//if (c.R == 255 && c.G == 000 && c.B == 000) { tp.x++; tp.y++; }

		//Check the next tile down for a simple half-intersection.
//			if (tp.y < m_h)
//			{
//				Tile t = m_tiles[tp.x + ((tp.y+2)*m_w)];
//				if (t.Stack.Count == 1)
//				{
//					if (p.Y % (YTO*2) > 16)
//					{
//						c = m_mousemap.GetPixel(p.X % m_tw, p.Y % (32 - 16));
//						if (c.R == 0 && c.G == 0 && c.B == 0) { tp.y += 2; tp.z = 1; return tp; }
//					}
//				}
//			}

		//Current tile (ct) we are checking
		//Tile ct = m_tiles[tp.x + (tp.y * m_w)];

		//Check each tile straight (+2) down until we hit the bottom of the map.
		//for (int iy = 2; tp.y+iy < m_h; iy += 2)
		//{
			//We're at tp.y + iy * YTO for y pixel coordinate of tile.
			//Tile t = m_tiles[tp.x + ((tp.y+iy) * m_w)];

			//Is tile below taller than the smallest possible intersection? (2x per tile)
			//if (t.Stack.Count >= iy/*-1*/) //-1 adds half intersection into calculation.
			//{
				//Locate mouse's Y position of mouse on this intersected tile.
				//Locate the level of height the mouse is on (/ YTO)
				//int hoff = yoff/YTO;
				//tp.y += iy;
				//ct = t;
				//Check the mouse map from the top down, if we ever hit pitch black, we're on that level.
				//for (int iz = t.Stack.Count; iz > 0; iz--)
				//{
					//c = m_mousemap.GetPixel(p.X % m_tw, (p.Y+yoff) % step);
					//if (c.R == 000 && c.G == 000 && c.B == 000) { tp.y += iy; tp.z = iz; break; }
					//yoff += YTO;
				//}
		//    }
		//}

		//Now lets get the height of the tile we're at.
		//int yoff = p.Y - (tp.y * YTO) + (ct.Stack.Count * 16) - 16;
		//RESERVED = yoff;
		//tp.z = ct.Stack.Count-(yoff/16);
		//tp.z = m_tiles[tp.x + (tp.y * m_w)].Stack.Count;
		//return tp;
	//}

	public void SetMarker(int x, int y)
	{
		Vertex tp = AreaToTile(x, y);
		if (tp != null) Map.Marker = new RPoint(tp.x, tp.y);
	}

	public void SetMarker(Point p) { SetMarker(p.x, p.y); }

	public Point2D.Float GetMoveCollision(int FromX, int FromY, int ToX, int ToY)
	{
		if (AreaToTile(FromX, FromY) == AreaToTile(ToX, ToY))
			return new Point2D.Float(ToX, ToY);
		return new Point2D.Float(FromX, FromY);
	}

	public Point ScreenToArea(int x, int y)
	{
		return new Point(x + (int)Engine.intMain.CameraX, y + (int)Engine.intMain.CameraY);
	}

	public void AddObject(Entity e)
	{
		Objects.add(e);
	}
}