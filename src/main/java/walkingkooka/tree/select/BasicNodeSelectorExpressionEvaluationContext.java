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
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContextDelegator;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextDelegator;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContextDelegator;
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.select.parser.NodeSelectorAttributeName;

import java.time.LocalDateTime;
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
    implements NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE>,
    ConverterContextDelegator,
    EnvironmentContextDelegator,
    LocaleContextDelegator {

    /**
     * Factory that creates a new {@link BasicNodeSelectorExpressionEvaluationContext}, using the given {@link Node} as the context.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
    BasicNodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> with(final N node,
                                                                              final ExpressionEvaluationContext context) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(context, "context");

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
    public N node() {
        return this.node;
    }

    private final N node;

    // namedFunction.........................................................................................................

    @Override
    public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
        Objects.requireNonNull(scoped, "scoped");

        throw new UnsupportedOperationException("Scoped variables not supported");
    }

    @Override
    public Object evaluate(final String expression) {
        return this.context.evaluate(expression);
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
    public boolean isText(final Object value) {
        return this.context.isText(value);
    }

    @Override
    public CaseSensitivity stringEqualsCaseSensitivity() {
        return this.context.stringEqualsCaseSensitivity();
    }

    // ConverterContextDelegator........................................................................................

    @Override
    public ConverterContext converterContext() {
        return this.context;
    }

    @Override
    public ExpressionNumberKind expressionNumberKind() {
        return this.context.expressionNumberKind();
    }

    // EnvironmentContext...............................................................................................

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> cloneEnvironment() {
        final ExpressionEvaluationContext before = this.context;
        final ExpressionEvaluationContext after = before.cloneEnvironment();

        return before == after ?
            this :
            new BasicNodeSelectorExpressionEvaluationContext<>(
                this.node,
                null
            );
    }

    @Override
    public NodeSelectorExpressionEvaluationContext<N, NAME, ANAME, AVALUE> setEnvironmentContext(final EnvironmentContext environmentContext) {
        Objects.requireNonNull(environmentContext, "environmentContext");

        final ExpressionEvaluationContext before = this.context;
        final ExpressionEvaluationContext after = before.setEnvironmentContext(environmentContext);
        return before == after ?
            this :
            new BasicNodeSelectorExpressionEvaluationContext<>(
                this.node,
                this.context
            );
    }

    @Override
    public Indentation indentation() {
        return this.context.indentation();
    }

    @Override
    public LineEnding lineEnding() {
        return this.context.lineEnding();
    }

    @Override
    public LocalDateTime now() {
        return this.environmentContext()
            .now();
    }

    @Override
    public EnvironmentContext environmentContext() {
        return this.context;
    }

    // LocaleContext....................................................................................................

    @Override
    public LocaleContext localeContext() {
        return this.context;
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public void setLocale(final Locale locale) {
        this.context.setLocale(locale);
    }

    private final ExpressionEvaluationContext context;

    @Override
    public String toString() {
        return this.node.toString();
    }
}
