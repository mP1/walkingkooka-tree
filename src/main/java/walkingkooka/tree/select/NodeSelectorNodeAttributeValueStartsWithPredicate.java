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
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;

/**
 * A {@link java.util.function.Predicate} that returns true if an attribute value starts with the given test value.
 */
final class NodeSelectorNodeAttributeValueStartsWithPredicate<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    extends NodeSelectorNodeAttributeValuePredicate<N, NAME, ANAME, AVALUE> {

    static <N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE>
    NodeSelectorNodeAttributeValueStartsWithPredicate<N, NAME, ANAME, AVALUE> with(final ANAME name, final AVALUE value) {
        return new NodeSelectorNodeAttributeValueStartsWithPredicate<>(name, value);
    }

    private NodeSelectorNodeAttributeValueStartsWithPredicate(final ANAME name,
                                                              final AVALUE value) {
        super(name, value);
    }

    @Override
    boolean testNonAttributeValue(final AVALUE value,
                                  final AVALUE current) {
        return current.toString().startsWith(value.toString());
    }

    @Override
    boolean isSameType(final Object other) {
        return other instanceof NodeSelectorNodeAttributeValueStartsWithPredicate;
    }

    @Override
    String toString0(final ANAME name, final AVALUE value) {
        //[starts-with(@href, '/')]
        return "starts-with(@" + name.value() + "," + CharSequences.quoteIfChars(value) + ")";
    }
}
