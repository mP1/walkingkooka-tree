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
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.Node;
import walkingkooka.tree.TestNode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class NodeSelectorContext2TestCase<C extends NodeSelectorContext<N, NAME, ANAME, AVALUE>,
        N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements NodeSelectorContextTesting<C,
        N,
        NAME,
        ANAME,
        AVALUE>,
        ClassTesting2<C> {

    NodeSelectorContext2TestCase() {
        super();
    }

    final NodeSelectorContext<TestNode, StringName, StringName, Object> contextWithToString(final String toString) {
        return new FakeNodeSelectorContext<>() {
            @Override
            public String toString() {
                return toString;
            }
        };
    }

    final void checkType(final NodeSelectorContext2<?, ?, ?, ?> context, final Class<? extends NodeSelectorContext2> type) {
        assertEquals(type.getName(), context.getClass().getName(), () -> "" + context + " must be " + type.getName());
    }

    @Override
    public final String typeNameSuffix() {
        return NodeSelectorContext2.class.getSimpleName();
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
