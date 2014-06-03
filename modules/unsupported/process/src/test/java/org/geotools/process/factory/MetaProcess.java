package org.geotools.process.factory;

import org.geotools.data.Parameter;

/**
 * Fake process using some metadata in the annotations
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
@DescribeProcess(title = "Meta", description = "Process used to test the metadata annotations")
public class MetaProcess {

    @DescribeResult(name = "value", description = "the value provided as input", meta = { "mimeTypes=application/shapefile,application/json" })
    public byte[] execute(
            @DescribeParameter(name = "extension", meta = { Parameter.EXT + "=shp" }) String ext,
            @DescribeParameter(name = "password", meta = { Parameter.IS_PASSWORD + "=true" }) String pwd) {
        return null;
    }

}
