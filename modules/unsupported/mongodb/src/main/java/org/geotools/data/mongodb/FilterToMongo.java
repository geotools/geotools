/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;
import static org.geotools.util.Converters.convert;
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
 * @author Gerald Gay, Data Tactics Corp.
 * @author Alan Mangan, Data Tactics Corp.
 * @author Tom Kunicki, Boundless Spatial Inc.
 * @source $URL$ (C) 2011, Open Source Geospatial Foundation (OSGeo)
 * @see The GNU Lesser General Public License (LGPL)
 */

public class FilterToMongo implements FilterVisitor, ExpressionVisitor {

    final CollectionMapper mapper;

    final MongoGeometryBuilder geometryBuilder;

    public FilterToMongo(CollectionMapper mapper) {
        this(mapper, new MongoGeometryBuilder());
    }

    public FilterToMongo(CollectionMapper mapper, MongoGeometryBuilder geometryBuilder) {
        this.mapper = mapper;
        this.geometryBuilder = geometryBuilder;
    }

    protected BasicDBObject asDBObject(Object extraData) {
        if ((extraData != null) || (extraData instanceof BasicDBObject)) {
            return (BasicDBObject) extraData;
        }
        return new BasicDBObject();
    }

    //
    // primitives
    //
    @Override
    public Object visit(Literal expression, Object extraData) {
        return encodeLiteral(expression.getValue());
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        String prop = expression.getPropertyName();
        if (extraData == Geometry.class) {
            return mapper.getGeometryPath();
        }
        return mapper.getPropertyPath(prop);
    }

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

    // Expressions like ((A == 1) AND (B == 2)) are basically
    // implied. So just build up all sub expressions
    @Override
    public Object visit(And filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        List<Filter> children = filter.getChildren();
        if (children != null) {
            for (Filter child : children) {
                child.accept(this, output);
            }
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
        BasicDBObject expr = (BasicDBObject) filter.getFilter().accept(this, null);
        output.put("$not", expr);
        return output;
    }

    //
    // comparison
    //
    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        String propName = convert(filter.getExpression().accept(this, null), String.class);
        Object lower = filter.getLowerBoundary().accept(this, null);
        Object upper = filter.getUpperBoundary().accept(this, null);

        BasicDBObject dbo = new BasicDBObject();
        dbo.put("$gte", lower);
        dbo.put("$lte", upper);
        output.put(propName, dbo);
        return propName;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return encodeBinaryComparisonOp(filter, null, extraData);
    }

    BasicDBObject encodeBinaryComparisonOp(BinaryComparisonOperator filter, String op,
            Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        Object expr1 = filter.getExpression1().accept(this, null);
        Object expr2 = filter.getExpression2().accept(this, null);

        if (expr2 instanceof String && !(expr1 instanceof String)) {
            // reverse
            Object tmp = expr1;
            expr1 = expr2;
            expr2 = tmp;
        }

        output.put((String) expr1, op == null ? expr2 : new BasicDBObject(op, expr2));
        return output;
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

    // Mongo doesn't have LIKE but it does have Regex. So
    // I'm converting it like this:
    //
    // filter.getWildCard() returns SQL-like '%'
    // filter.getSingleChar() returns SQL-like '_'
    // So I'm converting "foo_bar%" to /foo.bar.*/
    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);
        String expr = convert(filter.accept(this, null), String.class);

        String multi = filter.getWildCard();
        String single = filter.getSingleChar();
        int flags = (filter.isMatchingCase()) ? 0 : Pattern.CASE_INSENSITIVE;

        String regex = filter.getLiteral().replaceAll(multi, ".*").replaceAll(single, ".");
        Pattern p = Pattern.compile(regex, flags);
        output.put((String) expr, p);

        return output;
    }

    // There is no "NULL" in MongoDB, but I assume that TODO add null support
    // the non-existence of a column is the same...
    @Override
    public Object visit(PropertyIsNull filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        String prop = convert(filter.accept(this, null), String.class);
        output.put(prop, new BasicDBObject("$exists", false));
        return output;
    }

    @Override
    public Object visit(Id filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        Set<Identifier> ids = filter.getIdentifiers();

        List<ObjectId> objectIds = new ArrayList<ObjectId>(ids.size());
        for (Identifier id : ids) {
            objectIds.add(new ObjectId(id.toString()));
        }

        Object objectIdDBO = (objectIds.size() > 1) ? new BasicDBObject("$in", objectIds)
                : objectIds.get(0);

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

        DBObject dbo = BasicDBObjectBuilder.start().push("$geoIntersects")
                .add("$geometry", geometryBuilder.toObject(envelope)).get();

        output.put((String) e1, dbo);
        return output;
    }

    @Override
    public Object visit(Intersects filter, Object extraData) {

        BasicDBObject output = asDBObject(extraData);

        // TODO: handle swapping of operands
        Object e1 = filter.getExpression1().accept(this, Geometry.class);

        Geometry geometry = filter.getExpression2().evaluate(null, Geometry.class);

        DBObject dbo = BasicDBObjectBuilder.start().push("$geoIntersects")
                .add("$geometry", geometryBuilder.toObject(geometry)).get();

        output.put((String) e1, dbo);
        return output;
    }

    @Override
    public Object visit(Within filter, Object extraData) {
        BasicDBObject output = asDBObject(extraData);

        // TODO: handle swapping of operands
        Object e1 = filter.getExpression1().accept(this, Geometry.class);

        Geometry geometry = filter.getExpression2().evaluate(null, Geometry.class);

        DBObject dbo = BasicDBObjectBuilder.start().push("$geoWithin")
                .add("$geometry", geometryBuilder.toObject(geometry)).get();

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

    Object encodeLiteral(Object literal) {
        if (literal instanceof Envelope) {
            return geometryBuilder.toObject((Envelope) literal);
        } else if (literal instanceof Geometry) {
            return geometryBuilder.toObject((Geometry) literal);
        } else {
            return literal.toString();
        }
    }
}
