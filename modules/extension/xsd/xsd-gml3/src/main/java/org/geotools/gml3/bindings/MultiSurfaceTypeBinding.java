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
package org.geotools.gml3.bindings;

import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.MultiSurface;
import org.geotools.gml3.ArcParameters;
import org.geotools.gml3.GML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * Binding object for the type http://www.opengis.net/gml:MultiSurfaceType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="MultiSurfaceType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A MultiSurface is defined by one or more Surfaces, referenced through surfaceMember elements.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="gml:AbstractGeometricAggregateType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;The members of the geometric aggregate can be specified either using the "standard" property or the array property style. It is also valid to use both the "standard" and the array property style in the same collection.
 *  NOTE: Array properties cannot reference remote geometry elements.&lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:surfaceMember"/&gt;
 *                  &lt;element minOccurs="0" ref="gml:surfaceMembers"/&gt;
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
public class MultiSurfaceTypeBinding extends AbstractComplexBinding {

    protected GeometryFactory gf;
    protected CoordinateSequenceFactory cs;
    protected ArcParameters arcParameters;

    public MultiSurfaceTypeBinding(GeometryFactory gFactory) {
        this.gf = gFactory;
    }

    public MultiSurfaceTypeBinding(
            GeometryFactory gFactory, CoordinateSequenceFactory csFactory, ArcParameters arcParameters) {
        this(gFactory);
        this.cs = csFactory;
        this.arcParameters = arcParameters;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return GML.MultiSurfaceType;
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
        return MultiSurface.class;
    }

    @Override
    public int getExecutionMode() {
        return BEFORE;
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

        // &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:surfaceMember"/&gt;
        List<Polygon> surfaces = node.getChildValues(Polygon.class);

        // &lt;element minOccurs="0" ref="gml:surfaceMembers"/&gt;
        if (node.hasChild(Polygon[].class)) {
            surfaces.addAll(Arrays.asList(node.getChildValue(Polygon[].class)));
        }

        CurvedGeometryFactory factory = GML3ParsingUtils.getCurvedGeometryFactory(arcParameters, gf, null);

        return factory.createMultiSurface(surfaces);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("surfaceMember".equals(name.getLocalPart())) {
            if (object instanceof MultiSurface) {
                MultiPolygon multiSurface = (MultiPolygon) object;
                Polygon[] members = new Polygon[multiSurface.getNumGeometries()];

                for (int i = 0; i < members.length; i++) {
                    members[i] = (Polygon) multiSurface.getGeometryN(i);
                }

                GML3EncodingUtils.setChildIDs(multiSurface);

                return members;
            } else {
                return object;
            }
        }

        return null;
    }
}
