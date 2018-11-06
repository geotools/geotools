/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.brewer.styling.builder;

import org.geotools.styling.ChannelSelection;

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

        if (original.getGrayChannel() != null) {
            gray.reset(original.getGrayChannel());
        } else {
            red.reset(original.getRGBChannels()[0]);
            green.reset(original.getRGBChannels()[1]);
            blue.reset(original.getRGBChannels()[2]);
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
