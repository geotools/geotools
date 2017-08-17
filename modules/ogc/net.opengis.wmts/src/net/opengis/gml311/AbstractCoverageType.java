/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract element which acts as the head of a substitution group for coverages. Note that a coverage is a GML feature.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractCoverageType#getDomainSetGroup <em>Domain Set Group</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoverageType#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoverageType#getRangeSet <em>Range Set</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoverageType#getDimension <em>Dimension</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractCoverageType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractCoverageType' kind='elementOnly'"
 * @generated
 */
public interface AbstractCoverageType extends AbstractFeatureType {
    /**
     * Returns the value of the '<em><b>Domain Set Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain Set Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Domain Set Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getAbstractCoverageType_DomainSetGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='group' name='domainSet:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getDomainSetGroup();

    /**
     * Returns the value of the '<em><b>Domain Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain Set</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Domain Set</em>' containment reference.
     * @see #setDomainSet(DomainSetType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoverageType_DomainSet()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='domainSet' namespace='##targetNamespace' group='domainSet:group'"
     * @generated
     */
    DomainSetType getDomainSet();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoverageType#getDomainSet <em>Domain Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Domain Set</em>' containment reference.
     * @see #getDomainSet()
     * @generated
     */
    void setDomainSet(DomainSetType value);

    /**
     * Returns the value of the '<em><b>Range Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range Set</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range Set</em>' containment reference.
     * @see #setRangeSet(RangeSetType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoverageType_RangeSet()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='rangeSet' namespace='##targetNamespace'"
     * @generated
     */
    RangeSetType getRangeSet();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoverageType#getRangeSet <em>Range Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Set</em>' containment reference.
     * @see #getRangeSet()
     * @generated
     */
    void setRangeSet(RangeSetType value);

    /**
     * Returns the value of the '<em><b>Dimension</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Dimension</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Dimension</em>' attribute.
     * @see #setDimension(BigInteger)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoverageType_Dimension()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='dimension'"
     * @generated
     */
    BigInteger getDimension();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoverageType#getDimension <em>Dimension</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Dimension</em>' attribute.
     * @see #getDimension()
     * @generated
     */
    void setDimension(BigInteger value);

} // AbstractCoverageType
