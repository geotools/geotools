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

import org.picocontainer.MutablePicoContainer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Function;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:BinaryLogicOpType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="BinaryLogicOpType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:LogicOpsType"&gt;
 *              &lt;xsd:choice maxOccurs="unbounded" minOccurs="2"&gt;
 *                  &lt;xsd:element ref="ogc:comparisonOps"/&gt;
 *                  &lt;xsd:element ref="ogc:spatialOps"/&gt;
 *                  &lt;xsd:element ref="ogc:logicOps"/&gt;
 *              &lt;/xsd:choice&gt;
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
 *
 *
 * @source $URL$
 */
public class OGCBinaryLogicOpTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCBinaryLogicOpTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.BinaryLogicOpType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return BinaryLogicOperator.class;
    }

    /**
     * <!-- begin-user-doc -->
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
        //implemented by element bindigns
        return null;

        //        //TODO: replace with element bindings
        //        Filter f1 = (Filter) node.getChildValue(0);
        //        Filter f2 = (Filter) node.getChildValue(1);
        //
        //        String name = instance.getName();
        //
        //        //		<xsd:element name="And" substitutionGroup="ogc:logicOps" type="ogc:BinaryLogicOpType"/>
        //        if ("And".equals(name)) {
        //            return factory.and(f1, f2);
        //        }
        //        //		<xsd:element name="Or" substitutionGroup="ogc:logicOps" type="ogc:BinaryLogicOpType"/>
        //        else if ("Or".equals(name)) {
        //            return factory.or(f1, f2);
        //        } else {
        //            throw new IllegalStateException(name);
        //        }
    }

    public Object getProperty(Object object, QName qName)
        throws Exception {
        BinaryLogicOperator operator = (BinaryLogicOperator) object;

        // this method is acutally used by later version of the filter spec, so it handles 
        // everything
        
        //use the local part to handle both OGC and FES namespaces
        String name = qName.getLocalPart();

        if ("comparisonOps".equals(name)) {
            List comparison = new ArrayList();

            for (Iterator f = operator.getChildren().iterator(); f.hasNext();) {
                Filter filter = (Filter) f.next();

                if (!(filter instanceof BinarySpatialOperator || filter instanceof BinaryTemporalOperator) && 
                     (filter instanceof BinaryComparisonOperator ||
                      filter instanceof PropertyIsLike || 
                      filter instanceof PropertyIsNull || 
                      filter instanceof PropertyIsNil || 
                      filter instanceof PropertyIsBetween) ) {
                    
                    comparison.add(filter);
                }
            }

            if (!comparison.isEmpty()) {
                return comparison;
            }
        }

        if ("spatialOps".equals(name)) {
            List spatial = new ArrayList();

            for (Iterator f = operator.getChildren().iterator(); f.hasNext();) {
                Filter filter = (Filter) f.next();

                if (filter instanceof BinarySpatialOperator) {
                    spatial.add(filter);
                }
            }

            if (!spatial.isEmpty()) {
                return spatial;
            }
        }
        
        if ("temporalOps".equals(name)) {
            List temporal = new ArrayList();

            for (Iterator f = operator.getChildren().iterator(); f.hasNext();) {
                Filter filter = (Filter) f.next();

                if (filter instanceof BinaryTemporalOperator) {
                    temporal.add(filter);
                }
            }

            if (!temporal.isEmpty()) {
                return temporal;
            }
        }

        if ("logicOps".equals(name)) {
            List logic = new ArrayList();

            for (Iterator f = operator.getChildren().iterator(); f.hasNext();) {
                Filter filter = (Filter) f.next();

                if (filter instanceof BinaryLogicOperator || filter instanceof Not) {
                    logic.add(filter);
                }
            }

            if (!logic.isEmpty()) {
                return logic;
            }
        }

        if ("_Id".equals(name)) {
            List ids = new ArrayList();
            for (Filter filter : operator.getChildren()) {
                if (filter instanceof Id) {
                    ids.add(filter);
                }
            }
            if (!ids.isEmpty()) {
                return ids;
            }
        }
        
        if ("Function".equals(name)) {
            List functions = new ArrayList();
            for (Filter filter : operator.getChildren()) {
                if (filter instanceof Function) {
                    functions.add(filter);
                }
            }
            if (!functions.isEmpty()) {
                return functions;
            }
        }
        
        //TODO:
        //<xsd:element ref="fes:extensionOps"/>
        
        return null;
    }
}
