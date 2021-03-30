/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mongodb;

import static org.geotools.util.Converters.convert;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;
import org.geotools.data.mongodb.complex.JsonSelectAllFunction;
import org.geotools.data.mongodb.complex.JsonSelectFunction;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.And;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
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
import org.opengis.filter.PropertyIsNil;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
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
 * Abstract visitor responsible for generating a BasicDBObject to use as a MongoDB query.
 *
 * @author Gerald Gay, Data Tactics Corp.
 * @author Alan Mangan, Data Tactics Corp.
 * @author Tom Kunicki, Boundless Spatial Inc. (C) 2011, Open Source Geospatial Foundation (OSGeo)
 * @see The GNU Lesser General Public License (LGPL)
 */
public abstract class AbstractFilterToMongo implements FilterVisitor, ExpressionVisitor {

    protected final MongoGeometryBuilder geometryBuilder;

    /** The whole world in WGS84 */
    private static final Envelope WORLD = new Envelope(-179.99, 179.99, -89.99, 89.99);

    public AbstractFilterToMongo(MongoGeometryBuilder builder) {
        this.geometryBuilder = new MongoGeometryBuilder();
    }

    public AbstractFilterToMongo() {
        this(new MongoGeometryBuilder());
    }

    protected BasicDBObject asDBObject(Object extraData) {
        if (extraData instanceof BasicDBObject) {
            return (BasicDBObject) extraData;
        }
        return new BasicDBObject();
    }

    //
    // primitives
    //
    @Override
    public Object visit(Literal expression, Object extraData) {
        Class<?> targetType = null;
        if (extraData != null && extraData instanceof Class) {
            targetType = (Class<?>) extraData;
        }

        return encodeLiteral(expression.getValue(), targetType);
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        String prop = expression.getPropertyName();
        if (extraData == Geometry.class) {
            return getGeometryPath();
        }
        return getPropertyPath(prop);
    }

    /**
     * Method responsible of retrieving the MongoDB geometry json path
     *
     * @return the MongoDB json paath for the default geometry
     */
    protected abstract String getGeometryPath();

    /**
     * Method responsible of mapping a PropertyName to the corresponding MongoDB json path
     *
     * @param property the string property name to map
     * @return the MongoDB json path
     */
    protected abstract String getPropertyPath(String property);

