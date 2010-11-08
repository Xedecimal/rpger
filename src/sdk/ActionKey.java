package sdk;

import sdk.input.KeyHandler;

/**
 *
 * @author Xedecimal
 */
public class ActionKey
{
	public boolean Hold;
	public KeyHandler Handler;

	public ActionKey(KeyHandler handler, boolean hold)
	{
		Hold = hold;
		Handler = handler;
	}

	public void Handler() { }
}