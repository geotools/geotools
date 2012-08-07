/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.parsers;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.namespace.QName;

import org.geotools.data.DataSourceException;
import org.geotools.data.wfs.internal.GetFeatureParser;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * A {@link GetFeatureParser} implementation that uses plain xml pull to parse a GetFeature
 * response.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * 
 * 
 * 
 * @source $URL$
 *         http://svn.geotools.org/trunk/modules/plugin/wfs/src/main/java/org/geotools/wfs/v_1_1_0
 *         /data/XmlSimpleFeatureParser.java $ //@deprecated should be removed as long as
 *         {@link StreamingParserFeatureReader} works well
 */
@SuppressWarnings("nls")
public class XmlSimpleFeatureParser extends XmlFeatureParser<SimpleFeatureType, SimpleFeature> implements GetFeatureParser {
	
	private final Map<String, AttributeDescriptor> expectedProperties;
	
	private final SimpleFeatureBuilder builder;
	
	public XmlSimpleFeatureParser(final InputStream getFeatureResponseStream,
            final SimpleFeatureType targetType, QName featureDescriptorName) throws IOException {
    	super(getFeatureResponseStream, targetType, featureDescriptorName);

        this.builder = new SimpleFeatureBuilder(this.targetType);

        // HACK! use a case insensitive set to compare the coming attribute names with the ones in
        // the schema. Rationale being that the FGDC CubeWerx server has a mismatch in the case of
        // property names between what it states in a DescribeFeatureType and in a GetFeature requests
        expectedProperties = new TreeMap<String, AttributeDescriptor>(String.CASE_INSENSITIVE_ORDER);
        for (AttributeDescriptor desc : this.targetType.getAttributeDescriptors()) {
            expectedProperties.put(desc.getLocalName(), desc);
        }
    }

    @Override
    public SimpleFeature parse() throws IOException {
        final String fid;
        try {
            fid = seekFeature();
            if (fid == null) {
                return null;
            }

            int tagType;
            String tagNs;
            String tagName;
            Object attributeValue;

            while (true) {
                tagType = parser.next();
                if (XmlPullParser.END_DOCUMENT == tagType) {
                    close();
                    return null;
                }

                tagNs = parser.getNamespace();
                tagName = parser.getName();
                if (END_TAG == tagType && featureNamespace.equals(tagNs)
                        && featureName.equals(tagName)) {
                    // found end of current feature
                    break;
                }

                if (START_TAG == tagType) {
                    AttributeDescriptor descriptor = expectedProperties.get(tagName);
                    if (descriptor != null) {
                    	attributeValue = parseAttributeValue(descriptor);
                        builder.set(descriptor.getLocalName(), attributeValue);
                    }
                }
            }
        } catch (XmlPullParserException e) {
            throw new DataSourceException(e);
        }

        return builder.buildFeature(fid);
    }
}














