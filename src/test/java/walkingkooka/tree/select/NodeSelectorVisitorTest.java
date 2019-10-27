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

import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.select.parser.NodeSelectorVisitorTesting;

public final class NodeSelectorVisitorTest implements NodeSelectorVisitorTesting<NodeSelectorVisitor<TestNode, StringName, StringName, Object>, TestNode, StringName, StringName, Object> {

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public NodeSelectorVisitor<TestNode, StringName, StringName, Object> createVisitor() {
        return new FakeNodeSelectorVisitor<>();
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
    public Class<NodeSelectorVisitor<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorVisitor.class);
    }
}
