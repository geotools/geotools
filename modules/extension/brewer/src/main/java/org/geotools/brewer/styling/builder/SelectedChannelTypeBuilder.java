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

import org.geotools.styling.SelectedChannelType;
import org.opengis.filter.expression.Expression;

public class SelectedChannelTypeBuilder extends AbstractStyleBuilder<SelectedChannelType> {

    private Expression channelName;

    private ContrastEnhancementBuilder contrastEnhancement =
            new ContrastEnhancementBuilder(this).unset();

    public SelectedChannelTypeBuilder() {
        this(null);
    }

    public SelectedChannelTypeBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public SelectedChannelTypeBuilder channelName(String channelName) {
        this.unset = false;
        this.channelName = FF.literal(channelName);
        return this;
    }

    public SelectedChannelTypeBuilder channelName(Expression channelName) {
        this.unset = false;
        this.channelName = channelName;
        return this;
    }

    public ContrastEnhancementBuilder contrastEnhancement() {
        this.unset = false;
        return contrastEnhancement;
    }

    public SelectedChannelType build() {
        if (unset) {
            return null;
        }
        SelectedChannelType selectedChannelType =
                sf.selectedChannelType(channelName, contrastEnhancement.build());
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
