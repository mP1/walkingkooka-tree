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

import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.HasName;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Captures an individual parameter to a @link ExpressionFunction}.
 */
public final class ExpressionFunctionParameter<T> implements HasName<ExpressionFunctionParameterName> {

    /**
     * Type parameters for types that are not generic and have NO type parameters.
     */
    public final static List<Class<?>> NO_TYPE_PARAMETERS = Lists.empty();

    /**
     * No {@link ExpressionFunctionParameterKind}.
     */
    public final static Set<ExpressionFunctionParameterKind> NO_KINDS = Sets.empty();

    // order important otherwise NO_KINDS will be null when ExpressionFunctionParameter are initialized.

    public final static List<ExpressionFunctionParameter<?>> EMPTY = Lists.empty();

    public final static ExpressionFunctionParameter<Boolean> BOOLEAN = ExpressionFunctionParameterName.BOOLEAN.required(Boolean.class);

    public final static ExpressionFunctionParameter<Character> CHARACTER = ExpressionFunctionParameterName.CHARACTER.required(Character.class);

    public final static ExpressionFunctionParameter<LocalDate> DATE = ExpressionFunctionParameterName.DATE.required(LocalDate.class);

    public final static ExpressionFunctionParameter<LocalDateTime> DATETIME = ExpressionFunctionParameterName.DATETIME.required(LocalDateTime.class);

    public final static ExpressionFunctionParameter<ExpressionNumber> NUMBER = ExpressionFunctionParameterName.NUMBER.required(ExpressionNumber.class);

    public final static ExpressionFunctionParameter<ExpressionReference> REFERENCE = ExpressionFunctionParameterName.REFERENCE.required(ExpressionReference.class);

    public final static ExpressionFunctionParameter<String> STRING = ExpressionFunctionParameterName.STRING.required(String.class);

    public final static ExpressionFunctionParameter<String> TEXT = ExpressionFunctionParameterName.TEXT.required(String.class);

    public final static ExpressionFunctionParameter<LocalTime> TIME = ExpressionFunctionParameterName.TIME.required(LocalTime.class);

    public final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.VALUE.required(Object.class);

    /**
     * Helper that creates a read only list of the given parameters.
     */
    public static List<ExpressionFunctionParameter<?>> list(final ExpressionFunctionParameter<?>... parameters) {
        return Lists.of(parameters);
    }

    // @VisibleForTesting
    public static <T> ExpressionFunctionParameter<T> with(final ExpressionFunctionParameterName name,
                                                          final Class<T> type,
                                                          final List<Class<?>> typeParameters,
                                                          final ExpressionFunctionParameterCardinality cardinality,
                                                          final Optional<T> defaultValue,
                                                          final Set<ExpressionFunctionParameterKind> kinds) {
        return new ExpressionFunctionParameter<>(
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(type, "type"),
            Objects.requireNonNull(cardinality, "cardinality"),
            Lists.immutable(
                Objects.requireNonNull(typeParameters, "typeParameters")
            ),
            Objects.requireNonNull(defaultValue, "defaultValue"),
            Sets.immutable(
                Objects.requireNonNull(kinds, "kinds")
            )
        );
    }

    private ExpressionFunctionParameter(final ExpressionFunctionParameterName name,
                                        final Class<T> type,
                                        final ExpressionFunctionParameterCardinality cardinality,
                                        final List<Class<?>> typeParameters,
                                        final Optional<T> defaultValue,
                                        final Set<ExpressionFunctionParameterKind> kinds) {
        this.name = name;
        this.type = type;
        this.cardinality = cardinality;
        this.typeParameters = typeParameters;
        this.defaultValue = defaultValue;
        this.kinds = kinds;
    }

    @Override
    public ExpressionFunctionParameterName name() {
        return this.name;
    }

    /**
     * Would be setter that returns a {@link ExpressionFunctionParameter} with the given {@link ExpressionFunctionParameterName}.
     */
    public ExpressionFunctionParameter<T> setName(final ExpressionFunctionParameterName name) {
        return this.name.equals(name) ?
            this :
            new ExpressionFunctionParameter<>(
                Objects.requireNonNull(name, "name"),
                this.type,
                this.cardinality,
                this.typeParameters,
                this.defaultValue,
                this.kinds
            );
    }

