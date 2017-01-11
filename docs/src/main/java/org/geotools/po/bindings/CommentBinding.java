package org.geotools.po.bindings;

import javax.xml.namespace.QName;

import org.geotools.po.ObjectFactory;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

public class CommentBinding extends AbstractSimpleBinding {
    ObjectFactory factory;              
    public CommentBinding( ObjectFactory factory ) {
            super();
            this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
            return PO.comment;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *      
     * @generated modifiable
     */     
    public Class getType() {
            return String.class;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *      
     * @generated modifiable
     */     
    public Object parse(InstanceComponent instance, Object value) 
            throws Exception {
            String comment = (String) value;
            return comment;
    }
}
