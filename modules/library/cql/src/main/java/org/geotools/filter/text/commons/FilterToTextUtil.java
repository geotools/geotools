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
package org.geotools.filter.text.commons;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.filter.temporal.During;
import org.opengis.temporal.Period;

/**
 * 
 * The method of this utility class allows to build the CQL/ECQL predicate associated
 * to a {@link Filter}. 
 * 
 * <p>
 * Warning: This component is not published. It is part of module implementation. 
 * Client module should not use this feature.
 * </p>
 * 
 * @author Mauricio Pazos
 *
 *
 * @source $URL$
 */
public final class FilterToTextUtil {

	private final static Logger LOGGER = Logger.getLogger(FilterToTextUtil.class.getName());

	
	private FilterToTextUtil(){
		// utility class
	}
	
    /**
     * Process the possibly user supplied extraData parameter into a StringBuilder.
     * @param extraData
     * @return
     */
    static public  StringBuilder asStringBuilder( Object extraData){
        if( extraData instanceof StringBuilder){
            return (StringBuilder) extraData;
        }
        return new StringBuilder();
    }

	public static Object buildInclude(Object extraData) {
        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);
        output.append("INCLUDE");        
        return output;
	}

	public static Object buildExclude(Object extraData) {
        StringBuilder output = FilterToTextUtil.asStringBuilder(extraData);
        output.append("EXCLUDE");        
        return output;
	}

	/**
	 * builds: left predicate AND right predicate
	 */
	public static Object buildBinaryLogicalOperator(final String operator, FilterVisitor visitor, BinaryLogicOperator filter, Object extraData) {
    	
    	LOGGER.finer("exporting And filter");
    	
    	StringBuilder output = asStringBuilder(extraData);
		List<Filter> children = filter.getChildren();
		if (children != null) {
			output.append("(");
			for (Iterator<Filter> i = children.iterator(); i.hasNext();) {
				Filter child = i.next();
				child.accept(visitor, output);
				if (i.hasNext()) {
					output.append(" ").append(operator).append(" ");
				}
			}
			output.append(")");
		}
		return output;
	}

	public static Object buildBetween(PropertyIsBetween filter, Object extraData) {
    	LOGGER.finer("exporting PropertyIsBetween");

    	ExpressionVisitor exprVisitor = new ExpressionToText();
    	
        StringBuilder output = asStringBuilder(extraData);
        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(exprVisitor, output);
        output.append(" BETWEEN ");
        filter.getLowerBoundary().accept(exprVisitor, output);
        output.append(" AND ");
        filter.getUpperBoundary().accept(exprVisitor, output);
        
        return output;
	}

	public static Object buildNot(FilterVisitor filterToCQL, Not filter, Object extraData) {
        StringBuilder output = asStringBuilder(extraData);
        output.append( "NOT (");
        filter.getFilter().accept(filterToCQL, output );
        output.append( ")");        
        return output;
	}


	/**
	 * Builds a comparison predicate inserting the operato1 or operator2 taking into 
	 * account the PropertyName position in the comparison filter.
	 * 
	 * @param filter	
	 * @param extraData
	 * @param operator	an operator
	 * @return SringBuffer
	 */
	static public Object buildComparison(
						BinaryComparisonOperator filter,
						Object extraData, 
						String operator) {

		StringBuilder output = asStringBuilder(extraData);

		ExpressionToText visitor = new ExpressionToText();
		Expression expr = filter.getExpression1();
		expr.accept(visitor, output);
		output.append(" ").append(operator).append(" ");
		filter.getExpression2().accept(visitor, output);

		return output;
	}

	public static Object buildIsLike(PropertyIsLike filter, Object extraData) {
    	
        StringBuilder output = asStringBuilder(extraData);

        final String pattern = filter.getLiteral();

        Expression expr =  filter.getExpression();
        
		expr.accept(new ExpressionToText(), output);

		if(filter.isMatchingCase()){
	        output.append(" LIKE ");
		} else {
	        output.append(" ILIKE ");
		}

        output.append("'");
        output.append(pattern);
        output.append("'");
        
        return output;
	}

	public static Object buildIsNull(PropertyIsNull filter, Object extraData) {
        StringBuilder output = asStringBuilder(extraData);
        
        PropertyName propertyName = (PropertyName) filter.getExpression();
        propertyName.accept(new ExpressionToText(), output);        
        output.append(" IS NULL");
        return output;
	}

	public static Object buildBBOX(BBOX filter, Object extraData) {

		StringBuilder output = asStringBuilder(extraData);
        
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

	public static Object buildDistanceBufferOperation(final String geoOperation, DistanceBufferOperator filter, Object extraData){
		
        LOGGER.finer("exporting " + geoOperation);
        StringBuilder output = asStringBuilder(extraData);
        
        output.append(geoOperation).append("(");
        Expression expr = filter.getExpression1();
        ExpressionToText visitor = new ExpressionToText();
		expr.accept(visitor, output);
        output.append(", ");
        filter.getExpression2().accept(visitor, output);
        output.append(")");
        
        return output;
	}
	

	public static Object buildDWithin(DWithin filter, Object extraData) {
    	
    	LOGGER.finer("exporting DWITHIN");
        StringBuilder output = asStringBuilder(extraData);
        
        output.append("DWITHIN(");
        PropertyName propertyName = (PropertyName) filter.getExpression1();
        ExpressionToText visitor = new ExpressionToText();
		propertyName.accept(visitor, output);
        output.append(", ");
        filter.getExpression2().accept(visitor, output);
        output.append(", ");
        output.append( filter.getDistance() );
        output.append(", ");
        output.append( filter.getDistanceUnits() );
        output.append(")");
        
        return output;
	}

	public static Object buildBinarySpatialOperator(
			final String spatialOperator,
			final BinarySpatialOperator filter, 
			Object extraData) {
    	
        LOGGER.finer("exporting " + spatialOperator);
        StringBuilder output = asStringBuilder(extraData);
        
        output.append(spatialOperator).append("(");
        Expression expr =  filter.getExpression1();
        ExpressionToText visitor = new ExpressionToText();
		expr.accept(visitor, output);
        output.append(", ");
        filter.getExpression2().accept(visitor, output);
        output.append(")");
        
        return output;
	}

	public static Object buildBinaryTemporalOperator(final String temporalOperator, BinaryTemporalOperator filter, Object extraData) {
    	
        LOGGER.finer("exporting " + temporalOperator);

        StringBuilder output = asStringBuilder(extraData);

        PropertyName propertyName = (PropertyName) filter.getExpression1();
    	ExpressionToText visitor = new ExpressionToText();
		propertyName.accept(visitor, output);

        output.append(" ").append(temporalOperator).append(" ");
        
        Literal expr2 = (Literal) filter.getExpression2();
        expr2.accept(visitor, output);

        return output;
	}

	public static Object buildDuring(During during, Object extraData) {
        LOGGER.finer("exporting DURING" );
		
    	StringBuilder output = asStringBuilder(extraData);

        PropertyName propertyName = (PropertyName) during.getExpression1();
    	ExpressionToText visitor = new ExpressionToText();
		propertyName.accept(visitor, output);

        output.append(" DURING ");
        
        Literal expr2 = (Literal) during.getExpression2();
        Period period = (Period) expr2.getValue();
        
        String strBeginningData = dateToCQLDate( period.getBeginning().getPosition().getDate() );
		String strEndingDate = dateToCQLDate( period.getEnding().getPosition().getDate() );
		output.append(strBeginningData).append("/").append(strEndingDate);

        return output;
	}
	
    private static String dateToCQLDate(Date date){

    	StringBuilder cqlDate = new StringBuilder();
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
}
