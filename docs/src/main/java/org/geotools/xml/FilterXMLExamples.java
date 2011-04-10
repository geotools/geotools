package org.geotools.xml;

import org.geotools.filter.FilterFilter;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

@SuppressWarnings("unused")
public class FilterXMLExamples {

private void saxExample() throws Exception {
    SimpleFeatureType featureType = null;
    InputSource input = null;
    // saxExample start
    
    class SimpleFilterHandler extends DefaultHandler implements FilterHandler {
    public Filter filter;
    
    public void filter(Filter filter) {
        this.filter = filter;
    }
    }
    
    SimpleFilterHandler simpleFilterHandler = new SimpleFilterHandler();
    FilterFilter filterFilter = new FilterFilter(simpleFilterHandler, featureType);
    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFilter);
    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
    
    // parse xml
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setContentHandler(filterDocument);
    reader.parse(input);
    Filter filter = simpleFilterHandler.filter;
    // saxExample end
}
}
