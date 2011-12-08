package org.geotools.data.mongodb;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.opengis.filter.And;
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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Gerald Gay, Data Tactics Corp.
 * @author Alan Mangan, Data Tactics Corp.
 * @source $URL$
 * 
 *         (C) 2011, Open Source Geospatial Foundation (OSGeo)
 * 
 * @see The GNU Lesser General Public License (LGPL)
 */
/* This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA */
public class FilterToMongoQuery implements FilterVisitor, ExpressionVisitor
{

    // temp holder
    public FilterToMongoQuery ()
    {
    }

    protected BasicDBObject asDBObject (Object extraData)
    {
        if ((extraData != null) || (extraData instanceof BasicDBObject))
        {
            return (BasicDBObject) extraData;
        }
        return new BasicDBObject();
    }

    public Object visit (Literal expression, Object extraData)
    {
        Object literal = expression.getValue();
        String ret = literal.toString();
        return ret;
    }

    public Object visit (PropertyName expression, Object extraData)
    {
        return expression.getPropertyName();
    }

    public Object visit (ExcludeFilter filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        output.put( "foo", "not_likely_to_exist" );
        return output;
    }

    // An empty object should be an "all" query
    public Object visit (IncludeFilter filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        return output;
    }

    // Expressions like ((A == 1) AND (B == 2)) are basically
    // implied. So just build up all sub expressions
    public Object visit (And filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );

        List<Filter> children = filter.getChildren();
        if (children != null)
        {
            for (Iterator<Filter> i = children.iterator(); i.hasNext();)
            {
                Filter child = i.next();
                child.accept( this, output );
            }
        }

