/**
 */
package org.w3._2001.smil20.language;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.w3._2001.smil20.Smil20Package;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 *    <div xmlns="http://www.w3.org/1999/xhtml">
 *     <h1>About the XML namespace</h1>
 * 
 *     <div class="bodytext">
 *      <p>
 *       This schema document describes the XML namespace, in a form
 *       suitable for import by other schema documents.
 *      </p>
 *      <p>
 *       See <a href="http://www.w3.org/XML/1998/namespace.html">
 *       http://www.w3.org/XML/1998/namespace.html</a> and
 *       <a href="http://www.w3.org/TR/REC-xml">
 *       http://www.w3.org/TR/REC-xml</a> for information 
 *       about this namespace.
 *      </p>
 *      <p>
 *       Note that local names in this namespace are intended to be
 *       defined only by the World Wide Web Consortium or its subgroups.
 *       The names currently defined in this namespace are listed below.
 *       They should not be used with conflicting semantics by any Working
 *       Group, specification, or document instance.
 *      </p>
 *      <p>   
 *       See further below in this document for more information about <a href="#usage">how to refer to this schema document from your own
 *       XSD schema documents</a> and about <a href="#nsversioning">the
 *       namespace-versioning policy governing this schema document</a>.
 *      </p>
 *     </div>
 *    </div>
 *   
 * 
 *    <div xmlns="http://www.w3.org/1999/xhtml">
 *    
 *     <h3>Father (in any context at all)</h3> 
 * 
 *     <div class="bodytext">
 *      <p>
 *       denotes Jon Bosak, the chair of 
 *       the original XML Working Group.  This name is reserved by 
 *       the following decision of the W3C XML Plenary and 
 *       XML Coordination groups:
 *      </p>
 *      <blockquote>
 *        <p>
 * 	In appreciation for his vision, leadership and
 * 	dedication the W3C XML Plenary on this 10th day of
 * 	February, 2000, reserves for Jon Bosak in perpetuity
 * 	the XML name "xml:Father".
 *        </p>
 *      </blockquote>
 *     </div>
 *    </div>
 *   
 * 
 *    <div id="usage" xml:id="usage" xmlns="http://www.w3.org/1999/xhtml">
 *     <h2>
 *       <a name="usage">About this schema document</a>
 *     </h2>
 * 
 *     <div class="bodytext">
 *      <p>
 *       This schema defines attributes and an attribute group suitable
 *       for use by schemas wishing to allow <code>xml:base</code>,
 *       <code>xml:lang</code>, <code>xml:space</code> or
 *       <code>xml:id</code> attributes on elements they define.
 *      </p>
 *      <p>
 *       To enable this, such a schema must import this schema for
 *       the XML namespace, e.g. as follows:
 *      </p>
 *      <pre>
 *           &lt;schema . . .&gt;
 *            . . .
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2001/xml.xsd"/&gt;
 *      </pre>
 *      <p>
 *       or
 *      </p>
 *      <pre>
 *            &lt;import namespace="http://www.w3.org/XML/1998/namespace"
 *                       schemaLocation="http://www.w3.org/2009/01/xml.xsd"/&gt;
 *      </pre>
 *      <p>
 *       Subsequently, qualified reference to any of the attributes or the
 *       group defined below will have the desired effect, e.g.
 *      </p>
 *      <pre>
 *           &lt;type . . .&gt;
 *            . . .
 *            &lt;attributeGroup ref="xml:specialAttrs"/&gt;
 *      </pre>
 *      <p>
 *       will define a type which will schema-validate an instance element
 *       with any of those attributes.
 *      </p>
 *     </div>
 *    </div>
 *   
 * 
 *    <div id="nsversioning" xml:id="nsversioning" xmlns="http://www.w3.org/1999/xhtml">
 *     <h2>
 *       <a name="nsversioning">Versioning policy for this schema document</a>
 *     </h2>
 *     <div class="bodytext">
 *      <p>
 *       In keeping with the XML Schema WG's standard versioning
 *       policy, this schema document will persist at
 *       <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd</a>.
 *      </p>
 *      <p>
 *       At the date of issue it can also be found at
 *       <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd</a>.
 *      </p>
 *      <p>
 *       The schema document at that URI may however change in the future,
 *       in order to remain compatible with the latest version of XML
 *       Schema itself, or with the XML namespace itself.  In other words,
 *       if the XML Schema or XML namespaces change, the version of this
 *       document at <a href="http://www.w3.org/2001/xml.xsd">
 *        http://www.w3.org/2001/xml.xsd 
 *       </a> 
 *       will change accordingly; the version at 
 *       <a href="http://www.w3.org/2009/01/xml.xsd">
 *        http://www.w3.org/2009/01/xml.xsd 
 *       </a> 
 *       will not change.
 *      </p>
 *      <p>
 *       Previous dated (and unchanging) versions of this schema 
 *       document are at:
 *      </p>
 *      <ul>
 *       <li>
 *           <a href="http://www.w3.org/2009/01/xml.xsd">
 * 	http://www.w3.org/2009/01/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2007/08/xml.xsd">
 * 	http://www.w3.org/2007/08/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2004/10/xml.xsd">
 * 	http://www.w3.org/2004/10/xml.xsd</a>
 *         </li>
 *       <li>
 *           <a href="http://www.w3.org/2001/03/xml.xsd">
 * 	http://www.w3.org/2001/03/xml.xsd</a>
 *         </li>
 *      </ul>
 *     </div>
 *    </div>
 *   
 * <!-- end-model-doc -->
 * @see org.w3._2001.smil20.language.LanguageFactory
 * @model kind="package"
 * @generated
 */
