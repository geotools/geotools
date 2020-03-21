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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.geotools.arcsde.session.ISession;

/**
 * Seems to visit a list and update the entries and fill in the blanks qualifying them.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @since 2.3.x
 */
class ItemsListQualifier implements ItemsListVisitor {

    ItemsList _qualifiedList;

    private ISession session;

    private Map<String, Object> tableAliases;

    /** Creates a new ItemsListQualifier object. */
    public ItemsListQualifier(ISession session, Map<String, Object> tableAliases) {
        this.session = session;
        this.tableAliases = tableAliases;
    }

    public static ItemsList qualify(
            ISession session, Map<String, Object> tableAliases, ItemsList items) {
        if (items == null) {
            return null;
        }

        ItemsListQualifier q = new ItemsListQualifier(session, tableAliases);
        items.accept(q);

        return q._qualifiedList;
    }

    public void visit(SubSelect subSelect) {
        SubSelect qualified = SubSelectQualifier.qualify(session, subSelect);
        this._qualifiedList = qualified;
    }

    @SuppressWarnings("unchecked")
    public void visit(ExpressionList expressionList) {
        List<Expression> expressions = expressionList.getExpressions();
        List<Expression> qualifiedList = new ArrayList<Expression>(expressions.size());

        for (Iterator<Expression> it = expressions.iterator(); it.hasNext(); ) {
            Expression exp = (Expression) it.next();
            Expression qExp = ExpressionQualifier.qualify(session, tableAliases, exp);

            qualifiedList.add(qExp);
        }

        ExpressionList qExpList = new ExpressionList();
        qExpList.setExpressions(qualifiedList);
        this._qualifiedList = qExpList;
    }
}
