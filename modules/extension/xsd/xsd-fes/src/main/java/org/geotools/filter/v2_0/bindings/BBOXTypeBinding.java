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
package org.geotools.filter.v2_0.bindings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.geotools.filter.v1_0.OGCBBOXTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.v3_2.GML;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Binding object for the type http://www.opengis.net/ogc:BBOXType.
 *
 * <p>
 *
 * <pre>
 *       <code>
 *  &lt;xsd:complexType name="BBOXType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" ref="fes:expression"/&gt;
 *                  &lt;xsd:any namespace="##other"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *        </code>
 *         </pre>
 *
 * @generated
 */
public class BBOXTypeBinding extends OGCBBOXTypeBinding {

    static final XSDParticle ENVELOPE_PARTICLE;

    static {
        ENVELOPE_PARTICLE = XSDFactory.eINSTANCE.createXSDParticle();
        ENVELOPE_PARTICLE.setMinOccurs(0);
        ENVELOPE_PARTICLE.setMaxOccurs(-1);
        try {
            ENVELOPE_PARTICLE.setContent(
                    GML.getInstance()
                            .getSchema()
                            .resolveElementDeclaration(GML.Envelope.getLocalPart()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public QName getTarget() {
        return FES.BBOXType;
    }

    public Class getType() {
        return BBOX.class;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        BBOX box = (BBOX) object;

        if (FES.ValueReference.equals(name)) {
            return box.getExpression1();
        }

        return null;
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        BBOX box = (BBOX) object;

        List properties = new ArrayList();
        Envelope env = null;
        try {
            String srs = box.getSRS();
            if (srs != null) {
                CoordinateReferenceSystem crs = CRS.decode(srs);
                env =
                        new ReferencedEnvelope(
                                box.getMinX(), box.getMaxX(), box.getMinY(), box.getMaxY(), crs);
            }
        } catch (Throwable t) {
            // never mind
        }
        if (env == null) {
            env = new Envelope(box.getMinX(), box.getMaxX(), box.getMinY(), box.getMaxY());
        }

        properties.add(new Object[] {ENVELOPE_PARTICLE, env});
        return properties;
    }
}
