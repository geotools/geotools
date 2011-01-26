package org.geotools.styling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.AnchorPoint;
import org.opengis.style.Displacement;
import org.opengis.style.Graphic;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.StyleVisitor;

/**
 * Test the various Impl classes; many of these provide public api that is used by StyleImplFactory.
 * <p>
 * In particular this class is focused on:
 * <ul>
 * <li>Testing any methods not hit by StyleFactoryImpl and SLDParsing
 * <li>Going over the "cast" methods used to promote org.opengis.styling instances to a StyleImpl if
 * required. These are used to ensure that any set methods can handle a org.opengis.styling
 * instances.
 * </ul>
 *
 * @source $URL$
 */
public class StyleTest {
    static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    static StyleFactoryImpl2 sf = new StyleFactoryImpl2(ff);
    
    @Test
    public void displacement() {
        assertNull(DisplacementImpl.cast(null));

        DisplacementImpl displacement = new DisplacementImpl();
        displacement.setDisplacementX(1.0);
        displacement.setDisplacementY(1.0);

        assertSame(displacement, DisplacementImpl.cast(displacement));

        assertEquals( displacement, sf.displacement( ff.literal(1.0), ff.literal(1.0)));
        
        org.opengis.style.Displacement external = new Displacement() {
            public Expression getDisplacementY() {
                return ff.literal(1.0);
            }

            public Expression getDisplacementX() {
                return ff.literal(1.0);
            }

            public Object accept(StyleVisitor visitor, Object data) {
                return visitor.visit(this, data);
            }
        };        
        displacement = DisplacementImpl.cast( external );
        assertEquals( ff.literal(1.0), displacement.getDisplacementX());
        
    }
    @SuppressWarnings("deprecation")
    @Test
    public void font() throws Exception {
        List<Expression> family = new ArrayList<Expression>();
        family.add( ff.literal("ariel"));
        family.add( ff.literal("Helvetica"));
        family.add( ff.literal("sanserif"));
     
        Expression style=ff.literal("noraml");
        Expression weight = ff.literal("normal");
        Expression size = ff.literal(12);
        
        Font font = sf.font(family, style, weight, size);
        
        assertEquals( family, font.getFamily() );
        assertEquals( style, font.getStyle()); // oblique or italic
        assertEquals( weight, font.getWeight() ); // bold or normal
        assertEquals( size, font.getSize() );
        
        assertSame( font.getFontStyle(), font.getStyle() );
        assertSame( font.getFontFamily(), family.get(0));
        assertSame( font.getFontWeight(), font.getWeight() );
        assertSame( font.getFontSize(), font.getSize());
        
        FontImpl cast = FontImpl.cast( font );
        assertSame( cast, font );
    }
    @Test
    public void graphic() throws Exception {
        List<GraphicalSymbol> symbols = new ArrayList<GraphicalSymbol>();
        symbols.add(sf.mark(ff.literal("square"), null, null));
        symbols.add(sf.mark(ff.literal("circle"), null, null));
        
        Expression opacity = null;
        Expression size = null;
        Expression rotation = null;
        AnchorPoint anchor = null;
        Displacement disp = null;
        Graphic graphic = sf.graphic(symbols, opacity, size, rotation, anchor, disp);
        
        assertNotNull( graphic );
    }
}
