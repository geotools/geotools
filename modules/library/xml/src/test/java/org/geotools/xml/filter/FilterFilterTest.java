/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.filter;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.filter.FilterHandler;
import org.geotools.filter.LogicFilterImpl;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Intersects;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.ParserAdapter;
import org.xml.sax.helpers.XMLFilterImpl;

public class FilterFilterTest {

    @Test
    public void testWithoutFunction() throws Exception {
        String filter =
                "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" "
                        + "outputFormat=\"GML2\" "
                        + "xmlns:topp=\"http://www.openplans.org/topp\" "
                        + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                        + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">"
                        + "<wfs:Query typeName=\"topp:states\">"
                        + "<Filter xmlns=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\">"
                        + "<And>"
                        + "<Intersects>"
                        + "<PropertyName>the_geom</PropertyName>"
                        + "<gml:Polygon>"
                        + "<gml:outerBoundaryIs>"
                        + "<gml:LinearRing><gml:coordinates decimal=\".\" cs=\",\" ts=\" \">-99.79800943339099,30.41833858217994 -99.79800943339099,30.71813913408305 -99.49820888148788,30.71813913408305 -99.49820888148788,30.41833858217994 -99.79800943339099,30.41833858217994</gml:coordinates>"
                        + "</gml:LinearRing>"
                        + "</gml:outerBoundaryIs>"
                        + "</gml:Polygon>"
                        + "</Intersects>"
                        + "<BBOX><PropertyName>the_geom</PropertyName>"
                        + "<gml:Box><gml:coordinates decimal=\".\" cs=\",\" ts=\" \">-124.731422,24.955967 -66.969849,49.371735</gml:coordinates></gml:Box>"
                        + "</BBOX>"
                        + "</And>"
                        + "</Filter>"
                        + "</wfs:Query>"
                        + "</wfs:GetFeature>";
        StringReader reader = new StringReader(filter);

        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        MyHandler contentHandler = new MyHandler();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ParserAdapter adapter = new ParserAdapter(parser.getParser());
        adapter.setContentHandler(documentFilter);
        adapter.parse(requestSource);

        assertEquals(contentHandler.filters.size(), 1);

        LogicFilterImpl f = (LogicFilterImpl) contentHandler.filters.get(0);
        List sub = f.getChildren();
        assertEquals(2, sub.size());

        Filter f1 = (Filter) sub.get(0);
        Filter f2 = (Filter) sub.get(1);

        assertThat(f1, instanceOf(Intersects.class));
        assertThat(f2, instanceOf(BBOX.class));
    }

    @Test
    public void testLikeMatchCase_v1_0() throws Exception {
        testLikeMatchCase_v1_0(true);
        testLikeMatchCase_v1_0(false);
    }

    void testLikeMatchCase_v1_0(boolean matchCase) throws Exception {
        String filter =
                "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" "
                        + "outputFormat=\"GML2\" "
                        + "xmlns:topp=\"http://www.openplans.org/topp\" "
                        + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                        + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">"
                        + "<wfs:Query typeName=\"cite:Buildings\">"
                        + "<Filter xmlns=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\">"
                        + "<PropertyIsLike wildCard=\"*\" singleChar=\".\" escapeChar=\"\\\" matchCase=\""
                        + matchCase
                        + "\">"
                        + "<PropertyName>ADDRESS</PropertyName>"
                        + "<Literal>* MAIN STREET</Literal>"
                        + "</PropertyIsLike>"
                        + "</Filter>"
                        + "</wfs:Query>"
                        + "</wfs:GetFeature>";

        StringReader reader = new StringReader(filter);

        InputSource requestSource = new InputSource(reader);

        // instantiante parsers and content handlers
        MyHandler contentHandler = new MyHandler();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ParserAdapter adapter = new ParserAdapter(parser.getParser());
        adapter.setContentHandler(documentFilter);
        adapter.parse(requestSource);

        assertEquals(contentHandler.filters.size(), 1);

        assertTrue(contentHandler.filters.get(0) instanceof PropertyIsLike);
        PropertyIsLike like = (PropertyIsLike) contentHandler.filters.get(0);
        assertEquals(matchCase, like.isMatchingCase());
    }

