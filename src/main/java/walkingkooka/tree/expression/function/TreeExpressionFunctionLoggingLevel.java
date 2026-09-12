package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A function returns the current {@link LoggingLevel} or converts the only parameter to a {@link LoggingLevel}.
 */
final class TreeExpressionFunctionLoggingLevel<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<LoggingLevel, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLoggingLevel<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLoggingLevel<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLoggingLevel<>();

    private TreeExpressionFunctionLoggingLevel() {
        super("loggingLevel");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return 0 == count ?
            NO_PARAMETERS :
            PARAMETERS;
    }

    final static ExpressionFunctionParameter<LoggingLevel> LOGGER_LEVEL = ExpressionFunctionParameterName.with("loggerLevel")
        .required(LoggingLevel.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        LOGGER_LEVEL
    );

    @Override
    public Class<LoggingLevel> returnType() {
        return LoggingLevel.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return false;
    }

    @Override
    public LoggingLevel apply(final List<Object> parameters,
                              final C context) {
        LoggingLevel loggingLevel;

        if (parameters.isEmpty()) {
            loggingLevel = context.loggingLevel();
        } else {
            loggingLevel = LOGGER_LEVEL.getOrFail(
                parameters,
                0
            );
        }

        return loggingLevel;
    }
}
