package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.currency.CurrencyValue;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A function that accepts either a parameter and returns a {@link CurrencyValue}.
 */
final class TreeExpressionFunctionCurrencyValue<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<CurrencyValue, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionCurrencyValue<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionCurrencyValue<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionCurrencyValue<>();

    private TreeExpressionFunctionCurrencyValue() {
        super("currencyValue");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<CurrencyValue> CURRENCY_VALUE = ExpressionFunctionParameterName.with("currencyValue")
        .required(CurrencyValue.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        CURRENCY_VALUE
    );

    @Override
    public Class<CurrencyValue> returnType() {
        return CurrencyValue.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public CurrencyValue apply(final List<Object> parameters,
                               final C context) {
        return CURRENCY_VALUE.getOrFail(
            parameters,
            0
        );
    }
}
