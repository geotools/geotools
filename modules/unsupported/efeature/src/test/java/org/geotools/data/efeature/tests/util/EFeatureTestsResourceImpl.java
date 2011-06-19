/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.util;

import org.eclipse.emf.common.util.URI;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * @see org.geotools.data.efeature.tests.util.EFeatureTestsResourceFactoryImpl
 * @generated
 */
public class EFeatureTestsResourceImpl extends XMIResourceImpl {
    /**
     * Creates an instance of the resource.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param uri the URI of the new resource.
     * @generated
     */
    public EFeatureTestsResourceImpl(URI uri) {
        super(uri);
    }

    @Override
    protected XMLHelper createXMLHelper() {
        return super.createXMLHelper();//return new EFeatureXMLHelper();
    }
    
    

} //EFeatureTestsResourceImpl
