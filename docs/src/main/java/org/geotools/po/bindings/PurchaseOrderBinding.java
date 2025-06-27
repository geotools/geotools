/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.po.bindings;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.geotools.po.Items;
import org.geotools.po.ObjectFactory;
import org.geotools.po.PurchaseOrderType;
import org.geotools.po.USAddress;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class PurchaseOrderBinding extends AbstractComplexBinding {
    ObjectFactory factory;

    public PurchaseOrderBinding(ObjectFactory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return PO.purchaseOrder;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return PurchaseOrderType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
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
