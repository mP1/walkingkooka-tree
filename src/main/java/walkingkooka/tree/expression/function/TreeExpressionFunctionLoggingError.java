package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

/**
 * A function that logs a ERROR message.
 */
final class TreeExpressionFunctionLoggingError<C extends ExpressionEvaluationContext> extends TreeExpressionFunctionLogging<C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLoggingError<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLoggingError<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLoggingError<>();

    private TreeExpressionFunctionLoggingError() {
        super("error");
    }

    @Override
    void log(final String message,
             final C context) {
        context.error(message);
    }
}
