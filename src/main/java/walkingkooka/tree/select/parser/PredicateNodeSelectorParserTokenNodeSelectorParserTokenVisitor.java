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

package walkingkooka.tree.select.parser;

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.select.NodeSelectorException;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * This {@link NodeSelectorParserTokenVisitor} translates a {@link PredicateNodeSelectorParserToken} into a {@link Expression} equivalent.
 * A support {@link walkingkooka.tree.expression.ExpressionEvaluationContext} will provide namedFunction to definition and attribute to value lookups.
 */
final class PredicateNodeSelectorParserTokenNodeSelectorParserTokenVisitor extends NodeSelectorParserTokenVisitor {

    /**
     * Converts the contents of a predicate into a {@link Expression}.
     */
    static Expression toExpression(final PredicateNodeSelectorParserToken token,
                                   final Predicate<ExpressionFunctionName> functions) {
        final PredicateNodeSelectorParserTokenNodeSelectorParserTokenVisitor visitor = new PredicateNodeSelectorParserTokenNodeSelectorParserTokenVisitor(functions);
        token.accept(visitor);

        final List<Expression> nodes = visitor.children;
        final int count = nodes.size();
        if (1 != count) {
            throw new IllegalArgumentException("Expected either 0 or 1 Expressions but got " + count + "=" + nodes);
        }

        return nodes.get(0);
    }

    /**
     * Private ctor use static method.
     */
    // @VisibleForTesting
    PredicateNodeSelectorParserTokenNodeSelectorParserTokenVisitor(final Predicate<ExpressionFunctionName> functions) {
        super();
        this.functions = functions;
    }

    @Override
    protected Visiting startVisit(final AdditionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final AdditionNodeSelectorParserToken token) {
        this.exitBinary(Expression::add, token);
    }

    @Override
    protected Visiting startVisit(final AndNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final AndNodeSelectorParserToken token) {
        this.exitBinary(Expression::and, token);
    }

    @Override
    protected Visiting startVisit(final DivisionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final DivisionNodeSelectorParserToken token) {
        this.exitBinary(Expression::divide, token);
    }

    @Override
    protected Visiting startVisit(final EqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final EqualsNodeSelectorParserToken token) {
        this.exitBinary(Expression::equalsExpression, token);
    }

    @Override
    protected Visiting startVisit(final FunctionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final FunctionNodeSelectorParserToken token) {
        final ExpressionFunctionName functionName = ExpressionFunctionName.with(token.functionName().value());

        if (false == this.functions.test(functionName)) {
            throw new NodeSelectorException("Unknown function " + CharSequences.quoteAndEscape(functionName.value()));
        }

        final List<Expression> children = this.children;
        final Expression function = Expression.call(
            Expression.namedFunction(functionName),
            children.subList(1, children.size())
        );

        this.exit();
        this.add(function, token);
    }

    private final Predicate<ExpressionFunctionName> functions;

    @Override
    protected Visiting startVisit(final GreaterThanNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final GreaterThanNodeSelectorParserToken token) {
        this.exitBinary(Expression::greaterThan, token);
    }

    @Override
    protected Visiting startVisit(final GreaterThanEqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final GreaterThanEqualsNodeSelectorParserToken token) {
        this.exitBinary(Expression::greaterThanEquals, token);
    }

    @Override
    protected Visiting startVisit(final GroupNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final GroupNodeSelectorParserToken token) {
        this.exitBinary(Expression::add, token);
    }

    @Override
    protected Visiting startVisit(final LessThanNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final LessThanNodeSelectorParserToken token) {
        this.exitBinary(Expression::lessThan, token);
    }

    @Override
    protected Visiting startVisit(final LessThanEqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final LessThanEqualsNodeSelectorParserToken token) {
        this.exitBinary(Expression::lessThanEquals, token);
    }

    @Override
    protected Visiting startVisit(final ModuloNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final ModuloNodeSelectorParserToken token) {
        this.exitBinary(Expression::modulo, token);
    }

    @Override
    protected Visiting startVisit(final MultiplicationNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final MultiplicationNodeSelectorParserToken token) {
        this.exitBinary(Expression::multiply, token);
    }

    @Override
    protected Visiting startVisit(final NegativeNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NegativeNodeSelectorParserToken token) {
        final Expression parameter = this.children.get(0);
        this.exit();
        this.add(Expression.negative(parameter), token);
    }

    @Override
    protected Visiting startVisit(final NotEqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NotEqualsNodeSelectorParserToken token) {
        this.exitBinary(Expression::notEquals, token);
    }

    @Override
    protected Visiting startVisit(final OrNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final OrNodeSelectorParserToken token) {
        this.exitBinary(Expression::or, token);
    }

    @Override
    protected Visiting startVisit(final SubtractionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final SubtractionNodeSelectorParserToken token) {
        this.exitBinary(Expression::subtract, token);
    }

    // Leaf................................................................................................

    @Override
    protected void visit(final AttributeNameNodeSelectorParserToken token) {
        this.addReference(token.value(), token);
    }

    @Override
    protected void visit(final FunctionNameNodeSelectorParserToken token) {
        this.addReference(token.value(), token);
    }

    @Override
    protected void visit(final ExpressionNumberNodeSelectorParserToken token) {
        this.add(
            Expression.value(
                ExpressionNumberKind.DEFAULT.create(token.value())
            ),
            token
        );
    }

    @Override
    protected void visit(final QuotedTextNodeSelectorParserToken token) {
        this.add(
            Expression.value(
                token.value()
            ),
            token
        );
    }

    // GENERAL PURPOSE .................................................................................................

    private Visiting enter() {
        this.previousChildren = this.previousChildren.push(this.children);
        this.children = Lists.array();

        return Visiting.CONTINUE;
    }

    private void exitBinary(final BiFunction<Expression, Expression, Expression> factory,
                            final NodeSelectorParserToken token) {
        final Expression left = this.children.get(0);
        final Expression right = this.children.get(1);
        this.exit();
        this.add(factory.apply(left, right), token);
    }

    private void exit() {
        this.children = this.previousChildren.peek();
        this.previousChildren = this.previousChildren.pop();
    }

    private void addReference(final ExpressionReference reference, final NodeSelectorParserToken token) {
        final Expression node = Expression.reference(reference);
        this.add(node, token);
    }

    private void add(final Expression node, final NodeSelectorParserToken token) {
        if (null == node) {
            throw new NullPointerException("Null node returned for " + token);
        }
        this.children.add(node);
    }

    private Stack<List<Expression>> previousChildren = Stacks.arrayList();

    /**
     * Aggregates the child {@link Expression}.
     */
    private List<Expression> children = Lists.array();

    @Override
    public String toString() {
        return this.children + "," + this.previousChildren;
    }
}
