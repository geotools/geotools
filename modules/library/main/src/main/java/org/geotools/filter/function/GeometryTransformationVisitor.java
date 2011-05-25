package org.geotools.filter.function;

import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Given an original rendering envelope it visits an expression, finds all
 * {@link GeometryTransformation}, collects and merges all the returned query envelopes
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/filter/function/GeometryTransformationVisitor.java $
 */
public class GeometryTransformationVisitor extends DefaultFilterVisitor {

    public GeometryTransformationVisitor() {
    }

    @Override
    public Object visit(Function expression, Object data) {
        // drill down and merge
        ReferencedEnvelope merged = new ReferencedEnvelope((ReferencedEnvelope) data);
        for(Expression param : expression.getParameters()) {
            ReferencedEnvelope result = (ReferencedEnvelope) param.accept(this, data);
            if(result != null)
                merged.expandToInclude(result);
        }

        // apply the current function is possible
        if (expression instanceof GeometryTransformation) {
            merged = ((GeometryTransformation) expression).invert(merged);
        }

        return merged;
    }

}
