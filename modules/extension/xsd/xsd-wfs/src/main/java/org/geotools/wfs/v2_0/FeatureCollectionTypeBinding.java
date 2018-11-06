/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0;

import javax.xml.namespace.QName;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.wfs.bindings.WFSParsingUtils;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:FeatureCollectionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:complexType name="FeatureCollectionType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="wfs:SimpleFeatureCollectionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" name="AdditionalObjects" type="wfs:SimpleValueCollectionType"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="wfs:TruncatedResponse"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attributeGroup ref="wfs:StandardResponseParameters"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class FeatureCollectionTypeBinding extends AbstractComplexEMFBinding {
    private static final String UNKNOWN = "unknown";
    boolean generateBounds;

    private Encoder encoder;

    public FeatureCollectionTypeBinding(Wfs20Factory factory, Configuration configuration) {
        this(factory, configuration, null);
    }

    public FeatureCollectionTypeBinding(
            Wfs20Factory factory, Configuration configuration, Encoder encoder) {
        super(factory);
        this.generateBounds = true;
        this.encoder = encoder;
        if (configuration != null) {
            this.generateBounds =
                    !configuration.getProperties().contains(GMLConfiguration.NO_FEATURE_BOUNDS);
        }
    }

    /** @generated */
    public QName getTarget() {
        return WFS.FeatureCollectionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return WFSParsingUtils.FeatureCollectionType_parse(
                (EObject) super.parse(instance, node, value), instance, node);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("boundedBy".equals(name.getLocalPart()) && !generateBounds) {
            return null;
        }
        Object result = null;
        if (!WFSParsingUtils.features((EObject) object).isEmpty()) {
            result = WFSParsingUtils.FeatureCollectionType_getProperty((EObject) object, name);
            if (result instanceof SimpleFeatureCollection
                    && encoder.getConfiguration()
                            .hasProperty(GMLConfiguration.OPTIMIZED_ENCODING)) {
                return new WFS20FeatureCollectionEncoderDelegate(
                        (SimpleFeatureCollection) result, encoder);
            }
        }
        if (result == null) {
            result = super.getProperty(object, name);
        }
        if ("numberMatched".equals(name.getLocalPart())) {
            if (result == null || !(result instanceof Number)) {
                return UNKNOWN;
            } else if (result instanceof Number) {
                long numberMatched = ((Number) result).longValue();
                if (numberMatched < 0) {
                    return UNKNOWN;
                } else {
                    return numberMatched;
                }
            }
        }
        return result;
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if ("member".equalsIgnoreCase(property)) {
            // ignore feature, handled in parse()
        } else {
            super.setProperty(eObject, property, value, lax);
        }
    }
}