    @Test
    public void testWithFunction() throws Exception {
        String filter =
                "<wfs:GetFeature service=\"WFS\" version=\"1.0.0\" "
                        + "outputFormat=\"GML2\" "
                        + "xmlns:topp=\"http://www.openplans.org/topp\" "
                        + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                        + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">"
                        + "<wfs:Query typeName=\"topp:states\">"
                        + "<Filter xmlns=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\">"
                        + "<Or>"
                        + "<And>"
                        + "<Intersects>"
                        + "<PropertyName>the_geom</PropertyName>"
                        + "<gml:Polygon>"
                        + "<gml:outerBoundaryIs>"
                        + "<gml:LinearRing><gml:coordinates decimal=\".\" cs=\",\" ts=\" \">-99.79800943339099,30.41833858217994 -99.79800943339099,30.71813913408305 -99.49820888148788,30.71813913408305 -99.49820888148788,30.41833858217994 -99.79800943339099,30.41833858217994</gml:coordinates>"
                        + "</gml:LinearRing>"
                        + "</gml:outerBoundaryIs>"
                        + "</gml:Polygon>"
                        + "</Intersects>"
                        + "<BBOX><PropertyName>the_geom</PropertyName>"
                        + "<gml:Box><gml:coordinates decimal=\".\" cs=\",\" ts=\" \">-124.731422,24.955967 -66.969849,49.371735</gml:coordinates></gml:Box>"
                        + "</BBOX>"
                        + "</And>"
                        + "<PropertyIsEqualTo>"
                        + "<Function name=\"geometryType\"><PropertyName>the_geom</PropertyName></Function>"
                        + "<Literal>Point</Literal>"
                        + "</PropertyIsEqualTo>"
                        + "</Or>"
                        + "</Filter>"
                        + "</wfs:Query>"
                        + "</wfs:GetFeature>";

        StringReader reader = new StringReader(filter);

        InputSource requestSource = new InputSource(reader);

        //		 instantiante parsers and content handlers
        MyHandler contentHandler = new MyHandler();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        // read in XML file and parse to content handler

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ParserAdapter adapter = new ParserAdapter(parser.getParser());
        adapter.setContentHandler(documentFilter);
        adapter.parse(requestSource);

        assertEquals(contentHandler.filters.size(), 1);
    }

    @Test
    public void testWithFunction2() throws Exception {
        String filter =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<sld:StyledLayerDescriptor xmlns:sld=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:gml=\"http://www.opengis.net/gml\" version=\"1.0.0\">"
                        + "<sld:UserLayer>"
                        + "<sld:LayerFeatureConstraints>"
                        + "<sld:FeatureTypeConstraint/>"
                        + "</sld:LayerFeatureConstraints>"
                        + "<sld:UserStyle>"
                        + "<sld:FeatureTypeStyle>"
                        + "<sld:FeatureTypeName>Feature</sld:FeatureTypeName>"
                        + "<sld:Rule>"
                        + "<ogc:Filter>"
                        + "<ogc:PropertyIsEqualTo>"
                        + "<ogc:Function name=\"geometryType\">"
                        + "<ogc:PropertyName>the_geom</ogc:PropertyName>"
                        + "</ogc:Function>"
                        + "<ogc:Literal>Point</ogc:Literal>"
                        + "</ogc:PropertyIsEqualTo>"
                        + "</ogc:Filter>"
                        + "<sld:PointSymbolizer>"
                        + "</sld:PointSymbolizer>"
                        + "</sld:Rule>"
                        + "<sld:Rule>"
                        + "<ogc:Filter>"
                        + "<ogc:PropertyIsEqualTo>"
                        + "<ogc:Function name=\"geometryType\">"
                        + "<ogc:PropertyName>the_geom</ogc:PropertyName>"
                        + "</ogc:Function>"
                        + "<ogc:Literal>MultiPoint</ogc:Literal>"
                        + "</ogc:PropertyIsEqualTo>"
                        + "</ogc:Filter>"
                        + "<sld:PointSymbolizer>"
                        + "</sld:PointSymbolizer>"
                        + "</sld:Rule>"
                        + "</sld:FeatureTypeStyle>"
                        + "</sld:UserStyle>"
                        + "</sld:UserLayer>"
                        + "</sld:StyledLayerDescriptor>";

        StringReader reader = new StringReader(filter);

        InputSource requestSource = new InputSource(reader);

        //		 instantiante parsers and content handlers
        MyHandler contentHandler = new MyHandler();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        //        Logger logger = Logging.getLogger(FilterFilterTest.class);
        //        logger.setLevel(Level.ALL);
        //        ConsoleHandler consoleHandler = new ConsoleHandler();
        //        consoleHandler.setLevel(Level.ALL);
        //		logger.addHandler(consoleHandler);
        // read in XML file and parse to content handler
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ParserAdapter adapter = new ParserAdapter(parser.getParser());
        adapter.setContentHandler(documentFilter);
        adapter.parse(requestSource);

        //        assertEquals(contentHandler.filters.size(),1);
    }

