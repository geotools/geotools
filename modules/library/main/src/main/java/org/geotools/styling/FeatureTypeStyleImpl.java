/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.styling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.feature.NameImpl;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.opengis.feature.type.Name;
import org.opengis.filter.Id;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.FeatureTypeStyle;
import org.opengis.style.SemanticType;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;

/**
 * Implementation of Feature Type Style; care is taken to ensure everything
 * is mutable.
 *
 * @author James Macgill
 * @author Johann Sorel (Geomatys)
 * @source $URL$
 * @version $Id$
 */
public class FeatureTypeStyleImpl implements org.geotools.styling.FeatureTypeStyle, Cloneable {
    
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.styling");
    
    private List<Rule> rules = new ArrayList<Rule>();
    private Set<SemanticType> semantics = new LinkedHashSet<SemanticType>();
    private Id featureInstances = null;
    private Set<Name> featureTypeNames = new LinkedHashSet<Name>();
    
    private DescriptionImpl description = new DescriptionImpl();
    private String name = "name";
    private OnLineResource online = null;

    /**
     * Creates a new instance of FeatureTypeStyleImpl
     *
     * @param rules DOCUMENT ME!
     */
    protected FeatureTypeStyleImpl(Rule[] rules) {
        this(Arrays.asList(rules));
    }

    protected FeatureTypeStyleImpl(List<Rule> arules) {
        rules = new ArrayList<Rule>();
        rules.addAll(arules);
    }

    /**
     * Creates a new instance of FeatureTypeStyleImpl
     */
    protected FeatureTypeStyleImpl() {
        rules = new ArrayList<Rule>();
    }
    
    public FeatureTypeStyleImpl(org.opengis.style.FeatureTypeStyle fts){
        this.description = new DescriptionImpl( fts.getDescription() );
        this.featureInstances = fts.getFeatureInstanceIDs();
        this.featureTypeNames = new LinkedHashSet<Name>(fts.featureTypeNames());
        this.name = fts.getName();
        this.rules = new ArrayList<Rule>();
        if( fts.rules() != null ){
            for (org.opengis.style.Rule rule : fts.rules()) {
                rules.add( RuleImpl.cast(rule) ); // need to deep copy?
            }
        }
        this.semantics = new LinkedHashSet<SemanticType>(fts.semanticTypeIdentifiers());
    }
    
    public List<Rule> rules() {
        return rules;
    }
    
    @Deprecated
    public org.geotools.styling.Rule[] getRules() {
        final org.geotools.styling.Rule[] ret;

        ret = new org.geotools.styling.Rule[rules.size()];
        for(int i=0, n=rules.size(); i<n; i++){
            ret[i] = (org.geotools.styling.Rule) rules.get(i);
        }
        
        return ret;
    }

    @Deprecated
    public void setRules(org.geotools.styling.Rule[] newRules) {
        rules = new ArrayList<Rule>();
        rules.addAll(Arrays.asList(newRules));

        // fireChanged();
    }

    @Deprecated
    public void addRule(org.geotools.styling.Rule rule) {
        rules.add(rule);

        // fireChildAdded(rule);
    }

    public Set<SemanticType> semanticTypeIdentifiers() {
        return semantics;
    }
    
    @Deprecated
    public String[] getSemanticTypeIdentifiers() {
        String[] ids = new String[semantics.size()];
        
        Iterator<SemanticType> types = semantics.iterator();
        int i=0;
        while (types.hasNext()){
            ids[i] = types.next().name();
            i++;
        }
        
        if(ids.length == 0){
            ids = new String[]{SemanticType.ANY.toString()};
        }
        
        return ids;
    }

    @Deprecated
    public void setSemanticTypeIdentifiers(String[] types) {
        semantics.clear();
        
        for(String id : types){
            
            SemanticType st = SemanticType.valueOf(id);
            
            if(st != null) semantics.add(st);
        }
        
    }

    public Set<Name> featureTypeNames() {
        return featureTypeNames;
    }
    
    @Deprecated
    public String getFeatureTypeName() {
        if(!featureTypeNames.isEmpty()){
            return featureTypeNames.iterator().next().getLocalPart();
        }else{
            return "Feature"; // this is the deafault value - matches to any feature
        }
    }
        
    @Deprecated
    public void setFeatureTypeName(String name) {
        featureTypeNames.clear();
        
        if (name.equals("feature")) {
            LOGGER.warning("FeatureTypeStyle with typename 'feature' - " +
                    "did you mean to say 'Feature' (with a capital F) for the 'generic' FeatureType");
        }

        Name featurename = new NameImpl(name);
        
        featureTypeNames.add(featurename);
    }

