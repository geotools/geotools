/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;


/**
 * Represents the read parameters in the geotools space, as opposed to
 * {@linkplain GrassBinaryImageReadParam} that are for the imageio space.
 * <p>
 * Represents the parameters needed by the {@linkplain GrassCoverageReader} for reading coverage,
 * i.e. the portion and resolution of the map you want to get from the GridCoverageReader.
 * </p>
 * <p>
 * The needed parameters to read a GRASS raster map, are the following:
 * <ul>
 * <li>the northern boundary coordinate</li>
 * <li>the southern boundary coordinate</li>
 * <li>the eastern boundary coordinate</li>
 * <li>the western boundary coordinate</li>
 * <li>the north-south or west-east resolution</li>
 * <li>the number of rows and columns</li>
 * </ul>
 * </p>
 * <p>
 * All these values are already handled in the {@linkplain JGrassRegion}, so that has to be supplied
 * in order to choose a region different from the native data region.
 * </p>
 * <b>Note:</b> it is enough to have bounds and row-cols, or bounds and resolutions, or also a
 * corner and row-cols and resolutions.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see {@link JGrassRegion}
 * @see {@link JGrassMapEnvironment}
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/grassraster/src/main/java/org/geotools/gce/grassraster/GrassCoverageReadParam.java $
 */
public class GrassCoverageReadParam {

    /**
     * The active read region used by the {@linkplain GrassCoverageReader} for defining the
     * requested map portion.
     */
    private JGrassRegion requestedWorldRegion = null;

    /**
     * Constructs a {@link GrassCoverageReadParam}.
     * 
     * @param requestedWorldRegion the active region to which to set read region to.
     */
    public GrassCoverageReadParam( JGrassRegion requestedWorldRegion ) {
        this.requestedWorldRegion = requestedWorldRegion;
    }

    /**
     * Getter for the {@linkplain GrassCoverageReadParam#requestedWorldRegion active region}
     * 
     * @param activeRegion the active region. If this is null, the whole raster map region should be
     *        used.
     */
    public JGrassRegion getRequestedWorldRegion() {
        return requestedWorldRegion;
    }

}
