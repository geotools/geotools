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
import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows11.DescriptionType;
import net.opengis.ows11.Ows11Factory;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.geotools.xsd.ows.OWS;

/**
 * Binding object for the type http://www.opengis.net/ows:DescriptionType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="DescriptionType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Human-readable descriptive information for the object it is included within.
 *  This type shall be extended if needed for specific OWS use to include additional metadata for each type of information. This type shall not be restricted for a specific OWS to change the multiplicity (or optionality) of some elements. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence&gt;
 *          &lt;element minOccurs="0" ref="ows:Title"/&gt;
 *          &lt;element minOccurs="0" ref="ows:Abstract"/&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:Keywords"/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class DescriptionTypeBinding extends AbstractComplexEMFBinding {

    Ows10Factory ows10Factory;

    public DescriptionTypeBinding(Ows10Factory factory) {
        super(factory);
        ows10Factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return OWS.DescriptionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return super.getType();
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        DescriptionType dt;

        // DescriptionType description;
        if (!(value instanceof DescriptionType)) {
            dt = Ows11Factory.eINSTANCE.createDescriptionType();
        } else {
            dt = (DescriptionType) value;
        }

        List<Node> children2 = node.getChildren("Keywords");
        for (Node c : children2) {
            dt.getKeywords().add(c.getValue());
        }
        List<Node> children3 = node.getChildren("Title");
        for (Node c : children3) {
            dt.getTitle().add(c.getValue());
        }
        List<Node> children4 = node.getChildren("Abstract");
        for (Node c : children4) {
            dt.getAbstract().add(c.getValue());
        }
        return dt;
    }
}
