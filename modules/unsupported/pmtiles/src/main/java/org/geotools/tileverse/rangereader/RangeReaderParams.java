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
package org.geotools.tileverse.rangereader;

import com.google.api.client.util.store.DataStoreFactory;
import io.tileverse.storage.StorageConfig;
import io.tileverse.storage.StorageFactory;
import io.tileverse.storage.StorageParameter;
import io.tileverse.storage.azure.AzureBlobStorageProvider;
import io.tileverse.storage.gcs.GoogleCloudStorageProvider;
import io.tileverse.storage.http.HttpStorageProvider;
import io.tileverse.storage.s3.S3StorageProvider;
import io.tileverse.storage.spi.AbstractStorageProvider;
import io.tileverse.storage.spi.StorageProvider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.geotools.api.data.DataAccessFactory.Param;
import org.geotools.pmtiles.store.PMTilesDataStoreFactory;
import org.geotools.util.Converters;

/**
 * Bridges Tileverse Range Reader configuration to GeoTools DataStore parameters.
 *
 * <p>This class converts between:
 *
 * <ul>
 *   <li>{@link StorageParameter} - Tileverse Storage SPI configuration parameters
 *   <li>{@link Param} - GeoTools DataStore factory parameters
 *   <li>{@link Properties} - Configuration properties for {@link StorageFactory}
 * </ul>
 *
 * <p><b>Purpose:</b> The Tileverse Storage library uses an SPI mechanism with {@link StorageProvider}s that declare
 * their configuration parameters. This class dynamically discovers all available providers and converts their
 * parameters to GeoTools DataStore parameters, enabling seamless configuration through GeoServer or other GeoTools
 * applications.
 *
 * <p><b>Supported Parameters:</b>
 *
 * <ul>
 *   <li><b>General:</b> Provider selection, caching configuration
 *   <li><b>AWS S3:</b> Region, credentials, path style, credential profiles
 *   <li><b>Azure Blob:</b> Blob name, account key, SAS token
 *   <li><b>Google Cloud Storage:</b> Project ID, quota project, application credentials
 * </ul>
 *
 * <p><b>Usage:</b>
 *
 * <pre>{@code
 * // In PMTilesDataStoreFactory
 * Map<String, Object> connectionParams = ...;
 * Properties storageConfig = RangeReaderParams.toProperties(connectionParams);
 * try (Storage storage = StorageFactory.open(parent, storageConfig);
 *         RangeReader reader = storage.openRangeReader(key)) { ... }
 * }</pre>
 *
 * @see StorageFactory
 * @see StorageProvider
 * @see PMTilesDataStoreFactory
 */
public class RangeReaderParams {

    /**
     * Param {@link DataStoreFactory} can use to force selecting a specific {@link StorageProvider} with
     * {@link RangeReaderParams#toProperties(Map) toProperties(connectionParameters)} and used to obtain the storage
     * through {@link StorageFactory#open(Properties)} or {@link StorageConfig#fromProperties(Properties)}
     */
    public static final Param RANGEREADER_PROVIDER_ID = dataStoreParam(StorageConfig.FORCE_PROVIDER_ID);

    public static final Param MEMORY_CACHE_ENABLED = dataStoreParam(AbstractStorageProvider.MEMORY_CACHE_ENABLED);

