/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Section</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wcs20.Wcs20Package#getContainmentType()
 * @model extendedMetaData="name='ContainmentType'"
 */
public enum Section implements Enumerator {
    /**
     * The '<em><b>CoverageDescriptions</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CoverageDescriptions_VALUE
     * @ordered
     */
    COVERAGEDESCRIPTIONS(0, "CoverageDescriptions", "CoverageDescriptions"),

    /**
     * The '<em><b>CONTAINS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DatasetSeriesDescriptions_VALUE
     * @ordered
     */
    DATASETSERIESDESCRIPTIONS(1, "DatasetSeriesDescriptions", "DatasetSeriesDescriptions"),
    
    /**
     * The '<em><b>ALL</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ALL_VALUE
     * @ordered
     */
    ALL(2, "All", "All");

    /**
     * The '<em><b>OVERLAPS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>ALL</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OVERLAPS
     * @model
     * @ordered
     */
    public static final int COVERAGEDESCRIPTIONS_VALUE = 0;

    /**
     * The '<em><b>CONTAINS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>CONTAINS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CONTAINS
     * @model
     * @ordered
     */
    public static final int DATASETSERIESDESCRIPTIONS_VALUE = 1;

    /**
     * The '<em><b>CONTAINS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>CONTAINS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CONTAINS
     * @model
     * @ordered
     */
    public static final int ALL_VALUE = 2;

    
    /**
	 * An array of all the '<em><b>Section</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static final Section[] VALUES_ARRAY =
        new Section[] {
			COVERAGEDESCRIPTIONS,
			DATASETSERIESDESCRIPTIONS,
			ALL,
		};

    /**
	 * A public read-only list of all the '<em><b>Section</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final List<Section> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
	 * Returns the '<em><b>Section</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static Section get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Section result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Section</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static Section getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			Section result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Section</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static Section get(int value) {
		switch (value) {
			case COVERAGEDESCRIPTIONS_VALUE: return COVERAGEDESCRIPTIONS;
			case DATASETSERIESDESCRIPTIONS_VALUE: return DATASETSERIESDESCRIPTIONS;
			case ALL_VALUE: return ALL;
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
    private Section(int value, String name, String literal) {
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
    
} //AllSomeType
