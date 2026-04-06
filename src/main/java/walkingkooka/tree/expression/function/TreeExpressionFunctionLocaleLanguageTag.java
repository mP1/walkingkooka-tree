package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.locale.LocaleLanguageTag;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A function that returns a {@link LocaleLanguageTag}.
 */
final class TreeExpressionFunctionLocaleLanguageTag<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<LocaleLanguageTag, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionLocaleLanguageTag<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionLocaleLanguageTag<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionLocaleLanguageTag<>();

    private TreeExpressionFunctionLocaleLanguageTag() {
        super("localeLanguageTag");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<LocaleLanguageTag> LOCALE_LANGUAGE_TAG = ExpressionFunctionParameterName.with("localeLanguageTag")
        .required(LocaleLanguageTag.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        LOCALE_LANGUAGE_TAG
    );

    @Override
    public Class<LocaleLanguageTag> returnType() {
        return LocaleLanguageTag.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public LocaleLanguageTag apply(final List<Object> parameters,
                                   final C context) {
        return LOCALE_LANGUAGE_TAG.getOrFail(parameters, 0);
    }
}
