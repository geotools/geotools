package org.geotools.jtinv2.main;

/**
 * A set of methods implemented by TIN building blocks, like TIN points, TIN breaklines,
 * and TIN boundaries.
 */
public interface TinBuildingBlock 
{
	/**
	 * Defines the official type of TIN building blocks.
	 */
	public enum Type
	{
		TINPOINT,
		TINBREAKLINE,
		TINBOUNDARY
	}
	
	/**
	 * Returns an integer that uniquly identifies this building block within
	 * its type of building block and within the TIN.
	 */
	public abstract int getIdentifier();
	
	/**
	 * Returns the category of this building block. Categories can be used
	 * to organize TIN building blocks into layers.
	 */
	public abstract String getCategory();
	
	/**
	 * Returns a short description of this building block. For example: When land
	 * surveyors collect topographic data, they often describe the points created
	 * from their measurements with a short description.
	 */
	public abstract String getDescription();
}


