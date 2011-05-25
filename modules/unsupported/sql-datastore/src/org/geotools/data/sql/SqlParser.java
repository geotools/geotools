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
 *
 */
package org.geotools.data.sql;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.geotools.data.DataSourceException;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

/**
 * Just a utility class to parse a string with a plain SQL SELECT statement into
 * an POJO object model as defined by <a href="http://jsqlparser.sf.net">jsqlparser</a>
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 * @since 2.3.x
  */
public class SqlParser {
	
	
	public static SelectBody parse(String selectStatement)throws IOException{
		CCJSqlParserManager pm = new CCJSqlParserManager();
		Reader reader = new StringReader(selectStatement);
		Statement statement;
		try {
			statement = pm.parse(reader);
		} catch (Exception e) {
			throw new DataSourceException("parsing select statement: " + e.getCause().getMessage(), e);
		}
		if(!(statement instanceof Select)){ //either PlainSelect or Union
			throw new IllegalArgumentException("expected select or union statement: " + statement);
		}
		SelectBody selectBody = ((Select)statement).getSelectBody();
		return selectBody;
	}
}
