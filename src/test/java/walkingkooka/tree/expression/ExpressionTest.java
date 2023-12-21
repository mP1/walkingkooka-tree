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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.Printers;
import walkingkooka.tree.NodeTesting;

public final class ExpressionTest implements NodeTesting<Expression, FunctionExpressionName, Name, Object>,
        ClassTesting<Expression> {

    @Override
    public void testParentWithoutChild() {
        // nop
    }

    @Override
    public void testSetSameAttributes() {
        // nop
    }

    @Override
    public void testTypeNaming() {
        // nop
    }

    // replaceIf........................................................................................................

    @Test
    public void testReplaceIfChild() {
        final Expression left = Expression.value(123);
        final Expression right = Expression.value(456);

        final Expression expression = Expression.add(
                left,
                right
        );

        final Expression replacement = Expression.value(789);

        this.replaceIfAndCheck(
                expression,
                (e) -> e.equals(left),
                (e) -> replacement,
                Expression.add(
                        replacement,
                        right
                )
        );
    }

    @Test
    public void testReplaceIfChild2() {
        final Expression left = Expression.value(123);
        final Expression right = Expression.value(456);

        final Expression expression = Expression.add(
                left,
                right
        );

        final Expression replacement = Expression.value(789);

        this.replaceIfAndCheck(
                expression,
                (e) -> e.equals(right),
                (e) -> replacement,
                Expression.add(
                        left,
                        replacement
                )
        );
    }

    @Test
    public void testReplaceIfBothChildren() {
        final Expression left = Expression.value(111);
        final Expression right = Expression.value(222);

        final Expression expression = Expression.add(
                left,
                right
        );

        final Expression replacement1 = Expression.value(333);
        final Expression replacement2 = Expression.value(444);

        this.replaceIfAndCheck(
                expression,
                (e) -> e.equals(left) || e.equals(right),
                (e) -> e.equals(left) ?
                        replacement1 :
                        e.equals(right) ? replacement2 :
                                e,
                Expression.add(
                        replacement1,
                        replacement2
                )
        );
    }

    @Test
    public void testReplaceIfGrandChild() {
        final Expression child1 = Expression.value(111);
        final Expression grandChild1 = Expression.value(222);
        final Expression grandChild2 = Expression.value(333);

        final Expression root = Expression.add(
                child1,
                Expression.add(
                        grandChild1,
                        grandChild2
                )
        );

        final Expression replacement = Expression.value(444);

        this.replaceIfAndCheck(
                root,
                (e) -> e.equals(grandChild2),
                (e) -> replacement,
                Expression.add(
                        child1,
                        Expression.add(
                                grandChild1,
                                replacement
                        )
                )
        );
    }

    @Test
    public void testReplaceIfChildAndGrandChild() {
        final Expression child1 = Expression.value(111);
        final Expression grandChild1 = Expression.value(222);
        final Expression grandChild2 = Expression.value(333);

        final Expression root = Expression.add(
                child1,
                Expression.add(
                        grandChild1,
                        grandChild2
                )
        );

        final Expression replacement1 = Expression.value(444);
        final Expression replacement2 = Expression.value(555);

        this.replaceIfAndCheck(
                root,
                (e) -> e.equals(child1) || e.equals(grandChild2),
                (e) -> e.equals(child1) ?
                        replacement1 :
                        e.equals(grandChild2) ?
                                replacement2 :
                                e,
                Expression.add(
                        replacement1,
                        Expression.add(
                                grandChild1,
                                replacement2
                        )
                )
        );
    }

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

        try (final IndentingPrinter printer = Printers.stringBuilder(printed, LineEnding.NL).indenting(Indentation.SPACES2)) {
            expression.printTree(printer);

            printer.flush();
            this.checkEquals(
                    "AddExpression\n" +
                            "  ValueExpression 1.5 (walkingkooka.tree.expression.ExpressionNumberDouble)\n" +
                            "  MultiplyExpression\n" +
                            "    ValueExpression \"20\" (java.lang.String)\n" +
                            "    ValueExpression true (java.lang.Boolean)\n",
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

    // NodeTesting......................................................................................................

    @Override
    public Expression createNode() {
        return Expression.value(123);
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }
}
