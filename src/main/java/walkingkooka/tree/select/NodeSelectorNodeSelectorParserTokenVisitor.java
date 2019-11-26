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

package walkingkooka.tree.select;

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.select.parser.NodeSelectorAbsoluteParserToken;
import walkingkooka.tree.select.parser.NodeSelectorAncestorOrSelfParserToken;
import walkingkooka.tree.select.parser.NodeSelectorAncestorParserToken;
import walkingkooka.tree.select.parser.NodeSelectorChildParserToken;
import walkingkooka.tree.select.parser.NodeSelectorDescendantOrSelfParserToken;
import walkingkooka.tree.select.parser.NodeSelectorDescendantParserToken;
import walkingkooka.tree.select.parser.NodeSelectorFirstChildParserToken;
import walkingkooka.tree.select.parser.NodeSelectorFollowingParserToken;
import walkingkooka.tree.select.parser.NodeSelectorFollowingSiblingParserToken;
import walkingkooka.tree.select.parser.NodeSelectorLastChildParserToken;
import walkingkooka.tree.select.parser.NodeSelectorNodeName;
import walkingkooka.tree.select.parser.NodeSelectorNodeNameParserToken;
import walkingkooka.tree.select.parser.NodeSelectorParentOfParserToken;
import walkingkooka.tree.select.parser.NodeSelectorParserToken;
import walkingkooka.tree.select.parser.NodeSelectorParserTokenVisitor;
import walkingkooka.tree.select.parser.NodeSelectorPrecedingParserToken;
import walkingkooka.tree.select.parser.NodeSelectorPrecedingSiblingParserToken;
import walkingkooka.tree.select.parser.NodeSelectorPredicateParserToken;
import walkingkooka.tree.select.parser.NodeSelectorSelfParserToken;
import walkingkooka.tree.select.parser.NodeSelectorSlashSeparatorSymbolParserToken;
import walkingkooka.tree.select.parser.NodeSelectorWildcardParserToken;
import walkingkooka.visit.Visiting;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This {@link NodeSelectorParserTokenVisitor} helps convert a {@link NodeSelectorParserToken} into a {@link NodeSelector}.
 */
final class NodeSelectorNodeSelectorParserTokenVisitor<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        extends NodeSelectorParserTokenVisitor {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name, AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> with(final NodeSelectorParserToken token,
                                                                                  final Function<NodeSelectorNodeName, NAME> nameFactory,
                                                                                  final Predicate<FunctionExpressionName> functions,
                                                                                  final Class<N> node) {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(nameFactory, "nameFactory");
        Objects.requireNonNull(functions, "functions");
        Objects.requireNonNull(node, "node");

        return new NodeSelectorNodeSelectorParserTokenVisitor<>(nameFactory,
                functions,
                node).acceptAndComplete(token);
    }

    /**
     * Private ctor use static factory
     */
    // @VisibleForTesting
    NodeSelectorNodeSelectorParserTokenVisitor(final Function<NodeSelectorNodeName, NAME> nameFactory,
                                               final Predicate<FunctionExpressionName> functions,
                                               final Class<N> node) {
        super();

        this.nameFactory = nameFactory;
        this.functions = functions;
        this.prepare();
    }

    private NodeSelector<N, NAME, ANAME, AVALUE> acceptAndComplete(final NodeSelectorParserToken token) {
        this.accept(token);

        if (this.wildcard && this.axis) {
            this.update(this.selector.children());
        }

        return this.selector.setToString(token.text());
    }

    @Override
    protected Visiting startVisit(final NodeSelectorPredicateParserToken token) {
        this.childrenIfAxis();
        this.test(this.selector.expression(token.toExpression(this.functions)));
        return Visiting.SKIP;
    }

    private final Predicate<FunctionExpressionName> functions;


    // Leaf tokens...........................................................................................

    @Override
    protected void visit(final NodeSelectorSlashSeparatorSymbolParserToken token) {
        this.prepare();
    }

    @Override
    protected void visit(final NodeSelectorAbsoluteParserToken token) {
        this.selector = NodeSelector.absolute();
        this.prepare();
    }

    @Override
    protected void visit(final NodeSelectorAncestorParserToken token) {
        this.axis(this.selector.ancestor());
    }

    @Override
    protected void visit(final NodeSelectorAncestorOrSelfParserToken token) {
        this.axis(this.selector.ancestorOrSelf());
    }

    @Override
    protected void visit(final NodeSelectorChildParserToken token) {
        this.axis(this.selector.children());
    }

    @Override
    protected void visit(final NodeSelectorDescendantParserToken token) {
        this.axis(this.selector.descendant());
    }

    @Override
    protected void visit(final NodeSelectorDescendantOrSelfParserToken token) {
        this.axis(this.selector.descendantOrSelf());
    }

    @Override
    protected void visit(final NodeSelectorFirstChildParserToken token) {
        this.axis(this.selector.firstChild());
    }

    @Override
    protected void visit(final NodeSelectorFollowingParserToken token) {
        this.axis(this.selector.following());
    }

    @Override
    protected void visit(final NodeSelectorFollowingSiblingParserToken token) {
        this.axis(this.selector.followingSibling());
    }

    @Override
    protected void visit(final NodeSelectorLastChildParserToken token) {
        this.axis(this.selector.lastChild());
    }

    @Override
    protected void visit(final NodeSelectorNodeNameParserToken token) {
        this.childrenIfAxis();
        this.test(this.selector.named(this.nameFactory.apply(token.value())));
    }

    @Override
    protected void visit(final NodeSelectorParentOfParserToken token) {
        this.axis(this.selector.parent());
    }

    @Override
    protected void visit(final NodeSelectorPrecedingParserToken token) {
        this.axis(this.selector.preceding());
    }

    @Override
    protected void visit(final NodeSelectorPrecedingSiblingParserToken token) {
        this.axis(this.selector.precedingSibling());
    }

    @Override
    protected void visit(final NodeSelectorSelfParserToken token) {
        this.axis(this.selector.self());
    }

    @Override
    protected void visit(final NodeSelectorWildcardParserToken token) {
        this.wildcard = this.axis;
    }

    private void prepare() {
        this.axis = true;
        this.wildcard = false;
    }

    private void childrenIfAxis() {
        if (this.axis) {
            this.update(this.selector.children());
        }
    }

    private void axis(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        this.update(selector);
        this.axis = false;
    }

    private void test(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        this.update(selector);
        this.axis = false;
    }

    private void update(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        this.selector = selector;
    }

    /**
     * Factory used to create a {@link Name} from a {@link NodeSelectorNodeName}.
     */
    private final Function<NodeSelectorNodeName, NAME> nameFactory;

    /**
     * When true indicates expecting an axis.
     */
    private boolean axis;

    /**
     * True for a trailing wildcard
     */
    private boolean wildcard;

    /**
     * Builds the selector.
     */
    private NodeSelector<N, NAME, ANAME, AVALUE> selector = NodeSelector.relative();

    @Override
    public String toString() {
        return this.selector.toString();
    }
}
