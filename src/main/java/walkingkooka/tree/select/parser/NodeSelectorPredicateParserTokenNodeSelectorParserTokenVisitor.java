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
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.select.NodeSelectorException;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * This {@link NodeSelectorParserTokenVisitor} translates a {@link NodeSelectorPredicateParserToken} into a {@link Expression} equivalent.
 * A support {@link walkingkooka.tree.expression.ExpressionEvaluationContext} will provide function to definition and attribute to value lookups.
 */
final class NodeSelectorPredicateParserTokenNodeSelectorParserTokenVisitor extends NodeSelectorParserTokenVisitor {

    /**
     * Converts the contents of a predicate into a {@link Expression}.
     */
    static Expression toExpression(final NodeSelectorPredicateParserToken token,
                                   final Predicate<FunctionExpressionName> functions) {
        final NodeSelectorPredicateParserTokenNodeSelectorParserTokenVisitor visitor = new NodeSelectorPredicateParserTokenNodeSelectorParserTokenVisitor(functions);
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
    NodeSelectorPredicateParserTokenNodeSelectorParserTokenVisitor(final Predicate<FunctionExpressionName> functions) {
        super();
        this.functions = functions;
    }

    @Override
    protected Visiting startVisit(final NodeSelectorAdditionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorAdditionParserToken token) {
        this.exitBinary(Expression::add, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorAndParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorAndParserToken token) {
        this.exitBinary(Expression::and, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorDivisionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorDivisionParserToken token) {
        this.exitBinary(Expression::divide, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorEqualsParserToken token) {
        this.exitBinary(Expression::equalsExpression, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorFunctionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorFunctionParserToken token) {
        final FunctionExpressionName functionName = FunctionExpressionName.with(token.functionName().value());
        if (!this.functions.test(functionName)) {
            throw new NodeSelectorException("Unknown function " + CharSequences.quoteAndEscape(functionName.value()) + " in " + CharSequences.quoteAndEscape(token.toString()));
        }

        final Expression function = Expression.function(
                functionName,
                this.children.subList(1, this.children.size()));
        this.exit();
        this.add(function, token);
    }

    private final Predicate<FunctionExpressionName> functions;

    @Override
    protected Visiting startVisit(final NodeSelectorGreaterThanParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorGreaterThanParserToken token) {
        this.exitBinary(Expression::greaterThan, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        this.exitBinary(Expression::greaterThanEquals, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGroupParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorGroupParserToken token) {
        this.exitBinary(Expression::add, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorLessThanParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorLessThanParserToken token) {
        this.exitBinary(Expression::lessThan, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorLessThanEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorLessThanEqualsParserToken token) {
        this.exitBinary(Expression::lessThanEquals, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorModuloParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorModuloParserToken token) {
        this.exitBinary(Expression::modulo, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorMultiplicationParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorMultiplicationParserToken token) {
        this.exitBinary(Expression::multiply, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorNegativeParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorNegativeParserToken token) {
        final Expression parameter = this.children.get(0);
        this.exit();
        this.add(Expression.negative(parameter), token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorNotEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorNotEqualsParserToken token) {
        this.exitBinary(Expression::notEquals, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorOrParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorOrParserToken token) {
        this.exitBinary(Expression::or, token);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorSubtractionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorSubtractionParserToken token) {
        this.exitBinary(Expression::subtract, token);
    }

    // Leaf................................................................................................

    @Override
    protected void visit(final NodeSelectorAttributeNameParserToken token) {
        this.addReference(token.value(), token);
    }

    @Override
    protected void visit(final NodeSelectorFunctionNameParserToken token) {
        this.addReference(token.value(), token);
    }

    @Override
    protected void visit(final NodeSelectorNumberParserToken token) {
        this.add(Expression.bigDecimal(token.value()), token);
    }

    @Override
    protected void visit(final NodeSelectorQuotedTextParserToken token) {
        this.add(Expression.string(token.value()), token);
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
