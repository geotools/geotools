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

import java.util.Map;
import net.sf.jsqlparser.statement.select.ColumnReference;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.OrderByVisitor;
import org.geotools.arcsde.session.ISession;

/**
 * Qualifies a column reference in an order by clause
 *
 * @author Gabriel Roldan, Axios Engineering
 * @since 2.3.x
 */
public class OrderByElementQualifier implements OrderByVisitor {

    private OrderByElement _qualifiedOrderBy;

    private ISession session;

    private Map<String, Object> tableAliases;

    /** Creates a new OrderByElementQualifier object. */
    private OrderByElementQualifier(ISession session, Map<String, Object> tableAliases) {
        this.session = session;
        this.tableAliases = tableAliases;
    }

    public static OrderByElement qualify(
            ISession session, Map<String, Object> tableAliases, OrderByElement orderBy) {
        if (orderBy == null) {
            return null;
        }

        OrderByElementQualifier qualifier = new OrderByElementQualifier(session, tableAliases);
        orderBy.accept(qualifier);

        return qualifier._qualifiedOrderBy;
    }

    public void visit(OrderByElement orderBy) {
        OrderByElement qualifiedOrderBy = new OrderByElement();
        qualifiedOrderBy.setAsc(orderBy.isAsc());

        ColumnReference colRef = orderBy.getColumnReference();

        ColumnReference qualifiedColRef =
                ColumnReferenceQualifier.qualify(session, tableAliases, colRef);

        qualifiedOrderBy.setColumnReference(qualifiedColRef);

        this._qualifiedOrderBy = qualifiedOrderBy;
    }
}
