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

package walkingkooka.tree.select.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.BinaryOperatorTransformerTesting;

public final class NodeSelectorEbnfParserCombinatorSyntaxTreeTransformerBinaryOperatorTransformerTest implements BinaryOperatorTransformerTesting<NodeSelectorEbnfParserCombinatorSyntaxTreeTransformerBinaryOperatorTransformer> {

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBinaryOperatorTransformer(), NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer.class.getSimpleName());
    }

    @Override
    public NodeSelectorEbnfParserCombinatorSyntaxTreeTransformerBinaryOperatorTransformer createBinaryOperatorTransformer() {
        return NodeSelectorEbnfParserCombinatorSyntaxTreeTransformerBinaryOperatorTransformer.INSTANCE;
    }

    @Override
    public String typeNamePrefix() {
        return NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer.class.getSimpleName();
    }

    @Override
    public Class<NodeSelectorEbnfParserCombinatorSyntaxTreeTransformerBinaryOperatorTransformer> type() {
        return NodeSelectorEbnfParserCombinatorSyntaxTreeTransformerBinaryOperatorTransformer.class;
    }
}
