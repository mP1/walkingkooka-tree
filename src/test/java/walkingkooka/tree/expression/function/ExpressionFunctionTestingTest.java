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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public final class ExpressionFunctionTestingTest implements ClassTesting<ExpressionFunctionTesting<?, ?, ?>> {

    private final ExpressionFunctionTesting<ExpressionFunction<Object, ExpressionFunctionContext>, Object, ExpressionFunctionContext> TESTING = new ExpressionFunctionTesting<>() {

        @Override
        public Class<ExpressionFunction<Object, ExpressionFunctionContext>> type() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExpressionFunction<Object, ExpressionFunctionContext> createBiFunction() {
            return stringConcatParameters();
        }

        @Override
        public ExpressionFunctionContext createContext() {
            return ExpressionFunctionTestingTest.this.createContext();
        }
    };

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

    private FakeExpressionFunction<Object, ExpressionFunctionContext> stringConcatParameters() {
        return new FakeExpressionFunction<>() {
            @Override
            public String apply(final List<Object> objects,
                                final ExpressionFunctionContext context) {
                return objects.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining());
            }
        };
    }

    // testParameterNamesUnique.........................................................................................

    @Test
    public void testTestParameterNamesUnique() {
        new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionFunctionContext>, Void, FakeExpressionFunctionContext>() {

            @Override
            public FakeExpressionFunction<Void, FakeExpressionFunctionContext> createBiFunction() {
                return new FakeExpressionFunction<>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters() {
                        return Lists.of(
                                ExpressionFunctionParameter.BOOLEAN,
                                ExpressionFunctionParameter.CHARACTER,
                                ExpressionFunctionParameter.DATE
                        );
                    }
                };
            }

            @Override
            public FakeExpressionFunctionContext createContext() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<FakeExpressionFunction<Void, FakeExpressionFunctionContext>> type() {
                throw new UnsupportedOperationException();
            }
        }.testParameterNamesUnique();
    }

    @Test
    public void testTestParameterNamesUniqueDuplicatesFail() {
        boolean fail = false;

        try {
            new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionFunctionContext>, Void, FakeExpressionFunctionContext>() {

                @Override
                public FakeExpressionFunction<Void, FakeExpressionFunctionContext> createBiFunction() {
                    return new FakeExpressionFunction<>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters() {
                            return Lists.of(
                                    ExpressionFunctionParameter.BOOLEAN,
                                    ExpressionFunctionParameter.CHARACTER,
                                    ExpressionFunctionParameter.BOOLEAN
                            );
                        }
                    };
                }

                @Override
                public FakeExpressionFunctionContext createContext() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Class<FakeExpressionFunction<Void, FakeExpressionFunctionContext>> type() {
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
        new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionFunctionContext>, Void, FakeExpressionFunctionContext>() {

            @Override
            public FakeExpressionFunction<Void, FakeExpressionFunctionContext> createBiFunction() {
                return new FakeExpressionFunction<>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters() {
                        return Lists.of(
                                ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                ExpressionFunctionParameterName.CHARACTER.optional(Character.class)
                        );
                    }
                };
            }

            @Override
            public FakeExpressionFunctionContext createContext() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<FakeExpressionFunction<Void, FakeExpressionFunctionContext>> type() {
                throw new UnsupportedOperationException();
            }
        }.testParametersOptionalNotBeforeRequired();
    }

    @Test
    public void testParametersOptionalNotBeforeRequiredFails() {
        boolean fail = false;

        try {
            new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionFunctionContext>, Void, FakeExpressionFunctionContext>() {

                @Override
                public FakeExpressionFunction<Void, FakeExpressionFunctionContext> createBiFunction() {
                    return new FakeExpressionFunction<>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters() {
                            return Lists.of(
                                    ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                    ExpressionFunctionParameterName.CHARACTER.optional(Character.class),
                                    ExpressionFunctionParameterName.DATE.required(LocalDate.class)
                            );
                        }
                    };
                }

                @Override
                public FakeExpressionFunctionContext createContext() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Class<FakeExpressionFunction<Void, FakeExpressionFunctionContext>> type() {
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
        new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionFunctionContext>, Void, FakeExpressionFunctionContext>() {

            @Override
            public FakeExpressionFunction<Void, FakeExpressionFunctionContext> createBiFunction() {
                return new FakeExpressionFunction<>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters() {
                        return Lists.of(
                                ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                ExpressionFunctionParameterName.CHARACTER.variable(Character.class)
                        );
                    }
                };
            }

            @Override
            public FakeExpressionFunctionContext createContext() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Class<FakeExpressionFunction<Void, FakeExpressionFunctionContext>> type() {
                throw new UnsupportedOperationException();
            }
        }.testParametersOnlyLastMayBeVariable();
    }

    @Test
    public void testParametersOnlyLastMayBeVariableFails() {
        boolean fail = false;

        try {
            new ExpressionFunctionTesting<FakeExpressionFunction<Void, FakeExpressionFunctionContext>, Void, FakeExpressionFunctionContext>() {

                @Override
                public FakeExpressionFunction<Void, FakeExpressionFunctionContext> createBiFunction() {
                    return new FakeExpressionFunction<>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters() {
                            return Lists.of(
                                    ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class),
                                    ExpressionFunctionParameterName.CHARACTER.variable(Character.class),
                                    ExpressionFunctionParameterName.DATE.required(LocalDate.class)
                            );
                        }
                    };
                }

                @Override
                public FakeExpressionFunctionContext createContext() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public Class<FakeExpressionFunction<Void, FakeExpressionFunctionContext>> type() {
                    throw new UnsupportedOperationException();
                }
            }.testParametersOnlyLastMayBeVariable();
        } catch (final AssertionError expected) {
            fail = true;
        }

        this.checkEquals(true, fail);
    }

    private ExpressionFunctionContext createContext() {
        return ExpressionFunctionContexts.fake();
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
