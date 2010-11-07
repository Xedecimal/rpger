package sdk;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import org.w3c.dom.Document;
import sdk.input.InputManager;
import sdk.types.GameState;
import sdk.types.Interface;
import sdk.types.RegionManager;
import sdk.gui.GUI;
import sdk.types.particle.ParticleEmitterCollection;
import sdk.types.player.Player;
import sdk.world.Area;

/**
 *
 * @author Xedecimal
 */
public class Engine {
	public static Random r = new Random();
	public static GameState State = GameState.Login;
	public static Player Player;

	public static Interface intMain;
	public static ParticleEmitterCollection pecMain;
	public static GUI guiMain;
	public static Area araMain;
	//public static RPNetNode netMain;
	public static RegionManager regMain;
	public static InputManager inpMain;
	public static float Delta;
	public static int FPS;
	private static long m_deltaTime, m_curTime;
	private static int m_frames;

	public static String m_debug = "";

	/**
	 * Is the game currently running? Set to false on exit.
	 */
	public static boolean Running;

	public static void InitInterface() throws IOException
	{
		intMain = new Interface();
		intMain.init();
	}
	public static void InitWorld() throws IOException
	{
		araMain = new Area(50, 50);
	}
	public static void InitParticles() { pecMain = new ParticleEmitterCollection(); }
	public static void InitGUI() { guiMain = new GUI(); }
	//public static void InitNetwork(Document config) { netMain = new RPNetNode(config); }
	public static void InitRegions() { regMain = new RegionManager(); }
	public static void InitInput(Document config) { inpMain = new InputManager(config); }

	/**
	 * Simulates a single frame.
	 */
	public static void Update()
	{
		m_curTime = Calendar.getInstance().getTimeInMillis();
		if (m_deltaTime / 1000 != m_curTime / 1000)
		{
			FPS = ((FPS + m_frames) / 2);
			m_frames = 0;
		}
		Delta = m_curTime - m_deltaTime;
		m_deltaTime = Calendar.getInstance().getTimeInMillis();
		Interface.Clear();
		int offx = (int)intMain.CameraX;
		int offy = (int)intMain.CameraY;
		if (State == GameState.Login)
		{
			inpMain.Update();
			pecMain.Render(offx, offy);
			guiMain.Render();
		}
		if (State == GameState.Active)
		{
			inpMain.Update();
			araMain.Update(offx, offy);
			araMain.Render(offx, offy, regMain.GetRegions("tiles"));
			guiMain.Render();
			pecMain.Render(offx, offy);
		}
		Engine.intMain.DrawText("FPS: " + FPS, 10, Engine.intMain.Height - 16);
		Engine.intMain.DrawText(m_debug, 10, Engine.intMain.Height - 32);
		intMain.Flip();
		m_frames++;
	}

	public static int Random(long start, long end)
	{
		long range = end - start + 1;
		long fraction = (long)(range * r.nextDouble());
		return (int)(fraction + start);
	}
}
