/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.oracle;

import java.util.List;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.transform.Definition;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A simple interface which provides FeatureType mapping information such as the name of the
 * original type name and the mapped one, the wrapped feature type as well as the customized version
 *
 * @author Daniele Romagnoli, GeoSolutions SAS @TODO: Move that interface on gt-transform when ready
 */
public interface FeatureTypeMapper {

    /** Get the original name */
    public Name getName();

    /** Get the remapped name */
    public String getMappedName();

    /** Get the definitions list */
    public List<Definition> getDefinitions();

    /** Get the coordinate Reference System */
    public CoordinateReferenceSystem getCoordinateReferenceSystem();

    /** Get the remapped FeatureType */
    public SimpleFeatureType getMappedFeatureType();

    /** Get the original FeatureType */
    public SimpleFeatureType getWrappedFeatureType();

    public SimpleFeatureSource getSimpleFeatureSource();

    /** define the mapping rule */
    public String remap(String name);
}
