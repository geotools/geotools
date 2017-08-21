/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.DescriptionType;
import net.opengis.ows11.DomainMetadataType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dimension Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.DimensionType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DimensionType#getUOM <em>UOM</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DimensionType#getUnitSymbol <em>Unit Symbol</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DimensionType#getDefault <em>Default</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DimensionType#isCurrent <em>Current</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.DimensionType#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getDimensionType()
 * @model extendedMetaData="name='Dimension_._type' kind='elementOnly'"
 * @generated
 */
public interface DimensionType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A name of dimensional axis
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDimensionType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DimensionType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>UOM</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Units of measure of dimensional axis.
     * <!-- end-model-doc -->
     * @return the value of the '<em>UOM</em>' containment reference.
     * @see #setUOM(DomainMetadataType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDimensionType_UOM()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='UOM' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    DomainMetadataType getUOM();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DimensionType#getUOM <em>UOM</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>UOM</em>' containment reference.
     * @see #getUOM()
     * @generated
     */
    void setUOM(DomainMetadataType value);

    /**
     * Returns the value of the '<em><b>Unit Symbol</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Symbol of the units.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Unit Symbol</em>' attribute.
     * @see #setUnitSymbol(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDimensionType_UnitSymbol()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='UnitSymbol' namespace='##targetNamespace'"
     * @generated
     */
    String getUnitSymbol();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DimensionType#getUnitSymbol <em>Unit Symbol</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Unit Symbol</em>' attribute.
     * @see #getUnitSymbol()
     * @generated
     */
    void setUnitSymbol(String value);

    /**
     * Returns the value of the '<em><b>Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 									Default value that will be used if a tile request does 
     * 									not specify a value or uses the keyword 'default'.
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Default</em>' attribute.
     * @see #setDefault(String)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDimensionType_Default()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='Default' namespace='##targetNamespace'"
     * @generated
     */
    String getDefault();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DimensionType#getDefault <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' attribute.
     * @see #getDefault()
     * @generated
     */
    void setDefault(String value);

    /**
     * Returns the value of the '<em><b>Current</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 									A value of 1 (or 'true') indicates (a) that temporal data are 
     * 									normally kept current and (b) that the request value of this 
     * 									dimension accepts the keyword 'current'.
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Current</em>' attribute.
     * @see #isSetCurrent()
     * @see #unsetCurrent()
     * @see #setCurrent(boolean)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDimensionType_Current()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='element' name='Current' namespace='##targetNamespace'"
     * @generated
     */
    boolean isCurrent();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.DimensionType#isCurrent <em>Current</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Current</em>' attribute.
     * @see #isSetCurrent()
     * @see #unsetCurrent()
     * @see #isCurrent()
     * @generated
     */
    void setCurrent(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.DimensionType#isCurrent <em>Current</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetCurrent()
     * @see #isCurrent()
     * @see #setCurrent(boolean)
     * @generated
     */
    void unsetCurrent();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.DimensionType#isCurrent <em>Current</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Current</em>' attribute is set.
     * @see #unsetCurrent()
     * @see #isCurrent()
     * @see #setCurrent(boolean)
     * @generated
     */
    boolean isSetCurrent();

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Available value for this dimension.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' attribute list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getDimensionType_Value()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='Value' namespace='##targetNamespace'"
     * @generated
     */
    EList<String> getValue();

} // DimensionType
