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
import walkingkooka.stream.push.PushableStreamConsumer;
import walkingkooka.text.CharacterConstant;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;
import walkingkooka.tree.select.parser.NodeSelectorNodeName;
import walkingkooka.tree.select.parser.NodeSelectorParserToken;
import walkingkooka.visit.Visitable;
import walkingkooka.visit.Visiting;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A select maybe used to select zero or more nodes within a tree given a {@link Node}.
 */
public abstract class NodeSelector<N extends Node<N, NAME, ANAME, AVALUE>,
    NAME extends Name,
    ANAME extends Name,
    AVALUE> implements Visitable {

    /**
     * Path separator
     */
    final static CharacterConstant SEPARATOR = CharacterConstant.with('/');

    /**
     * Creates a {@link NodeSelector} from a {@link NodeSelectorParserToken}.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> parserToken(final NodeSelectorExpressionParserToken token,
                                                                 final Function<NodeSelectorNodeName, NAME> nameFactory,
                                                                 final Predicate<ExpressionFunctionName> functions,
                                                                 final Class<N> nodeType) {

        return NodeSelectorNodeSelectorParserTokenVisitor.with(token, nameFactory, functions, nodeType);
    }

    /**
     * All index or positions in xpath are ONE based not ZERO.
     */
    public final static int INDEX_BIAS = 1;

    // static factories ......................................................................................

    /**
     * Creates a {@link NodeSelector} that will begin with the root of the {@link Node}.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> absolute() {
        return AbsoluteNodeSelector.get();
    }

    /**
     * Creates a selector which matches any given {@link Node}.
     */
    public static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> relative() {
        return TerminalNodeSelector.get();
    }

    /**
     * Creates a {@link TerminalNodeSelector}.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> NodeSelector<N, NAME, ANAME, AVALUE> terminal() {
        return TerminalNodeSelector.get();
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelector() {
    }

    // builder...........................................................................................................

    /**
     * Appends an ancestor axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> ancestor() {
        return this.append(AncestorNodeSelector.get());
    }

    /**
     * Appends an ancestor-or-self axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> ancestorOrSelf() {
        return this.append(AncestorOrSelfNodeSelector.get());
    }

    /**
     * Appends an attribute value contains test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueContains(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueContainsPredicate.contains(name, value));
    }

    /**
     * Appends an attribute value ends with test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueEndsWith(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueEndsWithPredicate.endsWith(name, value));
    }

    /**
     * Appends an attribute value equals test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueEquals(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueEqualsPredicate.equalsPredicate(name, value));
    }

    /**
     * Appends an attribute value starts with test.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> attributeValueStartsWith(final ANAME name, final AVALUE value) {
        return this.predicate(NodeSelectorNodeAttributeValueStartsWithPredicate.startsWith(name, value));
    }

    /**
     * Appends a child axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> children() {
        return this.append(ChildrenNodeSelector.get());
    }

    /**
     * Appends a descendant axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> descendant() {
        return this.append(DescendantNodeSelector.get());
    }

    /**
     * Appends a descendant-or-self axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> descendantOrSelf() {
        return this.append(DescendantOrSelfNodeSelector.get());
    }

    /**
     * Appends an expression
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> expression(final Expression expression) {
        return NodeSelectorExpressionExpressionVisitor.acceptExpression(expression, this);
    }

    /**
     * Appends a first-child axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> firstChild() {
        return this.append(FirstChildNodeSelector.get());
    }

    /**
     * Appends a following axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> following() {
        return this.append(FollowingNodeSelector.get());
    }

    /**
     * Appends a following-sibling axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> followingSibling() {
        return this.append(FollowingSiblingNodeSelector.get());
    }

    /**
     * Appends a last-child
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> lastChild() {
        return this.append(LastChildNodeSelector.get());
    }

    /**
     * Appends a name test
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> named(final NAME name) {
        return this.append(NamedNodeSelector.with(name));
    }

    /**
     * Appends a parent axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> parent() {
        return this.append(ParentNodeSelector.get());
    }

    /**
     * Appends a preceding axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> preceding() {
        return this.append(PrecedingNodeSelector.get());
    }

    /**
     * Appends a preceding-sibling axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> precedingSibling() {
        return this.append(PrecedingSiblingNodeSelector.get());
    }

    /**
     * Appends a predicate.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> predicate(final Predicate<N> predicate) {
        return this.append(NodePredicateNodeSelector.with(predicate));
    }

    /**
     * Appends a self axis
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> self() {
        return this.append(SelfNodeSelector.get());
    }

    final NodeSelector<N, NAME, ANAME, AVALUE> append(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        Objects.requireNonNull(selector, "select");

        return this.append0(selector);
    }

    abstract NodeSelector<N, NAME, ANAME, AVALUE> append0(final NodeSelector<N, NAME, ANAME, AVALUE> selector);

    // apply...........................................................................................................

    /**
     * Accepts a starting {@link Node} anywhere in a tree returning all matching nodes.
     * The {@link Consumer} is invoked for each and every {@link Node} prior to any test and continued traversal. It may be
     * used to abort the visiting process by throwing an {@link RuntimeException}
     */
    final public N apply(final N node, final NodeSelectorContext<N, NAME, ANAME, AVALUE> context) {
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(context, "context");

        return !context.isFinished() && context.test(node) ?
            this.apply0(node, NodeSelectorContext2.all(context)) :
            node;
    }

    /**
     * Sub classes must call this method which calls the observer and then immediately calls {@link #apply1(Node, NodeSelectorContext2)}
     * This method assumes that {@link NodeSelectorContext#test(Node)} was previously called for the given {@link Node}.
     */
    final N apply0(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return this.apply1(node, this.beginPrepareContext(context));
    }

    /**
     * Sub classes except for {@link ExpressionNodeSelector} call next#finishPrepareContext
     */
    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> beginPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context);

    abstract NodeSelectorContext2<N, NAME, ANAME, AVALUE> finishPrepareContext(final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context);

    /**
     * Sub classes must implement this to contain the core logic in testing if a node is actually selected.
     */
    abstract N apply1(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context);

    // select...........................................................................................................

    /**
     * Selects all preceding siblings of the given {@link Node}.
     */
    final N selectPrecedingSiblings(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        N result = node;

        final Optional<N> parent = node.parent();

        if (parent.isPresent()) {
            final int index = node.index();
            N current = node;

            for (; ; ) {
                final Optional<N> next = current.previousSibling();
                if (!next.isPresent()) {
                    result = current.parentOrFail().children().get(index);
                    break;
                }
                current = this.testThenSelect(next.get(), context);

                if (context.isFinished()) {
                    result = current.parentOrFail().children().get(index);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Selects all following siblings of the given {@link Node}.
     */
    final N selectFollowingSiblings(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        N result = node;

        final Optional<N> parent = node.parent();
        if (parent.isPresent()) {
            final int index = node.index();
            N current = node;

            for (; ; ) {
                final Optional<N> next = current.nextSibling();
                if (!next.isPresent()) {
                    result = current.parentOrFail().children().get(index);
                    break;
                }
                current = this.testThenSelect(next.get(), context);

                if (context.isFinished()) {
                    result = current.parentOrFail().children().get(index);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Selects all direct children of the given {@link Node node}.`
     */
    final N selectChildren(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        N result = node;

        Optional<N> next = node.firstChild();
        if (next.isPresent()) {
            for (; ; ) {
                final N nextNode = next.get();
                if (context.isFinished()) {
                    result = nextNode.parentOrFail();
                    break;
                }

                result = this.testThenSelect(nextNode, context);
                next = result.nextSibling();
                if (!next.isPresent()) {
                    result = result.parentOrFail();
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Matches the parent only if one is present.
     */
    final N selectParent(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return context.isFinished() ?
            node :
            node.parent()
                .map(parent -> this.testThenSelect(parent, context).children().get(node.index()))
                .orElse(node);
    }

    /**
     * Perform a conditional predicate test of the provided {@link Node} and if that passes calls {@link #select(Node, NodeSelectorContext2)}.
     */
    final N testThenSelect(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context) {
        return context.test(node) ?
            this.select(node, context) :
            node;
    }

    /**
     * Handles a selected {@link Node}
     */
    abstract N select(final N node, final NodeSelectorContext2<N, NAME, ANAME, AVALUE> context);

    // NodeSelectorVisitor..............................................................................................

    /**
     * Begins visiting with this {@link NodeSelector} using the given {@link NodeSelectorVisitor}
     */
    public final void accept(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        Objects.requireNonNull(visitor, "visitor");

        visitor.traverse(this);
    }

    final void traverse(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.accept0(visitor);
        }
        visitor.endVisit(this);
    }

    abstract void accept0(final NodeSelectorVisitor<N, NAME, ANAME, AVALUE> visitor);

    // Stream...........................................................................................................

    /**
     * Returns a stream which will execute this selector starting with the given {@link Node} and then push matches to the
     * {@link Stream} for further stream processing.
     */
    public final <C extends ExpressionNumberConverterContext> Stream<N> stream(final N node,
                                                                               final Function<NodeSelectorContext<N, NAME, ANAME, AVALUE>, ExpressionEvaluationContext> expressionEvaluationContext,
                                                                               final Class<N> nodeType) {
        return PushableStreamConsumer.stream(
            NodeSelectorStreamConsumerPushableStreamConsumer.with(node,
                this,
                expressionEvaluationContext,
                nodeType));
    }

    // Object...........................................................................................................

    /**
     * Force sub classes to implement.
     */
    @Override
    public final String toString() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        this.toString0(b);
        return b.build();
    }

    abstract void toString0(final NodeSelectorToStringBuilder b);

    /**
     * Returns a {@link NodeSelector} with the given {@link String toString}, without changing functionality.
     */
    public final NodeSelector<N, NAME, ANAME, AVALUE> setToString(final String toString) {
        return CustomToStringNodeSelector.with(this, toString);
    }

    /**
     * All sub classes except for {@link CustomToStringNodeSelector} return this.
     */
    abstract NodeSelector<N, NAME, ANAME, AVALUE> unwrapIfCustomToStringNodeSelector();
}
