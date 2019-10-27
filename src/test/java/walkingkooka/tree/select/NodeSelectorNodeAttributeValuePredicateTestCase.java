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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.tree.TestNode;

import java.util.Collections;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodeSelectorNodeAttributeValuePredicateTestCase<N extends NodeSelectorNodeAttributeValuePredicate<TestNode, StringName, StringName, Object>>
        extends NodeSelectorTestCase2<N>
        implements PredicateTesting2<N, TestNode> {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    final static StringName ATTRIBUTE_NAME1 = Names.string("attribute-1");
    final static StringName ATTRIBUTE_NAME2 = Names.string("attribute-2");
    final static String VALUE = "123";

    NodeSelectorNodeAttributeValuePredicateTestCase() {
        super();
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> this.createPredicate(ATTRIBUTE_NAME1, null));
    }

    @Test
    public final void testAttributeMissing() {
        this.testFalse(TestNode.with("nodeName").setAttributes(Collections.singletonMap(ATTRIBUTE_NAME2, "value")));
    }

    @Test
    public final void testAttributeDifferentValue() {
        this.testFalse(TestNode.with("nodeName").setAttributes(Collections.singletonMap(ATTRIBUTE_NAME1, "*DIFFERENT*")));
    }

    @Override
    public final N createPredicate() {
        return this.createPredicate(ATTRIBUTE_NAME1);
    }

    final N createPredicate(final StringName name) {
        return this.createPredicate(name, VALUE);
    }

    abstract N createPredicate(final StringName name, final Object value);

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNameSuffix() {
        return Predicate.class.getSimpleName();
    }
}
