/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.Capabilities;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.visitor.ClientTransactionAccessor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * Determines what queries can be processed server side and which can be processed client side.
 * <p>
 * IMPLEMENTATION NOTE: This class is implemented as a stack processor. If you're curious how it
 * works, compare it with the old SQLUnpacker class, which did the same thing using recursion in a
 * more straightforward way.
 * </p>
 * <p>
 * Here's a non-implementors best-guess at the algorithm: Starting at the top of the filter, split
 * each filter into its constituent parts. If the given FilterCapabilities support the given
 * operator, then keep checking downwards.
 * </p>
 * <p>
 * The key is in knowing whether or not something "down the tree" from you wound up being supported
 * or not. This is where the stacks come in. Right before handing off to accept() the sub- filters,
 * we count how many things are currently on the "can be proccessed by the underlying datastore"
 * stack (the preStack) and we count how many things are currently on the "need to be post-
 * processed" stack.
 * </p>
 * <p>
 * After the accept() call returns, we look again at the preStack.size() and postStack.size(). If
 * the postStack has grown, that means that there was stuff down in the accept()-ed filter that
 * wasn't supportable. Usually this means that our filter isn't supportable, but not always.
 * 
 * In some cases a sub-filter being unsupported isn't necessarily bad, as we can 'unpack' OR
 * statements into AND statements (DeMorgans rule/modus poens) and still see if we can handle the
 * other side of the OR. Same with NOT and certain kinds of AND statements.
 * </p>
 * <p>
 * In addition this class supports the case where we're doing an split in the middle of a
 * client-side transaction. I.e. imagine doing a <Transaction> against a WFS-T where you have to
 * filter against actions that happened previously in the transaction. That's what the
 * ClientTransactionAccessor interface does, and this class splits filters while respecting the
 * information about deletes and updates that have happened previously in the Transaction. I can't
 * say with certainty exactly how the logic for that part of this works, but the test suite does
 * seem to test it and the tests do pass.
 * </p>
 * 
 * @author dzwiers
 * @author commented and ported from gt to ogc filters by saul.farber
 * @author ported to work upon {@code org.geotools.filter.Capabilities} by Gabriel Roldan
 *
 * @source $URL$
 * @since 2.5.3
 */
@SuppressWarnings( { "nls", "unchecked" })
public class CapabilitiesFilterSplitter implements FilterVisitor, ExpressionVisitor {

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.filter");

    /**
     * The stack holding the bits of the filter that are not processable by something with the given
     * {@link FilterCapabilities}
     */
    private Stack postStack = new Stack();

    /**
     * The stack holding the bits of the filter that <b>are</b> processable by something with the
     * given {@link FilterCapabilities}
     */
    private Stack preStack = new Stack();

    /**
     * Operates similar to postStack. When a update is determined to affect an attribute expression
     * the update filter is pushed on to the stack, then ored with the filter that contains the
     * expression.
     */
    private Set changedStack = new HashSet();

    /**
     * The given filterCapabilities that we're splitting on.
     */
    private Capabilities fcs = null;

    private FeatureType parent = null;

    private Filter original = null;

    /**
     * If we're in the middle of a client-side transaction, this object will help us figure out what
     * we need to handle from updates/deletes that we're tracking client-side.
     */
    private ClientTransactionAccessor transactionAccessor;

    private FilterFactory ff;

    /**
     * Create a new instance.
     * 
     * @param fcs
     *            The FilterCapabilties that describes what Filters/Expressions the server can
     *            process.
     * @param parent
     *            The FeatureType that this filter involves. Why is this needed?
     * @param transactionAccessor
     *            If the transaction is handled on the client and not the server then different
     *            filters must be sent to the server. This class provides a generic way of obtaining
     *            the information from the transaction.
     */
    public CapabilitiesFilterSplitter(Capabilities fcs, FeatureType parent,
            ClientTransactionAccessor transactionAccessor) {
        this.ff = CommonFactoryFinder.getFilterFactory(null);
        this.fcs = fcs;
        this.parent = parent;
        this.transactionAccessor = transactionAccessor;
    }

    /**
     * Gets the filter that cannot be sent to the server and must be post-processed on the client by
     * geotools.
     * 
     * @return the filter that cannot be sent to the server and must be post-processed on the client
     *         by geotools.
     */
    public Filter getFilterPost() {
        if (!changedStack.isEmpty())
            // Return the original filter to ensure that
            // correct features are filtered
            return original;

        if (postStack.size() > 1) {
            LOGGER.warning("Too many post stack items after run: " + postStack.size());
        }

        // JE: Changed to peek because get implies that the value can be retrieved multiple
        // times
        Filter f = postStack.isEmpty() ? Filter.INCLUDE : (Filter) postStack.peek();
        return f;
    }

