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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ResourceTesting;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class TestNodeTest implements ClassTesting2<TestNode>,
    ParentNodeTesting<TestNode, StringName, StringName, Object>,
    ResourceTesting {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testWith() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        this.childrenParentCheck(TestNode.with("parent", child1, child2));
        this.parentWithoutAndCheck2(child1, child2);
    }

    @Test
    public void testWithChildrenCopied() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        final TestNode parent = TestNode.with("parent", child1, child2);
        final String parentToString = parent.toString();
        this.childrenParentCheck(parent);

        TestNode.clear();

        final TestNode parent2 = TestNode.with("parent2", child1, child2);
        final String parent2ToString = parent2.toString();
        this.childrenParentCheck(parent2);

        this.checkEquals(parentToString, parent.toString(), "parent.toString");
        this.checkEquals(parentToString, parent2ToString.replace("parent2", "parent"), "parent2.toString");

        this.parentWithoutAndCheck2(child1, child2);
    }

    @Test
    public void testWithDuplicateNameFails() {
        final String duplicateName = "duplicate123";
        TestNode.with(duplicateName);

        boolean failed = false;
        try {
            TestNode.with(duplicateName);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(failed, true, "Factory should have failed with duplicate name");
    }

    @Test
    public void testDisableUniqueNameChecks() {
        final String duplicateName = "duplicate123";
        final TestNode first = TestNode.with(duplicateName);

        TestNode.disableUniqueNameChecks();

        this.checkEquals(first, TestNode.with(duplicateName));
    }

    @Test
    public void testClear() {
        final String duplicateName = "duplicate123";
        TestNode.with(duplicateName);

        TestNode.disableUniqueNameChecks();
        TestNode.with(duplicateName);

        TestNode.clear();
        TestNode.with(duplicateName);

        boolean failed = false;
        try {
            TestNode.with(duplicateName);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(failed, true, "Factory should have failed with duplicate name");
    }

    @Test
    public void testEnableUniqueNameChecks() {
        final String duplicateName = "duplicate123";
        TestNode.with(duplicateName);

        TestNode.disableUniqueNameChecks();
        TestNode.with(duplicateName);

        TestNode.enableUniqueNameChecks();

        boolean failed = false;
        try {
            TestNode.with(duplicateName);
        } catch (final AssertionError expected) {
            failed = true;
        }
        this.checkEquals(failed, true, "Factory should have failed with duplicate name");
    }

    @Test
    @Override
    public void testParentWithoutRoot() {
        this.parentMissingCheck(TestNode.with("root"));
    }

    @Test
    public void testParentWithoutRootWithChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode root = TestNode.with("root", child1, child2);

        this.parentMissingCheck(root);
    }

    @Test
    @Override
    public void testParentWithoutChild() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        TestNode.clear();

        this.parentMissingCheck(parent.child(0), TestNode.with("parent", child2));
    }

    @Test
    @Override
    public void testRemoveParent() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.removeParentAndCheck(parent);
    }

    @Test
    public void testRemoveParent2() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent = TestNode.with("parent", child1, child2);

        this.removeParentAndCheck(parent.child(1));
    }

    @Test
    public void testRoot() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");
        final TestNode parent1 = TestNode.with("parent1", child1, child2);
        final TestNode parent2 = TestNode.with("parent2");
        final TestNode root = TestNode.with("root", parent1, parent2);

        this.checkEquals(root, root.child(0).child(0).root());
        this.checkEquals(root, root.child(0).child(1).root());
        this.checkEquals(root, root.child(0).root());
        this.checkEquals(root, root);
    }

    @Test
    public void testSetChildIndex() {
        final TestNode child2 = TestNode.with("child2");
        final TestNode root = TestNode.with("root", TestNode.with("child1"));

        TestNode.clear();

        this.checkEquals(TestNode.with("root", TestNode.with("child2")),
            root.setChild(0, child2));
    }

    @Test
    public void testSetChildIndex2() {
        final TestNode child3 = TestNode.with("child3");
        final TestNode root = TestNode.with("root",
            TestNode.with("child1"),
            TestNode.with("child2"));

        TestNode.clear();

        this.checkEquals(TestNode.with("root",
                TestNode.with("child1"),
                TestNode.with("child3")),
            root.setChild(1, child3));
    }

    @Test
    public void testSetChildNameReplaces() {
        final TestNode child3 = TestNode.with("child3");
        final TestNode root = TestNode.with("root", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        this.checkEquals(TestNode.with("root", TestNode.with("child3"), TestNode.with("child2")),
            root.setChild(Names.string("child1"), child3));
    }

    @Test
    public void testSetChildNameReplaces2() {
        final TestNode child3 = TestNode.with("child3");
        final TestNode root = TestNode.with("root", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        this.checkEquals(TestNode.with("root", TestNode.with("child1"), TestNode.with("child3")),
            root.setChild(Names.string("child2"), child3));
    }

    @Test
    public void testSetChildNameAppends() {
        final TestNode child3 = TestNode.with("child3");
        final TestNode root = TestNode.with("root", TestNode.with("child1"), TestNode.with("child2"));

        TestNode.clear();

        this.checkEquals(TestNode.with("root", TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3")),
            root.setChild(Names.string("unknown"), child3));
    }

    @Test
    public void testSetChildren() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        final TestNode parent = TestNode.with("parent", child1, child2);
        final String parentToString = parent.toString();

        final TestNode parent2 = parent.setChildren(Lists.of(child2, child1))
            .setChildren(Lists.of(child1, child2));
        final String parent2ToString = parent2.toString();

        this.checkEquals(parentToString, parent2ToString);

        this.parentWithoutAndCheck2(child1, child2);
    }

    @Test
    public void testSetChildren2() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        final TestNode parent = TestNode.with("parent", child1, child2);
        final String parentToString = parent.toString();

        final TestNode child3 = TestNode.with("child3");
        final TestNode parent2 = parent.setChildren(Lists.of(child1, child3));
        final String parent2ToString = parent2.toString();

        this.checkEquals(parentToString, parent2ToString
            .replace("child3", "child2"));

        this.parentWithoutAndCheck2(child1, child2, child3);
    }

    @Test
    public void testSetChildrenWithParent() {
        final TestNode grandParent = TestNode.with("grandParent",
            TestNode.with("parent1",
                TestNode.with("child1"), TestNode.with("child2")
            ),
            TestNode.with("parent2",
                TestNode.with("child3"), TestNode.with("child4")
            ));

        final TestNode child5 = TestNode.with("child5");

        TestNode.clear();

        this.checkEquals(TestNode.with("grandParent",
                TestNode.with("parent1",
                    TestNode.with("child1"), TestNode.with("child2")
                ),
                TestNode.with("parent2",
                    TestNode.with("child3"), child5)),
            grandParent.child(1)
                .setChild(1, child5)
                .root());

        this.parentMissingCheck(child5);
    }

    @Test
    public void testSetAttributesWithParent() {
        final TestNode grandParent = TestNode.with("grandParent",
            TestNode.with("parent1",
                TestNode.with("child1"), TestNode.with("child2")
            ),
            TestNode.with("parent2",
                TestNode.with("child3"), TestNode.with("child4")
            ));

        TestNode.clear();

        final Map<StringName, Object> attributes = Maps.of(Names.string("attribute123"), "value456");

        this.checkEquals(TestNode.with("grandParent",
                TestNode.with("parent1",
                    TestNode.with("child1"), TestNode.with("child2")
                ),
                TestNode.with("parent2",
                    TestNode.with("child3"), TestNode.with("child4").setAttributes(attributes))),
            grandParent.child(1).child(1)
                .setAttributes(attributes)
                .root());
    }

    @Test
    @Override
    public void testEquals() {
        final TestNode node1 = TestNode.with(this.currentTestName() + "-" + this.i);
        TestNode.clear(); // required to reset cache which verifies uniqueness of nodes.
        final TestNode node2 = TestNode.with(this.currentTestName() + "-" + this.i);
        checkEqualsAndHashCode(node1, node2);
    }

    @Test
    public void testPointerRoot() {
        this.traverseAndCheck(TestNode.with("root"));
    }

    @Test
    public void testPointerChild() {
        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2");

        TestNode.with("parent", child1, child2);

        this.traverseAndCheck(child2);
    }

    @Test
    public void testPointerGrandChild2() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode grandChild3 = TestNode.with("grandChild3");

        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2", grandChild1, grandChild2, grandChild3);

        TestNode.with("parent", child1, child2);

        this.traverseAndCheck(grandChild3);
    }

    @Test
    public void testPointerGrandChild3() {
        final TestNode grandChild1 = TestNode.with("grandChild1");
        final TestNode grandChild2 = TestNode.with("grandChild2");
        final TestNode grandChild3 = TestNode.with("grandChild3");
        final TestNode grandChild4 = TestNode.with("grandChild4");

        final TestNode child1 = TestNode.with("child1");
        final TestNode child2 = TestNode.with("child2", grandChild1, grandChild2, grandChild3, grandChild4);

        TestNode.with("parent", child1, child2);

        this.traverseAndCheck(grandChild4);
    }

    private void traverseAndCheck(final TestNode node) {
        final NodePointer<TestNode, StringName> pointer = node.pointer();

        this.checkEquals(Optional.of(node),
            pointer.traverse(node.root()),
            "pointer returned wrong node");
    }

    @Override
    public void testReplaceChildDifferentParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAppendChild() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAppendChild2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRemoveChildFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testRemoveChildLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetChildrenSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestNode createNode() {
        return TestNode.with(this.currentTestName() + "-" + this.i++);
    }

    private int i = 0;

    private void parentWithoutAndCheck2(final TestNode... nodes) {
        Arrays.stream(nodes)
            .forEach(this::parentMissingCheck);
    }

    @Override
    public String typeNamePrefix() {
        return "Test";
    }

    @Override
    public Class<TestNode> type() {
        return TestNode.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
