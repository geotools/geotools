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
import org.geotools.po.ObjectFactory;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.geotools.org/po:SKU.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:simpleType name="SKU"&gt;
 *      &lt;xsd:restriction base="xsd:string"&gt;
 *          &lt;xsd:pattern value="\d{3}-[A-Z]{2}"/&gt;
 *      &lt;/xsd:restriction&gt;
 *  &lt;/xsd:simpleType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class SKUBinding extends AbstractSimpleBinding {

    ObjectFactory factory;

    public SKUBinding(ObjectFactory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return PO.SKU;
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
        return String.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        String sku = (String) value;

        if (!sku.matches("\\d{3}-[A-Z]{2}")) {
            throw new IllegalArgumentException("Illegal sku format: " + sku);
        }
        return sku;
    }
}
