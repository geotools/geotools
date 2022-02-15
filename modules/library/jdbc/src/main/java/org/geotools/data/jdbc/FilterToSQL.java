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
 */
package org.geotools.data.jdbc;

import static org.geotools.filter.capability.FunctionNameImpl.parameter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Length;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.FunctionImpl;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.function.InFunction;
import org.geotools.filter.spatial.BBOXImpl;
import org.geotools.jdbc.EnumMapper;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JoinId;
import org.geotools.jdbc.JoinPropertyName;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.referencing.CRS;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.NativeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
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
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.filter.temporal.After;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Before;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;
import org.opengis.parameter.Parameter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import si.uom.SI;

/**
 * Encodes a filter into a SQL WHERE statement. It should hopefully be generic enough that any SQL
 * database will work with it. This generic SQL encoder should eventually be able to encode all
 * filters except Geometry Filters. This is because the OGC's SFS for SQL document specifies two
 * ways of doing SQL databases, one with native geometry types and one without. To implement an
 * encoder for one of the two types simply subclass off of this encoder and put in the proper
 * GeometryFilter visit method. Then add the filter types supported to the capabilities by
 * overriding the {{@link #createFilterCapabilities()} method.
 *
 * <p>This version was ported from the original to support org.opengis.filter type Filters.
 *
 * @author originally by Chris Holmes, TOPP
 * @author ported by Saul Farber, MassGIS
 * @task REVISIT: need to figure out exceptions, we're currently eating io errors, which is bad.
 *     Probably need a generic visitor exception.
 */
/*
 * TODO: Use the new FilterCapabilities.  This may fall out of using the new
 * PrePostFilterSplitter code.
 *
 * TODO: Use the new Geometry classes from org.opengis.  Not sure
 * when this will be required, but it's on the horizon here.
 *
 * Non Javadoc comments:
 *
 * Note that the old method allowed us to write WAY fewer methods, as we didn't
 * need to cover all the cases with exlpicit methods (as the new
 * org.opengis.filter.FilterVisitor and ExpressionVisitor methods require
 * us to do).
 *
 * The code is split into methods to support the FilterVisitor interface first
 * then the ExpressionVisitor methods second.
 *
 */
public class FilterToSQL implements FilterVisitor, ExpressionVisitor {

    /** Conversion factor from common units to meter */
    private static final Map<String, Double> UNITS_MAP =
            new HashMap<String, Double>() {
                {
                    put("kilometers", 1000.0);
                    put("kilometer", 1000.0);
                    put("km", 1000.0);
                    put("m", 1.0);
                    put("meter", 1.0);
                    put("mm", 0.001);
                    put("millimeter", 0.001);
                    put("mi", 1609.344);
                    put("miles", 1609.344);
                    put("nm", 1852d);
                    put("feet", 0.3048);
                    put("ft", 0.3048);
                    put("in", 0.0254);
                }
            };

    /** error message for exceptions */
    protected static final String IO_ERROR = "io problem writing filter";

    /** filter factory */
    protected static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(null);

    /** The filter types that this class can encode */
    protected FilterCapabilities capabilities = null;

