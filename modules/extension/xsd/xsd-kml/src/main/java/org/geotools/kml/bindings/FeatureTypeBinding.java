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
import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.kml.KML;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


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
 * @source $URL$
 */
public class FeatureTypeBinding extends AbstractComplexBinding {
    /**
     * base feature type for kml features
     */
    protected static final SimpleFeatureType featureType;

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
        tb.add("LookAt", Coordinate.class);
        //&lt;element minOccurs="0" ref="kml:TimePrimitive"/&gt;
        //tb.add("TimePrimitive", ...);
        //&lt;element minOccurs="0" ref="kml:styleUrl"/&gt;
        tb.add("Style", FeatureTypeStyle.class);
        //&lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:StyleSelector"/&gt;

        //&lt;element minOccurs="0" ref="kml:Region"/&gt;
        tb.add("Region", Envelope.class);

        featureType = tb.buildFeatureType();
    }

    StyleMap styleMap;

    public FeatureTypeBinding(StyleMap styleMap) {
        this.styleMap = styleMap;
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

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
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

        //&lt;element minOccurs="0" name="Metadata" type="kml:MetadataType"/&gt;
        return b.buildFeature((String) node.getAttributeValue("id"));
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
