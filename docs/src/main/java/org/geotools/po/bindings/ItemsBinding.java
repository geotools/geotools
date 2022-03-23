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
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.geotools.org/po:Items.
 *
 * <p>
 *
 * <pre>
 *     <code>
 *  &lt;xsd:complexType name="Items"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" minOccurs="0" name="item"&gt;
 *              &lt;xsd:complexType name="Items_item"&gt;
 *                  &lt;xsd:sequence&gt;
 *                      &lt;xsd:element name="productName" type="xsd:string"/&gt;
 *                      &lt;xsd:element name="quantity"&gt;
 *                          &lt;xsd:simpleType&gt;
 *                              &lt;xsd:restriction base="xsd:positiveInteger"&gt;
 *                                  &lt;xsd:maxExclusive value="100"/&gt;
 *                              &lt;/xsd:restriction&gt;
 *                          &lt;/xsd:simpleType&gt;
 *                      &lt;/xsd:element&gt;
 *                      &lt;xsd:element name="USPrice" type="xsd:decimal"/&gt;
 *                      &lt;xsd:element minOccurs="0" ref="comment"/&gt;
 *                      &lt;xsd:element minOccurs="0" name="shipDate" type="xsd:date"/&gt;
 *                  &lt;/xsd:sequence&gt;
 *                  &lt;xsd:attribute name="partNum" type="SKU" use="required"/&gt;
 *              &lt;/xsd:complexType&gt;
 *          &lt;/xsd:element&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *      </code>
 *     </pre>
 *
 * @generated
 */
public class ItemsBinding extends AbstractComplexBinding {

    ObjectFactory factory;

    public ItemsBinding(ObjectFactory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return PO.Items;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Items.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @SuppressWarnings("unchecked")
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Items items = factory.createItems();
        items.getItem().addAll(node.getChildValues("item"));
        return items;
    }
}
