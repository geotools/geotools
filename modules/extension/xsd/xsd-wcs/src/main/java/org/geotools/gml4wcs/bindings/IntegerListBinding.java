package org.geotools.gml4wcs.bindings;

import java.math.BigInteger;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/gml:integerList.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;simpleType name=&quot;integerList&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML List based on XML Schema integer type.  An element of this type contains a space-separated list of integer values&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;list itemType=&quot;integer&quot;/&gt;
 *  &lt;/simpleType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class IntegerListBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.integerList;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return int[].class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
            throws Exception {
        List values = (List) value;

        int[] integers = new int[values.size()];

        for (int i = 0; i < values.size(); i++) {
            integers[i] = ((BigInteger)values.get(i)).intValue();
        }

        return integers;
    }

}