package org.geotools.wfs.v1_1;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.xml.namespace.QName;
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
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class WFSEncodingTest extends XmlTestSupport {

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
        assertThat(doc, hasXPath("//wfs:Update/wfs:Property/wfs:Value/gml:Point/gml:coord/gml:X", equalTo("5.2")));
        assertThat(doc, hasXPath("//wfs:Update/wfs:Property/wfs:Value/gml:Point/gml:coord/gml:Y", equalTo("7.5")));
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
}
