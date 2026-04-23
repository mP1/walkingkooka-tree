package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.currency.CurrencyCode;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A function that accepts a single parameter possibly converting and returns a {@link CurrencyCode}.
 */
final class TreeExpressionFunctionCurrencyCode<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<CurrencyCode, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionCurrencyCode<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionCurrencyCode<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionCurrencyCode<>();

    private TreeExpressionFunctionCurrencyCode() {
        super("currencyCode");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<CurrencyCode> CURRENCY_CODE = ExpressionFunctionParameterName.with("currencyCode")
        .required(CurrencyCode.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        CURRENCY_CODE
    );

    @Override
    public Class<CurrencyCode> returnType() {
        return CurrencyCode.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public CurrencyCode apply(final List<Object> parameters,
                              final C context) {
        return CURRENCY_CODE.getOrFail(parameters, 0);
    }
}
