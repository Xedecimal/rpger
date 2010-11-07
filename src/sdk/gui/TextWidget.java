package sdk.gui;

import sdk.types.Rect;

/**
 * A widget with a value to hold text, a title bar or editable text.
 * @author Xedecimal
 */
public class TextWidget extends Widget
{
	/** The text associated with this TextWidget.*/
	public String Text;

	public TextWidget() { super(); }

	/**
	 * Create a TextWidget with the dimensions specified by the parameter r.
	 * @param r The initial position and size of this widget.
	 */
	public TextWidget(Rect r)
	{
		super(r);
	}

	/**
	 * Create a TextWidget out of a Rect and set the initial text to the text
	 * parameter.
	 * @param r The initial position and size of this widget.
	 * @param text The initial text of this widget
	 */
	public TextWidget(Rect r, String text)
	{
		super(r);
		Text = text;
	}

	@Override
	public String ToString()
	{
		return super.ToString() + " Text (" + Text + ")";
	}
}
