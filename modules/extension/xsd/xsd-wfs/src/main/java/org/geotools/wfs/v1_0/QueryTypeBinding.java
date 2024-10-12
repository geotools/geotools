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
 *
 */

package org.geotools.wfs.v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.WfsFactory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.xs.bindings.XSQNameBinding;

public class QueryTypeBinding extends org.geotools.wfs.bindings.QueryTypeBinding {

    FilterFactory filterFactory;
    NamespaceContext namespaceContext;

    public QueryTypeBinding(WfsFactory factory, FilterFactory filterFactory, NamespaceContext namespaceContext) {
        super(factory);
        this.filterFactory = filterFactory;
        this.namespaceContext = namespaceContext;
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("typeName".equals(name.getLocalPart())) {
            QueryType query = (QueryType) object;
            if (!query.getTypeName().isEmpty()) {
                // bit of a hack but handle both string and qname
                Object obj = query.getTypeName().get(0);
                if (obj instanceof String) {
                    obj = new XSQNameBinding(namespaceContext).parse(null, obj);
                }
                return obj;
            }
            return null;
        } else if ("PropertyName".equals(name.getLocalPart())) {
            List<PropertyName> l = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<String> property = (List<String>) super.getProperty(object, name);
            for (String s : property) {
                l.add(filterFactory.property(s));
            }
            return l;
        } else {
            return super.getProperty(object, name);
        }
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {

        if ("typeName".equals(property)) {
            // in wfs 1.0 we are only allowed a singel type name
            QueryType query = (QueryType) eObject;

            List<Object> list = new ArrayList<>();
            list.add(value);
            query.setTypeName(list);
        } else if ("PropertyName".equals(property)) {
            // in wfs 1.0 propertynames are ogc:PropertyName
            PropertyName name = (PropertyName) value;
            super.setProperty(eObject, property, name.getPropertyName(), lax);
        } else {
            super.setProperty(eObject, property, value, lax);
        }
    }
}
