package org.geotools.wfs.v2_0;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.opengis.wfs20.FeatureCollectionType;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xml.*;
import org.opengis.feature.Attribute;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;

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
 *
 * @source $URL$
 */
public class MemberPropertyTypeBinding extends
        org.geotools.gml3.bindings.FeaturePropertyTypeBinding {

    SchemaIndex schemaIndex;
    
    public MemberPropertyTypeBinding(XSDIdRegistry idSet, SchemaIndex schemaIndex) {
        super(idSet);
        this.schemaIndex = schemaIndex;
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
        Object member = super.getProperty(object, org.geotools.gml3.GML._Feature);
        if (member != null) {
            //check for joined feature
            if (isJoinedFeature(member)) {
                list.add(new Object[]{WFS.Tuple, splitJoinedFeature(member)});
            }
            else {
                list.add(new Object[]{GML.AbstractFeature, object});
            }
        }
        else if (object instanceof FeatureCollectionType) {
            list.add(new Object[]{WFS.FeatureCollection, object});
        }
        else if (object instanceof Attribute) {
            //encoding a ValueCollection
            Attribute att = (Attribute) object;
            list.add(new Object[]{particle(att), att.getValue()});
        }

        return list;
    }

    XSDParticle particle(Attribute att) {
        XSDFactory factory = XSDFactory.eINSTANCE;

        AttributeType attType = att.getType();
        XSDTypeDefinition xsdType = schemaIndex.getTypeDefinition(
            new QName(attType.getName().getNamespaceURI(), attType.getName().getLocalPart()));

        XSDElementDeclaration element = factory.createXSDElementDeclaration();
        element.setName(att.getName().getLocalPart());
        element.setTargetNamespace(att.getName().getNamespaceURI());
        element.setTypeDefinition(xsdType);
        
        XSDParticle particle = factory.createXSDParticle();
        particle.setContent(element);
        return particle;
    }

    boolean isJoinedFeature(Object obj) {
        if (!(obj instanceof SimpleFeature)) {
            return false;
        }
        
        SimpleFeature feature = (SimpleFeature) obj;
        for (Object att : feature.getAttributes()) {
            if (att != null && att instanceof SimpleFeature) {
                return true;
            }
        }
            
        return false;
    }

    Feature[] splitJoinedFeature(Object obj) {
        SimpleFeature feature = (SimpleFeature) obj;
        List features = new ArrayList();
        features.add(feature);
        for (int i = 0; i < feature.getAttributeCount(); i++) {
            Object att = feature.getAttribute(i);
            if (att != null && att instanceof SimpleFeature) {
                features.add(att);
                
                //TODO: come up with a better approcach user, use user data or something to mark 
                // the attribute as encoded
                feature.setAttribute(i, null);
            }
        }
        
        return (Feature[])features.toArray(new Feature[features.size()]);
    }
}
