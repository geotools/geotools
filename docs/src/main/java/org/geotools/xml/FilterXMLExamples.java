/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
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
