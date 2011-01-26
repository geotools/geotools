package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wcs10.DomainSubsetType;
import net.opengis.wcs10.SpatialSubsetType;
import net.opengis.wcs10.Wcs10Factory;

import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:DomainSubsetType.
 * 
 * <p>
 * 
 * <pre>
 *	 <code>
 *  &lt;complexType name=&quot;DomainSubsetType&quot;&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Defines the desired subset of the domain set of the coverage. Is a GML property containing either or both spatialSubset and temporalSubset GML objects. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;choice&gt;
 *          &lt;sequence&gt;
 *              &lt;element ref=&quot;wcs:spatialSubset&quot;/&gt;
 *              &lt;element minOccurs=&quot;0&quot; ref=&quot;wcs:temporalSubset&quot;/&gt;
 *          &lt;/sequence&gt;
 *          &lt;element ref=&quot;wcs:temporalSubset&quot;/&gt;
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
public class DomainSubsetTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return WCS.DomainSubsetType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Class getType() {
        return DomainSubsetType.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        DomainSubsetType domainSubset = Wcs10Factory.eINSTANCE.createDomainSubsetType();
        
        SpatialSubsetType spatialSubset = (SpatialSubsetType) node.getChildValue("spatialSubset");
        if (spatialSubset != null)
            domainSubset.setSpatialSubset(spatialSubset);
        return domainSubset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.xml.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

}