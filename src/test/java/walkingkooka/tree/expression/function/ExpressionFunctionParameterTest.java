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
import walkingkooka.tree.expression.ExpressionReference;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionFunctionParameterTest implements HashCodeEqualsDefinedTesting2<ExpressionFunctionParameter<String>>,
        ConstantsTesting<ExpressionFunctionParameter<String>>,
        ToStringTesting<ExpressionFunctionParameter<String>> {

    private final static ExpressionFunctionParameterName NAME = ExpressionFunctionParameterName.with("name");

    private final static Class<String> TYPE = String.class;

    private final static ExpressionFunctionParameterCardinality CARDINALITY = ExpressionFunctionParameterCardinality.REQUIRED;

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionParameter.with(null, TYPE, CARDINALITY));
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionParameter.with(NAME, null, CARDINALITY));
    }

    @Test
    public void testWithNullCardinalityFails() {
        assertThrows(NullPointerException.class, () -> ExpressionFunctionParameter.with(NAME, TYPE, null));
    }

    // get.............................................................................................................

    @Test
    public void testGetMissing() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
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
                            public boolean resolveReferences() {
                                return resolveReferences;
                            }

                            public String toString() {
                                return "resolveReferences: " + this.resolveReferences();
                            }
                        },
                        new FakeExpressionFunctionContext() {
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
    public void testConvert() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(NAME, Integer.class, CARDINALITY);
        this.checkEquals(
                Either.left(12),
                parameter.convert(
                        "12",
                        this.expressionFunctionContext()
                )
        );
    }

    @Test
    public void testConvertOrFail() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(NAME, Integer.class, CARDINALITY);
        this.checkEquals(
                12,
                parameter.convertOrFail(
                        "12",
                        this.expressionFunctionContext()
                )
        );
    }

    private ExpressionFunctionContext expressionFunctionContext() {
        return new FakeExpressionFunctionContext() {
            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                checkEquals("12", value, "value");
                checkEquals(Integer.class, target, "target");
                return Cast.to(Either.left(12));
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
                        ExpressionFunctionParameterCardinality.OPTIONAL
                ),
                "java.lang.String name?"
        );
    }

    @Test
    public void testToStringVariabled() {
        this.toStringAndCheck(
                ExpressionFunctionParameter.with(
                        NAME,
                        TYPE,
                        ExpressionFunctionParameterCardinality.VARIABLE
                ),
                "java.lang.String name*"
        );
    }

    @Override
    public ExpressionFunctionParameter<String> createObject() {
        return ExpressionFunctionParameter.with(NAME, TYPE, CARDINALITY);
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
