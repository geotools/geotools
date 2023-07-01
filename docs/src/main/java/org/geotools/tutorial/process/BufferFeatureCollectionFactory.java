/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tutorial.process;

import java.util.Map;
import org.geotools.api.util.InternationalString;
import org.geotools.data.Parameter;
import org.geotools.process.feature.FeatureToFeatureProcessFactory;
import org.geotools.text.Text;

/**
 * Factory for process which buffers an entire feature collection.
 *
 * @author Justin Deoliveira, OpenGEO
 * @since 2.6
 */
public class BufferFeatureCollectionFactory extends FeatureToFeatureProcessFactory {

    /** Buffer amount */
    public static final Parameter<Double> BUFFER =
            new Parameter<>(
                    "buffer",
                    Double.class,
                    Text.text("Buffer Amount"),
                    Text.text("Amount to buffer each feature by"));

    public InternationalString getTitle() {
        return Text.text("Buffer Features");
    }

    public InternationalString getDescription() {
        return Text.text("Buffer each Feature in a Feature Collection");
    }

    @Override
    protected void addParameters(Map<String, Parameter<?>> parameters) {
        parameters.put(BUFFER.key, BUFFER);
    }

    public BufferFeatureCollectionProcess create() throws IllegalArgumentException {
        return new BufferFeatureCollectionProcess(this);
    }
}
