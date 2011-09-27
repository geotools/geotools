package org.geotools.gce;

import java.awt.geom.AffineTransform;

import javax.imageio.ImageTypeSpecifier;

import org.geotools.coverage.grid.RasterLayout;
import org.geotools.coverage.grid.io.imageio.ImageReaderSource;
import org.geotools.geometry.jts.ReferencedEnvelope;


/**
 * 
 *
 * @source $URL$
 */
public  class RasterSlice{
    public RasterLayout rasterDimensions;
    
    public ReferencedEnvelope envelope;
    
    public AffineTransform gridToWorld;
            
    public boolean overview;
    
    public ImageReaderSource source;

    public ImageTypeSpecifier imageType;
    
}
