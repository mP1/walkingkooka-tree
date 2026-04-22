package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.Currency;
import java.util.List;

/**
 * A function that accepts a parameter and creates a {@link Currency}.
 */
final class TreeExpressionFunctionCurrency<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<Currency, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionCurrency<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionCurrency<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionCurrency<>();

    private TreeExpressionFunctionCurrency() {
        super("currency");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<Currency> CURRENCY = ExpressionFunctionParameterName.with("currency")
        .required(Currency.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        CURRENCY
    );

    @Override
    public Class<Currency> returnType() {
        return Currency.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public Currency apply(final List<Object> parameters,
                          final C context) {
        return CURRENCY.getOrFail(parameters, 0);
    }
}
