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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Objects;

public abstract class NodePointerVisitor<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends Visitor<NodePointer<N, NAME>> {

    protected NodePointerVisitor() {
        super();
    }

    public final void accept(final NodePointer<N, NAME> node) {
        Objects.requireNonNull(node, "node");
        this.accept0(node);
    }

    final void accept0(final NodePointer<N, NAME> node) {
        if (Visiting.CONTINUE == this.startVisit(node)) {
            node.accept(this);
        }
        this.endVisit(node);
    }

    protected Visiting startVisit(final NodePointer<N, NAME> node) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodePointer<N, NAME> node) {
        // nop
    }

    protected void visitAny(final NodePointer<N, NAME> node) {
        // nop
    }

    protected void visitAppend(final NodePointer<N, NAME> node) {
        // nop
    }

    protected Visiting startVisitIndexedChild(final NodePointer<N, NAME> node,
                                              final int index) {
        return Visiting.CONTINUE;
    }

    protected void endVisitIndexedChild(final NodePointer<N, NAME> node,
                                        final int index) {
        // nop
    }

    protected Visiting startVisitNamedChild(final NodePointer<N, NAME> node,
                                            final NAME name) {
        return Visiting.CONTINUE;
    }

    protected void endVisitNamedChild(final NodePointer<N, NAME> node,
                                      final NAME name) {
        // nop
    }

    protected Visiting startVisitRelative(final NodePointer<N, NAME> node) {
        return Visiting.CONTINUE;
    }

    protected void endVisitRelative(final NodePointer<N, NAME> node) {
        // nop
    }
}
