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

import org.junit.jupiter.api.Test;
import walkingkooka.build.BuilderTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.ValueExpression;

import java.util.function.Predicate;

public final class NodeSelectorToStringBuilderTest implements ClassTesting2<NodeSelectorToStringBuilder>,
        BuilderTesting<NodeSelectorToStringBuilder, String> {

    @Test
    public void testAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axisName("axis1");
        this.buildAndCheck(b, "axis1::*");
    }

    @Test
    public void testAxisName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axisName("axis1");
        b.name(def2());
        this.buildAndCheck(b, "axis1::def2");
    }

    @Test
    public void testNameAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.axisName("axis2");
        this.buildAndCheck(b, "abc1/axis2::*");
    }

    @Test
    public void testAxisPredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axisName("axis1");
        b.predicate(predicate2());
        this.buildAndCheck(b, "axis1::*[j>2]");
    }

    @Test
    public void testPredicateAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate(predicate1());
        b.axisName("axis2");
        this.buildAndCheck(b, "*[i>1]/axis2::*");
    }

    @Test
    public void testAxisNamePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axisName("axis1");
        b.name(def2());
        b.predicate(predicate3());
        this.buildAndCheck(b, "axis1::def2[k>3]");
    }

    @Test
    public void testAxisNamePredicatePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axisName("axis1");
        b.name(def2());
        b.predicate(predicate3());
        b.predicate(predicate4());
        this.buildAndCheck(b, "axis1::def2[k>3][l>4]");
    }

    @Test
    public void testAxisPredicateName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axisName("axis1");
        b.predicate(predicate2());
        b.name(ghi3());
        this.buildAndCheck(b, "axis1::*[j>2]/ghi3");
    }

    @Test
    public void testName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        this.buildAndCheck(b, "abc1");
    }

    @Test
    public void testNamePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.predicate(predicate2());
        this.buildAndCheck(b, "abc1[j>2]");
    }

    @Test
    public void testNamePredicatePredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.predicate(predicate2());
        b.predicate(predicate3());
        this.buildAndCheck(b, "abc1[j>2][k>3]");
    }

    @Test
    public void testNamePredicatePredicateName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.predicate(predicate2());
        b.predicate(predicate3());
        b.name(jkl4());
        this.buildAndCheck(b, "abc1[j>2][k>3]/jkl4");
    }

    @Test
    public void testPredicateName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate(predicate1());
        b.name(def2());
        this.buildAndCheck(b, "*[i>1]/def2");
    }

    @Test
    public void testPredicateNamePredicateName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate(predicate1());
        b.name(def2());
        b.predicate(predicate3());
        b.name(jkl4());
        this.buildAndCheck(b, "*[i>1]/def2[k>3]/jkl4");
    }

    @Test
    public void testPredicate() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate(predicate1());
        this.buildAndCheck(b, "*[i>1]");
    }

    @Test
    public void testPredicateExpression() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.predicate(predicate1());
        b.expression(expressionNumber(22));
        this.buildAndCheck(b, "*[i>1][22]");
    }

    @Test
    public void testSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.self();
        this.buildAndCheck(b, ".");
    }

    @Test
    public void testSelfSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.self();
        b.self();
        this.buildAndCheck(b, "./.");
    }

    @Test
    public void testSelfParent() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.self();
        b.parent();
        this.buildAndCheck(b, "./..");
    }

    @Test
    public void testParent() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.parent();
        this.buildAndCheck(b, "..");
    }

    @Test
    public void testNameName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.name(def2());
        this.buildAndCheck(b, "abc1/def2");
    }

    @Test
    public void testNamePredicateName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.predicate(predicate2());
        b.name(ghi3());
        this.buildAndCheck(b, "abc1[j>2]/ghi3");
    }

    @Test
    public void testNamePredicateExpressionName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.predicate(predicate2());
        b.expression(expressionNumber(33));
        b.name(jkl4());
        this.buildAndCheck(b, "abc1[j>2][33]/jkl4");
    }

    @Test
    public void testNameSelfName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.self();
        b.name(ghi3());
        this.buildAndCheck(b, "abc1/./ghi3");
    }

    @Test
    public void testNameParentOf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.parent();
        this.buildAndCheck(b, "abc1/..");
    }

    @Test
    public void testNameParentOfName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.parent();
        b.name(ghi3());
        this.buildAndCheck(b, "abc1/../ghi3");
    }

    @Test
    public void testName3() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.name(def2());
        b.name(ghi3());
        this.buildAndCheck(b, "abc1/def2/ghi3");
    }

    @Test
    public void testNameNameAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.name(def2());
        b.axisName("axis3");
        this.buildAndCheck(b, "abc1/def2/axis3::*");
    }

    @Test
    public void testNameNameAxisName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.name(def2());
        b.axisName("axis3");
        b.axisName("axis4");
        this.buildAndCheck(b, "abc1/def2/axis3::*/axis4::*");
    }

    @Test
    public void testNameAxisNameAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.axisName("axis2");
        b.name(ghi3());
        b.axisName("self4");
        this.buildAndCheck(b, "abc1/axis2::ghi3/self4::*");
    }

    @Test
    public void testAxisNameNameAxis2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.axisName("axis1");
        b.name(def2());
        b.axisName("axis2");
        b.name(ghi3());
        this.buildAndCheck(b, "axis1::def2/axis2::ghi3");
    }

    @Test
    public void testNameSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.self();
        this.buildAndCheck(b, "abc1/.");
    }

    @Test
    public void testNameParent() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.parent();
        this.buildAndCheck(b, "abc1/..");
    }

    @Test
    public void testAbsolute() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute();
        this.buildAndCheck(b, "/");
    }

    @Test
    public void testAbsoluteName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute();
        b.name(abc1());
        this.buildAndCheck(b, "/abc1");
    }

    @Test
    public void testAbsoluteName2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute();
        b.name(abc1());
        b.name(def2());
        this.buildAndCheck(b, "/abc1/def2");
    }

    @Test
    public void testAbsoluteNamePredicateName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.absolute();
        b.name(abc1());
        b.predicate(predicate2());
        b.name(ghi3());
        this.buildAndCheck(b, "/abc1[j>2]/ghi3");
    }

    @Test
    public void testDescendantOrSelf() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf();
        this.buildAndCheck(b, "//");
    }

    @Test
    public void testDescendantOrSelfName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf();
        b.name(abc1());
        this.buildAndCheck(b, "//abc1");
    }

    @Test
    public void testDescendantOrSelfName2() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf();
        b.name(abc1());
        b.name(def2());
        this.buildAndCheck(b, "//abc1/def2");
    }

    @Test
    public void testDescendantOrSelfNamePredicateName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf();
        b.name(abc1());
        b.predicate(predicate2());
        b.name(ghi3());
        this.buildAndCheck(b, "//abc1[j>2]/ghi3");
    }

    @Test
    public void testNamePredicateAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf();
        b.name(abc1());
        b.predicate(predicate2());
        b.axisName("axis3");
        this.buildAndCheck(b, "//abc1[j>2]/axis3::*");
    }

    @Test
    public void testNameExpressionAxis() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf();
        b.name(abc1());
        b.expression(expressionNumber(2));
        b.axisName("axis3");
        this.buildAndCheck(b, "//abc1[2]/axis3::*");
    }

    @Test
    public void testNamePredicateAxisName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.descendantOrSelf();
        b.name(abc1());
        b.predicate(predicate2());
        b.axisName("axis3");
        b.name(jkl4());
        this.buildAndCheck(b, "//abc1[j>2]/axis3::jkl4");
    }

    @Test
    public void testNameExpressionBooleanTrue() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.expression(Expression.value(true));
        this.buildAndCheck(b, "abc1[true()]");
    }

    @Test
    public void testNameExpressionFunction() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.expression(Expression.function(
                FunctionExpressionName.with("def2"),
                Lists.of(expressionNumber(3))
        ));
        this.buildAndCheck(b, "abc1[def2(3)]");
    }

    @Test
    public void testNameExpressionBooleanTrueName() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.expression(Expression.value(true));
        b.name(ghi3());
        this.buildAndCheck(b, "abc1[true()]/ghi3");
    }

    @Test
    public void testCustomToString() {
        final String custom = "custom123";

        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.customToString(custom);
        this.buildAndCheck(b, custom);
    }

    @Test
    public void testNameCustomToString() {
        final String custom = "custom123";

        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.name(abc1());
        b.customToString(custom);
        this.buildAndCheck(b, "abc1/" + custom);
    }

    @Test
    public void testCustomToStringAxis() {
        final String custom = "custom123";

        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.customToString(custom);
        b.axisName("axis2");
        this.buildAndCheck(b, custom + "/axis2::*");
    }

    @Test
    public void testCustomToStringSelf() {
        final String custom = "custom123";

        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.customToString(custom);
        b.self();
        this.buildAndCheck(b, custom + "/.");
    }

    @Test
    public void testCustomToStringName() {
        final String custom = "custom123";

        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.customToString(custom);
        b.name(def2());
        this.buildAndCheck(b, custom + "/def2");
    }

    @Test
    public void testCustomToStringNamePredicate() {
        final String custom = "custom123";

        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.customToString(custom);
        b.name(def2());
        b.predicate(predicate3());
        this.buildAndCheck(b, custom + "/def2[k>3]");
    }

    @Test
    public void testCustomToStringPredicate() {
        final String custom = "custom123";

        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.customToString(custom);
        b.predicate(predicate2());
        this.buildAndCheck(b, custom + "[j>2]");
    }

    private ValueExpression<ExpressionNumber> expressionNumber(final double value) {
        return Expression.value(
                ExpressionNumberKind.DEFAULT.create(value)
        );
    }

    private StringName abc1() {
        return Names.string("abc1");
    }

    private StringName def2() {
        return Names.string("def2");
    }

    private StringName ghi3() {
        return Names.string("ghi3");
    }

    private StringName jkl4() {
        return Names.string("jkl4");
    }

    private Predicate<?> predicate1() {
        return predicate(1);
    }

    private Predicate<?> predicate2() {
        return predicate(2);
    }

    private Predicate<?> predicate3() {
        return predicate(3);
    }

    private Predicate<?> predicate4() {
        return predicate(4);
    }

    private Predicate<?> predicate(final int value) {
        final char c = (char) ('i' + value - 1);
        return Predicates.customToString(Predicates.fake(), c + ">" + value);
    }

    @Test
    public void testToString() {
        final NodeSelectorToStringBuilder b = NodeSelectorToStringBuilder.empty();
        b.append("1234");
        this.toStringAndCheck(b, "1234 (AXIS_NAME_OR_DESCENDANT_OR_SELF)");
    }

    @Override
    public NodeSelectorToStringBuilder createBuilder() {
        return NodeSelectorToStringBuilder.empty();
    }

    @Override
    public Class<String> builderProductType() {
        return String.class;
    }

    @Override
    public Class<NodeSelectorToStringBuilder> type() {
        return NodeSelectorToStringBuilder.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
