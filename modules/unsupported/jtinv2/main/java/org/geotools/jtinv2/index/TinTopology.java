package org.geotools.jtinv2.index;

import java.util.List;

import org.geotools.jtinv2.main.TinFace;
import org.geotools.jtinv2.main.TriangulatedIrregularNetwork;

import com.vividsolutions.jts.geom.Coordinate;

public interface TinTopology 
{
	public abstract TinFace[] getAdjacentFaces(TinFace argTarget);
}