    public static final Param HTTP_CONNECTION_TIMEOUT_MILLIS =
            dataStoreParam(HttpStorageProvider.HTTP_CONNECTION_TIMEOUT_MILLIS);
    public static final Param HTTP_TRUST_ALL_SSL_CERTIFICATES =
            dataStoreParam(HttpStorageProvider.HTTP_TRUST_ALL_SSL_CERTIFICATES);
    public static final Param HTTP_AUTH_USERNAME = dataStoreParam(HttpStorageProvider.HTTP_AUTH_USERNAME);
    public static final Param HTTP_AUTH_PASSWORD = dataStoreParam(HttpStorageProvider.HTTP_AUTH_PASSWORD);
    public static final Param HTTP_AUTH_BEARER_TOKEN = dataStoreParam(HttpStorageProvider.HTTP_AUTH_BEARER_TOKEN);
    public static final Param HTTP_AUTH_API_KEY_HEADERNAME =
            dataStoreParam(HttpStorageProvider.HTTP_AUTH_API_KEY_HEADERNAME);
    public static final Param HTTP_AUTH_API_KEY = dataStoreParam(HttpStorageProvider.HTTP_AUTH_API_KEY);
    public static final Param HTTP_AUTH_API_KEY_VALUE_PREFIX =
            dataStoreParam(HttpStorageProvider.HTTP_AUTH_API_KEY_VALUE_PREFIX);

    public static final Param AZURE_BLOB_NAME = dataStoreParam(AzureBlobStorageProvider.AZURE_BLOB_NAME);
    public static final Param AZURE_ANONYMOUS = dataStoreParam(AzureBlobStorageProvider.AZURE_ANONYMOUS);
    public static final Param AZURE_ACCOUNT_KEY = dataStoreParam(AzureBlobStorageProvider.AZURE_ACCOUNT_KEY);
    public static final Param AZURE_SAS_TOKEN = dataStoreParam(AzureBlobStorageProvider.AZURE_SAS_TOKEN);
    public static final Param AZURE_CONNECTION_STRING =
            dataStoreParam(AzureBlobStorageProvider.AZURE_CONNECTION_STRING);
    public static final Param AZURE_ENDPOINT = dataStoreParam(AzureBlobStorageProvider.AZURE_ENDPOINT);
    public static final Param AZURE_MAX_RETRIES = dataStoreParam(AzureBlobStorageProvider.AZURE_MAX_RETRIES);
    public static final Param AZURE_RETRY_DELAY = dataStoreParam(AzureBlobStorageProvider.AZURE_RETRY_DELAY);
    public static final Param AZURE_MAX_RETRY_DELAY = dataStoreParam(AzureBlobStorageProvider.AZURE_MAX_RETRY_DELAY);
    public static final Param AZURE_TRY_TIMEOUT = dataStoreParam(AzureBlobStorageProvider.AZURE_TRY_TIMEOUT);

    public static final Param S3_FORCE_PATH_STYLE = dataStoreParam(S3StorageProvider.S3_FORCE_PATH_STYLE);
    public static final Param S3_REQUESTER_PAYS = dataStoreParam(S3StorageProvider.S3_REQUESTER_PAYS);
    public static final Param S3_ENDPOINT = dataStoreParam(S3StorageProvider.S3_ENDPOINT);
    public static final Param S3_AWS_REGION = dataStoreParam(S3StorageProvider.S3_REGION);
    public static final Param S3_ANONYMOUS = dataStoreParam(S3StorageProvider.S3_ANONYMOUS);
    public static final Param S3_AWS_ACCESS_KEY_ID = dataStoreParam(S3StorageProvider.S3_AWS_ACCESS_KEY_ID);
    public static final Param S3_AWS_SECRET_ACCESS_KEY = dataStoreParam(S3StorageProvider.S3_AWS_SECRET_ACCESS_KEY);
    public static final Param S3_USE_DEFAULT_CREDENTIALS_PROVIDER =
            dataStoreParam(S3StorageProvider.S3_USE_DEFAULT_CREDENTIALS_PROVIDER);
    public static final Param S3_DEFAULT_CREDENTIALS_PROFILE =
            dataStoreParam(S3StorageProvider.S3_DEFAULT_CREDENTIALS_PROFILE);

