package org.geotools.po.bindings;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.geotools.po.Items;
import org.geotools.po.ObjectFactory;
import org.geotools.po.PurchaseOrderType;
import org.geotools.po.USAddress;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class PurchaseOrderBinding extends AbstractComplexBinding {
    ObjectFactory factory;              
    public PurchaseOrderBinding( ObjectFactory factory ) {
            super();
            this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
            return PO.purchaseOrder;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *      
     * @generated modifiable
     */     
    public Class getType() {
            return PurchaseOrderType.class;
    }
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *      
     * @generated modifiable
     */     
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        PurchaseOrderType po = new PurchaseOrderType();
            
        po.setBillTo((USAddress) node.getChildValue("billTo"));
        po.setShipTo((USAddress) node.getChildValue("shipTo"));
        po.setItems((Items) node.getChildValue("items"));
        po.setComment((String) node.getChildValue("comment"));
        po.setOrderDate((XMLGregorianCalendar) node.getAttributeValue("orderDate"));
            
        return po;
    }
}
