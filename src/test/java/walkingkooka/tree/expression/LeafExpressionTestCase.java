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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

public abstract class LeafExpressionTestCase<N extends LeafExpression<V>, V> extends ExpressionTestCase<N> {

    LeafExpressionTestCase() {
        super();
    }

    @Test
    public final void testCreate() {
        final N node = this.createExpression();
        this.checkEquals(Lists.empty(), node.children(), "children");
        this.parentMissingCheck(node);
        this.checkValue(node, this.value());
    }

    @Test
    public final void testEqualsDifferentValue() {
        this.checkNotEquals(this.createExpression(), this.createExpression(this.differentValue()));
    }

    @Test
    @Override
    public final void testParentWithoutChild() {
        this.parentMissingCheck(this.createNode());
    }

    @Test
    public final void testRemoveParentWithParent() {
        final N node = this.createExpression();

        final Expression parent = Expression.not(node);
        this.checkEquals(node,
            parent.children().get(0).removeParent());
    }

    @Override final N createExpression() {
        return this.createExpression(this.value());
    }

    abstract N createExpression(final V value);

    abstract V value();

    abstract V differentValue();

    final void checkValue(final N node, final V value) {
        this.checkEquals(value, node.value(), "value");
    }
}
