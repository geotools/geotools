/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature.internal;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.impl.EFeatureContextImpl;
import org.geotools.data.efeature.impl.EFeatureIDFactoryImpl;

/**
 * This class implements a void {@link EFeature#getID() ID} factory.
 * <p>
 * It is only used by {@link EFeatureContextHelper} to tell the {@link EFeatureContextImpl} 
 * to not automatically create IDs after {@link EObject}s are added to 
 * registered {@link EFeatureContext#eAdd(String, org.eclipse.emf.edit.domain.EditingDomain) domains}.
 * <p>  
 * Any attempt to create or use IDs will throw and {@link UnsupportedOperationException}.
 * </p>
 * 
 * @see {@link #createID(EObject)} - will throw an {@link UnsupportedOperationException}
 * @see {@link #createID(URI, EObject, EClass, EAttribute)} - will throw an {@link UnsupportedOperationException}
 * @see {@link #useID(EObject, String)} - will throw an {@link UnsupportedOperationException}
 * @see {@link #useID(URI, EObject, EAttribute, String)} - will throw an {@link UnsupportedOperationException}
 * 
 * @author kengu - 4. juni 2011
 * 
 */
public class EFeatureVoidIDFactory extends EFeatureIDFactoryImpl {
    
     @Override
    public String createID(EObject eObject) throws UnsupportedOperationException,
            IllegalStateException {
        throw new UnsupportedOperationException("ID creation not supported");
    }

    @Override
    public String useID(EObject eObject, String eID) throws UnsupportedOperationException,
            IllegalStateException {
        throw new UnsupportedOperationException("ID creation not supported");
    }

    @Override
    public String peekID(URI eURI, EClass eClass) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("ID creation not supported");
    }

    @Override
    public String peekID(URI eURI, EAttribute eID) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("ID creation not supported");
    }
    
}