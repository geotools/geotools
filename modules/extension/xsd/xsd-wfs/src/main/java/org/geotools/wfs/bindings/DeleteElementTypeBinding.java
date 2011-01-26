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

import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;


/**
 * Binding object for the type http://www.opengis.net/wfs:DeleteElementType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="DeleteElementType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="1" minOccurs="1" ref="ogc:Filter"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The Filter element is used to constrain the scope
 *                    of the delete operation to those features identified
 *                    by the filter.  Feature instances can be specified
 *                    explicitly and individually using the identifier of
 *                    each feature instance OR a set of features to be
 *                    operated on can be identified by specifying spatial
 *                    and non-spatial constraints in the filter.
 *                    If no filter is specified then an exception should
 *                    be raised since it is unlikely that a client application
 *                    intends to delete all feature instances.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="handle" type="xsd:string" use="optional"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                 The handle attribute allows a client application
 *                 to assign a client-generated request identifier
 *                 to an Insert action.  The handle is included to
 *                 facilitate error reporting.  If a Delete action
 *                 in a Transaction request fails, then a WFS may
 *                 include the handle in an exception report to localize
 *                 the error.  If no handle is included of the offending
 *                 Insert element then a WFS may employee other means of
 *                 localizing the error (e.g. line number).
 *              &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
 *      &lt;xsd:attribute name="typeName" type="xsd:QName" use="required"&gt;
 *          &lt;xsd:annotation&gt;
 *              &lt;xsd:documentation&gt;
 *                The value of the typeName attribute is the name
 *                of the feature type to be updated. The name
 *                specified must be a valid type that belongs to
 *                the feature content as defined by the GML
 *                Application Schema.
 *             &lt;/xsd:documentation&gt;
 *          &lt;/xsd:annotation&gt;
 *      &lt;/xsd:attribute&gt;
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
public class DeleteElementTypeBinding extends AbstractComplexEMFBinding {
    public DeleteElementTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.DeleteElementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DeleteElementType.class;
    }
}
