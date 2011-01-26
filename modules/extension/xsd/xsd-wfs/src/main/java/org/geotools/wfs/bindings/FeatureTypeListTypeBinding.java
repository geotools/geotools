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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;


/**
 * Binding object for the type http://www.opengis.net/wfs:FeatureTypeListType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="FeatureTypeListType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              A list of feature types available from  this server.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element minOccurs="0" name="Operations" type="wfs:OperationsType"/&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="FeatureType" type="wfs:FeatureTypeType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class FeatureTypeListTypeBinding extends AbstractComplexEMFBinding {
    public FeatureTypeListTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.FeatureTypeListType;
    }
}
