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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class BinaryExpressionNumberVisitorTest implements BinaryExpressionNumberVisitorTesting<BinaryExpressionNumberVisitor> {

    // BigDecimal.......................................................................................................

    @Test
    public void testAcceptBigDecimalBigDecimal() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigDecimal left, final BigDecimal right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(2));
    }

    @Test
    public void testAcceptBigDecimalBigInteger() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigDecimal left, final BigInteger right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigDecimal.valueOf(1),
                BigInteger.valueOf(2));
    }

    @Test
    public void testAcceptBigDecimalDouble() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigDecimal left, final Double right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigDecimal.valueOf(1),
                2.5);
    }

    @Test
    public void testAcceptBigDecimalLong() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigDecimal left, final Long right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigDecimal.valueOf(1),
                2L);
    }

    @Test
    public void testAcceptBigDecimalUnsupported() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigDecimal left, final BigDecimal right) {
                                    this.logVisit(left, right);
                                }

                                @Override
                                protected void visit(final BigDecimal left,
                                                     final Number right) {
                                    this.accept(left, BigDecimal.valueOf(right.intValue()));
                                }
                            },
                BigDecimal.valueOf(1),
                2,
                "startVisit BigDecimal 1 Integer 2", "startVisit BigDecimal 1 BigDecimal 2", "visit BigDecimal 1 BigDecimal 2", "endVisit BigDecimal 1 BigDecimal 2", "endVisit BigDecimal 1 Integer 2");
    }

    // BigInteger.......................................................................................................

    @Test
    public void testAcceptBigIntegerBigDecimal() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigInteger left, final BigDecimal right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigInteger.valueOf(1),
                BigDecimal.valueOf(2));
    }

    @Test
    public void testAcceptBigIntegerBigInteger() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigInteger left, final BigInteger right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigInteger.valueOf(1),
                BigInteger.valueOf(2));
    }

    @Test
    public void testAcceptBigIntegerDouble() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigInteger left, final Double right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigInteger.valueOf(1),
                2.0);
    }

    @Test
    public void testAcceptBigIntegerLong() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigInteger left, final Long right) {
                                    this.logVisit(left, right);
                                }
                            },
                BigInteger.valueOf(1),
                2L);
    }

    @Test
    public void testAcceptBigIntegerUnsupported() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final BigInteger left, final BigInteger right) {
                                    this.logVisit(left, right);
                                }

                                @Override
                                protected void visit(final BigInteger left,
                                                     final Number right) {
                                    this.accept(left, BigInteger.valueOf(right.intValue()));
                                }
                            },
                BigInteger.valueOf(1),
                2,
                "startVisit BigInteger 1 Integer 2", "startVisit BigInteger 1 BigInteger 2", "visit BigInteger 1 BigInteger 2", "endVisit BigInteger 1 BigInteger 2", "endVisit BigInteger 1 Integer 2");
    }

    // BigDouble.......................................................................................................

    @Test
    public void testAcceptDoubleBigDecimal() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Double left, final BigDecimal right) {
                                    this.logVisit(left, right);
                                }
                            },
                1.5,
                BigDecimal.valueOf(2));
    }

    @Test
    public void testAcceptDoubleBigInteger() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Double left, final BigInteger right) {
                                    this.logVisit(left, right);
                                }
                            },
                1.5,
                BigInteger.valueOf(2));
    }

    @Test
    public void testAcceptDoubleDouble() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Double left, final Double right) {
                                    this.logVisit(left, right);
                                }
                            },
                1.5,
                2.0);
    }

    @Test
    public void testAcceptDoubleLong() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Double left, final Long right) {
                                    this.logVisit(left, right);
                                }
                            },
                1.5,
                2L);
    }

    @Test
    public void testAcceptDoubleUnsupported() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Double left, final BigInteger right) {
                                    this.logVisit(left, right);
                                }

                                @Override
                                protected void visit(final Double left,
                                                     final Number right) {
                                    this.accept(left, BigInteger.valueOf(right.intValue()));
                                }
                            },
                1.5,
                2,
                "startVisit Double 1.5 Integer 2", "startVisit Double 1.5 BigInteger 2", "visit Double 1.5 BigInteger 2", "endVisit Double 1.5 BigInteger 2", "endVisit Double 1.5 Integer 2");
    }

    // BigInteger.......................................................................................................

    @Test
    public void testAcceptLongBigDecimal() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Long left, final BigDecimal right) {
                                    this.logVisit(left, right);
                                }
                            },
                1L,
                BigDecimal.valueOf(2));
    }

    @Test
    public void testAcceptLongBigInteger() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Long left, final BigInteger right) {
                                    this.logVisit(left, right);
                                }
                            },
                1L,
                BigInteger.valueOf(2));
    }

    @Test
    public void testAcceptLongDouble() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Long left, final Double right) {
                                    this.logVisit(left, right);
                                }
                            },
                1L,
                2.0);
    }

    @Test
    public void testAcceptLongLong() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Long left, final Long right) {
                                    this.logVisit(left, right);
                                }
                            },
                1L,
                2L);
    }

    @Test
    public void testAcceptLongUnsupported() {
        this.acceptAndCheck(new TestFakeBinaryExpressionNumberVisitor() {
                                @Override
                                protected void visit(final Long left, final BigInteger right) {
                                    this.logVisit(left, right);
                                }

                                @Override
                                protected void visit(final Long left,
                                                     final Number right) {
                                    this.accept(left, BigInteger.valueOf(right.intValue()));
                                }
                            },
                1L,
                2,
                "startVisit Long 1 Integer 2", "startVisit Long 1 BigInteger 2", "visit Long 1 BigInteger 2", "endVisit Long 1 BigInteger 2", "endVisit Long 1 Integer 2");
    }

    // helpers..........................................................................................................

    private void acceptAndCheck(final TestFakeBinaryExpressionNumberVisitor visitor,
                                final Number left,
                                final Number right) {
        this.acceptAndCheck(visitor,
                left,
                right,
                startVisit(left, right), visit(left, right), endVisit(left, right));
    }

    private void acceptAndCheck(final TestFakeBinaryExpressionNumberVisitor visitor,
                                final Number left,
                                final Number right,
                                final String... expected) {
        visitor.accept(left, right);
        assertEquals(Lists.of(expected), visitor.log);
    }

    abstract static class TestFakeBinaryExpressionNumberVisitor extends FakeBinaryExpressionNumberVisitor {
        TestFakeBinaryExpressionNumberVisitor() {
            super();
        }

        @Override
        protected Visiting startVisit(final Number left, final Number right) {
            this.log.add(BinaryExpressionNumberVisitorTest.startVisit(left, right));
            return Visiting.CONTINUE;
        }

        @Override
        protected void endVisit(final Number left, final Number right) {
            this.log.add(BinaryExpressionNumberVisitorTest.endVisit(left, right));
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }

        final void logVisit(final Number left, final Number right) {
            this.log.add(BinaryExpressionNumberVisitorTest.visit(left, right));
        }

        private final List<String> log = Lists.array();
    }

    private static String startVisit(final Number left, final Number right) {
        return "startVisit " + toString(left) + " " + toString(right);
    }

    private static String visit(final Number left, final Number right) {
        return "visit " + toString(left) + " " + toString(right);
    }

    private static String endVisit(final Number left, final Number right) {
        return "endVisit " + toString(left) + " " + toString(right);
    }

    private static String toString(final Number number) {
        return number.getClass().getSimpleName() + " " + number;
    }

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public BinaryExpressionNumberVisitor createVisitor() {
        return new FakeBinaryExpressionNumberVisitor();
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
    public Class<BinaryExpressionNumberVisitor> type() {
        return BinaryExpressionNumberVisitor.class;
    }
}
