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
package org.geotools.gce.imagemosaic.catalog;

import it.geosolutions.imageio.core.BasicAuthURI;
import it.geosolutions.imageio.core.SourceSPIProvider;
import it.geosolutions.imageioimpl.plugins.cog.CogImageInputStreamSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogImageReaderSpi;
import it.geosolutions.imageioimpl.plugins.cog.CogSourceSPIProvider;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import org.apache.commons.beanutils.BeanUtils;
import org.geotools.gce.imagemosaic.SourceSPIProviderFactory;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;
import org.geotools.util.Utilities;

/** Bean containing all COG related configuration properties */
public class CogConfiguration implements SourceSPIProviderFactory {

    private static final ImageReaderSpi COG_IMAGE_READER_SPI = new CogImageReaderSpi();

    private static final ImageInputStreamSpi COG_IMAGE_INPUT_STREAM_SPI =
            new CogImageInputStreamSpi();

    public CogConfiguration() {};

    public CogConfiguration(final CogConfiguration that) {
        Utilities.ensureNonNull("CogConfiguration", that);
        try {
            BeanUtils.copyProperties(this, that);
        } catch (IllegalAccessException | InvocationTargetException e) {
            final IllegalArgumentException iae = new IllegalArgumentException(e);
            throw iae;
        }
    }

    public CogConfiguration(final Indexer indexer) {
        String cogRangeReader = IndexerUtils.getParameter(Utils.Prop.COG_RANGE_READER, indexer);
        if (cogRangeReader != null) {
            this.rangeReader = cogRangeReader;
        } else {
            this.rangeReader = Utils.DEFAULT_RANGE_READER;
        }
        String user = IndexerUtils.getParameter(Utils.Prop.COG_USER, indexer);
        if (user != null) {
            this.user = user;
        }
        String password = IndexerUtils.getParameter(Utils.Prop.COG_PASSWORD, indexer);
        if (password != null) {
            this.password = password;
        }
        if (IndexerUtils.getParameterAsBoolean(Utils.Prop.COG_USE_CACHE, indexer)) {
            this.useCache = true;
        }
    }

    /** Optional user/accessId to access the data */
    private String user;

    /** Optional password/secret key to access the data */
    private String password;

    /** Full classname of the rangeReader implementation being used */
    private String rangeReader;

    /** Whether to use Caching stream or not */
    private boolean useCache;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRangeReader() {
        return rangeReader;
    }

    public void setRangeReader(String rangeReader) {
        this.rangeReader = rangeReader;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    /**
     * Create a {@link BasicAuthURI} instance on top of the provided source String optionally
     * embedding (when provided) user/pass credentials as UserInfo component of the underlying URI.
     */
    public BasicAuthURI createUri(String source) {
        // Create basic uri
        URI uri = URI.create(source);
        return new BasicAuthURI(uri, isUseCache(), getUser(), getPassword());
    }

    @Override
    public SourceSPIProvider getSourceSPIProvider(Object sourceUrl) {
        SourceSPIProvider readerInputObject =
                new CogSourceSPIProvider(
                        createUri(sourceUrl.toString()),
                        COG_IMAGE_READER_SPI,
                        COG_IMAGE_INPUT_STREAM_SPI,
                        getRangeReader());
        return readerInputObject;
    }
}
