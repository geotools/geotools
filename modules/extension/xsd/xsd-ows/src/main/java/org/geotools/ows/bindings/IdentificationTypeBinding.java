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

import javax.xml.namespace.QName;
import net.opengis.ows10.Ows10Factory;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.geotools.xsd.ows.OWS;

/**
 * Binding object for the type http://www.opengis.net/ows:IdentificationType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="IdentificationType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;General metadata identifying and describing a set of data. This type shall be extended if needed for each specific OWS to include additional metadata for each type of dataset. If needed, this type should first be restricted for each specific OWS to change the multiplicity (or optionality) of some elements. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="ows:DescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" ref="ows:Identifier"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Optional unique identifier or name of this dataset. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:BoundingBox"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of zero or more bounding boxes whose union describes the extent of this dataset. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:OutputFormat"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of zero or more references to data formats supported for server outputs. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:AvailableCRS"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Unordered list of zero or more available coordinate reference systems. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="ows:Metadata"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Optional unordered list of additional metadata about this data(set). A list of optional metadata elements for this data identification could be specified in the Implementation Specification for this service. &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class IdentificationTypeBinding extends AbstractComplexEMFBinding {
    public IdentificationTypeBinding(Ows10Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return OWS.IdentificationType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
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
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}
