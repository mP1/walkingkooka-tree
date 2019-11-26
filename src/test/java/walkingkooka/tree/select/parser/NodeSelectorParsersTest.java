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
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserTesting2;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodeSelectorParsersTest implements ParserTesting2<Parser<NodeSelectorParserContext>,
        NodeSelectorParserContext> {

    // descendant ...........................................................................................

    @Test
    public void testDescendantSlashSlashFails() {
        this.parseThrows2(descendantOrSelfSlashSlash());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFails() {
        this.parseThrows2(nodeName(), descendantOrSelfSlashSlash());
    }

    @Test
    public void testNodeNameDescendantSlashSlashSlashFails() {
        this.parseThrows2(nodeName(), descendantOrSelfSlashSlash(), slash());
    }

    @Test
    public void testNodeNameDescendantSlashSlashNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashWildcard() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), wildcard());
    }

    @Test
    public void testNodeNameDescendantSlashSlashSelfDot() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), selfDot());
    }

    @Test
    public void testNodeNameDescendantSlashSlashParentDotDot() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), parentDotDot());
    }

    @Test
    public void testNodeNameDescendantSlashSlashAncestorNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), ancestor(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashChildNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), child(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashDescendantNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), descendant(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFirstChildNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), firstChild(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFollowingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), following(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashFollowingSiblingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), followingSibling(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashLastChildNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), lastChild(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashParentOfNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), parent(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashPrecedingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), preceding(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashPrecedingSiblingNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), precedingSibling(), nodeName2());
    }

    @Test
    public void testNodeNameDescendantSlashSlashSelfNodeName() {
        this.parseAndCheck2(nodeName(), descendantOrSelfSlashSlash(), self(), nodeName2());
    }

    // parentDot ...............................................................................................

    @Test
    public void testParentDotDot() {
        this.parseAndCheck2(parentDotDot());
    }

    // selfDot ...............................................................................................

    @Test
    public void testSelfDot() {
        this.parseAndCheck2(selfDot());
    }

    // selfDot ...............................................................................................

    @Test
    public void testWildcard() {
        this.parseAndCheck2(wildcard());
    }

    // absolute node................................................................................................

    @Test
    public void testAbsoluteNodeName() {
        this.parseAndCheck2(absolute(), nodeName());
    }

    // absolute axis node .....................................................................................

    @Test
    public void testAbsoluteAncestorNodeNameMissingFails() {
        this.parseThrows2(absolute(), ancestor());
    }

    @Test
    public void testAbsoluteAncestorNodeName() {
        this.parseAndCheck2(absolute(), ancestor(), nodeName());
    }

    @Test
    public void testAbsoluteChildNodeName() {
        this.parseAndCheck2(absolute(), child(), nodeName());
    }

    @Test
    public void testAbsoluteDescendantNodeName() {
        this.parseAndCheck2(absolute(), descendant(), nodeName());
    }

    @Test
    public void testAbsoluteFirstChildNodeName() {
        this.parseAndCheck2(absolute(), firstChild(), nodeName());
    }

    @Test
    public void testAbsoluteFollowingNodeName() {
        this.parseAndCheck2(absolute(), following(), nodeName());
    }

    @Test
    public void testAbsoluteFollowingSiblingNodeName() {
        this.parseAndCheck2(absolute(), followingSibling(), nodeName());
    }

    @Test
    public void testAbsoluteLastChildNodeName() {
        this.parseAndCheck2(absolute(), lastChild(), nodeName());
    }

    @Test
    public void testAbsolutePrecedingNodeName() {
        this.parseAndCheck2(absolute(), preceding(), nodeName());
    }

    @Test
    public void testAbsolutePrecedingSiblingNodeName() {
        this.parseAndCheck2(absolute(), precedingSibling(), nodeName());
    }

    @Test
    public void testAbsoluteParentNodeName() {
        this.parseAndCheck2(absolute(), parent(), nodeName());
    }

    @Test
    public void testAbsoluteSelfNodeName() {
        this.parseAndCheck2(absolute(), self(), nodeName());
    }

    // absolute nodeName predicate child index ..........................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenIndexBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(number()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(whitespace(), number(), whitespace()),
                bracketClose());
    }

    // absolute nodeName predicate number.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        number()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenDecimalNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        NodeSelectorParserToken.number(BigDecimal.valueOf(12.5), "12.5")),
                bracketClose());
    }


    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        whitespace(), number(), whitespace()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        negative(
                                minusSymbol(),
                                number()
                        )
                ),
                bracketClose());
    }

    // absolute nodeName predicate quoted text.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        quotedText()
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceQuotedTextWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        whitespace(), quotedText(), whitespace()
                ),
                bracketClose());
    }

    // absolute nodeName predicate attribute.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        attribute(atSign(), attributeName())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceAttributeWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        whitespace(),
                        attribute(atSign(), attributeName()),
                        whitespace()
                ),
                bracketClose());
    }

    // absolute nodeName predicate function.....................................................................

    // /nodeName [
    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                whitespace(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(whitespace(),
                        function(
                                functionName(),
                                parenthesisOpen(),
                                whitespace(),
                                parenthesisClose()
                        ),
                        whitespace()),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                number(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                number(), comma(), number2(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                number(), comma(), number2(), comma(), number(3),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(),
                                parenthesisOpen(),
                                quotedText(),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        function(functionName(), parenthesisOpen(),
                                function(functionName("f2"), parenthesisOpen(), quotedText(), parenthesisClose()),
                                parenthesisClose())),
                bracketClose());
    }

    // absolute nodeName predicate EQ.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                number(),
                                equalsSymbol(),
                                number2()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                negative(
                                        minusSymbol(),
                                        number()
                                ),
                                equalsSymbol(),
                                number2()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNegativeNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                number(),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        number2()
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeNumberEqualsNegativeNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                negative(
                                        minusSymbol(),
                                        number()
                                ),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        number2()
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(quotedText(), equalsSymbol(), quotedText2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                equalsSymbol(),
                                number()
                        )
                ),
                bracketClose());
    }


    @Test
    public void testAbsoluteNodeNameBracketOpenNegativeFunctionEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                negative(
                                        minusSymbol(),
                                        function(functionName(), parenthesisOpen(), parenthesisClose())
                                ),
                                equalsSymbol(),
                                number()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionEqualsFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                equalsSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                attribute(atSign(), attributeName2())
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenAttributeNameLessThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThan(
                                attribute(atSign(), attributeName()),
                                lessThanSymbol(),
                                number())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenAttributeNameLessThanMinusNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThan(
                                attribute(atSign(), attributeName()),
                                lessThanSymbol(),
                                negative(
                                        minusSymbol(),
                                        number()
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                number())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsMinusNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        number()
                                ))),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                attribute(atSign(), attributeName()),
                                equalsSymbol(),
                                quotedText())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNegativeAttributeBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(
                                number(),
                                equalsSymbol(),
                                negative(
                                        minusSymbol(),
                                        attribute(
                                                atSign(),
                                                attributeName()
                                        )
                                )
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        equalsParserToken(number(), equalsSymbol(), quotedText())),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(whitespace(),
                        equalsParserToken(number(), whitespace(), equalsSymbol(), whitespace(), number2()),
                        whitespace()),
                bracketClose());
    }

    // absolute nodeName predicate GT.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(number(), greaterThanSymbol(), number2())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanMinusNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(
                                number(),
                                greaterThanSymbol(),
                                negative(
                                        minusSymbol(),
                                        number2()
                                ))
                ),
                bracketClose());
    }

    // absolute nodeName predicate GTE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanEqualNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThanEquals(number(), greaterThanEqualsSymbol(), number2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate LT.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThan(number(), lessThanSymbol(), number2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate LTE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanEqualNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        lessThanEquals(number(), lessThanEqualsSymbol(), number2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate NE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberNotEqualNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        notEquals(number(), notEqualsSymbol(), number2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate ADD.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberAdditionNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        addition(number(), plusSymbol(), number2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate DIVIDE.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberDivisionNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        division(number(), divideSymbol(), number2())
                ),
                bracketClose());
    }

    // absolute nodeName predicate MODULO.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberModuloNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        modulo(number(), moduloSymbol(), number2())),
                bracketClose());
    }

    // absolute nodeName predicate MULTIPLY.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberMultiplyNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        multiplication(number(), multiplySymbol(), number2())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberMultiplyNumberAddNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        addition(
                                multiplication(
                                        number(),
                                        multiplySymbol(),
                                        number2()),
                                plusSymbol(),
                                number3()
                        )
                ),
                bracketClose());
    }

    // absolute nodeName predicate GROUP.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(), number(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNegativeNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                negative(
                                        minusSymbol(),
                                        number()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(), quotedText(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenFunctionParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                function(
                                        functionName(), parenthesisOpen(), number(), parenthesisClose()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenNumberGreaterThanNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                greaterThan(number(), greaterThanSymbol(), number2()),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenOpenParenOpenNumberGreaterThanNumberParenCloseParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        group(
                                parenthesisOpen(),
                                group(
                                        parenthesisOpen(),
                                        greaterThan(
                                                number(),
                                                greaterThanSymbol(),
                                                number2()),
                                        parenthesisClose()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenParenNumberParensCloseGreaterThanNumberParenClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(
                                group(
                                        parenthesisOpen(),
                                        number(),
                                        parenthesisClose()
                                ),
                                greaterThanSymbol(),
                                number2()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberBracketGreaterThanParenOpenNumberParensCloseParenClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                bracketOpen(),
                predicate(
                        greaterThan(
                                number(),
                                greaterThanSymbol(),
                                group(
                                        parenthesisOpen(),
                                        number2(),
                                        parenthesisClose()
                                )
                        )
                ),
                bracketClose());
    }

    // absolute wildcard................................................................................................

    @Test
    public void testAbsoluteWildcard() {
        this.parseAndCheck2(absolute(), wildcard());
    }

    // absolute axis wildcard....................................................................................

    @Test
    public void testAbsoluteAncestorWildcardFails() {
        this.parseThrows2(absolute(), ancestor());
    }

    @Test
    public void testAbsoluteAncestorWildcard() {
        this.parseAndCheck2(absolute(), ancestor(), wildcard());
    }

    @Test
    public void testAbsoluteAncestorOrSelfWildcardFails() {
        this.parseThrows2(absolute(), ancestorOrSelf());
    }

    @Test
    public void testAbsoluteAncestorOrSelfWildcard() {
        this.parseAndCheck2(absolute(), ancestorOrSelf(), wildcard());
    }

    @Test
    public void testAbsoluteChildWildcard() {
        this.parseAndCheck2(absolute(), child(), wildcard());
    }

    @Test
    public void testAbsoluteDescendantWildcard() {
        this.parseAndCheck2(absolute(), descendant(), wildcard());
    }

    @Test
    public void testAbsoluteDescendantOrSelfWildcard() {
        this.parseAndCheck2(absolute(), descendantOrSelf(), wildcard());
    }

    @Test
    public void testAbsoluteFirstChildWildcard() {
        this.parseAndCheck2(absolute(), firstChild(), wildcard());
    }

    @Test
    public void testAbsoluteFollowingWildcard() {
        this.parseAndCheck2(absolute(), following(), wildcard());
    }

    @Test
    public void testAbsoluteFollowingSiblingWildcard() {
        this.parseAndCheck2(absolute(), followingSibling(), wildcard());
    }

    @Test
    public void testAbsoluteLastChildWildcard() {
        this.parseAndCheck2(absolute(), lastChild(), wildcard());
    }

    @Test
    public void testAbsolutePrecedingWildcard() {
        this.parseAndCheck2(absolute(), preceding(), wildcard());
    }

    @Test
    public void testAbsolutePrecedingSiblingWildcard() {
        this.parseAndCheck2(absolute(), precedingSibling(), wildcard());
    }

    @Test
    public void testAbsoluteParentWildcard() {
        this.parseAndCheck2(absolute(), parent(), wildcard());
    }

    @Test
    public void testAbsoluteSelfWildcard() {
        this.parseAndCheck2(absolute(), self(), wildcard());
    }

    // absolute wildcard predicate ...................................................................................

    @Test
    public void testIndexMissingNumberFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen());
    }

    @Test
    public void testIndexMissingNumberFails2() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                bracketClose());
    }

    @Test
    public void testIndexMissingBracketCloseFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(number()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenIndexBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(number()),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(whitespace(), number(), whitespace()),
                bracketClose());
    }

    // absolute wildcard predicate function.....................................................................

    // /wildcard [
    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        functionWithoutArguments()
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), whitespace(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(whitespace(),
                        function(
                                functionName(), parenthesisOpen(), whitespace(), parenthesisClose()
                        ),
                        whitespace()),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), number(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), number(), comma(), number2(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaNumberCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), number(), comma(), number2(), comma(), number3(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberWhitespaceCommaNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), number(), whitespace(), comma(), number2(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberCommaWhitespaceNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), number(), comma(), whitespace(), number2(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(), quotedText(), parenthesisClose()
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        function(
                                functionName(), parenthesisOpen(),
                                function(
                                        functionName("f2"), parenthesisOpen(), quotedText(), parenthesisClose()
                                ),
                                parenthesisClose()
                        )
                ),
                bracketClose());
    }

    // and ....................................................................................................

    @Test
    public void testAndMissingRightFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        number(),
                        andSymbol()));
    }

    @Test
    public void testAndMissingRightFails2() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        number(),
                        andSymbol()),
                bracketClose());
    }

    @Test
    public void testAndMissingBracketCloseFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        number(),
                        andSymbol()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberAndNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        and(
                                number(),
                                andSymbol(),
                                number2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        and(
                                number(),
                                andSymbol(),
                                number2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        and(
                                functionWithoutArguments(),
                                andSymbol(),
                                whitespace(),
                                functionWithArguments())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        whitespace(),
                        and(
                                functionWithoutArguments(),
                                whitespace(),
                                andSymbol(),
                                whitespace(),
                                functionWithArguments()
                        ),
                        whitespace()
                ),
                bracketClose());
    }

    // or ....................................................................................................

    @Test
    public void testOrMissingRightFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(number(),
                        orSymbol()));
    }

    @Test
    public void testOrMissingRightFails2() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(number(),
                        orSymbol()
                ),
                bracketClose());
    }

    @Test
    public void testOrMissingBracketCloseFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(number(),
                        orSymbol(),
                        number()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberOrNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                number(),
                                orSymbol(),
                                number2())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                number(),
                                orSymbol(),
                                number2())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                functionWithoutArguments(),
                                orSymbol(),
                                whitespace(),
                                functionWithArguments())),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                or(
                                        functionWithoutArguments(),
                                        orSymbol(),
                                        whitespace(),
                                        number()),
                                orSymbol(),
                                quotedText())
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        whitespace(),
                        or(
                                functionWithoutArguments(),
                                whitespace(),
                                orSymbol(),
                                whitespace(),
                                functionWithArguments()
                        ),
                        whitespace()
                ),
                bracketClose());
    }

    // and or...............................................................................................

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                and(functionWithoutArguments(),
                                        andSymbol(),
                                        functionWithArguments()),
                                orSymbol(),
                                functionWithoutArguments())),
                bracketClose());
    }

    // and or and...............................................................................................

    @Test
    public void testAbsoluteWildcardBracketOpenNumberAndNumberOrNumberAndNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                and(
                                        number(1),
                                        andSymbol(),
                                        number(2)),
                                orSymbol(),
                                and(
                                        number(3),
                                        andSymbol(),
                                        number(4))
                        )
                ),
                bracketClose());
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionAndFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                bracketOpen(),
                predicate(
                        or(
                                and(
                                        functionWithoutArguments(),
                                        andSymbol(),
                                        functionWithArguments()),
                                orSymbol(),
                                and(
                                        functionWithoutArguments(),
                                        andSymbol(),
                                        functionWithoutArguments()
                                )
                        )
                ),
                bracketClose());
    }

    // relative axis node .....................................................................................

    @Test
    public void testRelativeAncestorNodeNameMissingFails() {
        this.parseThrows2(ancestor());
    }

    @Test
    public void testRelativeAncestorNodeName() {
        this.parseAndCheck2(ancestor(), nodeName());
    }

    @Test
    public void testRelativeAncestorOrSelfNodeNameMissingFails() {
        this.parseThrows2(ancestorOrSelf());
    }

    @Test
    public void testRelativeAncestorOrSelfNodeName() {
        this.parseAndCheck2(ancestorOrSelf(), nodeName());
    }

    @Test
    public void testRelativeChildNodeName() {
        this.parseAndCheck2(child(), nodeName());
    }

    @Test
    public void testRelativeDescendantNodeNameMissingFails() {
        this.parseThrows2(descendant());
    }

    @Test
    public void testRelativeDescendantNodeName() {
        this.parseAndCheck2(descendant(), nodeName());
    }

    @Test
    public void testRelativeDescendantOrSelfNodeNameMissingFails() {
        this.parseThrows2(descendantOrSelf());
    }

    @Test
    public void testRelativeDescendantOrSelfNodeName() {
        this.parseAndCheck2(descendantOrSelf(), nodeName());
    }

    @Test
    public void testRelativeFirstChildNodeName() {
        this.parseAndCheck2(firstChild(), nodeName());
    }

    @Test
    public void testRelativeFollowingNodeName() {
        this.parseAndCheck2(following(), nodeName());
    }

    @Test
    public void testRelativeFollowingSiblingNodeName() {
        this.parseAndCheck2(followingSibling(), nodeName());
    }

    @Test
    public void testRelativeLastChildNodeName() {
        this.parseAndCheck2(lastChild(), nodeName());
    }

    @Test
    public void testRelativePrecedingNodeName() {
        this.parseAndCheck2(preceding(), nodeName());
    }

    @Test
    public void testRelativePrecedingSiblingNodeName() {
        this.parseAndCheck2(precedingSibling(), nodeName());
    }

    @Test
    public void testRelativeParentNodeName() {
        this.parseAndCheck2(parent(), nodeName());
    }

    @Test
    public void testRelativeSelfNodeName() {
        this.parseAndCheck2(self(), nodeName());
    }

    // relative axis wildcard....................................................................................

    @Test
    public void testRelativeAncestorWildcardFails() {
        this.parseThrows2(ancestor());
    }

    @Test
    public void testRelativeAncestorWildcard() {
        this.parseAndCheck2(ancestor(), wildcard());
    }

    @Test
    public void testRelativeChildWildcard() {
        this.parseAndCheck2(child(), wildcard());
    }

    @Test
    public void testRelativeDescendantWildcard() {
        this.parseAndCheck2(descendant(), wildcard());
    }

    @Test
    public void testRelativeFirstChildWildcard() {
        this.parseAndCheck2(firstChild(), wildcard());
    }

    @Test
    public void testRelativeFollowingWildcard() {
        this.parseAndCheck2(following(), wildcard());
    }

    @Test
    public void testRelativeFollowingSiblingWildcard() {
        this.parseAndCheck2(followingSibling(), wildcard());
    }

    @Test
    public void testRelativeLastChildWildcard() {
        this.parseAndCheck2(lastChild(), wildcard());
    }

    @Test
    public void testRelativePrecedingWildcard() {
        this.parseAndCheck2(preceding(), wildcard());
    }

    @Test
    public void testRelativePrecedingSiblingWildcard() {
        this.parseAndCheck2(precedingSibling(), wildcard());
    }

    @Test
    public void testRelativeParentWildcard() {
        this.parseAndCheck2(parent(), wildcard());
    }

    @Test
    public void testRelativeSelfWildcard() {
        this.parseAndCheck2(self(), wildcard());
    }

    // helpers....................................................................................................

    @Override
    public Parser<NodeSelectorParserContext> createParser() {
        return NodeSelectorParsers.expression()
                .orReport(ParserReporters.basic())
                .cast();
    }

    @Override
    public NodeSelectorParserContext createContext() {
        return NodeSelectorParserContexts.basic(DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    // helpers................................................................................................

    private void parseAndCheck2(final NodeSelectorParserToken... tokens) {
        final List<ParserToken> list = Lists.of(tokens);
        final String text = ParserToken.text(list);

        assertEquals(text, text, "text should be all upper case");

        final Parser<NodeSelectorParserContext> parser = this.createParser();
        this.parseAndCheck3(parser,
                text,
                NodeSelectorParserToken.expression(list, text),
                text);

        final List<ParserToken> lower = Arrays.stream(tokens)
                .map(NodeSelectorParsersTestNodeSelectorParserTokenVisitor::toUpper)
                .collect(Collectors.toList());
        final String textUpper = ParserToken.text(lower);

        this.parseAndCheck3(parser,
                textUpper,
                NodeSelectorParserToken.expression(lower, textUpper),
                textUpper);
    }

    private TextCursor parseAndCheck3(final Parser<NodeSelectorParserContext> parser,
                                      final String cursorText,
                                      final ParserToken token,
                                      final String text) {
        final TextCursor after = this.parseAndCheck(parser,
                cursorText,
                token,
                text,
                "");

        // if the expression has a NodeSelectorPredicateParserToken convert it to text and try parse back
        final TestNodeSelectorParserTokenVisitor visitor = new TestNodeSelectorParserTokenVisitor();
        visitor.accept(token);
        final NodeSelectorPredicateParserToken predicate = visitor.predicate;

        if (null != predicate) {
            final String predicateText = predicate.text();

            this.parseAndCheck(NodeSelectorParsers.predicate(),
                    predicateText,
                    predicate,
                    predicateText,
                    "");
        }
        return after;
    }

    static class TestNodeSelectorParserTokenVisitor extends NodeSelectorParserTokenVisitor {
        @Override
        protected Visiting startVisit(final NodeSelectorPredicateParserToken token) {
            this.predicate = token;
            return Visiting.SKIP;
        }

        NodeSelectorPredicateParserToken predicate;
    }

    private void parseThrows2(final NodeSelectorParserToken... tokens) {
        this.parseThrows(this.createParser().orFailIfCursorNotEmpty(ParserReporters.basic()),
                ParserToken.text(Lists.of(tokens)));
    }

    // token factories...............................................................................

    final NodeSelectorParserToken absolute() {
        return NodeSelectorParserToken.absolute("/", "/");
    }

    final NodeSelectorParserToken addition(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.addition(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken ancestor() {
        return NodeSelectorParserToken.ancestor("ancestor::", "ancestor::");
    }

    final NodeSelectorParserToken ancestorOrSelf() {
        return NodeSelectorParserToken.ancestorOrSelf("ancestor-or-self::", "ancestor-or-self::");
    }

    final NodeSelectorParserToken and(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.and(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken andSymbol() {
        return NodeSelectorParserToken.andSymbol("and", "and");
    }

    final NodeSelectorParserToken atSign() {
        return NodeSelectorParserToken.atSignSymbol("@", "@");
    }

    final NodeSelectorParserToken attribute(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.attribute(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken attributeName() {
        return attributeName("attribute1");
    }

    final NodeSelectorParserToken attributeName2() {
        return attributeName("attribute2");
    }

    final NodeSelectorParserToken attributeName(final String name) {
        return NodeSelectorParserToken.attributeName(NodeSelectorAttributeName.with(name), name);
    }

    final NodeSelectorParserToken bracketOpen() {
        return NodeSelectorParserToken.bracketOpenSymbol("[", "[");
    }

    final NodeSelectorParserToken bracketClose() {
        return NodeSelectorParserToken.bracketCloseSymbol("]", "]");
    }

    final NodeSelectorParserToken child() {
        return NodeSelectorParserToken.child("child::", "child::");
    }

    final NodeSelectorParserToken comma() {
        return NodeSelectorParserToken.parameterSeparatorSymbol(",", ",");
    }

    final NodeSelectorParserToken descendant() {
        return NodeSelectorParserToken.descendant("descendant::", "descendant::");
    }

    final NodeSelectorParserToken descendantOrSelf() {
        return NodeSelectorParserToken.descendantOrSelf("descendant-or-self::", "descendant-or-self::");
    }

    final NodeSelectorParserToken descendantOrSelfSlashSlash() {
        return NodeSelectorParserToken.descendantOrSelf("//", "//");
    }

    final NodeSelectorParserToken divideSymbol() {
        return NodeSelectorParserToken.divideSymbol("div", "div");
    }

    final NodeSelectorParserToken division(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.division(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken equalsParserToken(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.equalsParserToken(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken equalsSymbol() {
        return NodeSelectorParserToken.equalsSymbol("=", "=");
    }

    final NodeSelectorParserToken expression(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.expression(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken firstChild() {
        return NodeSelectorParserToken.firstChild("first-child::", "first-child::");
    }

    final NodeSelectorParserToken following() {
        return NodeSelectorParserToken.following("following::", "following::");
    }

    final NodeSelectorParserToken followingSibling() {
        return NodeSelectorParserToken.followingSibling("following-sibling::", "following-sibling::");
    }

    final NodeSelectorParserToken function(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.function(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken functionWithoutArguments() {
        return function(functionName(), parenthesisOpen(), parenthesisClose());
    }

    final NodeSelectorParserToken functionWithArguments() {
        return function(functionName(), parenthesisOpen(), number(), comma(), quotedText(), parenthesisClose());
    }

    final NodeSelectorParserToken functionName() {
        return functionName("contains");
    }

    final NodeSelectorParserToken functionName(final String text) {
        return NodeSelectorParserToken.functionName(NodeSelectorFunctionName.with(text), text);
    }

    final NodeSelectorParserToken greaterThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThan(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken greaterThanSymbol() {
        return NodeSelectorParserToken.greaterThanSymbol(">", ">");
    }

    final NodeSelectorParserToken greaterThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThanEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken greaterThanEqualsSymbol() {
        return NodeSelectorParserToken.greaterThanEqualsSymbol(">=", ">=");
    }

    final NodeSelectorParserToken group(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.group(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken lessThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThan(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken lessThanSymbol() {
        return NodeSelectorParserToken.lessThanSymbol("<", "<");
    }

    final NodeSelectorParserToken lessThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThanEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken lessThanEqualsSymbol() {
        return NodeSelectorParserToken.lessThanEqualsSymbol("<=", "<=");
    }

    final NodeSelectorParserToken lastChild() {
        return NodeSelectorParserToken.lastChild("last-child::", "last-child::");
    }

    final NodeSelectorParserToken minusSymbol() {
        return NodeSelectorParserToken.minusSymbol("-", "-");
    }

    final NodeSelectorParserToken modulo(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.modulo(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken moduloSymbol() {
        return NodeSelectorParserToken.moduloSymbol("mod", "mod");
    }

    final NodeSelectorParserToken multiplication(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.multiplication(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken multiplySymbol() {
        return NodeSelectorParserToken.multiplySymbol("*", "*");
    }

    final NodeSelectorParserToken negative(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.negative(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken nodeName() {
        return nodeName("Node1");
    }

    final NodeSelectorParserToken nodeName2() {
        return nodeName("Node22");
    }

    final NodeSelectorParserToken nodeName3() {
        return nodeName("Node333");
    }

    final NodeSelectorParserToken nodeName(final String name) {
        return NodeSelectorParserToken.nodeName(NodeSelectorNodeName.with(name), name);
    }

    final NodeSelectorParserToken notEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.notEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken notEqualsSymbol() {
        return NodeSelectorParserToken.notEqualsSymbol("!=", "!=");
    }

    final NodeSelectorParserToken number() {
        return number(1.5);
    }

    final NodeSelectorParserToken number2() {
        return number(23.5);
    }

    final NodeSelectorParserToken number3() {
        return number(345.5);
    }

    final NodeSelectorParserToken number4() {
        return number(4567.5);
    }

    final NodeSelectorParserToken number(final double value) {
        return NodeSelectorParserToken.number(BigDecimal.valueOf(value), String.valueOf(value));
    }

    final NodeSelectorOrParserToken or(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.or(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken orSymbol() {
        return NodeSelectorParserToken.orSymbol("or", "or");
    }

    final NodeSelectorParserToken parenthesisOpen() {
        return NodeSelectorParserToken.parenthesisOpenSymbol("(", "(");
    }

    final NodeSelectorParserToken parenthesisClose() {
        return NodeSelectorParserToken.parenthesisCloseSymbol(")", ")");
    }

    final NodeSelectorParserToken parent() {
        return NodeSelectorParserToken.parentOf("parent::", "parent::");
    }

    final NodeSelectorParserToken parentDotDot() {
        return NodeSelectorParserToken.parentOf("..", "..");
    }

    final NodeSelectorParserToken plusSymbol() {
        return NodeSelectorParserToken.plusSymbol("+", "+");
    }

    final NodeSelectorParserToken preceding() {
        return NodeSelectorParserToken.preceding("preceding::", "preceding::");
    }

    final NodeSelectorParserToken precedingSibling() {
        return NodeSelectorParserToken.precedingSibling("preceding-sibling::", "preceding-sibling::");
    }

    final NodeSelectorParserToken predicate(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.predicate(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken quotedText() {
        return quotedText("xyz");
    }

    final NodeSelectorParserToken quotedText2() {
        return quotedText("qrst");
    }

    final NodeSelectorParserToken quotedText(final String value) {
        return NodeSelectorParserToken.quotedText(value, CharSequences.quoteAndEscape(value).toString());
    }

    final NodeSelectorParserToken self() {
        return NodeSelectorParserToken.self("self::", "self::");
    }

    final NodeSelectorParserToken selfDot() {
        return NodeSelectorParserToken.self(".", ".");
    }

    final NodeSelectorParserToken slash() {
        return NodeSelectorParserToken.slashSeparatorSymbol("/", "/");
    }

    final NodeSelectorParserToken subtraction(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.subtraction(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken whitespace() {
        return NodeSelectorParserToken.whitespace("  ", "  ");
    }

    final NodeSelectorParserToken wildcard() {
        return NodeSelectorParserToken.wildcard("*", "*");
    }

    private static String text(final NodeSelectorParserToken... tokens) {
        return ParserToken.text(Lists.of(tokens));
    }

}
