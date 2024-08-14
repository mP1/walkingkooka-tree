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
import walkingkooka.naming.Name;
import walkingkooka.tree.ParentNodeTesting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class ParentExpressionTestCase<N extends ParentExpression> extends ExpressionTestCase<N>
        implements ParentNodeTesting<Expression, ExpressionFunctionName, Name, Object> {

    ParentExpressionTestCase() {
        super();
    }

    @Test
    public final void testCreate() {
        final N parent = this.createExpression();
        this.childCountCheck(parent, this.children().size());
    }

    @Test
    @Override
    public final void testSetChildrenSame() {
        final N expression = this.createExpression();
        assertSame(expression, expression.setChildren(expression.children()));
    }

    @Test
    public final void testSetChildrenEquivalent() {
        final N expression = this.createExpression();
        assertSame(expression, expression.setChildren(this.children()));
    }

    abstract List<Expression> children();

    final void checkChildren(final N node, final List<Expression> children) {
        // horrible if equals is used comparison will fail because children have different parents.
        this.checkEquals(children.toString(), node.children().toString(), "children");
    }
}
