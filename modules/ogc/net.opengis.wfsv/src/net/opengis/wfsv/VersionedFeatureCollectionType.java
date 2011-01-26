/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import net.opengis.wfs.FeatureCollectionType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Versioned Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A collection of versioned features
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.VersionedFeatureCollectionType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getVersionedFeatureCollectionType()
 * @model extendedMetaData="name='VersionedFeatureCollectionType' kind='empty'"
 * @generated
 */
public interface VersionedFeatureCollectionType extends FeatureCollectionType {
    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.wfsv.WfsvPackage#getVersionedFeatureCollectionType_Version()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.VersionedFeatureCollectionType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

} // VersionedFeatureCollectionType
