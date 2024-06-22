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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.expression.ExpressionEvaluationContexts;
import walkingkooka.tree.expression.ExpressionNumber;
import walkingkooka.tree.expression.ExpressionNumberConverterContext;
import walkingkooka.tree.expression.ExpressionNumberConverterContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.ValueExpression;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.expression.function.FakeExpressionFunction;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;
import walkingkooka.tree.select.parser.NodeSelectorParserContexts;
import walkingkooka.tree.select.parser.NodeSelectorParserTokenVisitorTesting;
import walkingkooka.tree.select.parser.NodeSelectorParsers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class NodeSelectorNodeSelectorParserTokenVisitorTest implements NodeSelectorParserTokenVisitorTesting<NodeSelectorNodeSelectorParserTokenVisitor<TestNode, StringName, StringName, Object>> {

    private final static ExpressionNumberKind EXPRESSION_NUMBER_KIND = ExpressionNumberKind.DEFAULT;

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testAbsoluteNodeNameEvaluate() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionEvaluateAndCheck("/branch",
                root,
                branch);
    }

    @Test
    public void testAbsoluteWildcardEvaluate() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionEvaluateAndCheck("/*",
                root,
                branch);
        this.parseExpressionEvaluateAndCheck("/*",
                root.child(0), // branch
                branch);
    }

    @Test
    public void testAbsoluteChildAxisWildcardEvaluate() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionEvaluateAndCheck("/child::*",
                root,
                branch);
    }

    @Test
    public void testAbsoluteDescendantOrSelfWildcardEvaluate() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionEvaluateAndCheck("//*",
                root,
                root, branch, leaf);
        this.parseExpressionEvaluateAndCheck("//*",
                root.child(0).child(0),
                leaf);
    }

    @Test
    public void testDescendantOrSelfNodeNameEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("//leaf1",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//leaf1",
                root.child(1));
    }

    @Test
    public void testDescendantOrSelfWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("//*",
                root,
                root, branch1, leaf1, branch2, leaf2, branch3, leaf3);
        this.parseExpressionEvaluateAndCheck("//*",
                root.child(1),
                branch2, leaf2);
    }

    @Test
    public void testDescendantOfSelfNodeNameWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("//root/*",
                root,
                branch1, branch2, branch3);
        this.parseExpressionEvaluateAndCheck("//branch1/*",
                root.child(0),
                leaf1);
    }

    @Test
    public void testAbsoluteNodeNameDescendantOrSelfWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branchOfBranch3 = node("branchOfBranch3", leaf3);
        final TestNode branch3 = node("branch3", branchOfBranch3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("/branch2//*",
                root.child(1),
                branch2, leaf2);
        this.parseExpressionEvaluateAndCheck("/branch3//*",
                root.child(2),
                branch3, branchOfBranch3, leaf3);
    }

    @Test
    public void testRelativeNodeNameDescendantOrSelfWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branchOfBranch3 = node("branchOfBranch3", leaf3);
        final TestNode branch3 = node("branch3", branchOfBranch3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("branch2//*",
                root,
                branch2, leaf2);
        this.parseExpressionEvaluateAndCheck("branch3//*",
                root,
                branch3, branchOfBranch3, leaf3);
    }

    @Test
    public void testAbsoluteNodeNameNodeNameEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("/branch2/*",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("/branch2/wrong/*",
                root);
    }

    @Test
    public void testRelativeNodeNameNodeNameEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("branch2/leaf2",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("branch2",
                root,
                branch2);
        this.parseExpressionEvaluateAndCheck("branch2",
                root.child(0));
    }

    @Test
    public void testAbsoluteNodeNameWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("/branch2/*",
                root.child(1),
                leaf2);
        this.parseExpressionEvaluateAndCheck("/branch3/*",
                root.child(2),
                leaf3);
    }

    @Test
    public void testRelativeNodeNameWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("branch2/leaf2",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("branch3/wrong",
                root.child(2));
        this.parseExpressionEvaluateAndCheck("branch3/leaf3",
                root,
                leaf3);
    }

    // predicate EQ ...................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsStringEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id=\"attribute-value-1\"]",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id=\"attribute-value-2\"]",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@unknown=\"*\"]",
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsStringMissingEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id=\"attribute-value-1\"]",
                root,
                leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsNumberEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id=1]",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id=2]",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id=999]",
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsNumberConversionEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionEvaluateAndCheck("//*[@id=1]",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id=2]",
                root,
                leaf2);
    }

    // predicate GT ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanStringEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id>\"a\"]",
                root,
                leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id>\"attribute-value-1\"]",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@unknown>\"z\"]",
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanStringMissingEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id>\"a\"]",
                root,
                leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanNumberEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id>0]",
                root,
                leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id>1]",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id>999]",
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanNumberConversionEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionEvaluateAndCheck("//*[@id>0]",
                root,
                leaf1, leaf2, leaf3);
        this.parseExpressionEvaluateAndCheck("//*[@id>1]",
                root,
                leaf2, leaf3);
        this.parseExpressionEvaluateAndCheck("//*[@id>999]",
                root,
                leaf3);
    }

    // predicate GTE ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsStringEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id>=\"attribute-value-1\"]",
                root,
                leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id>=\"attribute-value-2\"]",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@unknown>=\"z\"]",
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsStringMissingEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id>=\"a\"]",
                root,
                leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsNumberEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id>=0]",
                root,
                leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id>=2]",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id>=999]",
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsNumberConversionEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        //assertTrue("0".compareTo("NAN") >= 0);
        this.parseExpressionEvaluateAndCheck("//*[@id>=0]",
                root,
                leaf1, leaf2, leaf3);
        this.parseExpressionEvaluateAndCheck("//*[@id>=2]",
                root,
                leaf2, leaf3);
        this.parseExpressionEvaluateAndCheck("//*[@id>=999]",
                root,
                leaf3);
    }

    // predicate LT ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanStringEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id<\"attribute-value-2\"]",
                root,
                root, leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id<\"attribute-value-1\"]",
                root,
                root);
        this.parseExpressionEvaluateAndCheck("//*[@unknown<\".\"]",
                root,
                root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanStringMissingEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id<\"z\"]",
                root,
                root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanNumberEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id<3]",
                root,
                root, leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id<2]",
                root,
                root, leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id<-1]",
                root,
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanNumberConversionEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionEvaluateAndCheck("//*[@id<7]",
                root,
                root, leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id<2]",
                root,
                root, leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id<1]",
                root,
                root);
        this.parseExpressionEvaluateAndCheck("//*[@id<-999]",
                root,
                root);
    }

    // predicate LTE ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsStringEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id<=\"attribute-value-2\"]",
                root,
                root, leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id<=\"attribute-value-1\"]",
                root,
                root, leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@unknown<=\".\"]",
                root,
                root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsStringMissingEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id<=\"z\"]",
                root,
                root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsNumberEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id<=2]",
                root,
                root, leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id<=1]",
                root,
                root, leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id<=-1]",
                root,
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsNumberConversionEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionEvaluateAndCheck("//*[@id<=77]",
                root,
                root, leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id<=3]",
                root,
                root, leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id<=1]",
                root,
                root, leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id<=-999]",
                root,
                root);
    }

    // predicate NE ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsStringEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id!=\"attribute-value-1\"]",
                root,
                root, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id!=\"attribute-value-2\"]",
                root,
                root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsStringMissingEvaluate() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id!=\"attribute-value-1\"]",
                root,
                root, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsNumberEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[@id!=1]",
                root,
                root, leaf2);
        this.parseExpressionEvaluateAndCheck("//*[@id!=2]",
                root,
                root, leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id!=999]",
                root,
                root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsNumberConversionEvaluate() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionEvaluateAndCheck("//*[@id!=1]",
                root,
                root, leaf2, leaf3);
        this.parseExpressionEvaluateAndCheck("//*[@id!=2]",
                root, root, leaf1, leaf3);
    }

    // starts with ....................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWithEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "1x");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[starts-with(@id, \"a\")]",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//*[starts-with(@id, \"1\")]",
                root,
                leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWithMissingAttributeEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[starts-with(@id, \"a\")]",
                root,
                leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWithIntegerValueEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "1a");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[starts-with(@id, 1)]",
                root,
                leaf2);
        this.parseExpressionEvaluateAndCheck("//*[starts-with(@id, 9)]",
                root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWithIntegerValueEvaluate2() {
        final TestNode leaf1 = node("leaf1", 345);
        final TestNode leaf2 = node("leaf2", 567);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[starts-with(@id, \"3\")]",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//*[starts-with(@id, \"56\")]",
                root,
                leaf2);
    }

    // namedFunction: position().......................................................................................

    @Test
    public void testWildcardExpressionPosition() {
        this.parseExpressionAndCheck("*[position() = 2]",
                TestNode.relativeNodeSelector()
                        .children()
                        .expression(
                                Expression.equalsExpression(
                                        function("position"),
                                        expressionNumberExpression(2)
                                )));
    }

    @Test
    public void testChildrenNamedExpressionPosition() {
        this.parseExpressionAndCheck("ABC123[position() = 2]",
                TestNode.relativeNodeSelector()
                        .children()
                        .named(nameAbc123())
                        .expression(
                                Expression.equalsExpression(
                                        function("position"),
                                        expressionNumberExpression(2)
                                )));
    }

    //@Test
    // https://github.com/mP1/walkingkooka-tree/issues/203
    public void testWildcardExpressionPositionEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3a = node("leaf3a");
        final TestNode leaf3b = node("leaf3b");
        final TestNode branch3 = node("branch3", leaf3a, leaf3b);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionEvaluateAndCheck("*[position() = 2]",
                root,
                branch2);
        this.parseExpressionEvaluateAndCheck("*[position() = 2]",
                root.child(0));
    }

    // multiple predicates ........................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueStringTwiceEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "x");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[string-length(@id)=3][@id=\"abc\"]",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//*[@id=\"abc\"][string-length(@id)=3]",
                root,
                leaf1);
    }

    // wildcard ........................................................................................................

    @Test
    public void testWildcard() {
        this.parseExpressionAndCheck("*",
                TestNode.relativeNodeSelector().children());
    }

    // ancestor.......................................................................................

    @Test
    public void testAncestorAxisWildcard() {
        this.parseExpressionAndCheck("ancestor::*",
                TestNode.relativeNodeSelector().ancestor());
    }

    @Test
    public void testAncestorAxisNamed() {
        this.parseExpressionAndCheck("ancestor::ABC",
                TestNode.relativeNodeSelector()
                        .ancestor()
                        .named(Names.string("ABC")));
    }

    @Test
    public void testAncestorAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("ancestor::*",
                root.child(0),
                root);
    }

    @Test
    public void testAncestorAxisWildcardEvaluate2() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("ancestor::*",
                root.child(0).child(0), // leaf1
                branch1, root);
    }

    // ancestor-or-self.......................................................................................

    @Test
    public void testAncestorOrSelfAxisWildcard() {
        this.parseExpressionAndCheck("ancestor-or-self::*",
                TestNode.relativeNodeSelector()
                        .ancestorOrSelf());
    }

    @Test
    public void testAncestorOrSelfAxisNamed() {
        this.parseExpressionAndCheck("ancestor-or-self::ABC",
                TestNode.relativeNodeSelector()
                        .ancestorOrSelf()
                        .named(Names.string("ABC")));
    }

    @Test
    public void testAncestorOrSelfAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("ancestor-or-self::*",
                root.child(0).child(0), // leaf1
                leaf1, branch1, root);
    }

    public void testAncestorOrSelfAxisWildcardEvaluate2() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("ancestor-or-self::*",
                root.child(0), // branch1
                branch1, root);
    }

    // child.......................................................................................

    @Test
    public void testChildAxisWildcard() {
        this.parseExpressionAndCheck("child::*",
                TestNode.relativeNodeSelector().children());
    }

    @Test
    public void testChildAxisNamed() {
        this.parseExpressionAndCheck("child::ABC",
                TestNode.relativeNodeSelector().children().named(Names.string("ABC")));
    }

    @Test
    public void testChildAxisNamedWildcard() {
        this.parseExpressionAndCheck("child::ABC/*",
                TestNode.relativeNodeSelector().children().named(Names.string("ABC")).children());
    }

    @Test
    public void testAbsoluteNamedChildAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("/child::*",
                root,
                branch1, branch2);
        this.parseExpressionEvaluateAndCheck("/branch1/child::*",
                root.child(0),  // branch1
                leaf1);
    }

    // descendant.......................................................................................

    @Test
    public void testDescendantOrSelfWildcard() {
        this.parseExpressionAndCheck("//*",
                TestNode.relativeNodeSelector().descendantOrSelf());
    }

    @Test
    public void testDescendantOrSelfNamed() {
        this.parseExpressionAndCheck("//ABC123",
                TestNode.relativeNodeSelector().descendantOrSelf()
                        .named(nameAbc123()));
    }

    @Test
    public void testDescendantOrSelfNamed1Named2() {
        this.parseExpressionAndCheck("//ABC123/DEF456",
                TestNode.relativeNodeSelector()
                        .descendantOrSelf()
                        .named(nameAbc123())
                        .children()
                        .named(nameDef456()));
    }

    @Test
    public void testDescendantAxisWildcard() {
        this.parseExpressionAndCheck("descendant::*",
                TestNode.relativeNodeSelector().descendant());
    }

    @Test
    public void testDescendantAxisNamed() {
        this.parseExpressionAndCheck("descendant::ABC",
                TestNode.relativeNodeSelector().descendant().named(Names.string("ABC")));
    }

    @Test
    public void testDescendantAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("descendant::*",
                root.child(0),
                leaf1);
    }

    // descendant-or-self.......................................................................................

    @Test
    public void testDescendantOrSelfAxisWildcard() {
        this.parseExpressionAndCheck("descendant-or-self::*",
                TestNode.relativeNodeSelector().descendantOrSelf());
    }

    @Test
    public void testDescendantOrSelfAxisNamed() {
        this.parseExpressionAndCheck("descendant-or-self::ABC",
                TestNode.relativeNodeSelector().descendantOrSelf().named(Names.string("ABC")));
    }

    @Test
    public void testDescendantOrSelfAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("descendant-or-self::*",
                root.child(0),
                branch1, leaf1);
    }

    // first-child.......................................................................................

    @Test
    public void testFirstChildAxisWildcard() {
        this.parseExpressionAndCheck("first-child::*",
                TestNode.relativeNodeSelector().firstChild());
    }

    @Test
    public void testFirstChildAxisNamed() {
        this.parseExpressionAndCheck("first-child::ABC",
                TestNode.relativeNodeSelector().firstChild().named(Names.string("ABC")));
    }

    @Test
    public void testFirstChildAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("//first-child::*",
                root,
                branch1, leaf1, leaf2);
        this.parseExpressionEvaluateAndCheck("first-child::*",
                root.child(0),
                leaf1);
    }

    // following...........................................................................................

    @Test
    public void testFollowingAxisWildcard() {
        this.parseExpressionAndCheck("following::*",
                TestNode.relativeNodeSelector().following());
    }

    @Test
    public void testFollowingAxisNamed() {
        this.parseExpressionAndCheck("following::ABC",
                TestNode.relativeNodeSelector().following().named(Names.string("ABC")));
    }

    @Test
    public void testFollowingWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode leaf4 = node("leaf4");
        final TestNode branch4 = node("branch4", leaf4);

        final TestNode root = node("root", branch1, branch2, branch3, branch4);

        this.parseExpressionEvaluateAndCheck("following::*",
                root);
        this.parseExpressionEvaluateAndCheck("following::*",
                root.child(0),
                branch2, leaf2, branch3, leaf3, branch4, leaf4);
        this.parseExpressionEvaluateAndCheck("following::*",
                root.child(1),
                branch3, leaf3, branch4, leaf4);
    }

    // following-sibling...........................................................................................

    @Test
    public void testFollowingSiblingAxisWildcard() {
        this.parseExpressionAndCheck("following-sibling::*",
                TestNode.relativeNodeSelector().followingSibling());
    }

    @Test
    public void testFollowingSiblingAxisNamed() {
        this.parseExpressionAndCheck("following-sibling::ABC",
                TestNode.relativeNodeSelector().followingSibling().named(Names.string("ABC")));
    }

    @Test
    public void testFollowingSiblingAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("following-sibling::*",
                root.child(0),
                branch2);
        this.parseExpressionEvaluateAndCheck("following-sibling::*",
                root.child(1));
    }

    // number...........................................................................................

    @Test
    public void testAbsoluteWildcardNumberEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("/*[2]",
                root,
                branch2);
    }

    @Test
    public void testAbsoluteNamedNumberEvaluate() {
        TestNode.disableUniqueNameChecks();

        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch", leaf2);

        final TestNode root = node("root", TestNode.with("skip"), branch1, branch2);

        this.parseExpressionEvaluateAndCheck("/branch[1]",
                root,
                branch1);
    }

    @Test
    public void testAbsoluteNamedNumberEvaluate2() {
        TestNode.disableUniqueNameChecks();

        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch", leaf2);

        final TestNode root = node("root", TestNode.with("skip"), branch1, branch2);

        this.parseExpressionEvaluateAndCheck("/branch[2]",
                root,
                branch2);
    }

    // last-child.......................................................................................

    @Test
    public void testLastChildAxisWildcard() {
        this.parseExpressionAndCheck("last-child::*",
                TestNode.relativeNodeSelector().lastChild());
    }

    @Test
    public void testLastChildAxisNamed() {
        this.parseExpressionAndCheck("last-child::ABC",
                TestNode.relativeNodeSelector().lastChild().named(Names.string("ABC")));
    }

    @Test
    public void testLastChildWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("//last-child::*",
                root,
                leaf1, branch2, leaf2);
        this.parseExpressionEvaluateAndCheck("last-child::*",
                root.child(0),
                leaf1);
    }

    // named...........................................................................................

    @Test
    public void testNamed() {
        this.parseExpressionAndCheck("ABC123",
                TestNode.relativeNodeSelector()
                        .children()
                        .named(nameAbc123()));
    }

    @Test
    public void testNamedSlashNamed() {
        this.parseExpressionAndCheck("ABC123/DEF456",
                TestNode.relativeNodeSelector()
                        .children()
                        .named(nameAbc123())
                        .children()
                        .named(nameDef456()));
    }

    @Test
    public void testNamedSlashWildcard() {
        this.parseExpressionAndCheck("ABC123/*",
                TestNode.relativeNodeSelector()
                        .children()
                        .named(nameAbc123())
                        .children());
    }

    @Test
    public void testDescendantOrSelfNamedEvaluate() {
        TestNode.disableUniqueNameChecks();

        final TestNode leaf1 = node("ABC123");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("ABC123", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("descendant-or-self::ABC123",
                root,
                leaf1, branch2);
    }

    // parent.......................................................................................

    @Test
    public void testParentDotDot() {
        this.parseExpressionAndCheck("..",
                TestNode.relativeNodeSelector().parent());
    }

    @Test
    public void testParentDotDotEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("..",
                root.child(0),
                root);
    }

    @Test
    public void testParentAxisWildcard() {
        this.parseExpressionAndCheck("parent::*",
                TestNode.relativeNodeSelector().parent());
    }

    @Test
    public void testParentAxisNamed() {
        this.parseExpressionAndCheck("parent::ABC123",
                TestNode.relativeNodeSelector()
                        .parent()
                        .named(nameAbc123()));
    }

    @Test
    public void testParentAxisNamedNamed() {
        this.parseExpressionAndCheck("parent::ABC123/DEF456",
                TestNode.relativeNodeSelector().parent()
                        .named(nameAbc123())
                        .children()
                        .named(nameDef456()));
    }

    @Test
    public void testParentAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("parent::*",
                root.child(0),
                root);
    }

    // preceding...........................................................................................

    @Test
    public void testPrecedingAxisWildcard() {
        this.parseExpressionAndCheck("preceding::*",
                TestNode.relativeNodeSelector()
                        .preceding());
    }

    @Test
    public void testPrecedingAxisNamed() {
        this.parseExpressionAndCheck("preceding::ABC123",
                TestNode.relativeNodeSelector()
                        .preceding()
                        .named(nameAbc123()));
    }

    @Test
    public void testPrecedingAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode leaf4 = node("leaf4");
        final TestNode branch4 = node("branch4", leaf4);

        final TestNode root = node("root", branch1, branch2, branch3, branch4);

        this.parseExpressionEvaluateAndCheck("preceding::*",
                root.child(0));
        this.parseExpressionEvaluateAndCheck("preceding::*",
                root.child(1),
                branch1, leaf1);
        this.parseExpressionEvaluateAndCheck("preceding::*",
                root.child(2),
                branch2, leaf2, branch1, leaf1);
    }

    // preceding-sibling...........................................................................................

    @Test
    public void testPrecedingSiblingAxisWildcard() {
        this.parseExpressionAndCheck("preceding-sibling::*",
                TestNode.relativeNodeSelector()
                        .precedingSibling());
    }

    @Test
    public void testPrecedingSiblingAxisNamed() {
        this.parseExpressionAndCheck("preceding-sibling::ABC123",
                TestNode.relativeNodeSelector()
                        .precedingSibling()
                        .named(nameAbc123()));
    }

    @Test
    public void testPrecedingSiblingAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("preceding-sibling::*",
                root.child(1),
                branch1);
        this.parseExpressionEvaluateAndCheck("preceding-sibling::*",
                root.child(0));
    }

    // self.......................................................................................

    @Test
    public void testSelfAxisWildcard() {
        this.parseExpressionAndCheck("self::*",
                TestNode.relativeNodeSelector()
                        .self());
    }

    @Test
    public void testSelfAxisNamed() {
        this.parseExpressionAndCheck("self::ABC",
                TestNode.relativeNodeSelector()
                        .self()
                        .named(Names.string("ABC")));
    }

    @Test
    public void testSelfAxisWildcardEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("self::*",
                root,
                root);
        this.parseExpressionEvaluateAndCheck("self::*",
                root.child(0),
                root.child(0));
    }

    @Test
    public void testSelfDotSlashWildcard() {
        this.parseExpressionAndCheck("./*",
                TestNode.relativeNodeSelector()
                        .self()
                        .children());
    }

    @Test
    public void testSelfDotSlashNamed() {
        this.parseExpressionAndCheck("./ABC",
                TestNode.relativeNodeSelector().self()
                        .children()
                        .named(Names.string("ABC")));
    }

    @Test
    public void testSelfDotSlashNamed1Named2() {
        this.parseExpressionAndCheck("./ABC1/DEF2",
                TestNode.relativeNodeSelector().self()
                        .children()
                        .named(Names.string("ABC1"))
                        .children()
                        .named(Names.string("DEF2")));
    }

    @Test
    public void testSelfDotEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck(".",
                root,
                root);
        this.parseExpressionEvaluateAndCheck(".",
                root.child(0),
                branch1);
    }

    // namedFunction: boolean().......................................................................................

    @Test
    public void testWildcardExpressionBooleanTrue() {
        this.parseExpressionAndCheck("*[boolean(true())]",
                TestNode.relativeNodeSelector()
                        .children()
                        .expression(function("boolean", function("true"))));
    }

    @Test
    public void testNamedExpressionBooleanTrue() {
        this.parseExpressionAndCheck("ABC123[boolean(true())]",
                TestNode.relativeNodeSelector()
                        .children()
                        .named(nameAbc123())
                        .expression(function("boolean", function("true"))));
    }


    @Test
    public void testWildcardExpressionNumber() {
        this.parseExpressionAndCheck("*[123]",
                TestNode.relativeNodeSelector()
                        .children()
                        .expression(expressionNumberExpression(123)));
    }

    @Test
    public void testNamedExpressionNumber() {
        this.parseExpressionAndCheck("ABC123[123]",
                TestNode.relativeNodeSelector()
                        .children()
                        .named(nameAbc123())
                        .expression(expressionNumberExpression(123)));
    }

    @Test
    public void testDescendantWildcardExpressionBooleanTrueEvaluate() {
        final TestNode child = node("child");
        final TestNode parent = node("parent", child);

        this.parseExpressionEvaluateAndCheck("/*[boolean(true())]",
                parent,
                child);
    }

    @Test
    public void testDescendantWildcardExpressionBooleanFalseEvaluate() {
        final TestNode child = node("child");
        final TestNode parent = node("parent", child);

        this.parseExpressionEvaluateAndCheck("/*[boolean(false())]", parent);
    }

    @Test
    public void testDescendantWildcardExpressionBooleanStartsWithNameNodeEvaluate() {
        final TestNode child = node("child");
        final TestNode parent = node("parent", child);

        this.parseExpressionEvaluateAndCheck("/*[boolean(starts-with(name(node()), \"chi\"))]",
                parent,
                child);
    }

    // namedFunction: name.......................................................................................

    @Test
    public void testWildcardExpressionCurrentNodeName() {
        this.parseExpressionAndCheck("*[name()=\"123\"]",
                TestNode.relativeNodeSelector()
                        .children()
                        .expression(
                                Expression.equalsExpression(
                                        function("name"),
                                        Expression.value("123")
                                )));
    }

    @Test
    public void testDescendantsOrSelfExpressionCurrentNodeNameEvaluate() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionEvaluateAndCheck("//*[name(node())=\"leaf1\"]",
                root,
                leaf1);
        this.parseExpressionEvaluateAndCheck("//*[name(node())=\"branch2\"]",
                root,
                branch2);
        this.parseExpressionEvaluateAndCheck("//*[starts-with(name(node()), \"leaf\")]",
                root,
                leaf1, leaf2);
    }

    // namedFunction: number().......................................................................................

    @Test
    public void testDescendantsOrSelfExpressionNumberPredicate() {
        final TestNode leaf = node("leaf", 789);
        final TestNode branch = node("branch", "123", leaf);
        final TestNode root = node("root", "456", branch);

        this.parseExpressionEvaluateAndCheck("//*[number(@id) > 300]",
                root,
                root, leaf);
    }

    // namedFunction: true().......................................................................................

    @Test
    public void testExpressionStartsWithNameNodeEqualsTrueEvaluate() {
        final TestNode child = node("child");
        final TestNode parent = node("parent", child);

        this.parseExpressionEvaluateAndCheck("*[starts-with(name(node()), \"chi\")=true()]",
                parent,
                child);
    }

    @Test
    public void testExpressionStartsWithNameNodeEqualsTrueEvaluate2() {
        final TestNode child = node("child");
        final TestNode parent = node("parent", child);

        this.parseExpressionEvaluateAndCheck("*[starts-with(name(node()), \"X\")=true()]",
                parent);
    }

    @Test
    public void testDescendantsOrSelfExpressionStartsWithNameNodeEqualsTrueEvaluate() {
        final TestNode child = node("child");
        final TestNode parent = node("parent", child);

        this.parseExpressionEvaluateAndCheck("//*[starts-with(name(node()), \"c\")=true()]",
                parent,
                child);
    }

    // namedFunction: false().......................................................................................

    @Test
    public void testExpressionStartsWithNameNodeEqualsFalseEvaluate() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionEvaluateAndCheck("*[starts-with(name(node()), \"QQQ\")=false()]",
                root,
                branch);
    }

    @Test
    public void testExpressionStartsWithNameNodeEqualsFalseEvaluate2() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionEvaluateAndCheck("*[starts-with(name(node()), \"XYZ\")=false()]",
                root,
                branch);
    }

    @Test
    public void testDescendantsOrSelfExpressionStartsWithNameNodeEqualsFalseEvaluate() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionEvaluateAndCheck("//*[starts-with(name(node()), \"r\")=false()]",
                root,
                branch, leaf);
    }

    // and ........................................................................................................

    @Test
    public void testAbsoluteNodeNameTrueAndTrueEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[true() AND true()]",
                root,
                root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameFunctionAndFunctionEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[string-length(@id)=3 AND @id=\"abc\"]",
                root,
                leaf1);
    }

    // or ........................................................................................................

    @Test
    public void testAbsoluteNodeNameTrueOrTrueEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[false() OR true()]",
                root,
                root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameFunctionOrFunctionEvaluate() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[string-length(@id)=4 OR @id=\"abc\"]",
                root,
                leaf1);
    }

    @Test
    public void testAbsoluteNodeNameFunctionOrFunctionEvaluate2() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionEvaluateAndCheck("//*[string-length(@id)=3 OR @id=\"abc\"]",
                root,
                leaf1, leaf2);
    }

    // helpers ..................................................................................................

    @Override
    public NodeSelectorNodeSelectorParserTokenVisitor<TestNode, StringName, StringName, Object> createVisitor() {
        return new NodeSelectorNodeSelectorParserTokenVisitor<>(null, null, null);
    }

    private TestNode node(final String name, final TestNode... nodes) {
        return TestNode.with(name, nodes);
    }

    private TestNode node(final String name, final String idAttributeValue, final TestNode... nodes) {
        return TestNode.with(name, nodes).setAttributes(Maps.of(Names.string("id"), idAttributeValue));
    }

    private TestNode node(final String name, final Number idAttributeValue, final TestNode... nodes) {
        return TestNode.with(name, nodes)
                .setAttributes(Maps.of(Names.string("id"), EXPRESSION_NUMBER_KIND.create(idAttributeValue)));
    }

    private void parseExpressionAndCheck(final String expression,
                                         final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        this.checkEquals(selector,
                this.parseExpression(expression).unwrapIfCustomToStringNodeSelector(),
                () -> "Parse expression " + CharSequences.quoteAndEscape(expression));
    }

    private void parseExpressionEvaluateAndCheck(final String expression, final TestNode root) {
        this.parseExpressionEvaluateAndCheck(expression, root, Sets.empty());
    }

    private void parseExpressionEvaluateAndCheck(final String expression,
                                                 final TestNode root,
                                                 final TestNode... expected) {
        this.parseExpressionEvaluateAndCheck(expression, root, names(Lists.of(expected)));
    }

    private void parseExpressionEvaluateAndCheck(final String expression,
                                                 final TestNode root,
                                                 final Set<String> expected) {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = this.parseExpression(expression);

        final Set<TestNode> selected = Sets.ordered();
        selector.apply(root,
                new FakeNodeSelectorContext<>() {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }

                    @Override
                    public boolean test(final TestNode node) {
                        return true;
                    }

                    @Override
                    public void setNode(final TestNode node) {
                        this.node = node;
                    }

                    private TestNode node;

                    @Override
                    public TestNode selected(final TestNode node) {
                        selected.add(node);
                        return node;
                    }

                    @Override
                    public Object evaluate(final Expression expression) {
                        return expression.toValue(this.expressionEvaluationContext());
                    }

                    private NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> expressionEvaluationContext() {
                        return NodeSelectorExpressionEvaluationContexts.basic(
                                this.node,
                                (r) -> ExpressionEvaluationContexts.basic(
                                        EXPRESSION_NUMBER_KIND,
                                        Cast.to(this.functions()),
                                        (rr) -> {
                                            throw rr;
                                        },
                                        this.references(),
                                        ExpressionEvaluationContexts.referenceNotFound(),
                                        CaseSensitivity.SENSITIVE,
                                        this.converterContext()
                                )
                        );
                    }

                    private Function<FunctionExpressionName, Optional<ExpressionFunction<?, NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>>>> functions() {
                        return (n) -> {
                            switch (n.value()) {
                                case "boolean":
                                    return Optional.of(
                                            new TestExpressionFunction() {
                                                @Override
                                                public Object apply(final List<Object> parameters,
                                                                    final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context) {
                                                    return parameters.get(0);
                                                }
                                            }
                                    );
                                case "false":
                                    return Optional.of(
                                            new TestExpressionFunction() {
                                                @Override
                                                public Object apply(final List<Object> parameters,
                                                                    final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context) {
                                                    return false;
                                                }
                                            }
                                    );
                                case "node":
                                    return Optional.of(
                                            new TestExpressionFunction() {
                                                @Override
                                                public Object apply(final List<Object> parameters,
                                                                    final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context) {
                                                    return context.node();
                                                }
                                            }
                                    );
                                case "number":
                                    return Optional.of(
                                            new TestExpressionFunction() {
                                                @Override
                                                public Object apply(final List<Object> parameters,
                                                                    final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context) {
                                                    final Object parameter = parameters.get(0);
                                                    if (parameter instanceof String) {
                                                        return EXPRESSION_NUMBER_KIND.create(new BigDecimal((String) parameter));
                                                    }
                                                    return parameter;
                                                }
                                            }
                                    );
                                case "starts-with":
                                    return Optional.of(
                                            new TestExpressionFunction() {
                                                @Override
                                                public Object apply(final List<Object> parameters,
                                                                    final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context) {
                                                    final String string = parameters.get(0).toString();
                                                    final String contains = parameters.get(1).toString();
                                                    return string.startsWith(contains);
                                                }
                                            }
                                    );
                                case "string-length":
                                    return Optional.of(
                                            new TestExpressionFunction() {
                                                @Override
                                                public Object apply(final List<Object> parameters,
                                                                    final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context) {
                                                    return parameters.get(0).toString().length();
                                                }
                                            }
                                    );
                                case "true":
                                    return Optional.of(
                                            new TestExpressionFunction() {
                                                @Override
                                                public Object apply(final List<Object> parameters,
                                                                    final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context) {
                                                    return true;
                                                }
                                            }
                                    );
                                default:
                                    return Cast.to(
                                            NodeSelectorContexts.basicFunctions()
                                                    .apply(n)
                                    );
                            }
                        };
                    }

                    private Function<ExpressionReference, Optional<Optional<Object>>> references() {
                        return (r -> {
                            throw new UnsupportedOperationException();
                        });
                    }

                    private <T> Either<T, String> convert0(final Object value,
                                                           final Class<T> target) {
                        return this.convert(value, target);
                    }

                    private <T> Either<T, String> convert(final Object value, final Class<T> target) {
                        Objects.requireNonNull(value, "value");
                        Objects.requireNonNull(target, "target");

                        if (Integer.class == target && value instanceof ExpressionNumber) {
                            final ExpressionNumber expressionNumber = (ExpressionNumber) value;
                            return Cast.to(Either.left(expressionNumber.intValue()));
                        }

                        if (ExpressionNumber.class == target) {
                            if (value instanceof Number) {
                                return Cast.to(Either.left(ExpressionNumberKind.DEFAULT.create((Number) value)));
                            }
                            if (value instanceof Boolean) {
                                final Boolean booleanValue = (Boolean) value;
                                return Cast.to(Either.left(ExpressionNumberKind.DEFAULT.create(booleanValue ? BigDecimal.ONE : BigDecimal.ZERO)));
                            }
                        }

                        return Cast.to(target.isInstance(value) ?
                                Either.left(target.cast(value)) :
                                target == Boolean.class ?
                                        this.convertToBoolean(value) :
                                        target == String.class ?
                                                this.convertToString(value) :
                                                this.failConversion(value, target));
                    }

                    private Either<Boolean, String> convertToBoolean(final Object value) {
                        return Converters.numberToBoolean()
                                .convert(
                                        value,
                                        Boolean.class,
                                        this.converterContext()
                                );
                    }

                    private Either<String, String> convertToString(final Object value) {
                        return Converters.objectString().convert(value, String.class, this.converterContext());
                    }

                    private <T> Either<T, String> failConversion(final Object value, final Class<T> target) {
                        return Either.right("Failed to convert " + CharSequences.quoteIfChars(value) + " to " + target.getSimpleName());
                    }

                    private ExpressionNumberConverterContext converterContext() {
                        return ExpressionNumberConverterContexts.basic(
                                new Converter<>() {
                                    @Override
                                    public boolean canConvert(final Object value,
                                                              final Class<?> type,
                                                              final ExpressionNumberConverterContext context) {
                                        return true;
                                    }

                                    @Override
                                    public <T> Either<T, String> convert(final Object value,
                                                                         final Class<T> type,
                                                                         final ExpressionNumberConverterContext context) {
                                        return convert0(value, type);
                                    }
                                },
                                ConverterContexts.basic(
                                        Converters.fake(),
                                        DateTimeContexts.fake(),
                                        decimalNumberContext()),
                                EXPRESSION_NUMBER_KIND);
                    }
                });

        this.checkEquals(expected, names(selected), () -> expression + "\n" + selector.unwrapIfCustomToStringNodeSelector() + "\n" + root);
    }

    static abstract class TestExpressionFunction extends FakeExpressionFunction<Object, NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object>> {

        TestExpressionFunction() {
            super();
        }

        public abstract Object apply(final List<Object> parameters,
                                     final NodeSelectorExpressionEvaluationContext<TestNode, StringName, StringName, Object> context);

        @Override
        public List<ExpressionFunctionParameter<?>> parameters(final int count) {
            return Lists.of(
                    ExpressionFunctionParameterName.with("parameters")
                            .variable(Object.class)
                            .setKinds(
                                    ExpressionFunctionParameterKind.CONVERT_EVALUATE_FLATTEN_RESOLVE_REFERENCES
                            )
            );
        }
    }

    private Set<String> names(final Collection<TestNode> nodes) {
        return nodes.stream().map(n -> n.name().value()).collect(Collectors.toCollection(Sets::ordered));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> parseExpression(final String expression) {
        return NodeSelectorNodeSelectorParserTokenVisitor.with(this.parseOrFail(expression),
                (s) -> Names.string(s.value()),
                Predicates.always(),
                TestNode.class);
    }

    private NodeSelectorExpressionParserToken parseOrFail(final String expression) {
        return NodeSelectorParsers.expression()
                .orFailIfCursorNotEmpty(ParserReporters.basic())
                .parse(TextCursors.charSequence(expression),
                        NodeSelectorParserContexts.basic(
                                EXPRESSION_NUMBER_KIND,
                                this.decimalNumberContext().mathContext()
                        )
                )
                .orElseThrow(() -> new UnsupportedOperationException(expression))
                .cast(NodeSelectorExpressionParserToken.class);
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.american(MathContext.DECIMAL32);
    }

    private static StringName nameAbc123() {
        return Names.string("ABC123");
    }


    private static StringName nameDef456() {
        return Names.string("DEF456");
    }

    private Expression function(final String name,
                                final Expression... parameters) {
        return Expression.call(
                Expression.namedFunction(FunctionExpressionName.with(name)),
                Lists.of(parameters)
        );
    }

    private ValueExpression<ExpressionNumber> expressionNumberExpression(final int value) {
        return Expression.value(
                EXPRESSION_NUMBER_KIND.create(value)
        );
    }

    @Override
    public String typeNamePrefix() {
        return NodeSelector.class.getSimpleName();
    }

    @Override
    public Class<NodeSelectorNodeSelectorParserTokenVisitor<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorNodeSelectorParserTokenVisitor.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
