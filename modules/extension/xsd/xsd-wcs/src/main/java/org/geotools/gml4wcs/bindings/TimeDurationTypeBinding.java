package org.geotools.gml4wcs.bindings;

import javax.xml.namespace.QName;

import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;
import org.opengis.temporal.Duration;

/**
 * Binding object for the type http://www.opengis.net/gml:TimeDurationType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;simpleType name=&quot;TimeDurationType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation xml:lang=&quot;en&quot;&gt;
 *        Base type for describing temporal length or distance. The value space is further 
 *        constrained by subtypes that conform to the ISO 8601 or ISO 11404 standards.
 *        &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;union memberTypes=&quot;duration decimal&quot;/&gt;
 *  &lt;/simpleType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class TimeDurationTypeBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.TimeDurationType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
            throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, value);
    }

}