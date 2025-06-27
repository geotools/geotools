/**
 */
package org.w3._2001.smil20.util;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

import org.w3._2001.smil20.Smil20Package;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class Smil20XMLProcessor extends XMLProcessor {

    /**
     * Public constructor to instantiate the helper.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Smil20XMLProcessor() {
        super(EPackage.Registry.INSTANCE);
        Smil20Package.eINSTANCE.eClass();
    }
    
    /**
     * Register for "*" and "xml" file extensions the Smil20ResourceFactoryImpl factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected Map<String, Resource.Factory> getRegistrations() {
        if (registrations == null) {
            super.getRegistrations();
            registrations.put(XML_EXTENSION, new Smil20ResourceFactoryImpl());
            registrations.put(STAR_EXTENSION, new Smil20ResourceFactoryImpl());
        }
        return registrations;
    }

} //Smil20XMLProcessor
