package org.geotools.filter;


/**
 * 
 *
 * @source $URL$
 */
public class SubFilterBuilder<P> extends FilterBuilder {
    P parent;
    
    public SubFilterBuilder( P parent ){
        this.parent = parent;
    }
    P end(){
        return parent;
    }

}
