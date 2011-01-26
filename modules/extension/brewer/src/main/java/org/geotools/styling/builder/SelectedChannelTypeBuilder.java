package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleFactory;

public class SelectedChannelTypeBuilder<P> implements Builder<SelectedChannelType> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    boolean unset = true; // current value is null

    private String channelName;

    private ContrastEnhancementBuilder<SelectedChannelTypeBuilder<P>> contrastEnhancement = new ContrastEnhancementBuilder<SelectedChannelTypeBuilder<P>>(this);

    public SelectedChannelTypeBuilder() {
        this( null );
    }

    public SelectedChannelTypeBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public SelectedChannelType build() {
        if (unset) {
            return null;
        }
        SelectedChannelType selectedChannelType = sf.selectedChannelType(channelName,
                contrastEnhancement.build());
        return selectedChannelType;
    }

    public P end() {
        return parent;
    }

    public SelectedChannelTypeBuilder<P> reset() {
        contrastEnhancement.reset();
        channelName = null;
        unset = false;
        return this;
    }

    public SelectedChannelTypeBuilder<P> reset(SelectedChannelType selectedChannelType) {
        if (selectedChannelType == null) {
            return reset();
        }
        contrastEnhancement.reset(selectedChannelType.getContrastEnhancement());
        channelName = selectedChannelType.getChannelName();
        unset = false;
        return this;
    }

    public SelectedChannelTypeBuilder<P> unset() {
        contrastEnhancement.unset();
        channelName = null;
        unset = true;
        return this;
    }

}
