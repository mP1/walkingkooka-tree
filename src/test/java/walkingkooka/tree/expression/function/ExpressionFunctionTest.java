/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionTest implements ClassTesting<ExpressionFunction> {


    // checkOnlyRequiredParameters.....................................................................................

    @Test
    public void testCheckOnlyRequiredParametersLess() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters() {
                            return ExpressionFunctionParameter.list(
                                    ExpressionFunctionParameterName.with("first").setType(Integer.class),
                                    ExpressionFunctionParameterName.with("second").setType(Integer.class)
                            );
                        }
                    }.checkOnlyRequiredParameters(Lists.of(1));
                }
        );
    }

    @Test
    public void testCheckOnlyRequiredParametersSame() {
        new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
            @Override
            public List<ExpressionFunctionParameter<?>> parameters() {
                return ExpressionFunctionParameter.list(
                        ExpressionFunctionParameterName.with("first").setType(Integer.class),
                        ExpressionFunctionParameterName.with("second").setType(Integer.class)
                );
            }
        }.checkOnlyRequiredParameters(Lists.of(1, 2));
    }

    @Test
    public void testCheckOnlyRequiredParametersMoreFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters() {
                            return ExpressionFunctionParameter.list(
                                    ExpressionFunctionParameterName.with("first").setType(Integer.class)
                            );
                        }
                    }.checkOnlyRequiredParameters(Lists.of(1, 2));
                });
    }

    // checkWithoutExtraParameters.....................................................................................

    @Test
    public void testCheckWithoutExtraParametersLess() {
        new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
            @Override
            public List<ExpressionFunctionParameter<?>> parameters() {
                return ExpressionFunctionParameter.list(
                        ExpressionFunctionParameterName.with("first").setType(Integer.class),
                        ExpressionFunctionParameterName.with("second").setType(Integer.class)
                );
            }
        }.checkWithoutExtraParameters(Lists.of(1));
    }

    @Test
    public void testCheckWithoutExtraParametersSame() {
        new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
            @Override
            public List<ExpressionFunctionParameter<?>> parameters() {
                return ExpressionFunctionParameter.list(
                        ExpressionFunctionParameterName.with("first").setType(Integer.class),
                        ExpressionFunctionParameterName.with("second").setType(Integer.class)
                );
            }
        }.checkWithoutExtraParameters(Lists.of(1, 2));
    }

    @Test
    public void testCheckWithoutExtraParametersMoreFails() {
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
                        @Override
                        public List<ExpressionFunctionParameter<?>> parameters() {
                            return ExpressionFunctionParameter.list(
                                    ExpressionFunctionParameterName.with("first").setType(Integer.class)
                            );
                        }
                    }.checkWithoutExtraParameters(Lists.of(1, 2));
                });
    }

    // convertParameters ...............................................................................................

    private final static ExpressionFunctionParameter<Integer> INTEGER = ExpressionFunctionParameterName.with("integer").setType(Integer.class);

    private final static ExpressionFunctionParameter<Boolean> BOOLEAN = ExpressionFunctionParameterName.with("boolean").setType(Boolean.class);

    @Test
    public void testConvertParametersCorrectParameterCount() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER),
                false,
                Lists.of("123"),
                Lists.of(123)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCount2() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER, BOOLEAN),
                false,
                Lists.of("123", "true"),
                Lists.of(123, true)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCountLastParameterVariable() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER),
                true,
                Lists.of("123", "456"),
                Lists.of(123, 456)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCountLastParameterVariable2() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER),
                true,
                Lists.of("123", "456", "678"),
                Lists.of(123, 456, 678)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCountLastParameterVariable3() {
        this.convertParametersAndCheck(
                Lists.of(BOOLEAN, INTEGER),
                true,
                Lists.of("true", "456", "678"),
                Lists.of(true, 456, 678)
        );
    }

    @Test
    public void testConvertParametersMissingParameterInfos() {
        this.convertParametersAndCheck(
                Lists.empty(),
                false,
                Lists.of("abc"),
                Lists.of("abc")
        );
    }

    @Test
    public void testConvertParametersMissingParameterInfos2() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER),
                false,
                Lists.of("123", "abc"),
                Lists.of(123, "abc")
        );
    }

    @Test
    public void testConvertParametersMissingParameterInfos3() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER, BOOLEAN),
                false,
                Lists.of("123", "true", "abc"),
                Lists.of(123, true, "abc")
        );
    }

    @Test
    public void testConvertParametersNullParameterInfoFails() {
        this.convertParametersAndFail(
                Lists.of(new ExpressionFunctionParameter[]{null}),
                false,
                Lists.of("123"),
                NullPointerException.class
        );
    }

    @Test
    public void testConvertParametersNullParameterInfoFails2() {
        this.convertParametersAndFail(
                Lists.of(INTEGER, null),
                false,
                Lists.of("123", "456"),
                NullPointerException.class
        );
    }

    @Test
    public void testConvertParametersConvertFails2() {
        this.convertParametersAndFail(
                Lists.of(INTEGER, null),
                false,
                Lists.of("xyz"),
                NumberFormatException.class
        );
    }

    private void convertParametersAndCheck(final List<ExpressionFunctionParameter<?>> parameterInfos,
                                           final boolean lsLastParameterVariable,
                                           final List<Object> beforeValues,
                                           final List<Object> afterValues) {
        this.checkEquals(
                afterValues,
                this.convertParameters(parameterInfos, lsLastParameterVariable, beforeValues),
                () -> "convertParameters " + beforeValues + " " + parameterInfos
        );
    }

    private void convertParametersAndFail(final List<ExpressionFunctionParameter<?>> parameterInfos,
                                          final boolean lsLastParameterVariable,
                                          final List<Object> beforeValues,
                                          final Class<? extends Throwable> thrown) {
        assertThrows(
                thrown,
                () -> {
                    this.convertParameters(parameterInfos, lsLastParameterVariable, beforeValues);
                }
        );
    }

    private List<Object> convertParameters(final List<ExpressionFunctionParameter<?>> parameterInfos,
                                           final boolean lsLastParameterVariable,
                                           final List<Object> beforeValues) {
        return
                new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters() {
                        return parameterInfos;
                    }

                    @Override
                    public boolean lsLastParameterVariable() {
                        return lsLastParameterVariable;
                    }
                }.convertParameters(
                        beforeValues,
                        new FakeExpressionFunctionContext() {
                            @Override
                            public <T> T convertOrFail(final Object value,
                                                       final Class<T> target) {
                                if (value instanceof String && Integer.class == target) {
                                    return target.cast(Integer.parseInt((String) value));
                                }
                                if (value instanceof String && Double.class == target) {
                                    return target.cast(Double.parseDouble((String) value));
                                }
                                if (value instanceof String && Boolean.class == target) {
                                    return target.cast(Boolean.valueOf((String) value));
                                }
                                throw this.convertThrowable("Value=" + value + " target=" + target.getName());
                            }
                        }
                );
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ExpressionFunction> type() {
        return ExpressionFunction.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
