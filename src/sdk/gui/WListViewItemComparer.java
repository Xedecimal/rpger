/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sdk.gui;

import java.util.Comparator;

/**
 *
 * @author Xedecimal
 */
public class WListViewItemComparer implements Comparator<WListViewItem>
{
	private int m_head;
	public WListViewItemComparer(int head)
	{
		m_head = head;
	}

	public int compare(WListViewItem x, WListViewItem y)
	{
		return x.Cols[m_head].compareTo(y.Cols[m_head]);
	}
}
