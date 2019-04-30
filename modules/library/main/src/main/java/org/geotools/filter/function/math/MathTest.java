package org.geotools.filter.function.math;

import org.junit.Assert;
import org.junit.Test;

public final class MathTest {

    public static int add(int first, int second) {
        return first + second;
    }

    public static int multiply(int multiplicand, int multiplier) {
        return multiplicand * multiplier;
    }

    public static double divide(int dividend, int divisor) {
        if (divisor == 0) throw new IllegalArgumentException("Cannot divide by zero (0).");

        return dividend / divisor;
    }

    @Test
    public void add_TwoPlusTwo_ReturnsFour() {
        // Arrange
        final int expected = 4;
        final double expected2 = 2;

        // Act
        final int actual = MathTest.add(2, 2);

        final int num1 = 0;
        final int num2 = 3;

        final double actual2 = MathTest.divide(num1, num2);

        // Assert
        Assert.assertEquals(actual, expected);
        Assert.assertEquals(actual2, expected2, 0.001);
    }
}
