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

public final class ExpressionEqualsNode extends ExpressionComparisonBinaryNode {

    public final static ExpressionNodeName NAME = ExpressionNodeName.fromClass(ExpressionEqualsNode.class);

    public final static String SYMBOL = "=";

    static ExpressionEqualsNode with(final ExpressionNode left, final ExpressionNode right) {
        check(left, right);
        return new ExpressionEqualsNode(NO_INDEX, left, right);
    }

    private ExpressionEqualsNode(final int index, final ExpressionNode left, final ExpressionNode right) {
        super(index, left, right);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    @Override
    public ExpressionEqualsNode removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public ExpressionEqualsNode setChildren(final List<ExpressionNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    ExpressionEqualsNode replace1(final int index, final ExpressionNode left, final ExpressionNode right) {
        return new ExpressionEqualsNode(index, left, right);
    }

    // Visitor .........................................................................................................

    @Override
    public void accept(final ExpressionNodeVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // evaluation .........................................................................................................

    @Override
    boolean isComparisonTrue(final int comparisonResult) {
        return 0 == comparisonResult;
    }

    // object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ExpressionEqualsNode;
    }

    @Override
    void appendSymbol(final StringBuilder b) {
        b.append(SYMBOL);
    }
}
