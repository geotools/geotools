/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import net.opengis.wfs.DescribeFeatureTypeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Versioned Feature Type Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Same as wfs:DescribeFeatureType, but with the option to output
 *             a versioned feature type instead of a plain one
 *          
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.DescribeVersionedFeatureTypeType#isVersioned <em>Versioned</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getDescribeVersionedFeatureTypeType()
 * @model extendedMetaData="name='DescribeVersionedFeatureTypeType' kind='elementOnly'"
 * @generated
 */
public interface DescribeVersionedFeatureTypeType extends DescribeFeatureTypeType {
    /**
     * Returns the value of the '<em><b>Versioned</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                      If false, the output is the same as wfs:DescribeFeatureType,
     *                      if true on the contrary the generated feature type will descend
     *                      form wfsv:AbstractVersionedFeatureType
     *                   
     * <!-- end-model-doc -->
     * @return the value of the '<em>Versioned</em>' attribute.
     * @see #isSetVersioned()
     * @see #unsetVersioned()
     * @see #setVersioned(boolean)
     * @see net.opengis.wfsv.WfsvPackage#getDescribeVersionedFeatureTypeType_Versioned()
     * @model default="true" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='versioned'"
     * @generated
     */
    boolean isVersioned();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DescribeVersionedFeatureTypeType#isVersioned <em>Versioned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Versioned</em>' attribute.
     * @see #isSetVersioned()
     * @see #unsetVersioned()
     * @see #isVersioned()
     * @generated
     */
    void setVersioned(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wfsv.DescribeVersionedFeatureTypeType#isVersioned <em>Versioned</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetVersioned()
     * @see #isVersioned()
     * @see #setVersioned(boolean)
     * @generated
     */
    void unsetVersioned();

    /**
     * Returns whether the value of the '{@link net.opengis.wfsv.DescribeVersionedFeatureTypeType#isVersioned <em>Versioned</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Versioned</em>' attribute is set.
     * @see #unsetVersioned()
     * @see #isVersioned()
     * @see #setVersioned(boolean)
     * @generated
     */
    boolean isSetVersioned();

} // DescribeVersionedFeatureTypeType
