/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.select.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionNumberKind;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorParserContextTest implements ClassTesting2<BasicNodeSelectorParserContext>,
    NodeSelectorParserContextTesting<BasicNodeSelectorParserContext>,
    DecimalNumberContextDelegator {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    @Test
    public void testWithNullKindFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicNodeSelectorParserContext.with(
                null,
                this.mathContext()
            )
        );
    }

    @Test
    public void testWithNullMathContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> BasicNodeSelectorParserContext.with(KIND, null)
        );
    }

    @Override
    public void testCurrencySymbol() {
    }

    @Override
    public void testGroupSeparator() {
    }

    @Override
    public void testMathContext() {
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(),
            "decimalSeparator='.' exponentSymbol=\"E\" negativeSign='-' percentSymbol='%' positiveSign='+'");
    }

    @Override
    public BasicNodeSelectorParserContext createContext() {
        return BasicNodeSelectorParserContext.with(
            ExpressionNumberKind.DEFAULT,
            this.mathContext()
        );
    }

    // DecimalNumberContextDelegator....................................................................................

    @Override
    public MathContext mathContext() {
        return MathContext.DECIMAL32;
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(this.mathContext());
    }

    // class............................................................................................................

    @Override
    public Class<BasicNodeSelectorParserContext> type() {
        return BasicNodeSelectorParserContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // NodeSelectorParserContextTesting.................................................................................

    @Override
    public void testLocaleFails() {
        throw new UnsupportedOperationException();
    }
}
