/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource Factory</b> associated with the package.
 * <!-- end-user-doc -->
 * @see org.geotools.data.efeature.tests.util.EFeatureTestsResourceImpl
 * @generated
 */
public class EFeatureTestsResourceFactoryImpl extends ResourceFactoryImpl {
    
    private boolean binary;
    
    /**
     * Creates an instance of the resource factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EFeatureTestsResourceFactoryImpl() {
        super();
    }
    
    public EFeatureTestsResourceFactoryImpl(boolean binary) {
        this.binary = binary;
    }

    /**
     * Creates an instance of the resource. <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Resource createResourceGen(URI uri) {
        Resource result = new EFeatureTestsResourceImpl(uri);
        return result;
    }

    /**
     * Creates an instance of the resource.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Resource createResource(URI uri) {
        return createResource(uri,binary);
    }
    
    public Resource createResource(URI uri, boolean binary) {
        
        return binary ? new BinaryResourceImpl(uri) : createResourceGen(uri);
        
    }

} //EFeatureTestsResourceFactoryImpl
