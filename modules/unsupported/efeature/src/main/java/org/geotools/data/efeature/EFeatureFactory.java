/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.geotools.data.efeature.EFeaturePackage
 * @generated
 */
public interface EFeatureFactory extends EFactory {

    /**
     * {@link EFeatureContext} id.
     */
    public static final String eCONTEXT_ID = EFeatureFactory.class.getName();
    
    /**
     * {@link EditingDomain} id.
     */
    public static final String eDOMAIN_ID = EditingDomain.class.getName();    
    
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EFeatureFactory eINSTANCE = org.geotools.data.efeature.impl.EFeatureFactoryImpl.init();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    EFeaturePackage getEFeaturePackage();

} //EFeatureFactory
