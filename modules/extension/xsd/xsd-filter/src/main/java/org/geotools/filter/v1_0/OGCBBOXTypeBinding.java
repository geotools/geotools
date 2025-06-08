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
package org.geotools.filter.v1_0;

import javax.xml.namespace.QName;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.GML;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Envelope;

/**
 * Binding object for the type http://www.opengis.net/ogc:BBOXType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="BBOXType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="ogc:PropertyName"/&gt;
 *                  &lt;xsd:element ref="gml:Box"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class OGCBBOXTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;
    private CoordinateReferenceSystem crs;

    public OGCBBOXTypeBinding() {
        // (JD) TODO: fix this. The reason we dont use constructor injection to get
        // the factory is that pico does not do both setter + constructor injection
        // And since we support setter injection of a crs we just fall back on
        // common factory finder... since there is actually only one filter factory
        // impl not a huge deal, but it woul dbe nice to be consistent
        factory = CommonFactoryFinder.getFilterFactory(null);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return OGC.BBOXType;
    }

    /**
     * Setter for crs.
     *
     * <p>This is used to allow containing entities (liek a wfs query) to provide a coordinate reference system in the
     * context.
     */
    public void setCRS(CoordinateReferenceSystem crs) {
        this.crs = crs;
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
        return BBOX.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: crs
        PropertyName propertyName = node.getChildValue(PropertyName.class);
        Envelope box = node.getChildValue(Envelope.class);

        if (box instanceof ReferencedEnvelope) {
            return factory.bbox(propertyName == null ? factory.property("") : propertyName, (ReferencedEnvelope) box);
        } else {
            String name = null;
            if (propertyName != null) {
                name = propertyName.getPropertyName();
            }
            // JD: this is a bit hackish, we know that "" means default geometry
            // in SimpleFeaturePropertyAccessor, so instead of dying here set
            // to empty string to mean defualt geometry
            // TODO: come up with something a bit more concrete
            if (name == null) {
                name = "";
            }

            Node srsNode = node.getChild(Envelope.class).getAttribute("srsName");
            String srs = srsNode != null ? srsNode.getValue().toString() : null;

            if (srs == null && crs != null) {
                srs = GML2EncodingUtils.toURI(crs);
            }

            return factory.bbox(name, box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY(), srs);
        }
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        BBOX box = (BBOX) object;

        // &lt;xsd:element ref="ogc:PropertyName"/&gt;
        if (OGC.PropertyName.equals(name)) {
            return box.getExpression1();
        }

        // &lt;xsd:element ref="gml:Box"/&gt;
        if (GML.Box.equals(name) || org.geotools.gml3.GML.Envelope.equals(name)) {
            Envelope env = box.getExpression2().evaluate(null, ReferencedEnvelope.class);
            if (env == null) {
                env = box.getExpression2().evaluate(null, Envelope.class);
            }
            return env;
        }

        return null;
    }
}
