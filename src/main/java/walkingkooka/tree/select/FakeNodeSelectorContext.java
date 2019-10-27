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

import walkingkooka.Either;
import walkingkooka.naming.Name;
import walkingkooka.test.Fake;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNodeName;

import java.math.MathContext;
import java.util.List;

public class FakeNodeSelectorContext<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> implements NodeSelectorContext<N, NAME, ANAME, AVALUE>, Fake {

    @Override
    public boolean isFinished() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean test(final N node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNode(final N node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public N selected(final N node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Either<T, String> convert(final Object value, final Class<T> target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object function(final ExpressionNodeName name, final List<Object> parameters) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MathContext mathContext() {
        throw new UnsupportedOperationException();
    }
}