    @Override
    public Object visit(ExcludeFilter filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);
        output.put("foo", "not_likely_to_exist");
        return output;
    }

    // An empty object should be an "all" query
    @Override
    public Object visit(IncludeFilter filter, Object extraData) {
        return new BasicDBObject();
    }

    //
    // logical
    //

    @Override
    public Object visit(And filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);
        List<Filter> children = filter.getChildren();
        BasicDBList andList = new BasicDBList();
        if (children != null) {
            for (Filter child : children) {
                BasicDBObject item = (BasicDBObject) child.accept(this, null);
                andList.add(item);
            }
            output.put("$and", andList);
        }

        return output;
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);
        List<Filter> children = filter.getChildren();
        BasicDBList orList = new BasicDBList();
        if (children != null) {
            for (Filter child : children) {
                BasicDBObject item = (BasicDBObject) child.accept(this, null);
                orList.add(item);
            }
            output.put("$or", orList);
        }
        return output;
    }

    @Override
    public Object visit(Not filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);
        // in case of a not operator we cannot simply wrap the child filter
        // with a $not since mongo syntax is {property:{$not:{operator-expression}}}
        // thus using a Visitor to find the PropertyName
        class PropertyNameFinder extends FilterAttributeExtractor {
            List<PropertyName> pNames = new ArrayList<>();

            @Override
            public Object visit(PropertyName expression, Object data) {
                pNames.add(expression);
                return super.visit(expression, data);
            }

            PropertyName getPropertyName() {
                if (pNames.isEmpty()) {
                    return null;
                } else {
                    return pNames.get(0);
                }
            }
        }
        PropertyNameFinder finder = new PropertyNameFinder();
        filter.getFilter().accept(finder, null);
        PropertyName pn = finder.getPropertyName();
        // gets child filter as it is
        BasicDBObject expr = (BasicDBObject) filter.getFilter().accept(this, null);
        BasicDBObject dbObject;
        if (pn != null) {
            String strPn = getPropertyPath(pn.getPropertyName());
            // get only the operator expression
            Object exprValue = expr.get(strPn);
            dbObject = new BasicDBObject("$not", exprValue);
            // move up the PropertyName
            output.put(strPn, dbObject);
        } else {
            // no PropertyName found throwing exception
            throw new UnsupportedOperationException(
                    "No propertyName found, cannot use $not as top level operator");
        }
        return output;
    }

    //
    // comparison
    //
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        String propName = convert(filter.getExpression().accept(this, null), String.class);
        Object lower = filter.getLowerBoundary().accept(this, getValueType(filter.getExpression()));
        Object upper = filter.getUpperBoundary().accept(this, getValueType(filter.getExpression()));

        BasicDBObject dbo = new BasicDBObject();
        dbo.put("$gte", lower);
        dbo.put("$lte", upper);
        output.put(propName, dbo);

        return output;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return encodeBinaryComparisonOp(filter, "$eq", extraData);
    }

    BasicDBObject encodeBinaryComparisonOp(
            BinaryComparisonOperator filter, String op, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        Expression left = filter.getExpression1();
        Expression right = filter.getExpression2();
        Class<?> leftValueType = getValueType(right), rightValueType = getValueType(left);

        Object leftValue = filter.getExpression1().accept(this, leftValueType);
        Object rightValue = filter.getExpression2().accept(this, rightValueType);
        if (rightValue instanceof String && !(leftValue instanceof String)) {
            // reverse
            Object tmp = leftValue;
            leftValue = rightValue;
            rightValue = tmp;
        }

        output.put((String) leftValue, op == null ? rightValue : new BasicDBObject(op, rightValue));
        return output;
    }

    protected Class<?> getValueType(Expression e) {

        Class<?> ret = getValueTypeInternal(e);
        if (ret == null && e instanceof Function)
            // get the value type from the function return type
            ret = getFunctionReturnType((Function) e);
        return ret;
    }

    /**
     * Method responsible of retrieving the value type of an Expression
     *
     * @param expression the expression for which find the value type
     * @return the type as a Class of the Expression
     */
    protected abstract Class<?> getValueTypeInternal(Expression expression);

    private Class<?> getFunctionReturnType(Function f) {
        Class<?> clazz = Object.class;
        if (f.getFunctionName() != null && f.getFunctionName().getReturn() != null) {
            clazz = f.getFunctionName().getReturn().getType();
        }
        if (clazz == Object.class) {
            clazz = null;
        }
        return clazz;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return encodeBinaryComparisonOp(filter, "$ne", extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return encodeBinaryComparisonOp(filter, "$gt", extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return encodeBinaryComparisonOp(filter, "$gte", extraData);
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return encodeBinaryComparisonOp(filter, "$lt", extraData);
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return encodeBinaryComparisonOp(filter, "$lte", extraData);
    }

    /**
     * Encode LIKE using MongoDB Regex.
     *
     * <ul>
     *   <li>filter.getWildCard() returns SQL-like '%'
     *   <li>filter.getSingleChar() returns SQL-like '_'
     * </ul>
     *
     * As an example "foo_bar%" converts to foo.bar.*
     */
    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);
        Expression filterExpression = filter.getExpression();
        // Mongo's $regex operator only works on fields
        if (!(filterExpression instanceof PropertyName)) {
            throw new UnsupportedOperationException("LIKE only works with propertyName");
        }

        String expr = convert(filterExpression.accept(this, null), String.class);

        String multi = filter.getWildCard();
        String single = filter.getSingleChar();
        int flags = (filter.isMatchingCase()) ? 0 : Pattern.CASE_INSENSITIVE;

        String regex = filter.getLiteral().replace(multi, ".*").replace(single, ".");
        // force full string match
        regex = "^" + regex + "$";
        Pattern p = Pattern.compile(regex, flags);
        output.put(expr, p);

        return output;
    }

    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);
        String prop = convert(filter.getExpression().accept(this, null), String.class);
        // $eq matches either contain the item field whose value is null
        // or that do not contain the field
        BasicDBObject propIsNull = new BasicDBObject();
        propIsNull.put("$eq", null);
        output.put(prop, propIsNull);
        return output;
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        Set<Identifier> ids = filter.getIdentifiers();

        List<ObjectId> objectIds = new ArrayList<>(ids.size());
        for (Identifier id : ids) {
            objectIds.add(new ObjectId(id.toString()));
        }

        Object objectIdDBO =
                (objectIds.size() > 1) ? new BasicDBObject("$in", objectIds) : objectIds.get(0);

        output.put("_id", objectIdDBO);
        return output;
    }

    //
    // spatial
    //
    @Override
    public Object visit(BBOX filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        // TODO: handle swapping of operands
        Object e1 = filter.getExpression1().accept(this, Geometry.class);

        Envelope envelope = filter.getExpression2().evaluate(null, Envelope.class);

        // Mongodb cannot deal with filters using geometries that span beyond the whole world
        if (!WORLD.contains(envelope)) {
            envelope = envelope.intersection(WORLD);
        }

        DBObject geometryDBObject = geometryBuilder.toObject(envelope);
        addCrsToGeometryDBObject(geometryDBObject);
        DBObject dbo =
                BasicDBObjectBuilder.start()
                        .push("$geoIntersects")
                        .add("$geometry", geometryDBObject)
                        .get();

        output.put((String) e1, dbo);
        return output;
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {

        BasicDBObject output = asDBObject(extraData);

        // TODO: handle swapping of operands
        Object e1 = filter.getExpression1().accept(this, Geometry.class);

        Geometry geometry = filter.getExpression2().evaluate(null, Geometry.class);

        DBObject geometryDBObject = geometryBuilder.toObject(geometry);
        addCrsToGeometryDBObject(geometryDBObject);
        DBObject dbo =
                BasicDBObjectBuilder.start()
                        .push("$geoIntersects")
                        .add("$geometry", geometryDBObject)
                        .get();

        output.put((String) e1, dbo);
        return output;
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        // TODO: handle swapping of operands
        Object e1 = filter.getExpression1().accept(this, Geometry.class);

        Geometry geometry = filter.getExpression2().evaluate(null, Geometry.class);

        DBObject geometryDBObject = geometryBuilder.toObject(geometry);
        addCrsToGeometryDBObject(geometryDBObject);
        DBObject dbo =
                BasicDBObjectBuilder.start()
                        .push("$geoWithin")
                        .add("$geometry", geometryDBObject)
                        .get();

        output.put((String) e1, dbo);
        return output;
    }

    //
    // currently unsupported
    //

    @Override
    public Object visitNullFilter(Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Beyond filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Contains filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Crosses filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Disjoint filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(DWithin filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Equals filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Overlaps filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Touches filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Function function, Object extraData) {
        if (function instanceof JsonSelectFunction) {
            return ((JsonSelectFunction) function).getJsonPath();
        }
        if (function instanceof JsonSelectAllFunction) {
            return ((JsonSelectAllFunction) function).getJsonPath();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(PropertyIsNil filter, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(After after, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Before before, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Begins begins, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(BegunBy begunBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(During during, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(EndedBy endedBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Ends ends, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(Meets meets, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(MetBy metBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(TContains contains, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(TEquals equals, Object extraData) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object visit(TOverlaps contains, Object extraData) {
        throw new UnsupportedOperationException();
    }

    Object encodeLiteral(Object literal, Class<?> targetType) {
        if (literal instanceof Envelope) {
            return geometryBuilder.toObject((Envelope) literal);
        } else if (literal instanceof Geometry) {
            return geometryBuilder.toObject((Geometry) literal);
        } else if (literal instanceof Date) {
            if (targetType != null && Date.class.isAssignableFrom(targetType)) {
                // return date object as is, will be correctly encoded by BasicDBObject
                return literal;
            }
            // by default, convert date to ISO-8601 string
            return DateTimeFormatter.ISO_DATE_TIME.format(((Date) literal).toInstant());
        } else if (literal instanceof String) {
            if (targetType != null && Date.class.isAssignableFrom(targetType)) {
                // try parse string assuming it's ISO-8601 formatted
                return Date.from(
                        Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse((String) literal)));
            }
            // try to convert to the expected type
            return convertLiteral(literal, targetType);
        } else {
            // try to convert to the expected type
            return convertLiteral(literal, targetType);
        }
    }

    /** Java primitives types supported by MongoDB. */
    private static final Class[] SUPPORTED_PRIMITIVES_TYPES = {
        Boolean.class, Double.class, Integer.class, Long.class, String.class,
    };

    /**
     * Helper method that tries to convert a literal to the expected type. If the target type is not
     * supported by MongoDB the string representation of the literal is returned.
     */
    private Object convertLiteral(Object literal, Class<?> targetType) {
        if (literal == null) {
            // return the NULL value
            return null;
        }
        // do we have a target type ?
        if (targetType == null) {
            // no target type, if the literal is already a supported Java
            // primitive type we return it otherwise we do a to string
            return covertToPrimitive(literal);
        }
        // let's see if the target type is a primitive type supported by MongoDB
        if (!isPrimitiveTypeSupported(targetType)) {
            return literal.toString();
        }
        Object converted = Converters.convert(literal, targetType);
        if (converted == null) {
            // no conversion found return the literal as string
            return literal.toString();
        }
        // return the converted value
        return converted;
    }

    /**
     * Helper method that tries to convert a literal to a Java primitive type supported by MongoDB.
     */
    private Object covertToPrimitive(Object literal) {
        for (Class<?> supportedType : SUPPORTED_PRIMITIVES_TYPES) {
            if (supportedType.isAssignableFrom(literal.getClass())) {
                // literal is already a Java primitive type supported by MongoDB
                return literal;
            }
        }
        return literal.toString();
    }

    /** Returns TRUE if the Java primitive type is supported by MongoDB. */
    private boolean isPrimitiveTypeSupported(Class<?> type) {
        for (Class<?> supportedType : SUPPORTED_PRIMITIVES_TYPES) {
            if (supportedType.isAssignableFrom(type)) {
                // java primitive type is supported by MongoDB
                return true;
            }
        }
        // java primitive type not supported by MongoDB
        return false;
    }

    void addCrsToGeometryDBObject(DBObject geometryDBObject) {
        geometryDBObject.put(
                "crs",
                BasicDBObjectBuilder.start()
                        .add("type", "name")
                        .push("properties")
                        .add("name", "urn:x-mongodb:crs:strictwinding:EPSG:4326")
                        .get());
    }
}
