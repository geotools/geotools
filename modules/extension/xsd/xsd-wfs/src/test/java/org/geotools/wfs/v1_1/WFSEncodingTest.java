package org.geotools.wfs.v1_1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WfsFactory;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.test.xml.XmlTestSupport;
import org.geotools.xs.XSSchema;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class WFSEncodingTest extends XmlTestSupport {
    private final WKTReader reader = new WKTReader();

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(
                Namespace("xlink", "http://www.w3.org/1999/xlink"),
                Namespace("wfs", "http://www.opengis.net/wfs"),
                Namespace("gml", "http://www.opengis.net/gml"),
                Namespace("ogc", "http://www.opengis.net/ogc"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void encodeUpdate() throws IOException, SAXException, TransformerException {
        WfsFactory wfsfac = WfsFactory.eINSTANCE;
        FilterFactory filterfac = CommonFactoryFinder.getFilterFactory();
        GeometryFactory geomfac = new GeometryFactory(new PrecisionModel(10));

        UpdateElementType update = wfsfac.createUpdateElementType();
        PropertyType propertyType = wfsfac.createPropertyType();
        propertyType.setName(new QName("http://my.namespace", "myproperty", "mn"));
        update.getProperty().add(propertyType);
        update.setTypeName(new QName("http://my.namespace", "mytypename", "mn"));
        update.setFilter(filterfac.id(filterfac.featureId("someid")));

        // try with string
        propertyType.setValue("myvalue");

        Encoder encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(update, WFS.Update));
        Document doc = encoder.encodeAsDOM(update, WFS.Update);
        assertThat(doc, hasXPath("//wfs:Update/@typeName", equalTo("mn:mytypename")));
        assertThat(doc, hasXPath("//wfs:Update/wfs:Property/wfs:Name", equalTo("mn:myproperty")));
        assertThat(doc, hasXPath("//wfs:Update/wfs:Property/wfs:Value", equalTo("myvalue")));
        assertThat(doc, hasXPath("//wfs:Update/ogc:Filter/ogc:FeatureId/@fid", equalTo("someid")));

        // try with numeric value
        propertyType.setValue(100.25);

        encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(update, WFS.Update));
        doc = encoder.encodeAsDOM(update, WFS.Update);
        assertThat(doc, hasXPath("//wfs:Update/wfs:Property/wfs:Value", equalTo("100.25")));

        // try with geometry
        Coordinate insideCoord = new Coordinate(5.2, 7.5);
        Point myPoint = geomfac.createPoint(insideCoord);

        propertyType.setValue(myPoint);

        encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(update, WFS.Update));
        doc = encoder.encodeAsDOM(update, WFS.Update);
        assertThat(doc, hasXPath("//wfs:Update/wfs:Property/wfs:Value/gml:Point/gml:pos", equalTo("5.2 7.5")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void encodeInsert() throws IOException, SAXException, TransformerException {
        WfsFactory wfsfac = WfsFactory.eINSTANCE;
        FeatureFactory ff = CommonFactoryFinder.getFeatureFactory(null);
        FeatureTypeFactory ftf = new FeatureTypeFactoryImpl();

        AttributeDescriptor ad =
                ftf.createAttributeDescriptor(XSSchema.STRING_TYPE, new NameImpl("dummyAttribute"), 0, 1, true, null);
        SimpleFeatureType ft = ftf.createSimpleFeatureType(
                new NameImpl("dummyFeatureType"), Collections.singletonList(ad), null, false, null, null, null);
        SimpleFeature feature = ff.createSimpleFeature(new Object[] {"dummyValue"}, ft, "dummyId");

        InsertElementType insert = wfsfac.createInsertElementType();
        insert.getFeature().add(feature);

        Encoder encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(insert, WFS.Insert));
        Document doc = encoder.encodeAsDOM(insert, WFS.Insert);
        assertThat(doc, hasXPath("//wfs:Insert/wfs:dummyFeatureType/wfs:dummyAttribute", equalTo("dummyValue")));
        assertThat(doc, hasXPath("//wfs:Insert/wfs:dummyFeatureType/@gml:id", equalTo("dummyId")));
    }

    @Test
    public void encodeDelete() throws IOException, SAXException, TransformerException {
        WfsFactory wfsfac = WfsFactory.eINSTANCE;
        FilterFactory filterfac = CommonFactoryFinder.getFilterFactory();

        DeleteElementType delete = wfsfac.createDeleteElementType();
        delete.setFilter(filterfac.id(filterfac.featureId("someid")));

        Encoder encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(delete, WFS.Delete));
        Document doc = encoder.encodeAsDOM(delete, WFS.Delete);
        assertThat(doc, hasXPath("//wfs:Delete/ogc:Filter/ogc:FeatureId/@fid", equalTo("someid")));
    }

    @Test
    public void encodeUpdateGeometryPoint()
            throws IOException, SAXException, ParserConfigurationException, ParseException {

        Geometry updateGeometry = reader.read("POINT(-79.460958 43.972668)");

        testUpdateGeometry(updateGeometry);
    }

    @Test
    public void encodeUpdateGeometryMultiPoint()
            throws IOException, SAXException, ParserConfigurationException, ParseException {

        Geometry updateGeometry = reader.read("MULTIPOINT((-79.460958 43.972669), (-79.460858 43.972668))");

        testUpdateGeometry(updateGeometry);
    }

    @Test
    public void encodeUpdateGeometryLineString()
            throws IOException, SAXException, ParserConfigurationException, ParseException {

        Geometry updateGeometry =
                reader.read("LINESTRING(-71.160281 42.258729,-71.160837 42.259113,-71.161144 42.25932)");

        testUpdateGeometry(updateGeometry);
    }

    @Test
    public void encodeUpdateGeometryMultiLineString()
            throws IOException, SAXException, ParserConfigurationException, ParseException {

        Geometry updateGeometry = reader.read("MULTILINESTRING("
                + "(-71.160281 42.258729,-71.160837 42.259113,-71.161144 42.25932), "
                + "(-71.161281 42.258729,-71.161837 42.259113,-71.161144 42.25932))");

        testUpdateGeometry(updateGeometry);
    }

    @Test
    public void encodeUpdateGeometryPolygon()
            throws IOException, SAXException, ParserConfigurationException, ParseException {

        Geometry updateGeometry = reader.read(
                "POLYGON ((10.301758 43.935256, 10.301 43.934721, 10.302234 43.934650, 10.301758 43.935256))");

        testUpdateGeometry(updateGeometry);
    }

    @Test
    public void encodeUpdateGeometryPolygonWithInsidePolygons()
            throws IOException, SAXException, ParserConfigurationException, ParseException {
        Geometry updateGeometry = reader.read(
                "POLYGON((10.301758 43.935256, 10.301001 43.934721, 10.302234 43.934650, 10.301758 43.935256))");

        testUpdateGeometry(updateGeometry);
    }

    @Test
    public void encodeUpdateGeometryMultiPolygon()
            throws IOException, SAXException, ParserConfigurationException, ParseException {

        Geometry updateGeometry =
                reader.read("MULTIPOLYGON (((10.299993 43.935060, 10.300286 43.934755, 10.299704 43.934802, "
                        + "10.299993 43.935060)), "
                        + "((10.300093 43.935097, 10.300515 43.935170, 10.300372 43.934810, 10.300093 43.935097)))");

        testUpdateGeometry(updateGeometry);
    }

    @Test
    public void encodeUpdateGeometryMultiPolygons()
            throws IOException, SAXException, ParserConfigurationException, ParseException {
        Geometry updateGeometry = reader.read("MULTIPOLYGON (("
                + "(10.300335 43.934631, 10.300180 43.934321, 10.300614 43.934447, 10.300335 43.934631), "
                + "(10.300377 43.934554, 10.300473 43.934468, 10.300288 43.934404, 10.300288 43.934404, "
                + "10.300377 43.934554)), "
                + "((10.300415 43.934674, 10.300773 43.934797, 10.300663 43.934510, 10.300415 43.934674), "
                + "(10.300529 43.934662, 10.300705 43.934731, 10.300635 43.934591, 10.300529 43.934662)))");

        testUpdateGeometry(updateGeometry);
    }

    @SuppressWarnings("unchecked")
    private void testUpdateGeometry(Geometry updateGeometry)
            throws IOException, ParserConfigurationException, SAXException {
        WfsFactory wfsfac = WfsFactory.eINSTANCE;
        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

        UpdateElementType update = wfsfac.createUpdateElementType();
        update.setTypeName(new QName("http://my.namespace", "myproperty", "mn"));
        update.setFilter(filterFactory.id(filterFactory.featureId("1")));

        PropertyType propertyType = wfsfac.createPropertyType();
        propertyType.setName(new QName("http://my.namespace", "geomColumn", "mn"));
        propertyType.setValue(updateGeometry);
        update.getProperty().add(propertyType);

        Encoder encoder = new Encoder(new WFSConfiguration());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            encoder.encode(update, WFS.Update, out);

            Parser parser = new Parser(new WFSConfiguration());
            try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                UpdateElementType updateElementType = (UpdateElementType) parser.parse(in);

                List<PropertyType> properties = updateElementType.getProperty();
                Geometry parsedGeometry = (Geometry) properties.get(0).getValue();

                assertEquals(updateGeometry.toText(), parsedGeometry.toText());
            }
        }
    }
}
