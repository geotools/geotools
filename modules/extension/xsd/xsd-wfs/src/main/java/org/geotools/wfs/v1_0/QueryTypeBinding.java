package org.geotools.wfs.v1_0;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.geotools.xs.bindings.XSQNameBinding;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.PropertyName;

import net.opengis.wfs.QueryType;
import net.opengis.wfs.WfsFactory;

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
                //bit of a hack but handle both string and qname
                Object obj = query.getTypeName().get(0); 
                if (obj instanceof String) {
                    obj = new XSQNameBinding(namespaceContext).parse(null, obj);
                }
                return obj;
            }
            return null;
        }
        else if ("PropertyName".equals(name.getLocalPart())) {
            List l = new ArrayList();
            for (String s : (List<String>)super.getProperty(object, name)) {
                l.add(filterFactory.property(s));
            }
            return l;
        }
        else {
            return super.getProperty(object, name);
        }
        
    }
    
    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {

        if ("typeName".equals(property)) {
            //in wfs 1.0 we are only allowed a singel type name
            QueryType query = (QueryType)eObject;
            
            ArrayList list = new ArrayList();
            list.add(value);
            query.setTypeName(list);
        }
        else if ("PropertyName".equals(property)) {
            //in wfs 1.0 propertynames are ogc:PropertyName 
            PropertyName name = (PropertyName) value;
            super.setProperty(eObject, property, name.getPropertyName(), lax);
        }
        else {
            super.setProperty(eObject, property, value, lax);
        }
    }

}