    /**
     * As for GEOT-821, this test ensures that the filter parser makes proper use of the
     * characters(...) method in ContentHandler to not truncate the content of attribute names
     */
    @Test
    public void testLargeFilter() throws Exception {
        final int filterCount = 100;
        String filter =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<GetFeature xmlns=\"http://www.opengis.net/wfs\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\" service=\"WFS\" outputFormat=\"GML2\"><Query typeName=\"topp:roadevent_pnt\"><ogc:PropertyName>roadeventid</ogc:PropertyName>"
                        + "<ogc:Filter>"
                        + "<ogc:Or>";
        for (int i = 0; i < filterCount; i++) {
            StringBuffer attName = new StringBuffer();
            for (int repCount = 0; repCount <= i; repCount++) {
                attName.append("eventtype-" + repCount + "_");
            }

            filter +=
                    "<ogc:PropertyIsEqualTo><ogc:PropertyName>"
                            + attName
                            + "</ogc:PropertyName>"
                            + "<ogc:Literal>literal-"
                            + i
                            + "</ogc:Literal>"
                            + "</ogc:PropertyIsEqualTo>";
        }
        filter += "</ogc:Or>" + "</ogc:Filter>" + "</Query>" + "</GetFeature>";

        StringReader reader = new StringReader(filter);

        InputSource requestSource = new InputSource(reader);

        //       instantiante parsers and content handlers
        MyHandler contentHandler = new MyHandler();
        FilterFilter filterParser = new FilterFilter(contentHandler, null);
        GMLFilterGeometry geometryFilter = new GMLFilterGeometry(filterParser);
        GMLFilterDocument documentFilter = new GMLFilterDocument(geometryFilter);

        Logger logger = Logging.getLogger(FilterFilterTest.class);
        logger.setLevel(Level.INFO);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        logger.addHandler(consoleHandler);

        // read in XML file and parse to content handler
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        ParserAdapter adapter = new ParserAdapter(parser.getParser());
        adapter.setContentHandler(documentFilter);
        adapter.parse(requestSource);

        assertEquals(1, contentHandler.filters.size());
        Filter f = (Filter) contentHandler.filters.get(0);
        assertTrue(f instanceof BinaryLogicOperator);
        assertThat(f, instanceOf(Or.class));

        int i = 0;
        for (Iterator<org.opengis.filter.Filter> filters =
                        ((BinaryLogicOperator) f).getChildren().iterator();
                filters.hasNext();
                i++) {
            BinaryComparisonOperator subFilter = (BinaryComparisonOperator) filters.next();
            StringBuffer attName = new StringBuffer();
            for (int repCount = 0; repCount <= i; repCount++) {
                attName.append("eventtype-" + repCount + "_");
            }
            String parsedName = ((PropertyName) subFilter.getExpression1()).getPropertyName();
            assertEquals("at index " + i, attName.toString(), parsedName);
            assertEquals("literal-" + i, ((Literal) subFilter.getExpression2()).getValue());
        }
        assertEquals(filterCount, i);
    }

    static class MyHandler extends XMLFilterImpl implements FilterHandler {

        public List<org.opengis.filter.Filter> filters = new ArrayList<org.opengis.filter.Filter>();

        public void filter(org.opengis.filter.Filter filter) {
            filters.add(filter);
        }
    }
}
