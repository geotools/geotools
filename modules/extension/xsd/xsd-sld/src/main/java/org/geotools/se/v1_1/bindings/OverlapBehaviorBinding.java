/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.style.OverlapBehaviorEnum;
import org.geotools.se.v1_1.SE;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the element http://www.opengis.net/se:OverlapBehavior.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="OverlapBehavior"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *          "OverlapBehavior" tells a system how to behave when multiple
 *          raster images in a layer overlap each other, for example with
 *          satellite-image scenes.
 *        &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:simpleType&gt;
 *          &lt;xsd:restriction base="xsd:string"&gt;
 *              &lt;xsd:enumeration value="LATEST_ON_TOP"/&gt;
 *              &lt;xsd:enumeration value="EARLIEST_ON_TOP"/&gt;
 *              &lt;xsd:enumeration value="AVERAGE"/&gt;
 *              &lt;xsd:enumeration value="RANDOM"/&gt;
 *          &lt;/xsd:restriction&gt;
 *      &lt;/xsd:simpleType&gt;
 *  &lt;/xsd:element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class OverlapBehaviorBinding extends AbstractSimpleBinding {

    /** @generated */
    @Override
    public QName getTarget() {
        return SE.OverlapBehavior;
    }

    @Override
    public Class getType() {
        return OverlapBehaviorEnum.class;
    }

    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        OverlapBehaviorEnum overlap = OverlapBehaviorEnum.valueOf((String) value);
        if (overlap == null) {
            throw new IllegalArgumentException("Overlap behaviour " + value + " not supported");
        }

        return overlap;
    }
}
