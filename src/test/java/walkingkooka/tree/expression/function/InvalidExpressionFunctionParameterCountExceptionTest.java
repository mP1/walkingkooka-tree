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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting2;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class InvalidExpressionFunctionParameterCountExceptionTest implements ThrowableTesting2<InvalidExpressionFunctionParameterCountException> {

    @Override
    public void testAllConstructorsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testCheckParametersWithNullParametersFails() {
        assertThrows(
            NullPointerException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                null,
                new FakeExpressionFunction<Void, ExpressionEvaluationContext>()
            )
        );
    }

    @Test
    public void testCheckParametersWithNullFunctionsFails() {
        assertThrows(
            NullPointerException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                Lists.empty(),
                null
            )
        );
    }

    @Test
    public void testCheckParametersExpectingNone() {
        InvalidExpressionFunctionParameterCountException.checkParameters(
            Lists.empty(),
            new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                @Override
                public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                    checkEquals(0, count, "parameters count");
                    return ExpressionFunction.NO_PARAMETERS;
                }
            }
        );
    }

    @Test
    public void testCheckParametersExpectingSeveral() {
        InvalidExpressionFunctionParameterCountException.checkParameters(
            Lists.of(
                111
            ),
            new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                @Override
                public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                    checkEquals(1, count, "parameters count");
                    return Lists.of(
                        ExpressionFunctionParameter.NUMBER
                    );
                }
            }
        );
    }

    @Test
    public void testCheckParametersTooFewFails() {
        final InvalidExpressionFunctionParameterCountException thrown = assertThrows(
            InvalidExpressionFunctionParameterCountException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                Lists.of(
                    111
                ),
                new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        checkEquals(1, count, "parameters count");
                        return Lists.of(
                            ExpressionFunctionParameter.DATE,
                            ExpressionFunctionParameter.DATETIME,
                            ExpressionFunctionParameter.TIME
                        );
                    }

                    @Override
                    public Optional<ExpressionFunctionName> name() {
                        return Optional.of(
                            ExpressionFunctionName.with("Hello")
                        );
                    }
                }
            )
        );
        this.checkMessage(
            thrown,
            "Hello: Missing parameter(s): date-time, time"
        );
    }

    @Test
    public void testCheckParametersTooFewWithoutFunctionNameFails() {
        final InvalidExpressionFunctionParameterCountException thrown = assertThrows(
            InvalidExpressionFunctionParameterCountException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                Lists.of(
                    111
                ),
                new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        checkEquals(1, count, "parameters count");
                        return Lists.of(
                            ExpressionFunctionParameter.DATE,
                            ExpressionFunctionParameter.DATETIME,
                            ExpressionFunctionParameter.TIME
                        );
                    }

                    @Override
                    public Optional<ExpressionFunctionName> name() {
                        return ExpressionFunction.ANONYMOUS_NAME;
                    }
                }
            )
        );
        this.checkMessage(
            thrown,
            "Anonymous: Missing parameter(s): date-time, time"
        );
    }

    @Test
    public void testCheckParametersTooFewAndSetFunctionNameFails() {
        final InvalidExpressionFunctionParameterCountException thrown = assertThrows(
            InvalidExpressionFunctionParameterCountException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                Lists.of(
                    111
                ),
                new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        checkEquals(1, count, "parameters count");
                        return Lists.of(
                            ExpressionFunctionParameter.DATE,
                            ExpressionFunctionParameter.DATETIME,
                            ExpressionFunctionParameter.TIME
                        );
                    }

                    @Override
                    public Optional<ExpressionFunctionName> name() {
                        return Optional.of(
                            ExpressionFunctionName.with("Lost")
                        );
                    }
                }
            )
        );
        this.checkMessage(
            thrown.setFunctionName(
                Optional.of(
                    ExpressionFunctionName.with("Replaced")
                )
            ),
            "Replaced: Missing parameter(s): date-time, time"
        );
    }

    @Test
    public void testCheckParametersTooManyFails() {
        final InvalidExpressionFunctionParameterCountException thrown = assertThrows(
            InvalidExpressionFunctionParameterCountException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                Lists.of(
                    111,
                    222,
                    333
                ),
                new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        checkEquals(3, count, "parameters count");
                        return Lists.of(
                            ExpressionFunctionParameter.DATE
                        );
                    }

                    @Override
                    public Optional<ExpressionFunctionName> name() {
                        return Optional.of(
                            ExpressionFunctionName.with("Hello")
                        );
                    }
                }
            )
        );
        this.checkMessage(
            thrown,
            "Hello: 2 extra parameter values"
        );
    }

    @Test
    public void testCheckParametersTooManyWithoutFunctionNameFails() {
        final InvalidExpressionFunctionParameterCountException thrown = assertThrows(
            InvalidExpressionFunctionParameterCountException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                Lists.of(
                    111,
                    222,
                    333
                ),
                new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        checkEquals(3, count, "parameters count");
                        return Lists.of(
                            ExpressionFunctionParameter.DATE
                        );
                    }

                    @Override
                    public Optional<ExpressionFunctionName> name() {
                        return ExpressionFunction.ANONYMOUS_NAME;
                    }
                }
            )
        );
        this.checkMessage(
            thrown,
            "Anonymous: 2 extra parameter values"
        );
    }

    @Test
    public void testCheckParametersTooManySetFunctionNameFails() {
        final InvalidExpressionFunctionParameterCountException thrown = assertThrows(
            InvalidExpressionFunctionParameterCountException.class,
            () -> InvalidExpressionFunctionParameterCountException.checkParameters(
                Lists.of(
                    111,
                    222,
                    333
                ),
                new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        checkEquals(3, count, "parameters count");
                        return Lists.of(
                            ExpressionFunctionParameter.DATE
                        );
                    }

                    @Override
                    public Optional<ExpressionFunctionName> name() {
                        return Optional.of(
                            ExpressionFunctionName.with("Lost")
                        );
                    }
                }
            )
        );
        this.checkMessage(
            thrown.setFunctionName(
                Optional.of(
                    ExpressionFunctionName.with("Hello")
                )
            ),
            "Hello: 2 extra parameter values"
        );
    }

    // class............................................................................................................

    @Override
    public Class<InvalidExpressionFunctionParameterCountException> type() {
        return InvalidExpressionFunctionParameterCountException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
