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
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

import java.lang.reflect.Field;
import java.util.Arrays;
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

    private final static ExpressionFunctionParameterCardinality CARDINALITY = ExpressionFunctionParameterCardinality.REQUIRED;

    private final static Optional<String> DEFAULT_VALUE = Optional.of("DefaultStringValue");

    private final static Set<ExpressionFunctionParameterKind> KINDS = ExpressionFunctionParameter.NO_KINDS;

    @Test
    public void testWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionParameter.with(
                null,
                TYPE,
                CARDINALITY,
                DEFAULT_VALUE,
                KINDS
            )
        );
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionParameter.with(
                NAME,
                (Class<String>) null,
                CARDINALITY,
                DEFAULT_VALUE,
                KINDS
            )
        );
    }

    @Test
    public void testWithNullCardinalityFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                null,
                DEFAULT_VALUE,
                KINDS
            )
        );
    }

    @Test
    public void testWithNullDefaultValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                CARDINALITY,
                null,
                KINDS
            )
        );
    }

    @Test
    public void testWithNullKindsFails() {
        assertThrows(
            NullPointerException.class,
            () -> ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                CARDINALITY,
                DEFAULT_VALUE,
                null
            )
        );
    }

    // setName..........................................................................................................

    @Test
    public void testSetNameWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject().setName(null)
        );
    }

    @Test
    public void testSetNameWithSame() {
        final ExpressionFunctionParameter<?> parameter = this.createObject();
        final ExpressionFunctionParameterName name = parameter.name();
        assertSame(parameter, parameter.setName(name));
    }

    @Test
    public void testSetNameWithDifferent() {
        final ExpressionFunctionParameter<?> parameter = this.createObject();
        final ExpressionFunctionParameterName name = ExpressionFunctionParameterName.with("different123");

        final ExpressionFunctionParameter<?> different = parameter.setName(name);
        assertNotSame(parameter, different);

        this.nameAndCheck(different, name);
        this.typeAndCheck(different);
        this.cardinalityAndCheck(different);
        this.kindsAndCheck(different);

        this.nameAndCheck(parameter);
        this.typeAndCheck(parameter);
        this.cardinalityAndCheck(parameter);
        this.kindsAndCheck(parameter);
    }

    // setType..........................................................................................................

    @Test
    public void testSetTypeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject().setType(null)
        );
    }

    @Test
    public void testSetTypeWithSame() {
        final ExpressionFunctionParameter<?> parameter = this.createObject();
        assertSame(
            parameter,
            parameter.setType(TYPE)
        );
    }

    @Test
    public void testSetTypeWithDifferent() {
        final ExpressionFunctionParameter<?> parameter = this.createObject();
        final Class<ExpressionFunctionParameterTest> type = ExpressionFunctionParameterTest.class;

        final ExpressionFunctionParameter<?> different = parameter.setType(type);
        assertNotSame(parameter, different);

        this.nameAndCheck(different);
        this.typeAndCheck(different, type);
        this.cardinalityAndCheck(different);
        this.kindsAndCheck(different);

        this.nameAndCheck(parameter);
        this.typeAndCheck(parameter);
        this.cardinalityAndCheck(parameter);
        this.kindsAndCheck(parameter);
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

        this.nameAndCheck(different);
        this.typeAndCheck(different);
        this.cardinalityAndCheck(different, cardinality);
        this.kindsAndCheck(different);

        this.nameAndCheck(parameter);
        this.typeAndCheck(parameter);
        this.cardinalityAndCheck(parameter);
        this.kindsAndCheck(parameter);
    }

    // setKinds.........................................................................................................

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
            CARDINALITY,
            Optional.empty(), // defaultValue
            kinds
        );
        assertSame(
            parameter,
            parameter.setKinds(kinds)
        );
    }

    @Test
    public void testSetKindsDifferent() {
        final Class<List<?>> type = Cast.to(List.class);

        final ExpressionFunctionParameter<List<String>> parameter = ExpressionFunctionParameter.with(
            NAME,
            Cast.to(type),
            CARDINALITY,
            Optional.empty(), // defaultValue
            KINDS
        );

        final Set<ExpressionFunctionParameterKind> kinds = Sets.of(
            ExpressionFunctionParameterKind.CONVERT,
            ExpressionFunctionParameterKind.EVALUATE
        );
        final ExpressionFunctionParameter<List<String>> different = parameter.setKinds(kinds);
        assertNotSame(
            parameter,
            different
        );

        this.nameAndCheck(different);
        this.typeAndCheck(different, type);
        this.cardinalityAndCheck(different);
        this.kindsAndCheck(different, kinds);

        this.nameAndCheck(parameter);
        this.typeAndCheck(parameter, type);
        this.cardinalityAndCheck(parameter);
        this.kindsAndCheck(parameter);
    }

    private void nameAndCheck(final ExpressionFunctionParameter<?> parameter) {
        this.nameAndCheck(
            parameter,
            NAME
        );
    }

    private void nameAndCheck(final ExpressionFunctionParameter<?> parameter,
                              final ExpressionFunctionParameterName name) {
        this.checkEquals(
            parameter.name(),
            name,
            "name"
        );
    }

    private void typeAndCheck(final ExpressionFunctionParameter<?> parameter) {
        this.typeAndCheck(
            parameter,
            TYPE
        );
    }

    private void typeAndCheck(final ExpressionFunctionParameter<?> parameter,
                              final Class<?> type) {
        this.checkEquals(
            parameter.type(),
            type,
            "type"
        );
    }

    private void cardinalityAndCheck(final ExpressionFunctionParameter<?> parameter) {
        this.cardinalityAndCheck(
            parameter,
            CARDINALITY
        );
    }

    private void cardinalityAndCheck(final ExpressionFunctionParameter<?> parameter,
                                     final ExpressionFunctionParameterCardinality cardinality) {
        this.checkEquals(
            parameter.cardinality(),
            cardinality,
            "cardinality"
        );
    }

    private void defaultValueAndCheck(final ExpressionFunctionParameter<?> parameter) {
        this.defaultValueAndCheck(
            parameter,
            DEFAULT_VALUE
        );
    }

    private void defaultValueAndCheck(final ExpressionFunctionParameter<?> parameter,
                                      final Optional<?> defaultValue) {
        this.checkEquals(
            parameter.defaultValue(),
            defaultValue,
            "defaultValue"
        );
    }

    private void kindsAndCheck(final ExpressionFunctionParameter<?> parameter) {
        this.kindsAndCheck(
            parameter,
            KINDS
        );
    }

    private void kindsAndCheck(final ExpressionFunctionParameter<?> parameter,
                               final Set<ExpressionFunctionParameterKind> kinds) {
        this.checkEquals(
            parameter.kinds(),
            kinds,
            "kinds"
        );
    }

    // get..............................................................................................................

    @Test
    public void testGetWithNullParametersFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.empty(), // defaultValue
            KINDS
        );
        assertThrows(
            NullPointerException.class,
            () -> parameter.get(
                null,
                0
            )
        );
    }

    @Test
    @Disabled("Need to emulate Class.cast")
    public void testGetWrongTypeFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.empty(), // defaultValue
            KINDS
        );
        assertThrows(
            ClassCastException.class,
            () -> parameter.get(
                List.of(
                    "A"
                ),
                0
            )
        );
    }

    @Test
    public void testGetMissingReturnsDefaultValue() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.of(999), // defaultValue
            KINDS
        );
        this.checkEquals(
            Optional.of(999),
            parameter.get(
                List.of(
                    100
                ),
                1
            )
        );
    }

    @Test
    public void testGetMissing() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.empty(), // defaultValue
            KINDS
        );
        this.getAndCheck(
            parameter,
            List.of(
                100
            ),
            1
        );
    }

    @Test
    public void testGetNonNullValue() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.empty(), // defaultValue
            KINDS
        );
        this.getAndCheck(
            parameter,
            List.of(
                100,
                "B"
            ),
            0,
            100
        );
    }

    @Test
    public void testGetNullValue() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.empty(), // defaultValue
            KINDS
        );
        this.getAndCheck(
            parameter,
            Arrays.asList(
                null,
                "B"
            ),
            0
        );
    }

    @Test
    public void testGetAbsent() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.empty(), // defaultValue
            KINDS
        );
        this.getAndCheck(
            parameter,
            List.of(
                100,
                "B"
            ),
            99
        );
    }

    @Test
    public void testGetAbsentWithDefaultValue() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.of(999), // defaultValue
            KINDS
        );
        this.getAndCheck(
            parameter,
            List.of(
                100,
                "B"
            ),
            99,
            999
        );
    }

    private <T> void getAndCheck(final ExpressionFunctionParameter<T> parameter,
                                 final List<Object> values,
                                 final int index) {
        this.getAndCheck(
            parameter,
            values,
            index,
            Optional.empty()
        );
    }

    private <T> void getAndCheck(final ExpressionFunctionParameter<T> parameter,
                                 final List<Object> values,
                                 final int index,
                                 final T expected) {
        this.getAndCheck(
            parameter,
            values,
            index,
            Optional.ofNullable(expected)
        );
    }

    private <T> void getAndCheck(final ExpressionFunctionParameter<T> parameter,
                                 final List<Object> values,
                                 final int index,
                                 final Optional<T> expected) {
        this.checkEquals(
            expected,
            parameter.get(
                values,
                index
            ),
            () -> parameter + " get " + index
        );
    }

    // getOrFail........................................................................................................

    @Test
    public void testGetOrFailWithNullParametersFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.REQUIRED,
            Optional.empty(), // defaultValue
            KINDS
        );
        assertThrows(
            NullPointerException.class,
            () -> parameter.getOrFail(
                null,
                0
            )
        );
    }

    @Test
    public void testGetOrFailRequiredAndValueMissingFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.REQUIRED,
            Optional.empty(), // defaultValue
            KINDS
        );
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> parameter.getOrFail(
                List.of(
                    1, 2
                ),
                2
            )
        );
    }

    @Test
    @Disabled("Need to emulate Class.cast")
    public void testGetOrFailWrongTypeFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.REQUIRED,
            Optional.empty(), // defaultValue
            KINDS
        );
        assertThrows(
            ClassCastException.class,
            () -> parameter.getOrFail(
                List.of(
                    "A"
                ),
                0
            )
        );
    }

    @Test
    public void testGetOrFailRequiredValuePresent() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.REQUIRED,
            Optional.empty(), // defaultValue
            KINDS
        );
        this.getOrFailAndCheck(
            parameter,
            List.of(
                100,
                "B"
            ),
            0,
            100
        );
    }

    @Test
    public void testGetOrFailOptionalValuePresent() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.empty(), // defaultValue
            KINDS
        );
        this.getOrFailAndCheck(
            parameter,
            List.of(
                100,
                "B"
            ),
            0,
            100
        );
    }

    @Test
    public void testGetOrFailOptionalMissingGivesDefaultValue() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.OPTIONAL,
            Optional.of(999), // defaultValue
            KINDS
        );
        this.getOrFailAndCheck(
            parameter,
            List.of(
                100,
                "B"
            ),
            3,
            999
        );
    }

    private <T> void getOrFailAndCheck(final ExpressionFunctionParameter<T> parameter,
                                       final List<Object> values,
                                       final int index,
                                       final T expected) {
        this.checkEquals(
            expected,
            parameter.getOrFail(
                values,
                index
            ),
            () -> parameter + " get " + index
        );
    }

    // getVariable......................................................................................................

    @Test
    public void testGetVariableWithNullParametersFails() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.VARIABLE,
            Optional.empty(), // defaultValue
            KINDS
        );
        assertThrows(
            NullPointerException.class,
            () -> parameter.getOrFail(
                null,
                0
            )
        );
    }

    @Test
    public void testGetVariableNone() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            ExpressionFunctionParameterCardinality.VARIABLE,
            Optional.empty(), // defaultValue
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
            ExpressionFunctionParameterCardinality.VARIABLE,
            Optional.empty(), // defaultValue
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
            ExpressionFunctionParameterCardinality.VARIABLE,
            Optional.empty(), // defaultValue
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

    // convertOrFail....................................................................................................

    @Test
    public void testConvertOrFailWithScalar() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameter.with(
            NAME,
            Integer.class,
            CARDINALITY,
            Optional.empty(), // defaultValue
            KINDS
        );

        this.checkEquals(
            12,
            parameter.convertOrFail(
                "12",
                this.expressionEvaluationContext()
            )
        );
    }

    private <T> void convertOrFailAndCheck(final ExpressionFunctionParameter<T> parameter,
                                           final Object value,
                                           final T expected) {
        this.convertOrFailAndCheck(
            parameter,
            value,
            this.expressionEvaluationContext(),
            expected
        );
    }

    private <T> void convertOrFailAndCheck(final ExpressionFunctionParameter<T> parameter,
                                           final Object value,
                                           final ExpressionEvaluationContext context,
                                           final T expected) {
        this.checkEquals(
            expected,
            parameter.convertOrFail(
                value,
                context
            )
        );
    }

    private ExpressionEvaluationContext expressionEvaluationContext() {
        return new FakeExpressionEvaluationContext() {
            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                checkEquals(Integer.class, target, "target");
                return Cast.to(
                    Either.left(
                        Integer.parseInt((String) value
                        )
                    )
                );
            }
        };
    }

    // list.............................................................................................................

    @Test
    public void testList() {
        final ExpressionFunctionParameter<String> string = ExpressionFunctionParameterName.with("string")
            .required(String.class);
        final ExpressionFunctionParameter<Integer> integer = ExpressionFunctionParameterName.with("integer")
            .required(Integer.class);

        this.checkEquals(
            Lists.of(string, integer),
            ExpressionFunctionParameter.list(string, integer)
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(
            ExpressionFunctionParameter.with(
                ExpressionFunctionParameterName.with("different"),
                TYPE,
                CARDINALITY,
                DEFAULT_VALUE,
                KINDS
            )
        );
    }

    @Test
    public void testEqualsDifferentTypeAndDifferentDefaultValue() {
        this.checkNotEquals(
            ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                CARDINALITY,
                Optional.empty(),
                KINDS
            )
        );
    }

    @Test
    public void testEqualsDifferentCardinality() {
        assertNotSame(ExpressionFunctionParameterCardinality.OPTIONAL, CARDINALITY);

        this.checkNotEquals(
            ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                ExpressionFunctionParameterCardinality.OPTIONAL,
                DEFAULT_VALUE,
                KINDS
            )
        );
    }

    @Override
    public ExpressionFunctionParameter<String> createObject() {
        return ExpressionFunctionParameter.with(
            NAME,
            TYPE,
            CARDINALITY,
            DEFAULT_VALUE,
            KINDS
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWithoutDefaultValue() {
        this.toStringAndCheck(
            ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                ExpressionFunctionParameterCardinality.OPTIONAL,
                Optional.empty(),
                KINDS
            ),
            "java.lang.String name?"
        );
    }

    @Test
    public void testToStringOptional() {
        this.toStringAndCheck(
            ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                ExpressionFunctionParameterCardinality.OPTIONAL,
                Optional.empty(), // defaultValue
                KINDS
            ),
            "java.lang.String name?"
        );
    }

    @Test
    public void testToStringRequired() {
        this.toStringAndCheck(
            ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                ExpressionFunctionParameterCardinality.REQUIRED,
                Optional.empty(), // defaultValue
                KINDS
            ),
            "java.lang.String name"
        );
    }

    @Test
    public void testToStringVariable() {
        this.toStringAndCheck(
            ExpressionFunctionParameter.with(
                NAME,
                TYPE,
                ExpressionFunctionParameterCardinality.VARIABLE,
                Optional.empty(), // defaultValue
                KINDS
            ),
            "java.lang.String name*"
        );
    }

    @Test
    public void testToStringWithKinds() {
        this.toStringAndCheck(
            ExpressionFunctionParameter.with(
                NAME,
                List.class,
                ExpressionFunctionParameterCardinality.REQUIRED,
                Optional.empty(), // defaultValue
                EnumSet.of(
                    ExpressionFunctionParameterKind.CONVERT,
                    ExpressionFunctionParameterKind.EVALUATE
                )
            ),
            "@CONVERT, @EVALUATE java.util.List name"
        );
    }

    @Test
    public void testToStringWithKindsWithDefaultValue() {
        this.toStringAndCheck(
            ExpressionFunctionParameter.with(
                NAME,
                List.class,
                ExpressionFunctionParameterCardinality.REQUIRED,
                Optional.of(
                    Lists.of("DefaultValue")
                ), // defaultValue
                EnumSet.of(
                    ExpressionFunctionParameterKind.CONVERT,
                    ExpressionFunctionParameterKind.EVALUATE
                )
            ),
            "@CONVERT, @EVALUATE java.util.List name \"DefaultValue\""
        );
    }

    @Test
    public void testToStringWithKindsWithDefaultValueNonStringDefaultValue() {
        this.toStringAndCheck(
            ExpressionFunctionParameter.with(
                NAME,
                Integer.class,
                ExpressionFunctionParameterCardinality.REQUIRED,
                Optional.of(123), // defaultValue
                EnumSet.of(
                    ExpressionFunctionParameterKind.CONVERT,
                    ExpressionFunctionParameterKind.EVALUATE
                )
            ),
            "@CONVERT, @EVALUATE java.lang.Integer name 123"
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

            final ExpressionFunctionParameter<?> constant = (ExpressionFunctionParameter<?>) field.get(null);
            this.checkNotEquals(null, constant.name(), "name");
            this.checkNotEquals(null, constant.kinds(), "kinds");
            this.checkNotEquals(null, constant.type(), "type");
            this.checkNotEquals(null, constant.defaultValue(), "defaultValue");
            this.checkNotEquals(null, constant.cardinality(), "cardinality");

            i++;
        }

        this.checkNotEquals(0, i, "constant count");
    }

    @Override
    public Set<ExpressionFunctionParameter<String>> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // Class............................................................................................................

    @Override
    public Class<ExpressionFunctionParameter<String>> type() {
        return Cast.to(ExpressionFunctionParameter.class);
    }
}
