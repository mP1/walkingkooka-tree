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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.TestNode;
import walkingkooka.visit.Visiting;

public final class NodePointerVisitorTest implements NodePointerVisitorTesting<FakeNodePointerVisitor<TestNode, StringName>, TestNode, StringName> {

    @Override
    public void testTestNaming() {
    }

    @Test
    public void testAcceptSkipRemaining() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

        }.accept(NodePointer.parse("/1/abc/-", Names::string, TestNode.class));

        this.checkEquals("12", b.toString());
    }

    @Test
    public void testAny() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected void visitAny(final NodePointer<TestNode, StringName> node) {
                b.append("3");
            }
        }.accept(NodePointer.any(TestNode.class));

        this.checkEquals("132", b.toString());
    }

    @Test
    public void testAny2() {
        new NodePointerVisitor<TestNode, StringName>() {
        }.accept(NodePointer.any(TestNode.class));
    }

    @Test
    public void testRelative() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            protected Visiting startVisitRelative(final NodePointer<TestNode, StringName> node) {
                b.append("3");
                return Visiting.CONTINUE;
            }

            protected void endVisitRelative(final NodePointer<TestNode, StringName> node) {
                b.append("4");
                // nop
            }

        }.accept(NodePointer.relative(0, TestNode.class));

        this.checkEquals("1342", b.toString());
    }

    @Test
    public void testRelative2() {
        new NodePointerVisitor<TestNode, StringName>() {
        }.accept(NodePointer.relative(0, TestNode.class));
    }

    @Test
    public void testMultipleComponents() {
        final StringBuilder b = new StringBuilder();

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> node) {
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> node) {
                b.append("2");
            }

            @Override
            protected void visitAppend(final NodePointer<TestNode, StringName> node) {
                b.append("3");
            }

            @Override
            protected Visiting startVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                      final int index) {
                checkEquals(1, index, "index");
                b.append("4");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitIndexedChild(final NodePointer<TestNode, StringName> node,
                                                final int index) {
                b.append("5");
            }

            @Override
            protected Visiting startVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                                    final StringName name) {
                checkEquals(Names.string("abc"), name, "name");
                b.append("6");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitNamedChild(final NodePointer<TestNode, StringName> node,
                                              final StringName name) {
                b.append("7");
            }
        }.accept(NodePointer.parse("/1/abc/-", Names::string, TestNode.class));

        this.checkEquals("14161327252", b.toString());
    }

    @Test
    public void testMultipleComponents2() {
        this.visitAndCheck("/1/abc/-");
    }

    @Test
    public void testMultipleComponents3() {
        this.visitAndCheck("/1/abc/2/def/-");
    }

    private void visitAndCheck(final String path) {
        final StringBuilder b = new StringBuilder();

        final NodePointer<TestNode, StringName> pointer = NodePointer.parse(path, Names::string, TestNode.class);

        new FakeNodePointerVisitor<TestNode, StringName>() {
            @Override
            protected Visiting startVisit(final NodePointer<TestNode, StringName> p) {
                b.append("/");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodePointer<TestNode, StringName> p) {
            }

            @Override
            protected void visitAppend(final NodePointer<TestNode, StringName> p) {
                b.append("-");
            }

            @Override
            protected Visiting startVisitIndexedChild(final NodePointer<TestNode, StringName> p,
                                                      final int index) {
                b.append(index);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitIndexedChild(final NodePointer<TestNode, StringName> p,
                                                final int index) {
            }

            @Override
            protected Visiting startVisitNamedChild(final NodePointer<TestNode, StringName> p,
                                                    final StringName name) {
                b.append(name);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitNamedChild(final NodePointer<TestNode, StringName> p,
                                              final StringName name) {
            }
        }.accept(pointer);

        this.checkEquals(path, b.toString());

        new NodePointerVisitor<TestNode, StringName>() {
        }.accept(pointer);
    }

    @Override
    public FakeNodePointerVisitor<TestNode, StringName> createVisitor() {
        return new FakeNodePointerVisitor<>();
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public Class<FakeNodePointerVisitor<TestNode, StringName>> type() {
        return Cast.to(FakeNodePointerVisitor.class);
    }
}
