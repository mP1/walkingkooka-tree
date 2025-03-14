/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.sample;

import walkingkooka.collect.list.Lists;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class Sample {

    public static void main(final String[] args) {
        final Sample sample = new Sample();
        sample.testParseExpression();
        sample.testExpressionFunctionParameterGetOrFail();
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
        final NodeSelectorParserToken token = parser.parse(TextCursors.charSequence("/node123[45]"), context)
            .get()
            .cast(NodeSelectorParserToken.class);
        assertEquals(NodeSelector.absolute()
                .named(Names.string("node123"))
                .expression(Expression.value(kind.create(45)))
                .toString(),
            ParserToken.text(Lists.of(token))
        );
    }

    public void testExpressionFunctionParameterGetOrFail() {
        final ExpressionFunctionParameter<Integer> parameter = ExpressionFunctionParameterName.with("test123").required(Integer.class);
        assertEquals(
            100,
            parameter.getOrFail(
                Lists.of(
                    100,
                    "B"
                ),
                0
            )
        );
    }
}
