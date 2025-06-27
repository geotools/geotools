/**
 */
package net.opengis.gml311.util;

import java.util.Map;

import net.opengis.gml311.Gml311Package;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class Gml311XMLProcessor extends XMLProcessor {

    /**
     * Public constructor to instantiate the helper.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Gml311XMLProcessor() {
        super(EPackage.Registry.INSTANCE);
        Gml311Package.eINSTANCE.eClass();
    }
    
    /**
     * Register for "*" and "xml" file extensions the Gml311ResourceFactoryImpl factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected Map<String, Resource.Factory> getRegistrations() {
        if (registrations == null) {
            super.getRegistrations();
            registrations.put(XML_EXTENSION, new Gml311ResourceFactoryImpl());
            registrations.put(STAR_EXTENSION, new Gml311ResourceFactoryImpl());
        }
        return registrations;
    }

} //Gml311XMLProcessor
