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

    @Override
    public Class<ExpressionFunction> type() {
        return ExpressionFunction.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
