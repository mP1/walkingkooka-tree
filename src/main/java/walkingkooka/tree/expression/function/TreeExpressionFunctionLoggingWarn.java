package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

/**
 * A function that logs a WARN message.
 */
final class TreeExpressionFunctionLoggingWarn<C extends ExpressionEvaluationContext> extends TreeExpressionFunctionLogging<C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLoggingWarn<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLoggingWarn<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLoggingWarn<>();

    private TreeExpressionFunctionLoggingWarn() {
        super("warn");
    }

    @Override
    void log(final String message,
             final C context) {
        context.warn(message);
    }
}
