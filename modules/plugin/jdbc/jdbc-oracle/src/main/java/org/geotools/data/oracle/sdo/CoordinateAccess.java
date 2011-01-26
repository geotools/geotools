/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

import com.vividsolutions.jts.geom.CoordinateSequence;


/**
 * Allows manipulation of a Geometry's CoordianteSequece.
 * 
 * <p>
 * The number of ordinates in each Coordiante is getDimension() + getNumAttributes()<br>
 * The examples given in this interface are based on a 2-dimensional
 * coordinate system, x and y,<br>
 * with two attributes: z and t.<br>
 * In this case z is the third dimension, but is not used for rendering and
 * the coordinate<br>
 * remains 2-dimensional.
 * </p>
 *
 * @author jgarnett
 * @source $URL$
 */
 public interface CoordinateAccess extends CoordinateSequence {
    /**
     * Retrieve the number of spatial dimensions of Coordiantes.
     * 
     * <p>
     * This is the number of spatially significant ordiantes (usually 2 or 3)
     * </p>
     *
     * @return Number of spatially signficant ordinates
     */
    int getDimension();

    /**
     * Retrieve number of measures associated with a Coordiantes
     * 
     * <p>
     * This is the number of non spatially significant ordinates.
     * </p>
     *
     * @return Number of measures, or 0 if measures not used
     */
     int getNumAttributes();

    /**
     * Retrive a single ordinate
     *
     * @param coordinate Coordinate to retrive from
     * @param ordinate Ordinate to retrive from coordiante
     *
     * @return Specified ordinate
     */
     double getOrdinate(int coordinate, int ordinate);

    /**
     * getAttribute purpose.
     * 
     * <p>
     * attribute is between 0 and getNumAttributes()
     * </p>
     *
     * @param coordinate
     * @param attribute is between 0 and getNumAttributes()
     *
     */
     Object getAttribute(int coordinate, int attribute);

    /**
     * Set a single ordinate.
     *
     * @param coordinate Corrdinate to modify
     * @param ordinate Ordinate to modify in coordinate
     * @param value new value
     */
     void setOrdinate(int coordinate, int ordinate, double value);

    /**
     * setAttribute purpose.
     * 
     * <p>
     * attribute is between 0 and getNumAttributes()
     * </p>
     *
     * @param coordinate the coordinate to be modified
     * @param attribute between 0 and getNumAttributes()
     * @param value
     */
     void setAttribute(int coordinate, int attribute, Object value);

    /**
     * Retrive ordinates as an array.
     * 
     * <p>
     * Example: (x,y) getDimension()==2<br>
     * This is defined for the number of dimensions. If the other attributes
     * happen to be a double, they can still be accessed by using an ordinate
     * value geater than getDimension().
     * </p>
     * 
     * <ul>
     * <li>
     * ordinate 0: x ordinate
     * </li>
     * <li>
     * ordinate 1: y ordinate
     * </li>
     * <li>
     * ordinate 2: m attribute
     * </li>
     * <li>
     * ordinate 3: g attribute
     * </li>
     * </ul>
     * 
     *
     * @param ordinate Ordinate to retrieve. ordinate is less than <br>
     *        getDimension()+getMeasures() if the measures are doubles as
     *        well. Otherwise<br>
     *        ordinate is less than getDimensions().
     *
     * @return ordinate array
     */
     double[] toOrdinateArray(int ordinate);

    /**
     * toAttributeArray purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param attribute Between 0 and getNumAttrributes()
     *
     * @return an array of attributes
     */
     Object[] toAttributeArray(int attribute);

    /**
     * Supplies an array of ordiantes.
     * 
     * <p>
     * The ordinateArray should be the same length as the CoordinateSequence.<br>
     * ordinate should be between 0 and getDimension().<br>
     * If the attributes are doubles as well, then ordinate can be as great as<br>
     * getDimension() + getNumAttributes().
     * </p>
     */
     void setOrdinateArray(int ordinate, double[] ordinateArray);

    /**
     * setAttributeArray purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param attribute between 0 and getNumAttributes()
     * @param attributeArray May be an object or primative array
     */
     void setAttributeArray(int attribute, Object attributeArray);

    /**
     * Retrieve ordinate information (an array of ordinates for each
     * coordinate).
     * 
     * <p>
     * Example: (x,y,m,g) getDimension()==2, getNumAttributes()==2
     * </p>
     * <pre><code>
     * [ [ x1, x2,...,xN], [ y1, y2,...,yN] ]
     * </code></pre>
     *
     * @return column major ordinate arrays (these are spatially significant)
     */
     double[][] toOrdinateArrays();

    /**
     * Retrieve Attribute information (an array of attributes for each
     * coordiante).
     * 
     * <p>
     * Example: (x,y,m,g) getDimension()==2, getAttributes()==2
     * </p>
     * <pre><code>
     * [ [ m1, m2,...,mN], [ g1, g2,..., gN] ]
     * </code></pre>
     * 
     * <p></p>
     *
     * @return Attribute Arrays, may be object or primative arrays
     */
     Object[] toAttributeArrays();

    /**
     * Completely replace sequence with the provided information.
     * 
     * <p>
     * Example: (x,y,m,g) getDimension()==2, getNumAttributes()==2
     * </p>
     * <pre><code>
     * <b>dimensions</b>:[ [ x1, x2,...,xN], [ y1, y2,...,yN] ]
     * <b>attributes</b>:[ [ m1, m2,...,mN], [ g1, g2,..., gN] ]
     * </code></pre>
     *
     * @param ordinateArrays  dimensions column major ordinate arrays 
     *                        (these are spatially significant)
     * @param attributeArrays Indiviual attribute arrays may be primative or
     *                        object arrays
     */
     void setCoordinateArrays(double[][] ordinateArrays,
        Object[] attributeArrays);

    /**
     * Allows modification of a single coordinate (including attribute values).
     * 
     * <p>
     * Normal Use: where D is getDimensions() and N is getNumAttributes():
     * </p>
     * <pre><code>
     * <b>dimensions</b>: [ ordX, ordY, ..., ordD ]
     * <b>attributes</b>: [ atr1, atr2, ...., atrN ]
     * </code></pre>
     * 
     * <p>
     * When dealing with attributes that are all double values the ordinates
     * array may be used to set both ordinates and attribute values.
     * </p>
     * 
     * <p>
     * Optimized Use: where D is getDimensions() and N is getNumAttributes():
     * </p>
     * <pre><code>
     * <b>dimensions</b>: [ ordX, ordY, ..., ordD, atr1, atr2, ... attrN ]
     * <b>attributes</b>: null
     * </code></pre>
     * 
     * <p></p>
     *
     * @param coordinate index of coordiante to be modified
     * @param ordinates array ordinate values (may be extended with attribute
     *        values)
     * @param attributes array of attribute values, or null is ordinates has
     *        been extended
     */
     void setAt(int coordinate, double[] ordinates, Object[] attributes);
}
