/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.text.cql2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.filter.LikeFilterImpl;
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
import org.opengis.temporal.Period;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * This is a utility class used by CQL.encode( Filter ) method to do the
 * hard work.
 * <p>
 * Please note that this encoder is a bit more strict than you may be used to
 * (the Common Query Language for example demands Equals.getExpression1() is a
 * PropertyName). If you used FilterFactory to produce your filter you should be
 * okay (as it only provides methods to make a valid Filter); if not please
 * expect ClassCastExceptions.
 * <p>
 * This visitor will return a StringBuffer; you can also provide a StringBuffer
 * as the data parameter in order to cut down on the number of objects
 * created during encoding.<pre><code>
 * FilterToCQL toCQL = new FilterToCQL();
 * StringBuffer output = filter.accepts( toCQL, new StringBuffer() );
 * String cql = output.toString();
 * </code></pre> 
 * @author Johann Sorel
 */
class FilterToCQL implements FilterVisitor, ExpressionVisitor {
    /** Standard java logger */
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
    /**
     * Process the possibly user supplied extraData parameter into a StringBuffer.
     * @param extraData
     * @return
     */
    protected StringBuffer asStringBuffer( Object extraData){
        if( extraData instanceof StringBuffer){
            return (StringBuffer) extraData;
        }
        return new StringBuffer();
    }
    /**
     * Exclude everything; using an old SQL trick of 1=0.
     */
    public Object visit(ExcludeFilter filter, Object extraData) {
        StringBuffer output = asStringBuffer(extraData);
        output.append("1 = 1");        
        return output;
    }
    /**
     * Include everything; using an old SQL trick of 1=1.
     */
    public Object visit(IncludeFilter filter, Object extraData) {
        StringBuffer output = asStringBuffer(extraData);
        output.append("1 = 1");
        return output;
    }

    public Object visit(And filter, Object extraData) {
    	LOGGER.finer("exporting And filter");
    	
    	StringBuffer output = asStringBuffer(extraData);
		List<Filter> children = filter.getChildren();
		if (children != null) {
			output.append("(");
			for (Iterator<Filter> i = children.iterator(); i.hasNext();) {
				Filter child = i.next();
				child.accept(this, output);
				if (i.hasNext()) {
					output.append(" AND ");
				}
			}
			output.append(")");
		}
		return output;
	}

	/**
     * Encoding an Id filter is not supported by CQL.
     * <p>
     * This is because in the Catalog specification retreiving an object
     * by an id is a distinct operation seperate from a filter based query.
     */
    public Object visit(Id filter, Object extraData) {
        throw new IllegalStateException("Cannot encode an Id as legal CQL");
    }
    
    public Object visit(Not filter, Object extraData) {
        LOGGER.finer("exporting Not filter");

        StringBuffer output = asStringBuffer(extraData);
        output.append( "NOT (");
        filter.getFilter().accept(this, output );
        output.append( ")");        
        return output;
    }
    
    public Object visit(Or filter, Object extraData) {
        LOGGER.finer("exporting Or filter");

        StringBuffer output = asStringBuffer(extraData);
        List<Filter> children = filter.getChildren();
        if( children != null ){
            output.append("(");
            for( Iterator<Filter> i=children.iterator(); i.hasNext(); ){
                Filter child = i.next();
                child.accept(this, output);
                if (i.hasNext()) {
                    output.append(" OR ");
                }
            }
            output.append(")");
        }
        return output;
    }
    public Object visit(PropertyIsBetween filter, Object extraData) {
        LOGGER.finer("exporting PropertyIsBetween");
        
        StringBuffer output = asStringBuffer(extraData);
        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(this, output);
        output.append(" BETWEEN ");
        filter.getLowerBoundary().accept(this, output);
        output.append(" AND ");
        filter.getUpperBoundary().accept(this, output);
        
        return output;
    }
    
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        LOGGER.finer("exporting PropertyIsEqualTo");
        StringBuffer output = asStringBuffer(extraData);
        
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(" = ");
        filter.getExpression2().accept(this, output);
        
        return output;
    }
    
    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        LOGGER.finer("exporting PropertyIsNotEqualTo");
        StringBuffer output = asStringBuffer(extraData);
        
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(" != ");
        filter.getExpression2().accept(this, output);
      
        return output;
    }

    
    /**
     * Checks if the comparison filter has a literal date.
     * @param filter
     * @return true if the comparison has a literal date , false in other case.
     */
