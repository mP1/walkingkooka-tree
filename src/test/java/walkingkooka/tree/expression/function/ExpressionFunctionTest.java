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
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionTest implements ClassTesting<ExpressionFunction<?, ?>> {

    private final static ExpressionFunctionParameter<Integer> REQUIRED = ExpressionFunctionParameterName.with("required")
        .required(Integer.class);

    private final static ExpressionFunctionParameter<Boolean> OPTIONAL = ExpressionFunctionParameterName.with("optional")
        .optional(Boolean.class);

    private final static ExpressionFunctionParameter<String> VARIABLE = ExpressionFunctionParameterName.with("variable")
        .variable(String.class);

    // checkParameterCount..............................................................................................

    @Test
    public void testCheckParametersCountWithRequired() {
        this.checkParameterCount(
            1,
            REQUIRED
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredRequired() {
        this.checkParameterCount(
            2,
            REQUIRED, REQUIRED
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredRequiredRequired() {
        this.checkParameterCount(
            3,
            REQUIRED, REQUIRED, REQUIRED);
    }

    @Test
    public void testCheckParametersCountWithRequiredRequiredExtraFails() {
        this.checkParametersCountFails(
            2,
            REQUIRED
        );
    }

    @Test
    public void testCheckParametersCountWithOptional() {
        this.checkParameterCount(
            1,
            OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithOptionalMissing() {
        this.checkParameterCount(
            0,
            OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithOptionalOptionalMissingMissing() {
        this.checkParameterCount(
            0,
            OPTIONAL, OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithOptionalOptionalPresentMissing() {
        this.checkParameterCount(
            1,
            OPTIONAL, OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithOptionalOptionalPresentPresent() {
        this.checkParameterCount(
            2,
            OPTIONAL, OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithOptionalOptionalPresentPresentExtraFails() {
        this.checkParametersCountFails(
            3,
            OPTIONAL, OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithVariableNone() {
        this.checkParameterCount(
            0,
            VARIABLE
        );
    }

    @Test
    public void testCheckParametersCountWithVariable() {
        this.checkParameterCount(
            1,
            VARIABLE
        );
    }

    @Test
    public void testCheckParametersCountWithVariable2() {
        this.checkParameterCount(
            2,
            VARIABLE
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredVariable0Fails() {
        this.checkParametersCountFails(
            0,
            REQUIRED, VARIABLE
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredVariable1() {
        this.checkParameterCount(
            1,
            REQUIRED, VARIABLE
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredVariable2() {
        this.checkParameterCount(
            2,
            REQUIRED, VARIABLE
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredVariable3() {
        this.checkParameterCount(
            3,
            REQUIRED, VARIABLE
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredOptional0Fails() {
        this.checkParametersCountFails(
            0,
            REQUIRED, OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredOptional1() {
        this.checkParameterCount(
            1,
            REQUIRED, OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredOptional2() {
        this.checkParameterCount(
            2,
            REQUIRED, OPTIONAL
        );
    }

    @Test
    public void testCheckParametersCountWithRequiredOptional3Fails() {
        this.checkParametersCountFails(
            3,
            REQUIRED, OPTIONAL
        );
    }

    private void checkParameterCount(final int count,
                                     final ExpressionFunctionParameter<?>... parameters) {
        new FakeExpressionFunction<Void, FakeExpressionEvaluationContext>() {
            @Override
            public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                return Lists.of(parameters);
            }

            @Override
            public Optional<ExpressionFunctionName> name() {
                return Optional.of(
                    ExpressionFunctionName.with("TestFunctionName")
                );
            }
        }.checkParameterCount(
            Collections.nCopies(
                count,
                null
            )
        );
    }


    private void checkParametersCountFails(final int count,
                                           final ExpressionFunctionParameter<?>... parameters) {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.checkParameterCount(
                count,
                parameters
            )
        );
    }

    // parameters.......................................................................................................

    @Test
    public void testParametersNonVariable() {
        this.parametersGetAndCheck(
            Lists.of(REQUIRED),
            0,
            REQUIRED
        );
    }

    @Test
    public void testParametersNonVariable2() {
        this.parametersGetAndCheck(
            Lists.of(REQUIRED, OPTIONAL),
            1,
            OPTIONAL
        );
    }

    @Test
    public void testParametersVariable() {
        this.parametersGetAndCheck(
            Lists.of(VARIABLE),
            0,
            VARIABLE
        );
    }

    @Test
    public void testParametersVariable2() {
        this.parametersGetAndCheck(
            Lists.of(REQUIRED, OPTIONAL, VARIABLE),
            2,
            VARIABLE
        );
    }

    @Test
    public void testParametersVariable3() {
        this.parametersGetAndCheck(
            Lists.of(REQUIRED, OPTIONAL, VARIABLE),
            2,
            VARIABLE
        );
    }

    private void parametersGetAndCheck(final List<ExpressionFunctionParameter<?>> parameters,
                                       final int index,
                                       final ExpressionFunctionParameter<?> parameter) {
        this.parametersGetAndCheck(
            new FakeExpressionFunction<Void, ExpressionEvaluationContext>() {
                @Override
                public List<ExpressionFunctionParameter<?>> parameters(final int count) {
                    return parameters;
                }
            },
            index,
            parameter
        );
    }

    private void parametersGetAndCheck(final FakeExpressionFunction<?, ?> function,
                                       final int index,
                                       final ExpressionFunctionParameter<?> parameter) {
        this.checkEquals(
            parameter,
            function.parameters(0).get(index),
            () -> "function.parameters().get(" + index + ")"
        );
    }

    // setKinds........................................................................................................

    @Test
    public void testSetKindsWithNullParametersFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunction.setKinds(
                null,
                ExpressionFunctionParameter.NO_KINDS
            )
        );
    }

    @Test
    public void testSetKindsWithNullKindsFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunction.setKinds(
                ExpressionFunctionParameter.EMPTY,
                null
            )
        );
    }

    @Test
    public void testSetKindsWithNoParameters() {
        this.setKindsAndCheck(
            ExpressionFunctionParameter.EMPTY,
            ExpressionFunctionParameterKind.CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES
        );
    }

    @Test
    public void testSetKindsWithSameKinds() {
        this.setKindsAndCheck(
            Lists.of(
                ExpressionFunctionParameter.NUMBER.setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES)
            ),
            ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES
        );
    }

    @Test
    public void testSetKindsWithSameKinds2() {
        this.setKindsAndCheck(
            Lists.of(
                ExpressionFunctionParameter.NUMBER.setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES)
            ),
            ExpressionFunctionParameterKind.CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES
        );
    }

    @Test
    public void testSetKindsWithDifferent() {
        this.setKindsAndCheck(
            Lists.of(
                ExpressionFunctionParameter.NUMBER.setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES)
            ),
            ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES,
            Lists.of(
                ExpressionFunctionParameter.NUMBER.setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES)
            )
        );
    }

    @Test
    public void testSetKindsWithDifferent2() {
        this.setKindsAndCheck(
            Lists.of(
                ExpressionFunctionParameter.NUMBER.setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES),
                ExpressionFunctionParameter.BOOLEAN.setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES)
            ),
            ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES,
            Lists.of(
                ExpressionFunctionParameter.NUMBER.setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES),
                ExpressionFunctionParameter.BOOLEAN.setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES)
            )
        );
    }

    private void setKindsAndCheck(final List<ExpressionFunctionParameter<?>> parameters,
                                  final Set<ExpressionFunctionParameterKind> kinds) {
        this.setKindsAndCheck(
            parameters,
            kinds,
            parameters
        );
    }

    private void setKindsAndCheck(final List<ExpressionFunctionParameter<?>> parameters,
                                  final Set<ExpressionFunctionParameterKind> kinds,
                                  final List<ExpressionFunctionParameter<?>> expected) {
        this.checkEquals(
            expected,
            ExpressionFunction.setKinds(parameters, kinds),
            () -> parameters + " setKinds " + kinds
        );
    }

    // class............................................................................................................

    @Override
    public Class<ExpressionFunction<?, ?>> type() {
        return Cast.to(ExpressionFunction.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
