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

import java.util.List;

public final class ExpressionGreaterThanEqualsNode extends ExpressionComparisonBinaryNode {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionGreaterThanEqualsNode.class);

    public final static String SYMBOL = ">=";

    static ExpressionGreaterThanEqualsNode with(final ExpressionNode left, final ExpressionNode right) {
        check(left, right);
        return new ExpressionGreaterThanEqualsNode(NO_INDEX, left, right);
    }

    private ExpressionGreaterThanEqualsNode(final int index, final ExpressionNode left, final ExpressionNode right) {
        super(index, left, right);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionGreaterThanEqualsNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public ExpressionGreaterThanEqualsNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    ExpressionGreaterThanEqualsNode replace1(final int index, final ExpressionNode left, final ExpressionNode right) {
        return new ExpressionGreaterThanEqualsNode(index, left, right);
    }

    // Visitor .........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Evaluation .......................................................................................................

    @Override
    boolean isComparisonTrue(final int comparisonResult) {
        return comparisonResult >= 0;
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionGreaterThanEqualsNode;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
