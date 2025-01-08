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

package walkingkooka.tree;


import org.junit.jupiter.api.Assertions;
import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.naming.Names;
import walkingkooka.naming.PathSeparator;
import walkingkooka.naming.StringName;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A basic {@link Node} with some simplifications. Note that all node names must be unique and that children have their
 * parent and index properties modified when they get adopted. These considerations should be acocunted for in tests.
 */
public final class TestNode implements Node<TestNode, StringName, StringName, Object> {

    /**
     * Should be called before each and every test.
     */
    public static void clear() {
        names.clear();
        UNIQUE_NAMES_CHECK = true;
    }

    /**
     * Turns back assertions OFF so {@link TestNode} can be duplicates.
     */
    public static void disableUniqueNameChecks() {
        UNIQUE_NAMES_CHECK = false;
    }

    /**
     * Turns back assertions ON so that all {@link TestNode} names must be unique.
     */
    public static void enableUniqueNameChecks() {
        UNIQUE_NAMES_CHECK = true;
    }

    /**
     * The {@link PathSeparator} for node selector paths.
     */
    @SuppressWarnings("WeakerAccess")
    public static final PathSeparator PATH_SEPARATOR = PathSeparator.requiredAtStart('/');

    @SuppressWarnings("WeakerAccess")
    public final static Optional<TestNode> NO_PARENT = Optional.empty();

    public static TestNode with(final String name, final TestNode... children) {
        if (UNIQUE_NAMES_CHECK && !names.add(name)) {
            Assertions.fail("Name " + CharSequences.quote(name) + " must be unique per test");
        }

        return new TestNode(Names.string(name),
            NO_PARENT,
            copyChildren(Lists.of(children)),
            Maps.empty());
    }

    /**
     * Used to keep track of all node names, preventing duplicates.
     */
    private static final Set<String> names = SortedSets.tree();

    /**
     * When true all new {@link TestNode#with(String, TestNode...)} names must be unique otherwise an assertion will fail.
     */
    private static boolean UNIQUE_NAMES_CHECK = true;

    private TestNode(final StringName name,
                     final Optional<TestNode> parent,
                     final List<TestNode> children,
                     final Map<StringName, Object> attributes) {
        super();
        this.name = name;
        this.parent = parent;
        this.attributes = attributes;
        this.children = children;

        int i = 0;
        for (TestNode child : children) {
            child.parent = Optional.of(this);
            child.index = i;
            i++;
        }
    }

    public StringName name() {
        return this.name;
    }

    private final StringName name;

    @Override
    public boolean hasUniqueNameAmongstSiblings() {
        return true;
    }

    @Override
    public Optional<TestNode> parent() {
        return this.parent;
    }

    @SuppressWarnings("CanBeFinal")
    Optional<TestNode> parent;

    @Override
    public TestNode removeParent() {
        return this.parent
            .map(p -> new TestNode(this.name, NO_PARENT, copyChildren(children), this.attributes))
            .orElse(this);
    }

    @Override
    public int index() {
        return this.index;
    }

    private int index = -1;

    @Override
    public List<TestNode> children() {
        return this.children;
    }

    @Override
    public TestNode setChildren(final List<TestNode> children) {
        Objects.requireNonNull(children, "children");

        return this.children.equals(children) ?
            this :
            new TestNode(this.name, NO_PARENT, copyChildren(children), this.attributes)
                .replace(this.parent, this.index);
    }

    public TestNode child(final int i) {
        return this.children().get(i);
    }

    /**
     * It is necessary to take copies of children because mutations will reset in the parent being set again for the new graph.
     */
    private static List<TestNode> copyChildren(final List<TestNode> children) {
        return Lists.immutable(children.stream()
            .map(TestNode::copy)
            .collect(Collectors.toList())
        );
    }

    private TestNode copy() {
        return new TestNode(this.name,
            NO_PARENT,
            copyChildren(this.children),
            this.attributes);
    }

    private final List<TestNode> children;

