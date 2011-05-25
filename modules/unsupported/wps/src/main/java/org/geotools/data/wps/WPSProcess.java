/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wps;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.opengis.wps10.DataType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.ProcessDescriptionType;

import org.eclipse.emf.common.util.EList;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.ows.ServiceException;
import org.geotools.process.ProcessFactory;
import org.geotools.process.impl.AbstractProcess;
import org.opengis.util.ProgressListener;

/**
 * This is a representation of a process built from the WPSFactory class.
 * It is not a real process, but a representation of a process that can
 * be executed on the server, as described in the process' describeprocess.
 * 
 * @author GDavis
 *
 *
 *
 * @source $URL$
 */
public class WPSProcess extends AbstractProcess {

	protected WPSProcess(ProcessFactory factory) {
		super(factory);
	}

	/**
	 * Since this is not a real process, but a representation of one,
	 * this method doesn't actually execute the process.  Instead it
	 * builds a request to send to the server to execute the 
	 * process.  The response is parsed and returned as a map just like
	 * a process would return locally.  The inputs are not validated, they are just
	 * built and sent to the server in a request.  If they result in a bad request,
	 * the request system will handle the returned error.
	 * 
	 * @param input the map of inputs to process
	 * @param monitor currently this is not used for this process reprensentation but
	 * it could be implemented in some form in the future.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> execute(Map<String, Object> input,
			ProgressListener monitor) {

		// Get the describeprocess object so we can use it to build up a request and
		// get the server url to send the request to.
		WPSFactory wpsfactory = (WPSFactory) this.factory;
		ProcessDescriptionType pdt = wpsfactory.getProcessDescriptionType();
		URL url = wpsfactory.getServerURL();
		WebProcessingService wps;
		try {
			wps = new WebProcessingService(url);
		} catch (ServiceException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		// create the execute request object
		ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
		exeRequest.setIdentifier(wpsfactory.getIdentifier());
		
		// loop through each expected input in the describeprocess, and set it
		// based on what we have in the provided input map.
		if (pdt.getDataInputs()!=null){
			EList inputs = pdt.getDataInputs().getInput();
			Iterator iterator = inputs.iterator();
			while (iterator.hasNext()) {
				InputDescriptionType idt = (InputDescriptionType) iterator.next();
				String identifier = idt.getIdentifier().getValue();
				Object inputValue = input.get(identifier);
				if (inputValue != null) {
					// if our value is some sort of collection, then created multiple
					// dataTypes for this inputdescriptiontype.
					List<DataType> list = new ArrayList<DataType>();
					if (inputValue instanceof Map) {
						for (Object inVal : ((Map)inputValue).values()) {
							DataType createdInput = WPSUtils.createInputDataType(inVal, idt);
							list.add(createdInput);
						}
					} else if (inputValue instanceof Collection) {
						for (Object inVal : (Collection)inputValue) {
							DataType createdInput = WPSUtils.createInputDataType(inVal, idt);
							list.add(createdInput);
						}
					} else {
						// our value is a single object so create a single datatype for it
						DataType createdInput = WPSUtils.createInputDataType(inputValue, idt);
						list.add(createdInput);
					}
					// add the input to the execute request
					exeRequest.addInput(identifier, list);
				}
			}
		}
		
		// send the request and get the response
		ExecuteProcessResponse response;
		try {
			response = wps.issueRequest(exeRequest);
		} catch (ServiceException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		// if there is an exception in the response, return null
		// TODO:  properly handle the exception?
		if (response.getExceptionResponse() != null || response.getExecuteResponse() == null) {
			return null;
		}
		
		// get response object and create a map of outputs from it
		ExecuteResponseType executeResponse = response.getExecuteResponse();
		
		// create the result map of outputs
		Map<String, Object> results = new TreeMap<String, Object>();
		results = WPSUtils.createResultMap(executeResponse, results);
		
		return results;
	}

}
