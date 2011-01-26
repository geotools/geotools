/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.validation.relate;

import org.geotools.validation.DefaultIntegrityValidation;

/**
 * @author bowens
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * @source $URL$
 */
public abstract class RelationIntegrity extends DefaultIntegrityValidation 
{
	protected final static String EMPTY = "";
    private String geomTypeRefA;
    private String geomTypeRefB = EMPTY;
    protected boolean expected = false;			// what the user expects this test to return
    											// they could expect it to fail and return false, therefore they would enter false

    /**
     * PointCoveredByLineValidation constructor.
     * 
     * <p>
     * Super
     * </p>
     */
    public RelationIntegrity() {
        super();
    }

    /**
     * Implementation of getTypeNames. Should be called by sub-classes is being
     * overwritten.
     *
     * @return Array of typeNames, or empty array for all, null for disabled
     *
     * @see org.geotools.validation.Validation#getTypeRefs()
     */
    public String[] getTypeRefs() 
    {
        if (geomTypeRefA == null) 
            return null;

        if (geomTypeRefB == null || geomTypeRefB.equals(EMPTY))
        	return new String[] {geomTypeRefA};
        
        return new String[] { geomTypeRefA, geomTypeRefB };
    }

    /**
     * Access polygonTypeRef property.
     *
     * @return Returns the polygonTypeRef.
     */
    public final String getGeomTypeRefA() {
        return geomTypeRefA;
    }

    /**
     * Set polygonTypeRef to polygonTypeRef.
     *
     * @param typeRefA A String with the polygonTypeRef to set.
     */
    public final void setGeomTypeRefA(String typeRefA) {
        this.geomTypeRefA = typeRefA;
    }

    /**
     * Access restrictedPolygonTypeRef property.
     *
     * @return Returns the restrictedPolygonTypeRef.
     */
    public final String getGeomTypeRefB() {
        return geomTypeRefB;
    }

    /**
     * Set restrictedPolygonTypeRef to restrictedPolygonTypeRef.
     *
     * @param typeRefB A String with the restrictedPolygonTypeRef to set.
     */
    public final void setGeomTypeRefB(String typeRefB) 
    {
    	if (typeRefB.equals("") || typeRefB == null)
    		typeRefB = EMPTY;
    	else
    		this.geomTypeRefB = typeRefB;
    }
    
    
	public final void setExpected(String exp)
    {
    	if (exp == null)
    		expected = false;
    	else if (exp.equalsIgnoreCase("true") || exp.equalsIgnoreCase("t"))
    		expected = true;
    	else
    		expected = false;
    }
    
    public final void setExpected(boolean arg){
    	expected = arg;
    }
    
    public final boolean isExpected() {
   		return expected;
    }

}
