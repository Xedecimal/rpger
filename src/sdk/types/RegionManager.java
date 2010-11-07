package sdk.types;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xedecimal
 */
public class RegionManager
{
	HashMap<String, RegionSet> m_regions;

	public RegionManager() { m_regions = new HashMap<String, RegionSet>(); }

	public RegionSet GetRegions(String name)
	{
		if (m_regions.containsKey(name)) return m_regions.get(name);
		else
		{
			RegionSet rs;
			rs = new RegionSet(name);
			m_regions.put(name, rs);
			return rs;
		}
	}
}
