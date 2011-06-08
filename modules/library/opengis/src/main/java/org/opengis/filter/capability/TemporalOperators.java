package org.opengis.filter.capability;

import java.util.Collection;

/**
 * Supported temporal operators in a filter capabilities document.
 * 
 * &lt;xsd:simpleType name="TemporalOperatorNameType">
 *    &lt;xsd:union>
 *        &lt;xsd:simpleType>
 *           &lt;xsd:restriction base="xsd:string">
 *              &lt;xsd:enumeration value="After"/>
 *              &lt;xsd:enumeration value="Before"/>
 *              &lt;xsd:enumeration value="Begins"/>
 *              &lt;xsd:enumeration value="BegunBy"/>
 *              &lt;xsd:enumeration value="TContains"/>
 *              &lt;xsd:enumeration value="During"/>
 *              &lt;xsd:enumeration value="TEquals"/>
 *              &lt;xsd:enumeration value="TOverlaps"/>
 *              &lt;xsd:enumeration value="Meets"/>
 *              &lt;xsd:enumeration value="OverlappedBy"/>
 *              &lt;xsd:enumeration value="MetBy"/>
 *              &lt;xsd:enumeration value="Ends"/>
 *              &lt;xsd:enumeration value="EndedBy"/>
 *            &lt;/xsd:restriction>
 *        &lt;/xsd:simpleType>
 *        &lt;xsd:simpleType>
 *           &lt;xsd:restriction base="xsd:string">
 *              &lt;xsd:pattern value="extension:\w{2,}"/>
 *           &lt;/xsd:restriction>
 *        &lt;/xsd:simpleType>
 *     &lt;/xsd:union>
 *  &lt;/xsd:simpleType>
 *
 * @author Justin Deoliveira, OpenGeo
 *
 */
public interface TemporalOperators {

    /**
     * Provided temporal operators.
     */
    Collection<TemporalOperator> getOperators();

    /**
     * Looks up an operator by name, returning null if no such operator found.
     *
     * @param name the name of the operator.
     *
     * @return The operator, or null.
     */
    TemporalOperator getOperator(String name);
}
