package org.geotools.filter.function;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Function;

/**
 * This interface can be implemented by geometry transformation functions
 * that whish to be used in the {@link Symbolizer} geometry property.
 * <p>It gives the renderer a hint of what area should be queried given a certain rendering area
 * @author aaime
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/filter/function/GeometryTransformation.java $
 */
public interface GeometryTransformation extends Function {
    /**
     * Returns a query envelope given a certain 
     * @param renderingEnvelope
     * @return
     */
    ReferencedEnvelope invert(ReferencedEnvelope renderingEnvelope);
}
