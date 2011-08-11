package org.geotools.styling.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Icon;

import org.geotools.styling.ExternalGraphic;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.ColorReplacement;

public class ExternalGraphicBuilder extends AbstractStyleBuilder<ExternalGraphic> {
    private Icon inline;

    private String format;

    private OnLineResource resource;

    private Set<ColorReplacementBuilder> replacements = new HashSet<ColorReplacementBuilder>();

    public ExternalGraphicBuilder() {
        this(null);
    }

    public ExternalGraphicBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public String format() {
        return format;
    }

    public ExternalGraphicBuilder format(String format) {
        this.format = format;
        return this;
    }

    public Icon inline() {
        return inline;
    }

    public ExternalGraphicBuilder inline(Icon icon) {
        this.inline = icon;
        return this;
    }

    public Set<ColorReplacementBuilder> replacements() {
        return replacements;
    }

    public ColorReplacementBuilder replacement() {
        ColorReplacementBuilder replacement = new ColorReplacementBuilder(this);
        this.replacements.add(replacement);
        return replacement;
    }

    public ExternalGraphicBuilder resource(OnLineResource resource) {
        this.resource = resource;
        return this;
    }

    public ExternalGraphic build() {
        if (unset) {
            return null;
        }
        ExternalGraphic externalGraphic;
        Collection<ColorReplacement> set = new HashSet<ColorReplacement>();
        for (ColorReplacementBuilder replacement : replacements) {
            set.add(replacement.build());
        }
        if (inline != null) {
            externalGraphic = sf.externalGraphic(inline, set);
        } else {
            externalGraphic = sf.externalGraphic(resource, format, set);

        }
        if (parent == null)
            reset();

        return externalGraphic;
    }

    public ExternalGraphicBuilder reset() {
        unset = false;
        return this;
    }

    public ExternalGraphicBuilder unset() {
        return (ExternalGraphicBuilder) super.unset();
    }

    public ExternalGraphicBuilder reset(ExternalGraphic original) {
        this.unset = false;
        this.format = original.getFormat();
        this.inline = original.getInlineContent();
        this.replacements.clear();
        this.resource = original.getOnlineResource();
        if (original.getColorReplacements() != null) {
            for (ColorReplacement cr : original.getColorReplacements()) {
                replacements.add(new ColorReplacementBuilder().reset(cr));
            }
        }
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().graphic().externalGraphic().init(this);
    }

}
