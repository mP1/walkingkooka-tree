package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;

/**
 * A function that logs a INFO message.
 */
final class TreeExpressionFunctionLoggingInfo<C extends ExpressionEvaluationContext> extends TreeExpressionFunctionLogging<C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLoggingInfo<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLoggingInfo<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLoggingInfo<>();

    private TreeExpressionFunctionLoggingInfo() {
        super("info");
    }

    @Override
    void log(final String message,
             final C context) {
        context.info(message);
    }
}
