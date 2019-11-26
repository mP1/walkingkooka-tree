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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CustomToStringNodeSelectorTest extends NodeSelectorTestCase4<CustomToStringNodeSelector<TestNode, StringName, StringName, Object>> {

    private final static String TOSTRING = "!CustomToString123";

    @Test
    public void testWithSelectorNullFails() {
        assertThrows(NullPointerException.class, () -> CustomToStringNodeSelector.with(null, TOSTRING));
    }

    @Test
    public void testWithToStringNullFails() {
        assertThrows(NullPointerException.class, () -> CustomToStringNodeSelector.with(this.wrapped(), null));
    }

    @Test
    public void testUnnecessaryWrap() {
        final NodeSelector<TestNode, StringName, StringName, Object> wrapped = this.wrapped();
        assertSame(wrapped, CustomToStringNodeSelector.with(wrapped, wrapped.toString()));
    }

    @Test
    public void testWith() {
        final NodeSelector<TestNode, StringName, StringName, Object> wrapped = this.wrapped();
        final CustomToStringNodeSelector<TestNode, StringName, StringName, Object> custom = this.createSelector(wrapped);
        assertEquals(wrapped, custom.selector, "selector");
    }

    @Test
    public void testDoubleWrap() {
        final NodeSelector<TestNode, StringName, StringName, Object> wrapped = this.wrapped();
        final CustomToStringNodeSelector<TestNode, StringName, StringName, Object> custom = this.createSelector(wrapped, TOSTRING);

        final String toString2 = "CustomToString2";
        final CustomToStringNodeSelector<TestNode, StringName, StringName, Object> again = this.createSelector(custom, toString2);
        assertEquals(wrapped, again.selector, "selector");
        assertEquals(toString2, again.toString(), "toString");
    }

    @Test
    public void testAppend() {
        final String toString = "abc123!";

        final CustomToStringNodeSelector<TestNode, StringName, StringName, Object> custom = Cast.to(TestNode.relativeNodeSelector()
                .parent()
                .setToString(toString)
                .append(SelfNodeSelector.get()));
        this.toStringAndCheck(custom.selector, "../.");
    }

    @Test
    public void testAppendCustomToStringNodeSelector() {
        final String toString = "abc123!";
        final NodeSelector<TestNode, StringName, StringName, Object> custom = TestNode.relativeNodeSelector()
                .self()
                .setToString(toString);

        final ParentNodeSelector<TestNode, StringName, StringName, Object> parent = Cast.to(TestNode.relativeNodeSelector()
                .parent()
                .append(custom));
        assertEquals(parent.next, custom);
    }

    @Test
    public void testMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3"));

        this.acceptMapAndCheck(TestNode.relativeNodeSelector().firstChild().setToString(TOSTRING),
                parent,
                parent.setChild(0, TestNode.with("child1*0")));
    }

    @Test
    public void testEqualsDifferentWrappedNodeSelector() {
        this.checkNotEquals(CustomToStringNodeSelector.with(SelfNodeSelector.<TestNode, StringName, StringName, Object>get(), TOSTRING));
    }

    @Test
    public void testEqualsDifferentCustomToString() {
        this.checkNotEquals(CustomToStringNodeSelector.with(this.wrapped(), "different"));
    }

    @Test
    public void testAppendKeepsCustomToString() {
        final String toString = "abc123!";

        this.toStringAndCheck(TestNode.relativeNodeSelector()
                        .parent()
                        .setToString(toString)
                        .append(SelfNodeSelector.get()),
                toString);
    }

    @Test
    public void testAppendCustomToStringNodeSelectorToString() {
        final String toString = "abc123!";

        this.toStringAndCheck(TestNode.relativeNodeSelector()
                .parent()
                .append(TestNode.relativeNodeSelector().self().setToString(toString)), "../" + toString);
    }

    // Object..........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), TOSTRING);
    }

    @Override
    CustomToStringNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return this.createSelector(this.wrapped());
    }

    private CustomToStringNodeSelector<TestNode, StringName, StringName, Object> createSelector(final NodeSelector<TestNode, StringName, StringName, Object> wrap) {
        return this.createSelector(wrap, TOSTRING);
    }

    private CustomToStringNodeSelector<TestNode, StringName, StringName, Object> createSelector(final NodeSelector<TestNode, StringName, StringName, Object> wrap,
                                                                                                final String toString) {
        return Cast.to(CustomToStringNodeSelector.with(wrap, toString));
    }

    @Override final void applyAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                                        final TestNode start,
                                        final String... nodes) {
        this.applyAndCheckUsingContext(selector, start, nodes);
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<CustomToStringNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(CustomToStringNodeSelector.class);
    }
}
