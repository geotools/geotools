package org.geotools.styling.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.StyleFactory;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.ColorReplacement;

public class ExternalGraphicBuilder<P> implements Builder<ExternalGraphic> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private Icon inline;

    private String format;

    private OnLineResource resource;

    private Set<ColorReplacementBuilder<ExternalGraphicBuilder<P>>> replacements = new HashSet<ColorReplacementBuilder<ExternalGraphicBuilder<P>>>();

    public ExternalGraphicBuilder() {
        this(null);
    }

    public ExternalGraphicBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public String format() {
        return format;
    }
    public ExternalGraphicBuilder<P> format(String format) {
        this.format = format;
        return this;
    }
    public Icon inline() {
        return inline;
    }
    public ExternalGraphicBuilder<P> inline(Icon icon) {
        this.inline = icon;
        return this;
    }
    public Set<ColorReplacementBuilder<ExternalGraphicBuilder<P>>> replacements() {
        return replacements;
    }
    
    public ColorReplacementBuilder<ExternalGraphicBuilder<P>> replacement() {
        ColorReplacementBuilder<ExternalGraphicBuilder<P>> replacement = new ColorReplacementBuilder<ExternalGraphicBuilder<P>>(this);
        this.replacements.add( replacement );
        return replacement;
    }
    
    public OnLineResource resource() {
        return resource;
    }
    
    public ExternalGraphicBuilder<P> resource(OnLineResource resource) {
        this.resource = resource;
        return this;        
    }
    
    public ExternalGraphic build() {
        if (unset) {
            return null;
        }
        ExternalGraphic externalGraphic;
        Collection<ColorReplacement> set = new HashSet<ColorReplacement>();
        for (ColorReplacementBuilder<ExternalGraphicBuilder<P>> replacement : replacements) {
            set.add(replacement.build());
        }
        if (inline != null) {
            externalGraphic = sf.externalGraphic(inline, set);
        } else {
            externalGraphic = sf.externalGraphic(resource, format, set);

        }
        if (parent == null) reset();
        
        return externalGraphic;
    }

    public P end() {
        return parent;
    }

    public ExternalGraphicBuilder<P> reset() {
        unset = false;
        return this;
    }

    public ExternalGraphicBuilder<P> reset(AnchorPoint original) {
        if (original == null) {
            return reset();
        }
        unset = false;
        return this;
    }

    public ExternalGraphicBuilder<P> unset() {
        unset = true;
        return this;
    }

    public ExternalGraphicBuilder<P> reset(ExternalGraphic original) {
        return null;
    }

}
