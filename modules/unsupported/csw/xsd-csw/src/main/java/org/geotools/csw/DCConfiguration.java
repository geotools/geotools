package org.geotools.csw;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.cat.csw20.Csw20Factory;

import org.geotools.xml.Configuration;
import org.geotools.xml.SimpleContentComplexEMFBinding;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://purl.org/dc/elements/1.1/ schema.
 *
 * @generated
 */
public class DCConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public DCConfiguration() {
       super(DC.getInstance());
       
       //TODO: add dependencies here
    }
    
    /**
     * Registers the bindings for the configuration.
     */
    protected void registerBindings(Map bindings) {
        bindings.put(DC.elementContainer, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.elementContainer));
        bindings.put(DC.SimpleLiteral, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.SimpleLiteral));
        bindings.put(DC.contributor, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.contributor));
        bindings.put(DC.coverage, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.coverage));
        bindings.put(DC.creator, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.creator));
        bindings.put(DC.date, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.date));
        bindings.put(DC.DCelement, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.DCelement));
        bindings.put(DC.description, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.description));
        bindings.put(DC.format, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.format));
        bindings.put(DC.identifier, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.identifier));
        bindings.put(DC.language, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.language));
        bindings.put(DC.publisher, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.publisher));
        bindings.put(DC.relation, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.relation));
        bindings.put(DC.rights, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.rights));
        bindings.put(DC.source, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.source));
        bindings.put(DC.subject, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.subject));
        bindings.put(DC.title, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.title));
        bindings.put(DC.type, new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC.type));
    }
    
    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Csw20Factory.eINSTANCE);
    }
    
    /**
     * Generates the bindings registrations for this class
     * @param args
     */
    public static void main(String[] args) {
        for(Field f : DC.class.getFields()) {
            if((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0 && f.getType().equals(QName.class)) {
                System.out.println("bindings.put(DC." + f.getName() + ", new SimpleContentComplexEMFBinding(Csw20Factory.eINSTANCE, DC."  + f.getName() + "));");
            }
        }
    }
} 