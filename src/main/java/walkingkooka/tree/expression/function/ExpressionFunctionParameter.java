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
    static <T> ExpressionFunctionParameter<T> with(final ExpressionFunctionParameterName name,
                                                   final Class<T> type,
                                                   final List<Class<?>> typeParameters,
                                                   final ExpressionFunctionParameterCardinality cardinality,
                                                   final Set<ExpressionFunctionParameterKind> kinds) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(cardinality, "cardinality");

        return new ExpressionFunctionParameter<>(
                name,
                type,
                cardinality,
                typeParameters,
                kinds
        );
    }

    private ExpressionFunctionParameter(final ExpressionFunctionParameterName name,
                                        final Class<T> type,
                                        final ExpressionFunctionParameterCardinality cardinality,
                                        final List<Class<?>> typeParameters,
                                        final Set<ExpressionFunctionParameterKind> kinds) {
        this.name = name;
        this.type = type;
        this.cardinality = cardinality;
        this.typeParameters = typeParameters;
        this.kinds = kinds;
    }

    @Override
    public ExpressionFunctionParameterName name() {
        return this.name;
    }

    private final ExpressionFunctionParameterName name;

    public Class<T> type() {
        return this.type;
    }

    private final Class<T> type;

    public List<Class<?>> typeParameters() {
        return this.typeParameters;
    }

    /**
     * Sets the type parameter, this is intended to supply the type parameters for {@link List}.
     */
    public ExpressionFunctionParameter<T> setTypeParameters(final List<Class<?>> typeParameters) {
        Objects.requireNonNull(typeParameters, "typeParameters");

        return this.setTypeParameters0(Lists.immutable(typeParameters));
    }

    private ExpressionFunctionParameter<T> setTypeParameters0(final List<Class<?>> typeParameters) {
        return this.typeParameters.equals(typeParameters) ?
                this :
                new ExpressionFunctionParameter<>(
                        this.name,
                        this.type,
                        this.cardinality,
                        typeParameters,
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
        Objects.requireNonNull(cardinality, "cardinality");

        return this.cardinality.equals(cardinality) ?
                this :
                new ExpressionFunctionParameter<>(
                        this.name,
                        this.type,
                        cardinality,
                        this.typeParameters,
                        this.kinds
                );
    }

    private final ExpressionFunctionParameterCardinality cardinality;

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
                        Sets.readOnly(copy)
                );
    }

    private final Set<ExpressionFunctionParameterKind> kinds;

    /**
     * Gets the parameter at index or uses the default
     */
    public Optional<T> get(final List<Object> parameters,
                           final int index) {
        this.cardinality.get(this);

        return index >= parameters.size() ?
                Optional.empty() :
                Optional.of(Cast.to(parameters.get(index)));
    }

    /**
     * Gets the parameter at index or fails and also complains if it is the wrong type.
     */
    public T getOrFail(final List<Object> parameters,
                       final int index) {
        this.cardinality.getOrFail(this);

        if (index >= parameters.size()) {
            throw new IndexOutOfBoundsException("Required parameter " + this.name() + " missing");
        }

        final Object value = parameters.get(index);
        try {
            // https://github.com/mP1/walkingkooka-tree/issues/307
            // Emulate Class.cast
            return Cast.to(value);
        } catch (final ClassCastException cast) {
            throw new ClassCastException("Parameter " + this.name() + " of wrong type " + value.getClass().getName() + " expected " + this.type());
        }
    }

    /**
     * Gets the variable values starting at the given index.
     */
    public List<T> getVariable(final List<Object> parameters,
                               final int index) {
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
                this.kinds.equals(other.kinds);
    }

    @Override
    public String toString() {
        return this.toStringKinds() +
                this.type.getName() +
                this.toStringTypeParameters() +
                " " +
                this.name +
                this.cardinality.parameterToString;
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