    public Id getFeatureInstanceIDs() {
        return featureInstances;
    }
    
    public Description getDescription() {
        return description;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @Deprecated
    public String getAbstract() {
        if( description == null || description.getAbstract() == null){
            return null;
        }
        return description.getAbstract().toString();
    }
    
    @Deprecated
    public void setAbstract(String abstractStr) {
        description.setAbstract(new SimpleInternationalString(abstractStr));
    }
    
    @Deprecated
    public String getTitle() {
        if( description == null || description.getTitle() == null){
            return null;
        }
        return description.getTitle().toString();
    }

    @Deprecated
    public void setTitle(String title) {
        description.setTitle(new SimpleInternationalString(title));
    }

    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    /**
     * Creates a deep copy clone of the FeatureTypeStyle.
     *
     * @see org.geotools.styling.FeatureTypeStyle#clone()
     */
    public Object clone() {
        FeatureTypeStyle clone;

        try {
            clone = (FeatureTypeStyle) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e); // this should never happen.
        }

        final List<Rule> rulesCopy = new ArrayList<Rule>();
        
        for(Rule rl : rules){
            rulesCopy.add( (Rule) ((Cloneable) rl).clone() );
        }
        
        clone.rules().clear();
        ((List<Rule>)clone.rules()).addAll(rulesCopy);
        clone.featureTypeNames().clear();
        clone.featureTypeNames().addAll(featureTypeNames);
        clone.semanticTypeIdentifiers().clear();
        clone.semanticTypeIdentifiers().addAll(semantics);
        
        return clone;
    }

    /**
     * Overrides hashCode.
     *
     * @return The hashcode.
     */
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (rules != null) {
            result = (PRIME * result) + rules.hashCode();
        }
        
        if (featureInstances != null) {
            result = (PRIME * result) + featureInstances.hashCode();
        }
        
        if (semantics != null) {
            result = (PRIME * result) + semantics.hashCode();
        }
        
        if (featureTypeNames != null) {
            result = (PRIME * result) + featureTypeNames.hashCode();
        }

        if (name != null) {
            result = (PRIME * result) + name.hashCode();
        }

        if (description != null) {
            result = (PRIME * result) + description.hashCode();
        }

        return result;
    }

    /**
     * Compares this FeatureTypeStyleImpl with another.
     * 
     * <p>
     * Two FeatureTypeStyles are equal if they contain equal properties and an
     * equal list of Rules.
     * </p>
     *
     * @param oth The other FeatureTypeStyleImpl to compare with.
     *
     * @return True if this and oth are equal.
     */
    public boolean equals(Object oth) {
                
        if (this == oth) {
            return true;
        }

        if (oth instanceof FeatureTypeStyleImpl) {
            FeatureTypeStyleImpl other = (FeatureTypeStyleImpl) oth;

            return Utilities.equals(name, other.name)
            && Utilities.equals(description, other.description)
            && Utilities.equals(rules, other.rules)
            && Utilities.equals(featureTypeNames, other.featureTypeNames)
            && Utilities.equals(semantics, other.semantics);
        }

        return false;
    }
    
    public String toString() {
    	StringBuffer buf = new StringBuffer();
    	buf.append( "FeatureTypeStyleImpl");
        buf.append( "[");
    	if( name != null ) {
    		buf.append(" name=");
    		buf.append( name );
    	}
    	else {
    		buf.append( " UNNAMED");
    	}
    	buf.append( ", ");
    	buf.append( featureTypeNames );
    	buf.append( ", rules=<");
    	buf.append( rules.size() );
    	buf.append( ">" );
    	if( rules.size() > 0 ){
    		buf.append( "(" );
    		buf.append( rules.get(0));
    		if( rules.size() > 1 ){
    			buf.append(",...");
    		}
    		buf.append( ")");
    	}    	
    	buf.append("]");
    	return buf.toString();
    }

    public void setOnlineResource(OnLineResource online) {
        this.online = online;
    }

    public OnLineResource getOnlineResource() {
        return online;
    }

    static FeatureTypeStyleImpl cast(FeatureTypeStyle featureTypeStyle) {
        if( featureTypeStyle == null){
            return null;
        }
        else if ( featureTypeStyle instanceof FeatureTypeStyleImpl){
            return (FeatureTypeStyleImpl) featureTypeStyle;
        }
        else {
            FeatureTypeStyleImpl copy = new FeatureTypeStyleImpl();
            // the above is a deep copy - replace with cast if we can
            return copy;
        }

    }
}
