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
package org.geotools.validation.dto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * PlugInConfig purpose.
 * 
 * <p>
 * Description of PlugInConfig ...
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class PlugInDTO {
    /** the plug-in name */
    private String name;

    /** the plug-in description */
    private String description;

    /** the class name this plug-in represents */
    private String className;

    /** the default arguments */
    private Map args;

    /**
     * PlugInDTO constructor.
     * 
     * <p>
     * Does nothing.
     * </p>
     */
    public PlugInDTO() {
    }

    /**
     * PlugInDTO constructor.
     * 
     * <p>
     * Creates a copy of the DTO passed in in this object.
     * </p>
     *
     * @param pi
     */
    public PlugInDTO(PlugInDTO pi) {
        name = pi.getName();
        description = pi.getDescription();
        className = pi.getClassName();
        args = new HashMap();

        if (pi.getArgs() != null) {
            Iterator i = pi.getArgs().keySet().iterator();

            while (i.hasNext()) {
                String key = (String) i.next();

                //TODO clone value.
                args.put(key,
                    new ArgumentDTO((ArgumentDTO) pi.getArgs().get(key)));
            }
        }
    }

    /**
     * Implementation of clone.
     *
     * @return a copy of this class.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new PlugInDTO(this);
    }

    /**
     * Implementation of equals.
     *
     * @param obj
     *
     * @return true when the two objects are equal.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof PlugInDTO)) {
            return false;
        }

        PlugInDTO pi = (PlugInDTO) obj;
        boolean r = true;

        if (name != null) {
            r = r && (name.equals(pi.getName()));
        }

        if (description != null) {
            r = r && (description.equals(pi.getDescription()));
        }

        if (className != null) {
            r = r && (className.equals(pi.getClassName()));
        }

        if (args == null) {
            if (pi.getArgs() != null) {
                return false;
            }
        } else {
            if (pi.getArgs() != null) {
                r = r && args.equals(pi.getArgs());
            } else {
                return false;
            }
        }

        return r;
    }

    /**
     * Implementation of hashCode.
     *
     * @return the hashcode.
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int i = 1;

        if (name != null) {
            i *= name.hashCode();
        }

        if (description != null) {
            i *= description.hashCode();
        }

        if (className != null) {
            i *= className.hashCode();
        }

        if (args != null) {
            i *= args.hashCode();
        }

        return i;
    }

    /**
     * Access args property.
     *
     * @return Returns the args.
     */
    public Map getArgs() {
        return args;
    }

    /**
     * Set args to args.
     *
     * @param args The args to set.
     */
    public void setArgs(Map args) {
        this.args = args;
    }

    /**
     * Access className property.
     *
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Set className to className.
     *
     * @param className The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Access description property.
     *
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description to description.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Access name property.
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name to name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
