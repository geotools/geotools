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
package org.geotools.process.feature;

import java.util.Map;
import org.geotools.api.data.Parameter;
import org.geotools.api.util.InternationalString;
import org.geotools.text.Text;

/**
 * Factory for process which buffers an entire feature collection.
 *
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 */
public class BufferFeatureCollectionFactory extends FeatureToFeatureProcessFactory {

    /** Buffer amount */
    public static final Parameter<Double> BUFFER = new Parameter<>(
            "buffer", Double.class, Text.text("Buffer Amount"), Text.text("Amount to buffer each feature by"));

    @Override
    public InternationalString getTitle() {
        return Text.text("Buffer Features");
    }

    @Override
    public InternationalString getDescription() {
        return Text.text("Buffer each Feature in a Feature Collection");
    }

    @Override
    protected void addParameters(Map<String, Parameter<?>> parameters) {
        parameters.put(BUFFER.key, BUFFER);
    }

    @Override
    public BufferFeatureCollectionProcess create() throws IllegalArgumentException {
        return new BufferFeatureCollectionProcess(this);
    }
}
