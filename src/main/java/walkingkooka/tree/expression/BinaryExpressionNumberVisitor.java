/*
 * Copyright 2020 Miroslav Pokorny (github.com/mP1)
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/**
 * A visitor that accepts two {@link Number} and attempts to dispatch to the best visit overload.
 */
public class BinaryExpressionNumberVisitor {

    protected BinaryExpressionNumberVisitor() {
        super();
    }

    @SuppressWarnings("NumberEquality")
    public final void accept(final Number left,
                             final Number right) {
        Objects.requireNonNull(left, "left");

        do {
            if (left instanceof BigDecimal) {
                this.accept((BigDecimal) left, right);
                break;
            }
            if (left instanceof BigInteger) {
                this.accept((BigInteger) left, right);
                break;
            }
            if (left instanceof Double) {
                this.accept((Double) left, right);
                break;
            }
            if (left instanceof Long) {
                this.accept((Long) left, right);
                break;
            }
            this.visit(left, right);
        } while (false);
    }

    protected Visiting startVisit(final Number left,
                                  final Number right) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final Number left,
                            final Number right) {
    }

    /**
     * This is called whenever left is not one of the following types {@link BigDecimal}, {@link BigInteger}, {@link Double} or {@link Long}.
     */
    protected void visit(final Number left, final Number right) {

    }

    // BIG DECIMAL......................................................................................................

    public void accept(final BigDecimal left,
                       final Number right) {
        if (Visiting.CONTINUE == this.startVisit(left, right)) {
            do {
                if (right instanceof BigDecimal) {
                    this.visit(left, (BigDecimal) right);
                    break;
                }
                if (right instanceof BigInteger) {
                    this.visit(left, (BigInteger) right);
                    break;
                }
                if (right instanceof Double) {
                    this.visit(left, (Double) right);
                    break;
                }
                if (right instanceof Long) {
                    this.visit(left, (Long) right);
                    break;
                }
                this.visit(left, right);
            } while (false);
        }
        this.endVisit(left, right);
    }

    protected void visit(final BigDecimal left, final BigDecimal right) {
        // nop
    }

    protected void visit(final BigDecimal left, final BigInteger right) {
        // nop
    }

    protected void visit(final BigDecimal left, final Double right) {
        // nop
    }

    protected void visit(final BigDecimal left, final Long right) {
        // nop
    }

    protected void visit(final BigDecimal left, final Number right) {
        // nop
    }

    // BIG INTEGER......................................................................................................

    public void accept(final BigInteger left,
                       final Number right) {
        if (Visiting.CONTINUE == this.startVisit(left, right)) {
            do {
                if (right instanceof BigDecimal) {
                    this.visit(left, (BigDecimal) right);
                    break;
                }
                if (right instanceof BigInteger) {
                    this.visit(left, (BigInteger) right);
                    break;
                }
                if (right instanceof Double) {
                    this.visit(left, (Double) right);
                    break;
                }
                if (right instanceof Long) {
                    this.visit(left, (Long) right);
                    break;
                }
                this.visit(left, right);
            } while (false);
        }
        this.endVisit(left, right);
    }

    protected void visit(final BigInteger left, final BigDecimal right) {
        // nop
    }

    protected void visit(final BigInteger left, final BigInteger right) {
        // nop
    }

    protected void visit(final BigInteger left, final Double right) {
        // nop
    }

    protected void visit(final BigInteger left, final Long right) {
        // nop
    }

    protected void visit(final BigInteger left, final Number right) {
        // nop
    }

    // DOUBLE...........................................................................................................

    public void accept(final Double left,
                       final Number right) {
        if (Visiting.CONTINUE == this.startVisit(left, right)) {
            do {
                if (right instanceof BigDecimal) {
                    this.visit(left, (BigDecimal) right);
                    break;
                }
                if (right instanceof BigInteger) {
                    this.visit(left, (BigInteger) right);
                    break;
                }
                if (right instanceof Double) {
                    this.visit(left, (Double) right);
                    break;
                }
                if (right instanceof Long) {
                    this.visit(left, (Long) right);
                    break;
                }
                this.visit(left, right);
            } while (false);
        }
        this.endVisit(left, right);
    }

    protected void visit(final Double left, final BigDecimal right) {
        // nop
    }

    protected void visit(final Double left, final BigInteger right) {
        // nop
    }

    protected void visit(final Double left, final Double right) {
        // nop
    }

    protected void visit(final Double left, final Long right) {
        // nop
    }

    protected void visit(final Double left, final Number right) {
        // nop
    }
    // LONG.............................................................................................................

    public void accept(final Long left,
                       final Number right) {
        if (Visiting.CONTINUE == this.startVisit(left, right)) {
            do {
                if (right instanceof BigDecimal) {
                    this.visit(left, (BigDecimal) right);
                    break;
                }
                if (right instanceof BigInteger) {
                    this.visit(left, (BigInteger) right);
                    break;
                }
                if (right instanceof Double) {
                    this.visit(left, (Double) right);
                    break;
                }
                if (right instanceof Long) {
                    this.visit(left, (Long) right);
                    break;
                }
                this.visit(left, right);
            } while (false);
        }
        this.endVisit(left, right);
    }

    protected void visit(final Long left, final BigDecimal right) {
        // nop
    }

    protected void visit(final Long left, final BigInteger right) {
        // nop
    }

    protected void visit(final Long left, final Double right) {
        // nop
    }

    protected void visit(final Long left, final Long right) {
        // nop
    }

    protected void visit(final Long left, final Number right) {
        // nop
    }
}
