package org.geotools.csw.bindings;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Factory;
import net.opengis.cat.csw20.SimpleLiteral;

import org.eclipse.emf.ecore.EFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.SimpleContentComplexEMFBinding;

public class SimpleLiteralBinding extends SimpleContentComplexEMFBinding {
    
    

    public SimpleLiteralBinding(QName target) {
        super(Csw20Factory.eINSTANCE, target);
    }
    
    @Override
    public Class getType() {
        return SimpleLiteral.class;
    }
    
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        SimpleLiteral sl = Csw20Factory.eINSTANCE.createSimpleLiteral();
        sl.setName(instance.getName());
        sl.setValue(value);
        Node scheme = node.getAttribute("scheme");
        if(scheme != null) {
            sl.setScheme((String) scheme.getValue());
        }
        
        return sl;
    }

}
