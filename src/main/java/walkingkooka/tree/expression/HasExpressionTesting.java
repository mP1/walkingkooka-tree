package walkingkooka.tree.expression;

import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public interface HasExpressionTesting extends TreePrintableTesting {

    default void expressionAndCheck(final HasExpression has) {
        this.expressionAndCheck(
            has,
            Optional.empty()
        );
    }

    default void expressionAndCheck(final HasExpression has,
                                    final Expression expected) {
        this.expressionAndCheck(
            has,
            Optional.of(expected)
        );
    }

    default void expressionAndCheck(final HasExpression has,
                                    final Optional<Expression> expected) {
        this.checkEquals(
            expected,
            has.expression(),
            () -> has + " expression()"
        );
    }
}
