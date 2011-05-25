/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.attributes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.vividsolutions.jts.geom.Geometry;
/**
 * GazetteerNameValidationTest purpose.
 * <p>
 * Description of GazetteerNameValidationTest ...
 * </p>
 *  
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class GazetteerNameValidationOnlineTest extends TestCase {

	public GazetteerNameValidationOnlineTest(){super("");}
	public GazetteerNameValidationOnlineTest(String s){super(s);}
	
	public void XtestValidate() {
		class TestFeature implements SimpleFeature {
			Map attrs = new HashMap();
			public SimpleFeatureCollection getParent(){return null;}
			public void setParent(SimpleFeatureCollection collection){}
			public SimpleFeatureType getFeatureType(){return null;}
			public String getID(){return "";}
			public FeatureId getIdentifier(){return null;}
			public Object[] getAttributes(Object[] attributes){return attrs.entrySet().toArray();}
			//	used
			public Object getAttribute(String xPath){return attrs.get(xPath);}
			public Object getAttribute(int index){return attrs.entrySet().toArray()[index];}
			public void setAttribute(int position, Object val) throws IllegalAttributeException, ArrayIndexOutOfBoundsException{}
			public int getNumberOfAttributes(){return attrs.size();}
			public void setAttributes(Object[] attributes) throws IllegalAttributeException{}
			//	used
			public void setAttribute(String xPath, Object attribute)throws IllegalAttributeException{attrs.put(xPath,attribute);}
			
			public void setDefaultGeometry(Geometry geometry) throws IllegalAttributeException{}
			public ReferencedEnvelope getBounds(){return null;}
			public Object getDefaultGeometryValue() {
				
				return null;
			}
			public SimpleFeatureType getType() {
				
				return null;
			}
			public List getTypes() {
				
				return null;
			}
			public Object getValue(String name) {
				
				return null;
			}
			public Object getValue(int index) {
				
				return null;
			}
			public List getValues() {
				
				return null;
			}
			public Object operation(String name, Object parameters) {
				
				return null;
			}
			public void setDefaultGeometryValue(Object geometry) {
				
				
			}
			public void setValue(String name, Object value) {
				
				
			}
			public void setValue(int index, Object value) {
				
				
			}
			public void setValues(List values) {
				
				
			}
			public void setValues(Object[] values) {
				
				
			}
			public CoordinateReferenceSystem getCRS() {
				
				return null;
			}
			public Geometry getDefaultGeometry() {
			    return null;
			}
			public Object getUserData(Object key) {
				
				return null;
			}
			public void putUserData(Object key, Object value) {
				
				
			}
			public void setCRS(CoordinateReferenceSystem crs) {
				
				
			}
			public void setDefaultGeometry(GeometryAttribute geometryAttribute) {
				
				
			}
			public Collection associations() {
				
				return null;
			}
			public Collection attributes() {
				
				return null;
			}
			public Object get() {
				
				return null;
			}
			public List get(Name name) {
				
				return null;
			}
			public AttributeDescriptor getDescriptor() {
				
				return null;
			}
			public void set(Object newValue) throws IllegalArgumentException {
				
				
			}
			public boolean nillable() {
				
				return false;
			}
			public Object operation(Name name, List parameters) {
				
				return null;
			}
			public PropertyDescriptor descriptor() {
				
				return null;
			}
			public Name name() {
				
				return null;
			}
            public List getAttributes() {
                
                return null;
            }
            public Collection<? extends Property> getValue() {
                return null;
            }
            public void setValue( List values ) {
                
            }
            public void setValue( Object values ) {
                
            }
            public Object getAttribute(Name name) {
                return null;
            }
            public int getAttributeCount() {
                return 0;
            }
            public void setAttribute(Name name, Object value) {
            }
            public void setAttributes(List<Object> values) {
            }
            public void setDefaultGeometry(Object geometry) {
            }
            public GeometryAttribute getDefaultGeometryProperty() {
                return null;
            }
            public void setDefaultGeometryProperty(
                    GeometryAttribute geometryAttribute) {
            }
            public Collection<Property> getProperties(Name name) {
                return null;
            }
            public Collection<Property> getProperties(String name) {
                return null;
            }
            public Property getProperty(Name name) {
                return null;
            }
            public Property getProperty(String name) {
                return null;
            }
            public void setValue(Collection<Property> values) {
            }
            public Name getName() {
                return null;
            }
            public Map<Object, Object> getUserData() {
                return null;
            }
            public boolean isNillable() {
                return false;
            }
            public Collection<Property> getProperties() {
            	return null;
            }
            public void validate() {
            }
		}
		SimpleFeature f = new TestFeature();
		try{f.setAttribute("CityName","Vancouver");}catch(Exception e){}
		GazetteerNameValidation gnv = new GazetteerNameValidation();
		gnv.setAttributeName("CityName");
		try{gnv.setGazetteer(new URL("http://cgdi-dev.geoconnections.org/cgi-bin/prototypes/cgdigaz/cgdigaz.cgi?version=1.0&request=GetPlacenameGeometry&wildcards=false&geomtype=bbox"));}catch(Exception e){}
//		ValidationResults results = new RoadValidationResults();
//		if(!gnv.validate(f,null,results)){
//			fail("Did not validate.");
//		}
		
	}
	
	public void testURLConnection(){
		String place = "Vancouver";
		try{
			URL gazetteerURL = new URL("http://cgdi-dev.geoconnections.org/cgi-bin/prototypes/cgdigaz/cgdigaz.cgi?version=1.0&request=GetPlacenameGeometry&wildcards=false&geomtype=bbox&placename="+place);
			HttpURLConnection gazetteerConnection = (HttpURLConnection)gazetteerURL.openConnection();
//            gazetteerConnection.setConnectTimeout(100);
			if(!("OK".equals(gazetteerConnection.getResponseMessage())))
				throw new Exception("An error occured creating the connection to the Gazetteer.");
			InputStream gazetteerInputStream = gazetteerConnection.getInputStream();
			InputStreamReader gazetteerInputStreamReader = new InputStreamReader(gazetteerInputStream);
			BufferedReader gazetteerBufferedReader = new BufferedReader(gazetteerInputStreamReader);
			

			InputSource gazetteerInputSource = new InputSource(gazetteerBufferedReader);
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);

			// TODO turn on validation
			dfactory.setValidating(false);
			dfactory.setIgnoringComments(true);
			dfactory.setCoalescing(true);
			dfactory.setIgnoringElementContentWhitespace(true);

			Document serviceDoc = dfactory.newDocumentBuilder().parse(gazetteerInputSource);
			Element elem = serviceDoc.getDocumentElement();
			
			// elem == SimpleFeatureCollection at this point
			
			elem = getChildElement(elem,"queryInfo");
			if(elem==null)
				throw new NullPointerException("Invalid DOM tree returned by gazetteer.");

			// this number is the number of instances found.
			int number = Integer.parseInt(getChildText(elem,"numberOfResults"));
			
			if(number>0){
				// found vancouver!
			}
			else {
				// did not find vancouver
				// (but out plugin worked so we still pass the test
			}
        }
        catch ( ConnectException timedOut ){
            return; // ignore server must be down
        } catch(Exception e){
			e.printStackTrace();
			fail(e.toString());
		}
	}


	/**
	 * getChildElement purpose.
	 * 
	 * <p>
	 * Used to help with XML manipulations. Returns the first child element of
	 * the specified name.
	 * </p>
	 *
	 * @param root The root element to look for children in.
	 * @param name The name of the child element to look for.
	 *
	 * @return The child element found, null if not found.
	 *
	 * @see getChildElement(Element,String,boolean)
	 */
	public static Element getChildElement(Element root, String name) {
		Node child = root.getFirstChild();

		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (name.equals(child.getNodeName())) {
					return (Element) child;
				}
			}
			child = child.getNextSibling();
		}
		return null;
	}

	/**
	 * getChildText purpose.
	 * 
	 * <p>
	 * Used to help with XML manipulations. Returns the first child text value
	 * of the specified element name.
	 * </p>
	 *
	 * @param root The root element to look for children in.
	 * @param childName The name of the attribute to look for.
	 *
	 * @return The value if the child was found, the null otherwise.
	 */
	public static String getChildText(Element root, String childName) {
		Element elem = getChildElement(root, childName);
		if (elem != null) {
			Node child;
			NodeList childs = elem.getChildNodes();
			int nChilds = childs.getLength();
			for (int i = 0; i < nChilds; i++) {
				child = childs.item(i);
				if (child.getNodeType() == Node.TEXT_NODE) {
					return child.getNodeValue();
				}
			}
		}
		return null;
	}
}
