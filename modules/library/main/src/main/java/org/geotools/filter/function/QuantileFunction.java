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
package org.geotools.filter.function;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.visitor.CalcResult;
import org.geotools.feature.visitor.QuantileListVisitor;
import org.geotools.util.NullProgressListener;

/**
 * Breaks a SimpleFeatureCollection into classes with an equal number of items in each.
 * 
 * @author Cory Horner, Refractions Research Inc.
 *
 * @source $URL$
 */
public class QuantileFunction extends ClassificationFunction {

	public QuantileFunction() {
        setName("Quantile");
	}

    public int getArgCount() {
        return 2;
    }
    
	private Object calculate(SimpleFeatureCollection featureCollection) {
		// use a visitor to find the values in each bin
		QuantileListVisitor quantileVisit = new QuantileListVisitor(getExpression(), getClasses());
		if (progress == null) progress = new NullProgressListener();
		try {
            featureCollection.accepts(quantileVisit, progress);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "QuantileFunction calculate(SimpleFeatureCollection) failed" , e);
            return null;
        }
		if (progress.isCanceled()) return null;
		CalcResult calcResult = quantileVisit.getResult();
		if (calcResult == null) return null;
        List[] bin = (List[]) calcResult.getValue();
		
		//generate the min and max values, and round off if applicable/necessary
		Comparable globalMin = (Comparable) bin[0].toArray()[0];
        Object lastBin[] = bin[bin.length-1].toArray(); 
		if (lastBin.length == 0) {
		    return null;
        }
		Comparable globalMax = (Comparable) lastBin[lastBin.length-1];
	
		if ((globalMin instanceof Number) && (globalMax instanceof Number)) {
            return calculateNumerical(bin, globalMin, globalMax);
		} else {
            return calculateNonNumerical(bin);
		}
	}
    
    private Object calculateNumerical(List[] bin, Comparable globalMin, Comparable globalMax) {
        int classNum = bin.length;
        //size arrays
        Comparable[] localMin = new Comparable[classNum];
        Comparable[] localMax = new Comparable[classNum];
        //globally consistent
        //double slotWidth = (((Number) globalMax).doubleValue() - ((Number) globalMin).doubleValue()) / classNum;
        for (int i = 0; i < classNum; i++) {
            //copy the min + max values
            List thisBin = bin[i];
            localMin[i] = (Comparable) thisBin.get(0);
            localMax[i] = (Comparable) thisBin.get(thisBin.size()-1);
            //locally accurate
            double slotWidth = ((Number) localMax[i]).doubleValue() - ((Number) localMin[i]).doubleValue();
            if (slotWidth == 0.0) { //use global value, as there is only 1 value in this set
                slotWidth = (((Number) globalMax).doubleValue() - ((Number) globalMin).doubleValue()) / classNum;
            }
            //determine number of decimal places to allow
            int decPlaces = decimalPlaces(slotWidth);
            decPlaces = Math.max(decPlaces, decimalPlaces(((Number) localMin[i]).doubleValue()));
            decPlaces = Math.max(decPlaces, decimalPlaces(((Number) localMax[i]).doubleValue()));
            //clean up truncation error
            if (decPlaces > -1) {
                localMin[i] = new Double(round(((Number) localMin[i]).doubleValue(), decPlaces));
                localMax[i] = new Double(round(((Number) localMax[i]).doubleValue(), decPlaces));
            }
            
            if (i == 0) {
                //ensure first min is less than or equal to globalMin
                if (localMin[i].compareTo(new Double(((Number) globalMin).doubleValue())) > 0)
                    localMin[i] = new Double(fixRound(((Number) localMin[i]).doubleValue(), decPlaces, false));
            } else if (i == classNum - 1) { 
                //ensure last max is greater than or equal to globalMax
                if (localMax[i].compareTo(new Double(((Number) globalMax).doubleValue())) < 0)
                    localMax[i] = new Double(fixRound(((Number) localMax[i]).doubleValue(), decPlaces, true));
            }

            //synchronize previous max with current min; the ranged classifier is min <= x < y;
            if (i != 0){
            	localMax[i-1] = localMin[i];
            }
            
        }
        //TODO: disallow having 2 identical bins (ie 0..0, 0..0, 0..0, 0..100)
        return new RangedClassifier(localMin, localMax);
    }
    
    private Object calculateNonNumerical(List[] bin) {
        int classNum = bin.length;
        //it's a string.. leave it be (just copy the values)
        Set[] values = new Set[classNum];        
        for (int i = 0; i < classNum; i++) {
            values[i] = new HashSet();
            Iterator iterator = bin[i].iterator();
            while (iterator.hasNext()) {
                values[i].add(iterator.next());
            }
        }
        return new ExplicitClassifier(values);
//      alternative for ranged classifier            
//      localMin[i] = (Comparable) thisBin.get(0);
//      localMax[i] = (Comparable) thisBin.get(thisBin.size()-1);
    }

	public Object evaluate(Object feature) {
	    if (!(feature instanceof FeatureCollection)) {
	        return null;
        }
        return calculate((SimpleFeatureCollection) feature);
	}

}
