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
package org.geotools.data.wps.request;

import java.util.List;

import net.opengis.wps10.DataType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.ResponseFormType;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.ows.Request;


/**
 * Executes a process.
 *
 * @author gdavis
 *
 *
 *
 *
 * @source $URL$
 */
public interface ExecuteProcessRequest extends Request
{

    /** Represents the PROCESS parameter */
    public static final String IDENTIFIER = "IDENTIFIER"; // $NON-NLS-1$

    /**
     * Sets the name of the process to execute
     *
     * @param processname a unique process name
     */
    public void setIdentifier(String processname);

    /**
     * Sets an input for the process to execute
     *
     * @param name the input name
     * @param value the list of input objects. The list must contain either
     * all {@link DataType} object, or {@link InputReferenceType} objects
     */
    public void addInput(String name, List<EObject> value);


    public void setResponseForm(ResponseFormType responseForm);
}
