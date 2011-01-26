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

import java.util.ArrayList;
import java.util.List;

import org.picocontainer.MutablePicoContainer;
import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:BinarySpatialOpType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="BinarySpatialOpType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="ogc:PropertyName"/&gt;
 *                  &lt;xsd:choice&gt;
 *                      &lt;xsd:element ref="gml:_Geometry"/&gt;
 *                      &lt;xsd:element ref="gml:Box"/&gt;
 *                  &lt;/xsd:choice&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
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
public class OGCBinarySpatialOpTypeBinding extends AbstractComplexBinding {
    private FilterFactory2 factory;
    private GeometryFactory gFactory;

    public OGCBinarySpatialOpTypeBinding(FilterFactory2 factory, GeometryFactory gFactory) {
        this.factory = factory;
        this.gFactory = gFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.BinarySpatialOpType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BinarySpatialOperator.class;
    }

    /**
     * <!-- begin-user-doc -->
     * We check out the instance for the <code>op</code> so we can fail early.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //implemented by element bindings
        return null;

        //        //TODO: replace with element bindings
        //        PropertyName e1 = (PropertyName) node.getChildValue(PropertyName.class);
        //        Expression e2 = null;
        //
        //        if (node.hasChild(Geometry.class)) {
        //            e2 = factory.literal(node.getChildValue(Geometry.class));
        //        } else {
        //            //turn bounding box into geometry
        //            //TODO: not sure if this should be done here but I am pretty sure filter 
        //            // implementation expect this to be a geometry
        //            Envelope bbox = (Envelope) node.getChildValue(Envelope.class);
        //            e2 = factory.literal(gFactory.createPolygon(gFactory.createLinearRing(
        //                            new Coordinate[] {
        //                                new Coordinate(bbox.getMinX(), bbox.getMinY()),
        //                                new Coordinate(bbox.getMinX(), bbox.getMaxY()),
        //                                new Coordinate(bbox.getMaxX(), bbox.getMaxY()),
        //                                new Coordinate(bbox.getMaxX(), bbox.getMinY()),
        //                                new Coordinate(bbox.getMinX(), bbox.getMinY())
        //                            }), null));
        //        }
        //
        //        String name = instance.getName();
        //
        //        //<xsd:element name="Equals" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        if ("Equals".equals(name)) {
        //            return factory.equal(e1, e2);
        //        }
        //        //<xsd:element name="Disjoint" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        else if ("Disjoint".equals(name)) {
        //            return factory.disjoint(e1, e2);
        //        }
        //        //<xsd:element name="Touches" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        else if ("Touches".equals(name)) {
        //            return factory.touches(e1, e2);
        //        }
        //        //<xsd:element name="Within" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        else if ("Within".equals(name)) {
        //            //TODO: within method on FilterFactory2 needs to take two expressoins
        //            return factory.within(e1, e2);
        //        }
        //        //<xsd:element name="Overlaps" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        else if ("Overlaps".equals(name)) {
        //            return factory.overlaps(e1, e2);
        //        }
        //        //<xsd:element name="Crosses" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        else if ("Crosses".equals(name)) {
        //            return factory.crosses(e1, e2);
        //        }
        //        //<xsd:element name="Intersects" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        else if ("Intersects".equals(name)) {
        //            return factory.intersects(e1, e2);
        //        }
        //        //<xsd:element name="Contains" substitutionGroup="ogc:spatialOps" type="ogc:BinarySpatialOpType"/>
        //        else if ("Contains".equals(name)) {
        //            return factory.contains(e1, e2);
        //        } else {
        //            throw new IllegalStateException("Unknown - " + name);
        //        }
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        BinarySpatialOperator operator = (BinarySpatialOperator) object;

        return OGCUtils.property(operator.getExpression1(), operator.getExpression2(), name);
    }
    
    public List getProperties(Object object) throws Exception {
        //special hack for Functions, while not mandated by the spec we handle it 
        // here
        BinarySpatialOperator operator = (BinarySpatialOperator) object;
        if ( operator.getExpression2() instanceof Function ) {
            ArrayList props = new ArrayList();
            props.add( new Object[]{ OGC.Function, operator.getExpression2() } ); 
            return props;
        }
        
        return super.getProperties(object);
    }
    
}
