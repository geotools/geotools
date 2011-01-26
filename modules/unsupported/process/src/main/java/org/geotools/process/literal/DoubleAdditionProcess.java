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

/**
 * 	Adds two double precision floating point numbers and returns the sum as a double.
 *
 *	@author lreed@refractions.net
 */

package org.geotools.process.literal;

import java.util.HashMap;
import java.util.Map;

import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.AbstractProcess;
import org.geotools.text.Text;
import org.geotools.util.NullProgressListener;
import org.opengis.util.ProgressListener;

class DoubleAdditionProcess extends AbstractProcess
{
	private boolean started = false;

	public DoubleAdditionProcess(DoubleAdditionFactory factory)
	{
        super(factory);
    }

	public ProcessFactory getFactory()
	{
        return this.factory;
    }

	public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor)
	{
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

            Double input_a = (Double)input.get(DoubleAdditionFactory.INPUT_A.key);          
            Double input_b = (Double)input.get(DoubleAdditionFactory.INPUT_B.key);

            monitor.setTask(Text.text("Adding values"));
            monitor.progress(25.0f);

            if (monitor.isCanceled())
            {
                return null; // user has cancelled this operation
            }

            Double sum = input_a + input_b;

            monitor.setTask(Text.text("Encoding result"));
            monitor.progress(90.0f);

            Map<String,Object> result = new HashMap<String, Object>();
            result.put(DoubleAdditionFactory.RESULT.key, sum);
            monitor.complete(); // same as 100.0f

            return result;
		} catch(Exception e) {
			monitor.exceptionOccurred(e);
            return null;
		} finally {
            monitor.dispose();
        }
	}
}