        return output;
    }

    /**
     * Encoding an Id filter is not supported by CQL.
     * <p>
     * This is because in the Catalog specification retrieving an object by an id is a distinct
     * operation separate from a filter based query.
     */
    public Object visit (Id filter, Object extraData)
    {
        throw new IllegalStateException( "Cannot encode an Id as legal CQL" );
    }

    public Object visit (Not filter, Object extraData)
    {

        BasicDBObject output = asDBObject( extraData );
        BasicDBObject expr = (BasicDBObject) filter.getFilter().accept( this, null );
        output.put( "$not", expr );
        return output;
    }

    public Object visit (Or filter, Object extraData)
    {

        BasicDBObject output = asDBObject( extraData );
        List<Filter> children = filter.getChildren();
        BasicDBList orList = new BasicDBList();
        if (children != null)
        {
            for (Iterator<Filter> i = children.iterator(); i.hasNext();)
            {
                Filter child = i.next();
                BasicDBObject item = (BasicDBObject) child.accept( this, null );
                orList.add( item );
            }
            output.put( "$or", orList );
        }
        return output;
    }

    public Object visit (PropertyIsBetween filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object propName = filter.getExpression().accept( this, null );
        Object lower = filter.getLowerBoundary().accept( this, null );
        Object upper = filter.getUpperBoundary().accept( this, null );
        if ((propName instanceof String) && (lower instanceof String) && (upper instanceof String))
        {
            BasicDBObject dbo = new BasicDBObject();
            dbo.put( "$gte", lower );
            dbo.put( "$lte", upper );
            output.put( (String) propName, dbo );
        }
        return output;
    }

    public Object visit (PropertyIsEqualTo filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr1 = filter.getExpression1().accept( this, null );
        Object expr2 = filter.getExpression2().accept( this, null );
        if ((expr1 instanceof String) && (expr2 instanceof String))
        {
            // try to determine if equality check against a number, and if so whether fp or int
            // assuming no units/currency markings present, e.g. "$" "ft." etc.
            String expr2Str = ((String) expr2).trim();
            try
            {
                if (expr2Str.matches( "-? ?(\\d+,)*\\d+" )) // integer
                {
                    output.put( (String) expr1, new Long( (String) expr2 ) );
                }
                else if (expr2Str.matches( "-? ?(\\d+,)*\\d+\\.\\d+" )) // floating point
                {
                    output.put( (String) expr1, new Double( (String) expr2 ) );
                }
                else
                {
                    output.put( (String) expr1, expr2 );
                }
            }
            catch (NumberFormatException e)
            {
                output.put( (String) expr1, expr2 );
            }
        }
        return output;
    }

    public Object visit (PropertyIsNotEqualTo filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr1 = filter.getExpression1().accept( this, null );
        Object expr2 = filter.getExpression2().accept( this, null );
        if ((expr1 instanceof String) && (expr2 instanceof String))
        {
            BasicDBObject dbo = new BasicDBObject();
            try
            {
                dbo.put( "$ne", new Double( (String) expr2 ) );
            }
            catch (NumberFormatException e)
            {
                dbo.put( "$ne", expr2 );
            }
            output.put( (String) expr1, dbo );
        }
        return output;
    }

    public Object visit (PropertyIsGreaterThan filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr1 = filter.getExpression1().accept( this, null );
        Object expr2 = filter.getExpression2().accept( this, null );
        if ((expr1 instanceof String) && (expr2 instanceof String))
        {
            BasicDBObject dbo = new BasicDBObject();
            try
            {
                dbo.put( "$gt", new Double( (String) expr2 ) );
            }
            catch (NumberFormatException e)
            {
                dbo.put( "$gt", expr2 );
            }
            output.put( (String) expr1, dbo );
        }
        return output;
    }

    public Object visit (PropertyIsGreaterThanOrEqualTo filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr1 = filter.getExpression1().accept( this, null );
        Object expr2 = filter.getExpression2().accept( this, null );
        if ((expr1 instanceof String) && (expr2 instanceof String))
        {
            BasicDBObject dbo = new BasicDBObject();
            try
            {
                dbo.put( "$gte", new Double( (String) expr2 ) );
            }
            catch (NumberFormatException e)
            {
                dbo.put( "$gte", expr2 );
            }
            output.put( (String) expr1, dbo );
        }
        return output;
    }

    public Object visit (PropertyIsLessThan filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr1 = filter.getExpression1().accept( this, null );
        Object expr2 = filter.getExpression2().accept( this, null );
        if ((expr1 instanceof String) && (expr2 instanceof String))
        {
            BasicDBObject dbo = new BasicDBObject();
            try
            {
                dbo.put( "$lt", new Double( (String) expr2 ) );
            }
            catch (NumberFormatException e)
            {
                dbo.put( "$lt", expr2 );
            }
            output.put( (String) expr1, dbo );
        }
        return output;
    }

    public Object visit (PropertyIsLessThanOrEqualTo filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr1 = filter.getExpression1().accept( this, null );
        Object expr2 = filter.getExpression2().accept( this, null );
        if ((expr1 instanceof String) && (expr2 instanceof String))
        {
            BasicDBObject dbo = new BasicDBObject();
            try
            {
                dbo.put( "$lte", new Double( (String) expr2 ) );
            }
            catch (NumberFormatException e)
            {
                dbo.put( "$lte", expr2 );
            }
            output.put( (String) expr1, dbo );
        }
        return output;
    }

    // Mongo doesn't have LIKE but it does have Regex. So
    // I'm converting it like this:
    //
    // filter.getWildCard() returns SQL-like '%'
    // filter.getSingleChar() returns SQL-like '_'
    // So I'm converting "foo_bar%" to /foo.bar.*/
    public Object visit (PropertyIsLike filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr = filter.getExpression();
        if (expr instanceof String)
        {
            String multi = filter.getWildCard();
            String single = filter.getSingleChar();
            int flags = (filter.isMatchingCase()) ? 0 : Pattern.CASE_INSENSITIVE;
            String cqlPattern = filter.getLiteral();
            cqlPattern.replaceAll( multi, ".*" );
            cqlPattern.replaceAll( single, "." );
            try
            {
                Pattern p = Pattern.compile( cqlPattern, flags );
                output.put( (String) expr, p );
            }
            catch (Throwable t)
            {
            }
        }
        return output;
    }

    // There is no "NULL" in MongoDB, but I assume that TODO add null support
    // the non-existence of a column is the same...
    public Object visit (PropertyIsNull filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );
        Object expr = filter.accept( this, null );
        if (expr instanceof String)
        {
            BasicDBObject dbo = new BasicDBObject();
            dbo.put( "$exists", false );
            output.put( (String) expr, dbo );
        }
        return output;
    }

    public Object visit (BBOX filter, Object extraData)
    {
        BasicDBObject output = asDBObject( extraData );

        double minX = 180;
        double minY = 90;
        double maxX = -180;
        double maxY = -90;
        // replace deprecated BBOX.getMinX() w/ getExpression2() call
        // to determine mins and maxes
        Expression exp2 = filter.getExpression2();
        if (exp2 instanceof Literal)
        {
            Geometry bbox = (Geometry) ((Literal) exp2).getValue();
            Coordinate[] coords = bbox.getEnvelope().getCoordinates();
            minX = coords[0].x;
            minY = coords[0].y;
            maxX = coords[2].x;
            maxY = coords[2].y;
        }

        if (minX < -180)
            minX = -180;
        if (maxX > 180)
            maxX = 180;
        if (minY < -90)
            minY = -90;
        if (maxY > 90)
            maxY = 90;

        StringBuilder sb = new StringBuilder();
        sb.append( "gtmpGeoQuery([" );
        sb.append( minX );
        sb.append( "," );
        sb.append( minY );
        sb.append( "," );
        sb.append( maxX );
        sb.append( "," );
        sb.append( maxY );
        sb.append( "])" );
        output.put( "$where", sb.toString() );

        return output;
    }

    public Object visitNullFilter (Object extraData)
    {
        throw new NullPointerException( "Cannot encode null as a Filter" );
    }

    public Object visit (NilExpression expression, Object extraData)
    {
        return extraData;
    }

    /******************************************************
     * The rest are either filters that don't make sense or are currently not implemented.
     ******************************************************/

    public Object visit (Beyond filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Contains filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Crosses filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Disjoint filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (DWithin filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Equals filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Intersects filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Overlaps filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Touches filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Within filter, Object extraData)
    {
        return extraData;
    }

    public Object visit (Add expression, Object extraData)
    {
        return extraData;
    }

    public Object visit (Divide expression, Object extraData)
    {
        return extraData;
    }

    public Object visit (Function function, Object extraData)
    {
        return extraData;
    }

    public Object visit (Multiply expression, Object extraData)
    {
        return extraData;
    }

    public Object visit (Subtract expression, Object extraData)
    {
        return extraData;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.PropertyIsNil,
     * java.lang.Object) */
    @Override
    public Object visit (PropertyIsNil filter, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.After,
     * java.lang.Object) */
    @Override
    public Object visit (After after, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.AnyInteracts,
     * java.lang.Object) */
    @Override
    public Object visit (AnyInteracts anyInteracts, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.Before,
     * java.lang.Object) */
    @Override
    public Object visit (Before before, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.Begins,
     * java.lang.Object) */
    @Override
    public Object visit (Begins begins, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.BegunBy,
     * java.lang.Object) */
    @Override
    public Object visit (BegunBy begunBy, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.During,
     * java.lang.Object) */
    @Override
    public Object visit (During during, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.EndedBy,
     * java.lang.Object) */
    @Override
    public Object visit (EndedBy endedBy, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.Ends,
     * java.lang.Object) */
    @Override
    public Object visit (Ends ends, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.Meets,
     * java.lang.Object) */
    @Override
    public Object visit (Meets meets, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.MetBy,
     * java.lang.Object) */
    @Override
    public Object visit (MetBy metBy, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.OverlappedBy,
     * java.lang.Object) */
    @Override
    public Object visit (OverlappedBy overlappedBy, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.TContains,
     * java.lang.Object) */
    @Override
    public Object visit (TContains contains, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.TEquals,
     * java.lang.Object) */
    @Override
    public Object visit (TEquals equals, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * 
     * @see org.opengis.filter.FilterVisitor#visit(org.opengis.filter.temporal.TOverlaps,
     * java.lang.Object) */
    @Override
    public Object visit (TOverlaps contains, Object extraData)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
