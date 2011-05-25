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
package org.geotools.feature.visitor;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Expression;

/**
 * Determines the standard deviation. 
 *
 * <pre>
 *            ----------------------------
 *            |  1   ---
 * Std dev =  | ___  \   ( x - mean ) ^ 2
 *           \|  N   /__
 * </pre>
 *           
 * aka std dev = sqrt((sum((x-mean)^2))/N) where N is the number of samples.
 * 
 * It uses the rolling variance algorithm described here: 
 * http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#On-line_algorithm
 * 
 * @author Cory Horner, Refractions Research Inc.
 * @author Andrea Aime, GeoSolutions
 *
 *
 * @source $URL$
 */
public class StandardDeviationVisitor implements FeatureCalc {
	private Expression expr;

    boolean visited = false;
    int countNull = 0;
    int countNaN = 0;
    int count = 0;
    double mean = 0;
    double m2 = 0;

    /**
     * Constructor left for backwards compatibility. The current algorithm uses
     * a on line computation that does not require the average to be calculated
     * in advance
     * @param expr
     * @param average
     * @deprecated use {@link StandardDeviationVisitor(Expression)} insteads
     */
    @Deprecated
	public StandardDeviationVisitor(Expression expr, double average) {
		this.expr = expr;
	}
    
    /**
     * Constructs a standard deviation visitor based on the specified expression
     * @param expr
     * @param average
     */
	public StandardDeviationVisitor(Expression expr) {
		this.expr = expr;
	}

	public void init(SimpleFeatureCollection collection) {
		//do nothing
	}
	
	public CalcResult getResult() {
		if(count == 0) {
    		return CalcResult.NULL_RESULT;
    	}
		return new AbstractCalcResult() {
			public Object getValue() {
                if (count == 0) return null;
				return new Double(Math.sqrt(m2 / count));
			}
		};
	}

	public void visit(SimpleFeature feature) {
        visit((org.opengis.feature.Feature)feature);
    }
    public void visit(org.opengis.feature.Feature feature) {
        Object value = expr.evaluate(feature);

        if (value == null) {
			countNull++; // increment the null count
			return; // don't store this value
		}

		if (value instanceof Double) {
			double doubleVal = ((Double) value).doubleValue();
			if (Double.isNaN(doubleVal) || Double.isInfinite(doubleVal)) {
				countNaN++; // increment the NaN count
				return; // don't store NaN value
			}
		}
		
		double x = ((Number) value).doubleValue();
		count++;
		double delta = x - mean;
		mean = mean + delta / count;
		m2 = m2 + delta * (x - mean);  // This expression uses the new value of mean
	}
	
	public void reset() {
		this.count = 0;
	    this.countNull = 0;
	    this.countNaN = 0;
	    this.m2 = 0;
	    this.mean = 0;
	}

    /**
     * @return the number of features which returned a NaN
     */
    public int getNaNCount() {
    	return countNaN;
    }
    
    /**
     * @return the number of features which returned a null
     */
    public int getNullCount() {
    	return countNull;
    }
}