//FIXME    private boolean comparisonHasDate( Filter filter) {
//    	
//    	if(!(filter instanceof BinaryComparisonOperator)){
//    		return false;
//    		
//    	}
//    	BinaryComparisonOperator  comparison  = (BinaryComparisonOperator) filter;
//    	boolean bool;
//        if( comparison.getExpression2() instanceof Literal){
//            Literal literal = (Literal) comparison.getExpression2();
//            bool =  literal.getValue() instanceof Date;
//        } else {
//            Literal literal = (Literal) comparison.getExpression1();
//            bool =  literal.getValue() instanceof Date;
//        }
//        return bool;
//	}
    
    
    /**
     * This is where it would be noice to know if we are working on a Date.
     * <p>
     * I am tempted to do the SimpleFeature look aisde in order to guess
     * what kind of type I am working with.
     */
// FIXME    private StringBuffer after( PropertyIsGreaterThan filter, StringBuffer output ){
//        LOGGER.finer("exporting AFTER");
//        
//        Object expr1 = filter.getExpression1();
//        if( expr1 instanceof PropertyName){
//        	PropertyName propertyName = (PropertyName) expr1;
//        	propertyName.accept(this, output);
//        	output.append(" AFTER ");
//            filter.getExpression2().accept(this, output);        
//        }else { 
//        	PropertyName propertyName = (PropertyName) filter.getExpression2();
//            propertyName.accept(this, output);
//            output.append(" BEFORE ");
//            filter.getExpression1().accept(this, output);        
//        }
//        return output;
//        
//    }

    
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        
        LOGGER.finer("exporting PropertyIsGreaterThan");
        return buildComparison(filter, extraData, ">", "<");
    }
    
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        LOGGER.finer("exporting PropertyIsGreaterThanOrEqualTo");
        return buildComparison(filter, extraData, ">=", "<=");
    }
	/**
	 * Builds a comparison predicate inserting the operato1 or operator2 taking into 
	 * account the PropertyName position in the comparison filter.
	 * 
	 * @param filter	
	 * @param extraData
	 * @param operator1	an operator
	 * @param operator2 the opposite to the operator1
	 * @return SringBuffer
	 */
	private Object buildComparison(
						BinaryComparisonOperator filter,
						Object extraData, 
						String operator1, String operator2) {
       
		StringBuffer output = asStringBuffer(extraData);
        
        Object expr1 = filter.getExpression1();
        if (expr1 instanceof PropertyName) {
        	PropertyName propertyName = (PropertyName) filter.getExpression1();
        	propertyName.accept(this, output);
        	output.append(" ").append(operator1).append(" ");
        	filter.getExpression2().accept(this, output);
        } else {
        	PropertyName propertyName = (PropertyName) filter.getExpression2();
        	propertyName.accept(this, output);
        	output.append(" ").append(operator2).append(" ");
        	filter.getExpression1().accept(this, output);
        }
        return output;
	}
    
    public Object visit(PropertyIsLessThan filter, Object extraData) {
    	
        LOGGER.finer("exporting PropertyIsLessThan");
        return buildComparison(filter, extraData, "<", ">");
    }
    
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        LOGGER.finer("exporting PropertyIsLessThanOrEqualTo");
        return buildComparison(filter, extraData, "<=", ">=");
    }
    
    public Object visit(PropertyIsLike filter, Object extraData) {
        StringBuffer output = asStringBuffer(extraData);
        
        char esc = filter.getEscape().charAt(0);
        char multi = filter.getWildCard().charAt(0);
        char single = filter.getSingleChar().charAt(0);
        boolean matchCase = filter.isMatchingCase();
        String pattern = LikeFilterImpl.convertToSQL92(esc, multi, single, matchCase, 
            filter.getLiteral());

        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(this, output);

        output.append(" LIKE '");

        output.append(pattern);
        output.append("'");
        
        return output;
    }
    public Object visit(PropertyIsNull filter, Object extraData) {
        StringBuffer output = asStringBuffer(extraData);
        
        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(this, output);        
        output.append(" IS NULL");
        return output;
    }
    
    public Object visit(BBOX filter, Object extraData) {
        StringBuffer output = asStringBuffer(extraData);
        
        output.append( "BBOX(");
        output.append( filter.getPropertyName() );
        output.append( ", ");
        output.append( filter.getMinX() );
        output.append( ",");
        output.append( filter.getMinY() );
        output.append( ",");
        output.append( filter.getMaxX() );
        output.append( ",");
        output.append( filter.getMaxY() );
        output.append( ")");
        
        return output;
    }
    public Object visit(Beyond filter, Object extraData) {
        LOGGER.finer("exporting Beyond");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("BEYOND(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    
    public Object visit(Contains filter, Object extraData) {
        LOGGER.finer("exporting Contains");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("CONTAINS(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    
    public Object visit(Crosses filter, Object extraData) {
        LOGGER.finer("exporting Crosses");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("CROSS(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    public Object visit(Disjoint filter, Object extraData) {
        LOGGER.finer("exporting Crosses");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("DISJOINT(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    public Object visit(DWithin filter, Object extraData) {
        LOGGER.finer("exporting Crosses");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("DWITHIN(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(", ");
        output.append( filter.getDistance() );
        output.append(", ");
        output.append( filter.getDistanceUnits() );
        output.append(")");
        
        return output;
    }
    public Object visit(Equals filter, Object extraData) {
        LOGGER.finer("exporting Equals");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("EQUALS(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    public Object visit(Intersects filter, Object extraData) {
        LOGGER.finer("exporting Intersects");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("INTERSECT(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    public Object visit(Overlaps filter, Object extraData) {
        LOGGER.finer("exporting Overlaps");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("OVERLAP(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    public Object visit(Touches filter, Object extraData) {
        LOGGER.finer("exporting Touches");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("TOUCH(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    	     
    public Object visit(Within filter, Object extraData) {
        LOGGER.finer("exporting Within");
        StringBuffer output = asStringBuffer(extraData);
        
        output.append("WITHIN(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        propertyName.accept(this, output);
        output.append(", ");
        filter.getExpression2().accept(this, output);
        output.append(")");
        
        return output;
    }
    /**
     * A filter has not been provided.
     * <p>
     * In general this is a bad situtation which we ask people to
     * represent with Filter.INCLUDES or Filter.EXCLUDES depending
     * on what behaviour they want to see happen - in this case
     * literally <code>null</code> was provided.
     * <p>
     */
    public Object visitNullFilter(Object extraData) {
        throw new NullPointerException("Cannot encode null as a Filter");
    }
    /**
     * Not sure how to record an unset expression in CQL; going
     * to use an emptry string for now.
     */
    public Object visit(NilExpression expression, Object extraData) {
        LOGGER.finer("exporting Expression Nil");
        
        StringBuffer output = asStringBuffer(extraData);
        output.append( "\"\"" );
        
        return output;
    }
    public Object visit(Add expression, Object extraData) {
        LOGGER.finer("exporting Expression Add");

        StringBuffer output = asStringBuffer(extraData);        
        expression.getExpression1().accept(this, output );
        output.append( " + " );
        expression.getExpression2().accept(this, output );
        
        return output;
    }
    public Object visit(Divide expression, Object extraData) {
        LOGGER.finer("exporting Expression Divide");

        StringBuffer output = asStringBuffer(extraData);        
        expression.getExpression1().accept(this, output );
        output.append( " - " );
        expression.getExpression2().accept(this, output );
        
        return output;
    }
    public Object visit(Function function, Object extraData) {
        LOGGER.finer("exporting Function");

        StringBuffer output = asStringBuffer(extraData);        
        output.append( function.getName() );
        output.append( "(" );
        List<Expression> parameters = function.getParameters();

        if( parameters != null ){
            for( Iterator<Expression> i=parameters.iterator(); i.hasNext(); ){
                Expression argument = i.next();
                argument.accept(this, output );
                if( i.hasNext() ){
                    output.append(",");
                }
            }
        }
        output.append( ")" );        
        return output;
    }
    public Object visit(Literal expression, Object extraData) {
        LOGGER.finer("exporting LiteralExpression");
        StringBuffer output = asStringBuffer(extraData);
        
        Object literal = expression.getValue();
        if (literal instanceof Geometry) {
            Geometry geometry = (Geometry) literal;
            WKTWriter writer = new WKTWriter();
            String wkt = writer.write( geometry );
            output.append( wkt );
        }
        else if( literal instanceof Number ){
                // don't convert to string
                output.append( literal );
        }
        else if (literal instanceof Date ){
            return date( (Date) literal, output );
        }
        else {
            String escaped = literal.toString().replaceAll("'", "''");
            output.append("'" + escaped + "'");
        }
        return output;
    }
    /**
     * Uses the format <code>yyyy-MM-dd'T'HH:mm:ss'Z'</code> for
     * output the provided date.
     * @param date
     * @param output
     * @return output
     */
    public StringBuffer date( Date date, StringBuffer output ){
        
        DateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        
        String text = dateFormatter.format( date );
        output.append( text );        
        return output;
    }
    
    public Object visit(Multiply expression, Object extraData) {
        LOGGER.finer("exporting Expression Multiply");

        StringBuffer output = asStringBuffer(extraData);        
        expression.getExpression1().accept(this, output );
        output.append( " * " );
        expression.getExpression2().accept(this, output );
        
        return output;
    }
    public Object visit(PropertyName expression, Object extraData) {
        LOGGER.finer("exporting PropertyName");

        StringBuffer output = asStringBuffer(extraData);        
        output.append( expression.getPropertyName() );
        
        return output;
    }
    public Object visit(Subtract expression, Object extraData) {
        LOGGER.finer("exporting Expression Subtract");

        StringBuffer output = asStringBuffer(extraData);        
        expression.getExpression1().accept(this, output );
        output.append( " - " );
        expression.getExpression2().accept(this, output );
        
        return output;
    }
    
    public Object visit(After after, Object extraData) {
    	
        StringBuffer output = asStringBuffer(extraData);

        PropertyName propertyName = (PropertyName) after.getExpression1();
    	propertyName.accept(this, output);

        output.append(" AFTER ");
        
        Literal expr2 = (Literal) after.getExpression2();
        expr2.accept(this, output);

        return output;
    }
    
    public Object visit(Before before, Object extraData) {
        StringBuffer output = asStringBuffer(extraData);

        PropertyName propertyName = (PropertyName) before.getExpression1();
    	propertyName.accept(this, output);

        output.append(" BEFORE ");
        
        Literal expr2 = (Literal) before.getExpression2();
        expr2.accept(this, output);

        return output;
    }
    
    public Object visit(During during, Object extraData) {

    	StringBuffer output = asStringBuffer(extraData);

        PropertyName propertyName = (PropertyName) during.getExpression1();
    	propertyName.accept(this, output);

        output.append(" DURING ");
        
        Literal expr2 = (Literal) during.getExpression2();
        Period period = (Period) expr2.getValue();
        
        String strBeginningData = dateToCQLDate( period.getBeginning().getPosition().getDate() );
		String strEndingDate = dateToCQLDate( period.getEnding().getPosition().getDate() );
		output.append(strBeginningData).append("/").append(strEndingDate);

        return output;
    }
    
    private String dateToCQLDate(Date date){

    	StringBuffer cqlDate = new StringBuffer();
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);

    	// builds the string date
	    int years = cal.get(Calendar.YEAR);
	    String strYear = String.format("%04d", years);
    	cqlDate.append(strYear).append("-");
    	
	    int month = cal.get(Calendar.MONTH)+1;
	    String strMonth = String.format("%02d", month);
    	cqlDate.append(strMonth).append("-");

	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    String strDay = String.format("%02d", day);
    	cqlDate.append(strDay);
    	
    	// builds the string time
    	cqlDate.append("T");
	    int hour = cal.get(Calendar.HOUR);
	    String strHour = String.format("%02d", hour);
    	cqlDate.append(strHour).append(":");
    	
	    int minute = cal.get(Calendar.MINUTE);
	    String strMinute = String.format("%02d", minute);
    	cqlDate.append(strMinute).append(":");

	    int second = cal.get(Calendar.SECOND);
	    String strSecond = String.format("%02d", second);
    	cqlDate.append(strSecond).append("Z"); // TODO it is a bug in the cql specification. Zulu zone shouldn't be only one of various possibles zone times. 

    	return cqlDate.toString();
    }

    public Object visit(AnyInteracts anyInteracts, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter AnyInteracts has not a CQL expression"); 
    }
    
    
    public Object visit(Begins begins, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Begins has not a CQL expression"); 
    }
    
    public Object visit(BegunBy begunBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter BegunBy has not a CQL expression"); 
    }
    
    public Object visit(EndedBy endedBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter EndedBy has not a CQL expression"); 
    }
    public Object visit(Ends ends, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Ends has not a CQL expression"); 
    }
    public Object visit(Meets meets, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter Meets has not a CQL expression"); 
    }
    public Object visit(MetBy metBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter MetBy has not a CQL expression"); 
    }
    public Object visit(OverlappedBy overlappedBy, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter OverlappedBy not implemented"); 
    }
    public Object visit(TContains contains, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter TContains has not a CQL expression");
    }
    public Object visit(TEquals equals, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter TEquals has not a CQL expression"); 
    }
    public Object visit(TOverlaps contains, Object extraData) {
        throw new UnsupportedOperationException("Temporal filter TOverlaps has not a CQL expression"); 
    }
    
}
