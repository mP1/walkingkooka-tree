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

    // AdditionNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final AdditionNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final AdditionNodeSelectorParserToken token) {
        // nop
    }

    // AndNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final AndNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final AndNodeSelectorParserToken token) {
        // nop
    }

    // AttributeNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final AttributeNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final AttributeNodeSelectorParserToken token) {
        // nop
    }

    // DivisionNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final DivisionNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final DivisionNodeSelectorParserToken token) {
        // nop
    }

    // EqualsNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final EqualsNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final EqualsNodeSelectorParserToken token) {
        // nop
    }

    // ExpressionNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final ExpressionNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ExpressionNodeSelectorParserToken token) {
        // nop
    }

    // FunctionNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final FunctionNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final FunctionNodeSelectorParserToken token) {
        // nop
    }

    // GreaterThanNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final GreaterThanNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final GreaterThanNodeSelectorParserToken token) {
        // nop
    }

    // GreaterThanEqualsNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final GreaterThanEqualsNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final GreaterThanEqualsNodeSelectorParserToken token) {
        // nop
    }

    // GroupNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final GroupNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final GroupNodeSelectorParserToken token) {
        // nop
    }

    // LessThanNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final LessThanNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final LessThanNodeSelectorParserToken token) {
        // nop
    }

    // LessThanEqualsNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final LessThanEqualsNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final LessThanEqualsNodeSelectorParserToken token) {
        // nop
    }

    // ModuloNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final ModuloNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final ModuloNodeSelectorParserToken token) {
        // nop
    }

    // MultiplicationNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final MultiplicationNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final MultiplicationNodeSelectorParserToken token) {
        // nop
    }

    // NegativeNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final NegativeNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NegativeNodeSelectorParserToken token) {
        // nop
    }

    // NotEqualsNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final NotEqualsNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NotEqualsNodeSelectorParserToken token) {
        // nop
    }

    // OrNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final OrNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final OrNodeSelectorParserToken token) {
        // nop
    }

    // PredicateNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final PredicateNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final PredicateNodeSelectorParserToken token) {
        // nop
    }

    // SubtractionNodeSelectorParserToken....................................................................................

    protected Visiting startVisit(final SubtractionNodeSelectorParserToken token) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final SubtractionNodeSelectorParserToken token) {
        // nop
    }

    // LeafNodeSelectorParserToken ..........................................................................

    protected void visit(final AbsoluteNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final AncestorNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final AncestorOrSelfNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final AndSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final AtSignSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final AttributeNameNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final BracketOpenSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final BracketCloseSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final ChildNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final DescendantNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final DescendantOrSelfNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final DivideSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final EqualsSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final FirstChildNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final FollowingNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final FollowingSiblingNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final FunctionNameNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final GreaterThanSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final GreaterThanEqualsSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final LastChildNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final LessThanSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final LessThanEqualsSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final MinusSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final ModuloSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final MultiplySymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final NodeNameNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final NotEqualsSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final ExpressionNumberNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final OrSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final ParameterSeparatorSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final ParenthesisOpenSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final ParenthesisCloseSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final ParentOfNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final PlusSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final PrecedingNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final PrecedingSiblingNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final QuotedTextNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final SelfNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final SlashSeparatorSymbolNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final WhitespaceNodeSelectorParserToken token) {
        // nop
    }

    protected void visit(final WildcardNodeSelectorParserToken token) {
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
