/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sql.view;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;

import org.geotools.data.sql.SqlParser;

public class SqlParserTest extends TestCase {

	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testIllegalSelect(){
		String wrongStatement = "SELECT table SET val1 = 1";
		try{
			SqlParser.parse(wrongStatement);
			fail("No select statement should have been thrown an exception");
		}catch(IOException e){
			//ok
		}
	}

	public void testNoSelect(){
		String unsupportedStatement = "UPDATE table SET val1 = 1";
		try{
			SqlParser.parse(unsupportedStatement);
			fail("No select statement should have been thrown an exception");
		}catch(IOException e){
			//ok
		}
	}
	
	public void testSimpleSelect()throws IOException{
		String statement = "SELECT * FROM table1 INNER JOIN table2 on table1.id = table2.parent";
		PlainSelect select = (PlainSelect)SqlParser.parse(statement);
		
		assertNotNull(select);
		
		//I'll put this null checking only here as to demostrate
		//the elements of a PlainSelect
		assertNull(select.getDistinct());
		assertNull(select.getGroupByColumnReferences());
		assertNull(select.getHaving());
		assertNull(select.getInto());
		assertNull(select.getLimit());
		assertNull(select.getOrderByElements());
		assertNull(select.getTop());
		assertNull(select.getWhere());
		
		
		assertNotNull(select.getFromItems());
		assertNotNull(select.getSelectItems());
		
		List fromItems = select.getFromItems();
		assertEquals(1, fromItems.size());
		assertTrue(fromItems.get(0) instanceof Table);

		assertNotNull(select.getJoins());
		Join join = (Join)select.getJoins().get(0);
		assertFalse(join.isOuter());
		assertNull(join.getRightItem().getAlias());
		Expression joinExpr = join.getOnExpression();
		assertTrue(joinExpr instanceof EqualsTo);
	}
}
