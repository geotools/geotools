/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.ExplicitClassifier;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.style.SemanticType;

/**
 * Generates a style/featureTypeStyle using ColorBrewer. <br>
 * WARNING: this is unstable and subject to radical change.
 *
 * @author Cory Horner, Refractions Research Inc.
 */
public class StyleGenerator {
    private static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StyleGenerator.class);
    public static final int ELSEMODE_IGNORE = 0;
    public static final int ELSEMODE_INCLUDEASMIN = 1;
    public static final int ELSEMODE_INCLUDEASMAX = 2;
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private static StyleBuilder sb = new StyleBuilder(sf, ff);

    protected StyleGenerator() {}

    /**
     * Obtains the colour for the indexed rule. If an else rule is also to be created from the
     * colour palette, the appropriate offset is applied.
     */
    private static Color getColor(int elseMode, Color[] colors, int index) {
        if (elseMode == ELSEMODE_IGNORE) {
            return colors[index];
        } else if (elseMode == ELSEMODE_INCLUDEASMIN) {
            return colors[index + 1];
        } else if (elseMode == ELSEMODE_INCLUDEASMAX) {
            return colors[index];
        } else {
            return null;
        }
    }

    private static Color getElseColor(int elseMode, Color[] colors) {
        if (elseMode == ELSEMODE_INCLUDEASMIN) {
            return colors[0];
        } else if (elseMode == ELSEMODE_INCLUDEASMAX) {
            return colors[colors.length - 1];
        } else {
            return null;
        }
    }

    /**
     * Merges a classifier, array of colors and other data into a FeatureTypeStyle object. Yes, this
     * constructor is insane and likely to change very soon.
     *
     * @param typeId semantic type identifier, which will be prefixed with "colorbrewer:"
     */
    public static FeatureTypeStyle createFeatureTypeStyle(
            Classifier classifier,
            Expression expression,
            Color[] colors,
            String typeId,
            GeometryDescriptor geometryAttrType,
            int elseMode,
            double opacity,
            Stroke defaultStroke)
            throws IllegalFilterException {
        // init nulls
        if (defaultStroke == null) {
            defaultStroke = sb.createStroke();
        }

        // answer goes here
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();

        // update the number of classes

        // numeric
        if (classifier instanceof RangedClassifier) {
            RangedClassifier ranged = (RangedClassifier) classifier;

            Object localMin = null;
            Object localMax = null;

            // for each class
            for (int i = 0; i < ranged.getSize(); i++) {
                // obtain min/max values
                localMin = ranged.getMin(i);
                localMax = ranged.getMax(i);

                Rule rule =
                        createRuleRanged(
                                ranged,
                                expression,
                                localMin,
                                localMax,
                                geometryAttrType,
                                i,
                                elseMode,
                                colors,
                                opacity,
                                defaultStroke);
                fts.rules().add(rule);
            }
        } else if (classifier instanceof ExplicitClassifier) {
            ExplicitClassifier explicit = (ExplicitClassifier) classifier;

            // for each class
            for (int i = 0; i < explicit.getSize(); i++) {
                Set value = (Set) explicit.getValues(i);
                Rule rule =
                        createRuleExplicit(
                                explicit,
                                expression,
                                value,
                                geometryAttrType,
                                i,
                                elseMode,
                                colors,
                                opacity,
                                defaultStroke);
                fts.rules().add(rule);
            }
        } else {
            LOGGER.log(Level.SEVERE, "Error: no handler for this Classifier type");
        }

        // add an else rule to capture any missing features?
        if (elseMode != ELSEMODE_IGNORE) {
            Symbolizer symb =
                    createSymbolizer(
                            geometryAttrType,
                            getElseColor(elseMode, colors),
                            opacity,
                            defaultStroke);
            Rule elseRule = sb.createRule(symb);
            elseRule.setElseFilter(true);
            elseRule.getDescription().setTitle("Else");
            elseRule.setName("else");
            fts.rules().add(elseRule);
        }

        // our syntax will be: ColorBrewer:id
        Set<SemanticType> semanticTypes = fts.semanticTypeIdentifiers();
        semanticTypes.add(SemanticType.valueOf("generic:geometry"));
        semanticTypes.add(SemanticType.valueOf("colorbrewer:" + typeId));

        return fts;
    }

    /**
     * Creates a symbolizer for the given geometry
     *
     * @param defaultStroke stroke used for borders
     */
    private static Symbolizer createSymbolizer(
            GeometryDescriptor geometryAttrType,
            Color color,
            double opacity,
            Stroke defaultStroke) {
        Symbolizer symb;

        if (defaultStroke == null) {
            defaultStroke = sb.createStroke(color, 1, opacity);
        }

        if ((geometryAttrType.getType().getBinding() == MultiPolygon.class)
                || (geometryAttrType.getType().getBinding() == Polygon.class)) {
            Fill fill = sb.createFill(color, opacity);
            symb = sb.createPolygonSymbolizer(defaultStroke, fill);
        } else if (geometryAttrType.getType().getBinding() == LineString.class) {
            symb = sb.createLineSymbolizer(color);
        } else if ((geometryAttrType.getType().getBinding() == MultiPoint.class)
                || (geometryAttrType.getType().getBinding() == Point.class)) {
            Fill fill = sb.createFill(color, opacity);
            Mark square = sb.createMark(StyleBuilder.MARK_SQUARE, fill, defaultStroke);
            Graphic graphic = sb.createGraphic(null, square, null); // , 1, 4, 0);
            symb = sb.createPointSymbolizer(graphic);

            // TODO: handle Text and Raster
        } else {
            // we don't know what the heck you are, *snip snip* you're a line.
            symb = sb.createLineSymbolizer(color);
        }

        return symb;
    }

    /**
     * Truncates an unneeded trailing decimal zero (1.0 --> 1) by converting to an Integer object.
     *
     * @return Integer(value) if applicable
     */
    private static Object chopInteger(Object value) {
        if ((value instanceof Number) && (value.toString().endsWith(".0"))) {
            return Integer.valueOf(((Number) value).intValue());
        } else {
            return value;
        }
    }

    /** Generates a quick name for each rule with a leading zero. */
    private static String getRuleName(int count) {
        String strVal = Integer.valueOf(count).toString();

        if (strVal.length() == 1) {
            return "rule0" + strVal;
        } else {
            return "rule" + strVal;
        }
    }

    private static Rule createRuleRanged(
            RangedClassifier classifier,
            Expression expression,
            Object localMin,
            Object localMax,
            GeometryDescriptor geometryAttrType,
            int i,
            int elseMode,
            Color[] colors,
            double opacity,
            Stroke defaultStroke)
            throws IllegalFilterException {
        // 1.0 --> 1
        // (this makes our styleExpressions more readable. Note that the
        // filter always converts to double, so it doesn't care what we
        // do).
        localMin = chopInteger(localMin);
        localMax = chopInteger(localMax);

        // generate a title
        String title = classifier.getTitle(i);

        // construct filters
        Filter filter = null;

        if (localMin == localMax) {
            // build filter: =
            filter = ff.equals(expression, ff.literal(localMax));
        } else {
            // build filter: [min <= x] AND [x < max]
            Filter lowBoundFilter = null;
            Filter hiBoundFilter = null;

            if (localMin != null) {
                lowBoundFilter = ff.greaterOrEqual(expression, ff.literal(localMin));
            }
            if (localMax != null) {
                // if this is the global maximum, include the max value
                if (i == (classifier.getSize() - 1)) {
                    hiBoundFilter = ff.lessOrEqual(expression, ff.literal(localMax));
                } else {
                    hiBoundFilter = ff.less(expression, ff.literal(localMax));
                }
            }

            if ((localMin != null) && (localMax != null)) {
                filter = ff.and(lowBoundFilter, hiBoundFilter);
            } else if ((localMin == null) && (localMax != null)) {
                filter = hiBoundFilter;
            } else if ((localMin != null) && (localMax == null)) {
                filter = lowBoundFilter;
            }
        }

        // create a symbolizer
        Symbolizer symb =
                createSymbolizer(
                        geometryAttrType, getColor(elseMode, colors, i), opacity, defaultStroke);

        // create a rule
        Rule rule = sb.createRule(symb);
        rule.setFilter(filter);
        rule.getDescription().setTitle(title);
        rule.setName(getRuleName(i + 1));

        return rule;
    }

    private static Rule createRuleExplicit(
            ExplicitClassifier explicit,
            Expression expression,
            Set value,
            GeometryDescriptor geometryAttrType,
            int i,
            int elseMode,
            Color[] colors,
            double opacity,
            Stroke defaultStroke) {
        // create a sub filter for each unique value, and merge them
        // into the logic filter
        Object[] items = value.toArray();
        Arrays.sort(items);

        String title = "";
        List<Filter> filters = new ArrayList<Filter>();
        for (int item = 0; item < items.length; item++) {

            Filter filter;
            if (items[item] == null) {
                filter = ff.isNull(expression);
            } else {
                filter = ff.equals(expression, ff.literal(items[item]));
            }

            // add to the title
            if (items[item] == null) {
                title += "NULL";
            } else {
                title += items[item].toString();
            }

            if ((item + 1) != items.length) {
                title += ", ";
            }

            filters.add(filter);
        }

        // create the symbolizer
        Symbolizer symb =
                createSymbolizer(
                        geometryAttrType, getColor(elseMode, colors, i), opacity, defaultStroke);

        // create the rule
        Rule rule = sb.createRule(symb);

        if (filters.size() == 1) {
            rule.setFilter(filters.get(0));
        } else if (filters.size() > 1) {
            rule.setFilter(ff.or(filters));
        }

        rule.getDescription().setTitle(title);
        rule.setName(getRuleName(i + 1));

        return rule;
    }
    /** Used to update an existing style based on the provided input. */
    public static void modifyFTS(FeatureTypeStyle fts, int ruleIndex, String styleExpression)
            throws IllegalFilterException {
        Rule thisRule = fts.rules().get(ruleIndex);
        Filter filter = thisRule.getFilter();

        if (filter instanceof And) { // ranged expression
            // figure out the appropriate values

            String[] newValue = styleExpression.split("\\.\\."); // $NON-NLS-1$

            if (newValue.length != 2) {
                throw new IllegalArgumentException(
                        "StyleExpression has incorrect syntax; min..max expected.");
            }

            List<Filter> children = ((BinaryLogicOperator) filter).getChildren();

            if (children.size() > 2) {
                throw new IllegalArgumentException(
                        "This method currently only supports logical filters with exactly 2 children.");
            }

            // we're expecting 2 compare subfilters
            PropertyIsGreaterThanOrEqualTo filter1 =
                    (PropertyIsGreaterThanOrEqualTo) children.get(0);
            BinaryComparisonOperator filter2 = (BinaryComparisonOperator) children.get(1);

            // filter1 should be 1 <= x and filter2 should be x <(=) 5
            if (!(filter1.getExpression2().equals(filter2.getExpression1()))) {
                throw new IllegalArgumentException(
                        "Subfilters or subExpressions in incorrect order");
            }

            if (!filter1.getExpression1().toString().equals(newValue[0])) {
                // lower bound value has changed, update
                filter1 = ff.greaterOrEqual(filter1.getExpression1(), ff.literal(newValue[0]));
            }

            if (!filter2.getExpression2().toString().equals(newValue[1])) {
                // upper bound value has changed, update
                if (filter2 instanceof PropertyIsLessThan) {
                    filter2 = ff.less(filter1.getExpression1(), ff.literal(newValue[1]));
                } else if (filter2 instanceof PropertyIsLessThanOrEqualTo) {
                    filter2 = ff.lessOrEqual(filter1.getExpression1(), ff.literal(newValue[1]));
                } else {
                    throw new IllegalArgumentException(
                            "Filter 2 in the comparison is not less or less or equal??");
                }
            }

            // style events don't handle filters yet, so fire the change event for filter
            thisRule.setFilter(filter);

            // TODO: adjust the previous and next filters (uses isFirst, isLast)
        } else if (filter instanceof Or || filter instanceof PropertyIsEqualTo) {
            // explicit expression obtain the expression containing the attribute

            Expression attrExpression;

            if (filter instanceof Or) {
                attrExpression =
                        ((BinaryComparisonOperator) ((Or) filter).getChildren().get(0))
                                .getExpression1();
            } else { // COMPARE_EQUALS (simple explicit expression)
                attrExpression = ((PropertyIsEqualTo) filter).getExpression1();
            }

            // recreate the filter with the new values
            thisRule.setFilter(toExplicitFilter(styleExpression, attrExpression));

            // TODO: remove duplicate values from other filters
        } else {
            throw new IllegalArgumentException("Unrecognized filter type.");
        }
    }

    public static String toStyleExpression(Filter filter) {
        if (filter instanceof And) { // looks like a ranged filter
            return toRangedStyleExpression((And) filter);
        } else { // it's probably a filter with explicitly defined values
            return toExplicitStyleExpression(filter);
        }
    }

    public static String[] toStyleExpression(Filter[] filter) {
        String[] styleExpression = new String[filter.length];

        for (int i = 0; i < filter.length; i++) {
            styleExpression[i] = toStyleExpression(filter[i]);
        }

        return styleExpression;
    }

    /**
     * Converts an array of styleExpressions and attributes into Filters
     *
     * <p><code>styleExpression[0] = "1..5";</code><br>
     * <code>styleExpression[1] = "5..10";</code><br>
     * <code>styleExpression[2] = "11, -13";</code><br>
     * <code>---></code><br>
     * <code>filter[0] = [[1 <= attr] AND [attr < 5]]</code><br>
     * <code>filter[1] = [[6 <= attr] AND [attr <= 10]]</code><br>
     * <code>filter[2] = [[attr = 11] OR [attr = -13]]</code>
     *
     * @param styleExpression strings of ranged expressions "lowValue..highValue" or explicit values
     *     "value1, value2"
     * @return an array with all the filters
     */
    public static Filter[] toFilter(
            String[] styleExpression, SimpleFeatureType[] featureType, String[] attributeTypeName)
            throws IllegalFilterException {
        Filter[] filter = new Filter[styleExpression.length];

        // prepare the styleExpressions (fix out if they are ranged, and if so
        // their min and max values too
        boolean[] isRangedExpr = new boolean[styleExpression.length];
        List<String> min = new ArrayList<String>();
        String[] max = new String[styleExpression.length];

        for (int i = 0; i < styleExpression.length; i++) {
            if (isRanged(styleExpression[i])) {
                isRangedExpr[i] = true;

                String[] exprPart = styleExpression[i].split("\\.\\."); // $NON-NLS-1$
                min.add(exprPart[0]);
                max[i] = exprPart[1];
            } else {
                isRangedExpr[i] = false;
            }
        }

        // create each filter
        for (int i = 0; i < styleExpression.length; i++) {
            // is it ranged or specific?
            if (isRangedExpr[i]) {
                boolean upperBoundClosed = true;

                // check for lower bounds of the same value as the current upper
                // bound
                if (min.contains(max[i])) {
                    upperBoundClosed = false;
                }

                filter[i] =
                        toRangedFilter(
                                styleExpression[i],
                                featureType[i],
                                attributeTypeName[i],
                                upperBoundClosed);
            } else { // specific
                filter[i] =
                        toExplicitFilter(styleExpression[i], featureType[i], attributeTypeName[i]);
            }
        }

        return filter;
    }

    /**
     * Creates a filter for a range of values.
     *
     * <p>Examples:<br>
     * "1..5", closed=true --> [[1 <= attr] AND [attr <= 5]]<br>
     * "1..10", closed=false --> [[1 <= attr] AND [attr < 10]] "..10, closed=true --> [attr <= 10]
     *
     * @param styleExpression the ranged style expression (minValue..maxValue)
     * @param featureType the featureType
     * @param attributeTypeName the attributeTypeName whose values correspond to
     * @param upperBoundClosed does the upper bound include the max value? (true: <=, false: <)
     * @return a filter
     */
    public static Filter toRangedFilter(
            String styleExpression,
            SimpleFeatureType featureType,
            String attributeTypeName,
            boolean upperBoundClosed)
            throws IllegalFilterException {
        PropertyName attrib = ff.property(attributeTypeName);
        String[] strs = styleExpression.split("\\.\\."); // $NON-NLS-1$

        if (strs.length != 2) {
            throw new IllegalArgumentException(
                    "A ranged filter could not be created from the styleExpression given.");
        }

        Literal localMin = ff.literal(strs[0]);
        Literal localMax = ff.literal(strs[1]);
        Filter lowerBound = ff.lessOrEqual(localMin, localMax);
        Filter upperBound;
        if (upperBoundClosed) {
            upperBound = ff.lessOrEqual(attrib, localMax);
        } else {
            upperBound = ff.less(attrib, localMax);
        }

        return ff.and(lowerBound, upperBound);
    }

    /**
     * Converts a filter into a styleExpression with ranged values.
     *
     * <p>Example:<br>
     * <code>[[1 <= attr] AND [attr < 5]] --> "1..5"</code>
     *
     * @param filter A LOGIC_AND filter containing 2 CompareFilters or a single CompareFilter.
     * @return a styleExpression of the syntax "min..max"
     */
    private static String toRangedStyleExpression(Filter filter) {
        if (filter instanceof BinaryLogicOperator) {
            BinaryLogicOperator lFilter = (BinaryLogicOperator) filter;

            if (!(filter instanceof And)) {
                throw new IllegalArgumentException(
                        "Only logic filters constructed using the LOGIC_AND filterType are currently supported by this method.");
            }

            List<Filter> children = lFilter.getChildren();

            // we're expecting 2 subfilters
            Filter filter1 = children.get(0);
            Filter filter2 = children.get(1);

            if (children.size() > 2) {
                throw new IllegalArgumentException(
                        "This method currently only supports logical filters with exactly 2 children.");
            }

            if (!(filter1 instanceof BinaryComparisonOperator)
                    || !(filter2 instanceof BinaryComparisonOperator)) {
                throw new IllegalArgumentException(
                        "Only compare filters as logical filter children are currently supported by this method.");
            }

            // find min and max values
            Expression min1;
            Expression min2;
            Expression max1;
            Expression max2;

            if (filter1 instanceof PropertyIsLessThanOrEqualTo
                    || filter1 instanceof PropertyIsLessThan) {
                min1 = ((BinaryComparisonOperator) filter1).getExpression1();
                max1 = ((BinaryComparisonOperator) filter1).getExpression2();
            } else if (filter1 instanceof PropertyIsGreaterThanOrEqualTo
                    || filter1 instanceof PropertyIsGreaterThan) {
                min1 = ((BinaryComparisonOperator) filter1).getExpression2();
                max1 = ((BinaryComparisonOperator) filter1).getExpression1();
            } else {
                throw new IllegalArgumentException("Unsupported FilterType");
            }

            if (filter2 instanceof PropertyIsLessThanOrEqualTo
                    || filter2 instanceof PropertyIsLessThan) {
                min2 = ((BinaryComparisonOperator) filter2).getExpression1();
                max2 = ((BinaryComparisonOperator) filter2).getExpression2();
            } else if (filter2 instanceof PropertyIsGreaterThanOrEqualTo
                    || filter2 instanceof PropertyIsGreaterThan) {
                min2 = ((BinaryComparisonOperator) filter2).getExpression2();
                max2 = ((BinaryComparisonOperator) filter2).getExpression1();
            } else {
                throw new IllegalArgumentException("Unsupported FilterType");
            }

            // look for 2 equal expressions
            if (max1.equals(min2)) {
                return min1.toString() + ".." + max2.toString();
            } else if (max2.equals(min1)) {
                return min2.toString() + ".." + max1.toString();
            } else {
                throw new IllegalArgumentException(
                        "Couldn't find the expected arrangement of Expressions");
            }
        }

        throw new UnsupportedOperationException("Don't know how to handle this filter");
    }

    /** Determines if a string is an instance of a ranged expression or unique values. */
    public static boolean isRanged(String styleExpression) {
        return styleExpression.matches(".+\\.{2}.+");
    }

    /**
     * Creates a filter with each value explicitly defined.
     *
     * <p>Examples:<br>
     * "LIB" --> [PARTY = LIB]<br>
     * "LIB, NDP" --> [[PARTY = LIB] OR [PARTY = NDP]]
     *
     * @param styleExpression the list of attribute values, separated by commas (and optional
     *     spaces)
     * @param attributeTypeName A Sting with the attributeTypeName whose values correspond to
     * @return a filter
     */
    public static Filter toExplicitFilter(
            String styleExpression, SimpleFeatureType featureType, String attributeTypeName)
            throws IllegalFilterException {
        // eliminate spaces after commas
        String expr = styleExpression.replaceAll(",\\s+", ","); // $NON-NLS-1$//$NON-NLS-2$
        String[] attribValue = expr.split(","); // $NON-NLS-1$

        // create the first filter
        PropertyName attribExpr = ff.property(attributeTypeName);
        PropertyIsEqualTo cFilter = ff.equals(attribExpr, ff.literal(attribValue[0]));

        if (attribValue.length == 1) {
            return cFilter;
        }

        // more than one value exists, so wrap them inside a logical OR
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(cFilter);
        for (int i = 1; i < attribValue.length; i++) {
            cFilter = ff.equals(attribExpr, ff.literal(attribValue[i]));
            filters.add(cFilter);
        }

        return ff.or(filters);
    }

    /**
     * Creates a filter with each value explicitly defined.
     *
     * <p>Examples:<br>
     * "LIB" --> [PARTY = LIB]<br>
     * "LIB, NDP" --> [[PARTY = LIB] OR [PARTY = NDP]]
     *
     * @param styleExpression the list of attribute values, separated by commas (and optional
     *     spaces)
     * @param attribExpr an Expression to compare each value with (simple case =
     *     attributeExpression)
     * @return a filter
     */
    public static Filter toExplicitFilter(String styleExpression, Expression attribExpr)
            throws IllegalFilterException {
        // eliminate spaces after commas
        String expr = styleExpression.replaceAll(",\\s+", ","); // $NON-NLS-1$//$NON-NLS-2$
        String[] attribValue = expr.split(","); // $NON-NLS-1$

        // create the first filter
        PropertyIsEqualTo cFilter = ff.equals(attribExpr, ff.literal(attribValue[0]));

        if (attribValue.length == 1) {
            return cFilter;
        }

        // more than one value exists, so wrap them inside a logical OR
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(cFilter);
        for (int i = 1; i < attribValue.length; i++) {
            cFilter = ff.equals(attribExpr, ff.literal(attribValue[i]));
            filters.add(cFilter);
        }

        return ff.or(filters);
    }

    /**
     * Converts a filter into a styleExpression with explicitly defined values.
     *
     * <p>Example:<br>
     * <code>[[attr = 49] OR [attr = 92]] --> "49, 92"</code>
     */
    private static String toExplicitStyleExpression(Filter filter) {
        String styleExpression = "";

        if (filter instanceof PropertyIsEqualTo) {
            // figure out which side is the attributeExpression, and which side
            // is the LiteralExpression
            PropertyIsEqualTo compareFilter = (PropertyIsEqualTo) filter;
            Expression leftExpression = compareFilter.getExpression1();
            Expression rightExpression = compareFilter.getExpression2();

            if ((leftExpression instanceof PropertyName) && (rightExpression instanceof Literal)) {
                styleExpression = rightExpression.toString();
            } else if ((leftExpression instanceof Literal)
                    && (rightExpression instanceof PropertyName)) {
                styleExpression = leftExpression.toString();
            } else {
                throw new IllegalArgumentException(
                        "Could not extract an Explicit Style Expression from the CompareFilter");
            }
        } else if (filter instanceof Or) {
            // descend into the child elements of this filter
            Or parentFilter = (Or) filter;
            Iterator iterator = parentFilter.getChildren().iterator();

            while (iterator.hasNext()) {
                // recursive call
                styleExpression += toExplicitStyleExpression((Filter) iterator.next());

                if (iterator.hasNext()) {
                    styleExpression += ", ";
                }
            }
        }

        return styleExpression;
    }
}
