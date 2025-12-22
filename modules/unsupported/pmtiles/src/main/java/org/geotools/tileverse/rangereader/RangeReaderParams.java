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
import io.tileverse.rangereader.RangeReaderFactory;
import io.tileverse.rangereader.azure.AzureBlobRangeReaderProvider;
import io.tileverse.rangereader.gcs.GoogleCloudStorageRangeReaderProvider;
import io.tileverse.rangereader.http.HttpRangeReaderProvider;
import io.tileverse.rangereader.s3.S3RangeReaderProvider;
import io.tileverse.rangereader.spi.AbstractRangeReaderProvider;
import io.tileverse.rangereader.spi.RangeReaderConfig;
import io.tileverse.rangereader.spi.RangeReaderParameter;
import io.tileverse.rangereader.spi.RangeReaderProvider;
import java.io.IOException;
import java.io.UncheckedIOException;
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
 *   <li>{@link RangeReaderParameter} - Tileverse Range Reader SPI configuration parameters
 *   <li>{@link Param} - GeoTools DataStore factory parameters
 *   <li>{@link Properties} - Configuration properties for {@link RangeReaderFactory}
 * </ul>
 *
 * <p><b>Purpose:</b> The Tileverse Range Reader library uses an SPI mechanism with {@link RangeReaderProvider}s that
 * declare their configuration parameters. This class dynamically discovers all available providers and converts their
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
 * Properties rangeReaderConfig = RangeReaderParams.toProperties(connectionParams);
 * RangeReader reader = RangeReaderFactory.create(uri, rangeReaderConfig);
 * }</pre>
 *
 * @see RangeReaderFactory
 * @see RangeReaderProvider
 * @see PMTilesDataStoreFactory
 */
public class RangeReaderParams {

    /**
     * Param {@link DataStoreFactory} can use to force selecting a specific {@link RangeReaderProvider} with
     * {@link RangeReaderParams#toProperties(Map) toProperties(connectionParameters)} and used to obtain the range reder
     * through {@link RangeReaderFactory#create(Properties)} or {@link RangeReaderConfig#fromProperties(Properties)}
     */
    public static final Param RANGEREADER_PROVIDER_ID = dataStoreParam(RangeReaderConfig.FORCE_PROVIDER_ID);

    public static final Param MEMORY_CACHE_ENABLED = dataStoreParam(AbstractRangeReaderProvider.MEMORY_CACHE_ENABLED);
    public static final Param MEMORY_CACHE_BLOCK_ALIGNED =
            dataStoreParam(AbstractRangeReaderProvider.MEMORY_CACHE_BLOCK_ALIGNED);
    public static final Param MEMORY_CACHE_BLOCK_SIZE =
            dataStoreParam(AbstractRangeReaderProvider.MEMORY_CACHE_BLOCK_SIZE);

    public static final Param HTTP_CONNECTION_TIMEOUT_MILLIS =
            dataStoreParam(HttpRangeReaderProvider.HTTP_CONNECTION_TIMEOUT_MILLIS);
    public static final Param HTTP_TRUST_ALL_SSL_CERTIFICATES =
            dataStoreParam(HttpRangeReaderProvider.HTTP_TRUST_ALL_SSL_CERTIFICATES);
    public static final Param HTTP_AUTH_USERNAME = dataStoreParam(HttpRangeReaderProvider.HTTP_AUTH_USERNAME);
    public static final Param HTTP_AUTH_PASSWORD = dataStoreParam(HttpRangeReaderProvider.HTTP_AUTH_PASSWORD);
    public static final Param HTTP_AUTH_BEARER_TOKEN = dataStoreParam(HttpRangeReaderProvider.HTTP_AUTH_BEARER_TOKEN);
    public static final Param HTTP_AUTH_API_KEY_HEADERNAME =
            dataStoreParam(HttpRangeReaderProvider.HTTP_AUTH_API_KEY_HEADERNAME);
    public static final Param HTTP_AUTH_API_KEY = dataStoreParam(HttpRangeReaderProvider.HTTP_AUTH_API_KEY);
    public static final Param HTTP_AUTH_API_KEY_VALUE_PREFIX =
            dataStoreParam(HttpRangeReaderProvider.HTTP_AUTH_API_KEY_VALUE_PREFIX);

    public static final Param AZURE_BLOB_NAME = dataStoreParam(AzureBlobRangeReaderProvider.AZURE_BLOB_NAME);
    public static final Param AZURE_ACCOUNT_KEY = dataStoreParam(AzureBlobRangeReaderProvider.AZURE_ACCOUNT_KEY);
    public static final Param AZURE_SAS_TOKEN = dataStoreParam(AzureBlobRangeReaderProvider.AZURE_SAS_TOKEN);

