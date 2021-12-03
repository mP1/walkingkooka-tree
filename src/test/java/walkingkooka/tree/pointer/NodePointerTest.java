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
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.TestNode;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class NodePointerTest implements ClassTesting2<NodePointer<TestNode, StringName>>,
        ParseStringTesting<NodePointer<TestNode, StringName>>,
        ToStringTesting<NodePointer<TestNode, StringName>> {

    private final static StringName ABC = Names.string("abc");
    private final static StringName DEF = Names.string("def");
    private final static StringName GHI = Names.string("ghi");
    private final static StringName JKL = Names.string("jkl");

    private final static Function<String, StringName> NAME_FACTORY = (Names::string);

    @BeforeEach
    public void beforeEach() {
        TestNode.clear();
    }

    @Test
    public void testIndexInvalidIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> NodePointer.indexed(-1, TestNode.class));
    }

    @Test
    public void testIndexNullNodeClassFails() {
        assertThrows(NullPointerException.class, () -> NodePointer.indexed(0, null));
    }

    @Test
    public void testIndexInvalidIndexFails2() {
        assertThrows(IllegalArgumentException.class, () -> NodePointer.indexed(0, TestNode.class)
                .indexed(-1));
    }

    @Test
    public void testNamedNullNameFails() {
        assertThrows(NullPointerException.class, () -> NodePointer.named(null, TestNode.class));
    }

    @Test
    public void testNamedNullNodeClassFails() {
        assertThrows(NullPointerException.class, () -> NodePointer.named(ABC, null));
    }

    @Test
    public void testNamedNullNameFails2() {
        assertThrows(NullPointerException.class, () -> NodePointer.named(ABC, TestNode.class)
                .named(null));
    }

    // toString.......................................................................................................

    @Test
    public void testToStringRelative() {
        this.toStringAndCheck(NodePointer.relative(1, TestNode.class),
                "1");
    }

    @Test
    public void testToStringRelativeHash() {
        this.toStringAndCheck(NodePointer.relativeHash(1, TestNode.class),
                "1#");
    }

    @Test
    public void testToStringRelativeIndex() {
        this.toStringAndCheck(NodePointer.relative(1, TestNode.class)
                        .indexed(23),
                "1/23");
    }

    @Test
    public void testToStringRelativeNamed() {
        this.toStringAndCheck(NodePointer.relative(1, TestNode.class)
                        .named(Names.string("abc")),
                "1/abc");
    }

    @Test
    public void testToStringRelativeHashNamed() {
        this.toStringAndCheck(NodePointer.relativeHash(1, TestNode.class)
                        .named(Names.string("abc")),
                "1/abc");
    }

    @Test
    public void testToStringChildIndex() {
        this.toStringAndCheck(NodePointer.indexed(0, TestNode.class),
                "/0");
    }

    @Test
    public void testToStringChildNamed() {
        this.toStringAndCheck("/abc",
                NodePointer.named(ABC, TestNode.class)
                        .toString());
    }

    @Test
    public void testToStringChildNamedChildIndex() {
        this.toStringAndCheck(NodePointer.named(ABC, TestNode.class)
                        .indexed(0),
                "/abc/0");
    }

    @Test
    public void testToStringChildNamedChildIndexX2() {
        this.toStringAndCheck(NodePointer.named(ABC, TestNode.class)
                        .indexed(0)
                        .indexed(1),
                "/abc/0/1");
    }

    @Test
    public void testToStringChildIndexChildNamedChildIndex() {
        this.toStringAndCheck("/0/abc/1",
                NodePointer.indexed(0, TestNode.class)
                        .named(ABC)
                        .indexed(1)
                        .toString());
    }

    @Test
    public void testToStringChildNamedChildNamed() {
        this.toStringAndCheck(NodePointer.named(ABC, TestNode.class)
                        .named(DEF),
                "/abc/def");
    }

    @Test
    public void testToStringChildNamedChildNamedChildNamed() {
        this.toStringAndCheck(NodePointer.named(ABC, TestNode.class)
                        .named(DEF)
                        .named(GHI),
                "/abc/def/ghi");
    }

    @Test
    public void testToStringChildNamedChildNamedChildNamedChildNamed() {
        this.toStringAndCheck(NodePointer.named(ABC, TestNode.class)
                        .named(DEF)
                        .named(GHI)
                        .named(JKL),
                "/abc/def/ghi/jkl");
    }

    @Test
    public void testToStringChildIndexChildIndexChildIndexChildIndex() {
        this.toStringAndCheck(NodePointer.indexed(0, TestNode.class)
                        .indexed(1)
                        .indexed(2)
                        .indexed(3),
                "/0/1/2/3");
    }

    // traverse.........................................................................................................

    @Test
    public void testRelativeAbsent() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.relative(1, TestNode.class);
        this.checkIsRelative(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("match"));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testRelativeSelf() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.relative(0, TestNode.class);
        this.checkIsRelative(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child"));
        this.traverseAndCheck(pointer, root, root.toString());
    }

    @Test
    public void testRelativeHashSelf() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.relative(0, TestNode.class);
        this.checkIsRelative(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child"));
        this.traverseAndCheck(pointer, root, root.toString());
    }

    @Test
    public void testRelativeParent() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.relative(1, TestNode.class);
        this.checkIsRelative(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child"));
        this.traverseAndCheck(pointer, root.child(0), root.toString());
    }

    @Test
    public void testNamedAbsent() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.named(ABC, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child"));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexAbsent() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(55, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child"));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexAbsent2() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(1, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child"));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndex() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(0, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode zero = TestNode.with("match");
        final TestNode root = TestNode.with("root")
                .appendChild(zero);
        this.traverseAndCheck(pointer, root, zero.toString());
    }

    @Test
    public void testIndex2() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(1, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode one = TestNode.with("match");
        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("wrong"))
                .appendChild(one);
        this.traverseAndCheck(pointer, root, one.toString());
    }

    @Test
    public void testNamed() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.named(ABC, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode abc = TestNode.with("abc");
        final TestNode root = TestNode.with("root")
                .appendChild(abc);
        this.traverseAndCheck(pointer, root, abc.toString());
    }

    @Test
    public void testNamed2() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.named(DEF, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode def = TestNode.with("def");
        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc"))
                .appendChild(def);
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testNamed3() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.named(ABC, TestNode.class)
                .named(DEF);
        this.checkIsAbsolute(pointer);

        final TestNode def = TestNode.with("def");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc", def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testNamedLastAbsent() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.named(ABC, TestNode.class)
                .named(DEF)
                .named(GHI);
        this.checkIsAbsolute(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc")
                        .appendChild(TestNode.with("def")));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testNamedLastAbsent2() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.named(ABC, TestNode.class)
                .named(GHI);
        this.checkIsAbsolute(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc").appendChild(TestNode.with("match")));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexLastAbsent() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(0, TestNode.class)
                .indexed(1)
                .indexed(2);
        this.checkIsAbsolute(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child")
                        .appendChild(TestNode.with("wrong"))
                        .appendChild(TestNode.with("def")));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testIndexLastAbsent2() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(0, TestNode.class)
                .indexed(99);
        this.checkIsAbsolute(pointer);

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc")
                        .appendChild(TestNode.with("match")));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testNestedArray() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(0, TestNode.class)
                .indexed(1);
        this.checkIsAbsolute(pointer);

        final TestNode match = TestNode.with("match1");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child0")
                        .appendChild(TestNode.with("wrong"))
                        .appendChild(match));
        this.traverseAndCheck(pointer, root, match.toString());
    }

    @Test
    public void testIndexForObject() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.indexed(0, TestNode.class);
        this.checkIsAbsolute(pointer);

        final TestNode match = TestNode.with("match0");

        final TestNode root = TestNode.with("root")
                .appendChild(match);
        this.traverseAndCheck(pointer, root, match.toString());
    }

    @Test
    public void testIndexForObject2() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.named(ABC, TestNode.class)
                .indexed(0);
        this.checkIsAbsolute(pointer);

        final TestNode match = TestNode.with("name-abc-index-0");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc")
                        .appendChild(match));
        this.traverseAndCheck(pointer, root, match.toString());
    }

    @Test
    public void testRelativeNestedArray() {
        final NodePointer<TestNode, StringName> pointer = NodePointer.relative(1, TestNode.class)
                .indexed(2);
        this.checkIsRelative(pointer);

        final TestNode text = TestNode.with("match");
        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child").appendChild(TestNode.with("wrong")).appendChild(TestNode.with("wrong2")).appendChild(text));

        this.traverseAndCheck(pointer, root.child(0).child(1), text.toString());
    }

    // parse.............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseInvalidIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> NodePointer.parse("/abc/-99", NAME_FACTORY, TestNode.class));
    }

    @Test
    public void testParseInvalidNameFails() {
        assertThrows(IllegalArgumentException.class, () -> NodePointer.parse("/abc//xyz", NAME_FACTORY, TestNode.class));
    }

    @Test
    public void testParseInvalidNameFails2() {
        assertThrows(IllegalArgumentException.class, () -> NodePointer.parse("missing-leading-slash", NAME_FACTORY, TestNode.class));
    }

    @Test
    public void testParseNullNameFactoryFails() {
        assertThrows(NullPointerException.class, () -> NodePointer.parse("/valid-pointer", null, TestNode.class));
    }

    @Test
    public void testParseNullNodeTypeFails() {
        assertThrows(NullPointerException.class, () -> NodePointer.parse("/valid-pointer", NAME_FACTORY, null));
    }

    @Test
    public void testParseAppendWithNextFails() {
        this.parseStringFails("/-/1", UnsupportedOperationException.class);
    }

    @Test
    public void testParseThenTraverseElements() {
        final NodePointer<TestNode, StringName> pointer = parseString("/0/1");

        final TestNode def = TestNode.with("match");
        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("child").appendChild(TestNode.with("wrong")).appendChild(def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseNamed() {
        final NodePointer<TestNode, StringName> pointer = parseString("/abc/def");

        final TestNode def = TestNode.with("def");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc")
                        .appendChild(def));
        this.traverseAndCheck(pointer, root, def.toString());
    }

    @Test
    public void testParseThenTraverseNamedAndIndex() {
        final NodePointer<TestNode, StringName> pointer = parseString("/abc/0/def");

        final TestNode match = TestNode.with("def");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc")
                        .appendChild(TestNode.with("abc-index-0")
                                .appendChild(match)
                                .appendChild(TestNode.with("ghi-wrong")))
                        .appendChild(TestNode.with("another")));
        this.traverseAndCheck(pointer, root, match.toString());
    }

    @Test
    public void testParseThenTraverseAppend() {
        final NodePointer<TestNode, StringName> pointer = parseString("/abc/-");

        final TestNode match = TestNode.with("match");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc")
                        .appendChild(match));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testParseThenTraverseAppend2() {
        final NodePointer<TestNode, StringName> pointer = parseString("/abc/def/-");

        final TestNode def = TestNode.with("def");

        final TestNode root = TestNode.with("root")
                .appendChild(TestNode.with("abc")
                        .appendChild(def));
        this.traverseFail(pointer, root);
    }

    @Test
    public void testParseTilde() {
        final NodePointer<TestNode, StringName> pointer = parseString("/tilde~0");

        final TestNode match = TestNode.with("tilde~");

        final TestNode root = TestNode.with("root")
                .appendChild(match);
        this.traverseAndCheck(pointer, root, match.toString());
    }

    private void traverseAndCheck(final NodePointer<TestNode, StringName> pointer, final TestNode root, final String toString) {
        final Optional<TestNode> result = pointer.traverse(root);
        this.checkNotEquals(Optional.empty(), result, () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched a node but failed,\n" + root);
        this.checkEquals(toString,
                result.map(TestNode::toString).orElse(null),
                () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched the node\n" + root);
    }

    private void traverseFail(final NodePointer<TestNode, StringName> pointer, final TestNode root) {
        this.checkEquals(Optional.empty(),
                pointer.traverse(root),
                () -> "The pointer " + CharSequences.quote(pointer.toString()) + " should have matched nothing\n" + root);
    }

    private void checkIsAbsolute(final NodePointer<?, ?> pointer) {
        assertTrue(pointer.isAbsolute(), "isAbsolute");
        assertFalse(pointer.isRelative(), "isRelative");
        assertTrue(pointer.toString().startsWith("/"), () -> "pointer should start with '/' =" + pointer);
    }

    private void checkIsRelative(final NodePointer<?, ?> pointer) {
        assertFalse(pointer.isAbsolute(), "isAbsolute");
        assertTrue(pointer.isRelative(), "isRelative");
        assertFalse(pointer.toString().startsWith("/"), () -> "pointer shouldnt start with '/' =" + pointer);
    }

    @Override
    public Class<NodePointer<TestNode, StringName>> type() {
        return Cast.to(NodePointer.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public NodePointer<TestNode, StringName> parseString(final String pointer) {
        final NodePointer<TestNode, StringName> parsed = NodePointer.parse(pointer, NAME_FACTORY, TestNode.class);
        this.checkEquals(pointer,
                parsed.toString(),
                () -> "pointer.parse: " + CharSequences.quoteAndEscape(pointer));
        return parsed;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }
}
