package sdk.world;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import sdk.Engine;
import sdk.types.Entity;
import sdk.types.ImpactResult;
import sdk.types.ImpactType;
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

	//private final int MAX_HEIGHT = 10;
	//private Vertex m_marker;
	private BufferedImage m_mousemap;

	//Objects
	private Calendar m_deltaTime;
	//private float m_delta;

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
		//m_marker = new Vertex();
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
		Map.generate();
		Server = true;
	}

	public Area(ByteBuffer bb) throws IOException
	{
		this();
		Map = new MapIsometric(this, bb);
	}

	public void Serialize(ByteBuffer bb) throws IOException
	{
		Map.Serialize(bb);
	}

	public void Update(int offx, int offy)
	{
		//m_delta = (Calendar.getInstance().getTimeInMillis() -
		//	m_deltaTime.getTimeInMillis()) / 1000;
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

	/**
	 * Translates coordinates relative to the area into coordinates relative to
	 * tiles on the area.
	 * @param x Horizontal pixel location to translate into tile coordinate.
	 * @param y Vertical pixel location to translate into tile coordinate.
	 * @param height
	 * @return Tile coordinates calculated from pixel coordinate p.
	 */
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
			return new Vertex(tp.x, tp.y, t.count());
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