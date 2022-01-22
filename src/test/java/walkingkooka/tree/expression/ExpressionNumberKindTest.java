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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ExpressionNumberKindTest implements ClassTesting<ExpressionNumberKind> {

    // parse............................................................................................................

    @Test
    public void testParseBigDecimal() {
        final String text = "12.3";
        this.checkEquals(ExpressionNumberKind.BIG_DECIMAL.create(new BigDecimal(text)), ExpressionNumberKind.BIG_DECIMAL.parse(text));
    }

    @Test
    public void testParseDouble() {
        final String text = "12.3";
        this.checkEquals(ExpressionNumberKind.DOUBLE.create(Double.parseDouble(text)), ExpressionNumberKind.DOUBLE.parse(text));
    }

    // setSign..........................................................................................................

    @Test
    public void testSetSignBigDecimalNegative() {
        this.setSignAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                ExpressionNumberSign.NEGATIVE,
                ExpressionNumberKind.BIG_DECIMAL.create(-1)
        );
    }

    @Test
    public void testSetSignBigDecimalZero() {
        this.setSignAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                ExpressionNumberSign.ZERO,
                ExpressionNumberKind.BIG_DECIMAL.create(0)
        );
    }

    @Test
    public void testSetSignBigDecimalPositive() {
        this.setSignAndCheck(
                ExpressionNumberKind.BIG_DECIMAL,
                ExpressionNumberSign.POSITIVE,
                ExpressionNumberKind.BIG_DECIMAL.create(1)
        );
    }

    @Test
    public void testSetSignDoubleNegative() {
        this.setSignAndCheck(
                ExpressionNumberKind.DOUBLE,
                ExpressionNumberSign.NEGATIVE,
                ExpressionNumberKind.DOUBLE.create(-1.0)
        );
    }

    @Test
    public void testSetSignDoubleZero() {
        this.setSignAndCheck(
                ExpressionNumberKind.DOUBLE,
                ExpressionNumberSign.ZERO,
                ExpressionNumberKind.DOUBLE.create(0.0)
        );
    }

    @Test
    public void testSetSignDoublePositive() {
        this.setSignAndCheck(
                ExpressionNumberKind.DOUBLE,
                ExpressionNumberSign.POSITIVE,
                ExpressionNumberKind.DOUBLE.create(1.0)
        );
    }

    private void setSignAndCheck(final ExpressionNumberKind kind,
                                 final ExpressionNumberSign sign,
                                 final ExpressionNumber expected) {
        final ExpressionNumber number = kind.setSign(sign);
        this.checkEquals(
                expected,
                number,
                kind + " setSign " + sign
        );

        assertSame(
                kind,
                number.kind(),
                () -> "kind " + number
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ExpressionNumberKind> type() {
        return ExpressionNumberKind.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
