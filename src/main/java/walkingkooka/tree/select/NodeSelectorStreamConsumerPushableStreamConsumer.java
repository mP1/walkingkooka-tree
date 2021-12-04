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
import walkingkooka.predicate.Predicates;
import walkingkooka.stream.push.PushableStreamConsumer;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A {@link Consumer} given to {@link PushableStreamConsumer#stream(Consumer)} which will act as the final bridge between {@link NodeSelector} matches and a {@link Stream}.
 */
final class NodeSelectorStreamConsumerPushableStreamConsumer<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements Consumer<PushableStreamConsumer<N>> {

    /**
     * Factory called by {@link NodeSelector#stream}.
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE,
            C extends ExpressionNumberConverterContext> NodeSelectorStreamConsumerPushableStreamConsumer<N, NAME, ANAME, AVALUE> with(final N node,
                                                                                                                                      final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                                                                                                                      final Function<NodeSelectorContext<N, NAME, ANAME, AVALUE>, ExpressionEvaluationContext> expressionEvaluationContext,
                                                                                                                                      final Class<N> nodeType) {
        Objects.requireNonNull(node, "node");

        return new NodeSelectorStreamConsumerPushableStreamConsumer<N, NAME, ANAME, AVALUE>(node,
                selector,
                expressionEvaluationContext,
                nodeType);
    }

    /**
     * Private ctor
     */
    private NodeSelectorStreamConsumerPushableStreamConsumer(final N node,
                                                             final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                                             final Function<NodeSelectorContext<N, NAME, ANAME, AVALUE>, ExpressionEvaluationContext> expressionEvaluationContext,
                                                             final Class<N> nodeType) {
        super();
        this.selector = selector;
        this.node = node;

        this.context = NodeSelectorContexts.basic(this::finisher,
                Predicates.always(),
                this::mapper,
                expressionEvaluationContext,
                nodeType);
    }

    /**
     * The finisher for the {@link NodeSelectorContext} that always returns false.
     */
    private boolean finisher() {
        return false;
    }

    /**
     * The mapper gives the {@link Node} to the given {@link PushableStreamConsumer} provided its not finished.
     */
    private N mapper(final N node) {
        final PushableStreamConsumer<N> pushableStreamConsumer = this.pushableStreamConsumer;
        if (false == pushableStreamConsumer.isFinished()) {
            pushableStreamConsumer.accept(node);
        }
        return node;
    }

    @Override
    public void accept(final PushableStreamConsumer<N> pushableStreamConsumer) {
        this.pushableStreamConsumer = pushableStreamConsumer;
        this.selector.apply(this.node, this.context);
    }

    private PushableStreamConsumer<N> pushableStreamConsumer;

    /**
     * The selector that will be executed prior to sending matching {@link Node} to the stream.
     */
    private final NodeSelector<N, NAME, ANAME, AVALUE> selector;

    /**
     * The {@link Node} to start the search
     */
    private final N node;

    /**
     * A {@link NodeSelectorContext} used during selector finding.
     */
    private final NodeSelectorContext<N, NAME, ANAME, AVALUE> context;

    @Override
    public String toString() {
        return this.selector.toString();
    }
}
