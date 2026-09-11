package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

/**
 * A function that logs a debug message.
 */
final class TreeExpressionFunctionLoggingDebug<C extends ExpressionEvaluationContext> extends TreeExpressionFunctionLogging<C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLoggingDebug<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLoggingDebug<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLoggingDebug<>();

    private TreeExpressionFunctionLoggingDebug() {
        super("debug");
    }

    @Override
    void log(final String message,
             final C context) {
        context.debug(message);
    }
}
