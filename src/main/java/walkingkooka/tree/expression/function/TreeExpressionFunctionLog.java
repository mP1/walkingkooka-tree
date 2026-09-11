package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A function that logs a message at the given {@link walkingkooka.logging.LoggingLevel}.
 */
final class TreeExpressionFunctionLog<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<Void, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLog<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLog<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLog<>();

    private TreeExpressionFunctionLog() {
        super("log");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<LoggingLevel> LOGGER_LEVEL = ExpressionFunctionParameterName.with("loggerLevel")
        .required(LoggingLevel.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<String> MESSAGE = ExpressionFunctionParameterName.with("message")
        .required(String.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        LOGGER_LEVEL,
        MESSAGE
    );

    @Override
    public Class<Void> returnType() {
        return Void.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return false;
    }

    @Override
    public Void apply(final List<Object> parameters,
                      final C context) {
        final LoggingLevel loggingLevel = LOGGER_LEVEL.getOrFail(parameters, 0);
        final String message = MESSAGE.getOrFail(parameters, 1);

        context.log(
            loggingLevel,
            message
        );
        return null;
    }
}