    public static final Param GCS_PROJECT_ID = dataStoreParam(GoogleCloudStorageProvider.GCS_PROJECT_ID);
    public static final Param GCS_QUOTA_PROJECT_ID = dataStoreParam(GoogleCloudStorageProvider.GCS_QUOTA_PROJECT_ID);
    public static final Param GCS_USER_PROJECT = dataStoreParam(GoogleCloudStorageProvider.GCS_USER_PROJECT);
    public static final Param GCS_USE_DEFAULT_APPLICTION_CREDENTIALS =
            dataStoreParam(GoogleCloudStorageProvider.GCS_USE_DEFAULT_APPLICTION_CREDENTIALS);
    public static final Param GCS_ENDPOINT = dataStoreParam(GoogleCloudStorageProvider.GCS_ENDPOINT);

    /**
     * Aggregated list of supported {@link StorageProvider#getParameters() storage provider parameters} converted to
     * {@link Param DataAccessFactory.Param}
     */
    public static final List<Param> PROVIDER_PARAMS = List.of(
            RANGEREADER_PROVIDER_ID,
            MEMORY_CACHE_ENABLED,
            HTTP_CONNECTION_TIMEOUT_MILLIS,
            HTTP_TRUST_ALL_SSL_CERTIFICATES,
            HTTP_AUTH_USERNAME,
            HTTP_AUTH_PASSWORD,
            HTTP_AUTH_BEARER_TOKEN,
            HTTP_AUTH_API_KEY_HEADERNAME,
            HTTP_AUTH_API_KEY,
            HTTP_AUTH_API_KEY_VALUE_PREFIX,
            AZURE_BLOB_NAME,
            AZURE_ANONYMOUS,
            AZURE_ACCOUNT_KEY,
            AZURE_SAS_TOKEN,
            AZURE_CONNECTION_STRING,
            AZURE_ENDPOINT,
            AZURE_MAX_RETRIES,
            AZURE_RETRY_DELAY,
            AZURE_MAX_RETRY_DELAY,
            AZURE_TRY_TIMEOUT,
            S3_FORCE_PATH_STYLE,
            S3_REQUESTER_PAYS,
            S3_ENDPOINT,
            S3_AWS_REGION,
            S3_ANONYMOUS,
            S3_AWS_ACCESS_KEY_ID,
            S3_AWS_SECRET_ACCESS_KEY,
            S3_USE_DEFAULT_CREDENTIALS_PROVIDER,
            S3_DEFAULT_CREDENTIALS_PROFILE,
            GCS_PROJECT_ID,
            GCS_QUOTA_PROJECT_ID,
            GCS_USER_PROJECT,
            GCS_USE_DEFAULT_APPLICTION_CREDENTIALS,
            GCS_ENDPOINT);

    private RangeReaderParams() {
        // private constructor, utility class
    }

    /**
     * Appends all storage configuration parameters after the specified datastore parameters.
     *
     * <p>This method is used by {@link PMTilesDataStoreFactory#getParametersInfo()} to dynamically include all storage
     * configuration parameters based on available providers.
     *
     * @param dataStoreParams the base datastore parameters (e.g., URI, namespace)
     * @return array combining datastore parameters followed by all storage parameters
     */
    public static Param[] appendAfter(Param... dataStoreParams) {
        return appendAfter(param -> true, dataStoreParams);
    }

    /**
     * Appends storage configuration parameters after the specified datastore parameters.
     *
     * <p>This method is used by {@link PMTilesDataStoreFactory#getParametersInfo()} to dynamically include all storage
     * configuration parameters based on available providers.
     *
     * @param filter a filter predicate to apply in case some parameters need to be excluded
     * @param dataStoreParams the base datastore parameters (e.g., URI, namespace)
     * @return array combining datastore parameters followed by all storage parameters
     */
    public static Param[] appendAfter(Predicate<Param> filter, Param... dataStoreParams) {
        List<Param> storageParams = PROVIDER_PARAMS;
        return Stream.concat(Stream.of(dataStoreParams), storageParams.stream().filter(filter))
                .toArray(Param[]::new);
    }

