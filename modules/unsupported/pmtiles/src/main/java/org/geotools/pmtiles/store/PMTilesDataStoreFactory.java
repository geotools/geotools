/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.pmtiles.store;

import io.tileverse.cache.CacheManager;
import io.tileverse.cache.CacheStats;
import io.tileverse.io.ByteBufferPool;
import io.tileverse.io.ByteBufferPool.PoolStatistics;
import io.tileverse.pmtiles.PMTilesReader;
import io.tileverse.rangereader.RangeReader;
import io.tileverse.rangereader.RangeReaderFactory;
import io.tileverse.rangereader.azure.AzureBlobRangeReaderProvider;
import io.tileverse.rangereader.gcs.GoogleCloudStorageRangeReaderProvider;
import io.tileverse.rangereader.http.HttpRangeReaderProvider;
import io.tileverse.rangereader.s3.S3RangeReaderProvider;
import io.tileverse.rangereader.spi.RangeReaderConfig;
import io.tileverse.rangereader.spi.RangeReaderProvider;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.Parameter;
import org.geotools.tileverse.rangereader.ParamBuilder;
import org.geotools.tileverse.rangereader.RangeReaderParams;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

/**
 * Factory for creating {@link PMTilesDataStore} instances.
 *
 * <p>This factory supports creating PMTiles datastores from various sources including local files, HTTP/HTTPS servers,
 * and cloud storage providers (AWS S3, Azure Blob Storage, Google Cloud Storage). It uses the Tileverse Range Reader
 * library's SPI mechanism to automatically select the appropriate {@link RangeReaderProvider} based on the URI scheme.
 *
 * <p><b>Supported URI Schemes:</b>
 *
 * <ul>
 *   <li><b>Local files:</b> {@code file:/path/to/file.pmtiles}
 *   <li><b>AWS S3:</b> {@code s3://bucket/key.pmtiles} or {@code https://bucket.s3.amazonaws.com/key.pmtiles}
 *   <li><b>Azure Blob:</b> {@code https://account.blob.core.windows.net/container/key.pmtiles}
 *   <li><b>Google Cloud Storage:</b> {@code https://storage.googleapis.com/bucket/key.pmtiles}
 *   <li><b>HTTP/HTTPS:</b> {@code https://example.com/path/file.pmtiles}
 *   <li><b>S3-compatible (MinIO):</b> {@code http://localhost:9000/bucket/key.pmtiles}
 * </ul>
 *
 * <p><b>Configuration Parameters:</b>
 *
 * <ul>
 *   <li>{@linkplain #URIP pmtiles} - PMTiles archive URI (required)
 *   <li>{@linkplain #NAMESPACEP namespace} - Feature type namespace (optional)
 *   <li>{@linkplain #RANGEREADER_PROVIDER_ID io.tileverse.rangereader.provider} - Optionally force a specific
 *       {@code RangeReaderProvider} by its {@link RangeReaderProvider#getId() id}, defaults to automatic detection by
 *       {@link RangeReaderFactory#findBestProvider(io.tileverse.rangereader.spi.RangeReaderConfig)}
 *       <ul>
 *         <li>{@code http} for {@link HttpRangeReaderProvider}
 *         <li>{@code azure} for {@link AzureBlobRangeReaderProvider}
 *         <li>{@code s3} for {@link S3RangeReaderProvider}
 *         <li>{@code gcs} for {@link GoogleCloudStorageRangeReaderProvider}
 *       </ul>
 *   <li><strong>Caching parameters</strong>:
 *       <ul>
 *         <li>{@linkplain #MEMORY_CACHE_ENABLED io.tileverse.rangereader.caching.enabled} - Enable in-memory caching
 *       </ul>
 *   <li><strong>HTTP(S) parameters</strong>:
 *       <ul>
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_CONNECTION_TIMEOUT_MILLIS
 *             io.tileverse.rangereader.http.timeout-millis} - HTTP connection timeout in milliseconds
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_TRUST_ALL_SSL_CERTIFICATES
 *             io.tileverse.rangereader.http.trust-all-certificates} - Trust all SSL/TLS certificates
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_AUTH_USERNAME io.tileverse.rangereader.http.username} - HTTP
 *             Basic Auth username
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_AUTH_PASSWORD io.tileverse.rangereader.http.password} - HTTP
 *             Basic Auth password
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_AUTH_BEARER_TOKEN io.tileverse.rangereader.http.bearer-token} -
 *             HTTP Bearer Token
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_AUTH_API_KEY_HEADERNAME
 *             io.tileverse.rangereader.http.api-key-headername} - API-Key header name
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_AUTH_API_KEY io.tileverse.rangereader.http.api-key} - API key
 *             value
 *         <li>{@linkplain HttpRangeReaderProvider#HTTP_AUTH_API_KEY_VALUE_PREFIX
 *             io.tileverse.rangereader.http.api-key-value-prefix} - API key value prefix
 *       </ul>
 *   <li><strong>Azure Blob parameters</strong>:
 *       <ul>
 *         <li>{@linkplain AzureBlobRangeReaderProvider#AZURE_BLOB_NAME io.tileverse.rangereader.azure.blob-name} - Set
 *             the Azure blob name if the endpoint points to the account url
 *         <li>{@linkplain AzureBlobRangeReaderProvider#AZURE_ACCOUNT_KEY io.tileverse.rangereader.azure.account-key} -
 *             Azure Account access key
 *         <li>{@linkplain AzureBlobRangeReaderProvider#AZURE_SAS_TOKEN io.tileverse.rangereader.azure.sas-token} -
 *             Azure SAS token to use for authenticating requests
 *       </ul>
 *   <li><strong>AWS S3 parameters</strong>:
 *       <ul>
 *         <li>{@linkplain S3RangeReaderProvider#S3_FORCE_PATH_STYLE io.tileverse.rangereader.s3.force-path-style} -
 *             Enable S3 path style access
 *         <li>{@linkplain S3RangeReaderProvider#S3_AWS_REGION io.tileverse.rangereader.s3.region} - Configure the
 *             region with which the AWS S3 SDK should communicate
 *         <li>{@linkplain S3RangeReaderProvider#S3_AWS_ACCESS_KEY_ID io.tileverse.rangereader.s3.aws-access-key-id} -
 *             AWS Access Key ID
 *         <li>{@linkplain S3RangeReaderProvider#S3_AWS_SECRET_ACCESS_KEY
 *             io.tileverse.rangereader.s3.aws-secret-access-key} - AWS Secret Access Key
 *         <li>{@linkplain S3RangeReaderProvider#S3_USE_DEFAULT_CREDENTIALS_PROVIDER
 *             io.tileverse.rangereader.s3.use-default-credentials-provider} - Use Default Credentials Provider
 *         <li>{@linkplain S3RangeReaderProvider#S3_DEFAULT_CREDENTIALS_PROFILE
 *             io.tileverse.rangereader.s3.default-credentials-profile} - Default Credentials Profile
 *       </ul>
 *   <li><strong>Google Cloud Storage parameters</strong>:
 *       <ul>
 *         <li>{@linkplain GoogleCloudStorageRangeReaderProvider#GCS_PROJECT_ID io.tileverse.rangereader.gcs.project-id}
 *             - Google Cloud project ID
 *         <li>{@linkplain GoogleCloudStorageRangeReaderProvider#GCS_QUOTA_PROJECT_ID
 *             io.tileverse.rangereader.gcs.quota-project-id} - Quota ProjectId that specifies the project used for
 *             quota and billing purposes
 *         <li>{@linkplain GoogleCloudStorageRangeReaderProvider#GCS_USE_DEFAULT_APPLICTION_CREDENTIALS} - Whether to
 *             use the default application credentials chain
 *       </ul>
 * </ul>
 *
 * @see PMTilesDataStore
 * @see RangeReaderParams
 * @see <a href="https://tileverse.io">Tileverse Documentation</a>
 */
