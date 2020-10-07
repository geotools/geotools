/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import it.geosolutions.imageio.core.SourceSPIProvider;
import it.geosolutions.imageio.maskband.DatasetLayout;
import it.geosolutions.imageioimpl.plugins.cog.CogImageInputStreamSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogSourceSPIProvider;
import it.geosolutions.imageioimpl.plugins.cog.CogUri;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider.SpiHelper;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.CogConfigurationBean;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;

import static org.geotools.gce.imagemosaic.GranuleDescriptor.EXCLUDE_MOSAIC;

/** GranuleAccessProvider for COG Granules. */
class CogGranuleAccessProvider extends DefaultGranuleAccessProvider
        implements GranuleAccessProvider {

    private static final ImageReaderSpi DEFAULT_COG_IMAGE_READER_SPI = new CogImageReaderSpi();
    private static final ImageInputStreamSpi DEFAULT_COG_IMAGE_INPUT_STREAM_SPI =
            new CogImageInputStreamSpi();

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(CogGranuleAccessProvider.class);

    /** A catalog bean containing details o */
    private CogConfigurationBean cogConfig;

    public CogGranuleAccessProvider(CatalogConfigurationBean bean) {
        // A Cog Provider will always have at least a streamSpi and an ImageReaderSpi
        this(getHints(bean));
        cogConfig = bean.getCogBean();
    }

    private static Hints getHints(CatalogConfigurationBean bean) {
        // A Cog provider will always have an ImageReaderSpi and an ImageInputStream
        // Using defaults COGs when not specified
        Utilities.ensureNonNull("CatalogConfigurationBean", bean);
        Hints hints = new Hints();
        ImageReaderSpi readerSpi = createImageReaderSpiInstance(bean.getSuggestedSPI());
        if (readerSpi == null) {
            readerSpi = DEFAULT_COG_IMAGE_READER_SPI;
        }
        hints.put(GranuleAccessProvider.SUGGESTED_READER_SPI, readerSpi);
        ImageInputStreamSpi imageInputStreamSpi =
                createImageInputStreamSpiInstance(bean.getSuggestedIsSPI());
        if (imageInputStreamSpi == null) {
            imageInputStreamSpi = DEFAULT_COG_IMAGE_INPUT_STREAM_SPI;
        }
        hints.put(GranuleAccessProvider.SUGGESTED_STREAM_SPI, imageInputStreamSpi);
        hints.add(EXCLUDE_MOSAIC);
        return hints;
    }

    public CogGranuleAccessProvider(Hints hints) {
        super(hints);
    }

    @Override
    public void setGranuleInput(Object input) throws IOException {
        this.inputUrl = (URL) input;
        CogUri cogUri = cogConfig.createUri(inputUrl.toString());
        this.input =
                new CogSourceSPIProvider(
                        cogUri, imageReaderSpi, imageInputStreamSpi, cogConfig.getRangeReader());
    }

    @Override
    public MaskOverviewProvider getMaskOverviewsProvider() throws IOException {
        if (ovrProvider == null) {
            AbstractGridCoverage2DReader reader = getGridCoverageReader();
            DatasetLayout layout = reader.getDatasetLayout();
            spiHelper = new SpiHelper((SourceSPIProvider) input);
            ovrProvider = new MaskOverviewProvider(layout, inputUrl, spiHelper, true);
        }
        if (ovrProvider == null) {
            throw new IOException(
                    "Unable to find a MaskOverviewProvider for the specified input: " + input);
        }
        return ovrProvider;
    }

    @Override
    public ImageInputStreamSpi getInputStreamSpi() throws IOException {
        // The COG Granule Access Provider only works with COG Streams SPI.
        // only the suggested COG Stream SPI will be useful
        return imageInputStreamSpi;
    }

    @Override
    public ImageReaderSpi getImageReaderSpi() throws IOException {
        // The COG Granule Access Provider only works with COG ImageReader SPI.
        // only the suggested COG ImageReader SPI will be useful
        return imageReaderSpi;
    }

    @Override
    public ImageInputStream getImageInputStream() throws IOException {
        return ((CogSourceSPIProvider) input).getStream();
    }
}
