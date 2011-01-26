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
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/wfs:LockFeatureResponseType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="LockFeatureResponseType"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;
 *              The LockFeatureResponseType is used to define an
 *              element to contains the response to a LockFeature
 *              operation.
 *           &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element ref="wfs:LockId"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The LockFeatureResponse includes a LockId element
 *                    that contains a lock identifier.  The lock identifier
 *                    can be used by a client, in subsequent operations, to
 *                    operate upon the locked feature instances.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element minOccurs="0" name="FeaturesLocked" type="wfs:FeaturesLockedType"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    The LockFeature or GetFeatureWithLock operations
 *                    identify and attempt to lock a set of feature
 *                    instances that satisfy the constraints specified
 *                    in the request.  In the event that the lockAction
 *                    attribute (on the LockFeature or GetFeatureWithLock
 *                    elements) is set to SOME, a Web Feature Service will
 *                    attempt to lock as many of the feature instances from
 *                    the result set as possible.
 *
 *                    The FeaturesLocked element contains list of ogc:FeatureId
 *                    elements enumerating the feature instances that a WFS
 *                    actually managed to lock.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
 *          &lt;xsd:element minOccurs="0" name="FeaturesNotLocked" type="wfs:FeaturesNotLockedType"&gt;
 *              &lt;xsd:annotation&gt;
 *                  &lt;xsd:documentation&gt;
 *                    In contrast to the FeaturesLocked element, the
 *                    FeaturesNotLocked element contains a list of
 *                    ogc:Filter elements identifying feature instances
 *                    that a WFS did not manage to lock because they were
 *                    already locked by another process.
 *                 &lt;/xsd:documentation&gt;
 *              &lt;/xsd:annotation&gt;
 *          &lt;/xsd:element&gt;
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
public class LockFeatureResponseTypeBinding extends AbstractComplexEMFBinding {
    public LockFeatureResponseTypeBinding(WfsFactory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.LockFeatureResponseType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
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
