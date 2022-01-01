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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberFunction;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionPurityTesting;
import walkingkooka.tree.expression.FakeExpressionNumberFunction;
import walkingkooka.tree.expression.FunctionExpressionName;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberFunctionExpressionFunctionTest implements ExpressionFunctionTesting<ExpressionNumberFunctionExpressionFunction<FakeExpressionFunctionContext>, ExpressionNumber, FakeExpressionFunctionContext>,
        ExpressionPurityTesting,
        ToStringTesting<ExpressionNumberFunctionExpressionFunction<FakeExpressionFunctionContext>> {

    private final static FunctionExpressionName NAME = FunctionExpressionName.with("test");

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DOUBLE;

    private final static ExpressionNumberFunction FUNCTION = new FakeExpressionNumberFunction() {
        @Override
        public BigDecimal mapBigDecimal(final BigDecimal value,
                                        final MathContext context) {
            return value.multiply(BigDecimal.valueOf(3));
        }

        @Override
        public double mapDouble(double value) {
            return value * 3;
        }
    };

    @Test
    public void testApplyNonExpressionNumberFails() {
        assertThrows(
                ClassCastException.class,
                () -> {
                    this.apply2("String expected ExpressionNumber!");
                }
        );
    }

    @Test
    public void testApplyZeroParametersFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    this.apply2();
                }
        );
    }

    @Test
    public void testApplyTwoParametersFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    this.apply2(KIND.create(0), KIND.create(1));
                }
        );
    }

    @Test
    public void testApplyBigDecimal() {
        final ExpressionNumberKind kind = ExpressionNumberKind.BIG_DECIMAL;

        this.applyAndCheck2(
                Lists.of(
                        kind.create(4)
                ),
                kind.create(4 * 3)
        );
    }

    @Test
    public void testApplyDouble() {
        this.applyAndCheck2(
                Lists.of(
                        KIND.create(4)
                ),
                KIND.create(4 * 3)
        );
    }

    @Test
    public void testIsLastParameterVariable() {
        this.checkEquals(
                false,
                this.createBiFunction().lsLastParameterVariable()
        );
    }

    @Test
    public void testIsPure() {
        this.isPureAndCheck(
                this.createBiFunction(),
                null,
                true
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), NAME.toString());
    }

    @Override
    public ExpressionNumberFunctionExpressionFunction<FakeExpressionFunctionContext> createBiFunction() {
        return ExpressionNumberFunctionExpressionFunction.with(
                NAME,
                FUNCTION
        );
    }

    @Override
    public FakeExpressionFunctionContext createContext() {
        return new FakeExpressionFunctionContext() {
            @Override
            public MathContext mathContext() {
                return new MathContext(32, RoundingMode.HALF_UP);
            }
        };
    }

    @Override
    public Class<ExpressionNumberFunctionExpressionFunction<FakeExpressionFunctionContext>> type() {
        return Cast.to(ExpressionNumberFunctionExpressionFunction.class);
    }
}
