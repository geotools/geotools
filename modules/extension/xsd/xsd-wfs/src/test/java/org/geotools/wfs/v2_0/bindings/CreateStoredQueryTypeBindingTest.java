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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import net.opengis.wfs20.CreateStoredQueryType;
import net.opengis.wfs20.DropStoredQueryType;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.junit.Test;

public class CreateStoredQueryTypeBindingTest extends WFSTestSupport {
    @Test
    public void testParse() throws Exception {
        String xml = "<wfs:DropStoredQuery "
                + "   xmlns:wfs='http://www.opengis.net/wfs/2.0' "
                + "   service='WFS' "
                + "   version='2.0.0' id='foobar'/> ";

        buildDocument(xml);

        DropStoredQueryType dsq = (DropStoredQueryType) parse();
        assertNotNull(dsq);

        assertEquals("foobar", dsq.getId());
    }

    @Test
    public void testParseUnqualified() throws Exception {
        String xml =
                """
                <CreateStoredQuery xmlns="http://www.opengis.net/wfs/2.0" service="WFS" \
                version="2.0.0">
                  <StoredQueryDefinition xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                          id="urn:example:wfs2-query:GetFeatureByTypeName">
                      <Title>GetFeatureByTypeName</Title>
                      <Abstract>Returns feature representations by type name.</Abstract>
                      <Parameter name="typeName" type="xsd:QName">
                         <Abstract>Qualified name of feature type (required).</Abstract>
                      </Parameter>
                      <QueryExpressionText isPrivate="false"
                                           language="urn:ogc:def:queryLanguage:OGC-WFS::WFSQueryExpression"
                                           returnFeatureTypes="">
                         <Query typeNames="${typeName}"/>
                      </QueryExpressionText>
                </StoredQueryDefinition>
                </CreateStoredQuery>""";
        buildDocument(xml);

        CreateStoredQueryType csq = (CreateStoredQueryType) parse(WFS.StoredQueryDescriptionType);
        assertNotNull(csq);

        StoredQueryDescriptionType queryDefinition =
                csq.getStoredQueryDefinition().get(0);
        assertEquals(1, queryDefinition.getQueryExpressionText().size());
        QueryExpressionTextType text = queryDefinition.getQueryExpressionText().get(0);
        assertEquals(
                "<Query typeNames=\"${typeName}\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                        + "xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www"
                        + ".w3.org/2001/XMLSchema-instance\"></Query>",
                text.getValue().trim());
    }

    @Test
    public void testParseRepeatedNamespaces() throws Exception {
        String xml =
                """
                <CreateStoredQuery xmlns="http://www.opengis.net/wfs/2.0" service="WFS" \
                version="2.0.0">
                  <StoredQueryDefinition xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                          id="urn:example:wfs2-query:GetFeatureByTypeName">
                      <Title>GetFeatureByTypeName</Title>
                      <Abstract>Returns feature representations by type name.</Abstract>
                      <Parameter name="typeName" type="xsd:QName">
                         <Abstract>Qualified name of feature type (required).</Abstract>
                      </Parameter>
                      <QueryExpressionText isPrivate="false"
                                           language="urn:ogc:def:queryLanguage:OGC-WFS::WFSQueryExpression"
                                           returnFeatureTypes="">
                         <Query typeNames="${typeName}"/>
                      </QueryExpressionText>
                </StoredQueryDefinition>
                  <StoredQueryDefinition xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                          id="urn:example:wfs2-query:GetFeatureByTypeName">
                      <Title>GetFeatureByTypeName</Title>
                      <Abstract>Returns feature representations by type name.</Abstract>
                      <Parameter name="typeName" type="xsd:QName">
                         <Abstract>Qualified name of feature type (required).</Abstract>
                      </Parameter>
                      <QueryExpressionText isPrivate="false"
                                           language="urn:ogc:def:queryLanguage:OGC-WFS::WFSQueryExpression"
                                           returnFeatureTypes="">
                         <Query typeNames="${typeName}"/>
                      </QueryExpressionText>
                </StoredQueryDefinition>
                </CreateStoredQuery>""";
        buildDocument(xml);

        CreateStoredQueryType csq = (CreateStoredQueryType) parse(WFS.StoredQueryDescriptionType);
        assertNotNull(csq);

        StoredQueryDescriptionType queryDefinition =
                csq.getStoredQueryDefinition().get(0);
        assertEquals(1, queryDefinition.getQueryExpressionText().size());
        QueryExpressionTextType text = queryDefinition.getQueryExpressionText().get(0);
        assertEquals(
                "<Query typeNames=\"${typeName}\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                        + "xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www"
                        + ".w3.org/2001/XMLSchema-instance\"></Query>",
                text.getValue().trim());

        queryDefinition = csq.getStoredQueryDefinition().get(1);
        assertEquals(1, queryDefinition.getQueryExpressionText().size());
        text = queryDefinition.getQueryExpressionText().get(0);
        assertEquals(
                "<Query typeNames=\"${typeName}\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                        + "xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www"
                        + ".w3.org/2001/XMLSchema-instance\"></Query>",
                text.getValue().trim());
    }

