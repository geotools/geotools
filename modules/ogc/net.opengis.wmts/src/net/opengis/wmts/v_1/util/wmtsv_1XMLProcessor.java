/**
 */
package net.opengis.wmts.v_1.util;

import java.util.Map;

import net.opengis.wmts.v_1.wmtsv_1Package;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class wmtsv_1XMLProcessor extends XMLProcessor {

    /**
     * Public constructor to instantiate the helper.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public wmtsv_1XMLProcessor() {
        super(EPackage.Registry.INSTANCE);
        wmtsv_1Package.eINSTANCE.eClass();
    }
    
    /**
     * Register for "*" and "xml" file extensions the wmtsv_1ResourceFactoryImpl factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected Map<String, Resource.Factory> getRegistrations() {
        if (registrations == null) {
            super.getRegistrations();
            registrations.put(XML_EXTENSION, new wmtsv_1ResourceFactoryImpl());
            registrations.put(STAR_EXTENSION, new wmtsv_1ResourceFactoryImpl());
        }
        return registrations;
    }

} //wmtsv_1XMLProcessor
