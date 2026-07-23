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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.HasDateTimeSymbolsTesting;
import walkingkooka.text.BinaryTextContextTesting;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.TextPrinting;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FakeExpressionEvaluationContext;

public final class TreeExpressionFunctionTreeTextTest extends TreeExpressionFunctionTestCase<TreeExpressionFunctionTreeText<ExpressionEvaluationContext>,
    ExpressionEvaluationContext,
    String> implements BinaryTextContextTesting,
    HasDateTimeSymbolsTesting {

    @Test
    public void testApplyTreePrintable() {
        this.applyAndCheck2(
            parameters(
                DATE_TIME_SYMBOLS
            ),
            DATE_TIME_SYMBOLS.treeToString(
                TEXT_CONTEXT
            )
        );
    }

    @Test
    public void testApplyTreePrintableIndentation() {
        this.applyAndCheck2(
            parameters(
                DATE_TIME_SYMBOLS,
                DIFFERENT_INDENTATION
            ),
            DATE_TIME_SYMBOLS.treeToString(
                TextPrinting.with(
                    DIFFERENT_INDENTATION,
                    LINE_ENDING
                )
            )
        );
    }

    @Test
    public void testApplyTreePrintableIndentationLineEnding() {
        this.applyAndCheck2(
            parameters(
                DATE_TIME_SYMBOLS,
                DIFFERENT_INDENTATION,
                DIFFERENT_LINE_ENDING
            ),
            DATE_TIME_SYMBOLS.treeToString(
                TextPrinting.with(
                    DIFFERENT_INDENTATION,
                    DIFFERENT_LINE_ENDING
                )
            )
        );
    }

//    @Test
//    public void testApplyTreePrintableLineEnding() {
//        this.applyAndCheck2(
//            parameters(
//                DATE_TIME_SYMBOLS,
//                DIFFERENT_LINE_ENDING
//            ),
//            DATE_TIME_SYMBOLS.treeToString(
//                TextPrinting.with(
//                    INDENTATION,
//                    DIFFERENT_LINE_ENDING
//                )
//            )
//        );
//    }

    @Test
    public void testApplyNonPrintable() {
        final Object value = this;

        this.applyAndCheck2(
            parameters(value),
            value.toString()
        );
    }

    @Test
    public void testApplyString() {
        final String string = "Hello World";

        this.applyAndCheck2(
            parameters(string),
            string
        );
    }

    @Override
    public ExpressionEvaluationContext createContext() {
        return new FakeExpressionEvaluationContext() {

            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type) {
                return this.converter.canConvert(
                    value,
                    type,
                    this
                );
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> type) {
                return this.converter.convert(
                    value,
                    type,
                    this
                );
            }

            private final Converter<ConverterContext> converter = Converters.objectToString();

            @Override
            public Indentation indentation() {
                return TreeExpressionFunctionTreeTextTest.INDENTATION;
            }

            @Override
            public LineEnding lineEnding() {
                return TreeExpressionFunctionTreeTextTest.LINE_ENDING;
            }
        };
    }

    @Override
    public TreeExpressionFunctionTreeText<ExpressionEvaluationContext> createBiFunction() {
        return TreeExpressionFunctionTreeText.instance();
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createBiFunction(),
            "treeText"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TreeExpressionFunctionTreeText<ExpressionEvaluationContext>> type() {
        return Cast.to(TreeExpressionFunctionTreeText.class);
    }
}
