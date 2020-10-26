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
import walkingkooka.Cast;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicNodeSelectorContextTest implements ClassTesting2<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>>,
        NodeSelectorContextTesting<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>,
                TestNode,
                StringName,
                StringName,
                Object> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;

    @Test
    public void testWithNullFinisher() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(null,
                this.predicate(),
                this.mapper(),
                KIND,
                this.functions(),
                this.converter(),
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullFilter() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                null,
                this.mapper(),
                KIND,
                this.functions(),
                this.converter(),
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullSelectedFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                null,
                KIND,
                this.functions(),
                this.converter(),
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullExpressionNumberKindFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                null,
                this.functions(),
                this.converter(),
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                KIND,
                null,
                this.converter(),
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                KIND,
                this.functions(),
                null,
                this.converterContext(),
                this.nodeType()));
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                KIND,
                this.functions(),
                this.converter(),
                null,
                this.nodeType()));
    }

    @Test
    public void testWithNullNodeTypeFails() {
        assertThrows(NullPointerException.class, () -> BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                KIND,
                this.functions(),
                this.converter(),
                this.converterContext(),
                null));
    }

    @Test
    public void testFunctionNodeMissingFails() {
        final BasicNodeSelectorContext<TestNode, StringName, StringName, Object> context = this.createContext();

        TestNode.clear();

        assertThrows(NodeSelectorException.class, () -> context.function(this.node(), NodeSelectorContext.NO_PARAMETERS));
    }

    @Test
    public void testFunctionNode() {
        final BasicNodeSelectorContext<TestNode, StringName, StringName, Object> context = this.createContext();

        TestNode.clear();
        final TestNode current = TestNode.with("current123");
        context.setNode(current);

        assertEquals(current,
                context.function(this.node(), NodeSelectorContext.NO_PARAMETERS));
    }

    private FunctionExpressionName node() {
        return FunctionExpressionName.with("node");
    }

    @Test
    public void testToString() {
        final BooleanSupplier finisher = this.finisher();
        final Predicate<TestNode> filter = this.predicate();
        final Function<TestNode, TestNode> mapper = this.mapper();
        final Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions = this.functions();
        final Converter converter = this.converter();
        final ConverterContext converterContext = this.converterContext();
        this.toStringAndCheck(BasicNodeSelectorContext.with(finisher,
                filter,
                mapper,
                KIND,
                functions,
                converter,
                converterContext,
                this.nodeType()),
                finisher + " " + filter + " " + mapper + " " + KIND + " " + functions + " " + converter + " " + converterContext);
    }

    @Override
    public String typeNameSuffix() {
        return NodeSelectorContext.class.getSimpleName();
    }

    @Override
    public BasicNodeSelectorContext<TestNode, StringName, StringName, Object> createContext() {
        return BasicNodeSelectorContext.with(this.finisher(),
                this.predicate(),
                this.mapper(),
                KIND,
                this.functions(),
                this.converter(),
                this.converterContext(),
                this.nodeType());
    }

    private BooleanSupplier finisher() {
        return () -> false;
    }

    private Predicate<TestNode> predicate() {
        return Predicates.always();
    }

    private Function<TestNode, TestNode> mapper() {
        return Function.identity();
    }

    private Function<FunctionExpressionName, Optional<ExpressionFunction<?>>> functions() {
        return NodeSelectorContexts.basicFunctions();
    }

    private Converter converter() {
        return Converters.fake();
    }

    private ConverterContext converterContext() {
        return ConverterContexts.fake();
    }

    private Class<TestNode> nodeType() {
        return TestNode.class;
    }

    @Override
    public Class<BasicNodeSelectorContext<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(BasicNodeSelectorContext.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