    /**
     * Gets the filter that can be sent to the server for pre-processing.
     * 
     * @return the filter that can be sent to the server for pre-processing.
     */
    public Filter getFilterPre() {
        if (preStack.isEmpty()) {
            return Filter.INCLUDE;
        }

        if (preStack.size() > 1) {
            LOGGER.warning("Too many pre stack items after run: " + preStack.size());
        }

        // JE: Changed to peek because get implies that the value can be retrieved multiple
        // times
        Filter f = preStack.isEmpty() ? Filter.INCLUDE : (Filter) preStack.peek();
        // deal with deletes here !!!
        if (transactionAccessor != null && f != null && f != Filter.EXCLUDE) {
            Filter deleteFilter = transactionAccessor.getDeleteFilter();
            if (deleteFilter != null) {
                if (deleteFilter == Filter.EXCLUDE) {
                    f = Filter.EXCLUDE;
                } else {
                    f = ff.and(f, ff.not(deleteFilter));
                }
            }
        }

        if (changedStack.isEmpty())
            return f;

        Iterator iter = changedStack.iterator();
        Filter updateFilter = (Filter) iter.next();
        while (iter.hasNext()) {
            Filter next = (Filter) iter.next();
            if (next == Filter.INCLUDE) {
                updateFilter = next;
                break;
            } else {
                updateFilter = (Filter) ff.or(updateFilter, next);
            }
        }
        if (updateFilter == Filter.INCLUDE || f == Filter.INCLUDE)
            return Filter.INCLUDE;
        return ff.or(f, updateFilter);
    }

    /**
     * @see FilterVisitor#visit(IncludeFilter, Object)
     * 
     * @param filter
     *            the {@link Filter} to visit
     */
    public void visit(IncludeFilter filter) {
        return;
    }

    /**
     * @see FilterVisitor#visit(ExcludeFilter, Object)
     * 
     * @param filter
     *            the {@link Filter} to visit
     */
    public void visit(ExcludeFilter filter) {
        if (fcs.supports(Filter.EXCLUDE)) {
            preStack.push(filter);
        } else {
            postStack.push(filter);
        }
    }

    /**
     * @see FilterVisitor#visit(PropertyIsBetween, Object) NOTE: This method is extra documented as
     *      an example of how all the other methods are implemented. If you want to know how this
     *      class works read this method first!
     * 
     * @param filter
     *            the {@link Filter} to visit
     */
    public Object visit(PropertyIsBetween filter, Object extradata) {
        if (original == null)
            original = filter;

        // Do we support this filter type at all?
        if (fcs.supports(filter)) {
            // Yes, we do. Now, can we support the sub-filters?

            // first, remember how big the current list of "I can't support these"
            // filters is.
            int i = postStack.size();

            Expression lowerBound = filter.getLowerBoundary();
            Expression expr = filter.getExpression();
            Expression upperBound = filter.getUpperBoundary();
            if (lowerBound == null || upperBound == null || expr == null) {
                // Well, one of the boundaries is null, so I guess
                // we're saying that *no* datastore could support this.
                postStack.push(filter);
                return null;
            }

            // Ok, here's the magic. We know how big our list of "can't support"
            // filters is. Now we send off the lowerBound Expression to see if
            // it can be supported.
            lowerBound.accept(this, null);

            // Now we're back, and we check. Did the postStack get bigger?
            if (i < postStack.size()) {
                // Yes, it did. Well, that means we can't support
                // this particular filter. Let's back out anything that was
                // added by the lowerBound.accept() and add ourselves.
                postStack.pop(); // lowerBound.accept()'s bum filter
                postStack.push(filter);

                return null;
            }

            // Aha! The postStack didn't get any bigger, so we're still
            // all good. Now try again with the middle expression itself...

            expr.accept(this, null);

            // Did postStack get bigger?
            if (i < postStack.size()) {
                // Yes, it did. So that means we can't support
                // this particular filter. We need to back out what we've
                // done, which is BOTH the lowerbounds filter *and* the
                // thing that was added by expr.accept() when it failed.
                preStack.pop(); // lowerBound.accept()'s success
                postStack.pop(); // expr.accept()'s bum filter
                postStack.push(filter);

                return null;
            }

            // Same deal again...
            upperBound.accept(this, null);

            if (i < postStack.size()) {
                // post process it
                postStack.pop(); // upperBound.accept()'s bum filter
                preStack.pop(); // expr.accept()'s success
                preStack.pop(); // lowerBound.accept()'s success
                postStack.push(filter);

                return null;
            }

            // Well, by getting here it means that postStack didn't get
            // taller, even after accepting all three middle filters. This
            // means that this whole filter is totally pre-filterable.

            // Let's clean up the pre-stack (which got one added to it
            // for the success at each of the three above .accept() calls)
            // and add us to the stack.

            preStack.pop(); // upperBounds.accept()'s success
            preStack.pop(); // expr.accept()'s success
            preStack.pop(); // lowerBounds.accept()'s success

            // finally we add ourselves to the "can be pre-proccessed" filter
            // stack. Now when we return we've added exactly one thing to
            // the preStack...namely, the given filter.
            preStack.push(filter);
        } else {
            // No, we don't support this filter.
            // So we push it onto the postStack, saying
            // "Hey, here's one more filter that we don't support.
            // Someone who called us may look at this and say,
            // "Hmm, I called accept() on this filter and now
            // the postStack is taller than it was...I guess this
            // filter wasn't accepted.
            postStack.push(filter);
        }
        return null;
    }

