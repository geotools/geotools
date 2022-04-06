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

import java.math.BigDecimal;
import javax.xml.namespace.QName;
import org.geotools.po.ObjectFactory;
import org.geotools.po.USAddress;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.geotools.org/po:USAddress.
 *
 * <p>
 *
 * <pre>
 *     <code>
 *  &lt;xsd:complexType name="USAddress"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="name" type="xsd:string"/&gt;
 *          &lt;xsd:element name="street" type="xsd:string"/&gt;
 *          &lt;xsd:element name="city" type="xsd:string"/&gt;
 *          &lt;xsd:element name="state" type="xsd:string"/&gt;
 *          &lt;xsd:element name="zip" type="xsd:decimal"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute fixed="US" name="country" type="xsd:NMTOKEN"/&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *      </code>
 *     </pre>
 *
 * @generated
 */
public class USAddressBinding extends AbstractComplexBinding {

    ObjectFactory factory;

    public USAddressBinding(ObjectFactory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return PO.USAddress;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return USAddress.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        USAddress address = factory.createUSAddress();

        // elements
        address.setName((String) node.getChildValue("name"));
        address.setStreet((String) node.getChildValue("street"));
        address.setCity((String) node.getChildValue("city"));
        address.setState((String) node.getChildValue("state"));
        address.setZip((BigDecimal) node.getChildValue("zip"));

        // attribute
        address.setCountry((String) node.getAttributeValue("country"));

        return address;
    }
}
