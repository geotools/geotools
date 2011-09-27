package org.geotools.styling.builder;

import org.geotools.styling.ChannelSelection;

/**
 * 
 *
 * @source $URL$
 */
public class ChannelSelectionBuilder extends AbstractStyleBuilder<ChannelSelection> {

    SelectedChannelTypeBuilder gray = new SelectedChannelTypeBuilder().unset();

    SelectedChannelTypeBuilder red = new SelectedChannelTypeBuilder().unset();

    SelectedChannelTypeBuilder green = new SelectedChannelTypeBuilder().unset();

    SelectedChannelTypeBuilder blue = new SelectedChannelTypeBuilder().unset();

    public ChannelSelectionBuilder() {
        this(null);
    }

    public ChannelSelectionBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public SelectedChannelTypeBuilder gray() {
        unset = false;
        return gray;
    }

    public SelectedChannelTypeBuilder red() {
        unset = false;
        return red;
    }

    public SelectedChannelTypeBuilder green() {
        unset = false;
        return green;
    }

    public SelectedChannelTypeBuilder blue() {
        unset = false;
        return blue;
    }

    public ChannelSelection build() {
        if (unset) {
            return null;
        }
        ChannelSelection result;
        if (gray.isUnset()) {
            result = sf.channelSelection(red.build(), green.build(), blue.build());
        } else {
            result = sf.channelSelection(gray.build());
        }
        if (parent == null) {
            reset();
        }
        return result;
    }

    public ChannelSelectionBuilder reset() {
        gray.unset();
        red.unset();
        green.unset();
        blue.unset();
        unset = false;
        return this;
    }

    public ChannelSelectionBuilder reset(ChannelSelection original) {
        if (original == null) {
            return unset();
        }

        if (original.getRGBChannels() != null) {
            red.reset(original.getGrayChannel());
            green.reset(original.getGrayChannel());
            blue.reset(original.getGrayChannel());
        } else {
            gray.reset(original.getGrayChannel());
        }
        unset = false;
        return this;
    }

    public ChannelSelectionBuilder unset() {
        return (ChannelSelectionBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().channelSelection().init(this);
    }

}
