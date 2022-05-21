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
import walkingkooka.naming.HasName;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Captures an individual parameter to a @link ExpressionFunction}.
 */
public final class ExpressionFunctionParameter<T> implements HasName<ExpressionFunctionParameterName> {

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
     * Type parameters for types that are not generic and have NO type parameters.
     */
    public final static List<Class<?>> NO_TYPE_PARAMETERS = Lists.empty();

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
                                                   final ExpressionFunctionParameterCardinality cardinality) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(cardinality, "cardinality");

        return new ExpressionFunctionParameter<>(name, type, cardinality, typeParameters);
    }

    private ExpressionFunctionParameter(final ExpressionFunctionParameterName name,
                                        final Class<T> type,
                                        final ExpressionFunctionParameterCardinality cardinality,
                                        final List<Class<?>> typeParameters) {
        this.name = name;
        this.type = type;
        this.cardinality = cardinality;
        this.typeParameters = typeParameters;
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
                        name,
                        type,
                        cardinality,
                        typeParameters
                );
    }

    private final List<Class<?>> typeParameters;

    public ExpressionFunctionParameterCardinality cardinality() {
        return this.cardinality;
    }

    private final ExpressionFunctionParameterCardinality cardinality;

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
     * Gets the variable values starting at the given index flattening any Lists which may include {@link ExpressionReference} references.
     * If references contain lists or references these are flatten as well.
     */
    public <C extends ExpressionEvaluationContext> List<T> getVariableAndFlatten(final List<Object> parameters,
                                                                                 final int index,
                                                                                 final ExpressionFunction<?, C> function,
                                                                                 final C context) {
        this.cardinality.getVariable(this);

        return index >= parameters.size() ?
                Lists.empty() :
                Cast.to(
                        flatten(
                                parameters.subList(
                                        index,
                                        parameters.size()
                                ).iterator(),
                                function,
                                context
                        )
                );
    }

    private <C extends ExpressionEvaluationContext> List<T> flatten(final Iterator<Object> parameters,
                                                                    final ExpressionFunction<?, C> function,
                                                                    final C context) {
        final List<T> values = Lists.array();

        flatten0(
                parameters,
                (e) -> values.add((T) e),
                function.kinds()
                        .contains(ExpressionFunctionKind.RESOLVE_REFERENCES),
                context
        );

        return values;
    }

    private <T, C extends ExpressionEvaluationContext> void flatten0(final Iterator<Object> parameters,
                                                                     final Consumer<T> values,
                                                                     final boolean resolveReferences,
                                                                     final C context) {
        while (parameters.hasNext()) {
            this.flatten1(
                    parameters.next(),
                    values,
                    resolveReferences,
                    context
            );
        }
    }

    private <T, C extends ExpressionEvaluationContext> void flatten1(final Object parameter,
                                                                     final Consumer<T> values,
                                                                     final boolean resolveReferences,
                                                                     final C context) {
        if (resolveReferences && parameter instanceof ExpressionReference) {
            this.flatten1(
                    context.referenceOrFail((ExpressionReference) parameter),
                    values,
                    resolveReferences,
                    context
            );
        } else {
            if (parameter instanceof Iterable) {
                final Iterable<Object> iterable = (Iterable<Object>) parameter;
                this.flatten0(
                        iterable.iterator(),
                        values,
                        resolveReferences,
                        context
                );
            } else {
                values.accept((T) parameter);
            }
        }
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
                this.cardinality
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof ExpressionFunctionParameter && this.equals0((ExpressionFunctionParameter) other);
    }

    private boolean equals0(final ExpressionFunctionParameter other) {
        return this.name.equals(other.name) &&
                this.type.equals(other.type) &&
                this.typeParameters.equals(other.typeParameters) &&
                this.cardinality == other.cardinality;
    }

    @Override
    public String toString() {
        return this.type.getName() +
                this.toStringTypeParameters() +
                " " +
                this.name +
                this.cardinality.parameterToString;
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
