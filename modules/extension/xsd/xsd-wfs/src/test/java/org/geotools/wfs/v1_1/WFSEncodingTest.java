package org.geotools.wfs.v1_1;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WfsFactory;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.xml.Encoder;
import org.geotools.xs.XSSchema;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.filter.FilterFactory2;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class WFSEncodingTest {

    @Before
    public void setup() {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    @Test
    public void encodeUpdate()
            throws IOException, SAXException, TransformerException, XpathException {
        WfsFactory wfsfac = WfsFactory.eINSTANCE;
        FilterFactory2 filterfac = CommonFactoryFinder.getFilterFactory2();
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
        XMLAssert.assertXpathEvaluatesTo("mn:mytypename", "//wfs:Update/@typeName", doc);
        XMLAssert.assertXpathEvaluatesTo(
                "mn:myproperty", "//wfs:Update/wfs:Property/wfs:Name", doc);
        XMLAssert.assertXpathEvaluatesTo("myvalue", "//wfs:Update/wfs:Property/wfs:Value", doc);
        XMLAssert.assertXpathEvaluatesTo(
                "someid", "//wfs:Update/ogc:Filter/ogc:FeatureId/@fid", doc);

        // try with numeric value
        propertyType.setValue(100.25);

        encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(update, WFS.Update));
        doc = encoder.encodeAsDOM(update, WFS.Update);
        XMLAssert.assertXpathEvaluatesTo("100.25", "//wfs:Update/wfs:Property/wfs:Value", doc);

        // try with geometry
        Coordinate insideCoord = new Coordinate(5.2, 7.5);
        Point myPoint = geomfac.createPoint(insideCoord);

        propertyType.setValue(myPoint);

        encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(update, WFS.Update));
        doc = encoder.encodeAsDOM(update, WFS.Update);
        XMLAssert.assertXpathEvaluatesTo(
                "5.2", "//wfs:Update/wfs:Property/wfs:Value/gml:Point/gml:coord/gml:X", doc);
        XMLAssert.assertXpathEvaluatesTo(
                "7.5", "//wfs:Update/wfs:Property/wfs:Value/gml:Point/gml:coord/gml:Y", doc);
    }

    @Test
    public void encodeInsert()
            throws IOException, SAXException, TransformerException, XpathException {
        WfsFactory wfsfac = WfsFactory.eINSTANCE;
        FeatureFactory ff = CommonFactoryFinder.getFeatureFactory(null);
        FeatureTypeFactory ftf = new FeatureTypeFactoryImpl();

        AttributeDescriptor ad =
                ftf.createAttributeDescriptor(
                        XSSchema.STRING_TYPE, new NameImpl("dummyAttribute"), 0, 1, true, null);
        SimpleFeatureType ft =
                ftf.createSimpleFeatureType(
                        new NameImpl("dummyFeatureType"),
                        Collections.singletonList(ad),
                        null,
                        false,
                        null,
                        null,
                        null);
        SimpleFeature feature = ff.createSimpleFeature(new Object[] {"dummyValue"}, ft, "dummyId");

        InsertElementType insert = wfsfac.createInsertElementType();
        insert.getFeature().add(feature);

        Encoder encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(insert, WFS.Insert));
        Document doc = encoder.encodeAsDOM(insert, WFS.Insert);
        XMLAssert.assertXpathEvaluatesTo(
                "dummyId", "//wfs:Insert/wfs:dummyFeatureType/@gml:id", doc);
        XMLAssert.assertXpathEvaluatesTo(
                "dummyValue", "//wfs:Insert/wfs:dummyFeatureType/wfs:dummyAttribute", doc);
    }

    @Test
    public void encodeDelete()
            throws IOException, SAXException, TransformerException, XpathException {
        WfsFactory wfsfac = WfsFactory.eINSTANCE;
        FilterFactory2 filterfac = CommonFactoryFinder.getFilterFactory2();

        DeleteElementType delete = wfsfac.createDeleteElementType();
        delete.setFilter(filterfac.id(filterfac.featureId("someid")));

        Encoder encoder = new Encoder(new WFSConfiguration());
        // System.out.println(encoder.encodeAsString(delete, WFS.Delete));
        Document doc = encoder.encodeAsDOM(delete, WFS.Delete);
        XMLAssert.assertXpathEvaluatesTo(
                "someid", "//wfs:Delete/ogc:Filter/ogc:FeatureId/@fid", doc);
    }
}
