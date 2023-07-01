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

import java.util.HashMap;
import java.util.Map;
import org.geotools.data.util.NullProgressListener;
import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.AbstractProcess;
import org.geotools.text.Text;
import org.locationtech.jts.geom.Geometry;
import org.geotools.api.util.ProgressListener;

/**
 * Process for adding a buffer around a geometry
 *
 * @author gdavis
 */
class BufferProcess extends AbstractProcess {
    private boolean started = false;

    public BufferProcess(BufferFactory bufferFactory) {
        super(bufferFactory);
    }

    public ProcessFactory getFactory() {
        return factory;
    }

    public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor) {
        if (started) throw new IllegalStateException("Process can only be run once");
        started = true;

        if (monitor == null) monitor = new NullProgressListener();
        try {
            monitor.started();
            monitor.setTask(Text.text("Grabbing arguments"));
            monitor.progress(10.0f);
            Object value = input.get(BufferFactory.GEOM1.key);
            if (value == null) {
                throw new NullPointerException("geom1 parameter required");
            }
            if (!(value instanceof Geometry)) {
                throw new ClassCastException("geom1 requied Geometry, not " + value);
            }
            Geometry geom1 = (Geometry) value;

            value = input.get(BufferFactory.BUFFER.key);
            if (value == null) {
                throw new ClassCastException("geom1 requied Geometry, not " + value);
            }
            if (!(value instanceof Number)) {
                throw new ClassCastException("buffer requied number, not " + value);
            }
            Double buffer = ((Number) value).doubleValue();

            monitor.setTask(Text.text("Processing Buffer"));
            monitor.progress(25.0f);

            if (monitor.isCanceled()) {
                return null; // user has canceled this operation
            }

            Geometry resultGeom = geom1.buffer(buffer);

            monitor.setTask(Text.text("Encoding result"));
            monitor.progress(90.0f);

            Map<String, Object> result = new HashMap<>();
            result.put(BufferFactory.RESULT.key, resultGeom);
            monitor.complete(); // same as 100.0f

            return result;
        } catch (Exception eek) {
            monitor.exceptionOccurred(eek);
            return null;
        } finally {
            monitor.dispose();
        }
    }
}
