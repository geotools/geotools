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
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xs.bindings.XSQNameBinding;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
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
                QueryType q = (QueryType)eObject;
                
                //turn into list of qname
                List qNames = new ArrayList();
                for (Object s : q.getTypeNames()) {
                    try {
                        qNames.add(new XSQNameBinding(namespaceContext).parse(null, s));
                    } 
                    catch (Exception e) {
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
        if (name.getLocalPart().equals("aliases")) {
            List aliases = ((QueryType)object).getAliases();
            if (aliases.size() == 0) return null;
            
            StringBuffer ret = new StringBuffer();
            for (Object o : aliases) {
                String alias = (String)o;
                if (ret.length() > 0) ret.append(",");
                ret.append(alias);
            }
            
            return ret.toString();
        }
        
        if (name.getLocalPart().equals("typeNames")) {
            List qNames = ((QueryType)object).getTypeNames();
            if (qNames.size() == 0) return null;
            
            StringBuffer ret = new StringBuffer();
            
            for (Object o : qNames) {
                QName type = (QName)o;
                if (ret.length() > 0) ret.append(",");
                ret.append(type.getPrefix()+":"+type.getLocalPart());
            }
            
            return ret.toString();
        }
        
        return super.getProperty(object, name);
    }

}