@NullMarked
@SuppressWarnings("java:S2629")
public class PMTilesDataStoreFactory implements DataStoreFactorySpi {

    private static final Logger LOGGER = Logging.getLogger(PMTilesDataStoreFactory.class);

    /** Optional - uri of the FeatureType's namespace */
    public static final Param NAMESPACEP = ParamBuilder.builder()
            .key("namespace")
            .title("Namespace prefix")
            .type(String.class)
            .optional()
            .advancedLevel()
            .build();

    /** URI to the PMTiles file. */
    public static final Param URIP = ParamBuilder.builder()
            .key("pmtiles")
            .type(String.class)
            .required(true)
            .title(
                    """
			URI to a Protomaps PMTiles file containing Vector Tiles. Supports local files and cloud storage:
			• Local files: file:/path/to/file.pmtiles
			• AWS S3: s3://bucket/key.pmtiles or https://bucket.s3.amazonaws.com/key.pmtiles
			• Azure Blob Storage: https://account.blob.core.windows.net/container/key.pmtiles
			• Google Cloud Storage: https://storage.googleapis.com/bucket/key.pmtiles
			• HTTP/HTTPS: https://example.com/path/file.pmtiles (with optional authentication)
			• MinIO and S3-compatible services: http://localhost:9000/bucket/key.pmtiles
			""")
            .metadata(Parameter.EXT, "pmtiles")
            .metadata(Parameter.IS_LARGE_TEXT, true)
            .build();

    /**
     * Optional parameter to force selecting a specific {@link RangeReaderProvider} by its
     * {@link RangeReaderProvider#getId() id}
     */
    public static final Param RANGEREADER_PROVIDER_ID = RangeReaderParams.RANGEREADER_PROVIDER_ID;

