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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import org.geotools.arcsde.session.ISession;

import com.esri.sde.sdk.client.SeColumnDefinition;

/**
 * Qualifies instances of {@link net.sf.jsqlparser.statement.select.SelectExpressionItem}, and
 * creates a list of qualified {@link net.sf.jsqlparser.statement.select.SelectExpressionItem} for
 * each {@link net.sf.jsqlparser.statement.select.AllColumns} and
 * {@link net.sf.jsqlparser.statement.select.AllTableColumns} instances. So, this visitor may
 * produce more items than the visited.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/view/SelectItemQualifier.java $
 * @since 2.3.x
 */
class SelectItemQualifier implements net.sf.jsqlparser.statement.select.SelectItemVisitor {

    private List /* <SelectExpressionItem> */qualifiedItems = Collections.EMPTY_LIST;

    private ISession session;

    private Map tableAliases;

    /**
     * Creates a new SelectItemQualifier object.
     * 
     * @param session
     */
    private SelectItemQualifier(ISession session, Map tableAliases) {
        this.session = session;
        this.tableAliases = tableAliases;
    }

    public static List qualify(ISession session, Map tableAliases, SelectItem item) {
        if (item == null) {
            return null;
        }

        SelectItemQualifier qualifier = new SelectItemQualifier(session, tableAliases);
        item.accept(qualifier);

        return qualifier.qualifiedItems;
    }

    public void visit(AllColumns allColumns) {
        this.qualifiedItems = Collections.singletonList(allColumns);
    }

    public void visit(AllTableColumns allTableColumns) {
        AllTableColumns qualified = new AllTableColumns();

        Table qt = allTableColumns.getTable();
        Table unaliasedTable = (Table) tableAliases.get(qt.getName());

        if (unaliasedTable == null) {
            // not an aliased table, qualify it
            qt = TableQualifier.qualify(session, allTableColumns.getTable());
        } else {
            // AllTableColumns is refering to an aliased table in the FROM
            // clause,
            // replace its table by the original one to get rid of the alias
            qt = unaliasedTable;
        }

        qualified.setTable(qt);

        String tableName = qt.getSchemaName() + "." + qt.getName();
        SeColumnDefinition[] cols;
        try {
            cols = session.describe(tableName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        qualifiedItems = new ArrayList(cols.length);

        for (int i = 0; i < cols.length; i++) {
            String colName = cols[i].getName();

            Column column = new Column();
            column.setTable(qt);
            column.setColumnName(colName);

            SelectExpressionItem item = new SelectExpressionItem();
            item.setExpression(column);

            qualifiedItems.add(item);
        }
    }

    public void visit(SelectExpressionItem selectExpressionItem) {

        SelectExpressionItem qualifiedItem = new SelectExpressionItem();

        qualifiedItem.setAlias(selectExpressionItem.getAlias());

        Expression selectExpression = selectExpressionItem.getExpression();

        Expression qualifiedExpression = ExpressionQualifier.qualify(session, tableAliases,
                selectExpression);

        qualifiedItem.setExpression(qualifiedExpression);

        this.qualifiedItems = Collections.singletonList(qualifiedItem);
    }
}
