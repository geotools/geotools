/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.opengis.style.ChannelSelection;
import org.opengis.style.SelectedChannelType;

/** Enumeration for valid raster band values. */
public enum Band {
    GRAY("gray") {

        @Override
        public org.opengis.style.SelectedChannelType getFrom(
                org.opengis.style.ChannelSelection sel) {
            return sel.getGrayChannel();
        }

        @Override
        public void setTo(
                org.geotools.styling.ChannelSelection sel,
                org.geotools.styling.SelectedChannelType chan) {
            sel.setGrayChannel(chan);
        }
    },
    RED("red") {
        @Override
        public SelectedChannelType getFrom(ChannelSelection sel) {
            return Optional.ofNullable(sel.getRGBChannels()).map(a -> a[0]).orElse(null);
        }

        @Override
        public void setTo(
                org.geotools.styling.ChannelSelection sel,
                org.geotools.styling.SelectedChannelType chan) {
            org.geotools.styling.SelectedChannelType channels[] = sel.getRGBChannels();
            channels[0] = chan;
            sel.setRGBChannels(channels);
        }
    },
    GREEN("green") {
        @Override
        public SelectedChannelType getFrom(ChannelSelection sel) {
            return Optional.ofNullable(sel.getRGBChannels()).map(a -> a[1]).orElse(null);
        }

        @Override
        public void setTo(
                org.geotools.styling.ChannelSelection sel,
                org.geotools.styling.SelectedChannelType chan) {
            org.geotools.styling.SelectedChannelType channels[] = sel.getRGBChannels();
            channels[1] = chan;
            sel.setRGBChannels(channels);
        }
    },
    BLUE("blue") {
        @Override
        public SelectedChannelType getFrom(ChannelSelection sel) {
            return Optional.ofNullable(sel.getRGBChannels()).map(a -> a[2]).orElse(null);
        }

        @Override
        public void setTo(
                org.geotools.styling.ChannelSelection sel,
                org.geotools.styling.SelectedChannelType chan) {
            org.geotools.styling.SelectedChannelType channels[] = sel.getRGBChannels();
            channels[2] = chan;
            sel.setRGBChannels(channels);
        }
    };

    /** A list of the bands representing Red, Green, Blue (in order). */
    public static final List<Band> RGB =
            Collections.unmodifiableList(Arrays.asList(RED, GREEN, BLUE));

    /** Get the {@link SelectedChannelType} in sel that is represented by this band. */
    public abstract org.opengis.style.SelectedChannelType getFrom(
            org.opengis.style.ChannelSelection sel);

    /** Set the {@link SelectedChannelType} in sel that is represented by this band to chan. */
    public abstract void setTo(
            org.geotools.styling.ChannelSelection sel,
            org.geotools.styling.SelectedChannelType chan);

    public final String key;

    private Band(String key) {
        this.key = key;
    }
}
