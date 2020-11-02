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

package walkingkooka.tree.select;

import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;

import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ExpressionEvaluationContext} that wraps another {@link ExpressionEvaluationContext} and retrieves references
 * from a {@link Node node's} attributes.
 */
final class BasicNodeSelectorExpressionEvaluationContext<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
        implements ExpressionEvaluationContext {

    /**
     * Factory that creates a new {@link BasicNodeSelectorExpressionEvaluationContext}, using the given {@link Node} as the context.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    BasicNodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> with(final N node,
                                                                              final ExpressionEvaluationContext context) {
        return new BasicNodeSelectorExpressionEvaluationContext<>(node, context);
    }

    /**
     * Private ctor use factory.
     */
    private BasicNodeSelectorExpressionEvaluationContext(final N node,
                                                         final ExpressionEvaluationContext context) {
        super();
        this.node = node;
        this.context = context;
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    @Override
    public Object evaluate(final Expression expression) {
        return this.context.evaluate(expression);
    }

    @Override
    public Object function(final FunctionExpressionName name,
                           final List<Object> parameters) {
        return this.context.function(name, parameters);
    }

    /**
     * The reference should be an attribute name, cast and find the owner attribute.
     */
    @Override
    public Optional<Expression> reference(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        if (false == reference instanceof NodeSelectorAttributeName) {
            throw new IllegalArgumentException("Expected attribute name but got " + reference);
        }
        final NodeSelectorAttributeName attributeName = Cast.to(reference);
        final String attributeNameString = attributeName.value();

        final Optional<Expression> attributeValue = this.node.attributes()
                .entrySet()
                .stream()
                .filter(nameAndValue -> nameAndValue.getKey().value().equals(attributeNameString))
                .map(this::toExpression)
                .findFirst();
        return attributeValue.isPresent() ?
                attributeValue :
                ABSENT;
    }

    /**
     * Auto convert {@link Number} to {@link ExpressionNumber} and wrap in an {@link Expression}.
     */
    private Expression toExpression(final Entry<?, ?> nameAndValue) {
        final Object value = nameAndValue.getValue();

        return Expression.valueOrFail(ExpressionNumber.is(value) ?
                this.expressionNumberKind().create((Number)value) :
                value);
    }

    /**
     * Returns the current {@link Node}.
     */
    // @VisibleForTesting
    final Node<?, ?, ?, ?> node;

    private final static Optional<Expression> ABSENT = Optional.of(Expression.string(""));

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public <T> Either<T, String> convert(final Object value, final Class<T> target) {
        return this.context.convert(value, target);
    }

    @Override
    public String currencySymbol() {
        return this.context.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return this.context.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return this.context.exponentSymbol();
    }

    @Override
    public char groupingSeparator() {
        return this.context.groupingSeparator();
    }

    @Override
    public char percentageSymbol() {
        return this.context.percentageSymbol();
    }

    @Override
    public char negativeSign() {
        return this.context.negativeSign();
    }

    @Override
    public char positiveSign() {
        return this.context.positiveSign();
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.node.toString();
    }
}
