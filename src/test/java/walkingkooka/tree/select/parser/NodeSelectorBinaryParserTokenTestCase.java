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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodeSelectorBinaryParserTokenTestCase<T extends NodeSelectorBinaryParserToken<T>> extends NodeSelectorParentParserTokenTestCase<T> {

    NodeSelectorBinaryParserTokenTestCase() {
        super();
    }

    @Test
    public final void testWithLeftMissingFails() {
        final NodeSelectorParserToken whitespace = this.whitespace();
        final NodeSelectorParserToken symbol = this.operatorSymbol();
        final NodeSelectorParserToken right = this.rightToken();

        assertThrows(IllegalArgumentException.class, () -> this.createToken(whitespace.text() + symbol.text() + right.text(), whitespace, symbol, right));
    }

    @Test
    public final void testWithRightMissingFails() {
        final NodeSelectorParserToken left = this.leftToken();
        final NodeSelectorParserToken symbol = this.operatorSymbol();
        final NodeSelectorParserToken whitespace = this.whitespace();

        assertThrows(IllegalArgumentException.class, () -> this.createToken(left.text() + symbol.text() + whitespace.text(), left, symbol, whitespace));
    }

    @Test
    public final void testWithSymbolMissingFails() {
        final NodeSelectorParserToken left = this.leftToken();
        final NodeSelectorParserToken whitespace = this.whitespace();
        final NodeSelectorParserToken right = this.rightToken();

        this.createToken(left.text() + whitespace.text() + right.text(), left, whitespace, right);
    }

    @Test
    public final void testWithOnlyRequiredTokens() {
        final NodeSelectorParserToken left = this.leftToken();
        final NodeSelectorParserToken operator = this.operatorSymbol();
        final NodeSelectorParserToken right = this.rightToken();

        final String text = left.text() + operator.text() + right.text();
        final T token = this.createToken(text, left, right);
        this.textAndCheck(token, text);
        this.checkValue(token, left, right);
    }

    @Test
    public final void testWith() {
        final NodeSelectorParserToken left = this.leftToken();
        final NodeSelectorParserToken whitespace1 = this.whitespace();
        final NodeSelectorParserToken operator = this.operatorSymbol();
        final NodeSelectorParserToken whitespace2 = this.whitespace();
        final NodeSelectorParserToken right = this.rightToken();

        final String text = left.text() + whitespace1 + operator.text() + whitespace2 + right.text();
        final T token = this.createToken(text, left, operator, right);
        this.textAndCheck(token, text);
        this.checkValue(token, left, operator, right);
    }

    final NodeSelectorParserToken leftToken() {
        return this.nodeName();
    }

    abstract NodeSelectorParserToken operatorSymbol();

    final NodeSelectorParserToken rightToken() {
        return this.wildcard();
    }

    @Override final List<ParserToken> tokens() {
        return Lists.of(this.leftToken(), this.operatorSymbol(), this.rightToken());
    }

    @Override
    public final String text() {
        return this.leftToken().text() + this.operatorSymbol() + this.rightToken().text();
    }

    @Override
    public final T createDifferentToken() {
        final NodeSelectorParserToken left = this.leftToken();
        final NodeSelectorParserToken operatorSymbol = this.operatorSymbol();
        final NodeSelectorParserToken right = this.rightToken();

        return this.createToken(right.text() + operatorSymbol.text() + left.text(), right, operatorSymbol, left);
    }
}
