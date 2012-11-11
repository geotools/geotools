package org.geotools.renderer.lite;

import static org.geotools.filter.capability.FunctionNameImpl.*;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.referencing.CRS;
import org.opengis.filter.capability.FunctionName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A rendering transformation that reprojects a feature collection
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class ReprojectCollectionFunction extends FunctionExpressionImpl {

    public static FunctionName NAME = new FunctionNameImpl("ReprojectCollection", parameter("crs",
            String.class));

    public ReprojectCollectionFunction() {
        super(NAME);
    }

    @Override
    public int getArgCount() {
        return -1;
    }

    public Object evaluate(Object object) {
        String targetCRS = getAttribute(object, 0, String.class, true);
        try {
            CoordinateReferenceSystem crs = CRS.decode(targetCRS);
            
            return new ReprojectingFeatureCollection((SimpleFeatureCollection) object, crs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reproject the collection");
        }
    }

    <T> T getAttribute(Object object, int expressionIdx, Class<T> targetClass, boolean mandatory) {
        try { // attempt to get value and perform conversion
            T result = getExpression(expressionIdx).evaluate(object, targetClass);
            if (result == null && mandatory) {
                throw new IllegalArgumentException("Could not find function argument #"
                        + expressionIdx + ", but it's mandatory");
            }
            return result;
        } catch (Exception e) {
            // probably a type error
            if (mandatory) {
                throw new IllegalArgumentException("Could not find function argument #"
                        + expressionIdx + ", but it's mandatory");
            } else {
                return null;
            }
        }

    }

}
