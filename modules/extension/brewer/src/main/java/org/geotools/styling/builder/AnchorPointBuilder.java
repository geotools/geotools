package org.geotools.styling.builder;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.ChildExpressionBuilder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.StyleFactory;

/**
 * AnchorPoint allows you specify which part of a graphic indicates the location.
 * <p>
 * As an example if your graphic is a pin the AnchorPoint will be the end of the pin.
 * <pre><code>AnchorPointBuilder<?> b = new AnchorPointBuilder();
 * AnchorPoint anchor = b.x(0.5).y(0.9).build();
 * </code></pre
 * 
 * @author Jody Garnett (LISAsoft)
 *
 * @source $URL$
 */
public class AnchorPointBuilder<P> implements Builder<AnchorPoint> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private P parent;
    private ChildExpressionBuilder<AnchorPointBuilder<P>> x = new ChildExpressionBuilder<AnchorPointBuilder<P>>(this);
    private ChildExpressionBuilder<AnchorPointBuilder<P>> y = new ChildExpressionBuilder<AnchorPointBuilder<P>>(this);
    boolean unset = true; // current value is null
    public AnchorPointBuilder(){
        this( null );
    }
    public AnchorPointBuilder(P parent){
        this.parent = parent;
        reset();
    }
    
    public AnchorPoint build() {
        if( unset ){
            return null;
        }
        AnchorPoint anchorPoint = sf.anchorPoint(x.build(), y.build());
        if( parent == null ){
            reset();
        }
        return anchorPoint;
    }
    
    public P end(){
        return parent;
    }

    public ChildExpressionBuilder<AnchorPointBuilder<P>> x() {
        return x;
    }
    public AnchorPointBuilder<P> x(double x) {
        this.x.literal( x );
        return this;
    }
    public ChildExpressionBuilder<AnchorPointBuilder<P>> y() {
        return y;
    }
    public AnchorPointBuilder<P> y(double y) {
        this.y.literal( y );
        return this;
    }
    public AnchorPointBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;        
        return this;
    }

    public AnchorPointBuilder<P> reset(AnchorPoint anchorPoint) {
        if( anchorPoint == null ){
            return reset();
        }
        x.reset().literal(anchorPoint.getAnchorPointX());
        y.reset().literal(anchorPoint.getAnchorPointY());
        unset = false; 
        return this;
    }

    public AnchorPointBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;        
        return this;
    }
    public AnchorPointBuilder<P> reset(org.opengis.style.AnchorPoint anchorPoint) {
        if( anchorPoint == null ){
            return reset();
        }
        x.reset().literal(anchorPoint.getAnchorPointX());
        y.reset().literal(anchorPoint.getAnchorPointY());
        unset = false; 
        return this;
    }

}
