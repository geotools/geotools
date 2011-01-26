/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Valid domain (or allowed set of values) of one quantity, with its name or identifier. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.DomainType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getDomainType()
 * @model extendedMetaData="name='DomainType' kind='elementOnly'"
 * @generated
 */
public interface DomainType extends UnNamedDomainType {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name or identifier of this quantity. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see net.opengis.ows11.Ows11Package#getDomainType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DomainType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // DomainType
