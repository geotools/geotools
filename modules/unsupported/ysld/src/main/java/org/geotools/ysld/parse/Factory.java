package org.geotools.ysld.parse;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory;

public class Factory {
    StyleFactory style;
    StyleBuilder styleBuilder;
    FilterFactory filter;

    public Factory() {
        this(CommonFactoryFinder.getStyleFactory(), CommonFactoryFinder.getFilterFactory());
    }

    public Factory(StyleFactory style, FilterFactory filter) {
        this.style = style;
        this.styleBuilder = new StyleBuilder(style, filter);
        this.filter = filter;
    }
}
