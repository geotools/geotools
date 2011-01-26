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

package org.geotools.process.literal;

import java.util.HashMap;
import java.util.Map;

import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.AbstractProcess;
import org.geotools.text.Text;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Geometry;

/**
 * XXX Untested Buffer a Feature and return the result
 *
 * @author Lucas Reed, Refractions Research Inc
 *
 * @source $URL$
 */
public class FeatureBufferProcess extends AbstractProcess
{
	private boolean started = false;

	public FeatureBufferProcess(FeatureBufferFactory factory)
	{
		super(factory);
	}

	public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor)
	{
		Map<String,Object> result   = new HashMap<String, Object>();

		if (this.started)
		{
			throw new IllegalStateException("Process can only be run once");
		}

		this.started = true;

		if (null == monitor)
		{
			monitor = new NullProgressListener();
		}

		try
		{
			monitor.started();
            monitor.setTask(Text.text("Fetching arguments"));
            monitor.progress(10.0f);

            Feature input_a = (Feature)input.get(FeatureBufferFactory.INPUT_A.key);          
            Double  input_b = (Double)input.get( FeatureBufferFactory.INPUT_B.key);

            monitor.setTask(Text.text("Buffering Feature"));
            monitor.progress(25.0f);

            // Buffer
            for(Property prop : input_a.getProperties())
            {
            	if (monitor.isCanceled())
                {
                    return null; // user has cancelled this operation
                }

            	if (false == Geometry.class.equals(prop.getType().getBinding()))
            	{
            		continue;
            	}

            	Geometry buffered = ((Geometry)prop.getValue()).buffer(input_b);

            	prop.setValue(buffered);
            }

            monitor.setTask(Text.text("Encoding result"));
            monitor.progress(90.0f);

            result.put(FeatureBufferFactory.RESULT.key, input_a);
            monitor.complete(); // same as 100.0f
		} catch(Exception e) {
			monitor.exceptionOccurred(e);
            return null;
		} finally {
            monitor.dispose();
        }

		return result;
	}

	public ProcessFactory getFactory()
	{
        return this.factory;
    }
}
