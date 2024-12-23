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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.geotools.api.data.Parameter;
import org.geotools.api.util.InternationalString;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.impl.SingleProcessFactory;
import org.geotools.text.Text;
import org.locationtech.jts.geom.Geometry;

/**
 * A Buffer process used on a geometry object.
 *
 * <p>This process is based on the SFSQL specification and implemented by the JTS Topology Suite
 *
 * @author gdavis
 */
public class BufferFactory extends SingleProcessFactory {
    // making parameters available as static constants to help java programmers
    /** Geometry for operation */
    static final Parameter<Geometry> GEOM1 =
            new Parameter<>("geom1", Geometry.class, Text.text("Geometry"), Text.text("Geometry to buffer"));

    /** Buffer amount */
    static final Parameter<Double> BUFFER = new Parameter<>(
            "buffer", Double.class, Text.text("Buffer Amount"), Text.text("Amount to buffer the geometry by"));

    /** Map used for getParameterInfo; used to describe operation requirements for user interface creation. */
    static final Map<String, Parameter<?>> prameterInfo = new TreeMap<>();

    static {
        prameterInfo.put(GEOM1.key, GEOM1);
        prameterInfo.put(BUFFER.key, BUFFER);
    }

    static final Parameter<Geometry> RESULT = new Parameter<>(
            "result", Geometry.class, Text.text("Result"), Text.text("Result of Geometry.getBuffer( Buffer )"));

    /** Map used to describe operation results. */
    static final Map<String, Parameter<?>> resultInfo = new TreeMap<>();

    static {
        resultInfo.put(RESULT.key, RESULT);
    }

    public BufferFactory() {
        super(new NameImpl("gt", "buffer"));
    }

    public Process create(Map<String, Object> parameters) throws IllegalArgumentException {
        return new BufferProcess(this);
    }

    public InternationalString getDescription() {
        return Text.text("Buffer a geometry");
    }

    public Map<String, Parameter<?>> getParameterInfo() {
        return Collections.unmodifiableMap(prameterInfo);
    }

    public Map<String, Parameter<?>> getResultInfo(Map<String, Object> parameters) throws IllegalArgumentException {
        return Collections.unmodifiableMap(resultInfo);
    }

    public InternationalString getTitle() {
        // please note that this is a title for display purposes only
        // finding an specific implementation by name is not possible
        return Text.text("Buffer");
    }

    public Process create() throws IllegalArgumentException {
        return new BufferProcess(this);
    }

    public boolean supportsProgress() {
        return true;
    }

    public String getVersion() {
        return "1.0.0";
    }
}
