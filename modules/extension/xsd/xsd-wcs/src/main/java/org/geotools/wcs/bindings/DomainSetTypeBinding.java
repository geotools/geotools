package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wcs10.DomainSetType;

import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:DomainSetType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;DomainSetType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Defines the spatial-temporal domain set of a coverage offering. The domainSet shall include a SpatialDomain (describing the spatial locations for which coverages can be requested), a TemporalDomain (describing the time instants or inter-vals for which coverages can be requested), or both. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;choice&gt;
 *          &lt;sequence&gt;
 *              &lt;element ref=&quot;wcs:spatialDomain&quot;/&gt;
 *              &lt;element minOccurs=&quot;0&quot; ref=&quot;wcs:temporalDomain&quot;/&gt;
 *          &lt;/sequence&gt;
 *          &lt;element ref=&quot;wcs:temporalDomain&quot;/&gt;
 *      &lt;/choice&gt;
 *  &lt;/complexType&gt; 
 * 	
 * </code>
 *	 </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class DomainSetTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return WCS.DomainSetType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return DomainSetType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {

        return super.parse(instance, node, value);
    }

}