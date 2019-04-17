package org.geotools.s3;

/** Default {@link S3ConnectorFactory}. */
public class S3ConnectorFactoryImpl implements S3ConnectorFactory {
    @Override
    public S3Connector createConnector(String input) {
        return new S3Connector(input);
    }
}
