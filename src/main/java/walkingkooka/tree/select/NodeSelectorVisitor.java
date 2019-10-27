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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Visitor} for a {@link NodeSelector}
 */
public abstract class NodeSelectorVisitor<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> extends Visitor<NodeSelector<N, NAME, ANAME, AVALUE>> {

    protected NodeSelectorVisitor() {
        super();
    }

    @Override
    public final void accept(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        Objects.requireNonNull(selector, "selector");

        this.traverse(selector);
    }

    final void traverse(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        if (Visiting.CONTINUE == this.startVisit(selector)) {
            selector.accept0(this);
        }
        this.endVisit(selector);
    }

    protected Visiting startVisit(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitCustom(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String custom) {
        return Visiting.CONTINUE;
    }

    protected void endVisitCustom(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String custom) {
    }

    protected Visiting startVisitAbsolute(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitAbsolute(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitAncestor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitAncestor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitAncestorOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitAncestorOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitChildren(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitChildren(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitDescendant(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitDescendant(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitDescendantOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitDescendantOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitExpression(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final ExpressionNode expression) {
        return Visiting.CONTINUE;
    }

    protected void endVisitExpression(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final ExpressionNode expression) {
    }

    protected Visiting startVisitFirstChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitFirstChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitFollowing(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitFollowing(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitFollowingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitFollowingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitLastChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitLastChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitNamed(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final NAME name) {
        return Visiting.CONTINUE;
    }

    protected void endVisitNamed(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final NAME name) {
    }

    protected Visiting startVisitParent(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitParent(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitPreceding(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitPreceding(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitPrecedingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitPrecedingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected Visiting startVisitPredicate(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final Predicate<N> predicate) {
        return Visiting.CONTINUE;
    }

    protected void endVisitPredicate(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final Predicate<N> predicate) {
    }

    protected Visiting startVisitSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        return Visiting.CONTINUE;
    }

    protected void endVisitSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }

    protected void visitTerminal(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
    }
}
