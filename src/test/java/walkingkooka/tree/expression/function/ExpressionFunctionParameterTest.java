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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionParameterTest implements HashCodeEqualsDefinedTesting2<ExpressionFunctionParameter<String>>,
        ConstantsTesting<ExpressionFunctionParameter<String>>,
        ToStringTesting<ExpressionFunctionParameter<String>> {

    private final static ExpressionFunctionParameterName NAME = ExpressionFunctionParameterName.with("name");

    private final static Class<String> TYPE = String.class;

    private final static List<Class<?>> TYPE_PARAMETERS = ExpressionFunctionParameter.NO_TYPE_PARAMETERS;

    private final static ExpressionFunctionParameterCardinality CARDINALITY = ExpressionFunctionParameterCardinality.REQUIRED;

    @Test
    public void testWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionParameter.with(null, TYPE, TYPE_PARAMETERS, CARDINALITY)
        );
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionParameter.with(NAME, null, TYPE_PARAMETERS, CARDINALITY)
        );
    }

    @Test
    public void testWithNullCardinalityFails() {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionParameter.with(NAME, TYPE, TYPE_PARAMETERS, null)
        );
    }

    // setCardinality...................................................................................................

    @Test
    public void testSetCardinalityNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setCardinality(null)
        );
    }

    @Test
    public void testSetCardinalitySame() {
        final ExpressionFunctionParameter<?> parameter = this.createObject();
        assertSame(
                parameter,
                parameter.setCardinality(parameter.cardinality())
        );
    }

    @Test
    public void testSetCardinalityDifferent() {
        final ExpressionFunctionParameter<?> parameter = this.createObject();

        final ExpressionFunctionParameterCardinality cardinality = ExpressionFunctionParameterCardinality.OPTIONAL;
        final ExpressionFunctionParameter<?> different = parameter.setCardinality(cardinality);

        this.checkNotEquals(
                parameter,
                different
        );

        this.checkEquals(
                parameter.name(),
                different.name(),
                "name"
        );
        this.checkEquals(
                parameter.type(),
                different.type(),
                "type"
        );
        this.checkEquals(
                cardinality,
                different.cardinality(),
                "cardinality"
        );
        this.checkEquals(
                parameter.typeParameters(),
                different.typeParameters(),
                "typeParameters"
        );
    }

    // setTypeParameters................................................................................................

    @Test
    public void testSetTypeParametersNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setTypeParameters(null)
        );
    }

    @Test
    public void testSetTypeParameterSame() {
        final ExpressionFunctionParameter<String> parameter = this.createObject();
        assertSame(parameter, parameter.setTypeParameters(parameter.typeParameters()));
    }

    @Test
    public void testSetTypeParameterSame2() {
        final List<Class<?>> typeParameters = Lists.of(String.class);

        final ExpressionFunctionParameter<List<String>> parameter = ExpressionFunctionParameter.with(
                NAME,
                Cast.to(List.class),
                typeParameters,
                CARDINALITY
        );
        assertSame(parameter, parameter.setTypeParameters(parameter.typeParameters()));
    }

    @Test
    public void testSetTypeParameterDifferent() {
        final ExpressionFunctionParameter<List<String>> parameter = ExpressionFunctionParameter.with(
                NAME,
                Cast.to(List.class),
                ExpressionFunctionParameter.NO_TYPE_PARAMETERS,
                CARDINALITY
        );

        final List<Class<?>> typeParameters = Lists.of(String.class);
        final ExpressionFunctionParameter<List<String>> different = parameter.setTypeParameters(typeParameters);
        assertNotSame(parameter, different);

        this.checkEquals(parameter.name(), different.name(), "name");
        this.checkEquals(parameter.type(), different.type(), "type");
        this.checkEquals(typeParameters, different.typeParameters(), "typeParameters");
        this.checkEquals(parameter.cardinality(), different.cardinality(), "cardinality");
    }

    // get.............................................................................................................

    @Test
    public void testGetMissing() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.OPTIONAL
        );
        this.checkEquals(
                Optional.empty(),
                parameter.get(
                        List.of(
                                100
                        ),
                        1
                )
        );
    }

    @Test
    @Disabled("Need to emulate Class.cast")
    public void testGetWrongTypeFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.OPTIONAL
        );
        assertThrows(
                ClassCastException.class,
                () -> {
                    parameter.get(
                            List.of(
                                    "A"
                            ),
                            0
                    );
                }
        );
    }

    @Test
    public void testGet() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.OPTIONAL
        );
        this.checkEquals(
                Optional.of(100),
                parameter.get(
                        List.of(
                                100,
                                "B"
                        ),
                        0
                )
        );
    }

    // getOrFail.......................................................................................................

    @Test
    public void testGetOrFailMissingFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.REQUIRED
        );
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> {
                    parameter.getOrFail(
                            List.of(
                                    1, 2
                            ),
                            2
                    );
                }
        );
    }

    @Test
    @Disabled("Need to emulate Class.cast")
    public void testGetOrFailWrongTypeFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.REQUIRED
        );
        assertThrows(
                ClassCastException.class,
                () -> {
                    parameter.getOrFail(
                            List.of(
                                    "A"
                            ),
                            0
                    );
                }
        );
    }

    @Test
    public void testGetOrFail() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.REQUIRED
        );
        this.checkEquals(
                100,
                parameter.getOrFail(
                        List.of(
                                100,
                                "B"
                        ),
                        0
                )
        );
    }

    // getVariable.......................................................................................................

    @Test
    public void testGetVariableNone() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.VARIABLE
        );
        this.checkEquals(
                Lists.empty(),
                parameter.getVariable(
                        List.of(
                                "Q"
                        ),
                        2
                )
        );
    }

    @Test
    public void testGetVariableOne() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.VARIABLE
        );
        this.checkEquals(
                Lists.of(
                        10
                ),
                parameter.getVariable(
                        List.of(
                                "FIRST",
                                10
                        ),
                        1
                )
        );
    }

    @Test
    public void testGetVariableMany() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.VARIABLE
        );
        this.checkEquals(
                Lists.of(
                        10, 20, 30
                ),
                parameter.getVariable(
                        List.of(
                                "FIRST",
                                10,
                                20,
                                30
                        ),
                        1
                )
        );
    }

    // getVariableAndFlatten...........................................................................................

    @Test
    public void testGetVariableAndFlattenNone() {
        this.getVariableAndFlattenAndCheck(
                Lists.empty(),
                false // resolveReferences
        );
    }

    @Test
    public void testGetVariableAndFlattenNoReferences() {
        this.getVariableAndFlattenAndCheck(
                Lists.of(
                        10,
                        20
                ),
                false // resolveReferences
        );
    }

    @Test
    public void testGetVariableAndFlattenReferenceNotResolved() {
        this.getVariableAndFlattenAndCheck(
                Lists.of(
                        10,
                        20,
                        new TestExpressionReference(30)
                ),
                false // resolveReferences
        );
    }

    @Test
    public void testGetVariableAndFlattenReference() {
        this.getVariableAndFlattenAndCheck2(
                Lists.of(
                        10,
                        20,
                        new TestExpressionReference(30)
                ),
                true, // resolveReferences
                10, 20, 30
        );
    }

    @Test
    public void testGetVariableAndFlattenReference2() {
        this.getVariableAndFlattenAndCheck2(
                Lists.of(
                        10,
                        new TestExpressionReference(
                                Lists.of(20, 30)
                        ),
                        40
                ),
                true, // resolveReferences
                10, 20, 30, 40
        );
    }

    @Test
    public void testGetVariableAndFlattenReferenceToReference() {
        this.getVariableAndFlattenAndCheck2(
                Lists.of(
                        10,
                        new TestExpressionReference(
                                Lists.of(20,
                                        new TestExpressionReference(30),
                                        40
                                )
                        )
                ),
                true, // resolveReferences
                10, 20, 30, 40
        );
    }

    @Test
    public void testGetVariableAndFlattenReferenceToReference2() {
        this.getVariableAndFlattenAndCheck2(
                Lists.of(
                        10,
                        new TestExpressionReference(
                                Lists.of(20,
                                        new TestExpressionReference(
                                                Lists.of(
                                                        new TestExpressionReference(
                                                                30
                                                        )
                                                )
                                        ),
                                        40
                                )
                        )
                ),
                true, // resolveReferences
                10, 20, 30, 40
        );
    }

    static class TestExpressionReference implements ExpressionReference {

        TestExpressionReference(final Object value) {
            this.value = value;
        }

        final Object value;
    }

    private void getVariableAndFlattenAndCheck(final List<Object> lastParameterValues,
                                               final boolean resolveReferences) {
        this.getVariableAndFlattenAndCheck3(
                lastParameterValues,
                resolveReferences,
                lastParameterValues
        );
    }

    private void getVariableAndFlattenAndCheck2(final List<Object> lastParameterValues,
                                                final boolean resolveReferences,
                                                final Object... expected) {
        this.getVariableAndFlattenAndCheck3(
                lastParameterValues,
                resolveReferences,
                Lists.of(expected)
        );
    }

    private void getVariableAndFlattenAndCheck3(final List<Object> lastParameterValues,
                                                final boolean resolveReferences,
                                                final List<Object> expected) {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.VARIABLE
        );

        final List<Object> parameters = List.of(
                "FIRST",
                "SECOND",
                lastParameterValues
        );

        this.checkEquals(
                expected,
                parameter.getVariableAndFlatten(
                        parameters,
                        2,
                        new FakeExpressionFunction<>() {

                            @Override
                            public Set<ExpressionFunctionKind> kinds() {
                                return kinds;
                            }

                            private final Set<ExpressionFunctionKind> kinds = resolveReferences ?
                                    EnumSet.of(ExpressionFunctionKind.RESOLVE_REFERENCES) :
                                    Sets.empty();

                            public String toString() {
                                return this.kinds().toString();
                            }
                        },
                        new FakeExpressionEvaluationContext() {
                            @Override
                            public Object referenceOrFail(final ExpressionReference reference) {
                                final TestExpressionReference testExpressionReference = (TestExpressionReference) reference;
                                return testExpressionReference.value;
                            }
                        }
                ),
                () -> "getVariableAndFlatten " + parameters
        );
    }

    // convert.........................................................................................................

    @Test
    public void testConvertOrFailMissingListMissingTypeParameterFails() {
        assertThrows(
                IllegalStateException.class,
                () -> {
                    ExpressionFunctionParameter.with(
                            NAME,
                            Cast.to(List.class),
                            ExpressionFunctionParameter.NO_TYPE_PARAMETERS,
                            CARDINALITY
                    ).convertOrFail(Lists.empty(), ExpressionEvaluationContexts.fake());
                }
        );
    }

    @Test
    public void testConvertOrFail() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                CARDINALITY
        );

        this.checkEquals(
                12,
                parameter.convertOrFail(
                        "12",
                        this.ExpressionEvaluationContext()
                )
        );
    }

    @Test
    public void testConvertOrFailList() {
        final ExpressionFunctionParameter<List<Integer>> parameter = ExpressionFunctionParameter.with(
                NAME,
                Cast.to(List.class),
                List.of(Integer.class),
                CARDINALITY
        );

        this.checkEquals(
                List.of(12, 34, 56),
                parameter.convertOrFail(
                        Lists.of("12", "34", "56"),
                        this.ExpressionEvaluationContext()
                )
        );
    }

    private ExpressionEvaluationContext ExpressionEvaluationContext() {
        return new FakeExpressionEvaluationContext() {
            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                checkEquals(Integer.class, target, "target");
                return Cast.to(Either.left(Integer.parseInt((String) value)));
            }
        };
    }

    @Test
    public void testList() {
        final ExpressionFunctionParameter<String> string = ExpressionFunctionParameterName.with("string").required(String.class);
        final ExpressionFunctionParameter<Integer> integer = ExpressionFunctionParameterName.with("integer").required(Integer.class);

        this.checkEquals(
                Lists.of(string, integer),
                ExpressionFunctionParameter.list(string, integer)
        );
    }

    @Test
    public void testDifferentName() {
        this.checkNotEquals(
                ExpressionFunctionParameter.with(
                        ExpressionFunctionParameterName.with("different"),
                        TYPE,
                        TYPE_PARAMETERS,
                        CARDINALITY
                )
        );
    }

    @Test
    public void testDifferentType() {
        this.checkNotEquals(
                ExpressionFunctionParameter.with(
                        NAME,
                        Integer.class,
                        TYPE_PARAMETERS,
                        CARDINALITY
                )
        );
    }

    @Test
    public void testDifferentCardinality() {
        assertNotSame(ExpressionFunctionParameterCardinality.OPTIONAL, CARDINALITY);

        this.checkNotEquals(
                ExpressionFunctionParameter.with(
                        NAME,
                        Integer.class,
                        TYPE_PARAMETERS,
                        ExpressionFunctionParameterCardinality.OPTIONAL
                )
        );
    }

    @Test
    public void testToStringRequired() {
        this.toStringAndCheck(
                ExpressionFunctionParameter.with(
                        NAME,
                        TYPE,
                        TYPE_PARAMETERS,
                        ExpressionFunctionParameterCardinality.REQUIRED
                ),
                "java.lang.String name"
        );
    }

    @Test
    public void testToStringOptional() {
        this.toStringAndCheck(
                ExpressionFunctionParameter.with(
                        NAME,
                        TYPE,
                        TYPE_PARAMETERS,
                        ExpressionFunctionParameterCardinality.OPTIONAL
                ),
                "java.lang.String name?"
        );
    }

    @Test
    public void testToStringVariable() {
        this.toStringAndCheck(
                ExpressionFunctionParameter.with(
                        NAME,
                        TYPE,
                        TYPE_PARAMETERS,
                        ExpressionFunctionParameterCardinality.VARIABLE
                ),
                "java.lang.String name*"
        );
    }

    @Test
    public void testToStringWithTypeParameters() {
        this.toStringAndCheck(
                ExpressionFunctionParameter.with(
                        NAME,
                        List.class,
                        List.of(String.class),
                        ExpressionFunctionParameterCardinality.REQUIRED
                ),
                "java.util.List<java.lang.String> name"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public ExpressionFunctionParameter<String> createObject() {
        return ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                TYPE_PARAMETERS,
                CARDINALITY
        );
    }

    @Override
    public Class<ExpressionFunctionParameter<String>> type() {
        return Cast.to(ExpressionFunctionParameter.class);
    }

    @Override
    public Set<ExpressionFunctionParameter<String>> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}
