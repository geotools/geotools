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

import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;

public class StyledLayerDescriptorBuilder extends AbstractSLDBuilder<StyledLayerDescriptor> {

    List<AbstractSLDBuilder<? extends StyledLayer>> layers =
            new ArrayList<AbstractSLDBuilder<? extends StyledLayer>>();

    String name;

    String title;

    String sldAbstract;

    public StyledLayerDescriptorBuilder() {
        super(null);
        reset();
    }

    public StyledLayerDescriptorBuilder name(String name) {
        unset = false;
        this.name = name;
        return this;
    }

    public StyledLayerDescriptorBuilder title(String title) {
        unset = false;
        this.title = title;
        return this;
    }

    public StyledLayerDescriptorBuilder sldAbstract(String sldAbstract) {
        unset = false;
        this.sldAbstract = sldAbstract;
        return this;
    }

    public NamedLayerBuilder namedLayer() {
        unset = false;
        NamedLayerBuilder nlb = new NamedLayerBuilder(this);
        layers.add(nlb);
        return nlb;
    }

    public UserLayerBuilder userLayer() {
        unset = false;
        UserLayerBuilder ulb = new UserLayerBuilder(this);
        layers.add(ulb);
        return ulb;
    }

    /** Reset stroke to default values. */
    public StyledLayerDescriptorBuilder reset() {
        unset = false;
        this.name = null;
        this.title = null;
        this.sldAbstract = null;
        this.layers.clear();
        return this;
    }

    /** Reset builder to provided original stroke. */
    public StyledLayerDescriptorBuilder reset(StyledLayerDescriptor other) {
        if (other == null) {
            return unset();
        }
        this.name = other.getName();
        this.title = other.getTitle();
        this.sldAbstract = other.getAbstract();
        this.layers.clear();
        for (StyledLayer layer : other.getStyledLayers()) {
            if (layer instanceof UserLayer) {
                layers.add(new UserLayerBuilder().reset((UserLayer) layer));
            } else if (layer instanceof NamedLayer) {
                layers.add(new NamedLayerBuilder().reset((NamedLayer) layer));
            }
        }

        unset = false;
        return this;
    }

    public StyledLayerDescriptor build() {
        if (unset) {
            return null;
        }
        StyledLayerDescriptor sld = sf.createStyledLayerDescriptor();
        sld.setName(name);
        sld.setTitle(title);
        sld.setAbstract(sldAbstract);
        for (AbstractSLDBuilder<? extends StyledLayer> builder : layers) {
            sld.addStyledLayer(builder.build());
        }
        reset();
        return sld;
    }

    @Override
    public StyledLayerDescriptor buildSLD() {
        return build();
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        sb.init(this);
    }

    @Override
    public StyledLayerDescriptorBuilder unset() {
        return (StyledLayerDescriptorBuilder) super.unset();
    }
}
