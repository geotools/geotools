/**
 */
package org.w3._2001.smil20.language.util;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

import org.w3._2001.smil20.language.LanguagePackage;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class LanguageXMLProcessor extends XMLProcessor {

    /**
     * Public constructor to instantiate the helper.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LanguageXMLProcessor() {
        super(EPackage.Registry.INSTANCE);
        LanguagePackage.eINSTANCE.eClass();
    }
    
    /**
     * Register for "*" and "xml" file extensions the LanguageResourceFactoryImpl factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected Map<String, Resource.Factory> getRegistrations() {
        if (registrations == null) {
            super.getRegistrations();
            registrations.put(XML_EXTENSION, new LanguageResourceFactoryImpl());
            registrations.put(STAR_EXTENSION, new LanguageResourceFactoryImpl());
        }
        return registrations;
    }

} //LanguageXMLProcessor
