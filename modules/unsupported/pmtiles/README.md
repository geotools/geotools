# PMTiles DataStore

The PMTiles DataStore provides read and query access to Protomaps PMTiles format files, both local and remote, within the GeoTools framework.

[PMTiles](https://github.com/protomaps/PMTiles) is a cloud-optimized single-file archive format for pyramids of tiled data. PMTiles is designed for efficient random access to tiles, eliminating the need for a tile server and enabling direct hosting of tile data on object storage like S3 or Azure Blob Storage. The format is particularly well-suited for hosting vector tiles and other tiled geospatial data.

## Features

- **Local File Access**: Read PMTiles files from your local filesystem
- **Remote File Access**: Access PMTiles files via HTTP/HTTPS URLs
- **Cloud Storage Support**: Direct access to PMTiles files on:
    - AWS S3 and S3-compatible storage (MinIO, LocalStack)
    - Azure Blob Storage
    - Google Cloud Storage
- **HTTP Authentication**: Support for multiple authentication methods (Basic Auth, Bearer Token, API Key)
- **Cloud Storage Authentication**: Built-in authentication support for AWS S3, Azure Blob Storage, and Google Cloud Storage
- **Spatial Queries**: Full support for spatial filters (intersects, contains, within, etc.)
- **Attribute Filters**: Filter data based on attribute values
- **Vector Tiles**: Accesses Mapbox Vector Tile (MVT) data stored in PMTiles format
- **Efficient Random Access**: Leverages PMTiles' optimized structure for fast tile retrieval
- **Memory Caching**: Configurable in-memory caching for performance optimization

## How It Works

Under the hood, this DataStore uses the [Tileverse PMTiles](https://github.com/tileverse/tileverse) library and the [Tileverse Storage](https://github.com/tileverse/tileverse) library to provide flexible, high-performance access to PMTiles files. The implementation:

1. Uses the Storage library to access PMTiles files from various sources (local, HTTP, S3, Azure, GCS)
2. Reads the PMTiles header and directory structure for efficient tile lookups
3. Decodes Mapbox Vector Tiles (MVT) from the PMTiles archive
4. Translates GeoTools queries to tile requests based on spatial bounds and zoom levels
5. Provides a standard GeoTools DataStore interface for vector tile data

The Storage library provides a unified interface for reading byte ranges from different storage backends, with support for:

- **Decorators**: Composable caching and optimization layers
- **Authentication**: Flexible credential management for cloud storage and HTTP
- **Performance**: In-memory caching of byte ranges

## Usage

### Basic Example (Local File)

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "file:/path/to/data.pmtiles");

DataStore store = DataStoreFinder.getDataStore(params);
String[] typeNames = store.getTypeNames();
SimpleFeatureSource source = store.getFeatureSource(typeNames[0]);

// Read all features
SimpleFeatureCollection features = source.getFeatures();

// Or apply a spatial filter
FilterFactory ff = CommonFactoryFinder.getFilterFactory();
Filter filter = ff.intersects(
    ff.property("geometry"),
    ff.literal(geometryFactory.toGeometry(new Envelope(-122.5, -122.3, 37.7, 37.9)))
);
SimpleFeatureCollection filtered = source.getFeatures(filter);
```

### Remote PMTiles File (HTTP)

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "https://example.com/tiles/data.pmtiles");

DataStore store = DataStoreFinder.getDataStore(params);
```

### Remote PMTiles with HTTP Basic Authentication

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "https://example.com/secure/data.pmtiles");
params.put("storage.http.username", "myuser");
params.put("storage.http.password", "mypassword");

DataStore store = DataStoreFinder.getDataStore(params);
```

### Remote PMTiles with Bearer Token

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "https://example.com/secure/data.pmtiles");
params.put("storage.http.bearer-token", "your-bearer-token");

DataStore store = DataStoreFinder.getDataStore(params);
```

### AWS S3

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "s3://my-bucket/tiles/data.pmtiles");
params.put("storage.s3.region", "us-west-2");
params.put("storage.s3.aws-access-key-id", "AKIAIOSFODNN7EXAMPLE");
params.put("storage.s3.aws-secret-access-key", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");

DataStore store = DataStoreFinder.getDataStore(params);
```

### Azure Blob Storage

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "https://myaccount.blob.core.windows.net/container/tiles/data.pmtiles");
params.put("storage.azure.account-key", "BASE64_ENCODED_KEY");

DataStore store = DataStoreFinder.getDataStore(params);
```

### Google Cloud Storage

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "https://storage.googleapis.com/my-bucket/tiles/data.pmtiles");
params.put("storage.gcs.project-id", "my-project");
// Enable default credentials chain (looks for application default credentials)
params.put("storage.gcs.default-credentials-chain", true);

