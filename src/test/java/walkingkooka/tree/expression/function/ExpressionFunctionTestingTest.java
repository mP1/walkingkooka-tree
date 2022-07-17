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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionTestingTest implements ClassTesting<ExpressionFunctionTesting<?, ?, ?>> {

    private final static ExpressionEvaluationContext CONTEXT = ExpressionEvaluationContexts.fake();

    private final ExpressionFunctionTesting<ExpressionFunction<Object, ExpressionEvaluationContext>, Object, ExpressionEvaluationContext> TESTING = new ExpressionFunctionTesting<>() {

        @Override
        public Class<ExpressionFunction<Object, ExpressionEvaluationContext>> type() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExpressionFunction<Object, ExpressionEvaluationContext> createBiFunction() {
            return ExpressionFunctionTestingTest.this.stringConcatParameters();
        }

        @Override
        public ExpressionEvaluationContext createContext() {
            return ExpressionFunctionTestingTest.this.createContext();
        }
    };

    private final static ExpressionReference REFERENCE = new ExpressionReference() {
        @Override
        public String toString() {
            return "ExpressionReference123";
        }
    };

    // applyAndCheck ...................................................................................................

    @Test
    public void testApplyAndCheck() {
        final List<Object> parameters = Lists.of(true, 1, "***parameters3***");
        final Object expected = "**123**";

        new ExpressionFunctionTesting<ExpressionFunction<Object, ExpressionEvaluationContext>, Object, ExpressionEvaluationContext>() {

            @Override
            public Class<ExpressionFunction<Object, ExpressionEvaluationContext>> type() {
                throw new UnsupportedOperationException();
            }

            @Override
            public ExpressionFunction<Object, ExpressionEvaluationContext> createBiFunction() {
                return new FakeExpressionFunction<>() {
                    @Override
                    public Object apply(final List<Object> p,
                                        final ExpressionEvaluationContext c) {
                        checkEquals(parameters, p);
                        checkEquals(CONTEXT, c);
                        return expected;
                    }
                };
            }

            @Override
            public ExpressionEvaluationContext createContext() {
                return CONTEXT;
            }
        }.applyAndCheck(parameters, expected);
    }

    // applyAndCheck kind/parameter checks.............................................................................

    @Test
    public void testApplyEvaluateExpressionParameterExpressionFails() {
        this.applyFails(
                EnumSet.of(ExpressionFunctionKind.EVALUATE_PARAMETERS),
                Lists.of(
                        Expression.value(1)
                ),
                "Should not include parameter(s) of type walkingkooka.tree.expression.Expression ==> expected: <[]> but was: <[1]>"
        );
    }

    @Test
    public void testApplyFlattenParameterListFails() {
        this.applyFails(
                EnumSet.of(ExpressionFunctionKind.FLATTEN),
                Lists.of(
                        Lists.empty()
                ),
                "Should not include parameter(s) of type java.util.List ==> expected: <[]> but was: <[[]]>"
        );
    }

    @Test
    public void testApplyResolveReferenceParameterReferenceFails() {
        this.applyFails(
                EnumSet.of(ExpressionFunctionKind.RESOLVE_REFERENCES),
                Lists.of(
                        REFERENCE
                ),
                "Should not include parameter(s) of type walkingkooka.tree.expression.ExpressionReference ==> expected: <[]> but was: <[ExpressionReference123]>"
        );
    }

    private void applyFails(final Set<ExpressionFunctionKind> kinds,
                            final List<Object> parameters,
                            final String message) {
        final AssertionError thrown = assertThrows(
                AssertionError.class,
                () -> TESTING.applyAndCheck2(
                        new FakeExpressionFunction<>() {
                            @Override
                            public Set<ExpressionFunctionKind> kinds() {
                                return kinds;
                            }
                        },
                        parameters,
                        ExpressionEvaluationContexts.fake()
                )
        );
        this.checkEquals(
                message,
                thrown.getMessage(),
                kinds + " " + parameters
        );
    }

    @Test
    public void testApplyAndCheckWithExpressionParameter() {
        TESTING.applyAndCheck2(
                this.stringConcatParameters(),
                Lists.of(
                        Expression.value(1)
                ),
                "1"
        );
    }

    @Test
    public void testApplyAndCheckWithListParameter() {
        TESTING.applyAndCheck2(
                this.stringConcatParameters(),
                Lists.of(
                        Lists.empty()
                ),
                "[]"
        );
    }

    @Test
    public void testApplyAndCheckWithExpressionReferenceParameter() {
        TESTING.applyAndCheck2(
                this.stringConcatParameters(),
                Lists.of(
                        REFERENCE
                ),
                REFERENCE.toString()
        );
    }


    // applyAndCheck....................................................................................................

    @Test
    public void testApplyAndCheck2ObjectVar() {
        TESTING.apply2(Lists.of("hello", "2"));
    }

    @Test
    public void testApplyAndCheck2ListResult() {
        TESTING.applyAndCheck2(
                Lists.of("hello", "2"),
                "hello2"
        );
    }

    @Test
    public void testApplyAndCheck2ListResultFails() {
        boolean fails = false;

        try {
            TESTING.applyAndCheck2(
                    Lists.of("hello", "2"),
                    "fail!!"
            );
        } catch (final Error cause) {
            fails = true;
        }
        this.checkEquals(true, fails);
    }

    @Test
    public void testApplyAndCheck2FunctionListResult() {
        TESTING.applyAndCheck2(
                stringConcatParameters(),
                Lists.of("hello", "2"),
                "hello2"
        );
    }

    @Test
    public void testApplyAndCheck2FunctionListResultFails() {
        boolean fails = false;

        try {
            TESTING.applyAndCheck2(
                    stringConcatParameters(),
                    Lists.of("hello", "2"),
                    "fail!!"
            );
        } catch (final Error cause) {
            fails = true;
        }
        this.checkEquals(true, fails);
    }

    private FakeExpressionFunction<Object, ExpressionEvaluationContext> stringConcatParameters() {
        return new FakeExpressionFunction<>() {
            @Override
            public String apply(final List<Object> objects,
                                final ExpressionEvaluationContext context) {
                return objects.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining());
            }

            @Override
            public Set<ExpressionFunctionKind> kinds() {
                return Sets.empty();
            }
        };
    }

    // testParameterNamesUnique.........................................................................................

    @Test
    public void testTestParameterNamesUnique() {
        new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>, Void, FakeExpressionEvaluationContext>() {

            @Override
            public FakeExpressionFunction<Void, FakeExpressionEvaluationContext> createBiFunction() {
                return new FakeExpressionFunction<>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        return Lists.of(
                                ExpressionFunctionParameter.BOOLEAN,
                                ExpressionFunctionParameter.CHARACTER,
                                ExpressionFunctionParameter.DATE
                        );
                    }
                };
            }

            @Override
            public FakeExpressionEvaluationContext createContext() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>> type() {
                throw new UnsupportedOperationException();
            }
        }.testParameterNamesUnique();
    }

    @Test
    public void testTestParameterNamesUniqueDuplicatesFail() {
        boolean fail = false;

        try {
            new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>, Void, FakeExpressionEvaluationContext>() {

                @Override
                public FakeExpressionFunction<Void, FakeExpressionEvaluationContext> createBiFunction() {
                    return new FakeExpressionFunction<>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                            return Lists.of(
                                    ExpressionFunctionParameter.BOOLEAN,
                                    ExpressionFunctionParameter.CHARACTER,
                                    ExpressionFunctionParameter.BOOLEAN
                            );
                        }
                    };
                }

                @Override
                public FakeExpressionEvaluationContext createContext() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Class<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>> type() {
                    throw new UnsupportedOperationException();
                }
            }.testParameterNamesUnique();
        } catch (final AssertionError expected) {
            fail = true;
        }

        this.checkEquals(true, fail);
    }

    // testParametersOptionalNotBeforeRequired.........................................................................

    @Test
    public void testParametersOptionalNotBeforeRequired() {
        new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>, Void, FakeExpressionEvaluationContext>() {

            @Override
            public FakeExpressionFunction<Void, FakeExpressionEvaluationContext> createBiFunction() {
                return new FakeExpressionFunction<>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        return Lists.of(
                                ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                ExpressionFunctionParameterName.CHARACTER.optional(Character.class)
                        );
                    }
                };
            }

            @Override
            public FakeExpressionEvaluationContext createContext() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>> type() {
                throw new UnsupportedOperationException();
            }
        }.testParametersOptionalNotBeforeRequired();
    }

    @Test
    public void testParametersOptionalNotBeforeRequiredFails() {
        boolean fail = false;

        try {
            new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>, Void, FakeExpressionEvaluationContext>() {

                @Override
                public FakeExpressionFunction<Void, FakeExpressionEvaluationContext> createBiFunction() {
                    return new FakeExpressionFunction<>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                            return Lists.of(
                                    ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                    ExpressionFunctionParameterName.CHARACTER.optional(Character.class),
                                    ExpressionFunctionParameterName.DATE.required(LocalDate.class)
                            );
                        }
                    };
                }

                @Override
                public FakeExpressionEvaluationContext createContext() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Class<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>> type() {
                    throw new UnsupportedOperationException();
                }
            }.testParametersOptionalNotBeforeRequired();
        } catch (final AssertionError expected) {
            fail = true;
        }

        this.checkEquals(true, fail);
    }

    // testParametersOnlyLastMayBeVariable..............................................................................

    @Test
    public void testParametersOnlyLastMayBeVariable() {
        new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>, Void, FakeExpressionEvaluationContext>() {

            @Override
            public FakeExpressionFunction<Void, FakeExpressionEvaluationContext> createBiFunction() {
                return new FakeExpressionFunction<>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                        return Lists.of(
                                ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                ExpressionFunctionParameterName.CHARACTER.variable(Character.class)
                        );
                    }
                };
            }

            @Override
            public FakeExpressionEvaluationContext createContext() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>> type() {
                throw new UnsupportedOperationException();
            }
        }.testParametersOnlyLastMayBeVariable();
    }

    @Test
    public void testParametersOnlyLastMayBeVariableFails() {
        boolean fail = false;

        try {
            new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>, Void, FakeExpressionEvaluationContext>() {

                @Override
                public FakeExpressionFunction<Void, FakeExpressionEvaluationContext> createBiFunction() {
                    return new FakeExpressionFunction<>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                            return Lists.of(
                                    ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                    ExpressionFunctionParameterName.CHARACTER.variable(Character.class),
                                    ExpressionFunctionParameterName.DATE.required(LocalDate.class)
                            );
                        }
                    };
                }

                @Override
                public FakeExpressionEvaluationContext createContext() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Class<FakeExpressionFunction<Void, FakeExpressionEvaluationContext>> type() {
                    throw new UnsupportedOperationException();
                }
            }.testParametersOnlyLastMayBeVariable();
        } catch (final AssertionError expected) {
            fail = true;
        }

        this.checkEquals(true, fail);
    }

    private ExpressionEvaluationContext createContext() {
        return CONTEXT;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<ExpressionFunctionTesting<?, ?, ?>> type() {
        return Cast.to(ExpressionFunctionTesting.class);
    }
}
