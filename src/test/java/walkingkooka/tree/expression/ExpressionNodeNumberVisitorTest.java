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
import walkingkooka.math.NumberVisitorTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNodeNumberVisitorTest implements NumberVisitorTesting<ExpressionNodeNumberVisitor> {

    @Test
    public void testUnknownNumberFails() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionNodeNumberVisitor.expressionNode(new Number() {
            @Override
            public int intValue() {
                return 0;
            }

            @Override
            public long longValue() {
                return 0;
            }

            @Override
            public float floatValue() {
                return 0;
            }

            @Override
            public double doubleValue() {
                return 0;
            }

            private static final long serialVersionUID = 1L;
        }));
    }

    @Test
    public void testToString() {
        final ExpressionNodeNumberVisitor visitor = new ExpressionNodeNumberVisitor();
        visitor.accept(1.25);
        this.toStringAndCheck(visitor, "1.25");
    }

    @Override
    public ExpressionNodeNumberVisitor createVisitor() {
        return new ExpressionNodeNumberVisitor();
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return ExpressionNode.class.getSimpleName();
    }

    @Override
    public Class<ExpressionNodeNumberVisitor> type() {
        return ExpressionNodeNumberVisitor.class;
    }
}
