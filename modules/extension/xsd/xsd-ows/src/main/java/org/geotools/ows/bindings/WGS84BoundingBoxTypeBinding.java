/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.bindings;

import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.WGS84BoundingBoxType;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ows:WGS84BoundingBoxType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType name="WGS84BoundingBoxType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML encoded minimum rectangular bounding box (or region) parameter, surrounding all the associated data. This box is specialized for use with the 2D WGS 84 coordinate reference system with decimal values of longitude and latitude. &lt;/documentation&gt;
 *          &lt;documentation&gt;This type is adapted from the general BoundingBoxType, with modified contents and documentation for use with the 2D WGS 84 coordinate reference system. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;restriction base="ows:BoundingBoxType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element name="LowerCorner" type="ows:PositionType2D"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Position of the bounding box corner at which the values of longitude and latitude normally are the algebraic minimums within this bounding box. For more information, see Subclauses 10.4.5 and C.13. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element name="UpperCorner" type="ows:PositionType2D"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Position of the bounding box corner at which the values of longitude and latitude normally are the algebraic minimums within this bounding box. For more information, see Subclauses 10.4.5 and C.13. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute fixed="urn:ogc:def:crs:OGC:2:84" name="crs"
 *                  type="anyURI" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;This attribute can be included when considered useful. When included, this attribute shall reference the 2D WGS 84 coordinate reference system with longitude before latitude and decimal values of longitude and latitude. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *              &lt;attribute fixed="2" name="dimensions"
 *                  type="positiveInteger" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The number of dimensions in this CRS (the length of a coordinate sequence in this use of the PositionType). This number is specified by the CRS definition, but can also be specified here. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/restriction&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class WGS84BoundingBoxTypeBinding extends AbstractComplexEMFBinding {
    public WGS84BoundingBoxTypeBinding(Ows10Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS.WGS84BoundingBoxType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
