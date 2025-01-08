package test;

import com.google.gwt.junit.client.GWTTestCase;

import walkingkooka.collect.list.Lists;
import walkingkooka.j2cl.locale.LocaleAware;
import walkingkooka.naming.Names;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorParserContext;
import walkingkooka.tree.select.parser.NodeSelectorParserContexts;
import walkingkooka.tree.select.parser.NodeSelectorParserToken;
import walkingkooka.tree.select.parser.NodeSelectorParsers;

import java.math.MathContext;

@LocaleAware
public class TestGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "test.Test";
    }

    public void testAssertEquals() {
        assertEquals(
            1,
            1
        );
    }

    public void testParseExpression() {
        final ExpressionNumberKind kind = ExpressionNumberKind.DEFAULT;
        final Parser<NodeSelectorParserContext> parser = NodeSelectorParsers.expression()
            .orReport(ParserReporters.basic())
            .cast();
        final NodeSelectorParserContext context = NodeSelectorParserContexts.basic(
            kind,
            MathContext.DECIMAL32
        );
        final NodeSelectorParserToken token = parser.parse(
                TextCursors.charSequence("/node123[45]"),
                context
            ).get()
            .cast(NodeSelectorParserToken.class);
        assertEquals(
            NodeSelector.absolute()
                .named(Names.string("node123"))
                .expression(Expression.value(kind.create(45)))
                .toString(),
            ParserToken.text(
                Lists.of(token)
            )
        );
    }
}
