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

import static org.geotools.gce.imagemosaic.GranuleDescriptor.EXCLUDE_MOSAIC;

import it.geosolutions.imageio.core.BasicAuthURI;
import it.geosolutions.imageio.core.SourceSPIProvider;
import it.geosolutions.imageioimpl.plugins.cog.CogImageInputStreamSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogSourceSPIProvider;
import java.io.IOException;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider;
import org.geotools.coverage.grid.io.imageio.MaskOverviewProvider.SpiHelper;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.imagemosaic.catalog.CatalogConfigurationBean;
import org.geotools.gce.imagemosaic.catalog.CogConfiguration;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;

/** GranuleAccessProvider for COG Granules. */
public class CogGranuleAccessProvider extends DefaultGranuleAccessProvider
        implements GranuleAccessProvider {

    private static final ImageReaderSpi DEFAULT_COG_IMAGE_READER_SPI = new CogImageReaderSpi();
    private static final ImageInputStreamSpi DEFAULT_COG_IMAGE_INPUT_STREAM_SPI =
            new CogImageInputStreamSpi();

    private static final AbstractGridFormat DEFAULT_COG_FORMAT = new GeoTiffFormat();

    private static final String DEFAULT_RANGE_READER =
            "it.geosolutions.imageioimpl.plugins.cog.HttpRangeReader";

    /** A COG configuration bean */
    private CogConfiguration cogConfig;

    public CogGranuleAccessProvider(CatalogConfigurationBean bean) {
        // A Cog Provider will always have at least a streamSpi and an ImageReaderSpi
        this(getHints(bean));
        SourceSPIProviderFactory urlSourceSpiProvider = bean.getUrlSourceSPIProvider();
        if (urlSourceSpiProvider instanceof CogConfiguration) {
            cogConfig = (CogConfiguration) urlSourceSpiProvider;
        } else {
            throw new RuntimeException(
                    "This access provider needs a URL Source SPI Provider of "
                            + "type CogConfiguration whilst "
                            + urlSourceSpiProvider
                            + " has been found.");
        }
        this.skipExternalOverviews = bean.isSkipExternalOverviews();
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
        AbstractGridFormat format = createFormatInstance(bean.getSuggestedFormat());
        if (format == null) {
            format = DEFAULT_COG_FORMAT;
        }
        hints.put(GranuleAccessProvider.SUGGESTED_FORMAT, format);
        if (bean.isSkipExternalOverviews()) {
            hints.put(Hints.SKIP_EXTERNAL_OVERVIEWS, true);
        }

        hints.add(EXCLUDE_MOSAIC);
        return hints;
    }

    public CogGranuleAccessProvider(Hints hints) {
        super(hints);
    }

    @Override
    public void setGranuleInput(Object input) throws IOException {
        BasicAuthURI cogUri = cogConfig.createUri(input.toString());
        String rangeReader = cogConfig.getRangeReader();
        if (rangeReader == null) {
            rangeReader = DEFAULT_RANGE_READER;
        }
        CogSourceSPIProvider sourceSPIProvider =
                new CogSourceSPIProvider(cogUri, imageReaderSpi, imageInputStreamSpi, rangeReader);
        this.input = sourceSPIProvider;
        this.inputURL = sourceSPIProvider.getSourceUrl();
    }

    @Override
    public MaskOverviewProvider getMaskOverviewsProvider() throws IOException {
        if (ovrProvider == null) {
            SourceSPIProvider inputProvider = (SourceSPIProvider) input;
            spiHelper = new SpiHelper(inputProvider);
            ovrProvider =
                    new MaskOverviewProvider(
                            null, inputProvider.getSourceUrl(), spiHelper, skipExternalOverviews);
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

    @Override
    public GranuleAccessProvider copyProviders() {
        CogGranuleAccessProvider provider = new CogGranuleAccessProvider(hints);
        provider.cogConfig = this.cogConfig;
        provider.skipExternalOverviews = this.skipExternalOverviews;
        return provider;
    }
}
