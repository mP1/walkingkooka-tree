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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.visit.Visiting;

public abstract class NodeSelectorParserTokenVisitor extends ParserTokenVisitor {

    // NodeSelectorAdditionParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorAdditionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorAdditionParserToken token) {
        // nop
    }
    
    // NodeSelectorAndParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorAndParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorAndParserToken token) {
        // nop
    }

    // NodeSelectorAttributeParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorAttributeParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorAttributeParserToken token) {
        // nop
    }

    // NodeSelectorDivisionParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorDivisionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorDivisionParserToken token) {
        // nop
    }

    // NodeSelectorEqualsParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorEqualsParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorEqualsParserToken token) {
        // nop
    }

    // NodeSelectorExpressionParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorExpressionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorExpressionParserToken token) {
        // nop
    }

    // NodeSelectorFunctionParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorFunctionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorFunctionParserToken token) {
        // nop
    }

    // NodeSelectorGreaterThanParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorGreaterThanParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorGreaterThanParserToken token) {
        // nop
    }

    // NodeSelectorGreaterThanEqualsParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        // nop
    }

    // NodeSelectorGroupParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorGroupParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorGroupParserToken token) {
        // nop
    }

    // NodeSelectorLessThanParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorLessThanParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorLessThanParserToken token) {
        // nop
    }

    // NodeSelectorLessThanEqualsParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorLessThanEqualsParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorLessThanEqualsParserToken token) {
        // nop
    }

    // NodeSelectorModuloParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorModuloParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorModuloParserToken token) {
        // nop
    }

    // NodeSelectorMultiplicationParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorMultiplicationParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorMultiplicationParserToken token) {
        // nop
    }

    // NodeSelectorNegativeParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorNegativeParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorNegativeParserToken token) {
        // nop
    }
    
    // NodeSelectorNotEqualsParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorNotEqualsParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorNotEqualsParserToken token) {
        // nop
    }

    // NodeSelectorOrParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorOrParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorOrParserToken token) {
        // nop
    }

    // NodeSelectorPredicateParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorPredicateParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorPredicateParserToken token) {
        // nop
    }

    // NodeSelectorSubtractionParserToken....................................................................................

    protected Visiting startVisit(final NodeSelectorSubtractionParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorSubtractionParserToken token) {
        // nop
    }
    
    // NodeSelectorLeafParserToken ..........................................................................

    protected void visit(final NodeSelectorAbsoluteParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorAncestorParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorAncestorOrSelfParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorAndSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorAtSignSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorAttributeNameParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorBracketOpenSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorBracketCloseSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorChildParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorDescendantParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorDescendantOrSelfParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorDivideSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorEqualsSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorFirstChildParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorFollowingParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorFollowingSiblingParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorFunctionNameParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorGreaterThanSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorGreaterThanEqualsSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorLastChildParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorLessThanSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorLessThanEqualsSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorMinusSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorModuloSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorMultiplySymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorNodeNameParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorNotEqualsSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorNumberParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorOrSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorParameterSeparatorSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorParenthesisOpenSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorParenthesisCloseSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorParentOfParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorPlusSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorPrecedingParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorPrecedingSiblingParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorQuotedTextParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorSelfParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorSlashSeparatorSymbolParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorWhitespaceParserToken token) {
        // nop
    }

    protected void visit(final NodeSelectorWildcardParserToken token) {
        // nop
    }

    // ParserToken.......................................................................

    protected Visiting startVisit(final ParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ParserToken token) {
        // nop
    }

    // NodeSelectorParserToken.......................................................................

    protected Visiting startVisit(final NodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelectorParserToken token) {
        // nop
    }
}
