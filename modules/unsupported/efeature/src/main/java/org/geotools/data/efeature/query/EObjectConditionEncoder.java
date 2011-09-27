package org.geotools.data.efeature.query;

import static org.geotools.data.efeature.query.SpatialConditionEncoder.convert;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.Not;
import org.eclipse.emf.query.conditions.eobjects.EObjectCondition;
import org.eclipse.emf.query.conditions.eobjects.EObjectConditionAdapter;
import org.geotools.data.efeature.DataTypes;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.filter.Capabilities;
import org.geotools.filter.function.PropertyExistsFunction;
import org.geotools.filter.visitor.IsFullySupportedFilterVisitor;
import org.geotools.filter.visitor.IsSupportedFilterVisitor;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
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
import org.opengis.filter.temporal.During;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

/**
 * Encodes a {@link Filter} into an {@link EObjectCondition} statement.
 * <p>
 * This class is based on the org.geotools.data.jdbc.FilterToSQL source code by Chris Holmes (TOPP)
 * and Saul Farber (MassGIS)
 * 
 * @author kengu
 * 
 *
 * @source $URL$
 */
public class EObjectConditionEncoder implements FilterVisitor, ExpressionVisitor {
    
    /** {@link Logger} instance for this class */
    private Logger LOGGER = Logging.getLogger(EObjectConditionEncoder.class);

    /**
     * The srid of the schema, so the BBOX conforms.
     */
    private String srid;

    /**
     * Cached {@link SimpleFeatureType} instance
     */
    private SimpleFeatureType featureType;

    /**
     * The geometry attribute to use if none is specified.
     */
    // private String defaultGeom;

    /**
     * Cached {@link EFeatureInfo} instance. The following information is used:
     * <ol>
     * <li>Name of default geometry attribute, used if none is specified</li>
     * </ol>
     */
    private EFeatureInfo eFeatureInfo;

    /**
     * Whether the BBOX filter should be strict (using the exact geom), or loose (using the
     * envelopes)
     */
    private boolean looseBBox = false;

    /**
     * Cached {@link FilterCapabilities} supported by {@link EFeatureQuery}
     */
    private Capabilities capabilities;

    /**
     * Cached {@link EObjectCondition} built during encoding
     */
    private Condition eCondition;

    /**
     * Stack of {@link Condition}s waiting to be logically combined
     */
    private Stack<Condition> eConditionStack = new Stack<Condition>();

    /**
     * Stack of {@link Expression} evaluation results waiting to be combined into an
     * {@link EFeatureFilter} expression
     */
    private Stack<Object> eExpressionStack = new Stack<Object>();

    /**
     * {@link EObjectConditionEncoder} constructor
     * 
     * @param eFeatureInfo - {@link EFeatureInfo} instance
     * @param looseBBox -
     */
    public EObjectConditionEncoder(EFeatureInfo eFeatureInfo, boolean looseBBox) {
        this.eFeatureInfo = eFeatureInfo;
        this.srid = eFeatureInfo.getSRID();
        this.featureType = eFeatureInfo.getFeatureType();
        this.looseBBox = looseBBox;
    }

    public Capabilities getCapabilities() {
        if (capabilities == null) {
            capabilities = createFilterCapabilities();
        }
        return capabilities;
    }

    /**
     * Determines if specific filter passed in is supported.
     * 
     * @param filter - the Filter to be tested.
     * @return true if supported, false otherwise.
     * @see IsSupportedFilterVisitor
     */
    public boolean supports(Filter filter) {
        return getCapabilities().supports(filter);
    }

    /**
     * Determines if the filter and all its sub filters and expressions are supported.
     * <p>
     * 
     * @param filter - the filter to be tested.
     * @return true if all sub filters are supported, false otherwise.
     * @see IsFullySupportedFilterVisitor
     */
    public boolean fullySupports(Filter filter) {
        return getCapabilities().fullySupports(filter);
    }

    /**
     * Determines if the expression and all its sub expressions is supported.
     * <p>
     * 
     * @param filter the filter to be tested.
     * @return true if all sub filters are supported, false otherwise.
     * @see IsFullySupportedFilterVisitor
     */
    public boolean fullySupports(Expression expression) {
        return getCapabilities().fullySupports(expression);
    }

    /**
     * Gets whether the Filter.BBOX query will be strict and use an intersects or 'loose' and just
     * operate against the geometry envelopes.
     * 
     * @return <tt>true</tt> if this encoder is going to do loose filtering.
     */
    public boolean isLooseBBox() {
        return looseBBox;
    }

    /**
     * Sets whether the Filter.BBOX query should be 'loose', meaning that it should just do a
     * bounding box against the envelope. If set to <tt>false</tt> then the BBOX query will perform
     * a full intersects against the geometry, ensuring that it is exactly correct. If <tt>true</tt>
     * then the query will likely perform faster, but may not be exactly correct.
     * 
     * @param isLooseBBox - whether the BBOX should be loose or strict.
     */
    public void setLooseBBox(boolean isLooseBBox) {
        this.looseBBox = isLooseBBox;
    }

    public String getSRID() {
        return this.srid;
    }

    /**
     * Sets a spatial reference system ESPG number, so that the geometry can be properly encoded for
     * {@link EFeature}.
     * 
     * @param srid - the code for the spatial reference system.
     */
    public void setSRID(String srid) {
        this.srid = srid;
    }

    /**
     * Get feature type which EMF Queries are encoded for.
     * 
     * @return a {@link SimpleFeatureType} instance.
     */
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /**
     * Sets the {@link FeatureType} the encoder is encoding {@link EFeatureFilter} for.
     * <p>
     * This is used in the context for attribute expressions when encoding to
     * {@link EObjectCondition}s.
     * </p>
     * 
     * @param featureType - given {@link FeatureType} instance
     */
    public void setFeatureType(SimpleFeatureType featureType) {
        this.featureType = featureType;
    }

