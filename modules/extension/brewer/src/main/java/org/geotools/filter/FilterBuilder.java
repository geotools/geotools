package org.geotools.filter;


import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * FitlerBuilder acting as a simple wrapper around an Expression.
 *
 *
 * @source $URL$
 */
public class FilterBuilder implements Builder<Filter> {
    protected Filter filter; // placeholder just to keep us going right now
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
    protected boolean unset = false;
    protected Builder<? extends Filter> delegate = null;
    
    public FilterBuilder(){
        reset();    
    }
    public FilterBuilder( Filter filter ){
        reset( filter );
    }
    
    /**
     * Build the expression.
     */
    public Filter build() {
        if( unset ) {
            return null;
        }
        return filter;
    }

    public FilterBuilder reset() {
        this.delegate = null;
        this.filter = org.opengis.filter.Filter.EXCLUDE;
        this.unset = false;
        return this;
    }
    
    public FilterBuilder reset(Filter filter) {
        if( filter == null ){
            return unset();
        }
        this.filter = filter;
        this.unset = false;
        return this;
    }

    public FilterBuilder unset() {
        this.unset = true;
        this.delegate =null;
        this.filter = Filter.EXCLUDE;
        return this;
    }

}
