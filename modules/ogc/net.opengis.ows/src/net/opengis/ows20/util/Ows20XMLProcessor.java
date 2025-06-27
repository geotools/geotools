/**
 */
package net.opengis.ows20.util;

import java.util.Map;

import net.opengis.ows20.Ows20Package;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class Ows20XMLProcessor extends XMLProcessor {

  /**
   * Public constructor to instantiate the helper.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Ows20XMLProcessor() {
    super(EPackage.Registry.INSTANCE);
    Ows20Package.eINSTANCE.eClass();
  }
  
  /**
   * Register for "*" and "xml" file extensions the Ows20ResourceFactoryImpl factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected Map<String, Resource.Factory> getRegistrations() {
    if (registrations == null) {
      super.getRegistrations();
      registrations.put(XML_EXTENSION, new Ows20ResourceFactoryImpl());
      registrations.put(STAR_EXTENSION, new Ows20ResourceFactoryImpl());
    }
    return registrations;
  }

} //Ows20XMLProcessor
