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

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ExpressionNumberVisitorTest implements ExpressionNumberVisitorTesting<ExpressionNumberVisitor> {

    @Test
    public void testAcceptBigDecimal() {
        new ExpressionNumberVisitor() {
        }.accept(BigDecimal.valueOf(123.5));
    }

    @Test
    public void testAcceptBigDecima2l() {
        this.acceptAndCheck(new TestFakeExpressionNumberVisitor() {
            @Override
            protected void visit(final BigDecimal number) {
                ExpressionNumberVisitorTest.this.visit = number;
            }
        }, BigDecimal.valueOf(123.5));
    }

    @Test
    public void testAcceptBigInteger() {
        new ExpressionNumberVisitor() {
        }.accept(BigInteger.valueOf(123));
    }

    @Test
    public void testAcceptBigInteger2() {
        this.acceptAndCheck(new TestFakeExpressionNumberVisitor() {
            @Override
            protected void visit(final BigInteger number) {
                ExpressionNumberVisitorTest.this.visit = number;
            }
        }, BigInteger.valueOf(123));
    }

    @Test
    public void testAcceptDouble() {
        new ExpressionNumberVisitor() {
        }.accept(123.5);
    }

    @Test
    public void testAcceptDouble2() {
        this.acceptAndCheck(new TestFakeExpressionNumberVisitor() {
            @Override
            protected void visit(final Double number) {
                ExpressionNumberVisitorTest.this.visit = number;
            }
        }, 123.5);
    }

    @Test
    public void testAcceptLong() {
        new ExpressionNumberVisitor() {
        }.accept(123L);
    }

    @Test
    public void testAcceptLong2() {
        this.acceptAndCheck(new TestFakeExpressionNumberVisitor() {
            @Override
            protected void visit(final Long number) {
                ExpressionNumberVisitorTest.this.visit = number;
            }
        }, 123L);
    }

    @Test
    public void testAcceptRetry() {
        this.acceptAndCheck(new TestFakeExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigDecimal number) {
                                    ExpressionNumberVisitorTest.this.visit = number;
                                }

                                @Override
                                protected Number visit(final Number number) {
                                    return BigDecimal.valueOf(number.intValue());
                                }
                            }, 123,
                BigDecimal.valueOf(123));
    }

    @Test
    public void testAcceptUnsupportedNumberTypeFails() {
        this.acceptFails(new ExpressionNumberVisitor() {
        }, 123);
    }

    @Test
    public void testAcceptUnsupportedNumberTypeFails2() {
        this.acceptFails(new TestFakeExpressionNumberVisitor() {
            @Override
            protected void visit(final BigDecimal number) {
                ExpressionNumberVisitorTest.this.visit = number;
            }

            @Override
            protected Number visit(final Number number) {
                return number;
            }
        }, 123);
    }

    private void acceptFails(final ExpressionNumberVisitor visitor,
                             final Number number) {
        assertThrows(ExpressionException.class,
                () -> visitor.accept(number));
    }

    private void acceptAndCheck(final TestFakeExpressionNumberVisitor visitor,
                                final Number number) {
        this.acceptAndCheck(visitor, number, number);
    }

    private void acceptAndCheck(final TestFakeExpressionNumberVisitor visitor,
                                final Number number,
                                final Number expected) {
        this.start = null;
        this.visit = null;
        this.end = null;

        visitor.accept(number);

        assertSame(number, this.start, "start");
        assertEquals(expected, this.visit, "visit");
        assertSame(number, this.end, "end");
    }

    abstract class TestFakeExpressionNumberVisitor extends FakeExpressionNumberVisitor {
        TestFakeExpressionNumberVisitor() {
            super();
        }

        @Override
        protected Visiting startVisit(final Number number) {
            ExpressionNumberVisitorTest.this.start = number;
            return Visiting.CONTINUE;
        }

        @Override
        protected void endVisit(final Number number) {
            ExpressionNumberVisitorTest.this.end = number;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    private Number start;
    private Number visit;
    private Number end;

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public void testVisitReturnTypeVoid() {
    }

    @Override
    public ExpressionNumberVisitor createVisitor() {
        return new FakeExpressionNumberVisitor();
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
    public Class<ExpressionNumberVisitor> type() {
        return ExpressionNumberVisitor.class;
    }
}