    /**
     * Enable memory cache for raw byte data.
     *
     * <p>{@link RangeReaderParams#MEMORY_CACHE_BLOCK_ALIGNED} and {@link RangeReaderParams#MEMORY_CACHE_BLOCK_SIZE} are
     * not included because they don't make sense for PMTiles, which is optimized for byte-range requests natively
     */
    public static final Param MEMORY_CACHE_ENABLED = RangeReaderParams.MEMORY_CACHE_ENABLED;

    public static final Param HTTP_CONNECTION_TIMEOUT_MILLIS = RangeReaderParams.HTTP_CONNECTION_TIMEOUT_MILLIS;
    public static final Param HTTP_TRUST_ALL_SSL_CERTIFICATES = RangeReaderParams.HTTP_TRUST_ALL_SSL_CERTIFICATES;
    public static final Param HTTP_AUTH_USERNAME = RangeReaderParams.HTTP_AUTH_USERNAME;
    public static final Param HTTP_AUTH_PASSWORD = RangeReaderParams.HTTP_AUTH_PASSWORD;
    public static final Param HTTP_AUTH_BEARER_TOKEN = RangeReaderParams.HTTP_AUTH_BEARER_TOKEN;
    public static final Param HTTP_AUTH_API_KEY_HEADERNAME = RangeReaderParams.HTTP_AUTH_API_KEY_HEADERNAME;
    public static final Param HTTP_AUTH_API_KEY = RangeReaderParams.HTTP_AUTH_API_KEY;
    public static final Param HTTP_AUTH_API_KEY_VALUE_PREFIX = RangeReaderParams.HTTP_AUTH_API_KEY_VALUE_PREFIX;

    /** Set the blob name if the endpoint points to the account url */
    public static final Param AZURE_BLOB_NAME = RangeReaderParams.AZURE_BLOB_NAME;
    /** Account access key */
    public static final Param AZURE_ACCOUNT_KEY = RangeReaderParams.AZURE_ACCOUNT_KEY;
    /** SAS token to use for authenticating requests */
    public static final Param AZURE_SAS_TOKEN = RangeReaderParams.AZURE_SAS_TOKEN;

    /** Enable S3 path style access */
    public static final Param S3_FORCE_PATH_STYLE = RangeReaderParams.S3_FORCE_PATH_STYLE;
    /** Force an AWS region */
    public static final Param S3_AWS_REGION = RangeReaderParams.S3_AWS_REGION;
    /** AWS Access Key */
    public static final Param S3_AWS_ACCESS_KEY_ID = RangeReaderParams.S3_AWS_ACCESS_KEY_ID;
    /** AWS Secret Access Key */
    public static final Param S3_AWS_SECRET_ACCESS_KEY = RangeReaderParams.S3_AWS_SECRET_ACCESS_KEY;
    /** Whether to use the Default Credentials Provider */
    public static final Param S3_USE_DEFAULT_CREDENTIALS_PROVIDER =
            RangeReaderParams.S3_USE_DEFAULT_CREDENTIALS_PROVIDER;
    /** Default Credentials Profile */
    public static final Param S3_DEFAULT_CREDENTIALS_PROFILE = RangeReaderParams.S3_DEFAULT_CREDENTIALS_PROFILE;

    /** Google Cloud project ID */
    public static final Param GCS_PROJECT_ID = RangeReaderParams.GCS_PROJECT_ID;
    /** Quota Project ID */
    public static final Param GCS_QUOTA_PROJECT_ID = RangeReaderParams.GCS_QUOTA_PROJECT_ID;
    /** Whether to use the default application credentials chain, defaults to {@code false} */
    public static final Param GCS_USE_DEFAULT_APPLICTION_CREDENTIALS =
            RangeReaderParams.GCS_USE_DEFAULT_APPLICTION_CREDENTIALS;

    @NonNull
    private static CacheManager cacheManager = CacheManager.getDefault();

    /**
     * Sets the global {@link CacheManager} used by all PMTiles datastores created by this factory.
     *
     * <p>This allows applications to provide a custom cache manager for fine-grained control over caching behavior,
     * memory limits, and eviction policies. By default, uses {@link CacheManager#getDefault()}.
     *
     * @param cacheManager the cache manager to use, must not be {@code null}
     * @see #getCacheManager()
     * @see #clearCaches()
     */
    public static void setCacheManager(@NonNull CacheManager cacheManager) {
        PMTilesDataStoreFactory.cacheManager = cacheManager;
    }

