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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.naming.Name;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

final class NodeSelectorParsersTestNodeSelectorParserTokenVisitor extends NodeSelectorParserTokenVisitor {

    /**
     * Accepts a graph of {@link NodeSelectorParserToken} and returns the same tokens but with all text and values upper-cased.
     */
    static NodeSelectorParserToken toUpper(final NodeSelectorParserToken token) {
        final NodeSelectorParsersTestNodeSelectorParserTokenVisitor visitor = new NodeSelectorParsersTestNodeSelectorParserTokenVisitor();
        visitor.accept(token);
        return visitor.children.get(0).cast(NodeSelectorParserToken.class);
    }

    private NodeSelectorParsersTestNodeSelectorParserTokenVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final AdditionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final AdditionNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::addition);
    }

    @Override
    protected Visiting startVisit(final AndNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final AndNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::and);
    }

    @Override
    protected Visiting startVisit(final AttributeNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final AttributeNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::attribute);
    }

    @Override
    protected Visiting startVisit(final DivisionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final DivisionNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::division);
    }

    @Override
    protected Visiting startVisit(final EqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final EqualsNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::equalsNodeSelectorParserToken);
    }

    @Override
    protected Visiting startVisit(final ExpressionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final ExpressionNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::expression);
    }

    @Override
    protected Visiting startVisit(final FunctionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final FunctionNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::function);
    }

    @Override
    protected Visiting startVisit(final GreaterThanNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final GreaterThanNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::greaterThan);
    }

    @Override
    protected Visiting startVisit(final GreaterThanEqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final GreaterThanEqualsNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::greaterThanEquals);
    }

    @Override
    protected Visiting startVisit(final GroupNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final GroupNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::group);
    }

    @Override
    protected Visiting startVisit(final LessThanNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final LessThanNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::lessThan);
    }

    @Override
    protected Visiting startVisit(final LessThanEqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final LessThanEqualsNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::lessThanEquals);
    }

    @Override
    protected Visiting startVisit(final NotEqualsNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected Visiting startVisit(final ModuloNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final ModuloNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::modulo);
    }

    @Override
    protected Visiting startVisit(final MultiplicationNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final MultiplicationNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::multiplication);
    }

    @Override
    protected Visiting startVisit(final NegativeNodeSelectorParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NegativeNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::negative);
    }

    @Override
    protected void endVisit(final NotEqualsNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::notEquals);
    }

    @Override
    protected Visiting startVisit(final OrNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final OrNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::or);
    }

    @Override
    protected Visiting startVisit(final PredicateNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final PredicateNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::predicate);
    }

    @Override
    protected Visiting startVisit(final SubtractionNodeSelectorParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final SubtractionNodeSelectorParserToken token) {
        this.exit(NodeSelectorParserToken::subtraction);
    }

    @Override
    protected void visit(final AbsoluteNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::absolute);
    }

    @Override
    protected void visit(final AncestorNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::ancestor);
    }

    @Override
    protected void visit(final AncestorOrSelfNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::ancestorOrSelf);
    }

    @Override
    protected void visit(final AndSymbolNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::andSymbol);
    }

    @Override
    protected void visit(final AtSignSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final AttributeNameNodeSelectorParserToken token) {
        this.addName(token, NodeSelectorAttributeName::with, NodeSelectorParserToken::attributeName);
    }

    @Override
    protected void visit(final BracketOpenSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final BracketCloseSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ChildNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::child);
    }

    @Override
    protected void visit(final DescendantNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::descendant);
    }

    @Override
    protected void visit(final DescendantOrSelfNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::descendantOrSelf);
    }

    @Override
    protected void visit(final DivideSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final EqualsSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final FirstChildNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::firstChild);
    }

    @Override
    protected void visit(final FollowingNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::following);
    }

    @Override
    protected void visit(final FollowingSiblingNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::followingSibling);
    }

    @Override
    protected void visit(final FunctionNameNodeSelectorParserToken token) {
        this.addName(token, NodeSelectorFunctionName::with, NodeSelectorParserToken::functionName);
    }

    @Override
    protected void visit(final GreaterThanSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final GreaterThanEqualsSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final LessThanSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final LessThanEqualsSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final LastChildNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::lastChild);
    }

    @Override
    protected void visit(final MinusSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ModuloSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final MultiplySymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeNameNodeSelectorParserToken token) {
        this.addName(token, NodeSelectorNodeName::with, NodeSelectorParserToken::nodeName);
    }

    @Override
    protected void visit(final NotEqualsSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ExpressionNumberNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final OrSymbolNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::orSymbol);
    }

    @Override
    protected void visit(final ParameterSeparatorSymbolNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::parameterSeparatorSymbol);
    }

    @Override
    protected void visit(final ParenthesisOpenSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ParenthesisCloseSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final ParentOfNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::parentOf);
    }

    @Override
    protected void visit(final PlusSymbolNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::plusSymbol);
    }

    @Override
    protected void visit(final PrecedingNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::preceding);
    }

    @Override
    protected void visit(final PrecedingSiblingNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::precedingSibling);
    }

    @Override
    protected void visit(final QuotedTextNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::quotedText);
    }

    @Override
    protected void visit(final SelfNodeSelectorParserToken token) {
        this.add(token, NodeSelectorParserToken::self);
    }

    @Override
    protected void visit(final SlashSeparatorSymbolNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final WhitespaceNodeSelectorParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final WildcardNodeSelectorParserToken token) {
        this.add(token);
    }

    // GENERAL PURPOSE .................................................................................................

    private Visiting enter() {
        this.previousChildren = this.previousChildren.push(this.children);
        this.children = Lists.array();

        return Visiting.CONTINUE;
    }

    private void exit(final BiFunction<List<ParserToken>, String, NodeSelectorParserToken> factory) {
        final List<ParserToken> children = this.children;
        this.children = this.previousChildren.peek();
        this.previousChildren = this.previousChildren.pop();
        this.add(factory.apply(children, ParserToken.text(children)));
    }

    private <T extends LeafNodeSelectorParserToken<String>> void add(final T token,
                                                                     final BiFunction<String, String, T> factory) {
        this.add(factory.apply(token.value().toUpperCase(), token.text().toUpperCase()));
    }

    private <T extends LeafNodeSelectorParserToken<N>, N extends Name> void addName(final T token,
                                                                                    final Function<String, N> nameFactory,
                                                                                    final BiFunction<N, String, T> parserTokenFactory) {
        this.add(parserTokenFactory.apply(
            nameFactory.apply(token.value().value().toUpperCase()),
            token.text().toUpperCase()));
    }

    private void add(final NodeSelectorParserToken token) {
        if (null == token) {
            throw new NullPointerException("Null token returned");
        }
        this.children.add(token);
    }

    private Stack<List<ParserToken>> previousChildren = Stacks.arrayList();

    private List<ParserToken> children = Lists.array();

    @Override
    public String toString() {
        return this.children + "," + this.previousChildren;
    }
}
