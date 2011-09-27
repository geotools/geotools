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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.geotools.data.efeature.EFeature;
import org.geotools.data.efeature.EFeatureContext;
import org.geotools.data.efeature.EFeatureContextFactory;
import org.geotools.data.efeature.EFeatureContextInfo;
import org.geotools.data.efeature.EFeatureHints;
import org.geotools.data.efeature.EFeatureInfo;
import org.geotools.data.efeature.EFeatureReader;
import org.geotools.data.efeature.EFeatureStatus;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This helper class implements a solution to <i>the context startup problem</i>.
 * <p>
 * <b>The context startup problem</b>:
 * <p>
 * <ol>
 *      <li>Since multiple {@link EFeatureContextFactory} instances are allowed, 
 *          a {@link EFeatureContext context} ID is ambiguous without knowing 
 *          which factory created the actual context.</li> 
 *      <li>Since EMF {@link EPackage}s are completely unaware of the 
 *          {@link EFeatureContext context} during the construction of 
 *          {@link EOBject}s from {@link EClass}s, the actual 
 *          {@link EFeatureInfo structure} (and context) remains unknown until 
 *          it is {@link EFeature#setStructure(EFeatureInfo) explicitly set}.</li>
 * </ol>  
 * </p>
 * <b>Solution</b>
 * <p>
 * The problem is solved by an internal 
 * {@link EFeatureContext#isPrototype() prototype context} 
 * which {@link EFeatureInternal} objects belongs to during context-unaware 
 * construction. This context {@link EFeatureContextInfo#eFeatureInfoCache() caches} 
 * all the {@link EFeatureInfo structures} generated from {@link EObject} during the 
 * context-unaware construction. Only objects that implements {@link EFeature} 
 * or contains {@link Geometry EFeature compatible data} are added to the 
 * {@link EFeatureInfoCache cache}.
 * <p> 
 * Each {@link EFeatureInternal} instance use the structure returned by the 
 * {@link EFeatureContextHelper helper}, until the actual context is known.
 * <p> 
 * When the actual structure is known, {@link EFeatureInternal#setStructure(EFeatureInfo)} 
 * is called, mapping the {@link EObject} to given structure.
 * <p> 
 * The {@link EFeatureReader} does this during {@link EObject} iteration.  
 * </p>
 * <b>NOTE</b>: This solution adds some memory overhead since one additional 
 * {@link EFeatureInfo} instance is created for each unique {@link EClass} added 
 * to a {@link EFeatureContext context}.
 * </p>
 * 
 * @author kengu - 29. mai 2011 
 *
 *
 * @source $URL$
 */
public final class EFeatureContextHelper {
    
    public static final String INTERNAL_CONTEXT_ID = EFeatureContextHelper.class.getName();
    
    private static final EFeatureContext eContext;
    private static final EFeatureContextFactory eContextFactory;
    
    // ----------------------------------------------------- 
    //  Static construction
    // -----------------------------------------------------
    
    static {
        eContextFactory = new EFeatureContextFactory();
        eContext = eContextFactory.create(INTERNAL_CONTEXT_ID, new EFeatureVoidIDFactory(), new EFeatureHints());
    }

    // ----------------------------------------------------- 
    //  EFeatureContextHelper methods
    // -----------------------------------------------------
    
    /**
     * Get internal {@link EFeatureContext} instance.
     */
    public static final EFeatureContext eContext() {
        return eContext;
    }
    
    /**
     * Get internal context {@link EFeatureContext} instance.
     * <p>
     * If not already added, this method adds the {@link EPackage}, 
     * which given object belongs, to the context. 
     * @param eObject - given {@link EObject} instance
     * @return the {@link EFeatureContext} instance cached by this helper
     * @throws IllegalArgumentException If new package fails to validate.
     */
    public static final EFeatureContext eContext(EObject eObject) {
        //
        // Get package
        //
        EPackage ePackage = eObject.eClass().getEPackage();
        //
        // Add package to internal context
        //
        eAdd(ePackage);
        //
        // Finished
        //
        return eContext;
    }
    
    /**
     * Get the {@link EFeatureInfo#isPrototype() prototype} 
     * {@link EFeatureInfo} instance cached for the {@link EClass} 
     * defining given {@link EObject}.
     * </p> 
     * @param eObject - the {@link EObject} instance
     * @return a {@link EFeatureInfo} if found, <code>null</code> otherwise.
     */ 
    public static final EFeatureInfo ePrototype(EObject eObject) {
        return eContext.eStructure().eGetFeatureInfo(eObject);
    }
    
    /**
     * Get the {@link EFeatureInfo#isPrototype() prototype} 
     * {@link EFeatureInfo} instance cached for given {@link EClass}.
     * </p> 
     * @param eClass - the {@link EClass} instance
     * @return a {@link EFeatureInfo} if found, <code>null</code> otherwise.
     */ 
    public static final EFeatureInfo ePrototype(EClass eClass) {
        return eContext.eStructure().eGetFeatureInfo(eClass);
    }    
    
    // ----------------------------------------------------- 
    //  Helper methods
    // -----------------------------------------------------
    
    /**
     * Add {@link EPackage} instance to context.
     * <p>
     * @param ePackage - the {@link EPackage} instance
     * @return <code>null</code> if not already registered, or the 
     * {@link EPackage} replaced by given instance.
     * @throws IllegalStateException If no 
     * {@link EPackage#getNsURI() EPackage name space URI} is specified
     * @throws IllegalArgumentException If new package fails to validate.
     */
    private static final EPackage eAdd(EPackage ePackage) throws IllegalArgumentException {
        EPackage eOldPackage = eContext.eAdd(ePackage);
        if(eOldPackage!=ePackage) {
            EFeatureStatus eStatus = eContext().eStructure().validate();
            if(eStatus.isFailure()) {
                throw new IllegalArgumentException("EPackage not properly added. " 
                        + eStatus.getMessage(),eStatus.getCause());
            }        
        }
        return eOldPackage;
    }
      
}
