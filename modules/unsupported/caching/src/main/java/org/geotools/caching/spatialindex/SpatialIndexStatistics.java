package org.geotools.caching.spatialindex;
/** 
 * 
 * Data structure to store statistics about a spatial index.
 * 
 * Tracks:
 * <li>Number of reads.</li>
 * <li>Number of writes.</li>
 * <li>Number of nodes.</li>
 * <li>Size of data.</li>
*
* @author Christophe Rousson, SoC 2007, CRG-ULAVAL
*
 *
 * @source $URL$
*/
public class SpatialIndexStatistics implements Statistics {

	int stats_reads = 0;
	int stats_writes = 0;
	int stats_nodes = 0;
	int stats_data = 0;

	public long getNumberOfData() {
		return stats_data;
	}

	public long getNumberOfNodes() {
		return stats_nodes;
	}

	public long getReads() {
		return stats_reads;
	}

	public long getWrites() {
		return stats_writes;
	}

	public void addToReadsCounter(int count) {
		stats_reads += count;
	}

	public void addToWritesCounter(int count) {
		stats_writes += count;
	}

	public void addToNodesCounter(int count) {
		stats_nodes += count;
	}

	public void addToDataCounter(int count) {
		stats_data += count;
	}

	public void reset() {
		stats_reads = 0;
		stats_writes = 0;
		stats_nodes = 0;
		stats_data = 0;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Reads = " + stats_reads);
		sb.append(" ; Writes = " + stats_writes);
		sb.append(" ; Nodes = " + stats_nodes);
		sb.append(" ; Data = " + stats_data);

		return sb.toString();
	}

}