    private TestNode replace(final Optional<TestNode> previousParent, final int index) {
        return previousParent.map(p -> p.setChild(index, this).child(index))
            .orElse(this);
    }

    /**
     * Removes a child node with the given {@link StringName} or fails with a {@link IllegalArgumentException}.
     */
    public TestNode removeChild(final StringName name) {
        final List<TestNode> removed = this.children()
            .stream()
            .filter(c -> false == c.name().equals(name))
            .collect(Collectors.toList());
        if (removed.size() == this.children().size()) {
            throw new IllegalArgumentException("Child with name " + name + " missing");
        }
        return this.setChildren(removed);
    }

    @Override
    public Map<StringName, Object> attributes() {
        return this.attributes;
    }

    @Override
    public TestNode setAttributes(final Map<StringName, Object> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        final Map<StringName, Object> copy = Maps.immutable(attributes);
        return this.attributes.equals(copy) ?
            this :
            new TestNode(this.name, NO_PARENT, copyChildren(this.children), copy)
                .replace(this.parent, this.index);
    }

    private final Map<StringName, Object> attributes;

    public int hashCode() {
        return Objects.hash(this.name(), this.attributes());
    }

    public boolean equals(final Object other) {
        return this == other || other instanceof TestNode && equals0((TestNode) other);
    }

    private boolean equals0(final TestNode other) {
        return this.name().equals(other.name()) &&
            this.equalsParent(other.parent()) &&
            this.equalsChildren(other.children()) &&
            this.attributes().equals(other.attributes());
    }

    // check all properties, except for children of parent...
    private boolean equalsParent(final Optional<TestNode> other) {
        boolean equals = false;

        final Optional<TestNode> maybeParent = this.parent();
        if (parent.isPresent()) {
            if (other.isPresent()) {
                final TestNode parent = maybeParent.get();
                final TestNode otherParent = other.get();

                // skip checking children...
                equals = parent.name().equals(otherParent.name()) &&
                    parent.equalsParent(otherParent.parent()) &&
                    parent.attributes().equals(otherParent.attributes());
            }
        } else {
            // both should have no parent.
            equals = !other.isPresent();
        }

        return equals;
    }

    // check all properties of children except for parent.
    private boolean equalsChildren(final List<TestNode> otherChildren) {
        boolean equals = false;

        final List<TestNode> children = this.children();
        if (children.size() == otherChildren.size()) {
            final Iterator<TestNode> i = children.iterator();
            final Iterator<TestNode> j = otherChildren.iterator();
            equals = true;

            while (equals && i.hasNext()) {
                equals &= i.next().equalsChild(j.next());
            }
        }

        return equals;
    }

    // check all properties of children except for parent.
    private boolean equalsChild(final TestNode other) {
        return this.name().equals(other.name()) &&
            this.equalsChildren(other.children()) &&
            this.attributes().equals(other.attributes());
    }

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.value(this.name());
        b.surroundValues("[", "]").value(this.children());
        b.surroundValues("{", "}").value(this.attributes());
        return b.build();
    }

    // NodeSelector .......................................................................................................

    /**
     * {@see NodeSelector#absolute}
     */
    public static NodeSelector<TestNode, StringName, StringName, Object> absoluteNodeSelector() {
        return NodeSelector.absolute();
    }

    /**
     * {@see NodeSelector#relative}
     */
    public static NodeSelector<TestNode, StringName, StringName, Object> relativeNodeSelector() {
        return NodeSelector.relative();
    }

    /**
     * Creates a {@link NodeSelector} for {@link TestNode} from a {@link NodeSelectorExpressionParserToken}.
     */
    public static NodeSelector<TestNode, StringName, StringName, Object> nodeSelectorExpressionParserToken(final NodeSelectorExpressionParserToken token,
                                                                                                           final Predicate<ExpressionFunctionName> functions) {
        return NodeSelector.parserToken(token,
            n -> Names.string(n.value()),
            functions,
            TestNode.class);
    }
}