public interface LanguagePackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "language";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://www.w3.org/2001/SMIL20/Language";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "language";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    LanguagePackage eINSTANCE = org.w3._2001.smil20.language.impl.LanguagePackageImpl.init();

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.language.impl.AnimateColorTypeImpl <em>Animate Color Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.language.impl.AnimateColorTypeImpl
     * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getAnimateColorType()
     * @generated
     */
    int ANIMATE_COLOR_TYPE = 0;

    /**
     * The feature id for the '<em><b>Accumulate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ACCUMULATE = Smil20Package.ANIMATE_COLOR_PROTOTYPE__ACCUMULATE;

    /**
     * The feature id for the '<em><b>Additive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ADDITIVE = Smil20Package.ANIMATE_COLOR_PROTOTYPE__ADDITIVE;

    /**
     * The feature id for the '<em><b>Attribute Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ATTRIBUTE_NAME = Smil20Package.ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_NAME;

    /**
     * The feature id for the '<em><b>Attribute Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ATTRIBUTE_TYPE = Smil20Package.ANIMATE_COLOR_PROTOTYPE__ATTRIBUTE_TYPE;

    /**
     * The feature id for the '<em><b>By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__BY = Smil20Package.ANIMATE_COLOR_PROTOTYPE__BY;

    /**
     * The feature id for the '<em><b>From</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__FROM = Smil20Package.ANIMATE_COLOR_PROTOTYPE__FROM;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__TO = Smil20Package.ANIMATE_COLOR_PROTOTYPE__TO;

    /**
     * The feature id for the '<em><b>Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__VALUES = Smil20Package.ANIMATE_COLOR_PROTOTYPE__VALUES;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__GROUP = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ANY = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Alt</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ALT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Begin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__BEGIN = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Calc Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__CALC_MODE = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Class</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__CLASS = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__DUR = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>End</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__END = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Fill</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__FILL = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Fill Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__FILL_DEFAULT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ID = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__LANG = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Longdesc</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__LONGDESC = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 12;

    /**
     * The feature id for the '<em><b>Max</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__MAX = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 13;

    /**
     * The feature id for the '<em><b>Min</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__MIN = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 14;

    /**
     * The feature id for the '<em><b>Repeat</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__REPEAT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 15;

    /**
     * The feature id for the '<em><b>Repeat Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__REPEAT_COUNT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 16;

    /**
     * The feature id for the '<em><b>Repeat Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__REPEAT_DUR = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 17;

    /**
     * The feature id for the '<em><b>Restart</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__RESTART = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 18;

    /**
     * The feature id for the '<em><b>Restart Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__RESTART_DEFAULT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 19;

    /**
     * The feature id for the '<em><b>Skip Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__SKIP_CONTENT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 20;

    /**
     * The feature id for the '<em><b>Sync Behavior</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__SYNC_BEHAVIOR = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 21;

    /**
     * The feature id for the '<em><b>Sync Behavior Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__SYNC_BEHAVIOR_DEFAULT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 22;

    /**
     * The feature id for the '<em><b>Sync Tolerance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__SYNC_TOLERANCE = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 23;

    /**
     * The feature id for the '<em><b>Sync Tolerance Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__SYNC_TOLERANCE_DEFAULT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 24;

    /**
     * The feature id for the '<em><b>Target Element</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__TARGET_ELEMENT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 25;

    /**
     * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE__ANY_ATTRIBUTE = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 26;

    /**
     * The number of structural features of the '<em>Animate Color Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE_FEATURE_COUNT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_FEATURE_COUNT + 27;

    /**
     * The number of operations of the '<em>Animate Color Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_COLOR_TYPE_OPERATION_COUNT = Smil20Package.ANIMATE_COLOR_PROTOTYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.language.impl.AnimateMotionTypeImpl <em>Animate Motion Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.language.impl.AnimateMotionTypeImpl
     * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getAnimateMotionType()
     * @generated
     */
    int ANIMATE_MOTION_TYPE = 1;

    /**
     * The feature id for the '<em><b>Accumulate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__ACCUMULATE = Smil20Package.ANIMATE_MOTION_PROTOTYPE__ACCUMULATE;

    /**
     * The feature id for the '<em><b>Additive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__ADDITIVE = Smil20Package.ANIMATE_MOTION_PROTOTYPE__ADDITIVE;

    /**
     * The feature id for the '<em><b>By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__BY = Smil20Package.ANIMATE_MOTION_PROTOTYPE__BY;

    /**
     * The feature id for the '<em><b>From</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__FROM = Smil20Package.ANIMATE_MOTION_PROTOTYPE__FROM;

    /**
     * The feature id for the '<em><b>Origin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__ORIGIN = Smil20Package.ANIMATE_MOTION_PROTOTYPE__ORIGIN;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__TO = Smil20Package.ANIMATE_MOTION_PROTOTYPE__TO;

    /**
     * The feature id for the '<em><b>Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__VALUES = Smil20Package.ANIMATE_MOTION_PROTOTYPE__VALUES;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__GROUP = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__ANY = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Alt</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__ALT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Begin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__BEGIN = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Calc Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__CALC_MODE = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Class</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__CLASS = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__DUR = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>End</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__END = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Fill</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__FILL = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Fill Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__FILL_DEFAULT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__ID = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__LANG = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Longdesc</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__LONGDESC = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 12;

    /**
     * The feature id for the '<em><b>Max</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__MAX = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 13;

    /**
     * The feature id for the '<em><b>Min</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__MIN = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 14;

    /**
     * The feature id for the '<em><b>Repeat</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__REPEAT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 15;

    /**
     * The feature id for the '<em><b>Repeat Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__REPEAT_COUNT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 16;

    /**
     * The feature id for the '<em><b>Repeat Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__REPEAT_DUR = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 17;

    /**
     * The feature id for the '<em><b>Restart</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__RESTART = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 18;

    /**
     * The feature id for the '<em><b>Restart Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__RESTART_DEFAULT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 19;

    /**
     * The feature id for the '<em><b>Skip Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__SKIP_CONTENT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 20;

    /**
     * The feature id for the '<em><b>Sync Behavior</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__SYNC_BEHAVIOR = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 21;

    /**
     * The feature id for the '<em><b>Sync Behavior Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__SYNC_BEHAVIOR_DEFAULT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 22;

    /**
     * The feature id for the '<em><b>Sync Tolerance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__SYNC_TOLERANCE = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 23;

    /**
     * The feature id for the '<em><b>Sync Tolerance Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__SYNC_TOLERANCE_DEFAULT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 24;

    /**
     * The feature id for the '<em><b>Target Element</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__TARGET_ELEMENT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 25;

    /**
     * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE__ANY_ATTRIBUTE = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 26;

    /**
     * The number of structural features of the '<em>Animate Motion Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE_FEATURE_COUNT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_FEATURE_COUNT + 27;

    /**
     * The number of operations of the '<em>Animate Motion Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_MOTION_TYPE_OPERATION_COUNT = Smil20Package.ANIMATE_MOTION_PROTOTYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.language.impl.AnimateTypeImpl <em>Animate Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.language.impl.AnimateTypeImpl
     * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getAnimateType()
     * @generated
     */
    int ANIMATE_TYPE = 2;

    /**
     * The feature id for the '<em><b>Accumulate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ACCUMULATE = Smil20Package.ANIMATE_PROTOTYPE__ACCUMULATE;

    /**
     * The feature id for the '<em><b>Additive</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ADDITIVE = Smil20Package.ANIMATE_PROTOTYPE__ADDITIVE;

    /**
     * The feature id for the '<em><b>Attribute Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ATTRIBUTE_NAME = Smil20Package.ANIMATE_PROTOTYPE__ATTRIBUTE_NAME;

    /**
     * The feature id for the '<em><b>Attribute Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ATTRIBUTE_TYPE = Smil20Package.ANIMATE_PROTOTYPE__ATTRIBUTE_TYPE;

    /**
     * The feature id for the '<em><b>By</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__BY = Smil20Package.ANIMATE_PROTOTYPE__BY;

    /**
     * The feature id for the '<em><b>From</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__FROM = Smil20Package.ANIMATE_PROTOTYPE__FROM;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__TO = Smil20Package.ANIMATE_PROTOTYPE__TO;

    /**
     * The feature id for the '<em><b>Values</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__VALUES = Smil20Package.ANIMATE_PROTOTYPE__VALUES;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__GROUP = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ANY = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Alt</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ALT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Begin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__BEGIN = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Calc Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__CALC_MODE = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Class</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__CLASS = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__DUR = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>End</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__END = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Fill</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__FILL = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Fill Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__FILL_DEFAULT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ID = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__LANG = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Longdesc</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__LONGDESC = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 12;

    /**
     * The feature id for the '<em><b>Max</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__MAX = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 13;

    /**
     * The feature id for the '<em><b>Min</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__MIN = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 14;

    /**
     * The feature id for the '<em><b>Repeat</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__REPEAT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 15;

    /**
     * The feature id for the '<em><b>Repeat Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__REPEAT_COUNT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 16;

    /**
     * The feature id for the '<em><b>Repeat Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__REPEAT_DUR = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 17;

    /**
     * The feature id for the '<em><b>Restart</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__RESTART = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 18;

    /**
     * The feature id for the '<em><b>Restart Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__RESTART_DEFAULT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 19;

    /**
     * The feature id for the '<em><b>Skip Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__SKIP_CONTENT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 20;

    /**
     * The feature id for the '<em><b>Sync Behavior</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__SYNC_BEHAVIOR = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 21;

    /**
     * The feature id for the '<em><b>Sync Behavior Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__SYNC_BEHAVIOR_DEFAULT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 22;

    /**
     * The feature id for the '<em><b>Sync Tolerance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__SYNC_TOLERANCE = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 23;

    /**
     * The feature id for the '<em><b>Sync Tolerance Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__SYNC_TOLERANCE_DEFAULT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 24;

    /**
     * The feature id for the '<em><b>Target Element</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__TARGET_ELEMENT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 25;

    /**
     * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE__ANY_ATTRIBUTE = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 26;

    /**
     * The number of structural features of the '<em>Animate Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE_FEATURE_COUNT = Smil20Package.ANIMATE_PROTOTYPE_FEATURE_COUNT + 27;

    /**
     * The number of operations of the '<em>Animate Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int ANIMATE_TYPE_OPERATION_COUNT = Smil20Package.ANIMATE_PROTOTYPE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.language.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.language.impl.DocumentRootImpl
     * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 3;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MIXED = 0;

    /**
     * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

    /**
     * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

    /**
     * The feature id for the '<em><b>Animate</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANIMATE = 3;

    /**
     * The feature id for the '<em><b>Animate Color</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANIMATE_COLOR = 4;

    /**
     * The feature id for the '<em><b>Animate Motion</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ANIMATE_MOTION = 5;

    /**
     * The feature id for the '<em><b>Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SET = 6;

    /**
     * The number of structural features of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 7;

    /**
     * The number of operations of the '<em>Document Root</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.w3._2001.smil20.language.impl.SetTypeImpl <em>Set Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.w3._2001.smil20.language.impl.SetTypeImpl
     * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getSetType()
     * @generated
     */
    int SET_TYPE = 4;

    /**
     * The feature id for the '<em><b>Attribute Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ATTRIBUTE_NAME = Smil20Package.SET_PROTOTYPE__ATTRIBUTE_NAME;

    /**
     * The feature id for the '<em><b>Attribute Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ATTRIBUTE_TYPE = Smil20Package.SET_PROTOTYPE__ATTRIBUTE_TYPE;

    /**
     * The feature id for the '<em><b>To</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__TO = Smil20Package.SET_PROTOTYPE__TO;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__GROUP = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Any</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ANY = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Alt</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ALT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Begin</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__BEGIN = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Class</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__CLASS = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__DUR = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 5;

    /**
     * The feature id for the '<em><b>End</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__END = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 6;

    /**
     * The feature id for the '<em><b>Fill</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__FILL = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 7;

    /**
     * The feature id for the '<em><b>Fill Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__FILL_DEFAULT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 8;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ID = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 9;

    /**
     * The feature id for the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__LANG = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 10;

    /**
     * The feature id for the '<em><b>Longdesc</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__LONGDESC = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 11;

    /**
     * The feature id for the '<em><b>Max</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__MAX = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 12;

    /**
     * The feature id for the '<em><b>Min</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__MIN = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 13;

    /**
     * The feature id for the '<em><b>Repeat</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__REPEAT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 14;

    /**
     * The feature id for the '<em><b>Repeat Count</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__REPEAT_COUNT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 15;

    /**
     * The feature id for the '<em><b>Repeat Dur</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__REPEAT_DUR = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 16;

    /**
     * The feature id for the '<em><b>Restart</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__RESTART = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 17;

    /**
     * The feature id for the '<em><b>Restart Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__RESTART_DEFAULT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 18;

    /**
     * The feature id for the '<em><b>Skip Content</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__SKIP_CONTENT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 19;

    /**
     * The feature id for the '<em><b>Sync Behavior</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__SYNC_BEHAVIOR = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 20;

    /**
     * The feature id for the '<em><b>Sync Behavior Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__SYNC_BEHAVIOR_DEFAULT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 21;

    /**
     * The feature id for the '<em><b>Sync Tolerance</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__SYNC_TOLERANCE = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 22;

    /**
     * The feature id for the '<em><b>Sync Tolerance Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__SYNC_TOLERANCE_DEFAULT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 23;

    /**
     * The feature id for the '<em><b>Target Element</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__TARGET_ELEMENT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 24;

    /**
     * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE__ANY_ATTRIBUTE = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 25;

    /**
     * The number of structural features of the '<em>Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE_FEATURE_COUNT = Smil20Package.SET_PROTOTYPE_FEATURE_COUNT + 26;

    /**
     * The number of operations of the '<em>Set Type</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SET_TYPE_OPERATION_COUNT = Smil20Package.SET_PROTOTYPE_OPERATION_COUNT + 0;


    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.language.AnimateColorType <em>Animate Color Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Animate Color Type</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType
     * @generated
     */
    EClass getAnimateColorType();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateColorType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getGroup()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Group();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateColorType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getAny()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Any();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getAlt <em>Alt</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Alt</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getAlt()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Alt();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getBegin <em>Begin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Begin</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getBegin()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Begin();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getCalcMode <em>Calc Mode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Calc Mode</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getCalcMode()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_CalcMode();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getClass_ <em>Class</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Class</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getClass_()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Class();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getDur <em>Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dur</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getDur()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Dur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getEnd <em>End</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getEnd()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_End();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getFill <em>Fill</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getFill()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Fill();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getFillDefault <em>Fill Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getFillDefault()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_FillDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getId()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Id();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getLang <em>Lang</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lang</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getLang()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Lang();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getLongdesc <em>Longdesc</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Longdesc</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getLongdesc()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Longdesc();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getMax <em>Max</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getMax()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Max();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getMin <em>Min</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getMin()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Min();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getRepeat <em>Repeat</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getRepeat()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Repeat();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getRepeatCount <em>Repeat Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Count</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getRepeatCount()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_RepeatCount();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getRepeatDur <em>Repeat Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Dur</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getRepeatDur()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_RepeatDur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getRestart <em>Restart</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getRestart()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_Restart();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getRestartDefault <em>Restart Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getRestartDefault()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_RestartDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#isSkipContent <em>Skip Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Skip Content</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#isSkipContent()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_SkipContent();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getSyncBehavior <em>Sync Behavior</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getSyncBehavior()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_SyncBehavior();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getSyncBehaviorDefault()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_SyncBehaviorDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getSyncTolerance <em>Sync Tolerance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getSyncTolerance()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_SyncTolerance();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getSyncToleranceDefault()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_SyncToleranceDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateColorType#getTargetElement <em>Target Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Target Element</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getTargetElement()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_TargetElement();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateColorType#getAnyAttribute <em>Any Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any Attribute</em>'.
     * @see org.w3._2001.smil20.language.AnimateColorType#getAnyAttribute()
     * @see #getAnimateColorType()
     * @generated
     */
    EAttribute getAnimateColorType_AnyAttribute();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.language.AnimateMotionType <em>Animate Motion Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Animate Motion Type</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType
     * @generated
     */
    EClass getAnimateMotionType();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateMotionType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getGroup()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Group();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateMotionType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getAny()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Any();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getAlt <em>Alt</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Alt</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getAlt()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Alt();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getBegin <em>Begin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Begin</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getBegin()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Begin();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getCalcMode <em>Calc Mode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Calc Mode</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getCalcMode()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_CalcMode();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getClass_ <em>Class</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Class</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getClass_()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Class();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getDur <em>Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dur</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getDur()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Dur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getEnd <em>End</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getEnd()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_End();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getFill <em>Fill</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getFill()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Fill();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getFillDefault <em>Fill Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getFillDefault()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_FillDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getId()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Id();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getLang <em>Lang</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lang</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getLang()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Lang();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getLongdesc <em>Longdesc</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Longdesc</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getLongdesc()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Longdesc();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getMax <em>Max</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getMax()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Max();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getMin <em>Min</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getMin()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Min();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getRepeat <em>Repeat</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getRepeat()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Repeat();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getRepeatCount <em>Repeat Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Count</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getRepeatCount()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_RepeatCount();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getRepeatDur <em>Repeat Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Dur</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getRepeatDur()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_RepeatDur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getRestart <em>Restart</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getRestart()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_Restart();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getRestartDefault <em>Restart Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getRestartDefault()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_RestartDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#isSkipContent <em>Skip Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Skip Content</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#isSkipContent()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_SkipContent();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getSyncBehavior <em>Sync Behavior</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getSyncBehavior()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_SyncBehavior();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getSyncBehaviorDefault()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_SyncBehaviorDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getSyncTolerance <em>Sync Tolerance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getSyncTolerance()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_SyncTolerance();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getSyncToleranceDefault()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_SyncToleranceDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateMotionType#getTargetElement <em>Target Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Target Element</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getTargetElement()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_TargetElement();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateMotionType#getAnyAttribute <em>Any Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any Attribute</em>'.
     * @see org.w3._2001.smil20.language.AnimateMotionType#getAnyAttribute()
     * @see #getAnimateMotionType()
     * @generated
     */
    EAttribute getAnimateMotionType_AnyAttribute();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.language.AnimateType <em>Animate Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Animate Type</em>'.
     * @see org.w3._2001.smil20.language.AnimateType
     * @generated
     */
    EClass getAnimateType();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getGroup()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Group();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getAny()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Any();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getAlt <em>Alt</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Alt</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getAlt()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Alt();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getBegin <em>Begin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Begin</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getBegin()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Begin();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getCalcMode <em>Calc Mode</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Calc Mode</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getCalcMode()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_CalcMode();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getClass_ <em>Class</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Class</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getClass_()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Class();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getDur <em>Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dur</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getDur()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Dur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getEnd <em>End</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getEnd()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_End();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getFill <em>Fill</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getFill()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Fill();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getFillDefault <em>Fill Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getFillDefault()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_FillDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getId()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Id();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getLang <em>Lang</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lang</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getLang()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Lang();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getLongdesc <em>Longdesc</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Longdesc</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getLongdesc()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Longdesc();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getMax <em>Max</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getMax()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Max();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getMin <em>Min</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getMin()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Min();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getRepeat <em>Repeat</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getRepeat()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Repeat();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getRepeatCount <em>Repeat Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Count</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getRepeatCount()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_RepeatCount();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getRepeatDur <em>Repeat Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Dur</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getRepeatDur()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_RepeatDur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getRestart <em>Restart</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getRestart()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_Restart();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getRestartDefault <em>Restart Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getRestartDefault()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_RestartDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#isSkipContent <em>Skip Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Skip Content</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#isSkipContent()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_SkipContent();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehavior <em>Sync Behavior</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getSyncBehavior()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_SyncBehavior();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getSyncBehaviorDefault()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_SyncBehaviorDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getSyncTolerance <em>Sync Tolerance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getSyncTolerance()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_SyncTolerance();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance Default</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getSyncToleranceDefault()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_SyncToleranceDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.AnimateType#getTargetElement <em>Target Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Target Element</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getTargetElement()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_TargetElement();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.AnimateType#getAnyAttribute <em>Any Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any Attribute</em>'.
     * @see org.w3._2001.smil20.language.AnimateType#getAnyAttribute()
     * @see #getAnimateType()
     * @generated
     */
    EAttribute getAnimateType_AnyAttribute();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.language.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Document Root</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.DocumentRoot#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map '{@link org.w3._2001.smil20.language.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map '{@link org.w3._2001.smil20.language.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.language.DocumentRoot#getAnimate <em>Animate</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Animate</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot#getAnimate()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Animate();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.language.DocumentRoot#getAnimateColor <em>Animate Color</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Animate Color</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot#getAnimateColor()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AnimateColor();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.language.DocumentRoot#getAnimateMotion <em>Animate Motion</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Animate Motion</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot#getAnimateMotion()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AnimateMotion();

    /**
     * Returns the meta object for the containment reference '{@link org.w3._2001.smil20.language.DocumentRoot#getSet <em>Set</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Set</em>'.
     * @see org.w3._2001.smil20.language.DocumentRoot#getSet()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Set();

    /**
     * Returns the meta object for class '{@link org.w3._2001.smil20.language.SetType <em>Set Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Set Type</em>'.
     * @see org.w3._2001.smil20.language.SetType
     * @generated
     */
    EClass getSetType();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.SetType#getGroup <em>Group</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.w3._2001.smil20.language.SetType#getGroup()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Group();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.SetType#getAny <em>Any</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any</em>'.
     * @see org.w3._2001.smil20.language.SetType#getAny()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Any();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getAlt <em>Alt</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Alt</em>'.
     * @see org.w3._2001.smil20.language.SetType#getAlt()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Alt();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getBegin <em>Begin</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Begin</em>'.
     * @see org.w3._2001.smil20.language.SetType#getBegin()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Begin();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getClass_ <em>Class</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Class</em>'.
     * @see org.w3._2001.smil20.language.SetType#getClass_()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Class();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getDur <em>Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Dur</em>'.
     * @see org.w3._2001.smil20.language.SetType#getDur()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Dur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getEnd <em>End</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>End</em>'.
     * @see org.w3._2001.smil20.language.SetType#getEnd()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_End();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getFill <em>Fill</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill</em>'.
     * @see org.w3._2001.smil20.language.SetType#getFill()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Fill();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getFillDefault <em>Fill Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Fill Default</em>'.
     * @see org.w3._2001.smil20.language.SetType#getFillDefault()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_FillDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.w3._2001.smil20.language.SetType#getId()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Id();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getLang <em>Lang</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Lang</em>'.
     * @see org.w3._2001.smil20.language.SetType#getLang()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Lang();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getLongdesc <em>Longdesc</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Longdesc</em>'.
     * @see org.w3._2001.smil20.language.SetType#getLongdesc()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Longdesc();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getMax <em>Max</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Max</em>'.
     * @see org.w3._2001.smil20.language.SetType#getMax()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Max();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getMin <em>Min</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Min</em>'.
     * @see org.w3._2001.smil20.language.SetType#getMin()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Min();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getRepeat <em>Repeat</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat</em>'.
     * @see org.w3._2001.smil20.language.SetType#getRepeat()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Repeat();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getRepeatCount <em>Repeat Count</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Count</em>'.
     * @see org.w3._2001.smil20.language.SetType#getRepeatCount()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_RepeatCount();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getRepeatDur <em>Repeat Dur</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Repeat Dur</em>'.
     * @see org.w3._2001.smil20.language.SetType#getRepeatDur()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_RepeatDur();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getRestart <em>Restart</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart</em>'.
     * @see org.w3._2001.smil20.language.SetType#getRestart()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_Restart();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getRestartDefault <em>Restart Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Restart Default</em>'.
     * @see org.w3._2001.smil20.language.SetType#getRestartDefault()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_RestartDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#isSkipContent <em>Skip Content</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Skip Content</em>'.
     * @see org.w3._2001.smil20.language.SetType#isSkipContent()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_SkipContent();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getSyncBehavior <em>Sync Behavior</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior</em>'.
     * @see org.w3._2001.smil20.language.SetType#getSyncBehavior()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_SyncBehavior();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getSyncBehaviorDefault <em>Sync Behavior Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Behavior Default</em>'.
     * @see org.w3._2001.smil20.language.SetType#getSyncBehaviorDefault()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_SyncBehaviorDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getSyncTolerance <em>Sync Tolerance</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance</em>'.
     * @see org.w3._2001.smil20.language.SetType#getSyncTolerance()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_SyncTolerance();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getSyncToleranceDefault <em>Sync Tolerance Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Sync Tolerance Default</em>'.
     * @see org.w3._2001.smil20.language.SetType#getSyncToleranceDefault()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_SyncToleranceDefault();

    /**
     * Returns the meta object for the attribute '{@link org.w3._2001.smil20.language.SetType#getTargetElement <em>Target Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Target Element</em>'.
     * @see org.w3._2001.smil20.language.SetType#getTargetElement()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_TargetElement();

    /**
     * Returns the meta object for the attribute list '{@link org.w3._2001.smil20.language.SetType#getAnyAttribute <em>Any Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Any Attribute</em>'.
     * @see org.w3._2001.smil20.language.SetType#getAnyAttribute()
     * @see #getSetType()
     * @generated
     */
    EAttribute getSetType_AnyAttribute();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    LanguageFactory getLanguageFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.language.impl.AnimateColorTypeImpl <em>Animate Color Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.language.impl.AnimateColorTypeImpl
         * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getAnimateColorType()
         * @generated
         */
        EClass ANIMATE_COLOR_TYPE = eINSTANCE.getAnimateColorType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__GROUP = eINSTANCE.getAnimateColorType_Group();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__ANY = eINSTANCE.getAnimateColorType_Any();

        /**
         * The meta object literal for the '<em><b>Alt</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__ALT = eINSTANCE.getAnimateColorType_Alt();

        /**
         * The meta object literal for the '<em><b>Begin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__BEGIN = eINSTANCE.getAnimateColorType_Begin();

        /**
         * The meta object literal for the '<em><b>Calc Mode</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__CALC_MODE = eINSTANCE.getAnimateColorType_CalcMode();

        /**
         * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__CLASS = eINSTANCE.getAnimateColorType_Class();

        /**
         * The meta object literal for the '<em><b>Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__DUR = eINSTANCE.getAnimateColorType_Dur();

        /**
         * The meta object literal for the '<em><b>End</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__END = eINSTANCE.getAnimateColorType_End();

        /**
         * The meta object literal for the '<em><b>Fill</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__FILL = eINSTANCE.getAnimateColorType_Fill();

        /**
         * The meta object literal for the '<em><b>Fill Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__FILL_DEFAULT = eINSTANCE.getAnimateColorType_FillDefault();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__ID = eINSTANCE.getAnimateColorType_Id();

        /**
         * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__LANG = eINSTANCE.getAnimateColorType_Lang();

        /**
         * The meta object literal for the '<em><b>Longdesc</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__LONGDESC = eINSTANCE.getAnimateColorType_Longdesc();

        /**
         * The meta object literal for the '<em><b>Max</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__MAX = eINSTANCE.getAnimateColorType_Max();

        /**
         * The meta object literal for the '<em><b>Min</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__MIN = eINSTANCE.getAnimateColorType_Min();

        /**
         * The meta object literal for the '<em><b>Repeat</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__REPEAT = eINSTANCE.getAnimateColorType_Repeat();

        /**
         * The meta object literal for the '<em><b>Repeat Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__REPEAT_COUNT = eINSTANCE.getAnimateColorType_RepeatCount();

        /**
         * The meta object literal for the '<em><b>Repeat Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__REPEAT_DUR = eINSTANCE.getAnimateColorType_RepeatDur();

        /**
         * The meta object literal for the '<em><b>Restart</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__RESTART = eINSTANCE.getAnimateColorType_Restart();

        /**
         * The meta object literal for the '<em><b>Restart Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__RESTART_DEFAULT = eINSTANCE.getAnimateColorType_RestartDefault();

        /**
         * The meta object literal for the '<em><b>Skip Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__SKIP_CONTENT = eINSTANCE.getAnimateColorType_SkipContent();

        /**
         * The meta object literal for the '<em><b>Sync Behavior</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__SYNC_BEHAVIOR = eINSTANCE.getAnimateColorType_SyncBehavior();

        /**
         * The meta object literal for the '<em><b>Sync Behavior Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__SYNC_BEHAVIOR_DEFAULT = eINSTANCE.getAnimateColorType_SyncBehaviorDefault();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__SYNC_TOLERANCE = eINSTANCE.getAnimateColorType_SyncTolerance();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__SYNC_TOLERANCE_DEFAULT = eINSTANCE.getAnimateColorType_SyncToleranceDefault();

        /**
         * The meta object literal for the '<em><b>Target Element</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__TARGET_ELEMENT = eINSTANCE.getAnimateColorType_TargetElement();

        /**
         * The meta object literal for the '<em><b>Any Attribute</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_COLOR_TYPE__ANY_ATTRIBUTE = eINSTANCE.getAnimateColorType_AnyAttribute();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.language.impl.AnimateMotionTypeImpl <em>Animate Motion Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.language.impl.AnimateMotionTypeImpl
         * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getAnimateMotionType()
         * @generated
         */
        EClass ANIMATE_MOTION_TYPE = eINSTANCE.getAnimateMotionType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__GROUP = eINSTANCE.getAnimateMotionType_Group();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__ANY = eINSTANCE.getAnimateMotionType_Any();

        /**
         * The meta object literal for the '<em><b>Alt</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__ALT = eINSTANCE.getAnimateMotionType_Alt();

        /**
         * The meta object literal for the '<em><b>Begin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__BEGIN = eINSTANCE.getAnimateMotionType_Begin();

        /**
         * The meta object literal for the '<em><b>Calc Mode</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__CALC_MODE = eINSTANCE.getAnimateMotionType_CalcMode();

        /**
         * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__CLASS = eINSTANCE.getAnimateMotionType_Class();

        /**
         * The meta object literal for the '<em><b>Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__DUR = eINSTANCE.getAnimateMotionType_Dur();

        /**
         * The meta object literal for the '<em><b>End</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__END = eINSTANCE.getAnimateMotionType_End();

        /**
         * The meta object literal for the '<em><b>Fill</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__FILL = eINSTANCE.getAnimateMotionType_Fill();

        /**
         * The meta object literal for the '<em><b>Fill Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__FILL_DEFAULT = eINSTANCE.getAnimateMotionType_FillDefault();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__ID = eINSTANCE.getAnimateMotionType_Id();

        /**
         * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__LANG = eINSTANCE.getAnimateMotionType_Lang();

        /**
         * The meta object literal for the '<em><b>Longdesc</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__LONGDESC = eINSTANCE.getAnimateMotionType_Longdesc();

        /**
         * The meta object literal for the '<em><b>Max</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__MAX = eINSTANCE.getAnimateMotionType_Max();

        /**
         * The meta object literal for the '<em><b>Min</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__MIN = eINSTANCE.getAnimateMotionType_Min();

        /**
         * The meta object literal for the '<em><b>Repeat</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__REPEAT = eINSTANCE.getAnimateMotionType_Repeat();

        /**
         * The meta object literal for the '<em><b>Repeat Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__REPEAT_COUNT = eINSTANCE.getAnimateMotionType_RepeatCount();

        /**
         * The meta object literal for the '<em><b>Repeat Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__REPEAT_DUR = eINSTANCE.getAnimateMotionType_RepeatDur();

        /**
         * The meta object literal for the '<em><b>Restart</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__RESTART = eINSTANCE.getAnimateMotionType_Restart();

        /**
         * The meta object literal for the '<em><b>Restart Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__RESTART_DEFAULT = eINSTANCE.getAnimateMotionType_RestartDefault();

        /**
         * The meta object literal for the '<em><b>Skip Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__SKIP_CONTENT = eINSTANCE.getAnimateMotionType_SkipContent();

        /**
         * The meta object literal for the '<em><b>Sync Behavior</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__SYNC_BEHAVIOR = eINSTANCE.getAnimateMotionType_SyncBehavior();

        /**
         * The meta object literal for the '<em><b>Sync Behavior Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__SYNC_BEHAVIOR_DEFAULT = eINSTANCE.getAnimateMotionType_SyncBehaviorDefault();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__SYNC_TOLERANCE = eINSTANCE.getAnimateMotionType_SyncTolerance();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__SYNC_TOLERANCE_DEFAULT = eINSTANCE.getAnimateMotionType_SyncToleranceDefault();

        /**
         * The meta object literal for the '<em><b>Target Element</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__TARGET_ELEMENT = eINSTANCE.getAnimateMotionType_TargetElement();

        /**
         * The meta object literal for the '<em><b>Any Attribute</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_MOTION_TYPE__ANY_ATTRIBUTE = eINSTANCE.getAnimateMotionType_AnyAttribute();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.language.impl.AnimateTypeImpl <em>Animate Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.language.impl.AnimateTypeImpl
         * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getAnimateType()
         * @generated
         */
        EClass ANIMATE_TYPE = eINSTANCE.getAnimateType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__GROUP = eINSTANCE.getAnimateType_Group();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__ANY = eINSTANCE.getAnimateType_Any();

        /**
         * The meta object literal for the '<em><b>Alt</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__ALT = eINSTANCE.getAnimateType_Alt();

        /**
         * The meta object literal for the '<em><b>Begin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__BEGIN = eINSTANCE.getAnimateType_Begin();

        /**
         * The meta object literal for the '<em><b>Calc Mode</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__CALC_MODE = eINSTANCE.getAnimateType_CalcMode();

        /**
         * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__CLASS = eINSTANCE.getAnimateType_Class();

        /**
         * The meta object literal for the '<em><b>Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__DUR = eINSTANCE.getAnimateType_Dur();

        /**
         * The meta object literal for the '<em><b>End</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__END = eINSTANCE.getAnimateType_End();

        /**
         * The meta object literal for the '<em><b>Fill</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__FILL = eINSTANCE.getAnimateType_Fill();

        /**
         * The meta object literal for the '<em><b>Fill Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__FILL_DEFAULT = eINSTANCE.getAnimateType_FillDefault();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__ID = eINSTANCE.getAnimateType_Id();

        /**
         * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__LANG = eINSTANCE.getAnimateType_Lang();

        /**
         * The meta object literal for the '<em><b>Longdesc</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__LONGDESC = eINSTANCE.getAnimateType_Longdesc();

        /**
         * The meta object literal for the '<em><b>Max</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__MAX = eINSTANCE.getAnimateType_Max();

        /**
         * The meta object literal for the '<em><b>Min</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__MIN = eINSTANCE.getAnimateType_Min();

        /**
         * The meta object literal for the '<em><b>Repeat</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__REPEAT = eINSTANCE.getAnimateType_Repeat();

        /**
         * The meta object literal for the '<em><b>Repeat Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__REPEAT_COUNT = eINSTANCE.getAnimateType_RepeatCount();

        /**
         * The meta object literal for the '<em><b>Repeat Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__REPEAT_DUR = eINSTANCE.getAnimateType_RepeatDur();

        /**
         * The meta object literal for the '<em><b>Restart</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__RESTART = eINSTANCE.getAnimateType_Restart();

        /**
         * The meta object literal for the '<em><b>Restart Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__RESTART_DEFAULT = eINSTANCE.getAnimateType_RestartDefault();

        /**
         * The meta object literal for the '<em><b>Skip Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__SKIP_CONTENT = eINSTANCE.getAnimateType_SkipContent();

        /**
         * The meta object literal for the '<em><b>Sync Behavior</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__SYNC_BEHAVIOR = eINSTANCE.getAnimateType_SyncBehavior();

        /**
         * The meta object literal for the '<em><b>Sync Behavior Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__SYNC_BEHAVIOR_DEFAULT = eINSTANCE.getAnimateType_SyncBehaviorDefault();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__SYNC_TOLERANCE = eINSTANCE.getAnimateType_SyncTolerance();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__SYNC_TOLERANCE_DEFAULT = eINSTANCE.getAnimateType_SyncToleranceDefault();

        /**
         * The meta object literal for the '<em><b>Target Element</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__TARGET_ELEMENT = eINSTANCE.getAnimateType_TargetElement();

        /**
         * The meta object literal for the '<em><b>Any Attribute</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute ANIMATE_TYPE__ANY_ATTRIBUTE = eINSTANCE.getAnimateType_AnyAttribute();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.language.impl.DocumentRootImpl <em>Document Root</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.language.impl.DocumentRootImpl
         * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getDocumentRoot()
         * @generated
         */
        EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

        /**
         * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

        /**
         * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

        /**
         * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

        /**
         * The meta object literal for the '<em><b>Animate</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANIMATE = eINSTANCE.getDocumentRoot_Animate();

        /**
         * The meta object literal for the '<em><b>Animate Color</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANIMATE_COLOR = eINSTANCE.getDocumentRoot_AnimateColor();

        /**
         * The meta object literal for the '<em><b>Animate Motion</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__ANIMATE_MOTION = eINSTANCE.getDocumentRoot_AnimateMotion();

        /**
         * The meta object literal for the '<em><b>Set</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DOCUMENT_ROOT__SET = eINSTANCE.getDocumentRoot_Set();

        /**
         * The meta object literal for the '{@link org.w3._2001.smil20.language.impl.SetTypeImpl <em>Set Type</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.w3._2001.smil20.language.impl.SetTypeImpl
         * @see org.w3._2001.smil20.language.impl.LanguagePackageImpl#getSetType()
         * @generated
         */
        EClass SET_TYPE = eINSTANCE.getSetType();

        /**
         * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__GROUP = eINSTANCE.getSetType_Group();

        /**
         * The meta object literal for the '<em><b>Any</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__ANY = eINSTANCE.getSetType_Any();

        /**
         * The meta object literal for the '<em><b>Alt</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__ALT = eINSTANCE.getSetType_Alt();

        /**
         * The meta object literal for the '<em><b>Begin</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__BEGIN = eINSTANCE.getSetType_Begin();

        /**
         * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__CLASS = eINSTANCE.getSetType_Class();

        /**
         * The meta object literal for the '<em><b>Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__DUR = eINSTANCE.getSetType_Dur();

        /**
         * The meta object literal for the '<em><b>End</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__END = eINSTANCE.getSetType_End();

        /**
         * The meta object literal for the '<em><b>Fill</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__FILL = eINSTANCE.getSetType_Fill();

        /**
         * The meta object literal for the '<em><b>Fill Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__FILL_DEFAULT = eINSTANCE.getSetType_FillDefault();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__ID = eINSTANCE.getSetType_Id();

        /**
         * The meta object literal for the '<em><b>Lang</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__LANG = eINSTANCE.getSetType_Lang();

        /**
         * The meta object literal for the '<em><b>Longdesc</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__LONGDESC = eINSTANCE.getSetType_Longdesc();

        /**
         * The meta object literal for the '<em><b>Max</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__MAX = eINSTANCE.getSetType_Max();

        /**
         * The meta object literal for the '<em><b>Min</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__MIN = eINSTANCE.getSetType_Min();

        /**
         * The meta object literal for the '<em><b>Repeat</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__REPEAT = eINSTANCE.getSetType_Repeat();

        /**
         * The meta object literal for the '<em><b>Repeat Count</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__REPEAT_COUNT = eINSTANCE.getSetType_RepeatCount();

        /**
         * The meta object literal for the '<em><b>Repeat Dur</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__REPEAT_DUR = eINSTANCE.getSetType_RepeatDur();

        /**
         * The meta object literal for the '<em><b>Restart</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__RESTART = eINSTANCE.getSetType_Restart();

        /**
         * The meta object literal for the '<em><b>Restart Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__RESTART_DEFAULT = eINSTANCE.getSetType_RestartDefault();

        /**
         * The meta object literal for the '<em><b>Skip Content</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__SKIP_CONTENT = eINSTANCE.getSetType_SkipContent();

        /**
         * The meta object literal for the '<em><b>Sync Behavior</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__SYNC_BEHAVIOR = eINSTANCE.getSetType_SyncBehavior();

        /**
         * The meta object literal for the '<em><b>Sync Behavior Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__SYNC_BEHAVIOR_DEFAULT = eINSTANCE.getSetType_SyncBehaviorDefault();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__SYNC_TOLERANCE = eINSTANCE.getSetType_SyncTolerance();

        /**
         * The meta object literal for the '<em><b>Sync Tolerance Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__SYNC_TOLERANCE_DEFAULT = eINSTANCE.getSetType_SyncToleranceDefault();

        /**
         * The meta object literal for the '<em><b>Target Element</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__TARGET_ELEMENT = eINSTANCE.getSetType_TargetElement();

        /**
         * The meta object literal for the '<em><b>Any Attribute</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute SET_TYPE__ANY_ATTRIBUTE = eINSTANCE.getSetType_AnyAttribute();

    }

} //LanguagePackage