    /**
     * Converts a {@link StorageParameter} to a GeoTools {@link Param}.
     *
     * <p>This conversion handles:
     *
     * <ul>
     *   <li>Key mapping from storage parameter keys
     *   <li>Type conversion to GeoTools-compatible types
     *   <li>Default values and sample values
     *   <li>Parameter groups (basic vs advanced)
     *   <li>Descriptions and titles for UI display
     * </ul>
     *
     * @param param the storage parameter to convert
     * @return the equivalent GeoTools Param
     */
    public static Param dataStoreParam(StorageParameter<?> param) {
        Object defaultValue = param.defaultValue().orElse(null);
        List<?> sampleValues = param.sampleValues();
        Object[] options = sampleValues.isEmpty() ? null : sampleValues.toArray();

        return ParamBuilder.builder()
                .key(param.key())
                // meh, ParamInfo uses title for tooltips, we want the description
                // .title(param.title())
                .title(param.description())
                .description(param.description())
                .optional()
                .level(param.group())
                .type(param.type())
                .defaultValue(defaultValue)
                .options(options)
                .password(param.password())
                .build();
    }

    /**
     * Converts DataStore connection parameters to storage configuration properties.
     *
     * <p>This method extracts all storage-related parameters from the DataStore connection parameters map and converts
     * them to a {@link Properties} object suitable for passing to {@link StorageFactory#open(java.net.URI,
     * Properties)}.
     *
     * <p>The conversion includes:
     *
     * <ul>
     *   <li>Provider selection parameters
     *   <li>Caching configuration
     *   <li>Cloud provider authentication parameters (S3, Azure, GCS)
     *   <li>Any other provider-specific parameters
     * </ul>
     *
     * <p>Forward-compatible {@code storage.*} keys (canonical in tileverse 2.x) are translated to the canonical
     * {@code io.tileverse.rangereader.*} form via {@link RangeReaderConfig#normalizeKeys(Map)} before lookup, so
     * DataStoreInfo entries persisted by a future tileverse 2.x consumer (e.g. GeoServer 3.1+) are read correctly here.
     *
     * @param connectionParams the DataStore connection parameters (from
     *     {@link PMTilesDataStoreFactory#createDataStore})
     * @return properties object suitable for {@link StorageFactory#open(java.net.URI, Properties)}
     */
    public static Properties toProperties(Map<String, ?> connectionParams) {
        // Rewrite any legacy io.tileverse.rangereader.* keys into the canonical storage.* form;
        // Param.lookUp is keyed off param.key(), now storage.*, and would otherwise miss values
        // persisted in older GeoServer catalogs.
        Map<String, Object> normalized = StorageConfig.normalizeKeys(connectionParams);
        Properties configOpts = new Properties();
        addProperty(RANGEREADER_PROVIDER_ID, normalized, configOpts);
        PROVIDER_PARAMS.forEach(param -> addProperty(param, normalized, configOpts));
        return configOpts;
    }

    private static void addProperty(Param param, Map<String, ?> params, Properties configOpts) {
        Object lookUp;
        try {
            lookUp = param.lookUp(params);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (lookUp != null) {
            String val = Converters.convert(lookUp, String.class);
            configOpts.setProperty(param.key, val);
        }
    }

    /**
     * Builds a {@link StorageConfig} addressing {@code uri} as the leaf object, with backend-specific tuning taken from
     * GeoTools connection {@code params}. Pass the result to {@code PMTilesReader.open(StorageConfig)} or
     * {@code VersatilesReader.open(StorageConfig)}; the reader owns the resulting {@code Storage} and
     * {@code RangeReader} and closes both.
     */
    public static StorageConfig toStorageConfig(URI uri, Map<String, ?> params) {
        Properties merged = toProperties(params);
        merged.setProperty(StorageConfig.URI_KEY, uri.toString());
        return StorageConfig.fromProperties(merged);
    }
}
