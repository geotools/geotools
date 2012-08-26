package org.geotools.csw;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Factory;

import org.geotools.xml.Configuration;
import org.geotools.xml.SimpleContentComplexEMFBinding;

/**
 * Parser configuration for the http://purl.org/dc/terms/ schema.
 *
 * @generated
 */
public class DCTConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public DCTConfiguration() {
       super(DCT.getInstance());
       
       addDependency(new DCConfiguration());
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected void registerBindings(Map bindings) {
        bindings.put(DCT.recordAbstract, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.accessRights, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.alternative, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.audience, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.available, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.bibliographicCitation, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.conformsTo, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.created, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.dateAccepted, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.dateCopyrighted, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.dateSubmitted, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.educationLevel, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.extent, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.hasFormat, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.hasPart, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.hasVersion, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.isFormatOf, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.isPartOf, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.isReferencedBy, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.isReplacedBy, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.isRequiredBy, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.issued, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.isVersionOf, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.license, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.mediator, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.medium, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.modified, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.provenance, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.references, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.replaces, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.requires, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.rightsHolder, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.spatial, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.tableOfContents, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.temporal, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DCT.valid, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
    }
    
    /**
     * Generates the bindings registrations for this class
     * @param args
     */
    public static void main(String[] args) {
        for(Field f : DCT.class.getFields()) {
            if((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0 && f.getType().equals(QName.class)) {
                System.out.println("bindings.put(DCT." + f.getName() + ", new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));");
            }
        }
    }
} 