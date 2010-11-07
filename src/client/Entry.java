package client;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sdk.Engine;
import sdk.types.GameState;
import sdk.types.RPointD;
import sdk.types.particle.ParticleEmitter;
import sdk.types.particle.ParticleFilterFade;
import sdk.types.particle.ParticleFilterRotate;

/**
 *
 * @author Xedecimal
 */
public class Entry
{
	/** Main script engine, initially executes scripts/AutoExec.cs. */
	public ScriptEngineManager SEM;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SAXException
	{
		Entry e = new Entry();
	}

	/**
	 * Client's initialization begins here.
	 */
	public Entry() throws IOException
	{
		Document doc = null;
		File f = new File("config.xml");
		if (f.exists())
		{
			DocumentBuilder db = null;
			try {
				doc = db.parse(f);
			} catch (Exception ex) {
				Logger.getLogger(Entry.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		Engine.InitInterface();
		Engine.InitInput(doc);
		Engine.InitRegions();
		Engine.InitParticles();
		Engine.InitGUI();
		//Engine.InitNetwork(doc);

		//Engine
		Engine.State = GameState.Login;

		//Particles
		ParticleEmitter pe = new ParticleEmitter(Engine.intMain.Width/2,
				Engine.intMain.Height, 500, 1000,
				new RPointD(-1f, -3f), "particle_fire");
		pe.a = 1;
		pe.r = 1;
		pe.g = 1;
		pe.b = 0;
		pe.Spread.x = Engine.intMain.Width/4;
		//pe.Filters.Add(new ParticleFilterGravity(new RPointF(0, 0.0001f)));
		pe.Filters.add(new ParticleFilterFade(-0.005f, 0.005f, -0.005f, -0.005f));
		pe.Filters.add(new ParticleFilterRotate());
		Engine.pecMain.add(pe);

		//Script

		SEM = new ScriptEngineManager();
		ScriptEngine SE = SEM.getEngineByName("JavaScript");

		try
		{
			SE.put("e_int", Engine.intMain);
			SE.eval(new FileReader("scripts/autoexec.js"));
			Invocable inv = (Invocable)SE;
			inv.invokeFunction("autoexec");
		}
		catch (Exception ex)
		{
			Logger.getLogger(Entry.class.getName()).log(Level.SEVERE, null, ex);
		}

		Engine.intMain.Start();
	}
}