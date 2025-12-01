/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geoparquet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Tests for AWS credential chain support in GeoParquet datastore.
 *
 * <p>This test class covers both configuration parsing and SQL generation for AWS S3 authentication.
 */
public class GeoParquetS3CredentialChainTest {

    @Test
    public void testConfigWithCredentialChainEnabled() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "s3://test-bucket/data.parquet");
        params.put("use_aws_credential_chain", true);

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertTrue("Credential chain should be enabled", config.isUseAwsCredentialChain());
    }

    @Test
    public void testConfigWithCredentialChainDisabled() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "s3://test-bucket/data.parquet");
        params.put("use_aws_credential_chain", false);

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertFalse("Credential chain should be disabled", config.isUseAwsCredentialChain());
    }

    @Test
    public void testConfigDefaultsToFalse() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "s3://test-bucket/data.parquet");
        // Don't set use_aws_credential_chain parameter

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertFalse("Credential chain should default to false", config.isUseAwsCredentialChain());
    }

    @Test
    public void testConfigWithLocalFile() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "file:///tmp/data.parquet");
        params.put("use_aws_credential_chain", true);

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        // Credential chain can be enabled even for local files (harmless)
        assertTrue("Credential chain setting should be preserved", config.isUseAwsCredentialChain());
    }

    @Test
    public void testConfigWithHttpUri() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "https://example.com/data.parquet");
        params.put("use_aws_credential_chain", false);

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertFalse(config.isUseAwsCredentialChain());
    }

    @Test
    public void testConfigWithAwsRegion() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "s3://test-bucket/data.parquet");
        params.put("use_aws_credential_chain", true);
        params.put("aws_region", "eu-west-1");

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertTrue("Credential chain should be enabled", config.isUseAwsCredentialChain());
        assertEquals("Region should be set", "eu-west-1", config.getAwsRegion());
    }

    @Test
    public void testConfigWithAwsProfile() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "s3://test-bucket/data.parquet");
        params.put("use_aws_credential_chain", true);
        params.put("aws_profile", "my_profile");

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertTrue("Credential chain should be enabled", config.isUseAwsCredentialChain());
        assertEquals("Profile should be set", "my_profile", config.getAwsProfile());
    }

    @Test
    public void testConfigWithRegionAndProfile() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "s3://test-bucket/data.parquet");
        params.put("use_aws_credential_chain", true);
        params.put("aws_region", "us-east-1");
        params.put("aws_profile", "prod_profile");

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertTrue("Credential chain should be enabled", config.isUseAwsCredentialChain());
        assertEquals("Region should be set", "us-east-1", config.getAwsRegion());
        assertEquals("Profile should be set", "prod_profile", config.getAwsProfile());
    }

    @Test
    public void testConfigWithNullRegionAndProfile() throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("uri", "s3://test-bucket/data.parquet");
        params.put("use_aws_credential_chain", true);
        // Don't set aws_region or aws_profile

        GeoParquetConfig config = GeoParquetConfig.valueOf(params);
        assertNotNull(config);
        assertTrue("Credential chain should be enabled", config.isUseAwsCredentialChain());
        assertNull("Region should be null", config.getAwsRegion());
        assertNull("Profile should be null", config.getAwsProfile());
    }

    // Tests for buildCreateSecretSql method

    @Test
    public void testBuildCreateSecretSqlBasic() {
        String sql = GeoParquetViewManager.buildCreateSecretSql(null, null);

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain
                )
                """;

        assertEquals("Basic credential chain SQL should match expected", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlWithRegion() {
        String sql = GeoParquetViewManager.buildCreateSecretSql("us-east-1", null);

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain, REGION 'us-east-1'
                )
                """;

        assertEquals("SQL with region should match expected", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlWithProfile() {
        String sql = GeoParquetViewManager.buildCreateSecretSql(null, "production");

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain, CHAIN 'config', PROFILE 'production'
                )
                """;

        assertEquals("SQL with profile should match expected", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlWithRegionAndProfile() {
        String sql = GeoParquetViewManager.buildCreateSecretSql("eu-west-1", "staging");

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain, CHAIN 'config', PROFILE 'staging', REGION 'eu-west-1'
                )
                """;

        assertEquals("SQL with region and profile should match expected", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlWithEmptyStrings() {
        String sql = GeoParquetViewManager.buildCreateSecretSql("", "");

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain
                )
                """;

        assertEquals("SQL with empty strings should match basic SQL", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlEscapesSingleQuotesInRegion() {
        String sql = GeoParquetViewManager.buildCreateSecretSql("us'east'1", null);

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain, REGION 'us''east''1'
                )
                """;

        assertEquals("SQL should properly escape single quotes in region", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlEscapesSingleQuotesInProfile() {
        String sql = GeoParquetViewManager.buildCreateSecretSql(null, "prod'uction");

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain, CHAIN 'config', PROFILE 'prod''uction'
                )
                """;

        assertEquals("SQL should properly escape single quotes in profile", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlEscapesSingleQuotesInBothParameters() {
        String sql = GeoParquetViewManager.buildCreateSecretSql("ap'south'1", "dev'profile");

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain, CHAIN 'config', PROFILE 'dev''profile', REGION 'ap''south''1'
                )
                """;

        assertEquals("SQL should properly escape single quotes in both parameters", expected, sql);
    }

    @Test
    public void testBuildCreateSecretSqlWithWhitespaceOnlyStrings() {
        String sql = GeoParquetViewManager.buildCreateSecretSql("   ", "  \t  ");

        String expected =
                """
                CREATE OR REPLACE SECRET geoparquet_s3_secret (
                    TYPE s3,
                    PROVIDER credential_chain
                )
                """;

        assertEquals("SQL with whitespace-only strings should match basic SQL", expected, sql);
    }
}
