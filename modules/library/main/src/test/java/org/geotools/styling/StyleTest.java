package org.geotools.styling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;

/**
 * Test the various Impl classes; many of these provide public api that is used by StyleImplFactory.
 *
 * <p>In particular this class is focused on:
 *
 * <ul>
 *   <li>Testing any methods not hit by StyleFactoryImpl and SLDParsing
 *   <li>Going over the "cast" methods used to promote org.geotools.api.styling instances to a
 *       StyleImpl if required. These are used to ensure that any set methods can handle a
 *       org.geotools.api.styling instances.
 * </ul>
 */
public class StyleTest {
    static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    static StyleFactoryImpl sf = new StyleFactoryImpl(ff);

    @Test
    public void displacement() {
        assertNull(DisplacementImpl.cast(null));

        DisplacementImpl displacement = new DisplacementImpl();
        displacement.setDisplacementX(1.0);
        displacement.setDisplacementY(1.0);

        assertSame(displacement, DisplacementImpl.cast(displacement));

        assertEquals(displacement, sf.displacement(ff.literal(1.0), ff.literal(1.0)));

        org.geotools.api.style.Displacement external =
                new Displacement() {
                    @Override
                    public Expression getDisplacementY() {
                        return ff.literal(1.0);
                    }

                    @Override
                    public Expression getDisplacementX() {
                        return ff.literal(1.0);
                    }

                    @Override
                    public Object accept(TraversingStyleVisitor visitor, Object data) {
                        return visitor.visit(this, data);
                    }

                    /**
                     * Sets the expression that computes a pixel offset from the geometry point.
                     *
                     * @param x
                     */
                    @Override
                    public void setDisplacementX(Expression x) {}

                    /**
                     * Sets the expression that computes a pixel offset from the geometry point.
                     *
                     * @param y
                     */
                    @Override
                    public void setDisplacementY(Expression y) {}

                    @Override
                    public void accept(StyleVisitor visitor) {}
                };
        displacement = DisplacementImpl.cast(external);
        assertEquals(ff.literal(1.0), displacement.getDisplacementX());
    }

    @Test
    public void font() throws Exception {
        List<Expression> family = new ArrayList<>();
        family.add(ff.literal("ariel"));
        family.add(ff.literal("Helvetica"));
        family.add(ff.literal("sanserif"));

        Expression style = ff.literal("noraml");
        Expression weight = ff.literal("normal");
        Expression size = ff.literal(12);

        Font font = sf.font(family, style, weight, size);

        assertEquals(family, font.getFamily());
        assertEquals(style, font.getStyle()); // oblique or italic
        assertEquals(weight, font.getWeight()); // bold or normal
        assertEquals(size, font.getSize());

        FontImpl cast = FontImpl.cast(font);
        assertSame(cast, font);
    }

    @Test
    public void graphic() throws Exception {
        List<GraphicalSymbol> symbols = new ArrayList<>();
        symbols.add(sf.mark(ff.literal("square"), null, null));
        symbols.add(sf.mark(ff.literal("circle"), null, null));

        Expression opacity = null;
        Expression size = null;
        Expression rotation = null;
        AnchorPoint anchor = null;
        Displacement disp = null;
        Graphic graphic = sf.graphic(symbols, opacity, size, rotation, anchor, disp);

        assertNotNull(graphic);
    }
}
