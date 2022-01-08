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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;

public final class ExpressionTest implements ClassTesting2<Expression> {

    // TreePrinting......................................................................................................

    @Test
    public void testTreePrint() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DOUBLE;
        final Expression expression = Expression.add(
                Expression.value(kind.create(1.5)),
                Expression.multiply(
                        Expression.value("20"),
                        Expression.value(true)
                )
        );

        final StringBuilder printed = new StringBuilder();

        try (final IndentingPrinter printer = Printers.stringBuilder(printed, LineEnding.NL).indenting(Indentation.with("  "))) {
            expression.printTree(printer);

            printer.flush();
            this.checkEquals(
                    "AddExpression\n" +
                            "  ValueExpression 1.5\n" +
                            "  MultiplyExpression\n" +
                            "    ValueExpression \"20\"\n" +
                            "    ValueExpression true\n",
                    printed.toString()
            );
        }
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<Expression> type() {
        return Expression.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
