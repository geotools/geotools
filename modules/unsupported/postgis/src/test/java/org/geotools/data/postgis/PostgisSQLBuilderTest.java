/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.postgis.fidmapper.OIDFidMapper;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.filter.Filter;
import org.geotools.filter.SQLEncoder;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 *
 * @source $URL$
 */
public class PostgisSQLBuilderTest extends TestCase {

    PostgisSQLBuilder builder;

    private JDBCDataStoreConfig config;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        config = EasyMock.createMock(JDBCDataStoreConfig.class);
        builder = new PostgisSQLBuilder(4326, config);
        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link PostgisSQLBuilder#sqlFrom(java.lang.StringBuffer, java.lang.String)}.
     */
    public void testSqlFromSchemaDisabled() {
        builder.setSchemaEnabled(false);
        StringBuffer sql = new StringBuffer();
        builder.sqlFrom(sql, "typeName");
        assertEquals(" FROM \"typeName\"", sql.toString());
    }

    /**
     * Test method for {@link PostgisSQLBuilder#sqlFrom(java.lang.StringBuffer, java.lang.String)}.
     */
    public void testSqlFromSchemaEnabled() {
        builder.setSchemaEnabled(true);
        EasyMock.expect(config.getDatabaseSchemaName()).andReturn("schema");
        EasyMock.replay(config);

        StringBuffer sql = new StringBuffer();
        builder.sqlFrom(sql, "typeName");
        assertEquals(" FROM \"schema\".\"typeName\"", sql.toString());

        EasyMock.verify(config);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#sqlWhere(java.lang.StringBuffer, org.geotools.filter.Filter)}.
     * 
     * @throws SQLEncoderException
     */
    public void testSqlWhere() throws SQLEncoderException {
        StringBuffer sql = new StringBuffer();
        Filter preFilter = null;

        builder.sqlWhere(sql, preFilter);
        assertEquals("", sql.toString());

        FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);
        preFilter = (Filter) filterFactory.bbox("the_geom", 0, 0, 1, 1, null);

        SQLEncoder encoder = EasyMock.createMock(SQLEncoder.class);
        encoder.setFeatureType(null);
        EasyMock.expect(encoder.encode(EasyMock.same(preFilter))).andReturn("encoded");
        EasyMock.replay(encoder);

        builder = new PostgisSQLBuilder(encoder, config);
        builder.sqlWhere(sql, preFilter);
        assertEquals(" encoded", sql.toString());

        EasyMock.verify(encoder);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#buildSQLQuery(java.lang.String, org.geotools.data.jdbc.fidmapper.FIDMapper, org.opengis.feature.type.AttributeDescriptor[], org.opengis.filter.Filter, org.opengis.filter.sort.SortBy[], java.lang.Integer, java.lang.Integer)}.
     * 
     * @throws SQLEncoderException
     */
    public void testBuildSQLQueryNoOffsetNoLimit() throws SQLEncoderException {
        FIDMapper mapper = new OIDFidMapper();
        SortBy[] sortBy = null;
        Integer offset = null;
        Integer limit = null;

        String sql = buildSQLQuery(mapper, sortBy, offset, limit);
        final String baseSelect = "SELECT \"oid\", \"attribute\" FROM \"typeName\"";
        assertEquals(baseSelect, sql);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#buildSQLQuery(java.lang.String, org.geotools.data.jdbc.fidmapper.FIDMapper, org.opengis.feature.type.AttributeDescriptor[], org.opengis.filter.Filter, org.opengis.filter.sort.SortBy[], java.lang.Integer, java.lang.Integer)}.
     * 
     * @throws SQLEncoderException
     */
    public void testBuildSQLQueryOrderBy() throws SQLEncoderException {
        FIDMapper mapper = new OIDFidMapper();
        SortBy[] sortBy = { CommonFactoryFinder.getFilterFactory(null).sort("attribute",
                SortOrder.DESCENDING) };
        Integer offset = null;
        Integer limit = null;

        String sql = buildSQLQuery(mapper, sortBy, offset, limit);
        final String baseSelect = "SELECT \"oid\", \"attribute\" FROM \"typeName\"";
        assertEquals(baseSelect + " ORDER BY \"attribute\" DESC", sql);

        sortBy = new SortBy[] { SortBy.NATURAL_ORDER };
        sql = buildSQLQuery(mapper, sortBy, offset, limit);
        assertEquals(baseSelect + " ORDER BY oid ASC", sql);

        sortBy = new SortBy[] { SortBy.REVERSE_ORDER };
        sql = buildSQLQuery(mapper, sortBy, offset, limit);
        assertEquals(baseSelect + " ORDER BY oid DESC", sql);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#buildSQLQuery(java.lang.String, org.geotools.data.jdbc.fidmapper.FIDMapper, org.opengis.feature.type.AttributeDescriptor[], org.opengis.filter.Filter, org.opengis.filter.sort.SortBy[], java.lang.Integer, java.lang.Integer)}.
     * If offset is used and no sortby is provided, the primary key is used as sort order
     * 
     * @throws SQLEncoderException
     */
    public void testBuildSQLQueryOffset() throws SQLEncoderException {
        FIDMapper mapper = new OIDFidMapper();
        SortBy[] sortBy = null;
        Integer offset = new Integer(15);
        Integer limit = null;

        String sql = buildSQLQuery(mapper, sortBy, offset, limit);
        final String baseSelect = "SELECT \"oid\", \"attribute\" FROM \"typeName\"";
        assertEquals(baseSelect + " ORDER BY oid ASC OFFSET 15", sql);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#buildSQLQuery(java.lang.String, org.geotools.data.jdbc.fidmapper.FIDMapper, org.opengis.feature.type.AttributeDescriptor[], org.opengis.filter.Filter, org.opengis.filter.sort.SortBy[], java.lang.Integer, java.lang.Integer)}.
     * An explicit SortBy[] not including the primary key produces an ORDER BY clause including the
     * primary key at the end to ensure consistent order event if the requested attribute list to
     * sort by does not produces a consistently ordered result
     * 
     * @throws SQLEncoderException
     */
    public void testBuildSQLQueryOffsetOrderBy() throws SQLEncoderException {
        FIDMapper mapper = new OIDFidMapper();
        SortBy[] sortBy = { CommonFactoryFinder.getFilterFactory(null).sort("attribute",
                SortOrder.DESCENDING) };
        Integer offset = new Integer(15);
        Integer limit = null;

        String sql = buildSQLQuery(mapper, sortBy, offset, limit);
        final String baseSelect = "SELECT \"oid\", \"attribute\" FROM \"typeName\"";
        assertEquals(baseSelect + " ORDER BY \"attribute\" DESC, oid ASC OFFSET 15", sql);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#buildSQLQuery(java.lang.String, org.geotools.data.jdbc.fidmapper.FIDMapper, org.opengis.feature.type.AttributeDescriptor[], org.opengis.filter.Filter, org.opengis.filter.sort.SortBy[], java.lang.Integer, java.lang.Integer)}.
     * If maxFeatures was provided then encode it as LIMIT <maxFeatures>
     * 
     * @throws SQLEncoderException
     */
    public void testBuildSQLQueryMaxFeatures() throws SQLEncoderException {
        FIDMapper mapper = new OIDFidMapper();
        SortBy[] sortBy = null;
        Integer offset = null;
        Integer limit = new Integer(50);

        String sql = buildSQLQuery(mapper, sortBy, offset, limit);
        final String baseSelect = "SELECT \"oid\", \"attribute\" FROM \"typeName\"";
        assertEquals(baseSelect + " LIMIT 50", sql);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#buildSQLQuery(java.lang.String, org.geotools.data.jdbc.fidmapper.FIDMapper, org.opengis.feature.type.AttributeDescriptor[], org.opengis.filter.Filter, org.opengis.filter.sort.SortBy[], java.lang.Integer, java.lang.Integer)}.
     * If both startIndex and maxFeatures are provided but no sortBy, encode OFFSET and LIMIT as
     * well as PK based ORDER BY
     * 
     * @throws SQLEncoderException
     */
    public void testBuildSQLQueryOffsetMaxFeatures() throws SQLEncoderException {
        FIDMapper mapper = new OIDFidMapper();
        SortBy[] sortBy = null;
        Integer offset = new Integer(15);
        Integer limit = new Integer(30);

        String sql = buildSQLQuery(mapper, sortBy, offset, limit);
        final String baseSelect = "SELECT \"oid\", \"attribute\" FROM \"typeName\"";
        assertEquals(baseSelect + " ORDER BY oid ASC OFFSET 15 LIMIT 30", sql);
    }

    /**
     * Test method for
     * {@link PostgisSQLBuilder#buildSQLQuery(java.lang.String, org.geotools.data.jdbc.fidmapper.FIDMapper, org.opengis.feature.type.AttributeDescriptor[], org.opengis.filter.Filter, org.opengis.filter.sort.SortBy[], java.lang.Integer, java.lang.Integer)}.
     * If startIndex,maxFeatures and sortBy are provided, encode OFFSET and LIMIT and ORDER BY the
     * required attributes, as well as trailing PK based ORDER BY
     * 
     * @throws SQLEncoderException
     */
    public void testBuildSQLQueryOffsetMaxFeaturesOrderBy() throws SQLEncoderException {
        FIDMapper mapper = new OIDFidMapper();
        SortBy[] sortBy = { CommonFactoryFinder.getFilterFactory(null).sort("attribute",
                SortOrder.DESCENDING) };
        Integer offset = new Integer(15);
        Integer limit = new Integer(30);

        String sql = buildSQLQuery(mapper, sortBy, offset, limit);
        final String baseSelect = "SELECT \"oid\", \"attribute\" FROM \"typeName\"";
        assertEquals(baseSelect + " ORDER BY \"attribute\" DESC, oid ASC OFFSET 15 LIMIT 30", sql);
    }

    private String buildSQLQuery(FIDMapper mapper, SortBy[] sortBy, Integer offset, Integer limit)
            throws SQLEncoderException {

        // the attribute to include in the query
        AttributeType type = new AttributeTypeImpl(new NameImpl("attType"), String.class, false,
                false, null, null, null);
        AttributeDescriptor attribute = new AttributeDescriptorImpl(type,
                new NameImpl("attribute"), 0, 1, true, null);
        AttributeDescriptor[] attrTypes = { attribute };

        String typeName = "typeName";

        org.opengis.filter.Filter filter = null;

        builder.setSchemaEnabled(false);
        String sql = builder.buildSQLQuery(typeName, mapper, attrTypes, filter, sortBy, offset,
                limit);

        return sql;
    }

}
