/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.unidata;


/**
 * A bean that represents a row in the index used for mapping 2d grids to 2d slices in NetCDF files.
 * 
 * <p>
 * The elements are:
 * <ol>
 *      <li><b>imageIndex</b> the index of the image to work with</li>
 *      <li><b>tIndex</b> the index of the time dimension for this 2d slice</li>
 *      <li><b>zIndex</b> the index of the elevation dimension for this 2d slice</li>
 *      <li><b>variableName</b> the name of this variable, e.g. temperature</li>
 * </ol>
 * 
 * @author Andrea Antonello
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class UnidataSlice2DIndex {

    /** DEFAULT_INDEX */
    public static final int DEFAULT_INDEX = -1;
    
    private int tIndex = DEFAULT_INDEX;
    
    private int zIndex = DEFAULT_INDEX;
    
    private final String variableName;
    
    public UnidataSlice2DIndex(String variableName) {
        this(DEFAULT_INDEX, DEFAULT_INDEX, variableName);
    }

    public UnidataSlice2DIndex(int tIndex, int zIndex, String variableName) {
        org.geotools.util.Utilities.ensureNonNull("variableName", variableName);
        this.tIndex = tIndex;
        this.zIndex = zIndex;
        this.variableName = variableName;
    }

    public int getZIndex() {
        return zIndex;
    }

    public int getTIndex() {
        return tIndex;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String toString() {
        return "UnidataVariableIndex [tIndex=" + tIndex + ", zIndex="
                + zIndex + ", variableName=" + variableName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tIndex;
        result = prime * result + ((variableName == null) ? 0 : variableName.hashCode());
        result = prime * result + zIndex;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UnidataSlice2DIndex other = (UnidataSlice2DIndex) obj;
        if (tIndex != other.tIndex)
            return false;
        if (variableName == null) {
            if (other.variableName != null)
                return false;
        } else if (!variableName.equals(other.variableName))
            return false;
        if (zIndex != other.zIndex)
            return false;
        return true;
    }
      
}
