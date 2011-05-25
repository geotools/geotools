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

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.impl.SingleProcessFactory;
import org.geotools.text.Text;
import org.opengis.feature.Feature;
import org.opengis.util.InternationalString;

/**
 * XXX Untested Factory for FeatureBuffer process
 *
 * @author Lucas Reed, Refractions Research Inc
 *
 *
 * @source $URL$
 */
public class FeatureBufferFactory extends SingleProcessFactory  {
	static final Parameter<Feature> INPUT_A = new Parameter<Feature>("input_a",
			Feature.class, Text.text("Input Feature"),    Text.text("Feature to buffer"));
	static final Parameter<Double> INPUT_B = new Parameter<Double>("input_b", Double.class,
			Text.text("Buffer amount"), Text.text("Amount to buffer by"));

	static final Parameter<Feature> RESULT  = new Parameter<Feature>("result",
			Feature.class, Text.text("Buffered feature"), Text.text("Result of buffering"));

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
	
	public FeatureBufferFactory() {
        super(new NameImpl("gt", "FeatureBuffer"));
    }

	public Process create(Map<String, Object> parameters) throws IllegalArgumentException
	{
		return new FeatureBufferProcess(this);
	}
	
	public InternationalString getDescription()
	{
		return Text.text("Buffers a Feature by a constant.");
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
	    return Text.text("FeatureBuffer");
	}

	public boolean supportsProgress()
	{
		return true;
	}

	public String getVersion()
	{
		return "1.0.0";
	}

	public Process create() throws IllegalArgumentException
	{
	    return new FeatureBufferProcess(this);
	}
}
