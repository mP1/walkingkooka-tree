package walkingkooka.tree.expression.function;

import walkingkooka.Cast;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.TextContext;
import walkingkooka.text.TextPrinting;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionPurityContext;

import java.util.List;

/**
 * A function that invokes {@link TreePrintable#treeToString(TextContext)} or defaults to {@link Object#toString()}.
 */
final class TreeExpressionFunctionTreeText<C extends ExpressionEvaluationContext> extends TreeExpressionFunction<String, C> {

    static <C extends ExpressionEvaluationContext> TreeExpressionFunctionTreeText<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static TreeExpressionFunctionTreeText<ExpressionEvaluationContext> INSTANCE = new TreeExpressionFunctionTreeText<>();

    private TreeExpressionFunctionTreeText() {
        super("treeText");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.with("value")
        .required(Object.class)
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);

    final static ExpressionFunctionParameter<Indentation> INDENTATION = ExpressionFunctionParameterName.with("indentation")
        .optional(Indentation.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES);

    final static ExpressionFunctionParameter<LineEnding> LINE_ENDING = ExpressionFunctionParameterName.with("lineEnding")
        .optional(LineEnding.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE_RESOLVE_REFERENCES);

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = ExpressionFunctionParameter.list(
        VALUE,
        INDENTATION,
        LINE_ENDING
    );

    @Override
    public Class<String> returnType() {
        return String.class;
    }

    @Override
    public boolean isPure(final ExpressionPurityContext context) {
        return true;
    }

    @Override
    public String apply(final List<Object> parameters,
                        final C context) {
        this.checkParameterCount(parameters);

        final Object value = VALUE.getOrFail(parameters, 0);

        final Indentation indentation = INDENTATION.get(parameters, 1)
            .orElse(context.indentation());

        final LineEnding lineEnding = LINE_ENDING.get(parameters, 2)
            .orElse(context.lineEnding());

        final String string;
        if (value instanceof TreePrintable) {
            string = ((TreePrintable) value).treeToString(
                TextPrinting.with(
                    indentation,
                    lineEnding
                )
            );
        } else {
            string = context.convertOrFail(
                value,
                String.class
            );
        }

        return string;
    }
}
