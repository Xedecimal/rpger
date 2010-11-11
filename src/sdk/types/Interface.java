package sdk.types;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import sdk.Engine;
import sdk.world.Tile;

/**
 * The main graphics object, handles pretty much all of the visual aspects of
 * RPGER.
 * @author Xedecimal
 */
public class Interface
{
	private static int m_fntBase;
	private static Texture m_texFont;

	/** View offset, for dynamic camera movement around a fixed point
	 * (ViewPoint)
	 */
	public double CameraX;
	public double CameraY;

	/** The horizontal resolution of the viewport. */
	public int Width = 800;
	/** The vertical resolution of the viewport. */
	public int Height = 600;

	/** Average frames rendered per second. */
	public static float FPS = 0;
	/** The amount of time between each frame. */
	public static boolean EditMode = false;

	/**
	 * The currently loaded textures, returned from this cache instead of
	 * loading a texture if possible.
	 */
	public static HashMap<String, Texture> m_textures = new HashMap<String, Texture>();
	
	/** Fired when the main viewport is closed or error occurs. */
	//public event BlankHandler OnQuit;

	/** The initialization of the graphics interface is done here. */
	public void init()
	{
		setDisplay(Width, Height);

		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GL11.glViewport(0, 0, Width, Height);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Width, Height, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);

		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		LoadFont("img/font.png");
	}

	void setDisplay(int w, int h)
	{
		DisplayMode mode = null;
        try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++)
			{
				if ((modes[i].getWidth() == w) && (modes[i].getHeight() == h))
				{
					mode = modes[i];
					break;
				}
			}
		} catch (LWJGLException e) {
			Sys.alert("Error", "Unable to determine display modes.");
			System.exit(0);
		}

		try
		{
			Display.setDisplayMode(mode);
            Display.setFullscreen(false);
			Display.setVSyncEnabled(true);
            Display.setTitle("LWJGL window");
			Display.create();
		} catch (LWJGLException ex) {
			Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void Start() throws IOException
	{
		Engine.Running = true;
		while (Engine.Running)
		{
			Display.update();
			Engine.Update();

			if (Display.isCloseRequested()) Engine.Running = false;

			//Display.sync(30);
			/*Sdl.SDL_PollEvent(out msg);
			if (msg.type == Sdl.SDL_QUIT) { Quit(); break; }
			if (msg.type == Sdl.SDL_MOUSEBUTTONDOWN) Engine.inpMain.OnMouseDown(msg.button);
			if (msg.type == Sdl.SDL_MOUSEBUTTONUP) Engine.inpMain.OnMouseUp(msg.button);
			if (msg.type == Sdl.SDL_MOUSEMOTION) Engine.inpMain.OnMouseMove(msg.motion);
			if (msg.type == Sdl.SDL_KEYDOWN) Engine.inpMain.KeyDown(msg.key);
			if (msg.type == Sdl.SDL_KEYUP) Engine.inpMain.KeyUp(msg.key);*/
		}
		Display.destroy();
	}

	private void LoadFont(String filename)
	{
		GL11.glLoadIdentity();
		m_texFont = GetSurface(filename);
		m_fntBase = GL11.glGenLists(256);
		for (int ix = 0; ix < 256; ix++)
		{
			float cx = (float)ix % 16 / 16.0f;
			float cy = (float)Math.floor((float)(ix / 16)) / 16.0f;
			GL11.glNewList(m_fntBase+ix, GL11.GL_COMPILE);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(cx,           cy);           GL11.glVertex2i(0,  0);
			GL11.glTexCoord2f(cx + 0.0625f, cy);           GL11.glVertex2i(16, 0);
			GL11.glTexCoord2f(cx + 0.0625f, cy + 0.0625f); GL11.glVertex2i(16, 16);
			GL11.glTexCoord2f(cx,           cy + 0.0625f); GL11.glVertex2i(0,  16);
			GL11.glEnd();
			GL11.glTranslated(8, 0, 0);
			GL11.glEndList();
		}
		System.out.println("Loaded font.");
	}

	/** Clears the viewport to whatever glClearColor is set. */
	public static void Clear()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/** Flips the backbuffer to the front buffer. */
	public void Flip()
	{
		/*try {
			Display.swapBuffers();
			//Display.update();
			Display.sync(30);
		} catch (LWJGLException ex) {
			Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
		}*/
	}

	/**
	 * Checks the cache to see if this file has already been loaded, if so
	 * returns that int texture id, otherwise loads it and returns the newly
	 * created texture id, as well as storing the id in the cache.
	 * @param filename The filename of the image to load the texture from.
	 * @return Texture ID
	 */
	public Texture GetSurface(String ref)
	{
		// Return cached item.
		if (m_textures.containsKey(ref)) return m_textures.get(ref);

		System.out.println("Loading texture: "+ref);

		try {
			FileInputStream fis = new FileInputStream(ref);
			Texture t = TextureLoader.getTexture("PNG", fis);
			fis.close();
			m_textures.put(ref, t);
			return t;
		} catch (Exception ex) {
			Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	/**
	 * Draws sub-region rSrc from texture tex to destination rDst.
	 * @param tex Texture to use.
	 * @param rSrc Source rectangle.
	 * @param rDst Destination rectangle.
	 * @param z Overlapping depth.
	 */
	public static void Draw(Texture tex, RectD rSrc, RectD rDst, double z)
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(rSrc.x, rSrc.y);
		GL11.glVertex3d(rDst.x, rDst.y, z);
		GL11.glTexCoord2d(rSrc.r, rSrc.y);
		GL11.glVertex3d(rDst.r, rDst.y, z);
		GL11.glTexCoord2d(rSrc.r, rSrc.b);
		GL11.glVertex3d(rDst.r, rDst.b, z);
		GL11.glTexCoord2d(rSrc.x, rSrc.b);
		GL11.glVertex3d(rDst.x, rDst.b, z);
		GL11.glEnd();
	}

	/**
	 * Draws a rectangle using the coordinates specified in rDst
	 * @param rDst Destination rectangle.
	 */
	public static void DrawRect(Rect rDst)
	{
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(rDst.X, rDst.Y, 1);
		GL11.glVertex3d(rDst.right(), rDst.Y, 1);
		GL11.glVertex3d(rDst.right(), rDst.bottom(), 1);
		GL11.glVertex3d(rDst.X, rDst.bottom(), 1);
		GL11.glEnd();
	}

	/**
	 * Draw specified text to coordinates at x, y.
	 * @param text The text to draw.
	 * @param x The horizontal coordinate.
	 * @param y The vertical coordinate.
	 */
	public void DrawText(String text, int x, int y)
	{
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glTranslated(x, y, 1f);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_texFont.getTextureID());
		GL11.glListBase(m_fntBase-32);
		BufferedReader br = new BufferedReader(new StringReader(text));
		String buf = null;
		try {
			while ((buf = br.readLine()) != null)
			{
				ByteBuffer bb = ByteBuffer.allocateDirect(buf.length());
				bb.put(Charset.forName("UTF-8").encode(buf));
				bb.rewind();
				GL11.glCallLists(bb);
				GL11.glTranslated(-buf.length()*8, 16, 0);
			}
		} catch (IOException ex) {
			Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
		}

		GL11.glPopMatrix();
	}

	/**
	 * Draw specified text wherever we are.
	 * @param text The text to draw.
	 */
	public static void DrawText(String text) throws IOException
	{
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, m_texFont.getTextureID());
		GL11.glListBase(m_fntBase-32);
		BufferedReader br = new BufferedReader(new StringReader(text));
		String buf = br.readLine();
		while (buf != null)
		{
			ByteBuffer bb = Charset.forName("UTF-8").encode(buf);
			GL11.glCallLists(bb);
			//Gl.glTranslated(-buf.Length*8, 16, 0);
			buf = br.readLine();
		}
		GL11.glPopMatrix();
	}

	public void Quit()
	{
		//if (OnQuit != null) OnQuit();
		Engine.Running = false;
	}

	public void SetView(float x, float y)
	{
		CameraX = x;
		CameraY = y;

		//ViewPort.X = (int)(Width / 2) + (m_view.X * 3200);
		//ViewPort.Y = (int)(Height / 2) + (m_view.Y * 1600);
	}

	public Tile ScreenToAreaTile(int x, int y)
	{
		Point wp = Engine.araMain.ScreenToArea(x, y);
		Vertex wt = Engine.araMain.AreaToTile(wp.x, wp.y);
		if (wt != null) return Engine.araMain.Map.get(wt.x, wt.y);
		return null;
	}
}