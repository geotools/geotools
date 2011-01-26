/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3.xlink.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.w3.xlink.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XlinkFactoryImpl extends EFactoryImpl implements XlinkFactory {
    /**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static XlinkFactory init() {
		try {
			XlinkFactory theXlinkFactory = (XlinkFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.w3.org/1999/xlink"); 
			if (theXlinkFactory != null) {
				return theXlinkFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new XlinkFactoryImpl();
	}

    /**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public XlinkFactoryImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case XlinkPackage.DOCUMENT_ROOT: return createDocumentRoot();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case XlinkPackage.ACTUATE_TYPE:
				return createActuateTypeFromString(eDataType, initialValue);
			case XlinkPackage.SHOW_TYPE:
				return createShowTypeFromString(eDataType, initialValue);
			case XlinkPackage.ACTUATE_TYPE_OBJECT:
				return createActuateTypeObjectFromString(eDataType, initialValue);
			case XlinkPackage.SHOW_TYPE_OBJECT:
				return createShowTypeObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case XlinkPackage.ACTUATE_TYPE:
				return convertActuateTypeToString(eDataType, instanceValue);
			case XlinkPackage.SHOW_TYPE:
				return convertShowTypeToString(eDataType, instanceValue);
			case XlinkPackage.ACTUATE_TYPE_OBJECT:
				return convertActuateTypeObjectToString(eDataType, instanceValue);
			case XlinkPackage.SHOW_TYPE_OBJECT:
				return convertShowTypeObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ActuateType createActuateTypeFromString(EDataType eDataType, String initialValue) {
		ActuateType result = ActuateType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertActuateTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ShowType createShowTypeFromString(EDataType eDataType, String initialValue) {
		ShowType result = ShowType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertShowTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ActuateType createActuateTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createActuateTypeFromString(XlinkPackage.Literals.ACTUATE_TYPE, initialValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertActuateTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertActuateTypeToString(XlinkPackage.Literals.ACTUATE_TYPE, instanceValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ShowType createShowTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createShowTypeFromString(XlinkPackage.Literals.SHOW_TYPE, initialValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String convertShowTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertShowTypeToString(XlinkPackage.Literals.SHOW_TYPE, instanceValue);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public XlinkPackage getXlinkPackage() {
		return (XlinkPackage)getEPackage();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
    public static XlinkPackage getPackage() {
		return XlinkPackage.eINSTANCE;
	}

} //XlinkFactoryImpl
