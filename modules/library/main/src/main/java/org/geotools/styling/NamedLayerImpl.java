/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on November 3, 2003, 10:10 AM
 */
package org.geotools.styling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.util.Utilities;

/**
 * Default implementation of named layer.
 *
 * @author jamesm
 *
 * @source $URL$
 */
public class NamedLayerImpl extends StyledLayerImpl implements NamedLayer {
    List<Style> styles = new ArrayList<Style>();

    //FeatureTypeConstraint[] featureTypeConstraints = new FeatureTypeConstraint[0];    
    List<FeatureTypeConstraint> featureTypeConstraints = new ArrayList<FeatureTypeConstraint>();
    
    public List<FeatureTypeConstraint> layerFeatureConstraints() {
    	return featureTypeConstraints;
    }
    public FeatureTypeConstraint[] getLayerFeatureConstraints() {
        return featureTypeConstraints.toArray(new FeatureTypeConstraint[0]);
    }

    public void setLayerFeatureConstraints(FeatureTypeConstraint[] featureTypeConstraints) {
    	this.featureTypeConstraints.clear();
    	this.featureTypeConstraints.addAll(Arrays.asList(featureTypeConstraints));
    }
    
    public Style[] getStyles() {
        return styles.toArray(new Style[0]);
    }

    public List<Style> styles() {
    	return styles;
    }
    
    public void addStyle(Style sl) {
        styles.add(sl);
    }

    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

	public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }
        
        if (oth instanceof NamedLayerImpl) {
        	NamedLayerImpl other = (NamedLayerImpl) oth;

        	if (!Utilities.equals(styles, other.styles))
        		return false;
        	
        	if(!Utilities.equals(featureTypeConstraints, other.featureTypeConstraints)){
        		return false;
        	}
        	return true;
        }

        return false;
	}
	
}