    /**
     * Returns the current global {@link CacheManager} used by PMTiles datastores.
     *
     * @return the current cache manager, never {@code null}
     */
    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * Invalidates all caches managed by the current {@link CacheManager} and clears the {@link ByteBufferPool}.
     *
     * <p>This method is useful for:
     *
     * <ul>
     *   <li>Forcing a refresh when the underlying PMTiles file has been updated
     *   <li>Freeing memory under pressure
     *   <li>Testing and debugging cache behavior
     * </ul>
     *
     * <p>Cache statistics are logged at CONFIG level before invalidation.
     */
    public static void clearCaches() {
        Map<String, CacheStats> stats = cacheManager.stats();
        cacheManager.invalidateAll();
        stats.forEach((name, cache) -> LOGGER.config("Invalidated cache %s: %s".formatted(name, cache)));

        ByteBufferPool pool = ByteBufferPool.getDefault();
        PoolStatistics byteBufferPoolStats = pool.getStatistics();
        pool.clear();
        LOGGER.config("Invalidated %s".formatted(byteBufferPoolStats));
    }

    @Override
    public String getDisplayName() {
        return "PMTiles";
    }

    @Override
    public String getDescription() {
        return "Protomaps (.pmtiles) with vector tiles";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    /**
     * Dynamically builds the list of configuration parameters based on which {@link RangeReaderProvider}s are
     * available.
     *
     * @see RangeReaderParams#appendAfter(org.geotools.api.data.DataAccessFactory.Param...)
     */
    @Override
    public Param[] getParametersInfo() {
        List<String> ignore = Stream.of(
                        RangeReaderParams.MEMORY_CACHE_BLOCK_ALIGNED, RangeReaderParams.MEMORY_CACHE_BLOCK_SIZE)
                .map(p -> p.key)
                .toList();

        Predicate<Param> filter = p -> !ignore.contains(p.key);
        return RangeReaderParams.appendAfter(filter, NAMESPACEP, URIP);
    }

    @Override
    public DataStore createNewDataStore(Map<String, ?> params) {
        throw new UnsupportedOperationException("Creating new PMTiles files is unsupported");
    }

    @Override
    public boolean canProcess(java.util.Map<String, ?> params) {
        boolean canProcess = DataStoreFactorySpi.super.canProcess(params);
        URI toURI;
        try {
            toURI = lookup(URIP, params, URI.class);
        } catch (Exception e) {
            return false;
        }
        if (canProcess && (toURI == null)) {
            return false;
        }
        if (!canProcess && toURI != null) {
            Map<String, Object> p = new HashMap<>(params);
            p.put(URIP.key, toURI.toString());
            return DataStoreFactorySpi.super.canProcess(p);
        }
        return canProcess;
    }

    @Override
    public PMTilesDataStore createDataStore(Map<String, ?> params) throws IOException {

        @SuppressWarnings("PMD.CloseResource")
        PMTilesReader pmtilesReader = createPMTilesReader(params);
        PMTilesDataStore store = new PMTilesDataStore(this, pmtilesReader);

        String namespaceURI = lookup(NAMESPACEP, params, String.class);
        store.setNamespaceURI(namespaceURI);

        return store;
    }

    @SuppressWarnings("PMD.CloseResource")
    private PMTilesReader createPMTilesReader(Map<String, ?> params) throws IOException {
        RangeReader reader = createRangeReader(params);
        PMTilesReader pmTilesReader = new PMTilesReader(reader);
        pmTilesReader.cacheManager(cacheManager);
        return pmTilesReader;
    }

    static RangeReader createRangeReader(Map<String, ?> params) throws IOException {
        URI uri = lookup(URIP, params, URI.class);

        Properties rangeReaderConfig = RangeReaderParams.toProperties(params);
        return RangeReaderFactory.create(uri, rangeReaderConfig);
    }

    /**
     * Looks up a parameter, if not found it returns the default value, assuming there is one, or null otherwise
     *
     * @param <T>
     * @throws IOException
     */
    static <T> T lookup(Param param, Map<String, ?> params, Class<T> target) throws IOException {
        final Object rawValue = params.get(param.key);
        Object lookUp;
        if (param == URIP) {
            if (rawValue == null) {
                lookUp = null;
            } else {
                lookUp = RangeReaderConfig.convertToURI(rawValue);
            }
        } else {
            lookUp = param.lookUp(params);
        }
        if (lookUp == null) {
            lookUp = param.getDefaultValue();
        }
        T converted = Converters.convert(lookUp, target);
        boolean required = param.isRequired() != null && param.isRequired();
        if (required && converted == null) {
            String v = param.isPassword() ? "*****" : String.valueOf(rawValue);
            String actualType = rawValue == null ? "null" : rawValue.getClass().getCanonicalName();
            String expectedType = param.getType().getCanonicalName();
            throw new IllegalArgumentException("Unable to convert parameter %s with value %s of type %s to %s"
                    .formatted(param.key, v, actualType, expectedType));
        }
        return converted;
    }
}