    private final ExpressionFunctionParameterName name;

    public Class<T> type() {
        return this.type;
    }

    /**
     * Would be setter that returns a {@link ExpressionFunctionParameter} with the new type but copying all other properties.
     * This is particularly useful when updating a {@link ExpressionFunction} parameters such as COUNT which initially has its parameters declared but Object but within a
     * spreadsheet it needs all parameter values converted to a number.
     * Note if the type is different the default value is also cleared.
     */
    public <TT> ExpressionFunctionParameter<TT> setType(final Class<TT> type) {
        return this.type.equals(type) ?
            Cast.to(this) :
            new ExpressionFunctionParameter<>(
                this.name,
                Objects.requireNonNull(type, "type"),
                this.cardinality,
                this.typeParameters,
                Optional.empty(),
                this.kinds
            );
    }

    private final Class<T> type;

    public List<Class<?>> typeParameters() {
        return this.typeParameters;
    }

    /**
     * Sets the type parameter, this is intended to supply the type parameters for {@link List}.
     */
    public ExpressionFunctionParameter<T> setTypeParameters(final List<Class<?>> typeParameters) {
        return this.setTypeParameters0(
            Lists.immutable(
                Objects.requireNonNull(typeParameters, "typeParameters")
            )
        );
    }

    private ExpressionFunctionParameter<T> setTypeParameters0(final List<Class<?>> typeParameters) {
        return this.typeParameters.equals(typeParameters) ?
            this :
            new ExpressionFunctionParameter<>(
                this.name,
                this.type,
                this.cardinality,
                typeParameters,
                this.defaultValue,
                this.kinds
            );
    }

    private final List<Class<?>> typeParameters;

    public ExpressionFunctionParameterCardinality cardinality() {
        return this.cardinality;
    }

    /**
     * Would be setter that returns a {@link ExpressionFunctionParameter} with the given {@link ExpressionFunctionParameterCardinality}.
     */
    public ExpressionFunctionParameter<T> setCardinality(final ExpressionFunctionParameterCardinality cardinality) {
        return this.cardinality.equals(cardinality) ?
            this :
            new ExpressionFunctionParameter<>(
                this.name,
                this.type,
                Objects.requireNonNull(cardinality, "cardinality"),
                this.typeParameters,
                this.defaultValue,
                this.kinds
            );
    }

    private final ExpressionFunctionParameterCardinality cardinality;

    // defaultValue.....................................................................................................

    /**
     * The default value when this parameter is missing or null from function parameters.
     */
    public Optional<T> defaultValue() {
        return this.defaultValue;
    }

    public ExpressionFunctionParameter<T> setDefaultValue(final Optional<T> defaultValue) {
        return this.defaultValue.equals(defaultValue) ?
            this :
            new ExpressionFunctionParameter<>(
                this.name,
                this.type,
                this.cardinality,
                this.typeParameters,
                Objects.requireNonNull(defaultValue, "defaultValue"),
                this.kinds
            );
    }

    private final Optional<T> defaultValue;

    // kinds............................................................................................................

    /**
     * Returns the{@link ExpressionFunctionParameterKind} for this parameter.
     */
    public Set<ExpressionFunctionParameterKind> kinds() {
        return this.kinds;
    }

    /**
     * Returns a {@link ExpressionFunctionParameter} with the given {@link ExpressionFunctionParameterKind}.
     */
    public ExpressionFunctionParameter<T> setKinds(final Set<ExpressionFunctionParameterKind> kinds) {
        Objects.requireNonNull(kinds, "kinds");

        final Set<ExpressionFunctionParameterKind> copy = kinds.isEmpty() ?
            Sets.empty() :
            EnumSet.copyOf(kinds);
        return this.kinds.equals(copy) ?
            this :
            new ExpressionFunctionParameter<>(
                this.name,
                this.type,
                this.cardinality,
                this.typeParameters,
                this.defaultValue,
                Sets.readOnly(copy)
            );
    }

    private final Set<ExpressionFunctionParameterKind> kinds;

