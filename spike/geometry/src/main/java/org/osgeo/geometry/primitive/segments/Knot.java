/*
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2009 Department of Geography, University of Bonn
 *    (C) 2001-2009 lat/lon GmbH
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
package org.osgeo.geometry.primitive.segments;

/**
 * Used to define the basis functions of a {@link BSpline}.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class Knot {

    private double value;

    private int multiplicity;

    private double weight;

    /**
     * Creates a new {@link Knot} instance.
     * 
     * @param value
     * @param multiplicity
     * @param weight
     */
    public Knot( double value, int multiplicity, double weight ) {
        this.value = value;
        this.multiplicity = multiplicity;
        this.weight = weight;
    }

    /**
     * Returns the knot's value.
     * 
     * @return the knot's value
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns the knot's multiplicity.
     * 
     * @return the knot's multiplicity
     */    
    public int getMultiplicity() {
        return multiplicity;
    }

    /**
     * Returns the knot's weight.
     * 
     * @return the knot's weight
     */    
    public double getWeight() {
        return weight;
    }
}