    public Object visit(PropertyIsEqualTo filter, Object notUsed) {
        visitBinaryComparisonOperator(filter);
        return null;
    }

    public Object visit(PropertyIsGreaterThan filter, Object notUsed) {
        visitBinaryComparisonOperator(filter);
        return null;
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object notUsed) {
        visitBinaryComparisonOperator(filter);
        return null;
    }

    public Object visit(PropertyIsLessThan filter, Object notUsed) {
        visitBinaryComparisonOperator(filter);
        return null;
    }

    public Object visit(PropertyIsLessThanOrEqualTo filter, Object notUsed) {
        visitBinaryComparisonOperator(filter);
        return null;
    }

    public Object visit(PropertyIsNotEqualTo filter, Object notUsed) {
        visitBinaryComparisonOperator(filter);
        return null;
    }

    private void visitBinaryComparisonOperator(BinaryComparisonOperator filter) {
        if (original == null)
            original = filter;

        // supports it as a group -- no need to check the type
        if (!fcs.supports(filter)) {
            postStack.push(filter);
            return;
        }

        int i = postStack.size();
        Expression leftValue = filter.getExpression1();
        Expression rightValue = filter.getExpression2();
        if (leftValue == null || rightValue == null) {
            postStack.push(filter);
            return;
        }

        leftValue.accept(this, null);

        if (i < postStack.size()) {
            postStack.pop();
            postStack.push(filter);

            return;
        }

        rightValue.accept(this, null);

        if (i < postStack.size()) {
            preStack.pop(); // left
            postStack.pop();
            postStack.push(filter);

            return;
        }

        preStack.pop(); // left side
        preStack.pop(); // right side
        preStack.push(filter);

    }

    public Object visit(BBOX filter, Object notUsed) {
        if (!fcs.supports(filter)) {
            postStack.push(filter);
        } else {
            preStack.push(filter);
        }
        return null;
    }

