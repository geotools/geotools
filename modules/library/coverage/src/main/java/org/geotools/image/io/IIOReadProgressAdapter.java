/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

// Image I/O
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;


/**
 * An abstract adapter class for receiving image progress events.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 *
 * @since 2.1
 * @author Martin Desruisseaux (IRD)
 *
 * @source $URL$
 * @version $Id$
 */
public class IIOReadProgressAdapter implements IIOReadProgressListener {
    /**
     * Reports that a sequence of read operations is beginning.
     */
    public void sequenceStarted(ImageReader source, int minIndex) {
    }
    
    /**
     * Reports that a sequence of read operationshas completed.
     */
    public void sequenceComplete(ImageReader source) {
    }
    
    /**
     * Reports that an image read operation is beginning.
     */
    public void imageStarted(ImageReader source, int imageIndex) {
    }
    
    /**
     * Reports the approximate degree of completion of the current
     * {@code read} call of the associated {@code ImageReader}.
     */
    public void imageProgress(ImageReader source, float percentageDone) {
    }
    
    /**
     * Reports that the current image read operation has completed.
     */
    public void imageComplete(ImageReader source) {
    }
    
    /**
     * Reports that a thumbnail read operation is beginning.
     */
    public void thumbnailStarted(ImageReader source, int imageIndex, int thumbnailIndex) {
    }
    
    /**
     * Reports the approximate degree of completion of the current {@code getThumbnail}
     * call within the associated {@code ImageReader}.
     */
    public void thumbnailProgress(ImageReader source, float percentageDone) {
    }
    
    /**
     * Reports that a thumbnail read operation has completed.
     */
    public void thumbnailComplete(ImageReader source) {
    }
    
    /**
     * Reports that a read has been aborted via the reader's {@code abort} method.
     */
    public void readAborted(ImageReader source) {
    }
}
