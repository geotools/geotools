/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.CoverageDescriptionType#getCoverageOffering <em>Coverage Offering</em>}</li>
 *   <li>{@link net.opengis.wcs10.CoverageDescriptionType#getUpdateSequence <em>Update Sequence</em>}</li>
 *   <li>{@link net.opengis.wcs10.CoverageDescriptionType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getCoverageDescriptionType()
 * @model extendedMetaData="name='CoverageDescription_._type' kind='elementOnly'"
 * @generated
 */
public interface CoverageDescriptionType extends EObject {
    /**
	 * Returns the value of the '<em><b>Coverage Offering</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.CoverageOfferingType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Coverage Offering</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Coverage Offering</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageDescriptionType_CoverageOffering()
	 * @model type="net.opengis.wcs10.CoverageOfferingType" containment="true" required="true"
	 *        extendedMetaData="kind='element' name='CoverageOffering' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getCoverageOffering();

    /**
	 * Returns the value of the '<em><b>Update Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Service metadata (Capabilities) document version, having values that are "increased" whenever any change is made in service metadata document. Values are selected by each server, and are always opaque to clients.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Sequence</em>' attribute.
	 * @see #setUpdateSequence(String)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageDescriptionType_UpdateSequence()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='updateSequence'"
	 * @generated
	 */
    String getUpdateSequence();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageDescriptionType#getUpdateSequence <em>Update Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update Sequence</em>' attribute.
	 * @see #getUpdateSequence()
	 * @generated
	 */
    void setUpdateSequence(String value);

    /**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"1.0.0"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #setVersion(String)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageDescriptionType_Version()
	 * @model default="1.0.0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='version'"
	 * @generated
	 */
    String getVersion();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageDescriptionType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #isSetVersion()
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @generated
	 */
    void setVersion(String value);

    /**
	 * Unsets the value of the '{@link net.opengis.wcs10.CoverageDescriptionType#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    void unsetVersion();

    /**
	 * Returns whether the value of the '{@link net.opengis.wcs10.CoverageDescriptionType#getVersion <em>Version</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Version</em>' attribute is set.
	 * @see #unsetVersion()
	 * @see #getVersion()
	 * @see #setVersion(String)
	 * @generated
	 */
    boolean isSetVersion();

} // CoverageDescriptionType
