package org.geotools.filter;


import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.Identifier;

/**
 * FitlerBuilder acting as a simple wrapper around an Expression.
 *
 * @source $URL$
 */
public class AndBuilder<P> implements Builder<And> {
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
    protected P parent;
    protected List<FilterBuilder> list;

    private List<Identifier> ids = new ArrayList<Identifier>();
    
    public AndBuilder(){
        reset();    
    }
    
    public AndBuilder( P parent){
        this.parent = parent;
        reset();
    }
    
    
    /**
     * Build an And filter
     */
    public And build() {
        if( list == null ) {
            return null;
        }
        List<Filter> filters = new ArrayList<Filter>( list.size() );
        for( FilterBuilder build : list ){
            Filter filter = build.build();
            if( filter != null ){
                filters.add( filter );
            }
        }
        if (parent == null){
            list.clear();
        }
        return ff.and( filters );
    }

    public AndBuilder<P> fid( String fid ){
        ids.add( ff.featureId(fid));
        return this;
    }
    
    public AndBuilder<P> and( Filter filter ){
        list.add( new FilterBuilder().reset( filter ));
        return this;
    }
    
    public AndBuilder<P> fid( List<String> fids ){
        for( String fid : fids ){
            ids.add( ff.featureId(fid));            
        }
        return this;
    }
    
    public P end(){
        return parent;
    }
    public AndBuilder<P> reset() {
        this.list = new ArrayList<FilterBuilder>();
        return this;
    }
    
    public AndBuilder<P> reset(And filter) {
        if( filter == null ){
            return unset();
        }
        this.list = new ArrayList<FilterBuilder>();
        if( filter.getChildren() != null ){
            for( Filter child : filter.getChildren() ){
                list.add( new FilterBuilder().reset( child ));
            }
        }
        return this;
    }

    public AndBuilder<P> unset() {       
        this.list = null;
        return this;
    }

}
