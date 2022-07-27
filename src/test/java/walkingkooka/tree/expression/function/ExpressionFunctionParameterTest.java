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
import walkingkooka.reflect.FieldAttributes;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.lang.reflect.Field;
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

    private final static Set<ExpressionFunctionParameterKind> KINDS = ExpressionFunctionParameter.NO_KINDS;

    @Test
    public void testWithNullNameFails() {
        this.withNullFails(
                null,
                TYPE,
                TYPE_PARAMETERS,
                CARDINALITY,
                KINDS
        );
    }

    @Test
    public void testWithNullTypeFails() {
        this.withNullFails(
                NAME,
                null,
                TYPE_PARAMETERS,
                CARDINALITY,
                KINDS
        );
    }

    @Test
    public void testWithNullCardinalityFails() {
        this.withNullFails(
                NAME,
                TYPE,
                TYPE_PARAMETERS,
                null,
                KINDS
        );
    }

    private void withNullFails(final ExpressionFunctionParameterName name,
                               final Class<String> type,
                               final List<Class<?>> typeParameters,
                               final ExpressionFunctionParameterCardinality cardinality,
                               final Set<ExpressionFunctionParameterKind> kinds) {
        assertThrows(
                NullPointerException.class,
                () -> ExpressionFunctionParameter.with(
                        name,
                        type,
                        typeParameters,
                        cardinality,
                        kinds
                )
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

        this.checkName(different);
        this.checkType(different);
        this.checkTypeParameters(different);
        this.checkCardinality(different, cardinality);
        this.checkKinds(different);

        this.checkName(parameter);
        this.checkType(parameter);
        this.checkTypeParameters(parameter);
        this.checkCardinality(parameter);
        this.checkKinds(parameter);
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
                CARDINALITY,
                KINDS
        );
        assertSame(parameter, parameter.setTypeParameters(parameter.typeParameters()));
    }

    @Test
    public void testSetTypeParameterDifferent() {
        final Class<List> type = List.class;

        final ExpressionFunctionParameter<List<String>> parameter = ExpressionFunctionParameter.with(
                NAME,
                Cast.to(type),
                ExpressionFunctionParameter.NO_TYPE_PARAMETERS,
                CARDINALITY,
                KINDS
        );

        final List<Class<?>> typeParameters = Lists.of(String.class);
        final ExpressionFunctionParameter<List<String>> different = parameter.setTypeParameters(typeParameters);
        assertNotSame(parameter, different);

        this.checkName(different);
        this.checkType(different, type);
        this.checkTypeParameters(different, typeParameters);
        this.checkCardinality(different);
        this.checkKinds(different);

        this.checkName(parameter);
        this.checkType(parameter, type);
        this.checkTypeParameters(parameter);
        this.checkCardinality(parameter);
        this.checkKinds(parameter);
    }

    // setKinds................................................................................................

    @Test
    public void testSetKindsNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createObject().setKinds(null)
        );
    }

    @Test
    public void testSetKindsSame() {
        final ExpressionFunctionParameter<String> parameter = this.createObject();
        assertSame(parameter, parameter.setKinds(parameter.kinds()));
    }

    @Test
    public void testSetKindSame2() {
        final Set<ExpressionFunctionParameterKind> kinds = Sets.of(
                ExpressionFunctionParameterKind.CONVERT,
                ExpressionFunctionParameterKind.EVALUATE
        );

        final ExpressionFunctionParameter<List<String>> parameter = ExpressionFunctionParameter.with(
                NAME,
                Cast.to(List.class),
                TYPE_PARAMETERS,
                CARDINALITY,
                kinds
        );
        assertSame(parameter, parameter.setKinds(kinds));
    }

    @Test
    public void testSetKindsDifferent() {
        final Class<List> type = List.class;

        final ExpressionFunctionParameter<List<String>> parameter = ExpressionFunctionParameter.with(
                NAME,
                Cast.to(type),
                ExpressionFunctionParameter.NO_TYPE_PARAMETERS,
                CARDINALITY,
                KINDS
        );

        final Set<ExpressionFunctionParameterKind> kinds = Sets.of(
                ExpressionFunctionParameterKind.CONVERT,
                ExpressionFunctionParameterKind.EVALUATE
        );
        final ExpressionFunctionParameter<List<String>> different = parameter.setKinds(kinds);
        assertNotSame(parameter, different);

        this.checkName(different);
        this.checkType(different, type);
        this.checkTypeParameters(different);
        this.checkCardinality(different);
        this.checkKinds(different, kinds);

        this.checkName(parameter);
        this.checkType(parameter, type);
        this.checkTypeParameters(parameter);
        this.checkCardinality(parameter);
        this.checkKinds(parameter);
    }

    // get.............................................................................................................

    @Test
    public void testGetMissing() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                TYPE_PARAMETERS,
                ExpressionFunctionParameterCardinality.OPTIONAL,
                KINDS
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
                ExpressionFunctionParameterCardinality.OPTIONAL,
                KINDS
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
                ExpressionFunctionParameterCardinality.OPTIONAL,
                KINDS
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
                ExpressionFunctionParameterCardinality.REQUIRED,
                KINDS
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
                ExpressionFunctionParameterCardinality.REQUIRED,
                KINDS
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
                ExpressionFunctionParameterCardinality.REQUIRED,
                KINDS
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
                ExpressionFunctionParameterCardinality.VARIABLE,
                KINDS
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
                ExpressionFunctionParameterCardinality.VARIABLE,
                KINDS
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
                ExpressionFunctionParameterCardinality.VARIABLE,
                KINDS
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
                            CARDINALITY,
                            KINDS
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
                CARDINALITY,
                KINDS
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
                CARDINALITY,
                KINDS
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
                        CARDINALITY,
                        KINDS
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
                        CARDINALITY,
                        KINDS
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
                        ExpressionFunctionParameterCardinality.OPTIONAL,
                        KINDS
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
                        ExpressionFunctionParameterCardinality.REQUIRED,
                        KINDS
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
                        ExpressionFunctionParameterCardinality.OPTIONAL,
                        KINDS
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
                        ExpressionFunctionParameterCardinality.VARIABLE,
                        KINDS
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
                        ExpressionFunctionParameterCardinality.REQUIRED,
                        KINDS
                ),
                "java.util.List<java.lang.String> name"
        );
    }

    @Test
    public void testToStringWithKinds() {
        this.toStringAndCheck(
                ExpressionFunctionParameter.with(
                        NAME,
                        List.class,
                        List.of(String.class),
                        ExpressionFunctionParameterCardinality.REQUIRED,
                        EnumSet.of(
                                ExpressionFunctionParameterKind.CONVERT,
                                ExpressionFunctionParameterKind.EVALUATE
                        )
                ),
                "@CONVERT, @EVALUATE java.util.List<java.lang.String> name"
        );
    }

    // Constants........................................................................................................

    @Test
    public void testConstants() throws Exception {
        int i = 0;

        for (final Field field : ExpressionFunctionParameter.class.getFields()) {
            if (JavaVisibility.of(field) != JavaVisibility.PUBLIC) {
                continue;
            }
            if (!FieldAttributes.STATIC.is(field)) {
                continue;
            }
            if (ExpressionFunctionParameter.class != field.getType()) {
                continue;
            }

            final ExpressionFunctionParameter constant = (ExpressionFunctionParameter) field.get(null);
            this.checkNotEquals(null, constant.name(), "name");
            this.checkNotEquals(null, constant.typeParameters(), " typeParameters");
            this.checkNotEquals(null, constant.kinds(), "kinds");
            this.checkNotEquals(null, constant.type(), "type");
            this.checkNotEquals(null, constant.cardinality(), "cardinality");

            i++;
        }

        this.checkNotEquals(0, i, "constant count");
    }

    private void checkName(final ExpressionFunctionParameter<?> parameter) {
        this.checkName(
                parameter,
                NAME
        );
    }

    private void checkName(final ExpressionFunctionParameter<?> parameter,
                           final ExpressionFunctionParameterName name) {
        this.checkEquals(
                parameter.name(),
                name,
                "name"
        );
    }

    private void checkType(final ExpressionFunctionParameter<?> parameter) {
        this.checkType(
                parameter,
                TYPE
        );
    }

    private void checkType(final ExpressionFunctionParameter<?> parameter,
                           final Class<?> type) {
        this.checkEquals(
                parameter.type(),
                type,
                "type"
        );
    }

    private void checkTypeParameters(final ExpressionFunctionParameter<?> parameter) {
        this.checkTypeParameters(
                parameter,
                TYPE_PARAMETERS
        );
    }

    private void checkTypeParameters(final ExpressionFunctionParameter<?> parameter,
                                     final List<Class<?>> typeParameters) {
        this.checkEquals(
                parameter.typeParameters(),
                typeParameters,
                "typeParameters"
        );
    }

    private void checkCardinality(final ExpressionFunctionParameter<?> parameter) {
        this.checkCardinality(
                parameter,
                CARDINALITY
        );
    }

    private void checkCardinality(final ExpressionFunctionParameter<?> parameter,
                                  final ExpressionFunctionParameterCardinality cardinality) {
        this.checkEquals(
                parameter.cardinality(),
                cardinality,
                "cardinality"
        );
    }

    private void checkKinds(final ExpressionFunctionParameter<?> parameter) {
        this.checkKinds(
                parameter,
                KINDS
        );
    }

    private void checkKinds(final ExpressionFunctionParameter<?> parameter,
                            final Set<ExpressionFunctionParameterKind> kinds) {
        this.checkEquals(
                parameter.kinds(),
                kinds,
                "kinds"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public ExpressionFunctionParameter<String> createObject() {
        return ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                TYPE_PARAMETERS,
                CARDINALITY,
                KINDS
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
