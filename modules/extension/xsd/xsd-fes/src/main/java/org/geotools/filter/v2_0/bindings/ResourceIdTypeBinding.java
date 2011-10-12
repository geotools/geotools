package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

public class ResourceIdTypeBinding extends AbstractComplexBinding {

    FilterFactory factory;
    
    public ResourceIdTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }
    
    public Class getType() {
        return FeatureId.class;
    }
    
    public QName getTarget() {
        return FES.ResourceIdType;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return factory.featureId((String)node.getAttributeValue("rid"));
    }
    
    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if ("rid".equals(name.getLocalPart())) {
            FeatureId fid = (FeatureId) object;
            return fid.getID();
        }
        return null;
    }
}