    /**
     * Encode given {@link Filter} into an {@link EObjectCondition}.
     * 
     * @param filter - the {@link Filter} to be encoded.
     * 
     * 
     * 
     * @throws EFeatureEncoderException If filter type not supported.
     */
    public EObjectCondition encode(Filter filter) throws EFeatureEncoderException {
        try {
            eCondition = null;
            eConditionStack.clear();
            Condition condition = (Condition)filter.accept(this, null);
            if(condition instanceof EObjectCondition) {
                eCondition = condition;
            } else {
                eCondition = new EObjectConditionAdapter(condition);
            }
        } catch (Exception ex) {
            LOGGER.warning("Unable to export filter" + ex);
            throw new EFeatureEncoderException("Problem writing filter: " + filter, ex);
        }
        return new EObjectConditionAdapter(eCondition);
    }

    /**
     * Encode given {@link Expression} into an {@link Condition}.
     * 
     * @param expression the {@link Expression} to be encoded.
     * 
     * @throws EFeatureEncoderException If filter type not supported.
     */
    public Condition encode(Expression expression) throws EFeatureEncoderException {
        try {
            eCondition = null;
            eConditionStack.clear();
            expression.accept(this, null);
        } catch (Exception ex) {
            LOGGER.warning("Unable to export filter" + ex);
            throw new EFeatureEncoderException("Problem writing expression: " + expression, ex);
        }
        return eCondition;
    }

    // ----------------------------------------------------- 
    //  FilterVisitor implementation
    // -----------------------------------------------------

    /**
     * Push {@link EObjectCondition#E_FALSE} to {@link EObjectCondition} stack
     * <p>
     * 
     * @param filter - the {@link Filter} instance to be visited
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @see {@link FilterVisitor#visit(ExcludeFilter, Object)}
     */
    @Override
    public Condition visit(ExcludeFilter filter, Object extraData) {
        eConditionStack.add(EObjectCondition.E_FALSE);
        return eConditionStack.peek();
    }

    /**
     * Push {@link EObjectCondition#E_TRUE} to {@link EObjectCondition} stack
     * <p>
     * 
     * @param filter - the {@link Filter} instance to be visited
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @see {@link FilterVisitor#visit(IncludeFilter, Object)}
     * 
     */
    @Override
    public Condition visit(IncludeFilter filter, Object extraData) {
        eConditionStack.add(EObjectCondition.E_TRUE);
        return eConditionStack.peek();
    }

    /**
     * Create EMF query compatible {@link EAttributeValueIsBetween BETWEEN condition} from given
     * {@link PropertyIsBetween BETWEEN filter} and push it to {@link EObjectCondition} stack.
     * <p>
     * Literal values {@link Number}, {@link java.util.Date} and {@link String} are supported. Any
     * other {@link Literal} value will throw an {@link RuntimeException}.
     * </p>
     * 
     * @param filter - the {@link Filter} instance to be visited
     * @param extraData - not in use
     * 
     * @return a {@link EAttributeValueIsBetween} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     */
    @Override
    public EAttributeValueIsBetween visit(PropertyIsBetween filter, Object extraData)
            throws RuntimeException {
        //
        // Get all expressions
        //
        Expression expr = filter.getExpression();
        Expression lowerbounds = filter.getLowerBoundary();
        Expression upperbounds = filter.getUpperBoundary();
        //
        // Get expression data type
        //
        Class<?> type;
        AttributeDescriptor attType = (AttributeDescriptor) expr.evaluate(featureType);
        if (attType != null) {
            type = attType.getType().getBinding();
        } else {
            //
            // Assume it's a string
            //
            type = String.class;
        }
        //
        // Check if type is not supported
        //
        if (!DataTypes.supports(type)) {
            throw new RuntimeException("Type " + type + " not supported.");
        }
        //
        // For each expression in filter:
        // 1) Build expression recursively and put onto expression stack
        // 2) Cast expression result to expected type
        //
        expr.accept(this, false);
        PropertyName name = toType(eExpressionStack.pop(), PropertyName.class,
                "PropertyIsBetween expression is not a PropertyName");
        lowerbounds.accept(this, type);
        Literal lower = toLiteral(eExpressionStack.pop(),
                "PropertyIsBetween lower bound expression is not a Literal");
        upperbounds.accept(this, type);
        Literal upper = toLiteral(eExpressionStack.pop(),
                "PropertyIsBetween upper bound expression is not a Literal");
        //
        // Get EAttribute instance from definition
        //
        EAttribute eAttribute = eFeatureInfo.eGetAttribute(name.getPropertyName());
        //
        // Found attribute?
        //
        if (eAttribute == null) {
            throw new IllegalArgumentException("EAttribute " + name.getPropertyName()
                    + " not found");
        }

        try {
            //
            // Create EObjectCondition instance and push it to condition stack
            //
            EAttributeValueIsBetween eCondition = new EAttributeValueIsBetween(eAttribute, lower,
                    upper);
            //
            // Push to stack
            //
            eConditionStack.push(eCondition);
            //
            // Finished
            //
            return eCondition;

        } catch (EFeatureEncoderException e) {
            throw new RuntimeException("Failed to create BETWEEN condition", e);
        }

    }

