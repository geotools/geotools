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
package org.geotools.process.impl;

import java.awt.RenderingHints.Key;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.geotools.data.Parameter;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.util.SimpleInternationalString;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * Helper class for a process factory that will return just a single process
 * 
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public abstract class SingleProcessFactory implements ProcessFactory {

    Name processName;

    /**
     * Utility method for factories that will use the process factory name in order to define the
     * process name by stripping the "Factory" at the end of the name.
     */
    protected SingleProcessFactory() {
        String factoryName = this.getClass().getSimpleName();
        String localName;
        if (factoryName.endsWith("Factory") && !factoryName.equals("Factory")) {
            localName = factoryName.substring(0, factoryName.length() - 7);
        } else {
            localName = factoryName;
        }
        String GT_NAMESPACE = "gt";
        processName = new NameImpl(GT_NAMESPACE, localName);
    }

    /**
     * 
     * @param processName
     */
    protected SingleProcessFactory(Name processName) {
        if (processName == null)
            throw new NullPointerException("Process name cannot be null");
        this.processName = processName;
    }

    /**
     * Checks the process name and makes sure it's consistent with the only process name this
     * factory knows about
     * 
     * @param name
     */
    void checkName(Name name) {
        if(name == null)
            throw new NullPointerException("Process name cannot be null");
        if(!processName.equals(name))
            throw new IllegalArgumentException("Unknown process '" + name 
                    + "', this factory knows only about '" + processName +  "'");
    }

    public Process create(Name name) {
        checkName(name);
        return create();
    }
    
    public Set<Name> getNames() {
        return Collections.singleton(processName);
    }

    public InternationalString getDescription(Name name) {
        checkName(name);
        return getDescription();
    }
    

    public Map<String, Parameter<?>> getParameterInfo(Name name) {
        checkName(name);
        return getParameterInfo();
    }

    public Map<String, Parameter<?>> getResultInfo(Name name, Map<String, Object> parameters)
            throws IllegalArgumentException {
        checkName(name);
        return getResultInfo(parameters);
    }

    public InternationalString getTitle(Name name) {
        checkName(name);
        return getTitle();
    }

    public String getVersion(Name name) {
        checkName(name);
        return getVersion();
    }

    public boolean supportsProgress(Name name) {
        checkName(name);
        return supportsProgress();
    }
    
    /** 
     * Default Implementation return true
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * The default implementation returns an empty map.
     */
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    protected abstract Process create();

    protected abstract InternationalString getDescription();

    protected abstract Map<String, Parameter<?>> getParameterInfo();

    protected abstract Map<String, Parameter<?>> getResultInfo(Map<String, Object> parameters)
            throws IllegalArgumentException;

    /**
     * Name suitable for display to end user.
     *
     * @return A short name suitable for display in a user interface.
     */
    public InternationalString getTitle(){
        return new SimpleInternationalString(processName.getLocalPart());
    }

    protected abstract String getVersion();

    protected abstract boolean supportsProgress();

}
