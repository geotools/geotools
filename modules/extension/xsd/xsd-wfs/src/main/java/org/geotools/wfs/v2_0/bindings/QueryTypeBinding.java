/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.util.Converters;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xs.bindings.XSQNameBinding;
import org.geotools.xsd.ComplexEMFBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QueryTypeBinding extends ComplexEMFBinding {

    NamespaceContext namespaceContext;

    public QueryTypeBinding(NamespaceContext namespaceContext) {
        super(Wfs20Factory.eINSTANCE, WFS.QueryType);
        this.namespaceContext = namespaceContext;
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        super.setProperty(eObject, property, value, lax);
        if (!lax) {
            if ("typeNames".equalsIgnoreCase(property)) {
                QueryType q = (QueryType) eObject;

                // turn into list of qname
                List<QName> qNames = new ArrayList<>();
                for (Object s : q.getTypeNames()) {
                    try {
                        QName parsed = (QName) new XSQNameBinding(namespaceContext).parse(null, s);
                        qNames.add(parsed);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                q.getTypeNames().clear();
                q.getTypeNames().addAll(qNames);
            }
        }
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("aliases".equalsIgnoreCase(name.getLocalPart())) {
            List aliases = ((QueryType) object).getAliases();
            if (aliases.isEmpty()) return null;

            StringBuffer ret = new StringBuffer();
            for (Object o : aliases) {
                String alias = (String) o;
                if (ret.length() > 0) ret.append(",");
                ret.append(alias);
            }

            return ret.toString();
        } else if ("typeNames".equalsIgnoreCase(name.getLocalPart())) {
            StringBuilder s = new StringBuilder();
            for (Object typeName : ((QueryType) object).getTypeNames()) {
                if (typeName instanceof Collection) {
                    typeName = ((Collection) typeName).iterator().next();
                }
                s.append(Converters.convert(typeName, String.class));
                s.append(",");
            }
            s.setLength(s.length() - 1);
            return s.toString();
        } else if ("AbstractProjectionClause".equalsIgnoreCase(name.getLocalPart())) {
            return null;
        }

        return super.getProperty(object, name);
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        Element e = super.encode(object, document, value);

        QueryType resultType = (QueryType) object;

        Iterator it = resultType.getAbstractProjectionClause().iterator();
        while (it.hasNext()) {
            Element node = document.createElementNS(WFS.NAMESPACE, "PropertyName");
            node.setTextContent(Converters.convert(it.next(), String.class));
            e.appendChild(node);
        }

        return e;
    }
}
