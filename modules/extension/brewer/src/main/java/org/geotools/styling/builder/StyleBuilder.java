package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.util.SimpleInternationalString;

public class StyleBuilder<P> implements Builder<Style> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    P parent;
    
    List<FeatureTypeStyle> fts = new ArrayList<FeatureTypeStyle>();

    FeatureTypeStyleBuilder ftsBuilder;

    String name;

    String styleAbstract;

    String title;

    boolean isDefault;

    private boolean unset;

    public StyleBuilder(){
        this(null);
    }
    public StyleBuilder(P parent){
        parent = null;
        reset();
    }
    
    public StyleBuilder<P> name(String name) {
        this.name = name;
        return this;
    }

    public StyleBuilder title(String title) {
        this.title = title;
        return this;
    }

    public StyleBuilder styleAbstract(String styleAbstract) {
        this.styleAbstract = styleAbstract;
        return this;
    }

    public FeatureTypeStyleBuilder newFeatureTypeStyle() {
        if (ftsBuilder == null)
            ftsBuilder = new FeatureTypeStyleBuilder();
        else
            fts.add(ftsBuilder.build());
        return ftsBuilder;
    }

    public Style build() {
        if( unset ){
            return null;
        }
        if (ftsBuilder == null)
            ftsBuilder = new FeatureTypeStyleBuilder();
        fts.add(ftsBuilder.build());

        Style s = sf.createStyle();
        s.setName(name);
        if (styleAbstract != null)
            s.getDescription().setAbstract(new SimpleInternationalString(styleAbstract));
        if (title != null)
            s.getDescription().setTitle(new SimpleInternationalString(title));
        s.featureTypeStyles().addAll(fts);
        s.setDefault(isDefault);

        if( parent == null ) reset();
        return s;
    }

    public StyleBuilder unset() {
        reset();
        unset = true;
        return this;
    }
    public StyleBuilder reset() {
        fts.clear();
        ftsBuilder = null;
        name = null;
        styleAbstract = null;
        title = null;
        isDefault = false;
        unset = false;
        return this;
    }
    public StyleBuilder reset(Style style) {
        if( style == null ){
            return unset();
        }
        fts.clear();
        fts.addAll( style.featureTypeStyles() ); // TODO: copy into builders
        ftsBuilder = null;
        name = style.getName();
        styleAbstract = style.getAbstract();
        title = style.getTitle();
        isDefault = style.isDefault();
        unset = false;
        return this;
    }
}
