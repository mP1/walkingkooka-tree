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

package walkingkooka.tree.pointer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.tree.TestNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodePointerTestCase<N extends NodePointer<TestNode, StringName>> implements ClassTesting2<N>,
    HashCodeEqualsDefinedTesting2<N>,
    ToStringTesting<N>,
    TypeNameTesting<N> {

    NodePointerTestCase() {
        super();
    }

    @BeforeEach
    public final void beforeEach() {
        TestNode.clear();
    }

    // add...............................................................................................................

    @Test
    public final void testAddNullNodeFail() {
        assertThrows(NullPointerException.class, () -> this.createNodePointer().add(null, TestNode.with("ignore")));
    }

    @Test
    public final void testAddNullAddNodeFail() {
        assertThrows(NullPointerException.class, () -> this.createNodePointer().add(TestNode.with("ignore"), null));
    }

    // remove...........................................................................................................

    @Test
    public final void testRemoveNullNodeFail() {
        assertThrows(NullPointerException.class, () -> this.createNodePointer().remove(null));
    }

    // next.............................................................................................................

    @Test
    public final void testNextMissing() {
        this.nextAndCheck(this.createNodePointer(), null);
    }

    final void nextAndCheck(final NodePointer<TestNode, StringName> pointer,
                            final NodePointer<TestNode, StringName> next) {
        this.checkEquals(Optional.ofNullable(next),
            pointer.next(),
            () -> " next of " + pointer);
    }

    abstract N createNodePointer();

    @Override
    public final N createObject() {
        return this.createNodePointer();
    }

    final void addAndCheck(final NodePointer<TestNode, StringName> pointer,
                           final TestNode base,
                           final TestNode add,
                           final TestNode result) {
        this.checkEquals(result,
            pointer.add(base, add),
            () -> pointer + " add to " + base + ", " + add);
    }

    final void addAndFail(final NodePointer<TestNode, StringName> pointer,
                          final TestNode base,
                          final TestNode add) {
        assertThrows(NodePointerException.class, () -> pointer.add(base, add));
    }

    final void removeAndCheck(final NodePointer<TestNode, StringName> pointer,
                              final TestNode node,
                              final TestNode result) {
        this.checkEquals(result,
            pointer.remove(node),
            () -> pointer + " remove from " + node);
    }

    final void removeAndFail(final NodePointer<TestNode, StringName> pointer,
                             final TestNode node) {
        assertThrows(NodePointerException.class, () -> pointer.remove(node));
    }

    // ClassTesting......................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return NodePointer.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
