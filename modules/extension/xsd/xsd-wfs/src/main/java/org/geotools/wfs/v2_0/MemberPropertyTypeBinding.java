package org.geotools.wfs.v2_0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xml.*;

import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:MemberPropertyType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:complexType mixed="true" name="MemberPropertyType"&gt;
 *      &lt;xsd:choice minOccurs="0"&gt;
 *          &lt;xsd:any namespace="##other" processContents="lax"/&gt;
 *          &lt;xsd:element ref="wfs:Tuple"/&gt;
 *          &lt;xsd:element ref="wfs:SimpleFeatureCollection"/&gt;
 *      &lt;/xsd:choice&gt;
 *      &lt;xsd:attribute name="state" type="wfs:StateValueType"/&gt;
 *      &lt;xsd:attributeGroup ref="xlink:simpleLink"/&gt;
 *  &lt;/xsd:complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wfs/src/main/java/org/geotools/wfs/v2_0/MemberPropertyTypeBinding.java $
 */
public class MemberPropertyTypeBinding extends
        org.geotools.gml3.bindings.FeaturePropertyTypeBinding {

    public MemberPropertyTypeBinding(XSDIdRegistry idSet) {
        super(idSet);
    }
    
    /**
     * @generated
     */
    public QName getTarget() {
        return WFS.MemberPropertyType;
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
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        ArrayList list = new ArrayList();
        list.add(
            new Object[]{GML.AbstractFeature, super.getProperty(object, org.geotools.gml3.GML._Feature)});
        return list;
    }

}
