package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.IdBuilder;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.type.Name;
import org.opengis.filter.Id;
import org.opengis.style.SemanticType;

public class FeatureTypeStyleBuilder<P> implements Builder<FeatureTypeStyle> {
    StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    String name;
    
    List<RuleBuilder<FeatureTypeStyleBuilder<P>>> rules = new ArrayList<RuleBuilder<FeatureTypeStyleBuilder<P>>>();

    
    DescriptionBuilder<FeatureTypeStyleBuilder<P>> description = new DescriptionBuilder<FeatureTypeStyleBuilder<P>>();

    LinkedHashSet<Name> featureTypeNames = new LinkedHashSet<Name>();

    private IdBuilder<FeatureTypeStyleBuilder<P>> definedFor = new IdBuilder<FeatureTypeStyleBuilder<P>>(this);

    private Set<SemanticType> types = new LinkedHashSet<SemanticType>();

    private boolean unset;
    
    // TODO : add semantic type identifier, provided it makes any sense to have it

    public FeatureTypeStyleBuilder() {
        this(null);
    }

    public FeatureTypeStyleBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public RuleBuilder<FeatureTypeStyleBuilder<P>> rule() {
        RuleBuilder<FeatureTypeStyleBuilder<P>> ruleBuilder = new RuleBuilder<FeatureTypeStyleBuilder<P>>(
                this);
        rules.add(ruleBuilder);
        return ruleBuilder;
    }

    public FeatureTypeStyleBuilder<P> name(String name) {
        this.name = name;
        return this;
    }

    public FeatureTypeStyleBuilder<P> title(String title) {
        this.description.title(title);
        return this;
    }

    public DescriptionBuilder<FeatureTypeStyleBuilder<P>> description() {
        return description;
    }

    /**
     * Accumulates another feature type name in the list of the feature type names for this
     * {@link FeatureTypeStyle}
     * 
     * @param featureTypeName
     * @return
     */
    public FeatureTypeStyleBuilder<P> featureTypeName(String featureTypeName) {
        this.featureTypeNames.add(new NameImpl(featureTypeName));
        return this;
    }

    
    public String name() {
        return name;
    }

    public List<RuleBuilder<FeatureTypeStyleBuilder<P>>> rules() {
        unset = false;
        return rules;
    }

    public FeatureTypeStyleBuilder<P> rules(List<Rule> rules) {
        unset = false;
        for( Rule rule : rules ){
            this.rules.add( new RuleBuilder<FeatureTypeStyleBuilder<P>>(this).reset( rule ));
        }
        return this;
    }

    public FeatureTypeStyleBuilder<P> description(Description description) {
        this.description.reset( description );
        this.unset = false;
        return this;
    }

    public LinkedHashSet<Name> featureTypeNames() {
        return featureTypeNames;
    }

    public void setFeatureTypeNames(List<Name> featureTypeNames) {
        this.featureTypeNames.addAll( featureTypeNames );
    }

    public IdBuilder<FeatureTypeStyleBuilder<P>> definedFor() {
        return definedFor;
    }

    public void definedFor(Id fids) {
        this.definedFor.reset( fids );
    }

    public Set<SemanticType> types() {
        return types;
    }

    /**
     * Accumulates another feature type name in the list of the feature type names for this
     * {@link FeatureTypeStyle}
     * 
     * @param featureTypeName
     * @return
     */
    public FeatureTypeStyleBuilder<P> featureTypeName(Name featureTypeName) {
        this.featureTypeNames.add(featureTypeName);
        unset = false;
        return this;
    }

    public FeatureTypeStyle build() {
        if( unset ){
            return null;
        }
        List<org.opengis.style.Rule> list = new ArrayList<org.opengis.style.Rule>();
        for( RuleBuilder<FeatureTypeStyleBuilder<P>> ruleBuilder : rules ){
            list.add( ruleBuilder.build() );
        }
        FeatureTypeStyle fts = sf.featureTypeStyle(name, description.build(), definedFor.build(), featureTypeNames, types, list);
        if( parent == null ) reset();
        return fts;
    }

    public FeatureTypeStyleBuilder<P> reset() {
        rules.clear();
        this.name = null;
        this.description.reset();
        this.definedFor.reset();
        this.featureTypeNames.clear();
        this.rules.clear();
        
        this.unset = false;        
        return this;
    }

    public Builder<FeatureTypeStyle> reset(FeatureTypeStyle fts) {
        if( fts == null ){
            return unset();
        }
        this.name = fts.getName();
        this.description.reset( fts.getDescription() );
        this.definedFor.reset( fts.getFeatureInstanceIDs() );
        this.featureTypeNames.clear();
        if( fts.featureTypeNames() != null ){
            this.featureTypeNames.addAll( fts.featureTypeNames() );
        }
        this.rules.clear();
        if( fts.rules() != null ){
            for( Rule rule : fts.rules() ){
                this.rules.add( new RuleBuilder<FeatureTypeStyleBuilder<P>>(this).reset( rule ) );
            }
        }            
        this.unset = false;        
        return this;
    }

    public Builder<FeatureTypeStyle> unset() {
        this.unset = true;        
        return this;
    }

}
