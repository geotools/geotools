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

package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.Wcs10Factory;
import org.geotools.wcs.WCS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:DomainSubsetType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name=&quot;DomainSubsetType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Defines the desired subset of the domain set of the coverage. Is a GML property containing either or both spatialSubset and temporalSubset GML objects. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;choice&gt;
 *          &lt;sequence&gt;
 *              &lt;element ref=&quot;wcs:spatialSubset&quot;/&gt;
 *              &lt;element minOccurs=&quot;0&quot; ref=&quot;wcs:temporalSubset&quot;/&gt;
 *          &lt;/sequence&gt;
 *          &lt;element ref=&quot;wcs:temporalSubset&quot;/&gt;
 *      &lt;/choice&gt;
 *  &lt;/complexType&gt;
 *
 * </code>
 *  </pre>
 *
 * @generated
 */
public class DomainSubsetTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.DomainSubsetType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DomainSubsetType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        DomainSubsetType domainSubset = Wcs10Factory.eINSTANCE.createDomainSubsetType();

        SpatialSubsetType spatialSubset = (SpatialSubsetType) node.getChildValue("spatialSubset");
        if (spatialSubset != null) domainSubset.setSpatialSubset(spatialSubset);
        return domainSubset;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.xsd.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }
}
