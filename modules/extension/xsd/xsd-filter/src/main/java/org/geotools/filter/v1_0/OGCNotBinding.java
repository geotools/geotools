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

import java.util.Collections;

import javax.xml.namespace.QName;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Not;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the element http://www.opengis.net/ogc:Not.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:element name="Not" substitutionGroup="ogc:logicOps" type="ogc:UnaryLogicOpType"/&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class OGCNotBinding extends AbstractComplexBinding {
    FilterFactory filterfactory;

    public OGCNotBinding(FilterFactory filterfactory) {
        this.filterfactory = filterfactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.Not;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Not.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Filter filter = (Filter) node.getChildValue(Filter.class);
        if ( filter == null ) {
            //look for an Identifier, not in the spec but something we handle
            Identifier id = (Identifier) node.getChildValue( Identifier.class );
            if ( id != null ) {
                filter = filterfactory.id( Collections.singleton( id ) );
            }
        }

        return filterfactory.not(filter);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        Not not = (Not) object;

        if (OGC.spatialOps.equals(name) && not.getFilter() instanceof BinarySpatialOperator) {
            return not.getFilter();
        }

        if (OGC.logicOps.equals(name) && not.getFilter() instanceof BinaryLogicOperator) {
            return not.getFilter();
        }

        if (OGC.comparisonOps.equals(name) && not.getFilter() instanceof BinaryComparisonOperator) {
            return not.getFilter();
        }

        return null;
    }
}