    /** Standard java logger */
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FilterToSQL.class);

    /** Character used to escape database schema, table and column names */
    protected String sqlNameEscape = "";

    /** where to write the constructed string from visiting the filters. */
    protected Writer out;

    /** The primary key corresponding to the table the filter is being encoded against. */
    protected PrimaryKey primaryKey;

    /** The schema that contains the table the filter being encoded against. */
    protected String databaseSchema;

    /** the schmema the encoder will be used to be encode sql for */
    protected SimpleFeatureType featureType;

    /** flag which indicates that the encoder is currently encoding a function */
    protected boolean encodingFunction = false;

    /** the geometry descriptor corresponding to the current binary spatial filter being encoded */
    protected GeometryDescriptor currentGeometry;

    /** the srid corresponding to the current binary spatial filter being encoded */
    protected Integer currentSRID;

    /** The dimension corresponding to the current binary spatial filter being encoded */
    protected Integer currentDimension;

    /** inline flag, controlling whether "WHERE" will prefix the SQL encoded filter */
    protected boolean inline = false;

    /** Whether the encoder should try to encode "in" function into a SQL IN operator */
    protected boolean inEncodingEnabled = true;

    /** Default constructor */
    public FilterToSQL() {}

    public FilterToSQL(Writer out) {
        this.out = out;
    }

    /** Sets the writer the encoder will write to. */
    public void setWriter(Writer out) {
        this.out = out;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    /**
     * Whether "in" like functions are encoded in SQL or not
     *
     * @return a boolean value, true if the in linke functions are encoded
     */
    public boolean isInEncodingEnabled() {
        return inEncodingEnabled;
    }

    /**
     * Makes the encoder translate in like functions into SQL (see also {@link
     * InFunction#isInFunction(Expression)}
     *
     * @param inEncodingEnabled the new flag controlling in encoding
     */
    public void setInEncodingEnabled(boolean inEncodingEnabled) {
        this.inEncodingEnabled = inEncodingEnabled;
    }

    /**
     * Performs the encoding, sends the encoded sql to the writer passed in.
     *
     * @param filter the Filter to be encoded.
     * @throws FilterToSQLException If filter type not supported, or if there were io problems.
     */
    public void encode(Filter filter) throws FilterToSQLException {
        if (out == null) throw new FilterToSQLException("Can't encode to a null writer.");
        if (getCapabilities().fullySupports(filter)) {

            try {
                if (!inline) {
                    out.write("WHERE ");
                }

                filter.accept(this, null);

                // out.write(";");
            } catch (java.io.IOException ioe) {
                LOGGER.warning("Unable to export filter" + ioe);
                throw new FilterToSQLException("Problem writing filter: ", ioe);
            }
        } else {
            throw new FilterToSQLException("Filter type not supported: " + filter);
        }
    }

    /**
     * purely a convenience method.
     *
     * <p>Equivalent to:
     *
     * <p>StringWriter out = new StringWriter(); new FilterToSQL(out).encode(filter);
     * out.getBuffer().toString();
     *
     * @return a string representing the filter encoded to SQL.
     */
    @SuppressWarnings("PMD.CloseResource")
    public String encodeToString(Filter filter) throws FilterToSQLException {
        StringWriter out = new StringWriter();
        this.out = out;
        this.encode(filter);
        return out.getBuffer().toString();
    }

    /**
     * Performs the encoding, sends the encoded sql to the writer passed in.
     *
     * @param expression the Expression to be encoded.
     * @throws FilterToSQLException If filter type not supported, or if there were io problems.
     */
    public void encode(Expression expression) throws FilterToSQLException {
        if (out == null) throw new FilterToSQLException("Can't encode to a null writer.");
        expression.accept(this, null);
    }

    /**
     * purely a convenience method.
     *
     * <p>Equivalent to:
     *
     * <p>StringWriter out = new StringWriter(); new FilterToSQL(out).encode(filter);
     * out.getBuffer().toString();
     *
     * @return a string representing the filter encoded to SQL.
     */
    @SuppressWarnings("PMD.CloseResource")
    public String encodeToString(Expression expression) throws FilterToSQLException {
        StringWriter out = new StringWriter();
        this.out = out;
        this.encode(expression);
        return out.getBuffer().toString();
    }

    /**
     * Sets the featuretype the encoder is encoding sql for.
     *
     * <p>This is used for context for attribute expressions when encoding to sql.
     */
    public void setFeatureType(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    /** Returns the feature type set in this encoder, if any */
    public SimpleFeatureType getFeatureType() {
        return this.featureType;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getDatabaseSchema() {
        return databaseSchema;
    }

    public void setDatabaseSchema(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    /**
     * Sets the capabilities of this filter.
     *
     * @return FilterCapabilities for this Filter
     */
    protected FilterCapabilities createFilterCapabilities() {
        FilterCapabilities capabilities = new FilterCapabilities();
        if (inEncodingEnabled) {
            capabilities.addAll(InFunction.getInCapabilities());
        }

        // basic expressions
        capabilities.addType(Add.class);
        capabilities.addType(Subtract.class);
        capabilities.addType(Divide.class);
        capabilities.addType(Multiply.class);
        capabilities.addType(PropertyName.class);
        capabilities.addType(Literal.class);

        // basic filters
        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        capabilities.addType(PropertyIsNull.class);
        capabilities.addType(PropertyIsBetween.class);
        capabilities.addType(PropertyIsLike.class);
        capabilities.addType(Id.class);
        capabilities.addType(IncludeFilter.class);
        capabilities.addType(ExcludeFilter.class);

        // temporal filters
        capabilities.addType(After.class);
        capabilities.addType(Before.class);
        capabilities.addType(Begins.class);
        capabilities.addType(BegunBy.class);
        capabilities.addType(During.class);
        capabilities.addType(Ends.class);
        capabilities.addType(EndedBy.class);
        capabilities.addType(TContains.class);
        capabilities.addType(TEquals.class);

        return capabilities;
    }

    /**
     * Describes the capabilities of this encoder.
     *
     * <p>Performs lazy creation of capabilities. If you're subclassing this class, override
     * createFilterCapabilities to declare which filtercapabilities you support. Don't use this
     * method.
     *
     * @return The capabilities supported by this encoder.
     */
    public final synchronized FilterCapabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = createFilterCapabilities();
        }

        return capabilities; // maybe clone?  Make immutable somehow
    }

    /** Sets the capabilities for the encoder. */
    public void setCapabilities(FilterCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    // BEGIN IMPLEMENTING org.opengis.filter.FilterVisitor METHODS

    /**
     * @see {@link FilterVisitor#visit(ExcludeFilter, Object)}
     *     <p>Writes the SQL for the IncludeFilter by writing "FALSE".
     * @param filter the filter to be visited
     */
    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        try {
            out.write("0 = 1");
        } catch (IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    /**
     * @see {@link FilterVisitor#visit(IncludeFilter, Object)}
     *     <p>Writes the SQL for the IncludeFilter by writing "TRUE".
     * @param filter the filter to be visited
     */
    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        try {
            out.write("1 = 1");
        } catch (IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    /**
     * Writes the SQL for the PropertyIsBetween Filter.
     *
     * @param filter the Filter to be visited.
     * @throws RuntimeException for io exception with writer
     */
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting PropertyIsBetween");

        Expression expr = filter.getExpression();
        Expression lowerbounds = filter.getLowerBoundary();
        Expression upperbounds = filter.getUpperBoundary();

        Class context;
        AttributeDescriptor attType = (AttributeDescriptor) expr.evaluate(featureType);
        if (attType != null) {
            context = attType.getType().getBinding();
        } else {
            // assume it's a string?
            context = String.class;
        }

        try {
            expr.accept(this, extraData);
            out.write(" BETWEEN ");
            lowerbounds.accept(this, context);
            out.write(" AND ");
            upperbounds.accept(this, context);
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    /**
     * Writes the SQL for the Like Filter. Assumes the current java implemented wildcards for the
     * Like Filter: . for multi and .? for single. And replaces them with the SQL % and _,
     * respectively.
     *
     * @param filter the Like Filter to be visited.
     * @task REVISIT: Need to think through the escape char, so it works right when Java uses one,
     *     and escapes correctly with an '_'.
     */
    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        char esc = filter.getEscape().charAt(0);
        char multi = filter.getWildCard().charAt(0);
        char single = filter.getSingleChar().charAt(0);
        boolean matchCase = filter.isMatchingCase();

        String literal = filter.getLiteral();
        Expression att = filter.getExpression();

        // JD: hack for date values, we append some additional padding to handle
        // the matching of time/timezone/etc...
        Class attributeType = getExpressionType(att);
        // null check if returnType of expression is Object, null is returned
        // from getExpressionType
        if (attributeType != null && Date.class.isAssignableFrom(attributeType)) {
            literal += multi;
        }

        String pattern = LikeFilterImpl.convertToSQL92(esc, multi, single, matchCase, literal);

        try {
            if (!matchCase) {
                out.write(" UPPER(");
            }

            att.accept(this, extraData);

            if (!matchCase) {
                out.write(") LIKE '");
            } else {
                out.write(" LIKE '");
            }

            out.write(pattern);
            out.write("' ");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    /**
     * Write the SQL for an And filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(And filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, "AND");
    }

    /**
     * Write the SQL for a Not filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(Not filter, Object extraData) {
        try {
            if (filter.getFilter() instanceof PropertyIsNull) {
                Expression expr = ((PropertyIsNull) filter.getFilter()).getExpression();
                if (isEnumerated(expr)) {
                    // skip enum remapping
                    writeEncodedField(
                            Integer.class,
                            (PropertyName) expr,
                            (AttributeDescriptor) expr.evaluate(featureType));
                } else {
                    expr.accept(this, extraData);
                }
                out.write(" IS NOT NULL ");

            } else {
                out.write("NOT (");
                filter.getFilter().accept(this, extraData);
                out.write(")");
            }
            return extraData;
        } catch (IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }
    }

    /**
     * Write the SQL for an Or filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(Or filter, Object extraData) {
        // Check if we can encode the "or" as an "in", happens if the same property
        // is compared as equality with multiple values. This is important as some databases
        // cannot optimize index access otherwise
        // The collector contains name -> list<values> for equalities, and the filter->null
        // otherwise
        LinkedHashMap<Object, List<Literal>> grouped = new LinkedHashMap<>();
        int maxGroupSize = 0;
        for (Filter child : filter.getChildren()) {
            Expression[] nameLiteral = getNameLiteralFromEquality(child);
            if (nameLiteral == null) {
                grouped.put(child, null);
            } else {
                PropertyName name = (PropertyName) nameLiteral[0];
                Literal value = (Literal) nameLiteral[1];
                List<Literal> values = grouped.get(name);
                if (values == null) {
                    values = new ArrayList<>();
                    grouped.put(name, values);
                }
                values.add(value);
                maxGroupSize = Math.max(maxGroupSize, values.size());
            }
        }

        if (maxGroupSize < 2) {
            // no special behavior needed
            return visit((BinaryLogicOperator) filter, "OR");
        }

        try {
            Iterator<Map.Entry<Object, List<Literal>>> iterator = grouped.entrySet().iterator();

            // ok, we can output at least one "in" statement
            if (grouped.size() > 1) {
                out.write("(");
            }

            while (iterator.hasNext()) {
                Map.Entry<Object, List<Literal>> entry = iterator.next();
                if (entry.getKey() instanceof PropertyName) {
                    PropertyName pn = (PropertyName) entry.getKey();
                    List<Literal> literals = entry.getValue();

                    pn.accept(this, extraData);
                    Class binding = getExpressionType(pn);

                    // avoid bizarre enconding in case there is just one value in this one
                    // (there is at least one other variable in this OR that benefits from IN)
                    int literalsSize = literals.size();
                    if (literalsSize == 1) {
                        out.write(" = ");
                        literals.get(0).accept(this, binding);
                    } else {
                        out.write(" IN (");

                        for (int i = 0; i < literalsSize; i++) {
                            literals.get(i).accept(this, binding);
                            if (i < literalsSize - 1) {
                                out.write(", ");
                            }
                        }
                        out.write(")");
                    }

                } else {
                    ((Filter) entry.getKey()).accept(this, extraData);
                }

                if (iterator.hasNext()) {
                    out.write(" OR ");
                }
            }

            if (grouped.size() > 1) {
                out.write(")");
            }
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }

        return extraData;
    }

    /**
     * If the child is an equality between a property name and a literal, returns an array with the
     * property name first, and the expression second. Will return null otherwise.
     */
    private Expression[] getNameLiteralFromEquality(Filter child) {
        if (child instanceof PropertyIsEqualTo) {
            PropertyIsEqualTo equal = (PropertyIsEqualTo) child;
            Expression ex1 = equal.getExpression1();
            Expression ex2 = equal.getExpression2();
            if (ex1 instanceof PropertyName && ex2 instanceof Literal) {
                return new Expression[] {ex1, ex2};
            } else if (ex2 instanceof PropertyName && ex1 instanceof Literal) {
                return new Expression[] {ex2, ex1};
            }
        }

        return null;
    }

    /**
     * Common implementation for BinaryLogicOperator filters. This way they're all handled
     * centrally.
     *
     * @param filter the logic statement to be turned into SQL.
     * @param extraData extra filter data. Not modified directly by this method.
     */
    protected Object visit(BinaryLogicOperator filter, Object extraData) {
        LOGGER.finer("exporting LogicFilter");

        String type = (String) extraData;

        try {
            java.util.Iterator list = filter.getChildren().iterator();

            // AND or OR
            out.write("(");

            while (list.hasNext()) {
                ((Filter) list.next()).accept(this, extraData);

                if (list.hasNext()) {
                    out.write(" " + type + " ");
                }
            }

            out.write(")");

        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    /**
     * Write the SQL for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "=");
        return extraData;
    }

    /**
     * Write the SQL for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, ">=");
        return extraData;
    }

    /**
     * Write the SQL for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        visitBinaryComparisonOperator(filter, ">");
        return extraData;
    }

    /**
     * Write the SQL for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "<");
        return extraData;
    }

    /**
     * Write the SQL for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "<=");
        return extraData;
    }

    /**
     * Write the SQL for this kind of filter
     *
     * @param filter the filter to visit
     * @param extraData extra data (unused by this method)
     */
    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, "!=");
        return extraData;
    }

    /**
     * Common implementation for BinaryComparisonOperator filters.  This way
     * they're all handled centrally.
     *
     *  DJB: note, postgis overwrites this implementation because of the way
     *       null is handled.  This is for <PropertyIsNull> filters and <PropertyIsEqual> filters
     *       are handled.  They will come here with "property = null".
     *       NOTE:
     *        SELECT * FROM <table> WHERE <column> isnull;  -- postgresql
     *        SELECT * FROM <table> WHERE isnull(<column>); -- oracle???
     *
     * @param filter the comparison to be turned into SQL.
     *
     * @throws RuntimeException for io exception with writer
     */
    protected void visitBinaryComparisonOperator(BinaryComparisonOperator filter, Object extraData)
            throws RuntimeException {
        LOGGER.finer("exporting SQL ComparisonFilter");

        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();

        // shortcut for IN case, to have a more natural encoding for the common/sane case of
        // "in(property, v1, v2, ... vn) = true" into "property in (v1, v2, ..., vn)
        if (inEncodingEnabled && ("=".equals(extraData) || "!=".equals(extraData))) {
            if (right instanceof Literal
                    && InFunction.isInFunction(left)
                    && right.evaluate(null, Boolean.class) != null) {
                encodeInComparison((Function) left, (Literal) right, extraData);
                return;
            } else if (left instanceof Literal
                    && InFunction.isInFunction(right)
                    && left.evaluate(null, Boolean.class) != null) {
                encodeInComparison((Function) right, (Literal) left, extraData);
                return;
            }
        }

        Class rightContext = getExpressionType(left);
        Class leftContext = getExpressionType(right);

        encodeBinaryComparisonOperator(filter, extraData, left, right, leftContext, rightContext);
    }

    /**
     * Encode a BinaryComparisonOperator to SQL
     *
     * @param filter the comparison operator to be turned to SQL
     * @param extraData extraData
     * @param left left parameter of the binary operator
     * @param right right parameter of the binary operator
     * @param leftContext expression type of the right parameter used as context for the left
     *     parameter
     * @param rightContext expression type of the left parameter used as context for the right
     *     parameter
     */
    protected void encodeBinaryComparisonOperator(
            BinaryComparisonOperator filter,
            Object extraData,
            Expression left,
            Expression right,
            Class leftContext,
            Class rightContext) {
        // case sensitivity
        boolean matchCase = true;
        if (!filter.isMatchingCase()) {
            // we only do for = and !=
            if (filter instanceof PropertyIsEqualTo || filter instanceof PropertyIsNotEqualTo) {
                // and only for strings
                if (String.class.equals(leftContext) || String.class.equals(rightContext)) {
                    matchCase = false;
                }
            }
        }

        String type = (String) extraData;

        try {
            // for comparisons with enumerated types, back-map the values to numbers, to allow
            // database index usage
            if (isEnumerated(right) || isEnumerated(left)) {
                encodeEnumeratedComparison(right, left, type, matchCase);
                return;
            }

            if (matchCase) {
                writeBinaryExpressionMember(left, leftContext);
                out.write(" " + type + " ");
                writeBinaryExpressionMember(right, rightContext);
            } else {
                // wrap both sides in "lower"
                FunctionImpl f =
                        new FunctionImpl() {
                            {
                                functionName =
                                        new FunctionNameImpl(
                                                "lower",
                                                parameter("lowercase", String.class),
                                                parameter("string", String.class));
                            }
                        };
                f.setName("lower");

                f.setParameters(Arrays.asList(left));
                f.accept(this, Arrays.asList(leftContext));

                out.write(" " + type + " ");

                f.setParameters(Arrays.asList(right));
                f.accept(this, Arrays.asList(rightContext));
            }
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
    }

    private void encodeEnumeratedComparison(
            Expression right, Expression left, String type, boolean matchCase) throws IOException {
        EnumMapper mapper = getEnumMapper(right);
        if (mapper != null) {
            PropertyName rightName = (PropertyName) right;
            if (left instanceof Literal) {
                String value = left.evaluate(null, String.class);
                Integer code = mapper.fromString(value, matchCase);
                if (code == null) {
                    out.write("FALSE");
                } else {
                    out.write(String.valueOf(code));
                    out.write(" " + type + " ");
                    writeEncodedField(
                            Integer.class,
                            rightName,
                            (AttributeDescriptor) right.evaluate(featureType));
                }
            } else {
                out.write("CASE ");
                if (!matchCase) {
                    out.write("lower(");
                    writeBinaryExpressionMember(left, Integer.class);
                    out.write(")");
                }
                for (Map.Entry<String, Integer> entry : mapper.getStringToInteger().entrySet()) {
                    out.write("WHEN '" + entry.getKey() + "' THEN " + entry.getValue() + "\n");
                }
                out.write("END");
                out.write(" " + type + " ");
                writeEncodedField(
                        Integer.class,
                        rightName,
                        (AttributeDescriptor) right.evaluate(featureType));
            }
        } else {
            mapper = getEnumMapper(left);

            PropertyName leftName = (PropertyName) left;
            if (right instanceof Literal) {
                String value = right.evaluate(null, String.class);
                Integer code = mapper.fromString(value, matchCase);
                if (code == null) {
                    out.write("FALSE");
                } else {
                    writeEncodedField(
                            Integer.class,
                            leftName,
                            (AttributeDescriptor) left.evaluate(featureType));
                    out.write(" " + type + " ");
                    out.write(String.valueOf(code));
                }
            } else {
                writeEncodedField(
                        Integer.class, leftName, (AttributeDescriptor) left.evaluate(featureType));
                out.write(" " + type + " ");
                out.write("CASE ");
                if (!matchCase) {
                    out.write("lower(");
                    writeBinaryExpressionMember(right, Integer.class);
                    out.write(")");
                }
                for (Map.Entry<String, Integer> entry : mapper.getStringToInteger().entrySet()) {
                    out.write("WHEN '" + entry.getKey() + "' THEN " + entry.getValue() + "\n");
                }
                out.write("END");
            }
        }
    }

    private boolean isEnumerated(Expression ex) {
        return getEnumMapper(ex) != null;
    }

    private EnumMapper getEnumMapper(Expression ex) {
        if (ex instanceof PropertyName) {
            AttributeDescriptor ad = (AttributeDescriptor) ex.evaluate(featureType);
            if (ad != null) {
                Object o = ad.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);
                if (o instanceof EnumMapper) {
                    return (EnumMapper) o;
                }
            }
        }

        return null;
    }

    /** Writes out an expression, wrapping it in parenthesis if it's a binary one */
    protected void writeBinaryExpressionMember(Expression exp, Class context) throws IOException {
        if (context != null && isBinaryExpression(exp)) {
            writeBinaryExpression(exp, context);
        } else {
            exp.accept(this, context);
        }
    }

    /**
     * Returns the expression type, if can be found, or null otherwise. Based on feature type
     * inspection for attribute references, and function return types otherwise. Cannot yet handle
     * arithmetics or literals (the latter are actually not useful and should be ignored, expression
     * in geotools are free to use a string where a number is needed due to the on the fly
     * conversion, here we are concerned only with types that are a reliable reference).
     *
     * @return The expression return type, or null if cannot be computed
     */
    public Class getExpressionType(Expression expression) {
        Class binding = null;
        if (expression instanceof PropertyName) {
            // aha!  It's a propertyname, we should get the class and pass it in
            // as context to the tree walker.
            AttributeDescriptor attType = (AttributeDescriptor) expression.evaluate(featureType);
            if (attType != null) {
                binding = attType.getType().getBinding();
            }
        } else if (expression instanceof Function) {
            // check for a function return type
            Class ret = getFunctionReturnType((Function) expression);
            if (ret != null) {
                binding = ret;
            }
        }
        return binding;
    }

    /*
     * write out the binary expression and cast only the end result, not passing any context into
     * encoding the individual parts
     */

    private void encodeInComparison(Function in, Literal bool, Object extraData) {
        boolean negated = "!=".equals(extraData);
        if (bool.evaluate(null, Boolean.class) == Boolean.FALSE) {
            negated = !negated;
        }

        visitInFunction(in, false, negated, null);
    }

    @SuppressWarnings("PMD.CloseResource") // no need to close, it's a field
    protected void writeBinaryExpression(Expression e, Class context) throws IOException {
        Writer tmp = out;
        try {
            out = new StringWriter();
            out.write("(");
            e.accept(this, null);
            out.write(")");
            tmp.write(cast(out.toString(), context));
        } finally {
            out = tmp;
        }
    }

    /*
     * returns the return type of the function, or null if it could not be determined or is simply
     * of return type Object.class
     */
    protected Class getFunctionReturnType(Function f) {
        Class clazz = Object.class;
        if (f.getFunctionName() != null && f.getFunctionName().getReturn() != null) {
            clazz = f.getFunctionName().getReturn().getType();
        }
        if (clazz == Object.class) {
            clazz = null;
        }
        return clazz;
    }

    /*
     * determines if the function is a binary expression
     */
    protected boolean isBinaryExpression(Expression e) {
        return e instanceof BinaryExpression;
    }

    /**
     * Writes the SQL for the Null Filter.
     *
     * @param filter the null filter to be written to SQL.
     * @throws RuntimeException for io exception with writer
     */
    @Override
    public Object visit(PropertyIsNull filter, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting NullFilter");

        Expression expr = filter.getExpression();

        try {
            if (isEnumerated(expr)) {
                // skip enum remapping
                writeEncodedField(
                        Integer.class,
                        (PropertyName) expr,
                        (AttributeDescriptor) expr.evaluate(featureType));
            } else {
                expr.accept(this, extraData);
            }
            out.write(" IS NULL ");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException(IO_ERROR, ioe);
        }
        return extraData;
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException("isNil not supported");
    }

    /**
     * Encodes an Id filter
     *
     * @param filter the
     * @throws RuntimeException If there's a problem writing output
     */
    @Override
    public Object visit(Id filter, Object extraData) {
        if (primaryKey == null) {
            throw new RuntimeException("Must set primary key before trying to encode FIDFilters");
        }

        Set ids = filter.getIdentifiers();

        LOGGER.finer("Exporting FID=" + ids);
        try {
            if (ids.size() > 1) {
                out.write("(");
            }
            List<PrimaryKeyColumn> columns = primaryKey.getColumns();
            for (Iterator i = ids.iterator(); i.hasNext(); ) {
                Identifier id = (Identifier) i.next();
                List<Object> attValues = JDBCDataStore.decodeFID(primaryKey, id.toString(), false);

                out.write("(");

                for (int j = 0; j < attValues.size(); j++) {
                    // in case of join the pk columns need to be qualified with alias
                    if (filter instanceof JoinId) {
                        out.write(escapeName(((JoinId) filter).getAlias()));
                        out.write(".");
                    }
                    out.write(escapeName(columns.get(j).getName()));
                    out.write(" = '");
                    out.write(
                            attValues.get(j).toString()); // DJB: changed this to attValues[j] from
                    // attValues[i].
                    out.write("'");

                    if (j < (attValues.size() - 1)) {
                        out.write(" AND ");
                    }
                }

                out.write(")");

                if (i.hasNext()) {
                    out.write(" OR ");
                }
            }
            if (ids.size() > 1) {
                out.write(")");
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(IO_ERROR, e);
        }

        return extraData;
    }

    @Override
    public Object visit(BBOX filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        return visitBinarySpatialOperator(filter, extraData);
    }

    protected Object visitBinarySpatialOperator(BinarySpatialOperator filter, Object extraData) {
        // basic checks
        if (filter == null) throw new NullPointerException("Filter to be encoded cannot be null");
        if (!(filter instanceof BinarySpatialOperator))
            throw new IllegalArgumentException(
                    "This filter is not a binary spatial operator, "
                            + "can't do SDO relate against it: "
                            + filter.getClass());

        // extract the property name and the geometry literal
        BinarySpatialOperator op = filter;
        Expression e1 = op.getExpression1();
        Expression e2 = op.getExpression2();

        if (e1 instanceof Literal && e2 instanceof PropertyName) {
            e1 = op.getExpression2();
            e2 = op.getExpression1();
        }

        if (e1 instanceof PropertyName) {
            // handle native srid
            currentGeometry = null;
            currentSRID = null;
            currentDimension = null;
            if (featureType != null) {
                // going thru evaluate ensures we get the proper result even if the
                // name has
                // not been specified (convention -> the default geometry)
                AttributeDescriptor descriptor = (AttributeDescriptor) e1.evaluate(featureType);
                if (descriptor instanceof GeometryDescriptor) {
                    currentGeometry = (GeometryDescriptor) descriptor;
                    currentSRID =
                            (Integer) descriptor.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
                    currentDimension =
                            (Integer) descriptor.getUserData().get(Hints.COORDINATE_DIMENSION);
                }
            }
        }

        if (e1 instanceof PropertyName && e2 instanceof Literal) {
            // call the "regular" method
            return visitBinarySpatialOperator(
                    filter,
                    (PropertyName) e1,
                    (Literal) e2,
                    filter.getExpression1() instanceof Literal,
                    extraData);
        } else {
            // call the join version
            return visitBinarySpatialOperator(filter, e1, e2, extraData);
        }
    }

    /** Handles the common case of a PropertyName,Literal geometry binary spatial operator. */
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter,
            PropertyName property,
            Literal geometry,
            boolean swapped,
            Object extraData) {
        throw new RuntimeException(
                "Subclasses must implement this method in order to handle geometries");
    }

    /**
     * Handles the more general case of two generic expressions.
     *
     * <p>The most common case is two PropertyName expressions, which happens during a spatial join.
     */
    protected Object visitBinarySpatialOperator(
            BinarySpatialOperator filter, Expression e1, Expression e2, Object extraData) {
        throw new RuntimeException(
                "Subclasses must implement this method in order to handle geometries");
    }

    protected Object visitBinaryTemporalOperator(BinaryTemporalOperator filter, Object extraData) {
        if (filter == null) {
            throw new NullPointerException("Null filter");
        }

        Expression e1 = filter.getExpression1();
        Expression e2 = filter.getExpression2();

        if (e1 instanceof Literal && e2 instanceof PropertyName) {
            e1 = filter.getExpression2();
            e2 = filter.getExpression1();
        }

        if (e1 instanceof PropertyName && e2 instanceof Literal) {
            // call the "regular" method
            return visitBinaryTemporalOperator(
                    filter,
                    (PropertyName) e1,
                    (Literal) e2,
                    filter.getExpression1() instanceof Literal,
                    extraData);
        } else {
            // call the join version
            return visitBinaryTemporalOperator(filter, e1, e2, extraData);
        }
    }

    /**
     * Handles the common case of a PropertyName,Literal geometry binary temporal operator.
     *
     * <p>Subclasses should override if they support more temporal operators than what is handled in
     * this base class.
     */
    protected Object visitBinaryTemporalOperator(
            BinaryTemporalOperator filter,
            PropertyName property,
            Literal temporal,
            boolean swapped,
            Object extraData) {

        Class typeContext = null;
        AttributeDescriptor attType = (AttributeDescriptor) property.evaluate(featureType);
        if (attType != null) {
            typeContext = attType.getType().getBinding();
        }

        // check for time period
        Period period = null;
        if (temporal.evaluate(null) instanceof Period) {
            period = (Period) temporal.evaluate(null);
        }

        // verify that those filters that require a time period have one
        if ((filter instanceof Begins
                        || filter instanceof BegunBy
                        || filter instanceof Ends
                        || filter instanceof EndedBy
                        || filter instanceof During
                        || filter instanceof TContains)
                && period == null) {
            throw new IllegalArgumentException("Filter requires a time period");
        }
        if (filter instanceof TEquals && period != null) {
            throw new IllegalArgumentException("TEquals filter does not accept time period");
        }

        // ensure the time period is the correct argument
        if ((filter instanceof Begins || filter instanceof Ends || filter instanceof During)
                && swapped) {
            throw new IllegalArgumentException("Time period must be second argument of Filter");
        }
        if ((filter instanceof BegunBy || filter instanceof EndedBy || filter instanceof TContains)
                && !swapped) {
            throw new IllegalArgumentException("Time period must be first argument of Filter");
        }

        try {
            if (filter instanceof After || filter instanceof Before) {
                String op = filter instanceof After ? " > " : " < ";
                String inv = filter instanceof After ? " < " : " > ";

                if (period != null) {
                    out.write("(");

                    property.accept(this, extraData);
                    out.write(swapped ? inv : op);
                    visitBegin(period, extraData);

                    out.write(" AND ");

                    property.accept(this, extraData);
                    out.write(swapped ? inv : op);
                    visitEnd(period, extraData);

                    out.write(")");
                } else {
                    if (swapped) {
                        temporal.accept(this, typeContext);
                    } else {
                        property.accept(this, extraData);
                    }

                    out.write(op);

                    if (swapped) {
                        property.accept(this, extraData);
                    } else {
                        temporal.accept(this, typeContext);
                    }
                }
            } else if (filter instanceof Begins
                    || filter instanceof Ends
                    || filter instanceof BegunBy
                    || filter instanceof EndedBy) {
                property.accept(this, extraData);
                out.write(" = ");

                if (filter instanceof Begins || filter instanceof BegunBy) {
                    visitBegin(period, extraData);
                } else {
                    visitEnd(period, extraData);
                }
            } else if (filter instanceof During || filter instanceof TContains) {
                property.accept(this, extraData);
                out.write(" BETWEEN ");

                visitBegin(period, extraData);
                out.write(" AND ");
                visitEnd(period, extraData);
            } else if (filter instanceof TEquals) {
                property.accept(this, extraData);
                out.write(" = ");
                temporal.accept(this, typeContext);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error encoding temporal filter", e);
        }

        return extraData;
    }

    void visitBegin(Period p, Object extraData) {
        filterFactory.literal(p.getBeginning().getPosition().getDate()).accept(this, extraData);
    }

    void visitEnd(Period p, Object extraData) {
        filterFactory.literal(p.getEnding().getPosition().getDate()).accept(this, extraData);
    }

    /**
     * Handles the general case of two expressions in a binary temporal filter.
     *
     * <p>Subclasses should override if they support more temporal operators than what is handled in
     * this base class.
     */
    protected Object visitBinaryTemporalOperator(
            BinaryTemporalOperator filter, Expression e1, Expression e2, Object extraData) {

        if (!(filter instanceof After || filter instanceof Before || filter instanceof TEquals)) {
            throw new IllegalArgumentException(
                    "Unsupported filter: " + filter + ". Only After,Before,TEquals supported");
        }

        String op = filter instanceof After ? ">" : filter instanceof Before ? "<" : "=";

        try {
            e1.accept(this, extraData);
            out.write(" " + op + " ");
            e2.accept(this, extraData);
        } catch (IOException e) {
            return new RuntimeException("Error encoding temporal filter", e);
        }
        return extraData;
    }

    /**
     * Encodes a null filter value. The current implementation does exactly nothing.
     *
     * @param extraData extra data to be used to evaluate the filter
     * @return the untouched extraData parameter
     */
    @Override
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    // END IMPLEMENTING org.opengis.filter.FilterVisitor METHODS

    // START IMPLEMENTING org.opengis.filter.ExpressionVisitor METHODS

    /**
     * Writes the SQL for the attribute Expression.
     *
     * @param expression the attribute to turn to SQL.
     * @throws RuntimeException for io exception with writer
     */
    @Override
    public Object visit(PropertyName expression, Object extraData) throws RuntimeException {
        LOGGER.finer("exporting PropertyName");

        Class target = null;
        if (extraData instanceof Class) {
            target = (Class) extraData;
        }

        try {
            SimpleFeatureType featureType = this.featureType;

            // check for join
            if (expression instanceof JoinPropertyName) {
                // encode the prefix
                out.write(escapeName(((JoinPropertyName) expression).getAlias()));
                out.write(".");
            }

            // first evaluate expression against feautre type get the attribute,
            //  this handles xpath
            AttributeDescriptor attribute = null;
            EnumMapper mapper = null;
            try {
                attribute = (AttributeDescriptor) expression.evaluate(featureType);
                if (attribute != null) {
                    mapper = (EnumMapper) attribute.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);
                }
            } catch (Exception e) {
                // just log and fall back on just encoding propertyName straight up
                String msg = "Error occured mapping " + expression + " to feature type";
                LOGGER.log(Level.WARNING, msg, e);
            }

            // handle integer mapped enumerations
            if (mapper != null) {
                out.write("CASE ");
            }

            writeEncodedField(target, expression, attribute);

            // If we got here, it means the property is used inside some expression or function,
            // for this case we expand the property. For comparisons instead, the literal is
            // backmapped to an integer when possible, to allow index usage
            if (mapper != null) {
                out.write("\n ");
                for (Map.Entry<Integer, String> entry : mapper.getIntegerToString().entrySet()) {
                    out.write("WHEN " + entry.getKey() + " THEN '" + entry.getValue() + "'\n");
                }
                out.write("END");
            }

        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing attribute exp", ioe);
        }
        return extraData;
    }

    private void writeEncodedField(
            Class<?> target, PropertyName expression, AttributeDescriptor attribute)
            throws IOException {
        String encodedField;
        if (attribute != null) {
            encodedField = fieldEncoder.encode(escapeName(attribute.getLocalName()));
            if (target != null && target.isAssignableFrom(attribute.getType().getBinding())) {
                // no need for casting, it's already the right type
                target = null;
            }
        } else {
            // fall back to just encoding the property name
            encodedField = fieldEncoder.encode(escapeName(expression.getPropertyName()));
        }

        // handle destination type if necessary
        if (target != null) {
            out.write(cast(encodedField, target));
        } else {
            out.write(encodedField);
        }
    }

    /**
     * Gives the opportunity to subclasses to force the property to the desired type. By default it
     * simply writes out the property as-is (the property must be already escaped).
     */
    protected String cast(String encodedProperty, Class target) throws IOException {
        return encodedProperty;
    }

    /**
     * Export the contents of a Literal Expresion
     *
     * @param expression the Literal to export
     * @throws RuntimeException for io exception with writer
     */
    @Override
    public Object visit(Literal expression, Object context) throws RuntimeException {
        LOGGER.finer("exporting LiteralExpression");

        // type to convert the literal to
        Class target = null;
        if (context instanceof Class) {
            target = (Class) context;
        }

        try {
            // evaluate the expression
            Object literal = evaluateLiteral(expression, target);

            // handle geometry case
            if (literal instanceof Geometry) {
                // call this method for backwards compatibility with subclasses
                visitLiteralGeometry(filterFactory.literal(literal));
            } else if (literal instanceof Envelope) {
                visitLiteralGeometry(
                        filterFactory.literal(BBOXImpl.boundingPolygon((Envelope) literal)));
            } else {
                // write out the literal allowing subclasses to override this
                // behaviour (for writing out dates and the like using the BDMS custom functions)
                writeLiteral(literal);
            }

        } catch (IOException e) {
            throw new RuntimeException("IO problems writing literal", e);
        }
        return context;
    }

    public Object evaluateLiteral(Literal expression, Class<?> target) {
        Object literal = null;

        // HACK: let expression figure out the right value for numbers,
        // since the context is almost always improperly set and the
        // numeric converters try to force floating points to integrals
        // JD: the above is no longer true, so instead do a safe conversion
        if (target != null) {
            // use the target type
            if (Number.class.isAssignableFrom(target)) {
                literal = safeConvertToNumber(expression, target);

                if (literal == null) {
                    literal = safeConvertToNumber(expression, Number.class);
                }
            } else {
                literal = expression.evaluate(null, target);
            }
        }

        // check for conversion to number
        if (target == null) {
            // we don't know the target type, check for a conversion to a number

            Number number = safeConvertToNumber(expression, Number.class);
            if (number != null) {
                literal = number;
            }
        }

        // if the target was not known, of the conversion failed, try the
        // type guessing dance literal expression does only for the following
        // method call
        if (literal == null) literal = expression.evaluate(null);

        // if that failed as well, grab the value as is
        if (literal == null) literal = expression.getValue();

        return literal;
    }

    /*
     * helper to do a safe convesion of expression to a number
     */
    Number safeConvertToNumber(Expression expression, Class<?> target) {
        Object evaluated = expression.evaluate(null);
        // don't try conversion for arrays, to avoid wrong arraytosingle conversion
        if (evaluated != null && evaluated.getClass().isArray()) {
            return null;
        }
        return (Number)
                Converters.convert(
                        evaluated, target, new Hints(ConverterFactory.SAFE_CONVERSION, true));
    }

    /**
     * Writes out a non null, non geometry literal. The base class properly handles null, numeric
     * and booleans (true|false), and turns everything else into a string. Subclasses are expected
     * to override this shall they need a different treatment (e.g. for dates)
     */
    protected void writeLiteral(Object literal) throws IOException {
        if (literal == null) {
            out.write("NULL");
        } else if (literal instanceof Number || literal instanceof Boolean) {
            out.write(String.valueOf(literal));
        } else if (literal instanceof java.sql.Date || literal instanceof java.sql.Timestamp) {
            // java.sql.date toString declares to always format to yyyy-mm-dd
            // (and TimeStamp uses a similar approach)
            out.write("'" + literal + "'");
        } else if (literal instanceof java.util.Date) {
            // get back to the previous case
            Timestamp ts = new java.sql.Timestamp(((Date) literal).getTime());
            out.write("'" + ts + "'");
        } else if (literal instanceof Instant) {
            java.util.Date date = ((Instant) literal).getPosition().getDate();
            Timestamp ts = new java.sql.Timestamp(date.getTime());
            out.write("'" + ts + "'");
        } else if (literal.getClass().isArray()) {
            // write as a SQL99 array
            out.write("ARRAY[");
            int length = Array.getLength(literal);
            for (int i = 0; i < length; i++) {
                writeLiteral(Array.get(literal, i));
                if (i < length - 1) {
                    out.write(", ");
                }
            }
            out.write("]");
        } else {
            // we don't know the type...just convert back to a string
            String encoding = Converters.convert(literal, String.class, null);
            if (encoding == null) {
                // could not convert back to string, use original l value
                encoding = literal.toString();
            }

            // sigle quotes must be escaped to have a valid sql string
            String escaped = encoding.replaceAll("'", "''");
            out.write("'" + escaped + "'");
        }
    }

    /**
     * Subclasses must implement this method in order to encode geometry filters according to the
     * specific database implementation
     */
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        throw new RuntimeException(
                "Subclasses must implement this method in order to handle geometries");
    }

    protected void visitLiteralTimePeriod(Period expression) {
        throw new RuntimeException(
                "Time periods not supported, subclasses must implement this "
                        + "method to support encoding timeperiods");
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        return visit(expression, "+", extraData);
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        return visit(expression, "/", extraData);
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        return visit(expression, "*", extraData);
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        return visit(expression, "-", extraData);
    }

    /**
     * Writes the SQL for the Math Expression.
     *
     * @param expression the Math phrase to be written.
     * @param operator The operator of the expression.
     * @throws RuntimeException for io problems
     */
    protected Object visit(BinaryExpression expression, String operator, Object extraData)
            throws RuntimeException {
        LOGGER.finer("exporting Expression Math");

        try {
            encodeBinaryExpressionChild(expression.getExpression1(), extraData);
            out.write(" " + operator + " ");
            encodeBinaryExpressionChild(expression.getExpression2(), extraData);
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing expression", ioe);
        }
        return extraData;
    }

    private void encodeBinaryExpressionChild(Expression expression, Object extraData)
            throws IOException {
        boolean needsParens = expression instanceof BinaryExpression;
        if (needsParens) out.write("(");
        expression.accept(this, extraData);
        if (needsParens) out.write(")");
    }

    /**
     * Writes sql for a function expression. By default it will write the call by using the same
     * arguments provided to the GeoTools function, subclasses should override on a case by case
     * basis if this behavior is not the desired one.
     *
     * @param function a function expression
     * @throws RuntimeException If an IO error occurs.
     * @see #getFunctionName(Function)
     */
    @Override
    public Object visit(Function function, Object extraData) throws RuntimeException {
        // special case for IN functions
        if (inEncodingEnabled && InFunction.isInFunction(function)) {
            visitInFunction(function, true, false, extraData);
        } else {
            try {
                List<Expression> parameters = function.getParameters();

                // set the encoding function flag to signal we are inside a function
                encodingFunction = true;

                // write the name
                out.write(getFunctionName(function));

                // write the arguments
                out.write("(");
                List<Parameter<?>> arguments = function.getFunctionName().getArguments();
                Parameter<?> lastArgument =
                        arguments.isEmpty() ? null : arguments.get(arguments.size() - 1);
                for (int i = 0; i < parameters.size(); i++) {
                    Expression e = parameters.get(i);

                    Object context;
                    // the last argument can be multi-valued
                    if (arguments.size() <= i
                            && (lastArgument.getMaxOccurs() > 0
                                    || lastArgument.getMaxOccurs() == -1)) {
                        context = lastArgument.getType();
                    } else {
                        context = arguments.get(i).getType();
                    }

                    e.accept(this, context);

                    if (i < parameters.size() - 1) {
                        out.write(",");
                    }
                }
                out.write(")");

                // reset the encoding function flag
                encodingFunction = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return extraData;
    }

    /** Encodes a "in" function (as recognized by {@link InFunction#isInFunction(Expression)} */
    protected void visitInFunction(
            Function function, boolean encodeAsExpression, boolean negate, Object extraData) {
        try {
            if (encodeAsExpression) {
                out.write("(");
            }

            // figure out the context, the first property name found will be used as a type
            // reference,
            // otherwise null context will be used
            List<Expression> parameters = function.getParameters();
            Class context =
                    function.getParameters().stream()
                            .filter(p -> p instanceof PropertyName)
                            .map(p -> p.evaluate(featureType))
                            .filter(o -> o instanceof AttributeDescriptor)
                            .map(o -> ((AttributeDescriptor) o).getType().getBinding())
                            .findFirst()
                            .orElse(null);

            // encode the attribute being tested, as is
            function.getParameters().get(0).accept(this, context);

            if (negate) {
                out.write(" NOT IN (");
            } else {
                out.write(" IN (");
            }

            // encode all other values to be used
            int size = parameters.size();
            for (int i = 1; i < size; i++) {
                Expression e = function.getParameters().get(i);
                e.accept(this, context);

                if (i < size - 1) {
                    out.write(", ");
                }
            }
            out.write(")");

            if (encodeAsExpression) {
                out.write(")");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the n-th parameter of a function, throwing an exception if the parameter is not there
     * and has been marked as mandatory
     */
    protected Expression getParameter(Function function, int idx, boolean mandatory) {
        final List<Expression> params = function.getParameters();
        if (params == null || params.size() <= idx) {
            if (mandatory) {
                throw new IllegalArgumentException(
                        "Missing parameter number "
                                + (idx + 1)
                                + " for function "
                                + function.getName()
                                + ", cannot encode in SQL");
            } else {
                return null;
            }
        }
        return params.get(idx);
    }

    /** Maps the function to the native database function name */
    protected String getFunctionName(Function function) {
        return function.getName();
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        try {
            out.write(" ");
        } catch (java.io.IOException ioe) {
            throw new RuntimeException("IO problems writing expression", ioe);
        }

        return extraData;
    }

    // temporal filters, not supported
    @Override
    public Object visit(After after, Object extraData) {
        return visitBinaryTemporalOperator(after, extraData);
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        return visitBinaryTemporalOperator(anyInteracts, extraData);
    }

    @Override
    public Object visit(Before before, Object extraData) {
        return visitBinaryTemporalOperator(before, extraData);
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        return visitBinaryTemporalOperator(begins, extraData);
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        return visitBinaryTemporalOperator(begunBy, extraData);
    }

    @Override
    public Object visit(During during, Object extraData) {
        return visitBinaryTemporalOperator(during, extraData);
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        return visitBinaryTemporalOperator(endedBy, extraData);
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        return visitBinaryTemporalOperator(ends, extraData);
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        return visitBinaryTemporalOperator(meets, extraData);
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        return visitBinaryTemporalOperator(metBy, extraData);
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        return visitBinaryTemporalOperator(overlappedBy, extraData);
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        return visitBinaryTemporalOperator(contains, extraData);
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        return visitBinaryTemporalOperator(equals, extraData);
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        return visitBinaryTemporalOperator(contains, extraData);
    }

    /**
     * Sets the SQL name escape string.
     *
     * <p>The value of this string is prefixed and appended to table schema names, table names and
     * column names in an SQL statement to support mixed-case and non-English names. Without this,
     * the DBMS may assume a mixed-case name in the query should be treated as upper-case and an
     * SQLCODE of -204 or 206 may result if the name is not found.
     *
     * <p>Typically this is the double-quote character, ", but may not be for all databases.
     *
     * <p>If a name contains the escape string itself, the escape string is duplicated.
     *
     * <p>For example, consider the following query:
     *
     * <p>SELECT Geom FROM Spear.ArchSites May be interpreted by the database as: SELECT GEOM FROM
     * SPEAR.ARCHSITES If the column and table names were actually created using mixed-case, the
     * query needs to be specified as: SELECT "Geom" from "Spear"."ArchSites"
     *
     * @param escape the character to be used to escape database names
     */
    public void setSqlNameEscape(String escape) {
        sqlNameEscape = escape;
    }

    /** @return the sqlNameEscape */
    public String getSqlNameEscape() {
        return sqlNameEscape;
    }

    /**
     * Surrounds a name with the SQL escape string.
     *
     * <p>If the name contains the SQL escape string, the SQL escape string is duplicated.
     */
    public String escapeName(String name) {
        if (sqlNameEscape.isEmpty()) return name;
        StringBuilder sb = new StringBuilder();
        sb.append(sqlNameEscape);
        int offset = 0;
        int escapeOffset;
        while ((escapeOffset = name.indexOf(sqlNameEscape, offset)) != -1) {
            sb.append(name.substring(offset, escapeOffset));
            sb.append(sqlNameEscape);
            sb.append(sqlNameEscape);
            offset = escapeOffset + sqlNameEscape.length();
        }
        sb.append(name.substring(offset));
        sb.append(sqlNameEscape);
        return sb.toString();
    }

    /**
     * Interface for customizing the encoding of Fields, irrespective of which subclass of
     * FilterToSQL we are using.
     */
    public static interface FieldEncoder {
        public String encode(String s);
    }

    /** Default Field Encoder: simply returns name of field. */
    private static class DefaultFieldEncoder implements FieldEncoder {

        public static DefaultFieldEncoder DEFAULT_FIELD_ENCODER = new DefaultFieldEncoder();

        private DefaultFieldEncoder() {};

        @Override
        public String encode(String s) {
            return s;
        }
    }

    /** Current field encoder */
    protected FieldEncoder fieldEncoder = DefaultFieldEncoder.DEFAULT_FIELD_ENCODER;

    /**
     * Set custom field encoder
     *
     * @param fieldEncoder the field encoder
     */
    public void setFieldEncoder(FieldEncoder fieldEncoder) {
        this.fieldEncoder = fieldEncoder;
    }

    /**
     * Converts the distance of the operator in meters, or returns the current value if there is no
     * units distance
     */
    protected double getDistanceInMeters(DistanceBufferOperator operator) {
        double distance = operator.getDistance();
        String units = operator.getDistanceUnits();
        // no units or no SRID, no party, return value as-is
        if (units == null || UNITS_MAP.get(units.toLowerCase()) == null) {
            return distance;
        }

        double factor = UNITS_MAP.get(units.toLowerCase());
        return distance * factor;
    }

    /**
     * Rough evaluation of distance in the units of the current SRID, assuming that the SRID maps to
     * a known EPSG code. Will use a rather imprecise transformation for distances over degrees, but
     * better than nothing.
     */
    protected double getDistanceInNativeUnits(DistanceBufferOperator operator) {
        if (currentSRID == null) {
            return operator.getDistance();
        }
        try {
            CoordinateReferenceSystem crs = CRS.getHorizontalCRS(CRS.decode("EPSG:" + currentSRID));
            double distanceMeters = getDistanceInMeters(operator);
            if (crs instanceof GeographicCRS) {
                double sizeDegree = 110574.2727;
                Coordinate center = getReferenceGeometryCentroid(operator);
                if (center != null) {
                    double cosLat = Math.cos(Math.PI * center.y / 180.0);
                    double latAdjustment = Math.sqrt(1 + cosLat * cosLat) / Math.sqrt(2.0);
                    sizeDegree *= latAdjustment;
                }

                return distanceMeters / sizeDegree;
            } else {
                @SuppressWarnings("unchecked")
                Unit<Length> unit = (Unit<Length>) crs.getCoordinateSystem().getAxis(0).getUnit();
                if (unit == null) {
                    return distanceMeters;
                } else {
                    UnitConverter converter = SI.METRE.getConverterTo(unit);
                    return converter.convert(distanceMeters);
                }
            }
        } catch (Exception e) {
            LOGGER.log(
                    Level.FINE,
                    "Failed to turn the distance of spatial "
                            + "filter into native units, using it as a pure number instead",
                    e);
            // tried, fall back on pure value
            return operator.getDistance();
        }
    }

    /** Returns the center of the reference geometry of the distance buffer operator, in case */
    protected Coordinate getReferenceGeometryCentroid(DistanceBufferOperator operator) {
        Geometry geom = operator.getExpression1().evaluate(null, Geometry.class);
        if (geom == null) {
            geom = operator.getExpression2().evaluate(null, Geometry.class);
        }
        if (geom == null) {
            return null;
        }
        return geom.getCentroid().getCoordinate();
    }

    @Override
    public Object visit(NativeFilter filter, Object data) {
        try {
            out.write("(" + filter.getNative() + ")");
        } catch (Exception exception) {
            throw new RuntimeException(
                    String.format("Error encoding native filter '%s'.", filter.getNative()),
                    exception);
        }
        return data;
    }
}
