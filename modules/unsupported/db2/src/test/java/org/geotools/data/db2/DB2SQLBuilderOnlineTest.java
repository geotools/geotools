/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
package org.geotools.data.db2;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.DefaultQuery;
import org.geotools.data.db2.filter.SQLEncoderDB2;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.filter.SQLEncoderException;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Point;

/**
 * Exercise DB2SQLBuilder.
 * 
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2SQLBuilderOnlineTest extends AbstractDB2OnlineTestCase {
	private DB2SQLBuilder sqlBuilder = null;

	private DB2DataStore dataStore = null;

	/**
	 * Setup creates an encoder and SQLBuilder
	 * 
	 * @throws Exception
	 */
	public void connect() throws Exception {
		super.connect();
		this.dataStore = getDataStore();

		SQLEncoderDB2 encoder = new SQLEncoderDB2();
		encoder.setSqlNameEscape("\"");
		sqlBuilder = (DB2SQLBuilder) dataStore.getSqlBuilder("Places");
	}

	public void testFidFilter() throws SQLEncoderException, IOException {
		SimpleFeatureSource fs = dataStore.getFeatureSource("Places");
		SimpleFeatureType ft = fs.getSchema();
		FilterFactory ff2 = CommonFactoryFinder.getFilterFactory(null);

		Set ids = new HashSet();

		FeatureId fid1 = ff2.featureId("1");
		ids.add(fid1);
		Id ff3 = CommonFactoryFinder.getFilterFactory(null).id(ids);
		sqlBuilder = (DB2SQLBuilder) dataStore.getSqlBuilder("Places");
		DefaultQuery query = new DefaultQuery("Places", ff3);
		Filter preFilter = sqlBuilder.getPreQueryFilter(query.getFilter());
		Filter postFilter = sqlBuilder.getPostQueryFilter(query.getFilter());
		String[] attrNames = new String[ft.getAttributeCount()];
		AttributeDescriptor[] attrTypes = new AttributeDescriptor[ft.getAttributeCount()];

		for (int i = 0; i < ft.getAttributeCount(); i++) {
			attrNames[i] = ft.getDescriptor(i).getLocalName();
			attrTypes[i] = ft.getDescriptor(i);
		}

		String fidQuery = this.sqlBuilder.buildSQLQuery("Places",
				this.dataStore.getFIDMapper("Places"), attrTypes, preFilter);
		assertEquals(
				"FID encoding failed",
				"SELECT \"Id\", \"Name\", DB2GSE.ST_AsBinary(\"Geom\") FROM \"Test\".\"Places\" WHERE (\"Id\" = 1)",
				fidQuery);
		
		FeatureId fid2 = ff2.featureId("2");
		ids.add(fid2);
		ff3 = CommonFactoryFinder.getFilterFactory(null).id(ids);

		query = new DefaultQuery("Places", ff3);
		preFilter = sqlBuilder.getPreQueryFilter(query.getFilter());
		fidQuery = this.sqlBuilder.buildSQLQuery("Places",
				this.dataStore.getFIDMapper("Places"), attrTypes, preFilter);
		assertEquals(
				"FID encoding failed",
				"SELECT \"Id\", \"Name\", DB2GSE.ST_AsBinary(\"Geom\") FROM \"Test\".\"Places\" WHERE (\"Id\" = 2) OR (\"Id\" = 1)",
				fidQuery);		
	}

	public void testCompareFilter() throws SQLEncoderException, IOException {
		String typeName = "Places";
		SimpleFeatureSource fs = dataStore.getFeatureSource("Places");
		SimpleFeatureType ft = fs.getSchema();
		org.opengis.filter.FilterFactory ff = CommonFactoryFinder
				.getFilterFactory(null);

		PropertyName column = ff.property("Name");
		Literal compareValue = ff.literal("Zena");
		PropertyIsEqualTo filter = ff.equals(column, compareValue);

		String[] attrNames = new String[ft.getAttributeCount()];
		AttributeDescriptor[] attrTypes = new AttributeDescriptor[ft.getAttributeCount()];

		for (int i = 0; i < ft.getAttributeCount(); i++) {
			attrNames[i] = ft.getDescriptor(i).getLocalName();
			attrTypes[i] = ft.getDescriptor(i);
		}
		sqlBuilder = (DB2SQLBuilder) dataStore.getSqlBuilder("Places");
		DefaultQuery query = new DefaultQuery("Places", filter);
		Filter preFilter = sqlBuilder.getPreQueryFilter(query.getFilter());
		Filter postFilter = sqlBuilder.getPostQueryFilter(query.getFilter());
		String compareQuery = this.sqlBuilder.buildSQLQuery("Places",
				this.dataStore.getFIDMapper("Places"), attrTypes, preFilter);
		assertEquals(
				"compare encoding failed",
				"SELECT \"Id\", \"Name\", DB2GSE.ST_AsBinary(\"Geom\") FROM \"Test\".\"Places\" WHERE \"Name\" = 'Zena'",
				compareQuery);

		sqlBuilder = (DB2SQLBuilder) dataStore.getSqlBuilder("Roads");
		column = ff.property("Length");
		compareValue = ff.literal(2.5);
		filter = ff.equals(column, compareValue);
		query = new DefaultQuery("Roads", filter);
		preFilter = sqlBuilder.getPreQueryFilter(query.getFilter());
		postFilter = sqlBuilder.getPostQueryFilter(query.getFilter());
		compareQuery = this.sqlBuilder.buildSQLQuery("Roads", this.dataStore
				.getFIDMapper("Roads"), attrTypes, preFilter);
		assertEquals(
				"compare encoding failed",
				"SELECT \"ID\", \"Name\", DB2GSE.ST_AsBinary(\"Geom\") FROM \"Test\".\"Roads\" WHERE \"Length\" = 2.5",
				compareQuery);
	}

	public void testLikeFilter() throws SQLEncoderException, IOException {
		String typeName = "Places";
		SimpleFeatureSource fs = dataStore.getFeatureSource("Places");
		SimpleFeatureType ft = fs.getSchema();

		org.opengis.filter.FilterFactory ff = CommonFactoryFinder
				.getFilterFactory(null);
		PropertyName column = ff.property("Name");
		Literal compareValue = ff.literal("Zena");
		PropertyIsEqualTo filter = ff.equals(column, compareValue);

		String pattern = "s_met*s";
		PropertyIsLike lf = ff.like(column, pattern);
		sqlBuilder = (DB2SQLBuilder) dataStore.getSqlBuilder("Places");
		DefaultQuery query = new DefaultQuery("Places", lf);
		Filter preFilter = sqlBuilder.getPreQueryFilter(query.getFilter());
		Filter postFilter = sqlBuilder.getPostQueryFilter(query.getFilter());
		String[] attrNames = new String[ft.getAttributeCount()];
		AttributeDescriptor[] attrTypes = new AttributeDescriptor[ft.getAttributeCount()];

		for (int i = 0; i < ft.getAttributeCount(); i++) {
			attrNames[i] = ft.getDescriptor(i).getLocalName();
			attrTypes[i] = ft.getDescriptor(i);
		}

		String likeQuery = this.sqlBuilder.buildSQLQuery("Places",
				this.dataStore.getFIDMapper("Places"), attrTypes, preFilter);
		assertEquals(
				"LIKE encoding failed",
				"SELECT \"Id\", \"Name\", DB2GSE.ST_AsBinary(\"Geom\") FROM \"Test\".\"Places\" WHERE \"Name\" LIKE 's_met%s' ",
				likeQuery);
	}

	public void testSqlFrom() {
		StringBuffer sb;
		sb = new StringBuffer();
		sqlBuilder.sqlFrom(sb, "Test");
		assertEquals("Encoding didn't match", " FROM \"Test\".\"Test\"", sb
				.toString());
	}

	public void testSqlGeometryColumn() {
		String columnName = "Geom";
		Class geomClass = Point.class;
		
		AttributeTypeBuilder atb = new AttributeTypeBuilder();
		atb.setName(columnName);
		atb.setBinding(geomClass);
		
		AttributeDescriptor geomAttr = atb.buildDescriptor(columnName);

		StringBuffer sb = new StringBuffer();
		this.sqlBuilder.sqlGeometryColumn(sb, geomAttr);
		assertEquals("Encoding didn't match", "DB2GSE.ST_AsBinary(\"Geom\")", sb
				.toString());
	}
}
