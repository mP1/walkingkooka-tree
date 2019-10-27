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
import walkingkooka.tree.expression.ExpressionAdditionNode;
import walkingkooka.tree.expression.ExpressionAndNode;
import walkingkooka.tree.expression.ExpressionBigDecimalNode;
import walkingkooka.tree.expression.ExpressionBigIntegerNode;
import walkingkooka.tree.expression.ExpressionBooleanNode;
import walkingkooka.tree.expression.ExpressionDivisionNode;
import walkingkooka.tree.expression.ExpressionDoubleNode;
import walkingkooka.tree.expression.ExpressionEqualsNode;
import walkingkooka.tree.expression.ExpressionFunctionNode;
import walkingkooka.tree.expression.ExpressionGreaterThanEqualsNode;
import walkingkooka.tree.expression.ExpressionGreaterThanNode;
import walkingkooka.tree.expression.ExpressionLessThanEqualsNode;
import walkingkooka.tree.expression.ExpressionLessThanNode;
import walkingkooka.tree.expression.ExpressionLocalDateNode;
import walkingkooka.tree.expression.ExpressionLocalDateTimeNode;
import walkingkooka.tree.expression.ExpressionLocalTimeNode;
import walkingkooka.tree.expression.ExpressionLongNode;
import walkingkooka.tree.expression.ExpressionModuloNode;
import walkingkooka.tree.expression.ExpressionMultiplicationNode;
import walkingkooka.tree.expression.ExpressionNegativeNode;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.ExpressionNodeVisitor;
import walkingkooka.tree.expression.ExpressionNotEqualsNode;
import walkingkooka.tree.expression.ExpressionNotNode;
import walkingkooka.tree.expression.ExpressionOrNode;
import walkingkooka.tree.expression.ExpressionPowerNode;
import walkingkooka.tree.expression.ExpressionReferenceNode;
import walkingkooka.tree.expression.ExpressionSubtractionNode;
import walkingkooka.tree.expression.ExpressionTextNode;
import walkingkooka.tree.expression.ExpressionXorNode;
import walkingkooka.visit.Visiting;

import java.util.Arrays;
import java.util.List;

/**
 * A {@link ExpressionNodeVisitor} that turns a {@link ExpressionNode} into a xpath expression string.
 * It assumes that all {@link walkingkooka.tree.expression.ExpressionReference} are attributes,
 * some values such as {@link java.time.LocalDate} become function calls with a string literal.
 */
final class ExpressionNodeSelectorToStringExpressionNodeVisitor extends ExpressionNodeVisitor {

    static String toString(final ExpressionNode node) {
        final ExpressionNodeSelectorToStringExpressionNodeVisitor visitor = new ExpressionNodeSelectorToStringExpressionNodeVisitor();
        visitor.accept(node);
        return visitor.toString();
    }

    // Testing
    ExpressionNodeSelectorToStringExpressionNodeVisitor() {
        super();
    }

    @Override
    protected void visit(final ExpressionBigDecimalNode node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final ExpressionBigIntegerNode node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final ExpressionBooleanNode node) {
        this.function(node.value().toString()); // outputs either true(), false()
    }

    @Override
    protected void visit(final ExpressionDoubleNode node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final ExpressionLocalDateNode node) {
        this.function("localDate", node.value().toString()); // localDate()
    }

    @Override
    protected void visit(final ExpressionLocalDateTimeNode node) {
        this.function("localDateTime", node.value().toString()); // localDate()
    }

    @Override
    protected void visit(final ExpressionLocalTimeNode node) {
        this.function("localTime", node.value().toString()); // localDate()
    }

    @Override
    protected void visit(final ExpressionLongNode node) {
        this.numericLiteral(node);
    }

    @Override
    protected void visit(final ExpressionReferenceNode node) {
        this.append("@" + node); // must be an attribute
    }

    @Override
    protected void visit(final ExpressionTextNode node) {
        this.stringLiteral(node.value());
    }

    @Override
    protected Visiting startVisit(final ExpressionAdditionNode node) {
        return this.binary(node.left(), "+", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionAndNode node) {
        return this.binary(node.left(), " and ", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionDivisionNode node) {
        return this.binary(node.left(), " div ", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionEqualsNode node) {
        return this.binary(node.left(), "=", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionFunctionNode node) {
        return this.function(node.name().value(), node.children());
    }

    @Override
    protected Visiting startVisit(final ExpressionGreaterThanNode node) {
        return this.binary(node.left(), ">", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionGreaterThanEqualsNode node) {
        return this.binary(node.left(), ">=", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionLessThanNode node) {
        return this.binary(node.left(), "<", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionLessThanEqualsNode node) {
        return this.binary(node.left(), "<=", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionModuloNode node) {
        return this.binary(node.left(), " mod ", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionMultiplicationNode node) {
        return this.binary(node.left(), "*", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionNegativeNode node) {
        this.append('-');
        return super.startVisit(node);
    }

    @Override
    protected Visiting startVisit(final ExpressionNotEqualsNode node) {
        return this.binary(node.left(), "!=", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionNotNode node) {
        return this.function("not", node.children());
    }

    @Override
    protected Visiting startVisit(final ExpressionOrNode node) {
        return this.binary(node.left(), " or ", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionPowerNode node) {
        return this.function("pow", node.children());
    }

    @Override
    protected Visiting startVisit(final ExpressionSubtractionNode node) {
        return this.binary(node.left(), "-", node.right());
    }

    @Override
    protected Visiting startVisit(final ExpressionXorNode node) {
        return this.function("xor", node.children());
    }

    // helpers ........................................................................................................

    private Visiting binary(final ExpressionNode left,
                            final String operator,
                            final ExpressionNode right) {
        this.accept(left);
        this.append(operator);
        this.accept(right);
        return Visiting.SKIP;
    }

    private Visiting function(final String functionName,
                              final ExpressionNode... parameters) {
        return this.function(functionName, Arrays.asList(parameters));
    }

    private Visiting function(final String functionName,
                              final List<ExpressionNode> parameters) {
        this.functionName(functionName);
        this.parametersBegin();

        String separator = "";
        for(ExpressionNode p : parameters) {
            this.append(separator);
            this.accept(p);
            separator = ",";
        }

        this.parametersEnd();

        return Visiting.SKIP;
    }

    private void functionName(final String functionName) {
        this.append(ExpressionNodeName.with(functionName).value());
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

    private <T extends ExpressionNode & Value<? extends Number>> void numericLiteral(final T number) {
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
