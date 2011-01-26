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

import java.util.List;

import net.opengis.ows10.Ows10Factory;
import javax.xml.namespace.QName;
import org.geotools.ows.OWS;
import org.geotools.xml.*;


/**
 * Binding object for the type http://www.opengis.net/ows:PositionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;simpleType name="PositionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Position instances hold the coordinates of a position in a coordinate reference system (CRS) referenced by the related "crs" attribute or elsewhere. For an angular coordinate axis that is physically continuous for multiple revolutions, but whose recorded values can be discontinuous, special conditions apply when the bounding box is continuous across the value discontinuity:
 *  a)  If the bounding box is continuous clear around this angular axis, then ordinate values of minus and plus infinity shall be used.
 *  b)  If the bounding box is continuous across the value discontinuity but is not continuous clear around this angular axis, then some non-normal value can be used if specified for a specific OWS use of the BoundingBoxType. For more information, see Subclauses 10.2.5 and C.13. &lt;/documentation&gt;
 *          &lt;documentation&gt;This type is adapted from DirectPositionType and doubleList of GML 3.1. The adaptations include omission of all the attributes, since the needed information is included in the BoundingBoxType. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;list itemType="double"/&gt;
 *  &lt;/simpleType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class PositionTypeBinding extends AbstractSimpleBinding {
    public PositionTypeBinding() {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OWS.PositionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return List.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
        //TODO: implement and remove call to super
        return super.parse(instance, value);
    }
    
    @Override
    public String encode(Object object, String value) throws Exception {
        List list = (List) object;
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o).append(" ");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
        
    }
    
}
