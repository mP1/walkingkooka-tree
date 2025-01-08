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
import walkingkooka.text.CaseSensitivity;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link ExpressionEvaluationContext} that wraps another {@link ExpressionEvaluationContext} and retrieves references
 * from a {@link Node node's} attributes.
 * <br>
 * Note the provided {@link ExpressionEvaluationContext} needs to have the following functions registered to support
 * the similarly named predicates.
 * <ul>
 *     <li>contains</li>
 *     <li>ends-with</li>
 *     <li>equals</li>
 *     <li>starts-with</li>
 * </ul>
 */
final class BasicNodeSelectorExpressionEvaluationContext<N extends Node<N, NAME, ANAME, AVALUE>,
    NAME extends Name,
    ANAME extends Name,
    AVALUE>
    implements NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> {

    /**
     * Factory that creates a new {@link BasicNodeSelectorExpressionEvaluationContext}, using the given {@link Node} as the context.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
    BasicNodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> with(final N node,
                                                                              final Function<Function<ExpressionReference, Optional<Optional<Object>>>, ExpressionEvaluationContext> context) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(context, "context");

        return new BasicNodeSelectorExpressionEvaluationContext<>(node, context);
    }

    /**
     * Private ctor use factory.
     */
    private BasicNodeSelectorExpressionEvaluationContext(final N node,
                                                         final Function<Function<ExpressionReference, Optional<Optional<Object>>>, ExpressionEvaluationContext> context) {
        super();
        this.node = node;
        this.context = context.apply(
            BasicNodeSelectorExpressionEvaluationContextReferenceFunction.with(this)
        );
    }

    @Override
    public N node() {
        return this.node;
    }

    private final N node;

    // namedFunction.........................................................................................................

    @Override
    public ExpressionEvaluationContext context(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
        throw new UnsupportedOperationException("Scoped variables not supported");
    }

    @Override
    public ExpressionFunction<?, ExpressionEvaluationContext> expressionFunction(final ExpressionFunctionName name) {
        return this.context.expressionFunction(name);
    }

    @Override
    public <T> T prepareParameter(final ExpressionFunctionParameter<T> parameter,
                                  final Object value) {
        return this.context.prepareParameter(parameter, value);
    }

    @Override
    public Object evaluate(final Expression expression) {
        return expression.toValue(this);
    }

    /**
     * Before invoking the {@link ExpressionFunction} identified by the given {@link ExpressionFunctionName} parameters are resolved
     */
    @Override
    public Object evaluateFunction(final ExpressionFunction<?, ? extends ExpressionEvaluationContext> function,
                                   final List<Object> parameters) {
        return function.apply(
            this.prepareParameters(function, parameters),
            Cast.to(this)
        );
    }

    @Override
    public boolean isPure(final ExpressionFunctionName name) {
        return this.context.isPure(name);
    }

    @Override
    public Object handleException(final RuntimeException exception) {
        return this.context.handleException(exception);
    }

    /**
     * The reference should be an attribute name, cast and find the owner attribute.
     */
    @Override
    public Optional<Optional<Object>> reference(final ExpressionReference reference) {
        Objects.requireNonNull(reference, "reference");

        if (false == reference instanceof NodeSelectorAttributeName) {
            throw new IllegalArgumentException("Expected attribute name but got " + reference);
        }
        final NodeSelectorAttributeName attributeName = Cast.to(reference);
        final String attributeNameString = attributeName.value();

        final Object attributeValue = this.node.attributes()
            .entrySet()
            .stream()
            .filter(nameAndValue -> nameAndValue.getKey().value().equals(attributeNameString))
            .map(e -> wrapIfNumber(e.getValue()))
            .findFirst()
            .orElse(ABSENT);
        return Optional.of(
            Optional.of(
                attributeValue
            )
        );
    }

    private Object wrapIfNumber(final Object value) {
        return ExpressionNumber.is(value) ?
            this.expressionNumberKind()
                .create((Number) value) :
            value;
    }

    private final static Object ABSENT = "";

    @Override
    public long dateOffset() {
        return this.context.dateOffset();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return this.context.canConvert(value, type);
    }

    @Override
    public <T> Either<T, String> convert(final Object value, final Class<T> type) {
        return this.context.convert(value, type);
    }

    // DateTimeContext.................................................................................................

    @Override
    public List<String> ampms() {
        return this.context.ampms();
    }

    @Override
    public int defaultYear() {
        return this.context.defaultYear();
    }

    @Override
    public List<String> monthNames() {
        return this.context.monthNames();
    }

    @Override
    public List<String> monthNameAbbreviations() {
        return this.context.monthNameAbbreviations();
    }

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    @Override
    public int twoToFourDigitYear(final int year) {
        return this.context.twoToFourDigitYear(year);
    }

    @Override
    public int twoDigitYear() {
        return this.context.twoDigitYear();
    }

    @Override
    public List<String> weekDayNames() {
        return this.context.weekDayNames();
    }

    @Override
    public List<String> weekDayNameAbbreviations() {
        return this.context.weekDayNameAbbreviations();
    }

    // DecimalNumberContext............................................................................................

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
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    @Override
    public char groupSeparator() {
        return this.context.groupSeparator();
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

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public MathContext mathContext() {
        return this.context.mathContext();
    }

    @Override
    public CaseSensitivity stringEqualsCaseSensitivity() {
        return this.context.stringEqualsCaseSensitivity();
    }

    @Override
    public boolean isText(final Object value) {
        return this.context.isText(value);
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.node.toString();
    }
}