    /**
     * Gets the parameter at index or uses the default.
     * <br>
     * Note the index is not validated against the correct position of this parameter within the parameter list.
     */
    public Optional<T> get(final List<Object> parameters,
                           final int index) {
        Objects.requireNonNull(parameters, "parameters");

        this.cardinality.get(this);

        return index >= parameters.size() ?
            this.defaultValue :
            Optional.ofNullable(
                ExpressionFunctionParameterCast.cast(
                    parameters.get(index),
                    this
                )
            );
    }

    /**
     * Gets the parameter at index or fails and also complains if it is the wrong type.
     * <br>
     * Note the index is not validated against the correct position of this parameter within the parameter list.
     */
    public T getOrFail(final List<Object> parameters,
                       final int index) {
        Objects.requireNonNull(parameters, "parameters");

        Object value;

        if (index >= parameters.size()) {
            if (this.cardinality == ExpressionFunctionParameterCardinality.REQUIRED) {
                throw new IndexOutOfBoundsException("Required parameter " + this.name() + " missing");
            }
            value = this.defaultValue.orElse(null);
        } else {
            value = parameters.get(index);
        }

        return ExpressionFunctionParameterCast.cast(
            value,
            this
        );
    }

    /**
     * Gets the variable values starting at the given index.
     * <br>
     * Note the index is not validated against the correct position of this parameter within the parameter list.
     */
    public List<T> getVariable(final List<Object> parameters,
                               final int index) {
        Objects.requireNonNull(parameters, "parameters");

        this.cardinality.getVariable(this);

        return index >= parameters.size() ?
            Lists.empty() :
            Cast.to(parameters.subList(index, parameters.size()));
    }

    /**
     * Converts the given parameter value to match the required type of this parameter.
     */
    public T convertOrFail(final Object value,
                           final ExpressionEvaluationContext context) {
        final Class<T> type = this.type();
        final boolean isList = type.equals(List.class);
        return isList ?
            Cast.to(this.convertList((List<?>) value, context)) :
            context.convertOrFail(value, type);
    }

    private List<?> convertList(final List<?> list,
                                final ExpressionEvaluationContext context) {
        final List<Class<?>> typeParameters = this.typeParameters();
        final int typeParametersCount = typeParameters.size();
        final Class<?> listType;

        switch (typeParametersCount) {
            case 0:
                throw new IllegalStateException("Missing required type parameter for List");
            case 1:
                listType = typeParameters.get(0);
                break;
            default:
                throw new IllegalStateException("Expected only one type parameter for List but got " + typeParameters);
        }

        return Lists.immutable(
            list.stream()
                .map(v -> context.convertOrFail(v, listType))
                .collect(Collectors.toList()));
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.name,
            this.type,
            this.typeParameters,
            this.cardinality,
            this.defaultValue,
            this.kinds
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof ExpressionFunctionParameter && this.equals0((ExpressionFunctionParameter<?>) other);
    }

    private boolean equals0(final ExpressionFunctionParameter<?> other) {
        return this.name.equals(other.name) &&
            this.type.equals(other.type) &&
            this.typeParameters.equals(other.typeParameters) &&
            this.cardinality == other.cardinality &&
            this.defaultValue.equals(other.defaultValue) &&
            this.kinds.equals(other.kinds);
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .disable(ToStringBuilderOption.QUOTE)
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .separator("")
            .value(this.toStringKinds())
            .value(this.type.getName())
            .value(
                this.toStringTypeParameters()
            ).value(" ")
            .value(this.name)
            .value(this.cardinality.parameterToString)
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .enable(ToStringBuilderOption.QUOTE)
            .separator(" ")
            .value(this.defaultValue)
            .build();
    }

    private String toStringKinds() {
        final Set<ExpressionFunctionParameterKind> kinds = this.kinds();

        return kinds.isEmpty() ?
            "" :
            kinds.stream()
                .map(ExpressionFunctionParameterKind::parameterToString)
                .collect(Collectors.joining(", ", "", " "));
    }

    private String toStringTypeParameters() {
        final List<Class<?>> typeParameters = this.typeParameters();

        return typeParameters.isEmpty() ?
            "" :
            typeParameters.stream()
                .map(Class::getName)
                .collect(Collectors.joining(",", "<", ">"));
    }
}
