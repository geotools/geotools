package org.geotools.data.complex.filter;

import java.util.List;

import org.geotools.filter.BinaryComparisonAbstract;
import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.NestedAttributeExpression;
import org.geotools.filter.OrImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * A wrapper filter for filters involving nested features. Such filter involves nested attributes
 * that come from a different type/table, and is evaluated on the simple feature level (expecting a
 * single value), and join queries aren't supported. Therefore it won't work for multi-valued
 * attributes.. this is why NestedAttributeExpression is used to return all the possible values
 * (multi-valued). This wrapper filter will then get all the possible values from
 * NestedAttributeExpression and applies the filter for each value.
 * 
 * @author Rini Angreani, CSIRO Earth Science and Resource Engineering
 * 
 *
 * @source $URL$
 */
public class MultiValuedOrImpl extends OrImpl {

    private Filter filter;

    private NestedAttributeExpression expression;

    public MultiValuedOrImpl(FilterFactory ff, Filter filter, NestedAttributeExpression expression) {
        super(ff, null);
        this.filter = filter;
        this.expression = expression;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean evaluate(Object feature) {
        Object value = expression.evaluate(feature);
        if (value != null) {
            List<Object> values = (List<Object>) value;
            if (filter instanceof BinaryComparisonAbstract) {
                BinaryComparisonAbstract compFilter = ((BinaryComparisonAbstract) filter);

                Expression exp1 = compFilter.getExpression1();
                Expression exp2 = compFilter.getExpression2();
                boolean isLeftAttributeExp = false;
                if (exp1 == null) {
                    if (exp2 == null) {
                        return true;
                    }
                } else if (exp2 == null) {
                    isLeftAttributeExp = true;
                } else {
                    if (!(exp2 instanceof Literal) && !(exp1 instanceof Literal)) {
                        // unsupported
                        throw new UnsupportedOperationException(
                                "Comparison filters involving attributes of nested features are only supported if "
                                        + "only one of the expressions compared is a literal.");
                    }
                }
                if (exp2 instanceof Literal) {
                    isLeftAttributeExp = true;
                }
                if (isLeftAttributeExp) {
                    for (Object val : values) {
                        compFilter.setExpression1(this.factory.literal(val));
                        if (compFilter.evaluate(null)) {
                            ((BinaryComparisonAbstract) filter).setExpression1(exp1);
                            return true;
                        }
                    }
                    ((BinaryComparisonAbstract) filter).setExpression1(exp1);
                } else {
                    for (Object val : values) {
                        compFilter.setExpression2(this.factory.literal(val));
                        if (compFilter.evaluate(null)) {
                            ((BinaryComparisonAbstract) filter).setExpression2(exp2);
                            return true;
                        }
                    }
                    ((BinaryComparisonAbstract) filter).setExpression2(exp2);
                }
            } else if (filter instanceof LikeFilterImpl) {
                // PropertyIsLike can only have 1 attribute on LHS, and 1 literal on RHS
                for (Object val : values) {
                    ((LikeFilterImpl) filter).setExpression(this.factory.literal(val));
                    if (filter.evaluate(null)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return null;
    }
}
