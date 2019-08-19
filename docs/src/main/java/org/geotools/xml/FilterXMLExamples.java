/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.xml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.geotools.xml.filter.FilterFilter;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

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
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        SAXParser parser = parserFactory.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(filterDocument);
        reader.parse(input);
        Filter filter = simpleFilterHandler.filter;
        // saxExample end
    }
}
