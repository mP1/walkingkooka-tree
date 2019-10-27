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

import walkingkooka.InvalidTextLengthException;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

/**
 * The {@link Name} of a node.
 */
final public class NodeSelectorNodeName extends NodeSelectorNameValue implements Comparable<NodeSelectorNodeName> {

    final static CharPredicate INITIAL = CharPredicates.range('A', 'Z').or(CharPredicates.range('a', 'z'));

    final static CharPredicate PART = INITIAL.or(CharPredicates.range('0', '9').or(CharPredicates.is('-')));

    final static int MAX_LENGTH = 255;

    /**
     * Factory that creates a {@link NodeSelectorNodeName}
     */
    public static NodeSelectorNodeName with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(name, NodeSelectorNodeName.class.getSimpleName(), INITIAL, PART);

        if (name.length() > MAX_LENGTH) {
            throw new InvalidTextLengthException("NodeName", name, 0, MAX_LENGTH);
        }

        return new NodeSelectorNodeName(name);
    }

    /**
     * Private constructor
     */
    private NodeSelectorNodeName(final String name) {
        super(name);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorNodeName;
    }

    @Override
    public int compareTo(final NodeSelectorNodeName other) {
        return this.compareTo0(other);
    }
}
