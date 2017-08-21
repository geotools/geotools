/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>File Value Model Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * List of codes that identifies the file structure model for records stored in files.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getFileValueModelType()
 * @model extendedMetaData="name='FileValueModelType'"
 * @generated
 */
public enum FileValueModelType implements Enumerator {
    /**
     * The '<em><b>Record Interleaved</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RECORD_INTERLEAVED_VALUE
     * @generated
     * @ordered
     */
    RECORD_INTERLEAVED(0, "RecordInterleaved", "Record Interleaved");

    /**
     * The '<em><b>Record Interleaved</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Record Interleaved</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #RECORD_INTERLEAVED
     * @model name="RecordInterleaved" literal="Record Interleaved"
     * @generated
     * @ordered
     */
    public static final int RECORD_INTERLEAVED_VALUE = 0;

    /**
     * An array of all the '<em><b>File Value Model Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final FileValueModelType[] VALUES_ARRAY =
        new FileValueModelType[] {
            RECORD_INTERLEAVED,
        };

    /**
     * A public read-only list of all the '<em><b>File Value Model Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<FileValueModelType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>File Value Model Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FileValueModelType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FileValueModelType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>File Value Model Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FileValueModelType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FileValueModelType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>File Value Model Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FileValueModelType get(int value) {
        switch (value) {
            case RECORD_INTERLEAVED_VALUE: return RECORD_INTERLEAVED;
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
    private FileValueModelType(int value, String name, String literal) {
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
    
} //FileValueModelType
