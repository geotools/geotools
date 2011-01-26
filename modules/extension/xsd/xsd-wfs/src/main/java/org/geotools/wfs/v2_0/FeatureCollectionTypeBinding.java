package org.geotools.wfs.v2_0;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;

import org.geotools.gml3.GML;
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
 */
public class FeatureCollectionTypeBinding extends
        org.geotools.wfs.bindings.FeatureCollectionTypeBinding {

    public FeatureCollectionTypeBinding(WfsFactory factory) {
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

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        if (WFS.member.equals(name)) {
            return super.getProperty(object, GML.featureMember);
        }
        return super.getProperty(object, name);
    }
}