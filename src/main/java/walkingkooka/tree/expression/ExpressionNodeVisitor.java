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

package walkingkooka.tree.expression;

import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} for a graph of {@link ExpressionNode}.
 */
public abstract class ExpressionNodeVisitor extends Visitor<ExpressionNode> {

    // ExpressionNode.......................................................................

    public final void accept(final ExpressionNode node) {
        Objects.requireNonNull(node, "node");

        if (Visiting.CONTINUE == this.startVisit(node)) {
            node.accept(this);
        }
        this.endVisit(node);
    }

    protected Visiting startVisit(final ExpressionNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionNode node) {
        // nop
    }

    protected void visit(final ExpressionBigDecimalNode node) {
        // nop
    }

    protected void visit(final ExpressionBigIntegerNode node) {
        // nop
    }

    protected void visit(final ExpressionBooleanNode node) {
        // nop
    }

    protected void visit(final ExpressionDoubleNode node) {
        // nop
    }

    protected void visit(final ExpressionLocalDateNode node) {
        // nop
    }

    protected void visit(final ExpressionLocalDateTimeNode node) {
        // nop
    }

    protected void visit(final ExpressionLocalTimeNode node) {
        // nop
    }

    protected void visit(final ExpressionLongNode node) {
        // nop
    }

    protected void visit(final ExpressionReferenceNode node) {
        // nop
    }

    protected void visit(final ExpressionTextNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionAdditionNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionAdditionNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionAndNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionAndNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionDivisionNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionDivisionNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionEqualsNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionEqualsNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionFunctionNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionFunctionNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionGreaterThanNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionGreaterThanNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionGreaterThanEqualsNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionGreaterThanEqualsNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionLessThanNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionLessThanNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionLessThanEqualsNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionLessThanEqualsNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionModuloNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionModuloNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionMultiplicationNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionMultiplicationNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionNegativeNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionNegativeNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionNotEqualsNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionNotEqualsNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionNotNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionNotNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionOrNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionOrNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionPowerNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionPowerNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionSubtractionNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionSubtractionNode node) {
        // nop
    }

    protected Visiting startVisit(final ExpressionXorNode node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionXorNode node) {
        // nop
    }
}
