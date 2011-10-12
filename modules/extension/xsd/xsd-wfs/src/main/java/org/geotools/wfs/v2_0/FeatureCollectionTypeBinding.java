package org.geotools.wfs.v2_0;

import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.bindings.WFSParsingUtils;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:FeatureCollectionType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:complexType name="FeatureCollectionType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="wfs:SimpleFeatureCollectionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" name="AdditionalObjects" type="wfs:SimpleValueCollectionType"/&gt;
 *                  &lt;xsd:element minOccurs="0" ref="wfs:TruncatedResponse"/&gt;
 *              &lt;/xsd:sequence&gt;
 *              &lt;xsd:attributeGroup ref="wfs:StandardResponseParameters"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 *
 * @source $URL$
 */
public class FeatureCollectionTypeBinding extends AbstractComplexEMFBinding {

    public FeatureCollectionTypeBinding(Wfs20Factory factory) {
        super(factory);
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.FeatureCollectionType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return WFSParsingUtils.FeatureCollectionType_parse(
            (EObject) super.parse(instance, node, value), instance, node);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (!WFSParsingUtils.features((EObject) object).isEmpty()) {
            Object val = WFSParsingUtils.FeatureCollectionType_getProperty((EObject) object, name);
            if (val != null) {
                return val;
            }
        }
        
        return super.getProperty(object, name);
    }
    
    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if ("member".equalsIgnoreCase(property)) {
            //ignore feature, handled in parse()
        }
        else {
            super.setProperty(eObject, property, value, lax);
        }
    }
}
