package org.geotools.data.aggregate;

import java.util.Set;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.expression.PropertyName;

/**
 * Replaces all missing properties references with a null literal
 * 
 * @author Andrea Aime
 * 
 *
 * @source $URL$
 */
public class MissingPropertiesEraser extends DuplicatingFilterVisitor {

    Set<String> properties;

    public MissingPropertiesEraser(Set<String> properties) {
        this.properties = properties;
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        if (properties.contains(expression.getPropertyName())) {
            return super.visit(expression, extraData);
        } else {
            return ff.literal(null);
        }
    }
}
