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
package org.geotools.arcsde.data.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import org.geotools.arcsde.session.ISession;

import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeSqlConstruct;

/**
 * Visits a {@link net.sf.jsqlparser.statement.select.PlainSelect} SQL SELECT construct to create
 * the correspondent {@link com.esri.sde.sdk.client.SeQueryInfo} object, that can be used as an in
 * process view definition of ArcSDE Java API.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/view/QueryInfoParser.java $
 * @since 2.3.x
 */
@SuppressWarnings("unchecked")
public class QueryInfoParser {
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(QueryInfoParser.class.getPackage().getName());

    public static SeQueryInfo parse(ISession session, PlainSelect select) throws SeException,
            IOException {
        String[] columns = null;
        String[] tables = null;
        String where = null;
        String orderAndOrGroupByClause = null;

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("building SeQueryInfo to reflect " + select);
        }

        // obtain needed SeQueryInfo components

        columns = getColumns(session, select.getSelectItems());
        tables = getTables(select.getFromItems());

        Expression whereClause = select.getWhere();
        if (whereClause != null) {
            where = whereClause.toString();
        }

        if (select.getGroupByColumnReferences() != null
                && select.getGroupByColumnReferences().size() > 0) {
            String gb = PlainSelect.getFormatedList(select.getGroupByColumnReferences(),
                    " GROUP BY ");
            orderAndOrGroupByClause = gb;
        }
        if (select.getOrderByElements() != null && select.getOrderByElements().size() > 0) {
            String ob = PlainSelect.orderByToString(select.getOrderByElements());
            if (orderAndOrGroupByClause == null) {
                orderAndOrGroupByClause = "";
            }
            orderAndOrGroupByClause += " " + ob;
        }

        // build SeQueryInfo
        SeQueryInfo qinfo = new SeQueryInfo();
        qinfo.setColumns(columns);

        SeSqlConstruct sqlConstruct = new SeSqlConstruct();
        sqlConstruct.setTables(tables);
        if (where != null) {
            sqlConstruct.setWhere(where);
        }

        qinfo.setConstruct(sqlConstruct);

        if (orderAndOrGroupByClause != null) {
            qinfo.setByClause(orderAndOrGroupByClause);
        }

        return qinfo;
    }

    private static String[] getTables(List fromItems) {
        if (fromItems == null) {
            throw new NullPointerException("fromItems");
        }

        List tableNames = new ArrayList(fromItems.size());

        for (Iterator it = fromItems.iterator(); it.hasNext();) {
            FromItem fromItem = (FromItem) it.next();
            String fromItemDef = fromItem.toString();
            tableNames.add(fromItemDef);
        }
        return (String[]) tableNames.toArray(new String[tableNames.size()]);
    }

    /**
     * @param selectItems
     * @return <code>null</code> if <code>selectItems</code> is null or contains only an
     *         {@link net.sf.jsqlparser.statement.select.AllColumns}
     */
    private static String[] getColumns(ISession session, List selectItems) throws IOException {
        if (selectItems == null || selectItems.size() == 0) {
            return null;
        }

        SelectItem item;
        List colNames = new ArrayList(selectItems.size());
        for (Iterator it = selectItems.iterator(); it.hasNext();) {
            item = (SelectItem) it.next();
            if (item instanceof AllColumns) {
                continue;
            } else if (item instanceof AllTableColumns) {
                AllTableColumns allTableCols = (AllTableColumns) item;
                Table table = allTableCols.getTable();
                List tableColNames = getTableColumns(session, table);
                colNames.addAll(tableColNames);
            } else if (item instanceof SelectExpressionItem) {
                String stringItem = item.toString();
                colNames.add(stringItem);
            } else {
                throw new RuntimeException("unknown select item type: " + item);
            }
        }

        String[] columns = (String[]) colNames.toArray(new String[colNames.size()]);
        return columns;
    }

    private static List getTableColumns(ISession session, Table table) throws IOException {
        List colNames = new ArrayList();
        String tableName = table.getSchemaName() + "." + table.getName();
        SeColumnDefinition[] cols = session.describe(tableName);
        for (int i = 0; i < cols.length; i++) {
            String colName = cols[i].getName();
            colName = tableName + "." + colName;
            colNames.add(colName);
        }
        return colNames;
    }
}
