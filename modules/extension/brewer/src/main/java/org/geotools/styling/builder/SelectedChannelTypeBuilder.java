package org.geotools.styling.builder;

import org.geotools.styling.SelectedChannelType;

/**
 * 
 *
 * @source $URL$
 */
public class SelectedChannelTypeBuilder extends AbstractStyleBuilder<SelectedChannelType> {

    private String channelName;

    private ContrastEnhancementBuilder contrastEnhancement = new ContrastEnhancementBuilder(this)
            .unset();

    public SelectedChannelTypeBuilder() {
        this(null);
    }

    public SelectedChannelTypeBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
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

    public SelectedChannelTypeBuilder reset() {
        contrastEnhancement.reset();
        channelName = null;
        unset = false;
        return this;
    }

    public SelectedChannelTypeBuilder reset(SelectedChannelType selectedChannelType) {
        if (selectedChannelType == null) {
            return reset();
        }
        contrastEnhancement.reset(selectedChannelType.getContrastEnhancement());
        channelName = selectedChannelType.getChannelName();
        unset = false;
        return this;
    }

    public SelectedChannelTypeBuilder unset() {
        return (SelectedChannelTypeBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().channelSelection().gray().init(this);
    }

}
