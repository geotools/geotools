package org.geotools.gml4wcs.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.gml4wcs.GML;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/gml:doubleList.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;simpleType name=&quot;doubleList&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;XML List based on XML Schema double type.  An element of this type contains a space-separated list of double values&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;list itemType=&quot;double&quot;/&gt;
 *  &lt;/simpleType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class DoubleListBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.doubleList;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        // return double[].class;
        return Double[].class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
            throws Exception {
        List list = (List) value;

        return list.toArray(new Double[list.size()]);

        // String[] values = ((String) value).split(" +");
        // double[] doubles = new double[values.length];
        //
        // for (int i = 0; i < values.length; i++) {
        // doubles[i] = Double.parseDouble(values[i]);
        // }
        //
        // return doubles;
    }

}