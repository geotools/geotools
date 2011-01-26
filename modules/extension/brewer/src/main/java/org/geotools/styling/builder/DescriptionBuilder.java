package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Description;
import org.geotools.styling.StyleFactory;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

public class DescriptionBuilder<P> implements Builder<Description> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    P parent;
    private boolean unset;
    private InternationalString title;
    private InternationalString description;

    public DescriptionBuilder(){
        this(null);
    }
    public DescriptionBuilder(P parent) {
        this.parent = parent;
        reset();
    }
    public Description build() {
        if( unset ){
            return null;
        }
        Description descript = sf.description(title, description);
        if( parent == null ){
            reset();
        }
        return descript;
    }

    public DescriptionBuilder<P> reset() {
        unset = false;
        title = null;
        description = null;
        return this;
    }
    public DescriptionBuilder<P> title(InternationalString title) {
        this.title = title;
        return this;
    }
    public DescriptionBuilder<P> title(String title) {
        this.title = new SimpleInternationalString(title);
        return this;
    }
    public InternationalString title() {
        return title;
    }
    public DescriptionBuilder<P> description(InternationalString description) {
        this.description = description;
        return this;
    }
    public DescriptionBuilder<P> description(String description) {
        this.description = new SimpleInternationalString( description );
        return this;
    }
    public InternationalString description() {
        return description;
    }
    
    public DescriptionBuilder<P> reset(Description original) {
        unset = false;
        title = original.getTitle();
        description = original.getAbstract();
        return this;
    }

    public DescriptionBuilder<P> unset() {
        unset = true;
        title = null;
        description = null;
        return this;
    }

}