    /**
     * Create EMF query compatible {@link EAttributeValueIsLike LIKE condition} from given
     * {@link PropertyIsLike LIKE filter} and push it to {@link EObjectCondition} stack.
     * <p>
     * Only {@link String} literal values are supported. Any other {@link Literal} value will throw
     * a {@link RuntimeException}.
     * <p>
     * String pattern must comply with {@link Pattern} specification. An invalid pattern will throw
     * a {@link RuntimeException}.
     * </p>
     * 
     * @param filter - the LIKE Filter to be visited.
     * 
     * @return a {@link EAttributeValueIsLike} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public EAttributeValueIsLike visit(PropertyIsLike filter, Object extraData) {
        // Get LIKE clause information
        //
        // char esc = filter.getEscape().charAt(0);
        // char multi = filter.getWildCard().charAt(0);
        // char single = filter.getSingleChar().charAt(0);
        // boolean matchCase = filter.isMatchingCase();
        // TODO: Verify pattern expression

        // Get expression and literal
        //
        String pattern = filter.getLiteral();
        Expression expr = filter.getExpression();

        // Get expression data type
        //
        Class<?> type;
        AttributeDescriptor attType = (AttributeDescriptor) expr.evaluate(featureType);
        if (attType != null) {
            type = attType.getType().getBinding();
        } else {
            // assume it's a string
            type = String.class;
        }

        // Check if type is not supported
        //
        if (!DataTypes.supports(type)) {
            throw new RuntimeException("Type " + type + " not supported.");
        }

        // Build expression recursively and put onto expression stack
        //
        expr.accept(this, extraData);

        // Cast expression to PropertyName
        //
        PropertyName name = toType(eExpressionStack.pop(), PropertyName.class,
                "PropertyIsLike expression is not a PropertyName");

        // Get EAttribute instance from definition
        //
        EAttribute eAttribute = eFeatureInfo.eGetAttribute(name.getPropertyName());

        // Found attribute?
        //
        if (eAttribute == null) {
            throw new IllegalArgumentException("EAttribute " + name.getPropertyName()
                    + " not found");
        }

        try {
            // Create EObjectCondition instance and push it
            //
            EAttributeValueIsLike eCondition = new EAttributeValueIsLike(eAttribute, pattern);

            // Push to stack
            //
            eConditionStack.push(eCondition);

            // Finished
            //
            return eCondition;

        } catch (EFeatureEncoderException e) {
            throw new RuntimeException("Failed to create LIKE condition", e);
        }

    }

    /**
     * Create EMF Query compatible {@link Not <code>NOT</code> condition} from given Geotools
     * {@link org.opengis.filter.Not NOT filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(org.opengis.filter.Not filter, Object extraData) {
        //
        // Build filter recursively and put onto condition stack
        //
        filter.getFilter().accept(this,extraData);
        //
        // Invert condition
        //
        eCondition = new Not(eConditionStack.pop());
        //
        // Finished
        //
        return eCondition;
    }

    /**
     * Create EMF Query compatible {@link Condition#AND(Condition) AND condition}  from given
     * Geotools {@link org.opengis.filter.And AND filter} and push it to {@link EObjectCondition}
     * stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     */
    @Override
    public Condition visit(org.opengis.filter.And filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, "AND");
    }

    /**
     * Create EMF Query compatible {@link Condition#OR(Condition) OR condition}  from given Geotools
     * {@link org.opengis.filter.Or OR filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(org.opengis.filter.Or filter, Object extraData) {
        return visit((BinaryLogicOperator) filter, "OR");
    }

    /**
     * Common implementation for {@link BinaryLogicOperator binary logic operator} filters. This way
     * they're all handled centrally.
     * <p>
     * This method iterate over all filter children, concatenating them into a single EMF Query
     * {@link Condition logical operator condition} and push it to {@link EObjectCondition} stack.
     * <p>
     * Only {@link Not NOT}, {@link Condition#OR(Condition) OR} and {@link Condition#OR(Condition)
     * AND} operator filters are supported. Any other {@link BinaryLogicOperator binary logic
     * operator} filter instance will throw a {@link RuntimeException}.
     * </p>
     * 
     * @param filter - the logic statement.
     * @param extraData - extra filter data. Not modified directly by this method.
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    protected Condition visit(BinaryLogicOperator filter, Object extraData) {

        // Initialize
        //
        Condition eCondition = null;

        // Get name logic operator
        //
        String operator = ((String) extraData).toUpperCase();

        // Get filter iterator
        //
        Iterator<Filter> list = filter.getChildren().iterator();

        // Is inverse operator?
        //
        if ("OR".equals(operator)) {
            while (list.hasNext()) {

                // Build filter recursively and put onto condition stack
                //
                list.next().accept(this, extraData);

                // Initialize?
                //
                if (eCondition == null) {
                    eCondition = eConditionStack.pop();
                } else {
                    eCondition = eCondition.OR(eConditionStack.pop());
                }

            }
        } else if ("AND".equals(operator)) {
            while (list.hasNext()) {

                // Build filter recursively and put onto condition stack
                //
                list.next().accept(this, extraData);

                // Initialize?
                //
                if (eCondition == null) {
                    eCondition = eConditionStack.pop();
                } else {
                    eCondition = eCondition.AND(eConditionStack.pop());
                }

            }
        } else {
            throw new RuntimeException("Binary logical " + "operator " + operator
                    + " filter not supported");
        }

        // No condition created?
        //
        if (eCondition == null) {
            throw new NullPointerException("Binary logical " + "operator " + operator
                    + " filter not supported");
        }

        // Push concatenated condition to stack
        //
        eConditionStack.push(eCondition);

        // Finished
        //
        return eCondition;
    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsEqual EQ (==) condition}  from given
     * Geotools {@link PropertyIsEqualTo EQ filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     */
    @Override
    public Condition visit(PropertyIsEqualTo filter, Object extraData) {
        return visitBinaryComparisonOperator(filter, PropertyIsEqualTo.NAME);
    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsGreaterEqual GE (>=) condition}  from
     * given Geotools {@link PropertyIsGreaterThanOrEqualTo GE filter} and push it to
     * {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return visitBinaryComparisonOperator(filter, PropertyIsGreaterThanOrEqualTo.NAME);
    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsGreaterThan GE (>) condition}  from given
     * Geotools {@link PropertyIsGreaterThan GT filter} and push it to {@link EObjectCondition}
     * stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(PropertyIsGreaterThan filter, Object extraData) {
        return visitBinaryComparisonOperator(filter, PropertyIsGreaterThan.NAME);
    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsLessThan LE (<) condition}  from given
     * Geotools {@link PropertyIsLessThan LT filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(PropertyIsLessThan filter, Object extraData) {
        return visitBinaryComparisonOperator(filter, PropertyIsLessThan.NAME);
    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsLessEqual LE (<=) condition}  from given
     * Geotools {@link PropertyIsLessThanOrEqualTo LE filter} and push it to
     * {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return visitBinaryComparisonOperator(filter, PropertyIsLessThanOrEqualTo.NAME);
    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsNotEqual NE (!=) condition }  from given
     * Geotools {@link PropertyIsNotEqualTo NE filter} and push it to {@link EObjectCondition}
     * stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        visitBinaryComparisonOperator(filter, PropertyIsNotEqualTo.NAME);
        return extraData;
    }

    /**
     * Common implementation for {@link BinaryComparisonOperator binary comparison operator}
     * filters. This way they're all handled centrally.
     * <p>
     * This method iterate over all filter children, concatenating them into a single EMF Query
     * {@link Condition logical comparison condition} and push it to {@link EObjectCondition} stack.
     * <p>
     * Only {@link EAttributeValueIsEqual EQ (==)}, {@link EAttributeValueIsNotEqual NE (>)},
     * {@link EAttributeValueIsLessThan LT (<)}, {@link EAttributeValueIsLessThan LE (<=)},
     * {@link EAttributeValueIsGreaterThan GT (>)} and {@link EAttributeValueIsLessThan GE (>=)}
     * logical comparison filters are supported. Any other {@link BinaryLogicOperator binary logic
     * operator} filter instance will throw a {@link RuntimeException}.
     * </p>
     * 
     * @param filter - the logic statement.
     * @param extraData - extra filter data. Not modified directly by this method.
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    protected Condition visitBinaryComparisonOperator(BinaryComparisonOperator filter,
            Object extraData) throws RuntimeException {
        // LOGGER.finer("exporting SQL ComparisonFilter");
        //
        // Initialize
        //
        Literal value = null;
        String name = null;
        Condition eCondition = null;
        //
        // Get left and right expression
        //
        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();
        //
        // Detect functions
        //
        if(left instanceof Function || right instanceof Function) {
            throw new IllegalArgumentException("Functions are not supported");            
        }
        //
        // Detect implicit inner join
        //
        if (left instanceof PropertyName && right instanceof PropertyName) {
            throw new IllegalArgumentException("Implicit inner joint predicates not supported");
        }

        // Verify left expression
        //
        if (left instanceof PropertyName) {
            name = ((PropertyName) left).getPropertyName();
        } else if (left instanceof PropertyExistsFunction) {
            name = ((PropertyExistsFunction) left).getParameters().get(0).toString();
        } else if (left instanceof Literal) {
            value = (Literal) left;
        } else {
            throw new IllegalArgumentException("Left argument must be a Literal");
        }
        //
        // Verify right expression
        //
        if (right instanceof PropertyName) {
            name = ((PropertyName) right).getPropertyName();
        } else if (right instanceof PropertyExistsFunction) {
            name = ((PropertyExistsFunction) right).getParameters().get(0).toString();
        } else if (right instanceof Literal) {
            value = (Literal) right;
        } else {
            throw new IllegalArgumentException("Right argument must be a Literal");
        }

        // Get EAttribute instance from definition
        //
        EAttribute eAttribute = eFeatureInfo.eGetAttribute(name);

        // Found attribute?
        //
        if (eAttribute == null) {
            throw new IllegalArgumentException("EAttribute " + name + " not found");
        }

        // Get comparator
        //
        String comparator = (String) extraData;

        try {
            // Create comparator condition
            //
            if (PropertyIsEqualTo.NAME.equals(comparator)) {
                eCondition = new EAttributeValueIsEqual(eAttribute, value);
            } else if (PropertyIsLessThan.NAME.equals(comparator)) {
                eCondition = new EAttributeValueIsEqual(eAttribute, value);
            } else if (PropertyIsLessThanOrEqualTo.NAME.equals(comparator)) {
                eCondition = new EAttributeValueIsEqual(eAttribute, value);
            } else if (PropertyIsGreaterThan.NAME.equals(comparator)) {
                eCondition = new EAttributeValueIsEqual(eAttribute, value);
            } else if (PropertyIsGreaterThanOrEqualTo.NAME.equals(comparator)) {
                eCondition = new EAttributeValueIsEqual(eAttribute, value);
            } else if (PropertyIsNotEqualTo.NAME.equals(comparator)) {
                eCondition = new EAttributeValueIsEqual(eAttribute, value);
            } else {
                throw new IllegalArgumentException("Comparator " + comparator + " not supported");
            }
        } catch (EFeatureEncoderException e) {
            String msg = "Failed to create " + "EObjectCondition (" + comparator + ")";
            LOGGER.warning(msg);
            throw new IllegalArgumentException(msg, e);
        }

        // Push concatenated condition to stack
        //
        eConditionStack.push(eCondition);

        // Finished
        //
        return eCondition;

    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsNull IS NULL condition}  from given
     * Geotools {@link PropertyIsNull IS NULL filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link EAttributeValueIsNull} instance
     * 
     * @throws RuntimeException If one expression is not supported
     * 
     */
    @Override
    public EAttributeValueIsNull visit(PropertyIsNull filter, Object extraData)
            throws RuntimeException {
        // LOGGER.finer("exporting NullFilter");

        // Get filter expression
        //
        Expression expr = filter.getExpression();

        // Build expression recursively and put onto expression stack
        //
        expr.accept(this, extraData);

        // Get PropertyName instance
        //
        PropertyName name = toType(eExpressionStack.pop(), PropertyName.class,
                "PropertyIsNull expression is not a PropertyName");

        // Get EAttribute instance from definition
        //
        EAttribute eAttribute = eFeatureInfo.eGetAttribute(name.getPropertyName());

        // Found attribute?
        //
        if (eAttribute == null) {
            throw new IllegalArgumentException("EAttribute " + name.getPropertyName()
                    + " not found");
        }

        // Create EObjectCondition instance and push to stack
        //
        EAttributeValueIsNull eCondition = new EAttributeValueIsNull(eAttribute);

        // Push to stack
        //
        eConditionStack.push(eCondition);

        // Finished
        //
        return eCondition;

    }

    /**
     * Create EMF Query compatible {@link EAttributeValueIsID ID condition}  from given Geotools
     * {@link Id ID filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @param filter - the {@link Id identifier} filter
     * 
     * @return a {@link EAttributeValueIsID} instance
     * 
     * @throws RuntimeException - if no or more than one {@link Identifier identifier}s are given.
     * 
     */
    @Override
    public EAttributeValueIsID visit(Id filter, Object extraData) {

        // Get identifiers
        //
        Set<Identifier> ids = filter.getIdentifiers();
        //
        // LOGGER.finer("Exporting FID=" + ids);
        //
        // Verify that only one identifier is given
        //
        if (ids.isEmpty()) {
            throw new RuntimeException("Id filter " + filter + " has no identifier");
        } else if (ids.size() > 1) {
            throw new RuntimeException("Id filter " + filter + " has more than one identifier");
        }
        //
        // Get ID
        //
        Object id = ids.iterator().next().getID();
        //
        // Is ID invalid?
        //
        if (id == null) {
            throw new NullPointerException("Identifier is null");
        }
        //
        // Get ID EAttribute instance from definition
        //
        EAttribute eAttribute = eFeatureInfo.eIDAttribute();

        try {
            // Create EObjectCondition instance and push it
            //
            EAttributeValueIsID eCondition = new EAttributeValueIsID(eAttribute, id.toString());
            //
            // Push to stack
            //
            eConditionStack.push(eCondition);
            //
            // Finished
            //
            return eCondition;

        } catch (EFeatureEncoderException e) {
            throw new RuntimeException("Failed to create IS ID condition", e);
        }
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueBBox BBOX condition}  from given Geotools
     * {@link BBOX BBOX filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(BBOX filter, Object extraData) {
        return visitBinarySpatialOperator(filter, BBOX.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueBeyond BEYOND condition}  from given
     * Geotools {@link Beyond BEYOND filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Beyond filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Beyond.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueContains CONTAINS condition}  from given
     * Geotools {@link Contains CONTAINS filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Contains filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Contains.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueCrosses CROSSES condition}  from given
     * Geotools {@link Crosses CROSSES filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Crosses filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Crosses.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueDisjoint DISJOINT condition}  from given
     * Geotools {@link Disjoint DISJOINT filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Disjoint filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Disjoint.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueDWithin DWITHIN condition}  from given
     * Geotools {@link DWithin DWITHIN filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(DWithin filter, Object extraData) {
        return visitBinarySpatialOperator(filter, DWithin.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueEquals EQUALS condition}  from given
     * Geotools {@link Equals EQUALS filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Equals filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Equals.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueIntersects INTERSECTS condition}  from given
     * Geotools {@link Intersects INTERSECTS filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Intersects filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Intersects.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueOverlaps OVERLAPS condition}  from given
     * Geotools {@link Overlaps OVERLAPS filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Overlaps filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Overlaps.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueTouches TOUCHES condition}  from given
     * Geotools {@link Touches TOUCHES filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Object visit(Touches filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Touches.NAME);
    }

    /**
     * Create EMF Query compatible {@link EGeometryValueWithin WITHIN condition}  from given
     * Geotools {@link Within WITHIN filter} and push it to {@link EObjectCondition} stack.
     * 
     * @param filter - the filter to visit
     * @param extraData - not in use
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException If one or more expressions are not supported
     * 
     */
    @Override
    public Condition visit(Within filter, Object extraData) {
        return visitBinarySpatialOperator(filter, Within.NAME);
    }

    /**
     * Common implementation for {@link BinarySpatialOperator binary spatial operator} filters. This
     * way they're all handled centrally.
     * <p>
     * This method extracts property name, geometry literal and descriptor and evaluates if property
     * name and geometry literal arguments are swapped.
     * </p>
     * 
     * @param filter - binary spatial operator statement
     * @param name - operator name
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException - if one or more expressions are not supported
     * 
     */
    protected Condition visitBinarySpatialOperator(BinarySpatialOperator filter, String name) {
        // Perform some sanity checks
        //
        if (filter == null) {
            throw new NullPointerException("Filter to be encoded cannot be null");
        }

        // Extract the property name and the geometry literal
        //
        Literal geometry;
        PropertyName property;
        boolean swapped = false;
        BinaryComparisonOperator op = (BinaryComparisonOperator) filter;
        if (op.getExpression1() instanceof PropertyName && op.getExpression2() instanceof Literal) {
            property = (PropertyName) op.getExpression1();
            geometry = (Literal) op.getExpression2();
            swapped = true;
        } else if (op.getExpression2() instanceof PropertyName
                && op.getExpression1() instanceof Literal) {
            property = (PropertyName) op.getExpression2();
            geometry = (Literal) op.getExpression1();
        } else {
            throw new IllegalArgumentException("Can only encode spatial filters that do "
                    + "compare a property name and a geometry");
        }

        // Get EAttribute instance
        //
        EAttribute eAttribute = eFeatureInfo.eGetAttribute(property.getPropertyName());

        // Going through evaluate ensures we get the proper result even
        // if the name has not been specified (convention -> the default geometry)
        AttributeDescriptor descriptor = (AttributeDescriptor) property.evaluate(featureType);
        if (descriptor instanceof GeometryDescriptor) {

            // Forward
            //
            return visitBinarySpatialOperator(filter, name, eAttribute,
                    (GeometryDescriptor) descriptor, geometry, swapped);

        } else if(descriptor == null){
            throw new IllegalArgumentException("Attribute '" + 
                    property.getPropertyName() + "' not found in '" + 
                    featureType.getTypeName() + "'");
        } else {
            throw new IllegalArgumentException("Attribute '" + 
                    property.getPropertyName() + "' is not a geometry in " + 
                    featureType.getTypeName() + "'");
        }

    }

    /**
     * Common implementation for {@link BinarySpatialOperator binary spatial operator} filters. This
     * way they're all handled centrally.
     * <p>
     * This method extracts property name, geometry literal and descriptor and evaluates if property
     * name and geometry literal arguments are swapped.
     * </p>
     * 
     * @param filter - the binary spatial operator statement
     * @param name - operator name
     * @param property - property name expression
     * @param geometry - geometry literal expression
     * @param swapped - true if geometry literal is left expression (first)
     * @param eAttribute - EAttribute instance
     * @param descriptor - geometry descriptor
     * 
     * @return a {@link Condition} instance
     * 
     * @throws RuntimeException - if one or more expressions are not supported
     * 
     */
    protected Condition visitBinarySpatialOperator(BinarySpatialOperator filter, String name,
            EAttribute eAttribute, GeometryDescriptor descriptor, Literal geometry, boolean swapped) {
        try {
            if (BBOX.NAME.equals(name)) {
                return new EGeometryValueBBox(eAttribute, geometry, swapped);
            } else if (Beyond.NAME.equals(name)) {
                double distance = convert((Beyond) filter, descriptor);
                return new EGeometryValueBeyond(eAttribute, geometry, distance);
            } else if (DWithin.NAME.equals(name)) {
                double distance = convert((DWithin) filter, descriptor);
                return new EGeometryValueDWithin(eAttribute, geometry, distance);
            } else if (Contains.NAME.equals(name)) {
                return new EGeometryValueContains(eAttribute, geometry, swapped);
            } else if (Crosses.NAME.equals(name)) {
                return new EGeometryValueCrosses(eAttribute, geometry, swapped);
            } else if (Disjoint.NAME.equals(name)) {
                return new EGeometryValueDisjoint(eAttribute, geometry, swapped);
            } else if (Equals.NAME.equals(name)) {
                return new EGeometryValueEquals(eAttribute, geometry, swapped);
            } else if (Intersects.NAME.equals(name)) {
                return new EGeometryValueIntersects(eAttribute, geometry, swapped);
            } else if (Overlaps.NAME.equals(name)) {
                return new EGeometryValueOverlaps(eAttribute, geometry, swapped);
            } else if (Touches.NAME.equals(name)) {
                return new EGeometryValueTouches(eAttribute, geometry, swapped);
            } else if (Within.NAME.equals(name)) {
                return new EGeometryValueWithin(eAttribute, geometry, swapped);
            }
        } catch (Exception e) {
            throw new RuntimeException("Spatial operation " + name + " is not supported", e);
        }

        // Not supported, throw exception
        //
        throw new IllegalArgumentException("Spatial operation " + name + " is not supported");
    }

    @Override
    public Object visitNullFilter(Object extraData) {
        return extraData;
    }

    // ----------------------------------------------------- 
    //  ExpressionVisitor
    // -----------------------------------------------------

    /**
     * Validate property against current {@link #getFeatureType() feature} and push it or the
     * {@link AttributeDescriptor} to the {@link Expression} stack.
     * 
     * @param expression - the expression to visit
     * @param toAttribute - if {@link Boolean#TRUE TRUE}, push and return
     *        {@link AttributeDescriptor} found in current {@link #getFeatureType() feature type},
     *        otherwise return {@link PropertyName} instance.
     * 
     * @return a {@link AttributeDescriptor} or {@link PropertyName} instance depending on
     *         'toAttribute' flag
     * 
     * @throws RuntimeException If {@link #getFeatureType()} does not define a
     *         {@link AttributeDescriptor} with given property name.
     */
    @Override
    public Object visit(PropertyName expression, Object toAttribute) throws RuntimeException {

        // LOGGER.finer("exporting PropertyName");
        //
        // Get parse flag
        //
        boolean bFlag = (toAttribute instanceof Boolean ? (Boolean) toAttribute : false);
        //
        // First evaluate expression against feature type
        // get the attribute, this handles xpath
        //
        AttributeDescriptor attribute = null;
        try {
            attribute = (AttributeDescriptor) expression.evaluate(featureType);
        } catch (Exception e) {
            // Property name not supported
            //
            throw new IllegalArgumentException("Error occured mapping " + expression
                    + " to feature type", e);
        }
        // Verify
        //
        if (attribute == null) {
            throw new NullPointerException("Attribute with name " +
            		"[" + expression.getPropertyName() + "] not found");
        }

        // Push to stack
        //
        eExpressionStack.push((bFlag ? attribute : expression));

        // Finished
        //
        return eExpressionStack.peek();

    }

    /**
     * Get literal or it's value.
     * <p>
     * 
     * @param expression - the expression to visit
     * @param toValue - if {@link Boolean#TRUE TRUE}, push and return {@link Literal#getValue()
     *        literal value}, otherwise push and return the {@link Literal} itself.
     * 
     * @return a {@link Object value} or the {@link Literal itself} instance depending on 'toValue'
     *         flag
     * 
     */
    @Override
    public Object visit(Literal expression, Object toValue) {

        // Initialize
        //
        Object literal = expression;

        // Get parse flag
        //
        boolean bFlag = (toValue instanceof Boolean ? (Boolean) toValue : false);

        // Get literal value?
        //
        if (bFlag) {
            // Get literal value
            //
            literal = expression.evaluate(null);

            // If that failed, grab the value as is
            //
            if (literal == null) {
                literal = expression.getValue();
            }

        } else if (literal == null) {
            literal = Expression.NIL;
        }

        // Push to stack
        //
        eExpressionStack.push(literal);

        // Finished
        //
        return literal;

    }

    /**
     * Binary expression ADD is not supported.
     */
    @Override
    public Object visit(Add expression, Object extraData) {
        throw new IllegalArgumentException("Binary expression ADD is not supported");
        // return visit((BinaryExpression)expression, "+", extraData);
    }

    /**
     * Binary expression DIVIDE is not supported.
     */
    @Override
    public Object visit(Divide expression, Object extraData) {
        throw new IllegalArgumentException("Binary expression DIVIDE is not supported");
        // return visit((BinaryExpression)expression, "/", extraData);
    }

    /**
     * Binary expression MULTIPLY is not supported.
     */
    @Override
    public Object visit(Multiply expression, Object extraData) {
        throw new IllegalArgumentException("Binary expression MULTIPLY is not supported");
        // return visit((BinaryExpression)expression, "*", extraData);
    }

    /**
     * Binary expression SUBSTRACT is not supported.
     */
    @Override
    public Object visit(Subtract expression, Object extraData) {
        throw new IllegalArgumentException("Binary expression SUBSTRACT is not supported");
        // return visit((BinaryExpression)expression, "-", extraData);
    }

    // /**
    // * Writes the SQL for the Math Expression.
    // *
    // * @param expression the Math phrase to be written.
    // * @param operator The operator of the expression.
    // *
    // * @throws RuntimeException for io problems
    // */
    // protected Object visit(BinaryExpression expression, String operator, Object extraData) throws
    // RuntimeException {
    // LOGGER.finer("exporting Expression Math");
    //
    // try {
    // expression.getExpression1().accept(this, extraData);
    // out.write(" " + operator + " ");
    // expression.getExpression2().accept(this, extraData);
    // } catch (java.io.IOException ioe) {
    // throw new RuntimeException("IO problems writing expression", ioe);
    // }
    // return extraData;
    // }

    /**
     * {@link Function}s are not supported.
     */
    @Override
    public Object visit(Function function, Object extraData) throws RuntimeException {

        throw new IllegalArgumentException("Functions are not not supported");

        // try {
        // List<Expression> parameters = function.getParameters();
        // List contexts = null;
        // //check context, if a list which patches parameter size list assume its context
        // // to pass along to each Expression for encoding
        // if( extraData instanceof List && ((List)extraData).size() == parameters.size() ) {
        // contexts = (List) extraData;
        // }
        //
        // //set the encoding function flag to signal we are inside a function
        // encodingFunction = true;
        //
        // //write the name
        // out.write( function.getName() );
        //
        // //write the arguments
        // out.write( "(");
        // for ( int i = 0; i < parameters.size(); i++ ) {
        // Expression e = parameters.get( i );
        //
        // Object context = contexts != null ? contexts.get( i ) : extraData;
        // e.accept(this, context);
        //
        // if ( i < parameters.size()-1 ) {
        // out.write( ",");
        // }
        //
        // }
        // out.write( ")");
        //
        // //reset the encoding function flag
        // encodingFunction = false;
        // }
        // catch (IOException e) {
        // throw new RuntimeException( e );
        // }
        //
        // return extraData;
    }

    @Override
    public NilExpression visit(NilExpression expression, Object toValue) {

        // Get parse flag
        //
        boolean bFlag = (toValue instanceof Boolean ? (Boolean) toValue : false);

        return bFlag ? null : expression;
    }

    // private void encodeGeomFilter(BinarySpatialOperator filter, String function, String
    // comparison, boolean useIndex) {
    //
    // // This method blindly assumes that the filter is supported
    // //
    // Expression left = filter.getExpression1();
    // Expression right = filter.getExpression2();
    //
    // try {
    // //should we use the index?
    // if (useIndex) {
    // encodeExpression(left);
    // out.write(" && ");
    // encodeExpression(right);
    // }
    //
    // // looseBbox only applies to GEOMETRY_BBOX, so unless this is a
    // // BBOX, we will always generate the full SQL.
    // if (filter.getFilterType() != AbstractFilter.GEOMETRY_BBOX || !looseBBox) {
    // if (useIndex) {
    // out.write(" AND ");
    // }
    // out.write(function + "(");
    // encodeExpression(left);
    // out.write(", ");
    // encodeExpression(right);
    // out.write(")" + comparison);
    // }
    // } catch (IOException ex) {
    // EFeaturePlugin.warn("Unable to export filter", ex);
    // }
    // }
    //
    // private void encodeExpression(Expression expr) throws IOException {
    // if (expr == null) {
    // out.write("\"" + defaultGeom + "\"");
    // } else {
    // expr.accept(this,null);
    // }
    // }

    // /**
    // * Turns a geometry filter into the postgis sql bbox statement.
    // *
    // * @param filter the geometry filter to be encoded.
    // *
    // * @throws RuntimeException for IO exception (need a better error)
    // */
    // public void visit(BinarySpatialOperator filter) throws RuntimeException {
    //
    // //LOGGER.finer("exporting GeometryFilter");
    //
    // short filterType = filter.getFilterType();
    // DefaultExpression left = (DefaultExpression) filter.getLeftGeometry();
    // DefaultExpression right = (DefaultExpression) filter.getRightGeometry();
    //
    //
    //
    // // Figure out if we need to constrain this query with the && constraint.
    // int literalGeometryCount = 0;
    //
    // if ((left != null)
    // && (left.getType() == DefaultExpression.LITERAL_GEOMETRY)) {
    // literalGeometryCount++;
    // }
    //
    // if ((right != null)
    // && (right.getType() == DefaultExpression.LITERAL_GEOMETRY)) {
    // literalGeometryCount++;
    // }
    //
    // boolean constrainBBOX = (literalGeometryCount == 1);
    // boolean onlyBbox = filterType == AbstractFilter.GEOMETRY_BBOX
    // && looseBBox;
    //
    // try {
    //
    // // DJB: disjoint is not correctly handled in the pre-march 22/05
    // // version
    // // I changed it to not do a "&&" index search for disjoint because
    // // Geom1 and Geom2 can have a bbox overlap and be disjoint
    // // I also added test case.
    // // NOTE: this will not use the index, but its unlikely that using
    // // the index
    // // for a disjoint query will be the correct thing to do.
    //
    // // DJB NOTE: need to check for a NOT(A intersects G) filter
    // // --> NOT( (A && G) AND intersects(A,G))
    // // and check that it does the right thing.
    //
    // constrainBBOX = constrainBBOX
    // && (filterType != AbstractFilter.GEOMETRY_DISJOINT);
    //
    // if (constrainBBOX) {
    // encodeExpression(left);
    // out.write(" && ");
    // encodeExpression(right);
    //
    // if (!onlyBbox) {
    // out.write(" AND ");
    // }
    // }
    //
    // String closingParenthesis = ")";
    //
    // if (!onlyBbox) {
    // if (filterType == AbstractFilter.GEOMETRY_EQUALS) {
    // out.write("equals");
    // } else if (filterType == AbstractFilter.GEOMETRY_DISJOINT) {
    // out.write("NOT (intersects");
    // closingParenthesis += ")";
    // } else if (filterType == AbstractFilter.GEOMETRY_INTERSECTS) {
    // out.write("intersects");
    // } else if (filterType == AbstractFilter.GEOMETRY_CROSSES) {
    // out.write("crosses");
    // } else if (filterType == AbstractFilter.GEOMETRY_WITHIN) {
    // out.write("within");
    // } else if (filterType == AbstractFilter.GEOMETRY_CONTAINS) {
    // out.write("contains");
    // } else if (filterType == AbstractFilter.GEOMETRY_OVERLAPS) {
    // out.write("overlaps");
    // } else if (filterType == AbstractFilter.GEOMETRY_BBOX) {
    // out.write("intersects");
    // } else if (filterType == AbstractFilter.GEOMETRY_TOUCHES) {
    // out.write("touches");
    // } else {
    // // this will choke on beyond and dwithin
    // throw new RuntimeException("does not support filter type "
    // + filterType);
    // }
    // out.write("(");
    //
    // encodeExpression(left);
    // out.write(", ");
    // encodeExpression(right);
    //
    // out.write(closingParenthesis);
    // }
    // } catch (java.io.IOException ioe) {
    // LOGGER.warning("Unable to export filter" + ioe);
    // throw new RuntimeException("io error while writing", ioe);
    // }
    // }
    //
    // public void visit(PropertyIsLike filter) throws UnsupportedOperationException {
    // char esc = filter.getEscape().charAt(0);
    // char multi = filter.getWildcardMulti().charAt(0);
    // char single = filter.getWildcardSingle().charAt(0);
    // String pattern = LikeFilterImpl.convertToSQL92(esc,multi,single,filter.getPattern());
    //
    // DefaultExpression att = (DefaultExpression) filter.getValue();
    //
    // try {
    // out.write( " ( " );
    // att.accept(this);
    //
    // out.write(" LIKE '");
    // out.write(pattern);
    // out.write("' ");
    //
    // //JD: this is an ugly ugly hack!! hopefully when the new feature model is around we can
    // // fix this
    // //check for context for a date
    // if ( att instanceof AttributeExpression && context != null
    // && java.util.Date.class.isAssignableFrom( context ) ) {
    // //if it is a date, add additional logic for a timestamp, or a timestamp with
    // // timezone
    // out.write( " OR " );
    // att.accept( this );
    // out.write( " LIKE '" );
    // out.write(pattern + " __:__:__'" ); //timestamp
    //
    // out.write( " OR " );
    // att.accept( this );
    // out.write( " LIKE '" );
    // out.write(pattern + " __:__:_____'" ); //timestamp with time zone
    // }
    //
    // out.write( " ) " );
    //
    // } catch (java.io.IOException ioe) {
    // throw new RuntimeException(IO_ERROR, ioe);
    // }
    //
    //
    // }
    //
    // /**
    // * Writes the SQL for a Compare Filter.
    // *
    // * DJB: note, postgis overwrites this implementation because of the way
    // * null is handled. This is for <PropertyIsNull> filters and <PropertyIsEqual> filters
    // * are handled. They will come here with "property = null".
    // * NOTE:
    // * SELECT * FROM <table> WHERE <column> isnull; -- postgresql
    // * SELECT * FROM <table> WHERE isnull(<column>); -- oracle???
    // *
    // * @param filter - the comparison filter.
    // *
    // * @throws RuntimeException for io exception with writer
    // */
    // public void visit(BinaryComparisonOperator filter) throws RuntimeException {
    // //LOGGER.finer("exporting SQL ComparisonFilter");
    //
    // DefaultExpression left = (DefaultExpression) filter.getLeftValue();
    // DefaultExpression right = (DefaultExpression) filter.getRightValue();
    // LOGGER.finer("Filter type id is " + filter.getFilterType());
    // LOGGER.finer("Filter type text is "
    // + comparisions.get(new Integer(filter.getFilterType())));
    //
    // String type = (String) comparisions.get(new Integer(
    // filter.getFilterType()));
    //
    // try {
    // // a bit hacky, but what else can we really do?
    // if ( (right == null) && (filter.getFilterType()==FilterType.COMPARE_EQUALS ) )
    // {
    // left.accept(this);
    // out.write(" isnull");
    // }
    // else
    // {
    // //check for case insentivity (TODO: perhaps move this up to jdbc)
    // if ( !filter.isMatchingCase() ) {
    // //only for == or !=
    // if ( filter.getFilterType() == Filter.COMPARE_EQUALS ||
    // filter.getFilterType() == Filter.COMPARE_NOT_EQUALS ) {
    //
    // //only for strings
    // if ( left.getType() == Expression.LITERAL_STRING
    // || right.getType() == Expression.LITERAL_STRING ) {
    //
    // out.write( "lower(" ); left.accept( this ); out.write( ")");
    // out.write( " " + type + " " );
    // out.write( "lower(" ); right.accept( this ); out.write( ")");
    //
    // return;
    // }
    // }
    // }
    //
    // //normal execution
    // left.accept(this);
    // out.write(" " + type + " ");
    // right.accept(this);
    // }
    // } catch (java.io.IOException ioe) {
    // throw new RuntimeException(IO_ERROR, ioe);
    // }
    // }

    // ----------------------------------------------------- 
    //  Private helper methods
    // -----------------------------------------------------
    
    
    @Override
    public Object visit(After arg0, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(AnyInteracts expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(Before expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(Begins expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(BegunBy expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(During expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(EndedBy expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(Ends expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(Meets expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(MetBy expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(OverlappedBy expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(TContains expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(TEquals expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(TOverlaps expression, Object extraData) {
        // TODO Auto-generated method stub
        return null;
    }    
    
    // ----------------------------------------------------- 
    //  Private helper methods
    // -----------------------------------------------------
    
    private static <T> T toType(Object object, Class<T> type, String message) {
        if (!type.isInstance(object)) {
            throw new IllegalArgumentException(message);
        }
        return type.cast(object);
    }
    
    private static Literal toLiteral(Object object, String message) {
        return toType(object,Literal.class,message);
    }
    
    private static Capabilities createFilterCapabilities() {

        // Create Capabilities helper class
        //
        Capabilities capabilities = new Capabilities();

        // Add EFeature ID filter capability
        //
        capabilities.addType(Id.class);

        // Add Include All filter capability
        //
        capabilities.addType(IncludeFilter.class);

        // Add Exclude All filter capability
        //
        capabilities.addType(ExcludeFilter.class);

        // Add logical operators (AND, OR, NOT)
        //
        capabilities.addAll(Capabilities.LOGICAL);

        // Add simple comparators
        //
        capabilities.addAll(Capabilities.SIMPLE_COMPARISONS);

        // Add special comparators
        //
        capabilities.addName(PropertyIsNull.NAME);

        // Add spatial operations
        //
        capabilities.addName(BBOX.NAME);
        capabilities.addName(Equals.NAME);
        capabilities.addName(Disjoint.NAME);
        capabilities.addName(Intersects.NAME);
        capabilities.addName(Touches.NAME);
        capabilities.addName(Crosses.NAME);
        capabilities.addName(Within.NAME);
        capabilities.addName(Contains.NAME);
        capabilities.addName(Overlaps.NAME);
        capabilities.addName(Beyond.NAME);
        capabilities.addName(DWithin.NAME);

        // Finished
        //
        return capabilities;

    }

}
