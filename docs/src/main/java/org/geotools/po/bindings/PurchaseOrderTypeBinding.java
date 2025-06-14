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

import javax.xml.namespace.QName;
import org.geotools.po.Items;
import org.geotools.po.ObjectFactory;
import org.geotools.po.PurchaseOrderType;
import org.geotools.po.USAddress;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.geotools.org/po:PurchaseOrderType.
 *
 * <p>
 *
 * <pre>
 *     <code>
 *  &lt;xsd:complexType name="PurchaseOrderType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="shipTo" type="USAddress"/&gt;
 *          &lt;xsd:element name="billTo" type="USAddress"/&gt;
 *          &lt;xsd:element minOccurs="0" ref="comment"/&gt;
 *          &lt;xsd:element name="items" type="Items"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="orderDate" type="xsd:date"/&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *      </code>
 *     </pre>
 *
 * @generated
 */
public class PurchaseOrderTypeBinding extends AbstractComplexBinding {

    ObjectFactory factory;

    public PurchaseOrderTypeBinding(ObjectFactory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return PO.PurchaseOrderType;
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
        PurchaseOrderType purchaseOrder = factory.createPurchaseOrderType();

        purchaseOrder.setShipTo((USAddress) node.getChildValue("shipTo"));
        purchaseOrder.setBillTo((USAddress) node.getChildValue("billTo"));
        purchaseOrder.setComment((String) node.getChildValue("comment"));
        purchaseOrder.setItems((Items) node.getChildValue("items"));

        return purchaseOrder;
    }
}
