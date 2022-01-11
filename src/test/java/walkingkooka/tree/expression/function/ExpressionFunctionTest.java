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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionTest implements ClassTesting<ExpressionFunction> {

    private final static ExpressionFunctionParameter<Integer> REQUIRED = ExpressionFunctionParameterName.with("required")
            .required(Integer.class);

    private final static ExpressionFunctionParameter<Boolean> OPTIONAL = ExpressionFunctionParameterName.with("optional")
            .optional(Boolean.class);

    private final static ExpressionFunctionParameter<String> VARIABLE = ExpressionFunctionParameterName.with("variable")
            .variable(String.class);

    // checkParameterCount..............................................................................................

    @Test
    public void testCheckParametersRequired() {
        this.checkParameters(1, REQUIRED);
    }

    @Test
    public void testCheckParametersRequiredRequired() {
        this.checkParameters(2, REQUIRED, REQUIRED);
    }

    @Test
    public void testCheckParametersRequiredRequiredRequired() {
        this.checkParameters(3, REQUIRED, REQUIRED, REQUIRED);
    }

    @Test
    public void testCheckParametersRequiredRequiredExtraFails() {
        this.checkParametersFails(2, REQUIRED);
    }

    @Test
    public void testCheckParametersOptional() {
        this.checkParameters(1, OPTIONAL);
    }

    @Test
    public void testCheckParametersOptionalMissing() {
        this.checkParameters(0, OPTIONAL);
    }

    @Test
    public void testCheckParametersOptionalOptionalMissingMissing() {
        this.checkParameters(0, OPTIONAL, OPTIONAL);
    }

    @Test
    public void testCheckParametersOptionalOptionalPresentMissing() {
        this.checkParameters(1, OPTIONAL, OPTIONAL);
    }

    @Test
    public void testCheckParametersOptionalOptionalPresentPresent() {
        this.checkParameters(2, OPTIONAL, OPTIONAL);
    }

    @Test
    public void testCheckParametersOptionalOptionalPresentPresentExtraFails() {
        this.checkParametersFails(3, OPTIONAL, OPTIONAL);
    }

    @Test
    public void testCheckParametersVariableNone() {
        this.checkParameters(0, VARIABLE);
    }

    @Test
    public void testCheckParametersVariable() {
        this.checkParameters(1, VARIABLE);
    }

    @Test
    public void testCheckParametersVariable2() {
        this.checkParameters(2, VARIABLE);
    }

    @Test
    public void testCheckParametersRequiredVariable0Fails() {
        this.checkParametersFails(0, REQUIRED, VARIABLE);
    }

    @Test
    public void testCheckParametersRequiredVariable1() {
        this.checkParameters(1, REQUIRED, VARIABLE);
    }

    @Test
    public void testCheckParametersRequiredVariable2() {
        this.checkParameters(2, REQUIRED, VARIABLE);
    }

    @Test
    public void testCheckParametersRequiredVariable3() {
        this.checkParameters(3, REQUIRED, VARIABLE);
    }

    @Test
    public void testCheckParametersRequiredOptional0Fails() {
        this.checkParametersFails(0, REQUIRED, OPTIONAL);
    }

    @Test
    public void testCheckParametersRequiredOptional1() {
        this.checkParameters(1, REQUIRED, OPTIONAL);
    }

    @Test
    public void testCheckParametersRequiredOptional2() {
        this.checkParameters(2, REQUIRED, OPTIONAL);
    }

    @Test
    public void testCheckParametersRequiredOptional3Fails() {
        this.checkParametersFails(3, REQUIRED, OPTIONAL);
    }

    private void checkParameters(final int count,
                                 final ExpressionFunctionParameter<?>... parameters) {
        new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
            @Override
            public List<ExpressionFunctionParameter<?>> parameters() {
                return Lists.of(parameters);
            }
        }.checkParameterCount(Collections.nCopies(count, null));
    }


    private void checkParametersFails(final int count,
                                      final ExpressionFunctionParameter<?>... parameters) {
        boolean failed = false;

        try {
            this.checkParameters(count, parameters);
        } catch (final IllegalArgumentException expected) {
            failed = true;
        }
        this.checkEquals(true, failed);
    }

    // convertParameters ...............................................................................................

    private final static ExpressionFunctionParameter<Integer> INTEGER_REQUIRED = ExpressionFunctionParameterName.with("integer").required(Integer.class);

    private static final ExpressionFunctionParameter<Integer> INTEGER_VARIABLE = INTEGER_REQUIRED.name()
            .variable(Integer.class);

    private final static ExpressionFunctionParameter<Boolean> BOOLEAN = ExpressionFunctionParameterName.with("boolean").required(Boolean.class);

    @Test
    public void testConvertParametersCorrectParameterCount() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER_REQUIRED),
                Lists.of("123"),
                Lists.of(123)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCount2() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER_REQUIRED, BOOLEAN),
                Lists.of("123", "true"),
                Lists.of(123, true)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCountLastParameterVariable() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER_VARIABLE),
                Lists.of("123", "456"),
                Lists.of(123, 456)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCountLastParameterVariable2() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER_VARIABLE),
                Lists.of("123", "456", "678"),
                Lists.of(123, 456, 678)
        );
    }

    @Test
    public void testConvertParametersCorrectParameterCountLastParameterVariable3() {
        this.convertParametersAndCheck(
                Lists.of(BOOLEAN, INTEGER_VARIABLE),
                Lists.of("true", "456", "678"),
                Lists.of(true, 456, 678)
        );
    }

    @Test
    public void testConvertParametersMissingParameterInfos() {
        this.convertParametersAndCheck(
                Lists.empty(),
                Lists.of("abc"),
                Lists.of("abc")
        );
    }

    @Test
    public void testConvertParametersMissingParameterInfos2() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER_REQUIRED),
                Lists.of("123", "abc"),
                Lists.of(123, "abc")
        );
    }

    @Test
    public void testConvertParametersMissingParameterInfos3() {
        this.convertParametersAndCheck(
                Lists.of(INTEGER_REQUIRED, BOOLEAN),
                Lists.of("123", "true", "abc"),
                Lists.of(123, true, "abc")
        );
    }

    @Test
    public void testConvertParametersNullParameterInfoFails() {
        this.convertParametersAndFail(
                Lists.of(new ExpressionFunctionParameter[]{null}),
                Lists.of("123"),
                NullPointerException.class
        );
    }

    @Test
    public void testConvertParametersNullParameterInfoFails2() {
        this.convertParametersAndFail(
                Lists.of(INTEGER_REQUIRED, null),
                Lists.of("123", "456"),
                NullPointerException.class
        );
    }

    @Test
    public void testConvertParametersConvertFails2() {
        this.convertParametersAndFail(
                Lists.of(INTEGER_REQUIRED, null),
                Lists.of("xyz"),
                NumberFormatException.class
        );
    }

    private void convertParametersAndCheck(final List<ExpressionFunctionParameter<?>> parameterInfos,
                                           final List<Object> beforeValues,
                                           final List<Object> afterValues) {
        this.checkEquals(
                afterValues,
                this.convertParameters(parameterInfos, beforeValues),
                () -> "convertParameters " + beforeValues + " " + parameterInfos
        );
    }

    private void convertParametersAndFail(final List<ExpressionFunctionParameter<?>> parameterInfos,
                                          final List<Object> beforeValues,
                                          final Class<? extends Throwable> thrown) {
        assertThrows(
                thrown,
                () -> {
                    this.convertParameters(parameterInfos, beforeValues);
                }
        );
    }

    private List<Object> convertParameters(final List<ExpressionFunctionParameter<?>> parameterInfos,
                                           final List<Object> beforeValues) {
        return
                new FakeExpressionFunction<Void, FakeExpressionFunctionContext>() {
                    @Override
                    public List<ExpressionFunctionParameter<?>> parameters() {
                        return parameterInfos;
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