    @Test
    public void testParseRepeatedNamespacesWithFilters() throws Exception {
        String xml =
                """
                <CreateStoredQuery xmlns="http://www.opengis.net/wfs/2.0" service="WFS" \
                version="2.0.0">
                  <StoredQueryDefinition xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                          id="urn:example:wfs2-query:GetFeatureByTypeName">
                      <Title>GetFeatureByTypeName</Title>
                      <Abstract>Returns feature representations by type name.</Abstract>
                      <Parameter name="typeName" type="xsd:QName">
                         <Abstract>Qualified name of feature type (required).</Abstract>
                      </Parameter>
                      <QueryExpressionText isPrivate="false"
                                           language="urn:ogc:def:queryLanguage:OGC-WFS::WFSQueryExpression"
                                           returnFeatureTypes="">
                         <Query typeNames="${typeName}"><Filter><ResourceId rid='InWaterA_1M.1234'/></Filter>\
                </Query>
                      </QueryExpressionText>
                </StoredQueryDefinition>
                  <StoredQueryDefinition xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                                          id="urn:example:wfs2-query:GetFeatureByTypeName">
                      <Title>GetFeatureByTypeName</Title>
                      <Abstract>Returns feature representations by type name.</Abstract>
                      <Parameter name="typeName" type="xsd:QName">
                         <Abstract>Qualified name of feature type (required).</Abstract>
                      </Parameter>
                      <QueryExpressionText isPrivate="false"
                                           language="urn:ogc:def:queryLanguage:OGC-WFS::WFSQueryExpression"
                                           returnFeatureTypes="">
                         <Query typeNames="${typeName}"/>
                      </QueryExpressionText>
                </StoredQueryDefinition>
                </CreateStoredQuery>""";
        buildDocument(xml);

        CreateStoredQueryType csq = (CreateStoredQueryType) parse(WFS.StoredQueryDescriptionType);
        assertNotNull(csq);

        StoredQueryDescriptionType queryDefinition =
                csq.getStoredQueryDefinition().get(0);
        assertEquals(1, queryDefinition.getQueryExpressionText().size());
        QueryExpressionTextType text = queryDefinition.getQueryExpressionText().get(0);
        assertEquals(
                "<Query typeNames=\"${typeName}\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                        + "xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www"
                        + ".w3.org/2001/XMLSchema-instance\"><Filter>"
                        + "<ResourceId rid=\"InWaterA_1M.1234\"></ResourceId></Filter></Query>",
                text.getValue().trim());

        queryDefinition = csq.getStoredQueryDefinition().get(1);
        assertEquals(1, queryDefinition.getQueryExpressionText().size());
        text = queryDefinition.getQueryExpressionText().get(0);
        assertEquals(
                "<Query typeNames=\"${typeName}\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                        + "xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" xmlns:xsi=\"http://www"
                        + ".w3.org/2001/XMLSchema-instance\"></Query>",
                text.getValue().trim());
    }
}
