/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.kml.bindings;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.kml.FolderStack;
import org.geotools.kml.KML;
import org.geotools.kml.StyleMap;
import org.geotools.kml.v22.SchemaRegistry;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;


/**
 * Binding object for the type http://earth.google.com/kml/2.1:FeatureType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;complexType abstract="true" name="FeatureType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:ObjectType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" name="name" type="string"/&gt;
 *                  &lt;element default="1" minOccurs="0" name="visibility" type="boolean"/&gt;
 *                  &lt;element default="1" minOccurs="0" name="open" type="boolean"/&gt;
 *                  &lt;element minOccurs="0" name="address" type="string"/&gt;
 *                  &lt;element minOccurs="0" name="phoneNumber" type="string"/&gt;
 *                  &lt;element minOccurs="0" name="Snippet" type="kml:SnippetType"/&gt;
 *                  &lt;element minOccurs="0" name="description" type="string"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:LookAt"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:TimePrimitive"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:styleUrl"/&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:StyleSelector"/&gt;
 *                  &lt;element minOccurs="0" ref="kml:Region"/&gt;
 *                  &lt;element minOccurs="0" name="Metadata" type="kml:MetadataType"/&gt;
 *              &lt;/sequence&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class FeatureTypeBinding extends AbstractComplexBinding {
    /**
     * base feature type for kml features, used when no Schema element is specified
     */
    protected static final SimpleFeatureType FeatureType;

    StyleMap styleMap;
    private final FolderStack folderStack;
    private SchemaRegistry schemaRegistry;

    static {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setNamespaceURI(KML.NAMESPACE);
        tb.setName("feature");

        //&lt;element minOccurs="0" name="name" type="string"/&gt;
        tb.add("name", String.class);
        //&lt;element default="1" minOccurs="0" name="visibility" type="boolean"/&gt;
        tb.add("visibility", Boolean.class);
        //&lt;element default="1" minOccurs="0" name="open" type="boolean"/&gt;
        tb.add("open", Boolean.class);
        //&lt;element minOccurs="0" name="address" type="string"/&gt;
        tb.add("address", String.class);
        //&lt;element minOccurs="0" name="phoneNumber" type="string"/&gt;
        tb.add("phoneNumber", String.class);
        //&lt;element minOccurs="0" name="Snippet" type="kml:SnippetType"/&gt;
        //tb.add("Snippet",String.class):
        //&lt;element minOccurs="0" name="description" type="string"/&gt;
        tb.add("description", String.class);
        //&lt;element minOccurs="0" ref="kml:LookAt"/&gt;
        tb.add("LookAt", Point.class);
        //&lt;element minOccurs="0" ref="kml:TimePrimitive"/&gt;
        //tb.add("TimePrimitive", ...);
        //&lt;element minOccurs="0" ref="kml:styleUrl"/&gt;
        tb.add("Style", FeatureTypeStyle.class);
        //&lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:StyleSelector"/&gt;

        //&lt;element minOccurs="0" ref="kml:Region"/&gt;
        tb.add("Region", LinearRing.class);

        FeatureType = tb.buildFeatureType();
    }

    public FeatureTypeBinding(StyleMap styleMap, FolderStack folderStack,
            SchemaRegistry schemaRegistry) {
        this.styleMap = styleMap;
        this.folderStack = folderStack;
        this.schemaRegistry = schemaRegistry;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.FeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SimpleFeature.class;
    }

    private SimpleFeatureType appendAttributes(SimpleFeatureType acc, SimpleFeatureType typeToAppend) {
        if (typeToAppend == null) {
            return acc;
        }
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(acc);
        for (AttributeDescriptor ad : typeToAppend.getAttributeDescriptors()) {
            // only add attributes that we don't already have
            if (acc.getDescriptor(ad.getLocalName()) == null) {
                tb.add(ad);
            }
        }
        return tb.buildFeatureType();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {

        // start off with the default feature type, and retype as necessary
        SimpleFeatureType featureType = FeatureType;

        // retype based on schema if we have extended data pointing to a url
        @SuppressWarnings("unchecked")
        Map<String, Object> extData = (Map<String, Object>) node.getChildValue("ExtendedData");
        if (extData != null) {
            @SuppressWarnings("unchecked")
            List<URI> schemaURI = (List<URI>) extData.get("schemas");
            if (schemaURI != null) {
                for (URI uri : schemaURI) {
                    String normalizedSchemaName = normalizeSchemaName(uri);
                    SimpleFeatureType schemaType = schemaRegistry.get(normalizedSchemaName);
                    featureType = appendAttributes(featureType, schemaType);
                }
            }
        }

        // if we are a custom schema element, add the attributes to the type
        SimpleFeatureType customFeatureType = schemaRegistry.get(instance.getName());
        featureType = appendAttributes(featureType, customFeatureType);

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(featureType);

        //&lt;element minOccurs="0" name="name" type="string"/&gt;
        b.set("name", node.getChildValue("name"));

        //&lt;element default="1" minOccurs="0" name="visibility" type="boolean"/&gt;
        b.set("visibility", node.getChildValue("visibility", Boolean.TRUE));

        //&lt;element default="1" minOccurs="0" name="open" type="boolean"/&gt;
        b.set("open", node.getChildValue("open", Boolean.TRUE));

        //&lt;element minOccurs="0" name="address" type="string"/&gt;
        b.set("address", node.getChildValue("address"));

        //&lt;element minOccurs="0" name="phoneNumber" type="string"/&gt;
        b.set("phoneNumber", node.getChildValue("phoneNumber"));

        //&lt;element minOccurs="0" name="Snippet" type="kml:SnippetType"/&gt;
        //tb.add("Snippet",String.class):

        //&lt;element minOccurs="0" name="description" type="string"/&gt;
        b.set("description", node.getChildValue("description"));

        //&lt;element minOccurs="0" ref="kml:LookAt"/&gt;
        b.set("LookAt", node.getChildValue("LookAt"));

        //&lt;element minOccurs="0" ref="kml:TimePrimitive"/&gt;
        //tb.add("TimePrimitive", ...);

        //&lt;element minOccurs="0" ref="kml:styleUrl"/&gt;
        URI uri = (URI) node.getChildValue("styleUrl");

        if (uri != null) {
            //load the style from the style map
            //TODO: use a proxy to do forward referencing
            b.set("Style", styleMap.get(uri));
        }

        //&lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:StyleSelector"/&gt;

        //&lt;element minOccurs="0" ref="kml:Region"/&gt;
        b.set("Region", node.getChildValue("Region"));

        // stick extended data in feature user data
        if (extData != null) {
            b.featureUserData("UntypedExtendedData", extData.get("untyped"));
            @SuppressWarnings("unchecked")
            Map<String, Object> typedUserData = (Map<String, Object>) extData.get("typed");
            if (typedUserData != null) {
                for (Entry<String, Object> entry : typedUserData.entrySet()) {
                    String attrName = entry.getKey();
                    if (featureType.getDescriptor(attrName) != null) {
                        b.set(attrName, entry.getValue());
                    }
                }
            }
        }

        // if we are a custom schema type
        // add in any attributes from that type onto the feature
        if (customFeatureType != null) {
            for (AttributeDescriptor ad : customFeatureType.getAttributeDescriptors()) {
                String attrName = ad.getLocalName();
                Object childValue = node.getChildValue(attrName);
                if (childValue != null) {
                    b.set(attrName, childValue);
                }
            }
        }

        // stick folder stack in feature user data
        b.featureUserData("Folder", folderStack.asList());

        //&lt;element minOccurs="0" name="Metadata" type="kml:MetadataType"/&gt;
        return b.buildFeature((String) node.getAttributeValue("id"));
    }
    
    private String normalizeSchemaName(URI schemaURI) {
        if (schemaURI.getFragment() != null) {
            return schemaURI.getFragment();
        }
        return schemaURI.getPath();
    }

    public Object getProperty(Object object, QName name) throws Exception {
    	if( object instanceof FeatureCollection){
    		FeatureCollection features = (FeatureCollection) object;
    		if ( "id".equals( name.getLocalPart() ) ) {
                return features.getID(); 
            }    		
    	}
    	if( object instanceof SimpleFeature){
	        SimpleFeature feature = (SimpleFeature) object;
	        
	        if ( "id".equals( name.getLocalPart() ) ) {
	            return feature.getID(); 
	        }
	        
	        //&lt;element minOccurs="0" name="name" type="string"/&gt;
	        if ( "name".equals( name.getLocalPart() ) ) {
	            return feature.getAttribute( "name" );
	        }
	        
	        //&lt;element minOccurs="0" name="description" type="string"/&gt;
	        if ( "description".equals( name.getLocalPart() ) ) {
	            return feature.getAttribute( "description" );
	        }
	      
	        if ( KML.styleUrl.equals( name ) )  {
	            URI uri = (URI) feature.getAttribute( "Style" );
	            if ( uri != null ) {
	                return styleMap.get( uri );
	            }
	        }
	        
	        //&lt;element default="1" minOccurs="0" name="visibility" type="boolean"/&gt;
	        //&lt;element default="1" minOccurs="0" name="open" type="boolean"/&gt;
	        //&lt;element minOccurs="0" name="address" type="string"/&gt;
	        //&lt;element minOccurs="0" name="phoneNumber" type="string"/&gt;
	        //&lt;element minOccurs="0" name="Snippet" type="kml:SnippetType"/&gt;
	        //&lt;element minOccurs="0" name="description" type="string"/&gt;
	        //&lt;element minOccurs="0" ref="kml:LookAt"/&gt;
	        //&lt;element minOccurs="0" ref="kml:TimePrimitive"/&gt;
	        //&lt;element minOccurs="0" ref="kml:styleUrl"/&gt;
	        //&lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:StyleSelector"/&gt;
	        //&lt;element minOccurs="0" ref="kml:Region"/&gt;
	        //&lt;element minOccurs="0" name="Metadata" type="kml:MetadataType"/&gt;
    	}
        return super.getProperty(object, name);
    }
}
