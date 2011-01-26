package org.geotools.gtxml;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.geotools.xml.Configuration;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Operates as a front end to GTXML parser/encoder services.
 * <p>
 * This is a simple utility class; if you need more control please look at the implementation of
 * the provided methods.
 */
public class GTXML {
    /**
     * Parse a fature type; using the provided configuration.
     * <p>
     * Usually the configuration is based on org.geotools.wfs.v1_0.WFSConfiguration or
     * org.geotools.wfs.v1_1.WFSConfiguration; you need to indicate which name you want parsed
     * out as a FetureType.
     * 
     * @param configuration wfs configuration to use
     * @param name name to parse out as a feature type
     * @param schema xsd schema to parse
     * @param crs Optional coordinate reference system for generated feature type
     * @return FeatureType
     * @throws IOException
     */
    public static FeatureType parseFeatureType( Configuration configuration, QName name, CoordinateReferenceSystem crs  ) throws IOException {
        return EmfAppSchemaParser.parse(configuration, name, crs);
    }
    
}