    public static final Param S3_FORCE_PATH_STYLE = dataStoreParam(S3RangeReaderProvider.S3_FORCE_PATH_STYLE);
    public static final Param S3_AWS_REGION = dataStoreParam(S3RangeReaderProvider.S3_REGION);
    public static final Param S3_AWS_ACCESS_KEY_ID = dataStoreParam(S3RangeReaderProvider.S3_AWS_ACCESS_KEY_ID);
    public static final Param S3_AWS_SECRET_ACCESS_KEY = dataStoreParam(S3RangeReaderProvider.S3_AWS_SECRET_ACCESS_KEY);
    public static final Param S3_USE_DEFAULT_CREDENTIALS_PROVIDER =
            dataStoreParam(S3RangeReaderProvider.S3_USE_DEFAULT_CREDENTIALS_PROVIDER);
    public static final Param S3_DEFAULT_CREDENTIALS_PROFILE =
            dataStoreParam(S3RangeReaderProvider.S3_DEFAULT_CREDENTIALS_PROFILE);

    public static final Param GCS_PROJECT_ID = dataStoreParam(GoogleCloudStorageRangeReaderProvider.GCS_PROJECT_ID);
    public static final Param GCS_QUOTA_PROJECT_ID =
            dataStoreParam(GoogleCloudStorageRangeReaderProvider.GCS_QUOTA_PROJECT_ID);
    public static final Param GCS_USE_DEFAULT_APPLICTION_CREDENTIALS =
            dataStoreParam(GoogleCloudStorageRangeReaderProvider.GCS_USE_DEFAULT_APPLICTION_CREDENTIALS);

    /**
     * Aggregated list of supported {@link RangeReaderProvider#getParameters() range reader parameters} converted to
     * {@link Param DataAccessFactory.Param}
     */
    public static final List<Param> PROVIDER_PARAMS = List.of(
            RANGEREADER_PROVIDER_ID,
            MEMORY_CACHE_ENABLED,
            MEMORY_CACHE_BLOCK_ALIGNED,
            MEMORY_CACHE_BLOCK_SIZE,
            HTTP_CONNECTION_TIMEOUT_MILLIS,
            HTTP_TRUST_ALL_SSL_CERTIFICATES,
            HTTP_AUTH_USERNAME,
            HTTP_AUTH_PASSWORD,
            HTTP_AUTH_BEARER_TOKEN,
            HTTP_AUTH_API_KEY_HEADERNAME,
            HTTP_AUTH_API_KEY,
            HTTP_AUTH_API_KEY_VALUE_PREFIX,
            AZURE_BLOB_NAME,
            AZURE_ACCOUNT_KEY,
            AZURE_SAS_TOKEN,
            S3_FORCE_PATH_STYLE,
            S3_AWS_REGION,
            S3_AWS_ACCESS_KEY_ID,
            S3_AWS_SECRET_ACCESS_KEY,
            S3_USE_DEFAULT_CREDENTIALS_PROVIDER,
            S3_DEFAULT_CREDENTIALS_PROFILE,
            GCS_PROJECT_ID,
            GCS_QUOTA_PROJECT_ID,
            GCS_USE_DEFAULT_APPLICTION_CREDENTIALS);

    private RangeReaderParams() {
        // private constructor, utility class
    }

    /**
     * Appends Range Reader configuration parameters after the specified datastore parameters.
     *
     * <p>This method is used by {@link PMTilesDataStoreFactory#getParametersInfo()} to dynamically include all Range
     * Reader configuration parameters based on available providers.
     *
     * @param filter a filter predicate to apply in case some parameters need to be excluded
     * @param dataStoreParams the base datastore parameters (e.g., URI, namespace)
     * @return array combining datastore parameters followed by all Range Reader parameters
     */
    public static Param[] appendAfter(Predicate<Param> filter, Param... dataStoreParams) {
        List<Param> rangeReaderParams = PROVIDER_PARAMS;
        return Stream.concat(
                        Stream.of(dataStoreParams), rangeReaderParams.stream().filter(filter))
                .toArray(Param[]::new);
    }

    /**
     * Converts a {@link RangeReaderParameter} to a GeoTools {@link Param}.
     *
     * <p>This conversion handles:
     *
     * <ul>
     *   <li>Key mapping from Range Reader parameter keys
     *   <li>Type conversion to GeoTools-compatible types
     *   <li>Default values and sample values
     *   <li>Parameter groups (basic vs advanced)
     *   <li>Descriptions and titles for UI display
     * </ul>
     *
     * @param param the Range Reader parameter to convert
     * @return the equivalent GeoTools Param
     */
    public static Param dataStoreParam(RangeReaderParameter<?> param) {
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
     * Converts DataStore connection parameters to Range Reader configuration properties.
     *
     * <p>This method extracts all Range Reader-related parameters from the DataStore connection parameters map and
     * converts them to a {@link Properties} object suitable for passing to {@link RangeReaderFactory#create}.
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
     * @param connectionParams the DataStore connection parameters (from
     *     {@link PMTilesDataStoreFactory#createDataStore})
     * @return properties object suitable for {@link RangeReaderFactory#create}
     */
    public static Properties toProperties(Map<String, ?> connectionParams) {
        Properties configOpts = new Properties();
        addProperty(RANGEREADER_PROVIDER_ID, connectionParams, configOpts);
        PROVIDER_PARAMS.forEach(param -> addProperty(param, connectionParams, configOpts));
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
}
