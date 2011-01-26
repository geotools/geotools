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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.impl.SingleProcessFactory;
import org.geotools.text.Text;
import org.opengis.util.InternationalString;

public class DoubleAdditionFactory extends SingleProcessFactory
{
	static final Parameter<Double> INPUT_A = new Parameter<Double>("input_a", Double.class, Text.text("First value"),  Text.text("First value to add"));
	static final Parameter<Double> INPUT_B = new Parameter<Double>("input_b", Double.class, Text.text("Second value"), Text.text("Second value to add"));
	static final Parameter<Double> RESULT  = new Parameter<Double>("result",  Double.class, Text.text("Result value"), Text.text("Result of addition"));

	static final Map<String,Parameter<?>> prameterInfo = new TreeMap<String,Parameter<?>>();
	static
	{
        prameterInfo.put(INPUT_A.key, INPUT_A);
        prameterInfo.put(INPUT_B.key, INPUT_B);
    }

	static final Map<String,Parameter<?>> resultInfo = new TreeMap<String,Parameter<?>>();
	static
	{
        resultInfo.put(RESULT.key, RESULT);
    }
	
	public DoubleAdditionFactory() {
	    super(new NameImpl("gt", "DoubleAddition"));
	}

	public Process create(Map<String, Object> parameters) throws IllegalArgumentException
	{
		return new DoubleAdditionProcess(this);
	}

	public InternationalString getDescription()
	{
		return Text.text("Adds two double precision floating point numbers and returns the sum as a double");
	}

	public Map<String, Parameter<?>> getParameterInfo()
	{
		return Collections.unmodifiableMap(prameterInfo);
	}

	public Map<String, Parameter<?>> getResultInfo(Map<String, Object> parameters) throws IllegalArgumentException
	{
		return Collections.unmodifiableMap(resultInfo);
	}

	public InternationalString getTitle()
	{
	    return Text.text("DoubleAddition");
	}

	public Process create() throws IllegalArgumentException
	{
	    return new DoubleAdditionProcess(this);
	}

	public boolean supportsProgress()
	{
		return true;
	}

	public String getVersion()
	{
		return "1.0.0";
	}
}