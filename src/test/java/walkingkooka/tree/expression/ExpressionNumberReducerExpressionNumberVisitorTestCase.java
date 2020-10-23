/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExpressionNumberReducerExpressionNumberVisitorTestCase<V extends ExpressionNumberReducerExpressionNumberVisitor>
        extends ExpressionNumberReducerTestCase<V>
        implements ExpressionNumberVisitorTesting<V>, ToStringTesting<V> {

    ExpressionNumberReducerExpressionNumberVisitorTestCase() {
        super();
    }

    @Test
    public final void testByte() {
        this.functionAndCheck((byte) this.value(), Long.valueOf(this.expected()));
    }

    @Test
    public final void testShort() {
        this.functionAndCheck((short) this.value(), Long.valueOf(this.expected()));
    }

    @Test
    public final void testInteger() {
        this.functionAndCheck((int) this.value(), Long.valueOf(this.expected()));
    }

    @Test
    public final void testLong() {
        this.functionAndCheck((long) this.value(), Long.valueOf(this.expected()));
    }

    @Test
    public final void testFloat() {
        this.functionAndCheck((float) this.value(), Double.valueOf(this.expected()));
    }

    @Test
    public final void testDouble() {
        this.functionAndCheck((double) this.value(), Double.valueOf(this.expected()));
    }

    @Test
    public final void testBigInteger() {
        this.functionAndCheck(BigDecimal.valueOf(this.value()), BigDecimal.valueOf(this.expected()));
    }

    @Test
    public final void testBigDecimal() {
        this.functionAndCheck(BigDecimal.valueOf(this.value()), BigDecimal.valueOf(this.expected()));
    }

    // toString...........................................................................................................

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.createVisitor().toString(), this.expectedToString());
    }

    abstract String expectedToString();

    abstract Number expectedNumber(final Number number);

    // helpers..........................................................................................................

    final void functionAndCheck(final Number value, final Number expected) {
        assertEquals(this.expectedNumber(bigDecimalFix(expected)), bigDecimalFix(this.function(value, CONTEXT)));
    }

    abstract int value();

    abstract int expected();

    abstract Number function(final Number value, final ExpressionNumberReducerContext context);

    @Override
    public final String typeNamePrefix() {
        return ExpressionNumberReducerExpressionNumberVisitor.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
