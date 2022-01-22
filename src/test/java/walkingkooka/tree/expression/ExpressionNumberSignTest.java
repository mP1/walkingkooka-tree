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

public final class ExpressionNumberSignTest implements ClassTesting<ExpressionNumberSign> {

    @Test
    public void testBigDecimalZero() {
        assertSame(
                ExpressionNumberSign.ZERO,
                ExpressionNumberSign.pick(BigDecimal.ZERO)
        );
    }

    @Test
    public void testBigDecimalNegative() {
        assertSame(
                ExpressionNumberSign.NEGATIVE,
                ExpressionNumberSign.pick(BigDecimal.valueOf(-1))
        );
    }

    @Test
    public void testBigDecimalPositive() {
        assertSame(
                ExpressionNumberSign.POSITIVE,
                ExpressionNumberSign.pick(BigDecimal.ONE)
        );
    }

    @Test
    public void testDoubleZero() {
        assertSame(
                ExpressionNumberSign.ZERO,
                ExpressionNumberSign.pick(0.0)
        );
    }

    @Test
    public void testDoubleNegative() {
        assertSame(
                ExpressionNumberSign.NEGATIVE,
                ExpressionNumberSign.pick(-1)
        );
    }

    @Test
    public void testDoublePositive() {
        assertSame(
                ExpressionNumberSign.POSITIVE,
                ExpressionNumberSign.pick(1)
        );
    }

    @Override
    public Class<ExpressionNumberSign> type() {
        return ExpressionNumberSign.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
