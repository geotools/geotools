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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import org.geotools.coverage.grid.io.FileSetManager;
import org.geotools.coverage.io.CoverageSourceDescriptor;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog.WrappedCoverageSlicesCatalog;
import org.geotools.coverage.io.catalog.DataStoreConfiguration;
import org.geotools.data.Query;
import org.geotools.data.Repository;
import org.geotools.util.SuppressFBWarnings;
import org.opengis.feature.type.Name;

/**
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public abstract class GeoSpatialImageReader extends ImageReader implements FileSetManager {

    /** The source file */
    protected File file;

    /** the coverage slices slicesCatalog */
    CoverageSlicesCatalog slicesCatalog;

    protected int numImages = -1;

    private String auxiliaryFilesPath = null;

    /** Path of the auxiliary datastore properties file, used as low level granules index */
    private String auxiliaryDatastorePath = null;

    Repository repository;

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

        try {
            if (slicesCatalog != null) {
                slicesCatalog.dispose();
            }
        } catch (Throwable t) {

        } finally {
            slicesCatalog = null;
        }
    }

    @Override
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    /**
     * Simple check of the specified image index. Valid indexes are belonging the range [0 -
     * numRasters]. In case this constraint is not respected, an {@link IndexOutOfBoundsException}
     * is thrown.
     *
     * @param imageIndex the index to be checked
     * @throw {@link IndexOutOfBoundsException} in case the provided imageIndex is not in the range
     *     of supported ones.
     */
    @SuppressFBWarnings("INT_BAD_COMPARISON_WITH_NONNEGATIVE_VALUE")
    protected void checkImageIndex(final int imageIndex) {
        if (imageIndex < 0 || imageIndex >= numImages) {
            throw new IndexOutOfBoundsException(
                    "Invalid imageIndex "
                            + imageIndex
                            + ", it should "
                            + (numImages > 0
                                    ? ("belong the range [0," + (numImages - 1))
                                    : "be 0"));
        }
    }

    public int getNumImages(final boolean allowSearch) throws IOException {
        return numImages;
    }

    /** Return the name of coverages made available by this provider */
    public abstract Collection<Name> getCoveragesNames();

    /** The number of coverages made available by this provider. */
    public abstract int getCoveragesNumber();

    /** */
    public abstract CoverageSourceDescriptor getCoverageDescriptor(Name name);

    protected void setCatalog(CoverageSlicesCatalog catalog) {
        if (slicesCatalog != null) {
            slicesCatalog.dispose();
        }
        slicesCatalog = catalog;
    }

    /**
     * Return the list of imageIndex related to the feature in the slicesCatalog which result from
     * the specified query.
     *
     * @param filterQuery the filter query (temporal, vertical, name selection) to restrict the
     *     requested imageIndexes
     */
    public List<Integer> getImageIndex(Query filterQuery) throws IOException {
        List<CoverageSlice> descs = slicesCatalog.getGranules(filterQuery);
        List<Integer> indexes = new ArrayList<Integer>();
        for (CoverageSlice desc : descs) {
            Integer index =
                    (Integer) desc.getOriginator().getAttribute(CoverageSlice.Attributes.INDEX);
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

    public String getAuxiliaryDatastorePath() {
        return auxiliaryDatastorePath;
    }

    public void setAuxiliaryDatastorePath(String auxiliaryDatastorePath) {
        this.auxiliaryDatastorePath = auxiliaryDatastorePath;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    /** Returns the underlying slicesCatalog. */
    public CoverageSlicesCatalog getCatalog() {
        return slicesCatalog;
    }

    /** Initialize a slicesCatalog on top of the provided {@link DataStoreConfiguration} instance */
    protected void initCatalog(DataStoreConfiguration datastoreConfig) throws IOException {
        slicesCatalog =
                datastoreConfig.isShared()
                        ? new WrappedCoverageSlicesCatalog(datastoreConfig, file, repository)
                        : new CoverageSlicesCatalog(datastoreConfig, repository);
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }
}
