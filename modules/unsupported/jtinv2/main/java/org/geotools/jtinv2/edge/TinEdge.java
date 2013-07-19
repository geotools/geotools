package org.geotools.jtinv2.edge;

import org.geotools.jtinv2.main.ThreeDimensionalLine;

public class TinEdge
{
	private int identifier;
	private ThreeDimensionalLine line;
	
	public TinEdge(int argIdentifier, ThreeDimensionalLine argLine)
	{
		this.identifier = argIdentifier;
		this.line = argLine;
	}
	
	public int getIdentifier()
	{
		return this.identifier;
	}
	
	public ThreeDimensionalLine get3dLine()
	{
		return this.line;
	}
}
