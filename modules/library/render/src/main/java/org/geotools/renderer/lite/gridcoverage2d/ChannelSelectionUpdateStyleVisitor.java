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

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.ShadedRelief;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.filter.expression.Expression;

/**
 * ChannelSelectionUpdateStyleVisitor is a {@link DuplicatingStyleVisitor} that is used 
 * to "reset" style symbolizer's selection channel order when the GridCoverageReader 
 * used to read a coverage supports band selection. If a reader supports band selection
 * then channel ordering is done by band selection from the reader, so symbolizer does
 * not need to re-apply selection channel order.
 * 
 * Also, see {@link AbstractGridFormat#BANDS} for the reader band selection parameter 
 * description.
 * 
 * @source $URL$
 * @version $Id$
 *
 */

public class ChannelSelectionUpdateStyleVisitor extends DuplicatingStyleVisitor{

    private SelectedChannelType[] channels;
    
    public ChannelSelectionUpdateStyleVisitor(SelectedChannelType[] channels){
        super();
        this.channels = channels;
    }
    
    @Override
    public void visit(RasterSymbolizer raster) {

        ChannelSelection channelSelection = createChannelSelection();

        ColorMap colorMap = copy(raster.getColorMap());
        ContrastEnhancement ce = copy(raster.getContrastEnhancement());
        String geometryProperty = raster.getGeometryPropertyName();
        Symbolizer outline = copy(raster.getImageOutline());
        Expression overlap = copy(raster.getOverlap());
        ShadedRelief shadedRelief = copy(raster.getShadedRelief());

        Expression opacity = copy(raster.getOpacity());

        RasterSymbolizer copy = sf.createRasterSymbolizer(geometryProperty, opacity,
                channelSelection, overlap, colorMap, ce,
                shadedRelief, outline);
        if (STRICT && !copy.equals(raster)) {
            throw new IllegalStateException("Was unable to duplicate provided raster:" + raster);
        }
        pages.push(copy);
    }

    private ChannelSelection createChannelSelection() {
        if (channels.length != 3) {
            return sf.createChannelSelection(new SelectedChannelType[]{channels[0]});
        } else {
            return sf.createChannelSelection(channels);
        }
    }
    
    /**
     * Returns an int[] containing the indices of the coverage bands that are used for the
     * symbolizer's selection channels
     * @param symbolizer The input symbolizer
     * @return the band indices array (null if no channel selection was present in symbolizer)
     */
    
    public static int[] getBandIndicesFromSelectionChannels(RasterSymbolizer symbolizer){
        int[] bandIndices = null;
        ChannelSelection channelSelection = symbolizer.getChannelSelection();
        if (channelSelection!=null){
            SelectedChannelType[] channels = channelSelection.getSelectedChannels();
            if (channels!=null){
                    bandIndices = new int[channels.length];
                    for (int i=0;i<channels.length;i++){
                        //Note that in channel selection, channels start at index 1
                        bandIndices[i]= Integer.parseInt(channels[i].getChannelName())-1;
                    }
            }
        }
        return bandIndices;
    }
}