DataStore store = DataStoreFinder.getDataStore(params);
```

### With Caching and Optimization

```java
Map<String, Object> params = new HashMap<>();
params.put("pmtiles", "s3://my-bucket/tiles/data.pmtiles");
params.put("storage.s3.region", "us-west-2");

// Enable in-memory caching
params.put("storage.caching.enabled", true);

DataStore store = DataStoreFinder.getDataStore(params);
```

## Parameters

### Core Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| **pmtiles** | String | Yes | URI to a PMTiles file. Supports local files (file://), HTTP/HTTPS, AWS S3 (s3://), Azure Blob Storage, and Google Cloud Storage URLs |
| **namespace** | String | No | Namespace URI to use for features |
| **storage.provider** | String | No | Force a specific storage provider (file, http, s3, azure, gcs) |

### HTTP/HTTPS Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| **storage.http.timeout-millis** | Integer | HTTP connection timeout in milliseconds |
| **storage.http.trust-all-certificates** | Boolean | Trust all SSL/TLS certificates (use with caution, for development only) |
| **storage.http.username** | String | HTTP Basic Authentication username |
| **storage.http.password** | String | HTTP Basic Authentication password |
| **storage.http.bearer-token** | String | Bearer token for token-based authentication |
| **storage.http.api-key-headername** | String | Custom header name for API key authentication |
| **storage.http.api-key** | String | API key value |
| **storage.http.api-key-value-prefix** | String | Optional prefix for API key value (e.g., "Bearer " or "ApiKey ") |

### Memory Caching Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| **storage.caching.enabled** | Boolean | Enable in-memory caching (default: false) |

### AWS S3 Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| **storage.s3.region** | String | AWS region (e.g., us-west-2) |
| **storage.s3.aws-access-key-id** | String | AWS access key ID |
| **storage.s3.aws-secret-access-key** | String | AWS secret access key |
| **storage.s3.use-default-credentials-provider** | Boolean | Use AWS default credentials provider chain (default: false) |
| **storage.s3.default-credentials-profile** | String | AWS credentials profile name to use |
| **storage.s3.force-path-style** | Boolean | Enable S3 path style access for S3-compatible services like MinIO (default: false) |

### Azure Blob Storage Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| **storage.azure.blob-name** | String | Set the blob name if the endpoint points to the account URL |
| **storage.azure.account-key** | String | Azure storage account key |
| **storage.azure.sas-token** | String | Shared Access Signature (SAS) token |

### Google Cloud Storage Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| **storage.gcs.project-id** | String | Google Cloud project ID |
| **storage.gcs.quota-project-id** | String | Quota project ID for billing purposes |
| **storage.gcs.default-credentials-chain** | Boolean | Use default application credentials chain (default: false) |

## Performance Optimization

### Memory Caching Architecture

For optimal performance with cloud storage, enable in-memory caching:

```
Client Request → Memory Cache (fast, configurable size)
              → Storage Range Reader (S3/Azure/HTTP/File)
```

PMTiles is optimized for byte-range requests natively; enabling the memory cache is sufficient.

### Recommended Settings for Cloud Storage

```java
// Enable memory cache for optimal cloud storage performance
params.put("storage.caching.enabled", true);
```

## PMTiles Format

PMTiles is designed for efficient cloud-native tile serving:

- **Single File Archive**: All tiles in one file, simplifying deployment
- **Cloud-Optimized**: Optimized for HTTP range requests
- **Efficient Directory**: Hierarchical directory structure for fast tile lookups
- **Compression**: Supports gzip and brotli compression
- **Metadata**: JSON metadata describing tile content and attribution

## Implementation Notes

- The DataStore is read-only (no writing capabilities)
- All storage range reader implementations are thread-safe for concurrent server use
- Proper resource management: Use try-with-resources or call dispose() on the DataStore
- The PMTiles directory is cached in memory for fast tile lookups
- Tile data is decoded on-demand as features are requested

## Implemented Features

- Access to local and remote PMTiles files
- Support for major cloud storage providers (S3, Azure, GCS)
- Flexible HTTP authentication mechanisms
- Multi-level caching for performance
- Integration with GeoTools DataStore API
- Spatial and attribute query support

## Limitations

- Currently read-only (no writing capabilities)
- Requires PMTiles files to contain Mapbox Vector Tiles (MVT)
- This module is unsupported and still under development

## Requirements

- Java 17 or higher
- GeoTools 34 or later

## Dependencies

- GeoTools Core modules
- Tileverse PMTiles library (io.tileverse.pmtiles:tileverse-pmtiles)
- Tileverse Storage library (io.tileverse.storage:tileverse-storage-all)

## Resources

- [PMTiles Specification](https://github.com/protomaps/PMTiles/blob/main/spec/v3/spec.md)
- [Protomaps.com](https://protomaps.com/) - PMTiles hosting and tools
- [Mapbox Vector Tiles Specification](https://github.com/mapbox/vector-tile-spec)
- [Tileverse Storage](https://github.com/tileverse/tileverse-io)
