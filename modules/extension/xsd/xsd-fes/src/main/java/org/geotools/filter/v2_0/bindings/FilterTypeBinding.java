package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.opengis.filter.FilterFactory;

public class FilterTypeBinding extends org.geotools.filter.v1_1.FilterTypeBinding {

    public FilterTypeBinding(FilterFactory filterFactory) {
        super(filterFactory);
    }
    
    @Override
    public QName getTarget() {
        return FES.FilterType;
    }

}
