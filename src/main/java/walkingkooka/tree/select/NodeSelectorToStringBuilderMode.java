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
import walkingkooka.tree.expression.Expression;

import java.util.function.Predicate;

enum NodeSelectorToStringBuilderMode {
    AXIS_SYMBOL {
        @Override
        boolean requireSeparator() {
            return true;
        }

        @Override
        boolean requireWildcard() {
            return true;
        }

        @Override
        boolean requireFinishingWildcard() {
            return false;
        }
    },
    AXIS_NAME_OR_DESCENDANT_OR_SELF {
        @Override
        boolean requireSeparator() {
            return false;
        }

        @Override
        boolean requireWildcard() {
            return true;
        }

        @Override
        boolean requireFinishingWildcard() {
            return false;
        }
    },
    /**
     * Special mode to handle {@link CustomToStringNodeSelector}.
     */
    CUSTOM {
        @Override
        boolean requireSeparator() {
            return true;
        }

        @Override
        boolean requireWildcard() {
            return false;
        }

        @Override
        boolean requireFinishingWildcard() {
            return false;
        }
    },
    WILDCARD_OR_NAME_OR_PREDICATE {
        @Override
        boolean requireSeparator() {
            return false;
        }

        @Override
        boolean requireWildcard() {
            return true;
        }

        @Override
        boolean requireFinishingWildcard() {
            return true;
        }
    },
    PREDICATE {
        @Override
        boolean requireSeparator() {
            return true;
        }

        @Override
        boolean requireWildcard() {
            return false;
        }

        @Override
        boolean requireFinishingWildcard() {
            return false;
        }
    };

    final NodeSelectorToStringBuilderMode absolute(final StringBuilder b) {
        final char c = NodeSelector.SEPARATOR.character();
        b.append(c);
        return AXIS_NAME_OR_DESCENDANT_OR_SELF;
    }

    // AXIS_NAME_OR_DESCENDANT_OR_SELF ...........................................................................................................

    final NodeSelectorToStringBuilderMode axisName(final String axis, final StringBuilder b) {
        this.axis(axis + "::", b);
        return WILDCARD_OR_NAME_OR_PREDICATE;
    }

    final NodeSelectorToStringBuilderMode axisSymbol(final String axis, final StringBuilder b) {
        this.axis(axis, b);
        return AXIS_SYMBOL;
    }

    final NodeSelectorToStringBuilderMode descendantOrSelf(final StringBuilder b) {
        final char c = NodeSelector.SEPARATOR.character();
        b.append(c);
        b.append(c);
        return AXIS_NAME_OR_DESCENDANT_OR_SELF;
    }

    private void axis(final String axis, final StringBuilder b) {
        if (this.requireFinishingWildcard()) {
            b.append('*');
            b.append(NodeSelector.SEPARATOR.character());
        } else {
            if (this.requireSeparator()) {
                b.append(NodeSelector.SEPARATOR.character());
            }
        }
        b.append(axis);
    }

    abstract boolean requireSeparator();

    // WILDCARD / NAME...................................................................................................

    final NodeSelectorToStringBuilderMode name(final Name name, final StringBuilder b) {
        if (this.requireSeparator()) {
            b.append(NodeSelector.SEPARATOR.character());
        }
        b.append(name.value());
        return PREDICATE;
    }

    // PREDICATE / COMPONENTS...................................................................................................

    final NodeSelectorToStringBuilderMode predicate(final Predicate<?> predicate, final StringBuilder b) {
        return this.predicateOrExpression(predicate.toString(), b);
    }

    final NodeSelectorToStringBuilderMode expression(final Expression expression, final StringBuilder b) {
        return this.predicateOrExpression(ExpressionNodeSelectorToStringExpressionVisitor.toString(expression), b);
    }

    private NodeSelectorToStringBuilderMode predicateOrExpression(final String string, final StringBuilder b) {
        if (this.requireWildcard()) {
            b.append('*');
        }
        b.append('[').append(string).append(']');
        return PREDICATE;
    }

    abstract boolean requireWildcard();

    final void finish(final StringBuilder b) {
        if (this.requireFinishingWildcard()) {
            b.append('*');
        }
    }

    abstract boolean requireFinishingWildcard();
}
