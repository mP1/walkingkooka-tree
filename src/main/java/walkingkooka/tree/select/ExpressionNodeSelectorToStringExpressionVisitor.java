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

import walkingkooka.Value;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.AdditionExpression;
import walkingkooka.tree.expression.AndExpression;
import walkingkooka.tree.expression.BigDecimalExpression;
import walkingkooka.tree.expression.BigIntegerExpression;
import walkingkooka.tree.expression.BooleanExpression;
import walkingkooka.tree.expression.DivisionExpression;
import walkingkooka.tree.expression.DoubleExpression;
import walkingkooka.tree.expression.EqualsExpression;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionVisitor;
import walkingkooka.tree.expression.FunctionExpression;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.GreaterThanEqualsExpression;
import walkingkooka.tree.expression.GreaterThanExpression;
import walkingkooka.tree.expression.LessThanEqualsExpression;
import walkingkooka.tree.expression.LessThanExpression;
import walkingkooka.tree.expression.LocalDateExpression;
import walkingkooka.tree.expression.LocalDateTimeExpression;
import walkingkooka.tree.expression.LocalTimeExpression;
import walkingkooka.tree.expression.LongExpression;
import walkingkooka.tree.expression.ModuloExpression;
import walkingkooka.tree.expression.MultiplicationExpression;
import walkingkooka.tree.expression.NegativeExpression;
import walkingkooka.tree.expression.NotEqualsExpression;
import walkingkooka.tree.expression.NotExpression;
import walkingkooka.tree.expression.OrExpression;
import walkingkooka.tree.expression.PowerExpression;
import walkingkooka.tree.expression.ReferenceExpression;
import walkingkooka.tree.expression.StringExpression;
import walkingkooka.tree.expression.SubtractionExpression;
import walkingkooka.tree.expression.XorExpression;
import walkingkooka.visit.Visiting;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link ExpressionVisitor} that turns a {@link Expression} into a xpath expression string.
 * It assumes that all {@link walkingkooka.tree.expression.ExpressionReference} are attributes,
 * some values such as {@link java.time.LocalDate} become function calls with a string literal.
 */
final class ExpressionNodeSelectorToStringExpressionVisitor extends ExpressionVisitor {

    static String toString(final Expression node) {
        final ExpressionNodeSelectorToStringExpressionVisitor visitor = new ExpressionNodeSelectorToStringExpressionVisitor();
        visitor.accept(node);
        return visitor.toString();
    }

    // Testing
    ExpressionNodeSelectorToStringExpressionVisitor() {
        super();
    }

    @Override
    protected void visit(final BigDecimalExpression node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final BigIntegerExpression node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final BooleanExpression node) {
        this.function(node.value().toString()); // outputs either true(), false()
    }

    @Override
    protected void visit(final DoubleExpression node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final LocalDateExpression node) {
        this.function("localDate", node.value().toString()); // localDate()
    }

    @Override
    protected void visit(final LocalDateTimeExpression node) {
        this.function("localDateTime", node.value().toString()); // localDate()
    }

    @Override
    protected void visit(final LocalTimeExpression node) {
        this.function("localTime", node.value().toString()); // localDate()
    }

    @Override
    protected void visit(final LongExpression node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final ReferenceExpression node) {
        this.append("@" + node); // must be an attribute
    }

    @Override
    protected void visit(final StringExpression node) {
        this.stringLiteral(node.value());
    }

    @Override
    protected Visiting startVisit(final AdditionExpression node) {
        return this.binary(node.left(), "+", node.right());
    }

    @Override
    protected Visiting startVisit(final AndExpression node) {
        return this.binary(node.left(), " and ", node.right());
    }

    @Override
    protected Visiting startVisit(final DivisionExpression node) {
        return this.binary(node.left(), " div ", node.right());
    }

    @Override
    protected Visiting startVisit(final EqualsExpression node) {
        return this.binary(node.left(), "=", node.right());
    }

    @Override
    protected Visiting startVisit(final FunctionExpression node) {
        return this.function(node.name().value(), node.children());
    }

    @Override
    protected Visiting startVisit(final GreaterThanExpression node) {
        return this.binary(node.left(), ">", node.right());
    }

    @Override
    protected Visiting startVisit(final GreaterThanEqualsExpression node) {
        return this.binary(node.left(), ">=", node.right());
    }

    @Override
    protected Visiting startVisit(final LessThanExpression node) {
        return this.binary(node.left(), "<", node.right());
    }

    @Override
    protected Visiting startVisit(final LessThanEqualsExpression node) {
        return this.binary(node.left(), "<=", node.right());
    }

    @Override
    protected Visiting startVisit(final ModuloExpression node) {
        return this.binary(node.left(), " mod ", node.right());
    }

    @Override
    protected Visiting startVisit(final MultiplicationExpression node) {
        return this.binary(node.left(), "*", node.right());
    }

    @Override
    protected Visiting startVisit(final NegativeExpression node) {
        this.append('-');
        return super.startVisit(node);
    }

    @Override
    protected Visiting startVisit(final NotEqualsExpression node) {
        return this.binary(node.left(), "!=", node.right());
    }

    @Override
    protected Visiting startVisit(final NotExpression node) {
        return this.function("not", node.children());
    }

    @Override
    protected Visiting startVisit(final OrExpression node) {
        return this.binary(node.left(), " or ", node.right());
    }

    @Override
    protected Visiting startVisit(final PowerExpression node) {
        return this.function("pow", node.children());
    }

    @Override
    protected Visiting startVisit(final SubtractionExpression node) {
        return this.binary(node.left(), "-", node.right());
    }

    @Override
    protected Visiting startVisit(final XorExpression node) {
        return this.function("xor", node.children());
    }

    // helpers ........................................................................................................

    private Visiting binary(final Expression left,
                            final String operator,
                            final Expression right) {
        this.accept(left);
        this.append(operator);
        this.accept(right);
        return Visiting.SKIP;
    }

    private Visiting function(final String functionName,
                              final Expression... parameters) {
        return this.function(functionName, Arrays.asList(parameters));
    }

    private Visiting function(final String functionName,
                              final List<Expression> parameters) {
        this.functionName(functionName);
        this.parametersBegin();

        String separator = "";
        for (Expression p : parameters) {
            this.append(separator);
            this.accept(p);
            separator = ",";
        }

        this.parametersEnd();

        return Visiting.SKIP;
    }

    private void functionName(final String functionName) {
        this.append(FunctionExpressionName.with(functionName).value());
    }

    private void parametersBegin() {
        this.append('(');
    }

    private void parametersEnd() {
        this.append(')');
    }

    private void function(final String functionName, final String parameter) {
        this.functionName(functionName);
        this.parametersBegin();
        this.stringLiteral(parameter);
        this.parametersEnd();
    }

    private <T extends Expression & Value<? extends Number>> void numericLiteral(final T number) {
        this.append(number.value().toString());
    }

    private void stringLiteral(final String text) {
        this.append(CharSequences.quoteAndEscape(text));
    }

    private void append(final char text) {
        this.b.append(text);
    }

    private void append(final CharSequence text) {
        this.b.append(text);
    }

    private final StringBuilder b = new StringBuilder();

    @Override
    public String toString() {
        return this.b.toString();
    }
}
