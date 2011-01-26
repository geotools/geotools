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
 * TestConfig purpose.
 * 
 * <p>
 * Description of TestConfig ...
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class TestDTO {
    /** the test name */
    private String name;

    /** the test description */
    private String description;

    /**
     * The plug-in which contains the class definition and default runtime
     * values
     */
    private PlugInDTO plugIn;

    /**
     * The set of runtime args for this particular test to override the
     * defaults in the plug-in
     */
    private Map args;

    /**
     * TestConfig constructor.
     * 
     * <p>
     * Does nothing
     * </p>
     */
    public TestDTO() {
    }

    /**
     * TestConfig constructor.
     * 
     * <p>
     * Creates a copy from the TestConfig specified.
     * </p>
     *
     * @param t the data to copy
     */
    public TestDTO(TestDTO t) {
        name = t.getName();
        description = t.getDescription();
        plugIn = new PlugInDTO(t.getPlugIn());
        args = new HashMap();

        if (t.getArgs() != null) {
            Iterator i = t.getArgs().keySet().iterator();

            while (i.hasNext()) {
                String key = (String) i.next();

                //TODO clone value.
                args.put(key,
                    new ArgumentDTO((ArgumentDTO) t.getArgs().get(key)));
            }
        }
    }

    /**
     * Implementation of clone.
     *
     * @return A copy of this TestConfig
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new TestDTO(this);
    }

    /**
     * Implementation of equals.
     *
     * @param obj
     *
     * @return true when they have the same data.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TestDTO)) {
            return false;
        }

        TestDTO t = (TestDTO) obj;
        boolean r = true;

        if (name != null) {
            r = r && (name.equals(t.getName()));
        }

        if (description != null) {
            r = r && (description.equals(t.getDescription()));
        }

        if (plugIn == null) {
            if (t.getPlugIn() != null) {
                return false;
            }
        } else {
            if (t.getPlugIn() != null) {
                r = r && plugIn.equals(t.getPlugIn());
            } else {
                return false;
            }
        }

        if (args == null) {
            if (t.getArgs() != null) {
                return false;
            }
        } else {
            if (t.getArgs() != null) {
                r = r && args.equals(t.getArgs());
            } else {
                return false;
            }
        }

        return r;
    }

    /**
     * Implementation of hashCode.
     *
     * @return int hashcode
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int r = 1;

        if (name != null) {
            r *= name.hashCode();
        }

        if (description != null) {
            r *= description.hashCode();
        }

        if (plugIn != null) {
            r *= plugIn.hashCode();
        }

        if (args != null) {
            r *= args.hashCode();
        }

        return r;
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

    /**
     * Access plugIn property.
     *
     * @return Returns the plugIn.
     */
    public PlugInDTO getPlugIn() {
        return plugIn;
    }

    /**
     * Set plugIn to plugIn.
     *
     * @param plugIn The plugIn to set.
     */
    public void setPlugIn(PlugInDTO plugIn) {
        this.plugIn = plugIn;
    }
}
