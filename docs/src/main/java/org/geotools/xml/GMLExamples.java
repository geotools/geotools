package org.geotools.xml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterFeature;
import org.geotools.gml.GMLFilterGeometry;
import org.geotools.gml.GMLHandlerFeature;
import org.geotools.gml.producer.FeatureTransformer;
import org.geotools.gtxml.GTXML;
import org.geotools.referencing.CRS;
import org.geotools.xml.gml.GMLComplexTypes;
import org.geotools.xml.schema.Schema;
import org.geotools.xs.XSConfiguration;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

public class GMLExamples {

private void saxExample() throws Exception {
    InputSource input = null;
    // saxExample start
    YourCode handler = new YourCode();
    
    GMLFilterFeature filterFeature = new GMLFilterFeature(handler);
    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFeature);
    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
    
    // parse xml
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setContentHandler(filterDocument);
    reader.parse(input);
    
    List<SimpleFeature> features = handler.getFeatures();
    // saxExample end
}

private void saxExample2() throws Exception {
    InputSource input = null;
    // saxExample2 start
    class InlineHandler extends XMLFilterImpl implements GMLHandlerFeature {
    List<SimpleFeature> features = new ArrayList<SimpleFeature>();
    
    public void feature(SimpleFeature feature) {
        features.add(feature);
    }
    }
    InlineHandler inlineHandler = new InlineHandler();
    GMLFilterFeature filterFeature = new GMLFilterFeature(inlineHandler);
    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFeature);
    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
    
    // parse xml
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setContentHandler(filterDocument);
    reader.parse(input);
    
    List<SimpleFeature> features = inlineHandler.features;
    // saxExample2 end
}

private void transformExample() throws Exception {
    // transformExample start
    SimpleFeatureType TYPE = DataUtilities.createType("urn:org.geotools.xml.examples", "location", "geom:Point,name:String");
    TYPE.getUserData().put("prefix", "ex"); 
		
    WKTReader2 wkt = new WKTReader2();
    List<SimpleFeature> collection = new LinkedList<SimpleFeature>();
    collection.add(SimpleFeatureBuilder.build(TYPE, new Object[] { wkt.read("POINT (1 2)"), "name1" }, null));
    collection.add(SimpleFeatureBuilder.build(TYPE, new Object[] { wkt.read("POINT (4 4)"), "name2" }, null));

    SimpleFeatureCollection featureCollection = new ListFeatureCollection(TYPE, collection);
	
    FeatureTransformer transform = new FeatureTransformer();
    transform.setEncoding(Charset.defaultCharset());
    transform.setIndentation(4);
    transform.setGmlPrefixing(true);
	    
    // define feature information
    final SimpleFeatureType schema = featureCollection.getSchema();
    String prefix = (String) schema.getUserData().get("prefix");
    String namespace = schema.getName().getNamespaceURI();
    transform.getFeatureTypeNamespaces().declareDefaultNamespace(prefix, namespace);
    transform.addSchemaLocation(prefix, namespace);
    
    String srsName = CRS.toSRS(schema.getCoordinateReferenceSystem());
    if (srsName != null) {
        transform.setSrsName(srsName);
    }
    
    // define feature collection
    transform.setCollectionPrefix("col");
    transform.setCollectionNamespace("urn:org.geotools.xml.example.collection");
    
    // other configuration
    transform.setCollectionBounding(true); // include bbox info
    
    ByteArrayOutputStream xml = new ByteArrayOutputStream();
    transform.transform(featureCollection, xml);
    xml.close();
    
    System.out.println(xml.toString());
    // transformExample end
}

private void transformExample2() throws Exception {
    SimpleFeatureCollection fc = null;
    OutputStream out = null;
    // transformExample2 start
    SimpleFeatureType ft = fc.getSchema();
    FeatureTransformer tx = new FeatureTransformer();
    
    // set the SRS for the entire featureCollection.
    String srsName = CRS.toSRS(ft.getCoordinateReferenceSystem(), true);
    tx.setSrsName(srsName);
    
    // set the namespace and the prefix
    String namespaceURI = ft.getName().getNamespaceURI();
    tx.getFeatureTypeNamespaces().declareNamespace(ft, "wps", namespaceURI);
    
    // also work-around, get the schema of the featureType
    Schema s = SchemaFactory.getInstance(namespaceURI);
    
    // define a schemaLocation and allow thereby validation!
    tx.addSchemaLocation(namespaceURI, s.getURI().toASCIIString());
    
    tx.transform(fc, out);
    // transformExample2 end
}

private void xdoExample() throws Exception {
    // xdoExample start
    XMLReader reader = XMLReaderFactory.createXMLReader();
    URI schemaLoc = new java.net.URI(
            "http://giswebservices.massgis.state.ma.us/geoserver/wfs?request=describefeaturetype&service=wfs&version=1.0.0&typename=massgis:GISDATA.COUNTIES_POLY");
    
    XSISAXHandler schemaHandler = new XSISAXHandler(schemaLoc);
    
    reader.setContentHandler(schemaHandler);
    reader.parse(new InputSource(new URL(schemaLoc.toString()).openConnection().getInputStream()));
    
    SimpleFeatureType ft = GMLComplexTypes.createFeatureType(schemaHandler.getSchema()
            .getElements()[0]);
    // xdoExample end
}

private void rawSchemaExample() throws Exception {
    Name typeName = null;
    URL schemaLocation = null;
    CoordinateReferenceSystem crs = null;
    // rawSchemaExample start
    
    // assume we are working from WFS 1.1 / GML3 / etc...    
    final QName featureName = new QName(typeName.getNamespaceURI(), typeName.getLocalPart());

    String namespaceURI = featureName.getNamespaceURI();
    String uri = schemaLocation.toExternalForm();
    
    Configuration wfsConfiguration = new org.geotools.gml3.ApplicationSchemaConfiguration(
            namespaceURI, uri);

    FeatureType parsed = GTXML.parseFeatureType(wfsConfiguration, featureName, crs);
    // safely cast down to SimpleFeatureType
    SimpleFeatureType schema = DataUtilities.simple(parsed);
    // rawSchemaExample end
}

private void rawSchemaExample2() throws Exception {
    Name typeName = null;
    URL schemaLocation = null;
    CoordinateReferenceSystem crs = null;
    // rawSchemaExample2 start
    
    // more involved example showing a custom Configuration
    final QName featureName = new QName(typeName.getNamespaceURI(), typeName.getLocalPart());

    String namespaceURI = featureName.getNamespaceURI();
    String uri = schemaLocation.toExternalForm();
    
    // Step 1: Parse schema using XSD 
    XSD xsd = new org.geotools.gml2.ApplicationSchemaXSD(namespaceURI, uri);
    
    // Step 2: custom configuration
    Configuration configuration = new Configuration(xsd) {
        {
            addDependency(new XSConfiguration());
            addDependency(new org.geotools.gml2.GMLConfiguration());
        }
        protected void registerBindings(java.util.Map bindings) {
            // we have no special bindings
        }
    };
    FeatureType parsed = GTXML.parseFeatureType(configuration, featureName, crs);
    // safely cast down to SimpleFeatureType
    SimpleFeatureType schema = DataUtilities.simple(parsed);
    // rawSchemaExample2 end
}
}
