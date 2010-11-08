package sdk;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Xedecimal
 */
public class Config
{
	private Document m_doc;
	private XPath m_xpath;

	public Config(String filename)
	{
		File f = new File(filename);
		if (f.exists())
		{
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				m_doc = db.parse(f);
			} catch (SAXException ex) {
				Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (ParserConfigurationException ex) {
				Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		XPathFactory factory = XPathFactory.newInstance();
		m_xpath = factory.newXPath();
	}

	public NodeList get(String xpath)
	{
		XPathExpression expr;
		try {
			expr = m_xpath.compile(xpath);
			return (NodeList)expr.evaluate(m_doc, XPathConstants.NODESET);
		} catch (XPathExpressionException ex) {
			Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}
}