    public Object visit(Beyond filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Contains filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Crosses filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Disjoint filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(DWithin filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Equals filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Intersects filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Overlaps filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Touches filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    public Object visit(Within filter, Object notUsed) {
        visitBinarySpatialOperator(filter);
        return null;
    }

    private void visitBinarySpatialOperator(BinarySpatialOperator filter) {
        if (original == null)
            original = filter;

        Class[] spatialOps = new Class[] { Beyond.class, Contains.class, Crosses.class,
                Disjoint.class, DWithin.class, Equals.class, Intersects.class, Overlaps.class,
                Touches.class, Within.class };

        for (int i = 0; i < spatialOps.length; i++) {
            if (spatialOps[i].isAssignableFrom(filter.getClass())) {
                // if (!fcs.supports(spatialOps[i])) {
                if (!fcs.supports(filter)) {
                    postStack.push(filter);
                    return;
                } else {
                    // fcs supports this filter, no need to check the rest
                    break;
                }
            }
        }

        // TODO check against tranasaction ?

        int i = postStack.size();

        Expression leftGeometry, rightGeometry;
        leftGeometry = ((BinarySpatialOperator) filter).getExpression1();
        rightGeometry = ((BinarySpatialOperator) filter).getExpression2();

        if (leftGeometry == null || rightGeometry == null) {
            postStack.push(filter);
            return;
        }
        leftGeometry.accept(this, null);

        if (i < postStack.size()) {
            postStack.pop();
            postStack.push(filter);

            return;
        }

        rightGeometry.accept(this, null);

        if (i < postStack.size()) {
            preStack.pop(); // left
            postStack.pop();
            postStack.push(filter);

            return;
        }

        preStack.pop(); // left side
        preStack.pop(); // right side
        preStack.push(filter);
    }

    public Object visit(PropertyIsLike filter, Object notUsed) {
        if (original == null)
            original = filter;

        // if (!fcs.supports(PropertyIsLike.class)) {
        if (!fcs.supports(filter)) {
            postStack.push(filter);

            return null;
        }

        int i = postStack.size();
        filter.getExpression().accept(this, null);

        if (i < postStack.size()) {
            postStack.pop();
            postStack.push(filter);

            return null;
        }

        preStack.pop(); // value
        preStack.push(filter);
        return null;
    }

    public Object visit(And filter, Object notUsed) {
        visitLogicOperator(filter);
        return null;
    }

    public Object visit(Not filter, Object notUsed) {
        visitLogicOperator(filter);
        return null;
    }

    public Object visit(Or filter, Object notUsed) {
        visitLogicOperator(filter);
        return null;
    }

    private void visitLogicOperator(Filter filter) {
        if (original == null)
            original = filter;

        if (!fcs.supports(filter)) {
            postStack.push(filter);
            return;
        }

        int i = postStack.size();
        int j = preStack.size();
        if (filter instanceof Not) {

            if (((Not) filter).getFilter() != null) {
                Filter next = ((Not) filter).getFilter();
                next.accept(this, null);

                if (i < postStack.size()) {
                    // since and can split filter into both pre and post parts
                    // the parts have to be combined since ~(A^B) == ~A | ~B
                    // combining is easy since filter==combined result however both post and pre
                    // stacks
                    // must be cleared since both may have components of the filter
                    popToSize(postStack, i);
                    popToSize(preStack, j);
                    postStack.push(filter);
                } else {
                    popToSize(preStack, j);
                    preStack.push(filter);
                }
            }
        } else {
            if (filter instanceof Or) {
                Filter orReplacement;

                try {
                    orReplacement = translateOr((Or) filter);
                    orReplacement.accept(this, null);
                } catch (IllegalFilterException e) {
                    popToSize(preStack, j);
                    postStack.push(filter);
                    return;
                }
                if (postStack.size() > i) {
                    popToSize(postStack, i);
                    postStack.push(filter);

                    return;
                }

                preStack.pop();
                preStack.push(filter);
            } else {
                // it's an AND
                Iterator it = ((And) filter).getChildren().iterator();

                while (it.hasNext()) {
                    Filter next = (Filter) it.next();
                    next.accept(this, null);
                }

                // combine the unsupported and add to the top
                if (i < postStack.size()) {
                    if (filter instanceof And) {
                        Filter f = (Filter) postStack.pop();

                        while (postStack.size() > i)
                            f = ff.and(f, (Filter) postStack.pop());

                        postStack.push(f);

                        if (j < preStack.size()) {
                            f = (Filter) preStack.pop();

                            while (preStack.size() > j)
                                f = ff.and(f, (Filter) preStack.pop());
                            preStack.push(f);
                        }
                    } else {
                        LOGGER.warning("LogicFilter found which is not 'and, or, not");

                        popToSize(postStack, i);
                        popToSize(preStack, j);

                        postStack.push(filter);
                    }
                } else {
                    popToSize(preStack, j);
                    preStack.push(filter);
                }
            }
        }
    }

    private void popToSize(Stack stack, int j) {
        while (j < stack.size()) {
            stack.pop();
        }
    }

    public Object visitNullFilter(Object notUsed) {
        return null;
    }

    public Object visit(IncludeFilter filter, Object notUsed) {
        return null;
    }

    public Object visit(ExcludeFilter filter, Object notUsed) {
        if (fcs.supports(Filter.EXCLUDE)) {
            preStack.push(filter);
        } else {
            postStack.push(filter);
        }
        return null;
    }

    public Object visit(PropertyIsNull filter, Object notUsed) {
        if (original == null)
            original = filter;

        if (!fcs.supports(filter)) {
            postStack.push(filter);

            return null;
        }

        int i = postStack.size();
        ((PropertyIsNull) filter).getExpression().accept(this, null);

        if (i < postStack.size()) {
            postStack.pop();
            postStack.push(filter);
        }

        preStack.pop(); // null
        preStack.push(filter);

        return null;
    }

    public Object visit(Id filter, Object notUsed) {
        if (original == null)
            original = filter;

        // figure out how to check that this is top level.
        // otherwise this is fine
        if (!postStack.isEmpty()) {
            postStack.push(filter);
        }
        preStack.push(filter);
        return null;
    }

    public Object visit(PropertyName expression, Object notUsed) {
        // JD: use an expression to get at the attribute type intead of accessing directly
        if (parent != null && expression.evaluate(parent) == null) {
            throw new IllegalArgumentException("Property '" + expression.getPropertyName()
                    + "' could not be found in " + parent.getName());
        }
        if (transactionAccessor != null) {
            Filter updateFilter = (Filter) transactionAccessor.getUpdateFilter(expression
                    .getPropertyName());
            if (updateFilter != null) {
                changedStack.add(updateFilter);
                preStack.push(updateFilter);
            } else
                preStack.push(expression);
        } else {
            preStack.push(expression);
        }
        return null;
    }

    public Object visit(Literal expression, Object notUsed) {
        if (expression.getValue() == null) {
            postStack.push(expression);
        }
        preStack.push(expression);
        return null;
    }

    public Object visit(Add filter, Object notUsed) {
        visitMathExpression(filter);
        return null;
    }

    public Object visit(Divide filter, Object notUsed) {
        visitMathExpression(filter);
        return null;
    }

    public Object visit(Multiply filter, Object notUsed) {
        visitMathExpression(filter);
        return null;
    }

    public Object visit(Subtract filter, Object notUsed) {
        visitMathExpression(filter);
        return null;
    }

    private void visitMathExpression(BinaryExpression expression) {
        // if (!fcs.supports(Add.class) && !fcs.supports(Subtract.class)
        // && !fcs.supports(Multiply.class) && !fcs.supports(Divide.class)) {
        if (!fcs.fullySupports(expression)) {
            postStack.push(expression);
            return;
        }

        int i = postStack.size();
        Expression leftValue = expression.getExpression1();
        Expression rightValue = expression.getExpression2();
        if (leftValue == null || rightValue == null) {
            postStack.push(expression);
            return;
        }
        leftValue.accept(this, null);

        if (i < postStack.size()) {
            postStack.pop();
            postStack.push(expression);

            return;
        }

        rightValue.accept(this, null);

        if (i < postStack.size()) {
            preStack.pop(); // left
            postStack.pop();
            postStack.push(expression);

            return;
        }

        preStack.pop(); // left side
        preStack.pop(); // right side
        preStack.push(expression);
    }

    /**
     * 
     * @see org.geotools.filter.FilterVisitor#visit(org.geotools.filter.FunctionExpression)
     */
    public Object visit(Function expression, Object notUsed) {
        if (!fcs.fullySupports(expression)) {
            postStack.push(expression);
            return null;
        }

        if (expression.getName() == null) {
            postStack.push(expression);
            return null;
        }

        int i = postStack.size();
        int j = preStack.size();

        for (int k = 0; k < expression.getParameters().size(); k++) {
            ((Expression) expression.getParameters().get(i)).accept(this, null);

            if (i < postStack.size()) {
                while (j < preStack.size())
                    preStack.pop();
                postStack.pop();
                postStack.push(expression);

                return null;
            }
        }
        while (j < preStack.size())
            preStack.pop();
        preStack.push(expression);
        return null;
    }

    public Object visit(NilExpression nilExpression, Object notUsed) {
        postStack.push(nilExpression);
        return null;
    }

    private Filter translateOr(Or filter) throws IllegalFilterException {
        if (!(filter instanceof Or)) {
            return filter;
        }

        // a|b == ~~(a|b) negative introduction
        // ~(a|b) == (~a + ~b) modus ponens
        // ~~(a|b) == ~(~a + ~b) substitution
        // a|b == ~(~a + ~b) negative simpilification
        Iterator i = filter.getChildren().iterator();
        List translated = new ArrayList();

        while (i.hasNext()) {
            Filter f = (Filter) i.next();

            if (f instanceof Not) {
                // simplify it
                Not logic = (Not) f;
                Filter next = logic.getFilter();
                translated.add(next);
            } else {
                translated.add(ff.not(f));
            }
        }

        Filter and = ff.and(translated);
        return ff.not(and);
    }
}
