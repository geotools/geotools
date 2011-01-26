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

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.SubSelect;

import org.geotools.arcsde.session.ISession;

/**
 * Qualifies the column references (aliased or not) the ArcSDE "table.user." prefix as required by
 * the ArcSDE java api to not get confused when using joined tables.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/view/ExpressionQualifier.java $
 * @since 2.3.x
 */
class ExpressionQualifier implements ExpressionVisitor {
    private Expression _qualifiedExpression;

    private ISession session;

    private Map<String, Object> tableAliases;

    /**
     * Creates a new ExpressionQualifier object.
     * 
     * @param session
     */
    private ExpressionQualifier(ISession session, Map<String, Object> tableAliases) {
        this.session = session;
        this.tableAliases = tableAliases;
    }

    public static Expression qualify(ISession session, Map<String, Object> tableAliases,
            Expression exp) {
        if (exp == null) {
            return null;
        }

        ExpressionQualifier qualifier = new ExpressionQualifier(session, tableAliases);

        exp.accept(qualifier);

        return qualifier._qualifiedExpression;
    }

    public void visit(NullValue nullValue) {
        _qualifiedExpression = nullValue;
    }

    public void visit(Function function) {
        Function qfunction = new Function();
        qfunction.setAllColumns(function.isAllColumns());
        qfunction.setEscaped(function.isEscaped());
        qfunction.setName(function.getName());

        ExpressionList parameters = function.getParameters();
        ExpressionList qualifiedParams;

        qualifiedParams = (ExpressionList) ItemsListQualifier.qualify(session, tableAliases,
                parameters);

        qfunction.setParameters(qualifiedParams);

        this._qualifiedExpression = qfunction;
    }

    public void visit(InverseExpression inverseExpression) {
        InverseExpression qInv = new InverseExpression();

        Expression exp = inverseExpression.getExpression();
        Expression qExp = ExpressionQualifier.qualify(session, tableAliases, exp);

        qInv.setExpression(qExp);
        this._qualifiedExpression = qInv;
    }

    public void visit(JdbcParameter jdbcParameter) {
        this._qualifiedExpression = jdbcParameter;
    }

    public void visit(DoubleValue doubleValue) {
        this._qualifiedExpression = doubleValue;
    }

    public void visit(LongValue longValue) {
        this._qualifiedExpression = longValue;
    }

    public void visit(DateValue dateValue) {
        this._qualifiedExpression = dateValue;
    }

    public void visit(TimeValue timeValue) {
        this._qualifiedExpression = timeValue;
    }

    public void visit(TimestampValue timestampValue) {
        this._qualifiedExpression = timestampValue;
    }

    public void visit(Parenthesis parenthesis) {
        Expression pExp = parenthesis.getExpression();
        Expression qualifiedExpression;
        qualifiedExpression = qualify(session, tableAliases, pExp);

        Parenthesis qualified = new Parenthesis();
        qualified.setExpression(qualifiedExpression);
        this._qualifiedExpression = qualified;
    }

    public void visit(StringValue stringValue) {
        this._qualifiedExpression = stringValue;
    }

    private void visitBinaryExpression(BinaryExpression exp) {

        Expression left = ExpressionQualifier.qualify(session, tableAliases,
                exp.getLeftExpression());
        Expression right = ExpressionQualifier.qualify(session, tableAliases,
                exp.getRightExpression());

        BinaryExpression qualified;

        if (exp instanceof Addition)
            qualified = new Addition();
        else if (exp instanceof Division)
            qualified = new Division();
        else if (exp instanceof Multiplication)
            qualified = new Multiplication();
        else if (exp instanceof Subtraction)
            qualified = new Subtraction();
        else if (exp instanceof EqualsTo)
            qualified = new EqualsTo();
        else if (exp instanceof GreaterThan)
            qualified = new GreaterThan();
        else if (exp instanceof GreaterThanEquals)
            qualified = new GreaterThanEquals();
        else if (exp instanceof LikeExpression)
            qualified = new LikeExpression();
        else if (exp instanceof MinorThan)
            qualified = new MinorThan();
        else if (exp instanceof MinorThanEquals)
            qualified = new MinorThanEquals();
        else if (exp instanceof NotEqualsTo)
            qualified = new NotEqualsTo();
        else
            throw new IllegalArgumentException("Unkown binary expression: " + exp);

        qualified.setLeftExpression(left);
        qualified.setRightExpression(right);

        this._qualifiedExpression = qualified;
    }

    public void visit(Addition addition) {
        visitBinaryExpression(addition);
    }

    public void visit(Division division) {
        visitBinaryExpression(division);
    }

