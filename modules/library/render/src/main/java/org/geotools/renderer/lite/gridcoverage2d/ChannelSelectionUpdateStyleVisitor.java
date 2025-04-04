/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.renderer.lite.gridcoverage2d;

import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;

/**
 * ChannelSelectionUpdateStyleVisitor is a {@link DuplicatingStyleVisitor} that is used to "reset" style symbolizer's
 * selection channel order when the GridCoverageReader used to read a coverage supports band selection. If a reader
 * supports band selection then channel ordering is done by band selection from the reader, so symbolizer does not need
 * to re-apply selection channel order.
 *
 * <p>Also, see {@link AbstractGridFormat#BANDS} for the reader band selection parameter description.
 *
 * @version $Id$
 */
public class ChannelSelectionUpdateStyleVisitor extends DuplicatingStyleVisitor {

    private SelectedChannelType[] channels;

    public ChannelSelectionUpdateStyleVisitor(SelectedChannelType[] channels) {
        super();
        this.channels = channels;
    }

    @Override
    protected ChannelSelection copy(ChannelSelection channelSelection) {
        if (channels.length != 3) {
            return sf.createChannelSelection(channels[0]);
        } else {
            return sf.createChannelSelection(channels);
        }
    }

    /**
     * Returns an int[] containing the indices of the coverage bands that are used for the symbolizer's selection
     * channels
     *
     * @param symbolizer The input symbolizer
     * @return the band indices array (null if no channel selection was present in symbolizer)
     */
    public static int[] getBandIndicesFromSelectionChannels(RasterSymbolizer symbolizer) {
        int[] bandIndices = null;
        ChannelSelection channelSelection = symbolizer.getChannelSelection();
        if (channelSelection != null) {
            SelectedChannelType[] channels = channelSelection.getRGBChannels();
            if (channels != null && channels.length > 0 && channels[0] != null) {
                bandIndices = new int[channels.length];
                for (int i = 0; i < channels.length; i++) {
                    if (channels[i] == null) return null;
                    // Note that in channel selection, channels start at index 1
                    bandIndices[i] = channels[i].getChannelName().evaluate(null, Integer.class) - 1;
                }
            }
            SelectedChannelType grayChannel = channelSelection.getGrayChannel();
            if (grayChannel != null) {
                bandIndices = new int[1];
                bandIndices[0] = grayChannel.getChannelName().evaluate(null, Integer.class) - 1;
            }
        }
        return bandIndices;
    }
}
