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

import walkingkooka.naming.Name;
import walkingkooka.test.Fake;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeNode<N extends Node<N, NAME, ANAME, AVALUE>, NAME extends Name, ANAME extends Name, AVALUE>
    implements Node<N, NAME, ANAME, AVALUE>, Fake {

    @Override
    public NAME name() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int index() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<N> parent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public N removeParent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRoot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<N> children() {
        throw new UnsupportedOperationException();
    }

    @Override
    public N setChildren(List<N> children) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<ANAME, AVALUE> attributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public N setAttributes(Map<ANAME, AVALUE> attributes) {
        throw new UnsupportedOperationException();
    }
}
