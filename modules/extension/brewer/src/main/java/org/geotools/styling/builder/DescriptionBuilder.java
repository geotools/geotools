package org.geotools.styling.builder;

import org.geotools.styling.Description;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

public class DescriptionBuilder extends AbstractStyleBuilder<Description> {

    private InternationalString title;

    private InternationalString description;

    public DescriptionBuilder() {
        this(null);
    }

    public DescriptionBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public Description build() {
        if (unset) {
            return null;
        }
        Description descript = sf.description(title, description);
        if (parent == null) {
            reset();
        }
        return descript;
    }

    public DescriptionBuilder reset() {
        unset = false;
        title = null;
        description = null;
        return this;
    }

    public DescriptionBuilder title(InternationalString title) {
        this.title = title;
        unset = false;
        return this;
    }

    public DescriptionBuilder title(String title) {
        return title(new SimpleInternationalString(title));
    }

    public DescriptionBuilder description(InternationalString description) {
        this.description = description;
        unset = false;
        return this;
    }

    public DescriptionBuilder description(String description) {
        return description(new SimpleInternationalString(description));
    }

    public DescriptionBuilder reset(Description original) {
        unset = false;
        title = original.getTitle();
        description = original.getAbstract();
        return this;
    }

    public DescriptionBuilder unset() {
        unset = true;
        title = null;
        description = null;
        return this;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        throw new UnsupportedOperationException(
                "Does not make sense to build a style out of a description");

    }

}
