package org.geotools.filter.expression;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;

public class PropertyNameBuilder implements Builder<PropertyName> {
    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);    
    String xpath = null; // will result in Expression.NIL
    Name name = null;
    boolean unset = false;
    
    public PropertyNameBuilder(){
         reset();        
    }
    public PropertyNameBuilder( PropertyName propertyName ){
        reset( propertyName );        
    }
    public PropertyNameBuilder property( String xpath ){
        return name( xpath );
    }
    public PropertyNameBuilder name( String name ){
        this.xpath = name;
        this.name = null;
        unset = false;
        return this;
    }
    public PropertyNameBuilder name( Name name ){
        this.name = name;
        this.xpath = null;
        unset = false;
        return this;
    }
    public PropertyName build() {
        if( unset ){
            return null;
        }
        if( name != null ){
            return ff.property( name );
        }
        else {
            return ff.property( xpath );
        }
    }

    public PropertyNameBuilder reset() {
        unset = false;
        xpath = null;
        return this;
    }

    public PropertyNameBuilder reset( PropertyName original) {
        unset = false;
        xpath = original.getPropertyName();
        return this;
    }

    public PropertyNameBuilder unset() {
        unset = true;
        xpath = null;
        return this;
    }

}
