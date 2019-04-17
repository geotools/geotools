package org.geotools.s3;

/** Factory for creating S3 connectors based on string inputs. */
public interface S3ConnectorFactory {
    /**
     * Create a new connector for the given input.
     *
     * @param input The input for the connector.
     * @return The {@link S3Connector}.
     */
    S3Connector createConnector(String input);
}
