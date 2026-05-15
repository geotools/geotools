/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio;

import it.geosolutions.imageio.core.InitializingReader;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import org.geotools.api.data.Query;
import org.geotools.api.feature.type.Name;
import org.geotools.coverage.grid.io.FileSetManager;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.util.factory.Hints;

/**
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public abstract class GeoSpatialImageReader extends ImageReader implements FileSetManager, InitializingReader {

    /** The source file */
    protected File file;

    /** the coverage slices slicesCatalog */
    protected CoverageSlicesCatalog slicesCatalog;

    protected int numImages = -1;

    private String auxiliaryFilesPath = null;

    protected GeoSpatialImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    @Override
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        checkImageIndex(imageIndex);
        // metadata is not supported, the interface allows for returning null
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        slicesCatalog = null;
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    /**
     * Simple check of the specified image index. Valid indexes are belonging the range [0 - numRasters]. In case this
     * constraint is not respected, an {@link IndexOutOfBoundsException} is thrown.
     *
     * @param imageIndex the index to be checked
     * @throw {@link IndexOutOfBoundsException} in case the provided imageIndex is not in the range of supported ones.
     */
    @SuppressFBWarnings("INT_BAD_COMPARISON_WITH_NONNEGATIVE_VALUE")
    protected void checkImageIndex(final int imageIndex) {
        if (imageIndex < 0 || imageIndex >= numImages) {
            throw new IndexOutOfBoundsException("Invalid imageIndex "
                    + imageIndex
                    + ", it should "
                    + (numImages > 0 ? "belong the range [0," + (numImages - 1) : "be 0"));
        }
    }

    @Override
    public int getNumImages(final boolean allowSearch) throws IOException {
        return numImages;
    }

    /** Return the name of coverages made available by this provider */
    public abstract Collection<Name> getCoveragesNames();

    /** The number of coverages made available by this provider. */
    public abstract int getCoveragesNumber();

    /** */
    public abstract CoverageSourceDescriptor getCoverageDescriptor(Name name);

    /**
     * Return the list of imageIndex related to the feature in the slicesCatalog which result from the specified query.
     *
     * @param filterQuery the filter query (temporal, vertical, name selection) to restrict the requested imageIndexes
     */
    public List<Integer> getImageIndex(Query filterQuery) throws IOException {
        List<CoverageSlice> descs = slicesCatalog.getGranules(filterQuery);
        List<Integer> indexes = new ArrayList<>();
        for (CoverageSlice desc : descs) {
            Integer index = (Integer) desc.getOriginator().getAttribute(CoverageSlice.Attributes.INDEX);
            indexes.add(index);
        }
        return indexes;
    }

    public String getAuxiliaryFilesPath() {
        return auxiliaryFilesPath;
    }

    public void setAuxiliaryFilesPath(String auxiliaryFilesPath) {
        this.auxiliaryFilesPath = auxiliaryFilesPath;
    }

    /** Returns the underlying slicesCatalog. */
    public CoverageSlicesCatalog getCatalog() {
        return slicesCatalog;
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    @Override
    public boolean init(RenderingHints hints) {
        if (hints != null && hints.containsKey(Utils.AUXILIARY_FILES_PATH)) {
            String path = getPath(hints, Utils.AUXILIARY_FILES_PATH);
            if (path != null) {
                setAuxiliaryFilesPath(path);
            }
            return true;
        }
        return false;
    }

    private String getPath(RenderingHints hints, Hints.Key key) {
        String filePath = (String) hints.get(key);
        if (filePath != null && hints.containsKey(Utils.PARENT_DIR)) {
            String parentDir = (String) hints.get(Utils.PARENT_DIR);
            // check if the file is not already absolute (old configuration file)
            if (!new File(filePath).isAbsolute()) {
                filePath = parentDir + File.separatorChar + filePath;
            }
        }
        return filePath;
    }
}
