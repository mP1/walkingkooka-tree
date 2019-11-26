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
import walkingkooka.tree.expression.Expression;
import walkingkooka.visit.Visiting;

import java.util.function.Predicate;

public class FakeNodeSelectorVisitor<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> extends NodeSelectorVisitor<N, NAME, ANAME, AVALUE> {

    public FakeNodeSelectorVisitor() {
    }

    @Override
    protected Visiting startVisit(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitCustom(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String custom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitCustom(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final String custom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitAbsolute(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitAbsolute(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitAncestor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitAncestor(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitAncestorOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitAncestorOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitChildren(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitChildren(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitDescendant(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitDescendant(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitDescendantOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitDescendantOrSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitExpression(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final Expression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitExpression(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final Expression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitFirstChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitFirstChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitFollowing(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitFollowing(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitFollowingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitFollowingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitLastChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitLastChild(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitNamed(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final NAME name) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitNamed(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final NAME name) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitParent(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitParent(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitPreceding(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitPreceding(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitPrecedingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitPrecedingSibling(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitPredicate(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final Predicate<N> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitPredicate(final NodeSelector<N, NAME, ANAME, AVALUE> selector, final Predicate<N> predicate) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisitSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisitSelf(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTerminal(final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        throw new UnsupportedOperationException();
    }
}
