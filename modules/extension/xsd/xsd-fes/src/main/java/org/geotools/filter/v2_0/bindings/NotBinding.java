/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGCNotBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.*;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.spatial.BinarySpatialOperator;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:Not.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Not" substitutionGroup="fes:logicOps" type="fes:UnaryLogicOpType"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class NotBinding extends OGCNotBinding {

    public NotBinding(FilterFactory filterfactory) {
        super(filterfactory);
    }

    public QName getTarget() {
        return FES.Not;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        Not not = (Not) object;

        if (FES.spatialOps.equals(name) && not.getFilter() instanceof BinarySpatialOperator) {
            return not.getFilter();
        }

        if (FES.logicOps.equals(name) && not.getFilter() instanceof BinaryLogicOperator) {
            return not.getFilter();
        }

        if (FES.comparisonOps.equals(name) && not.getFilter() instanceof BinaryComparisonOperator) {
            return not.getFilter();
        }

        if (FES.comparisonOps.equals(name) && not.getFilter() instanceof PropertyIsNull) {
            return not.getFilter();
        }
        return null;
    }
}
