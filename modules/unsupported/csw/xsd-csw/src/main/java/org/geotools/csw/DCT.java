package org.geotools.csw;

import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://purl.org/dc/terms/ schema.
 *
 * @generated
 */
public final class DCT extends XSD {

    /** singleton instance */
    private static final DCT instance = new DCT();
    
    /**
     * Returns the singleton instance.
     */
    public static final DCT getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private DCT() {
    }
    
    protected void addDependencies(Set dependencies) {
       dependencies.add(DC.getInstance());
    }
    
    /**
     * Returns 'http://purl.org/dc/terms/'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'rec-dcterms.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("rec-dcterms.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://purl.org/dc/terms/";
    
    /* Type Definitions */

    /* Elements */
    /** @generated */
    public static final QName recordAbstract = 
        new QName("http://purl.org/dc/terms/","abstract");
    /** @generated */
    public static final QName accessRights = 
        new QName("http://purl.org/dc/terms/","accessRights");
    /** @generated */
    public static final QName alternative = 
        new QName("http://purl.org/dc/terms/","alternative");
    /** @generated */
    public static final QName audience = 
        new QName("http://purl.org/dc/terms/","audience");
    /** @generated */
    public static final QName available = 
        new QName("http://purl.org/dc/terms/","available");
    /** @generated */
    public static final QName bibliographicCitation = 
        new QName("http://purl.org/dc/terms/","bibliographicCitation");
    /** @generated */
    public static final QName conformsTo = 
        new QName("http://purl.org/dc/terms/","conformsTo");
    /** @generated */
    public static final QName created = 
        new QName("http://purl.org/dc/terms/","created");
    /** @generated */
    public static final QName dateAccepted = 
        new QName("http://purl.org/dc/terms/","dateAccepted");
    /** @generated */
    public static final QName dateCopyrighted = 
        new QName("http://purl.org/dc/terms/","dateCopyrighted");
    /** @generated */
    public static final QName dateSubmitted = 
        new QName("http://purl.org/dc/terms/","dateSubmitted");
    /** @generated */
    public static final QName educationLevel = 
        new QName("http://purl.org/dc/terms/","educationLevel");
    /** @generated */
    public static final QName extent = 
        new QName("http://purl.org/dc/terms/","extent");
    /** @generated */
    public static final QName hasFormat = 
        new QName("http://purl.org/dc/terms/","hasFormat");
    /** @generated */
    public static final QName hasPart = 
        new QName("http://purl.org/dc/terms/","hasPart");
    /** @generated */
    public static final QName hasVersion = 
        new QName("http://purl.org/dc/terms/","hasVersion");
    /** @generated */
    public static final QName isFormatOf = 
        new QName("http://purl.org/dc/terms/","isFormatOf");
    /** @generated */
    public static final QName isPartOf = 
        new QName("http://purl.org/dc/terms/","isPartOf");
    /** @generated */
    public static final QName isReferencedBy = 
        new QName("http://purl.org/dc/terms/","isReferencedBy");
    /** @generated */
    public static final QName isReplacedBy = 
        new QName("http://purl.org/dc/terms/","isReplacedBy");
    /** @generated */
    public static final QName isRequiredBy = 
        new QName("http://purl.org/dc/terms/","isRequiredBy");
    /** @generated */
    public static final QName issued = 
        new QName("http://purl.org/dc/terms/","issued");
    /** @generated */
    public static final QName isVersionOf = 
        new QName("http://purl.org/dc/terms/","isVersionOf");
    /** @generated */
    public static final QName license = 
        new QName("http://purl.org/dc/terms/","license");
    /** @generated */
    public static final QName mediator = 
        new QName("http://purl.org/dc/terms/","mediator");
    /** @generated */
    public static final QName medium = 
        new QName("http://purl.org/dc/terms/","medium");
    /** @generated */
    public static final QName modified = 
        new QName("http://purl.org/dc/terms/","modified");
    /** @generated */
    public static final QName provenance = 
        new QName("http://purl.org/dc/terms/","provenance");
    /** @generated */
    public static final QName references = 
        new QName("http://purl.org/dc/terms/","references");
    /** @generated */
    public static final QName replaces = 
        new QName("http://purl.org/dc/terms/","replaces");
    /** @generated */
    public static final QName requires = 
        new QName("http://purl.org/dc/terms/","requires");
    /** @generated */
    public static final QName rightsHolder = 
        new QName("http://purl.org/dc/terms/","rightsHolder");
    /** @generated */
    public static final QName spatial = 
        new QName("http://purl.org/dc/terms/","spatial");
    /** @generated */
    public static final QName tableOfContents = 
        new QName("http://purl.org/dc/terms/","tableOfContents");
    /** @generated */
    public static final QName temporal = 
        new QName("http://purl.org/dc/terms/","temporal");
    /** @generated */
    public static final QName valid = 
        new QName("http://purl.org/dc/terms/","valid");

    /* Attributes */

}
    