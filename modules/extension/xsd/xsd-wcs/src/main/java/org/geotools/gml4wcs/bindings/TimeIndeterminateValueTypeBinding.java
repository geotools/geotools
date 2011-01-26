package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;
import org.opengis.temporal.IndeterminateValue;

/**
 * Binding object for the type
 * http://www.opengis.net/gml:TimeIndeterminateValueType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;simpleType name=&quot;TimeIndeterminateValueType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation xml:lang=&quot;en&quot;&gt;
 *          This enumerated data type specifies values for indeterminate positions.
 *        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base=&quot;string&quot;&gt;
 *          &lt;enumeration value=&quot;after&quot;/&gt;
 *          &lt;enumeration value=&quot;before&quot;/&gt;
 *          &lt;enumeration value=&quot;now&quot;/&gt;
 *          &lt;enumeration value=&quot;unknown&quot;/&gt;
 *      &lt;/restriction&gt;
 *  &lt;/simpleType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class TimeIndeterminateValueTypeBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.TimeIndeterminateValueType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return IndeterminateValue.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
            throws Exception {
        IndeterminateValue timeValue = IndeterminateValue.valueOf((String)value);
        return timeValue;
    }

}