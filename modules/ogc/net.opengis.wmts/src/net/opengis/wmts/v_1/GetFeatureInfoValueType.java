/**
 */
package net.opengis.wmts.v_1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Get Feature Info Value Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getGetFeatureInfoValueType()
 * @model extendedMetaData="name='GetFeatureInfoValueType'"
 * @generated
 */
public enum GetFeatureInfoValueType implements Enumerator {
    /**
     * The '<em><b>Get Feature Info</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GET_FEATURE_INFO_VALUE
     * @generated
     * @ordered
     */
    GET_FEATURE_INFO(0, "GetFeatureInfo", "GetFeatureInfo");

    /**
     * The '<em><b>Get Feature Info</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Get Feature Info</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #GET_FEATURE_INFO
     * @model name="GetFeatureInfo"
     * @generated
     * @ordered
     */
    public static final int GET_FEATURE_INFO_VALUE = 0;

    /**
     * An array of all the '<em><b>Get Feature Info Value Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final GetFeatureInfoValueType[] VALUES_ARRAY =
        new GetFeatureInfoValueType[] {
            GET_FEATURE_INFO,
        };

    /**
     * A public read-only list of all the '<em><b>Get Feature Info Value Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<GetFeatureInfoValueType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Get Feature Info Value Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static GetFeatureInfoValueType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            GetFeatureInfoValueType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Get Feature Info Value Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static GetFeatureInfoValueType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            GetFeatureInfoValueType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Get Feature Info Value Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static GetFeatureInfoValueType get(int value) {
        switch (value) {
            case GET_FEATURE_INFO_VALUE: return GET_FEATURE_INFO;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private GetFeatureInfoValueType(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //GetFeatureInfoValueType
