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

package walkingkooka.tree.expression;

import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * A {@link Visitor} that dispatches to a visit overload for each fo the supported {@link Number} types.
 */
public abstract class ExpressionNumberVisitor extends Visitor<Number> {

    protected ExpressionNumberVisitor() {
        super();
    }

    @SuppressWarnings("NumberEquality")
    @Override
    public final void accept(final Number number) {
        Objects.requireNonNull(number, "number");

        if (Visiting.CONTINUE == this.startVisit(number)) {
            Number n = number;

            Exit:
            do {
                do {
                    if (n instanceof BigDecimal) {
                        this.visit((BigDecimal) n);
                        break Exit;
                    }
                    if (n instanceof BigInteger) {
                        this.visit((BigInteger) n);
                        break Exit;
                    }
                    if (n instanceof Double) {
                        this.visit((Double) n);
                        break Exit;
                    }
                    if (n instanceof Long) {
                        this.visit((Long) n);
                        break Exit;
                    }
                    n = this.visit(n);
                } while (n != number);

                throw new ExpressionException("Unsupported Number type " + number.getClass().getName() + "=" + number);
            } while (false);
        }
        this.endVisit(number);
    }

    protected Visiting startVisit(final Number number) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final Number number) {
    }

    protected void visit(final BigDecimal number) {
    }

    protected void visit(final BigInteger number) {
    }

    protected void visit(final Double number) {
    }

    protected void visit(final Long number) {
    }

    /**
     * Invoked for non native {@link Number} types. The default behaviour is to return the same {@link Number} but other
     * options include converting the given {@link Number} to one of the supported {@link Number} types.
     */
    protected Number visit(final Number number) {
        return number;
    }
}
