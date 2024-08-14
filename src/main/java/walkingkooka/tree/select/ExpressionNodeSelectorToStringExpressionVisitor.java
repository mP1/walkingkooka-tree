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

import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.AddExpression;
import walkingkooka.tree.expression.AndExpression;
import walkingkooka.tree.expression.CallExpression;
import walkingkooka.tree.expression.DivideExpression;
import walkingkooka.tree.expression.EqualsExpression;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionVisitor;
import walkingkooka.tree.expression.GreaterThanEqualsExpression;
import walkingkooka.tree.expression.GreaterThanExpression;
import walkingkooka.tree.expression.LessThanEqualsExpression;
import walkingkooka.tree.expression.LessThanExpression;
import walkingkooka.tree.expression.ListExpression;
import walkingkooka.tree.expression.ModuloExpression;
import walkingkooka.tree.expression.MultiplyExpression;
import walkingkooka.tree.expression.NamedFunctionExpression;
import walkingkooka.tree.expression.NegativeExpression;
import walkingkooka.tree.expression.NotEqualsExpression;
import walkingkooka.tree.expression.NotExpression;
import walkingkooka.tree.expression.OrExpression;
import walkingkooka.tree.expression.PowerExpression;
import walkingkooka.tree.expression.ReferenceExpression;
import walkingkooka.tree.expression.SubtractExpression;
import walkingkooka.tree.expression.ValueExpression;
import walkingkooka.tree.expression.XorExpression;
import walkingkooka.visit.Visiting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link ExpressionVisitor} that turns a {@link Expression} into a xpath expression string.
 * It assumes that all {@link walkingkooka.tree.expression.ExpressionReference} are attributes,
 * some values such as {@link java.time.LocalDate} become namedFunction calls with a string literal.
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
    protected void visit(final NamedFunctionExpression node) {
        this.append(node.value().value());
    }

    @Override
    protected void visit(final ReferenceExpression node) {
        this.append("@" + node); // must be an attribute
    }

    @Override
    protected void visit(final ValueExpression<?> node) {
        do {
            final Object value = node.value();
            if (value instanceof Boolean) {
                this.namedFunction(value.toString());
                break;
            }
            if (value instanceof String) {
                this.stringLiteral((String) value);
                break;
            }
            if (value instanceof ExpressionNumber) {
                this.append(value.toString());
                break;
            }
            if (value instanceof LocalDate) {
                this.namedFunction("localDate", value.toString());
                break;
            }
            if (value instanceof LocalDateTime) {
                this.namedFunction("localDateTime", value.toString());
                break;
            }
            if (value instanceof LocalTime) {
                this.namedFunction("localTime", value.toString());
                break;
            }
        } while (false);
    }


    @Override
    protected Visiting startVisit(final AddExpression node) {
        return this.binary(node.left(), "+", node.right());
    }

    @Override
    protected Visiting startVisit(final AndExpression node) {
        return this.binary(node.left(), " and ", node.right());
    }

    @Override
    protected Visiting startVisit(final CallExpression node) {
        final Expression callable = node.callable();
        if (!callable.isNamedFunction()) {
            throw new IllegalArgumentException("Only named functions callables supported " + callable);
        }

        final NamedFunctionExpression namedFunctionExpression = (NamedFunctionExpression) callable;

        return this.namedFunction(
                namedFunctionExpression.value()
                        .value(),
                node.value()
        );
    }

    @Override
    protected Visiting startVisit(final DivideExpression node) {
        return this.binary(node.left(), " div ", node.right());
    }

    @Override
    protected Visiting startVisit(final EqualsExpression node) {
        return this.binary(node.left(), "=", node.right());
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
    protected Visiting startVisit(final ListExpression node) {
        this.append('[');
        return Visiting.CONTINUE;
    }

    @Override
    protected void endVisit(final ListExpression node) {
        this.append(']');
    }

    @Override
    protected Visiting startVisit(final ModuloExpression node) {
        return this.binary(node.left(), " mod ", node.right());
    }

    @Override
    protected Visiting startVisit(final MultiplyExpression node) {
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
        return this.namedFunction("not", node.children());
    }

    @Override
    protected Visiting startVisit(final OrExpression node) {
        return this.binary(node.left(), " or ", node.right());
    }

    @Override
    protected Visiting startVisit(final PowerExpression node) {
        return this.namedFunction("pow", node.children());
    }

    @Override
    protected Visiting startVisit(final SubtractExpression node) {
        return this.binary(node.left(), "-", node.right());
    }

    @Override
    protected Visiting startVisit(final XorExpression node) {
        return this.namedFunction("xor", node.children());
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

    private Visiting namedFunction(final String functionName,
                                   final Expression... parameters) {
        return this.namedFunction(
                functionName,
                Arrays.asList(parameters)
        );
    }

    private Visiting namedFunction(final String functionName,
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
        this.append(ExpressionFunctionName.with(functionName).value());
    }

    private void parametersBegin() {
        this.append('(');
    }

    private void parametersEnd() {
        this.append(')');
    }

    private void namedFunction(final String functionName, final String parameter) {
        this.functionName(functionName);
        this.parametersBegin();
        this.stringLiteral(parameter);
        this.parametersEnd();
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
