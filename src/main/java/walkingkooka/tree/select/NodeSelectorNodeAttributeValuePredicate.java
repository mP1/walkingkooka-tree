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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.Objects;

/**
 * A base attribute that tests an existing attribute value with an expected value.
 */
abstract class NodeSelectorNodeAttributeValuePredicate<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
        implements java.util.function.Predicate<N> {

    /**
     * {@see NodeSelectorNodeAttributeValueContainsPredicate}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodeSelectorNodeAttributeValueContainsPredicate<N, NAME, ANAME, AVALUE> contains(final ANAME name, final AVALUE value) {
        return NodeSelectorNodeAttributeValueContainsPredicate.with(name, value);
    }

    /**
     * {@see NodeSelectorNodeAttributeValueEndsWithPredicate}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodeSelectorNodeAttributeValueEndsWithPredicate<N, NAME, ANAME, AVALUE> endsWith(final ANAME name, final AVALUE value) {
        return NodeSelectorNodeAttributeValueEndsWithPredicate.with(name, value);
    }

    /**
     * {@see NodeSelectorNodeAttributeValueEqualsPredicate}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodeSelectorNodeAttributeValueEqualsPredicate<N, NAME, ANAME, AVALUE> equalsPredicate(final ANAME name, final AVALUE value) {
        return NodeSelectorNodeAttributeValueEqualsPredicate.with(name, value);
    }

    /**
     * {@see NodeSelectorNodeAttributeValueStartsWithPredicate}
     */
    static <N extends Node<N, NAME, ANAME, AVALUE>,
            NAME extends Name,
            ANAME extends Name,
            AVALUE>
    NodeSelectorNodeAttributeValueStartsWithPredicate<N, NAME, ANAME, AVALUE> startsWith(final ANAME name, final AVALUE value) {
        return NodeSelectorNodeAttributeValueStartsWithPredicate.with(name, value);
    }

    /**
     * Package private to limit sub classing.
     */
    NodeSelectorNodeAttributeValuePredicate(final ANAME name, final AVALUE value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        this.name = name;
        this.value = value;
    }

    private final ANAME name;
    private final AVALUE value;

    @Override
    public final boolean test(final N node) {
        final AVALUE current = node.attributes().get(this.name);
        return null != current && this.test0(this.value, current);
    }

    abstract boolean test0(final AVALUE value, final AVALUE current);

    @Override
    public final int hashCode() {
        return Objects.hash(this.name, this.value);
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public final boolean equals(final Object other) {
        return this == other || this.isSameType(other) && this.equals0(Cast.to(other));
    }

    abstract boolean isSameType(final Object other);

    final boolean equals0(final NodeSelectorNodeAttributeValuePredicate<?, ?, ?, ?> predicate) {
        return this.name.equals(predicate.name) && this.value.equals(predicate.value);
    }

    @Override
    public final String toString() {
        return this.toString0(this.name, this.value);
    }

    abstract String toString0(final ANAME name, final AVALUE value);
}
