/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.types;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.newdawn.slick.opengl.Texture;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sdk.Engine;

/**
 * A set of regions an associated texture for a single image.
 * @author Xedecimal
 */
public class RegionSet extends ArrayList<RectD>
{
	public String Name;

	/** OpenGL texture id of this RegionSet. */
	public Texture Texture;

	/**
	 * Initializes this RegionSet with the name.png and name.xml that holds
	 * definitions for regions inside name.png.
	 * @param name Name of RegionSet to load.
	 */
	public RegionSet(String name)
	{
		Name = name;
		File fpng = new File("img/" + name + ".png");
		File fxml = new File("xml/" + name + ".xml");
		if (!fpng.exists())
			Logger.getLogger(RegionSet.class.getName()).log(Level.SEVERE, null, "Texture not found.");

		try
		{
			// Load the texture.
			Texture = Engine.intMain.GetSurface("img/" + name + ".png");

			// Load the regions.
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(fxml);
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			XPathExpression xpe = xp.compile("//region");
			NodeList result = (NodeList)xpe.evaluate(doc, XPathConstants.NODESET);
			for (int ix = 0; ix < result.getLength(); ix++)
			{
				Node n = result.item(ix);

				float x = Float.parseFloat(n.getAttributes().getNamedItem("x").getNodeValue()) / Texture.getImageWidth();
				float y = Float.parseFloat(n.getAttributes().getNamedItem("y").getNodeValue()) / Texture.getImageHeight();
				float w = Float.parseFloat(n.getAttributes().getNamedItem("w").getNodeValue()) / Texture.getImageWidth();
				float h = Float.parseFloat(n.getAttributes().getNamedItem("h").getNodeValue()) / Texture.getImageHeight();
				add(new RectD(x, y, w, h));
			}
		}
		catch (Exception ex)
		{
			Logger.getLogger(RegionSet.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Draw a graphic.
	 * @param id Region ID in this RegionSet to draw.
	 * @param rDst Destination coordinates to draw.
	 * @param z Destination depth to draw.
	 */
	public void Draw(int id, RectD rDst, double z)
	{
		//Texture.bind();
		Interface.Draw(Texture, this.get(id), rDst, z);
	}
}
