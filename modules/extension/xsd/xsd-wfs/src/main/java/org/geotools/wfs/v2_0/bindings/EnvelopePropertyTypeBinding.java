/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.wfs20.EnvelopePropertyType;
import net.opengis.wfs20.Wfs20Factory;
import net.opengis.wfs20.Wfs20Package;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class EnvelopePropertyTypeBinding extends AbstractComplexBinding {

    private static Class<ReferencedEnvelope> referencedEnvelopeClass = ReferencedEnvelope.class;
    private static EClass newClass = EcoreFactory.eINSTANCE.createEClass();
    private static EAttribute newAttribute = EcoreFactory.eINSTANCE.createEAttribute();
    private static EAttribute anyAttribute = Wfs20Package.eINSTANCE.getEnvelopePropertyType_Any();

    static {
        newClass.setInstanceClass(referencedEnvelopeClass);
        newAttribute.setEType(newClass);
    }

    public EnvelopePropertyTypeBinding() {
        super();
    }

    @Override
    public QName getTarget() {
        return WFS.EnvelopePropertyType;
    }

    @Override
    public Class<?> getType() {
        return referencedEnvelopeClass;
    }

    @Override
    public List<Object[]> getProperties(Object object, XSDElementDeclaration element)
            throws Exception {
        List<Object[]> l = new ArrayList<>();
        l.add(new Object[] {GML.Envelope, object});
        return l;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        EnvelopePropertyType envelopPropertyType =
                Wfs20Factory.eINSTANCE.createEnvelopePropertyType();

        Object childValue = node.getChildValue(referencedEnvelopeClass);
        if (childValue != null) {
            // FeatureMap below requires FeatureMap.Entry as members each having FeatureMap.Entry as
            // value.
            // Considering the respective schema fragment this is somewhat surprising.
            // Maybe a generator or EMF or issue?
            // Assuming "boundedBy" is rarely used it seems acceptable to nest it twice.
            //
            //   <xsd:element name="boundedBy" type="wfs:EnvelopePropertyType"/>
            //   <xsd:complexType name="EnvelopePropertyType">
            //      <xsd:sequence>
            //         <xsd:any namespace="##other"/>
            //      </xsd:sequence>
            //   </xsd:complexType>
            FeatureMap anyMap = envelopPropertyType.getAny();
            Entry innerEntry = FeatureMapUtil.createEntry(newAttribute, childValue);
            Entry anyMapEntry = FeatureMapUtil.createEntry(anyAttribute, innerEntry);
            anyMap.add(0, anyMapEntry);
        }
        return envelopPropertyType;
    }
}
