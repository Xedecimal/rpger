/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.gui;

/**
 *
 * @author Xedecimal
 */
public enum ClickResponse 
{
	/** No response to the mouse, allow it to fall through the widget to the
	 * next object.
	 */
	None,
	/** Request that the GUI make this widget the current dragging object. */
	Drag,
	/** Request that the GUI set the current keyboard and mouse focus on this
	 * object. */
	Focus,
	/** Request that the GUI terminate this window. */
	Close
}