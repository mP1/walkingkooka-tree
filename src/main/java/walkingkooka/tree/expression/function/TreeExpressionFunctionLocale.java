package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;
import java.util.Locale;

/**
 * A function that returns a {@link Locale}.
 */
final class TreeExpressionFunctionLocale<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<Locale, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLocale<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLocale<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLocale<>();

    private TreeExpressionFunctionLocale() {
        super("locale");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<Locale> LOCALE = ExpressionFunctionParameterName.with("locale")
        .required(Locale.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        LOCALE
    );

    @Override
    public Class<Locale> returnType() {
        return Locale.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public Locale apply(final List<Object> parameters,
                        final C context) {
        return LOCALE.getOrFail(parameters, 0);
    }
}
