package sdk.gui;

/**
 *
 * @author Xedecimal
 */
public class WListViewHeader
{
	public String Text;
	public int Width;

	public WListViewHeader(String text)
	{
		Text = text;
		Width = text.length() * 16;
	}
}
