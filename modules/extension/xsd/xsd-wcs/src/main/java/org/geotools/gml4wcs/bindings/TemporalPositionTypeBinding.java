/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 */

package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml4wcs.GML;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/gml:TemporalPositionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;simpleType name=&quot;TemporalPositionType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;
 *        Here we have collapsed the hierarchy of subtypes for temporal position in 19108
 *        by defining a union of simple types for indicating temporal position relative to a
 *        specific reference system.
 *        Date and time may be indicated with varying degrees of precision:
 *        year, year-month, date, or dateTime (all ISO 8601 format). Note
 *        that the dateTime type does not allow right-truncation (i.e. omitting
 *        seconds). An ordinal era may be referenced via URI, and a decimal value
 *        can be used to indicate the distance from the scale origin (e.g. UNIX time,
 *        GPS calendar).
 *        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;union memberTypes=&quot;dateTime date gYearMonth gYear anyURI decimal&quot;/&gt;
 *  &lt;/simpleType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class TemporalPositionTypeBinding extends AbstractSimpleBinding {

    /** @generated */
    public QName getTarget() {
        return GML.TemporalPositionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, value);
    }
}
