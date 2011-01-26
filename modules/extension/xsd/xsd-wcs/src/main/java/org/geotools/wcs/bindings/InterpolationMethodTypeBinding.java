package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wcs10.InterpolationMethodType;

import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

/**
 * Binding object for the type
 * http://www.opengis.net/wcs:InterpolationMethodType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;simpleType name=&quot;InterpolationMethodType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Codes that identify interpolation methods. The meanings of these codes are defined in Annex B of ISO 19123: Geographic information — Schema for coverage geometry and functions. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;restriction base=&quot;string&quot;&gt;
 *          &lt;enumeration value=&quot;nearest neighbor&quot;/&gt;
 *          &lt;enumeration value=&quot;bilinear&quot;/&gt;
 *          &lt;enumeration value=&quot;bicubic&quot;/&gt;
 *          &lt;enumeration value=&quot;lost area&quot;/&gt;
 *          &lt;enumeration value=&quot;barycentric&quot;/&gt;
 *          &lt;enumeration value=&quot;none&quot;&gt;
 *              &lt;annotation&gt;
 *                  &lt;documentation&gt;No interpolation. &lt;/documentation&gt;
 *              &lt;/annotation&gt;
 *          &lt;/enumeration&gt;
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
public class InterpolationMethodTypeBinding extends AbstractSimpleBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return WCS.InterpolationMethodType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return InterpolationMethodType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value)
            throws Exception {

        return InterpolationMethodType.get((String) value);
    }

}