    public void visit(Multiplication multiplication) {
        visitBinaryExpression(multiplication);
    }

    public void visit(Subtraction subtraction) {
        visitBinaryExpression(subtraction);
    }

    public void visit(AndExpression andExpression) {
        Expression left = qualify(session, tableAliases, andExpression.getLeftExpression());
        Expression rigth = qualify(session, tableAliases, andExpression.getRightExpression());

        AndExpression and = new AndExpression(left, rigth);
        this._qualifiedExpression = and;
    }

    public void visit(OrExpression orExpression) {
        Expression left = qualify(session, tableAliases, orExpression.getLeftExpression());
        Expression rigth = qualify(session, tableAliases, orExpression.getRightExpression());

        OrExpression or = new OrExpression(left, rigth);
        this._qualifiedExpression = or;
    }

    public void visit(Between between) {
        Between qualified = new Between();

        Expression start = qualify(session, tableAliases, between.getBetweenExpressionStart());
        Expression end = qualify(session, tableAliases, between.getBetweenExpressionEnd());
        Expression left = qualify(session, tableAliases, between.getLeftExpression());

        qualified.setBetweenExpressionStart(start);
        qualified.setBetweenExpressionEnd(end);
        qualified.setLeftExpression(left);
        this._qualifiedExpression = qualified;
    }

    public void visit(EqualsTo equalsTo) {
        visitBinaryExpression(equalsTo);
    }

    public void visit(GreaterThan greaterThan) {
        visitBinaryExpression(greaterThan);
    }

    public void visit(GreaterThanEquals greaterThanEquals) {
        visitBinaryExpression(greaterThanEquals);
    }

    public void visit(InExpression inExpression) {
        Expression left = qualify(session, tableAliases, inExpression.getLeftExpression());
        ItemsList itemsList = ItemsListQualifier.qualify(session, tableAliases,
                inExpression.getItemsList());

        InExpression qualified = new InExpression();
        qualified.setLeftExpression(left);
        qualified.setItemsList(itemsList);
        this._qualifiedExpression = qualified;
    }

    public void visit(IsNullExpression isNullExpression) {
        IsNullExpression qualified = new IsNullExpression();
        Expression left = qualify(session, tableAliases, isNullExpression.getLeftExpression());

        qualified.setLeftExpression(left);
        qualified.setNot(isNullExpression.isNot());
        this._qualifiedExpression = qualified;
    }

    public void visit(LikeExpression likeExpression) {
        visitBinaryExpression(likeExpression);
    }

    public void visit(MinorThan minorThan) {
        visitBinaryExpression(minorThan);
    }

    public void visit(MinorThanEquals minorThanEquals) {
        visitBinaryExpression(minorThanEquals);
    }

    public void visit(NotEqualsTo notEqualsTo) {
        visitBinaryExpression(notEqualsTo);
    }

    public void visit(Column tableColumn) {

        Column qualified = ColumnQualifier.qualify(session, tableAliases, tableColumn);
        this._qualifiedExpression = qualified;

    }

    public void visit(SubSelect subSelect) {
        SubSelect qualified = SubSelectQualifier.qualify(session, subSelect);
        this._qualifiedExpression = qualified;
    }

    @SuppressWarnings("unchecked")
    public void visit(CaseExpression caseExpression) {
        Expression switchExpr = qualify(session, tableAliases, caseExpression.getSwitchExpression());
        Expression elseExpr = qualify(session, tableAliases, caseExpression.getElseExpression());

        List<WhenClause> whenClauses = null;
        if (caseExpression.getWhenClauses() != null) {
            whenClauses = new ArrayList<WhenClause>();
            for (Iterator<WhenClause> it = caseExpression.getWhenClauses().iterator(); it.hasNext();) {
                WhenClause whenClause = it.next();
                WhenClause qWhen = (WhenClause) qualify(session, tableAliases, whenClause);
                whenClauses.add(qWhen);
            }
        }

        CaseExpression qualifiedWhen = new CaseExpression();
        qualifiedWhen.setElseExpression(elseExpr);
        qualifiedWhen.setSwitchExpression(switchExpr);
        qualifiedWhen.setWhenClauses(whenClauses);
        this._qualifiedExpression = qualifiedWhen;
    }

    public void visit(WhenClause whenClause) {
        Expression whenExpr = qualify(session, tableAliases, whenClause.getWhenExpression());
        Expression thenExpr = qualify(session, tableAliases, whenClause.getThenExpression());

        WhenClause q = new WhenClause();
        q.setWhenExpression(whenExpr);
        q.setThenExpression(thenExpr);
        this._qualifiedExpression = q;
    }
}
