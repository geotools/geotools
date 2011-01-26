package org.geotools.caching.grid.spatialindex;

import org.geotools.caching.spatialindex.SpatialIndexStatistics;

/**
 * Class to track statistics about a grid spatial index.

 * <p>In addition to all statistics tracked
 * by the default spatial index statistics this
 * also tracks the number of evictions.</p>
 *
 *
 * @source $URL$
 */
public class GridSpatialIndexStatistics extends SpatialIndexStatistics {
	int stats_evictions = 0;

	public void addToEvictionCounter(int count) {
		stats_evictions += count;
	}

	public int getEvictions() {
		return stats_evictions;
	}

	@Override
	public void reset() {
		stats_evictions = 0;
		super.reset();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(super.toString());
		sb.append(" ; Evictions = " + stats_evictions);

		return sb.toString();
	}

}
