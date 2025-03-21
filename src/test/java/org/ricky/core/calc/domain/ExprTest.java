package org.ricky.core.calc.domain;

import org.junit.jupiter.api.Test;

import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className ExprTest
 * @desc
 */
class ExprTest {

    @Test
    void it_works() {
        Expr expr = new Expr("3 + 4*2/(1-5)^2");
        Expr expr2 = new Expr("2^3^2");
        Expr expr3 = new Expr("-5%3");
        Expr expr4 = new Expr("3.5 + 4.2*(2-5.1)/2");

        assertEquals("3.0 + 4.0 * 2.0 / ( 1.0 - 5.0 ) ^ 2.0", expr.toString());
        assertEquals("2.0 ^ 3.0 ^ 2.0", expr2.toString());
        assertEquals("u- 5.0 % 3.0", expr3.toString());
        assertEquals("3.5 + 4.2 * ( 2.0 - 5.1 ) / 2.0", expr4.toString());

        assertEquals(3 + 4 * 2 / pow(1 - 5, 2), expr.eval());
        assertEquals(pow(2, pow(3, 2)), expr2.eval());
        assertEquals(-5 % 3, expr3.eval());
        assertEquals(3.5 + 4.2 * (2 - 5.1) / 2, expr4.eval());
    }

}