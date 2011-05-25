package org.geotools.filter.expression;


import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * ExpressionBuilder acting as a simple wrapper around an Expression.
 *
 *
 * @source $URL$
 */
public class ExpressionBuilder implements Builder<Expression> {
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
    protected boolean unset = false;
    protected Builder<? extends Expression> delegate = new NilBuilder();
    
    public ExpressionBuilder(){
        reset();    
    }
    public ExpressionBuilder( Expression expr ){
        reset( expr );
    }
    
    /**
     * Define expression as a literal.
     * <p>
     * Example:<code>b.literal().value( 1 );</code>
     */
    public LiteralBuilder literal(){
        delegate = new LiteralBuilder();
        unset = false;
        return (LiteralBuilder) delegate;
    }
    public Builder<?> literal( Object literal ){
        delegate = new LiteralBuilder().value( literal );
        unset = false;
        return this;
    }
    
    public AddBuilder add(){
        delegate = new AddBuilder();
        unset = false;
        return (AddBuilder) delegate;
    }
    public MultiplyBuilder multiply(){
        delegate = new MultiplyBuilder();
        unset = false;
        return (MultiplyBuilder) delegate;
    }
    public DivideBuilder divide(){
        delegate = new DivideBuilder();
        unset = false;
        return (DivideBuilder) delegate;
    }
    
    public SubtractBuilder subtract(){
        delegate = new SubtractBuilder();
        unset = false;
        return (SubtractBuilder) delegate;
    }
    
    public PropertyNameBuilder property(){
        delegate = new PropertyNameBuilder();
        unset = false;
        return (PropertyNameBuilder) delegate;
    }
    
    public Builder<?> property(String xpath){
        delegate = new PropertyNameBuilder().property(xpath);
        unset = false;
        return this;
    }
    
    public FunctionBuilder function(){
        this.delegate = new FunctionBuilder();
        unset = false;
        return (FunctionBuilder) delegate;        
    }
    public FunctionBuilder function(String name){
        this.delegate = new FunctionBuilder().name(name);
        unset = false;
        return (FunctionBuilder) delegate;                
    }
    
    /**
     * Build the expression.
     */
    public Expression build() {
        if( unset ) {
            return null;
        }
        return delegate.build();
    }

    public ExpressionBuilder reset() {
        this.delegate = new NilBuilder();
        this.unset = false;
        return this;
    }
    
    public ExpressionBuilder reset(Expression original) {
        if( original == null ){
            return unset();
        }
        this.unset = false;
        if( original instanceof Literal){
            delegate = new LiteralBuilder( (Literal) original );
        }
        else if( original instanceof PropertyName){
            delegate = new PropertyNameBuilder( (PropertyName) original );
        }
        else if( original instanceof Function){
            delegate = new FunctionBuilder( (Function) original );
        }
        else if( original instanceof Add){
            delegate = new AddBuilder( (Add) original );
        }
        else if( original instanceof Divide){
            delegate = new DivideBuilder( (Divide) original );
        }
        else if( original instanceof Multiply){
            delegate = new MultiplyBuilder( (Multiply) original );
        }
        else if( original instanceof Subtract){
            delegate = new SubtractBuilder( (Subtract) original );
        }
        else {
            this.delegate = new NilBuilder();
        }
        return this;
    }

    public ExpressionBuilder unset() {
        this.unset = true;
        this.delegate = new NilBuilder();
        return this;
    }

    public boolean isUnset() {
        return unset;
    }
}
