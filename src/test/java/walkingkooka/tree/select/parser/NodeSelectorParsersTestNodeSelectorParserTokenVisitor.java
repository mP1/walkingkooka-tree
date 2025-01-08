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
    protected Visiting startVisit(final NodeSelectorAdditionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorAdditionParserToken token) {
        this.exit(NodeSelectorParserToken::addition);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorAndParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorAndParserToken token) {
        this.exit(NodeSelectorParserToken::and);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorAttributeParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorAttributeParserToken token) {
        this.exit(NodeSelectorParserToken::attribute);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorDivisionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorDivisionParserToken token) {
        this.exit(NodeSelectorParserToken::division);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorEqualsParserToken token) {
        this.exit(NodeSelectorParserToken::equalsParserToken);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorExpressionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorExpressionParserToken token) {
        this.exit(NodeSelectorParserToken::expression);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorFunctionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorFunctionParserToken token) {
        this.exit(NodeSelectorParserToken::function);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGreaterThanParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorGreaterThanParserToken token) {
        this.exit(NodeSelectorParserToken::greaterThan);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorGreaterThanEqualsParserToken token) {
        this.exit(NodeSelectorParserToken::greaterThanEquals);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorGroupParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorGroupParserToken token) {
        this.exit(NodeSelectorParserToken::group);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorLessThanParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorLessThanParserToken token) {
        this.exit(NodeSelectorParserToken::lessThan);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorLessThanEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorLessThanEqualsParserToken token) {
        this.exit(NodeSelectorParserToken::lessThanEquals);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorNotEqualsParserToken token) {
        return this.enter();
    }

    @Override
    protected Visiting startVisit(final NodeSelectorModuloParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorModuloParserToken token) {
        this.exit(NodeSelectorParserToken::modulo);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorMultiplicationParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorMultiplicationParserToken token) {
        this.exit(NodeSelectorParserToken::multiplication);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorNegativeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final NodeSelectorNegativeParserToken token) {
        this.exit(NodeSelectorParserToken::negative);
    }

    @Override
    protected void endVisit(final NodeSelectorNotEqualsParserToken token) {
        this.exit(NodeSelectorParserToken::notEquals);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorOrParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorOrParserToken token) {
        this.exit(NodeSelectorParserToken::or);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorPredicateParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorPredicateParserToken token) {
        this.exit(NodeSelectorParserToken::predicate);
    }

    @Override
    protected Visiting startVisit(final NodeSelectorSubtractionParserToken token) {
        return this.enter();
    }

    @Override
    protected void endVisit(final NodeSelectorSubtractionParserToken token) {
        this.exit(NodeSelectorParserToken::subtraction);
    }

    @Override
    protected void visit(final NodeSelectorAbsoluteParserToken token) {
        this.add(token, NodeSelectorParserToken::absolute);
    }

    @Override
    protected void visit(final NodeSelectorAncestorParserToken token) {
        this.add(token, NodeSelectorParserToken::ancestor);
    }

    @Override
    protected void visit(final NodeSelectorAncestorOrSelfParserToken token) {
        this.add(token, NodeSelectorParserToken::ancestorOrSelf);
    }

    @Override
    protected void visit(final NodeSelectorAndSymbolParserToken token) {
        this.add(token, NodeSelectorParserToken::andSymbol);
    }

    @Override
    protected void visit(final NodeSelectorAtSignSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorAttributeNameParserToken token) {
        this.addName(token, NodeSelectorAttributeName::with, NodeSelectorParserToken::attributeName);
    }

    @Override
    protected void visit(final NodeSelectorBracketOpenSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorBracketCloseSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorChildParserToken token) {
        this.add(token, NodeSelectorParserToken::child);
    }

    @Override
    protected void visit(final NodeSelectorDescendantParserToken token) {
        this.add(token, NodeSelectorParserToken::descendant);
    }

    @Override
    protected void visit(final NodeSelectorDescendantOrSelfParserToken token) {
        this.add(token, NodeSelectorParserToken::descendantOrSelf);
    }

    @Override
    protected void visit(final NodeSelectorDivideSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorEqualsSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorFirstChildParserToken token) {
        this.add(token, NodeSelectorParserToken::firstChild);
    }

    @Override
    protected void visit(final NodeSelectorFollowingParserToken token) {
        this.add(token, NodeSelectorParserToken::following);
    }

    @Override
    protected void visit(final NodeSelectorFollowingSiblingParserToken token) {
        this.add(token, NodeSelectorParserToken::followingSibling);
    }

    @Override
    protected void visit(final NodeSelectorFunctionNameParserToken token) {
        this.addName(token, NodeSelectorFunctionName::with, NodeSelectorParserToken::functionName);
    }

    @Override
    protected void visit(final NodeSelectorGreaterThanSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorGreaterThanEqualsSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorLessThanSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorLessThanEqualsSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorLastChildParserToken token) {
        this.add(token, NodeSelectorParserToken::lastChild);
    }

    @Override
    protected void visit(final NodeSelectorMinusSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorModuloSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorMultiplySymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorNodeNameParserToken token) {
        this.addName(token, NodeSelectorNodeName::with, NodeSelectorParserToken::nodeName);
    }

    @Override
    protected void visit(final NodeSelectorNotEqualsSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorExpressionNumberParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorOrSymbolParserToken token) {
        this.add(token, NodeSelectorParserToken::orSymbol);
    }

    @Override
    protected void visit(final NodeSelectorParameterSeparatorSymbolParserToken token) {
        this.add(token, NodeSelectorParserToken::parameterSeparatorSymbol);
    }

    @Override
    protected void visit(final NodeSelectorParenthesisOpenSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorParenthesisCloseSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorParentOfParserToken token) {
        this.add(token, NodeSelectorParserToken::parentOf);
    }

    @Override
    protected void visit(final NodeSelectorPlusSymbolParserToken token) {
        this.add(token, NodeSelectorParserToken::plusSymbol);
    }

    @Override
    protected void visit(final NodeSelectorPrecedingParserToken token) {
        this.add(token, NodeSelectorParserToken::preceding);
    }

    @Override
    protected void visit(final NodeSelectorPrecedingSiblingParserToken token) {
        this.add(token, NodeSelectorParserToken::precedingSibling);
    }

    @Override
    protected void visit(final NodeSelectorQuotedTextParserToken token) {
        this.add(token, NodeSelectorParserToken::quotedText);
    }

    @Override
    protected void visit(final NodeSelectorSelfParserToken token) {
        this.add(token, NodeSelectorParserToken::self);
    }

    @Override
    protected void visit(final NodeSelectorSlashSeparatorSymbolParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorWhitespaceParserToken token) {
        this.add(token);
    }

    @Override
    protected void visit(final NodeSelectorWildcardParserToken token) {
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

    private <T extends NodeSelectorLeafParserToken<String>> void add(final T token,
                                                                     final BiFunction<String, String, T> factory) {
        this.add(factory.apply(token.value().toUpperCase(), token.text().toUpperCase()));
    }

    private <T extends NodeSelectorLeafParserToken<N>, N extends Name> void addName(final T token,
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
