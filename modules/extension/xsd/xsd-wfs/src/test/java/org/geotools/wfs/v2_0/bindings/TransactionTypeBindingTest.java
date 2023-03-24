/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import net.opengis.wfs20.AbstractTransactionActionType;
import net.opengis.wfs20.DeleteType;
import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.PropertyType;
import net.opengis.wfs20.TransactionType;
import net.opengis.wfs20.UpdateType;
import net.opengis.wfs20.ValueReferenceType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.emf.common.util.EList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.v2_0.FES;
import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.geotools.xlink.XLINK;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;
import org.w3c.dom.Document;

public class TransactionTypeBindingTest extends WFSTestSupport {

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(
                Namespace("bla", "http://blabla"),
                Namespace("wfs", WFS.NAMESPACE),
                Namespace("gml", GML.NAMESPACE),
                Namespace("fes", FES.NAMESPACE),
                Namespace("xlink", XLINK.NAMESPACE),
                Namespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"),
                Namespace("xsd", "http://www.w3.org/2001/XMLSchema"),
                Namespace("xs", "http://www.w3.org/2001/XMLSchema"));
    }

    @Test
    public void testParseInsert() throws Exception {
        String xml =
                "<wfs:Transaction "
                        + "   version='2.0.0' "
                        + "   service='WFS' "
                        + "   xmlns='http://www.someserver.com/myns' "
                        + "   xmlns:gml='http://www.opengis.net/gml/3.2' "
                        + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                        + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                        + "   xsi:schemaLocation='http://www.someserver.com/myns ./SampleSchema.xsd "
                        + "                       http://www.opengis.net/wfs/2.0 "
                        + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd "
                        + "                       http://www.opengis.net/gml/3.2 "
                        + "                       http://schemas.opengis.net/gml/3.2.1/gml.xsd'> "
                        + "   <wfs:Insert> "
                        + "    <InWaterA_1M gml:id='F1'> "
                        + "      <wkbGeom> "
                        + "        <gml:Polygon srsName='urn:ogc:def:crs:EPSG::4326' gml:id='P1'> "
                        + "          <gml:exterior> "
                        + "            <gml:LinearRing> "
                        + "              <gml:posList>-30.93597221374512 117.6290588378906 -30.94830513000489 117.6447219848633 -30.95219421386719 117.6465530395508 -30.95219421386719 117.6431121826172 -30.94802856445312 117.6386108398438 -30.94799995422363 117.6314163208008 -30.946138381958 117.62850189209 -30.94430541992188 117.6295852661133 -30.93280601501464 117.6240539550781 -30.92869377136231 117.624641418457 -30.92386054992676 117.6201400756836 -30.92111206054688 117.6206970214844 -30.92458343505859 117.6275863647461 -30.93597221374512 117.6290588378906</gml:posList> "
                        + " "
                        + "            </gml:LinearRing> "
                        + "          </gml:exterior> "
                        + "        </gml:Polygon> "
                        + "      </wkbGeom> "
                        + "      <id>28022</id> "
                        + "      <fCode>BH000</fCode> "
                        + "      <hyc>6</hyc> "
                        + " "
                        + "      <tileId>177</tileId> "
                        + "      <facId>132</facId> "
                        + "    </InWaterA_1M> "
                        + "    <InWaterA_1M gml:id='F2'> "
                        + "      <wkbGeom> "
                        + "        <gml:Polygon srsName='urn:ogc:def:crs:EPSG::4326' gml:id='P2'> "
                        + "          <gml:exterior> "
                        + "            <gml:LinearRing> "
                        + " "
                        + "               <gml:posList>-30.92013931274414 117.6552810668945 -30.92383384704589 117.661361694336 -30.93005561828613 117.6666412353516 -30.93280601501464 117.6663589477539 -30.93186187744141 117.6594467163086 -30.93780517578125 117.6541137695312 -30.94397163391114 117.6519470214844 -30.94255638122559 117.6455535888672 -30.93402862548828 117.6336364746094 -30.92874908447266 117.6355285644531 -30.92138862609864 117.6326370239258 -30.92236137390137 117.6395568847656 -30.91708374023438 117.6433029174805 -30.91711044311523 117.6454467773437 -30.92061042785645 117.6484985351563 -30.92061042785645 117.6504135131836 -30.91638946533203 117.6504440307617 -30.92013931274414 117.6552810668945 </gml:posList> "
                        + "            </gml:LinearRing> "
                        + "          </gml:exterior> "
                        + "        </gml:Polygon> "
                        + "      </wkbGeom> "
                        + "      <id>28021</id> "
                        + "      <fCode>BH000</fCode> "
                        + " "
                        + "      <hyc>6</hyc> "
                        + "      <tileId>177</tileId> "
                        + "      <facId>131</facId> "
                        + "    </InWaterA_1M> "
                        + "   </wfs:Insert> "
                        + "</wfs:Transaction> ";
        buildDocument(xml);

        TransactionType t = (TransactionType) parse();
        assertNotNull(t);

        assertEquals(1, t.getAbstractTransactionAction().size());
        assertEquals(1, t.getGroup().size());

        InsertType i = (InsertType) t.getAbstractTransactionAction().get(0);
        assertEquals(2, i.getAny().size());
    }

    @Test
    public void testParseUpdate() throws Exception {
        String xml =
                "<wfs:Transaction "
                        + "   version='2.0.0' "
                        + "   service='WFS' "
                        + "   xmlns='http://www.someserver.com/myns' "
                        + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                        + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                        + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                        + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                        + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                        + "   <wfs:Update typeName='BuiltUpA_1M'> "
                        + "      <wfs:Property> "
                        + "         <wfs:ValueReference>population</wfs:ValueReference> "
                        + "         <wfs:Value>4070000</wfs:Value> "
                        + "      </wfs:Property> "
                        + "      <fes:Filter> "
                        + "         <fes:ResourceId rid='BuiltUpA_1M.10131'/> "
                        + " "
                        + "      </fes:Filter> "
                        + "   </wfs:Update> "
                        + "</wfs:Transaction> ";
        buildDocument(xml);

        TransactionType t = (TransactionType) parse();
        assertNotNull(t);

        assertEquals(1, t.getAbstractTransactionAction().size());

        UpdateType u = (UpdateType) t.getAbstractTransactionAction().get(0);
        assertEquals(1, u.getProperty().size());

        PropertyType p = u.getProperty().get(0);
        assertEquals("population", p.getValueReference().getValue().getLocalPart());
        assertEquals("4070000", p.getValue());

        Id id = (Id) u.getFilter();
        assertNotNull(id);
        assertTrue(id.getIDs().contains("BuiltUpA_1M.10131"));
    }

    @Test
    public void testEncode() throws Exception {
        Wfs20Factory factory = Wfs20Factory.eINSTANCE;
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        TransactionType t = factory.createTransactionType();
        EList<AbstractTransactionActionType> abstractTransactionAction =
                t.getAbstractTransactionAction();

        UpdateType update = factory.createUpdateType();
        update.setTypeName(new QName("http://blabla", "MyFeature", "bla"));
        PropertyType property = factory.createPropertyType();
        ValueReferenceType ref = factory.createValueReferenceType();
        ref.setValue(new QName("http://blabla", "MyProperty", "bla"));
        property.setValueReference(ref);
        property.setValue("myvalue");
        update.getProperty().add(property);
        Set<Identifier> ids = new HashSet<>();
        ids.add(ff.featureId("myid"));
        update.setFilter(ff.id(ids));

        abstractTransactionAction.add(update);

        DeleteType delete = factory.createDeleteType();
        delete.setTypeName(new QName("http://blabla", "MyFeature", "bla"));
        Set<Identifier> ids2 = new HashSet<>();
        ids2.add(ff.featureId("myid2"));
        delete.setFilter(ff.id(ids2));
        abstractTransactionAction.add(delete);

        InsertType insert = factory.createInsertType();
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("MyFeature");
        tb.setNamespaceURI("http://blabla");
        tb.add("geometry", Point.class);
        tb.add("integer", Integer.class);
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());
        b.add(new GeometryFactory().createPoint(new Coordinate(0, 0)));
        b.add(0);
        insert.getAny().add(b.buildFeature("zero"));

        abstractTransactionAction.add(insert);

        Document doc = encode(t, WFS.Transaction);

        assertThat(doc, hasXPath("//wfs:Transaction/wfs:Update", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Transaction/wfs:Insert", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Transaction/wfs:Delete", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Update//wfs:ValueReference", equalTo("bla:MyProperty")));
        assertThat(doc, hasXPath("//wfs:Update//wfs:Value", equalTo("myvalue")));
        assertThat(
                doc,
                hasXPath("//wfs:Update//fes:Filter/fes:ResourceId", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Update//fes:Filter/fes:ResourceId/@rid", equalTo("myid")));
        assertThat(
                doc,
                hasXPath("//wfs:Delete//fes:Filter/fes:ResourceId", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Delete//fes:Filter/fes:ResourceId/@rid", equalTo("myid2")));
        assertThat(doc, hasXPath("//wfs:Insert//bla:MyFeature", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Insert//bla:MyFeature/@gml:id", equalTo("zero")));
        assertThat(
                doc,
                hasXPath("//wfs:Insert//bla:MyFeature/bla:geometry", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Insert//bla:MyFeature/bla:integer", equalTo("0")));
    }

    @Test
    public void testParseDelete() throws Exception {
        String xml =
                "<wfs:Transaction "
                        + "   version='2.0.0' "
                        + "   service='WFS' "
                        + "   xmlns='http://www.someserver.com/myns' "
                        + "   xmlns:fes='http://www.opengis.net/fes/2.0' "
                        + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                        + "   xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' "
                        + "   xsi:schemaLocation='http://www.opengis.net/wfs/2.0 "
                        + "                       http://schemas.opengis.net/wfs/2.0/wfs.xsd'> "
                        + "   <wfs:Delete typeName='InWaterA_1M'> "
                        + "      <fes:Filter> "
                        + "         <fes:ResourceId rid='InWaterA_1M.1013'/> "
                        + "      </fes:Filter> "
                        + "   </wfs:Delete> "
                        + "</wfs:Transaction> ";
        buildDocument(xml);

        TransactionType t = (TransactionType) parse();
        assertEquals(1, t.getAbstractTransactionAction().size());

        DeleteType d = (DeleteType) t.getAbstractTransactionAction().get(0);
        assertNotNull(d.getFilter());

        Id id = (Id) d.getFilter();
        assertTrue(id.getIDs().contains("InWaterA_1M.1013"));
    }
}
