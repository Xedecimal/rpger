package sdk.gui;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Xedecimal
 */
public class WListViewItemCollection extends ArrayList<WListViewItem>
{
	public void Add(String[] cols)
	{
		WListViewItem wlvi = new WListViewItem();
		wlvi.Cols = cols;
		add(wlvi);
	}

	public void Sort(int head)
	{
		Collections.sort(this, new WListViewItemComparer(head));
	}
}
