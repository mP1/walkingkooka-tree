[![Build Status](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree/badge.svg)](https://coveralls.io/github/mP1/walkingkooka-tree)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka-tree)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

## Background

Trees are everywhere in computing, representing more complex than a list or sequence. The former is two dimensional,
while the other is linear or one dimensional.

Examples of trees in computing:

- JSON
- XML documents
- Computer languages

Before expanding on their utility consider the following key classes/interfaces.

# [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression/Expression.java)

An agnostic AST that supports complex expressions, including computation, logical operands and invoking user defined
functions.

- Immutable
- Functional
- Fully traversable
- May be evaluated - see below

## [ExpressionNumber](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/ExpressionNumber.java)

An abstraction holding a number value within an `Expression`, which available choices including:

- `BigDecimal` slower but more precision and various `Rounding` options.
- `double` faster but limited 64-bit precision
- `float` fastest but limited 32-bit precision [TODO](https://github.com/mP1/walkingkooka-tree/issues/722)

## [ExpressionEvaluationContext](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression/ExpressionEvaluationContext.java)

A context accompanies the evaluation of an `Expression` and provides context aware values and behaviours.

- A powerful construct that supports many values and behaviours that modify evaluation of an expression.
- Provides the preferred `ExpressionNumber` to use at evaluation time, during number / calculations.
-

Extends [LocaleContext](https://github.com/mP1/walkingkooka-locale/blob/master/src/main/java/walkingkooka/locale/LocaleContext.java),
which provides locale aware values such as decimal point, currency, day names and more.

## [ExpressionFunction](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)

A function provides an independent function which describes

- *name*
  The [name](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/ExpressionFunctionName.java),
  which is used within expressions.
- *return type* The return type
- *parameters* All the parameters including their name, cardinality and type.
- *purity* Useful to determine whether the computed value can be cached, Within a spreadsheet this can help improve
  speed by avoiding unnecessary recalculations, because component values have or will never change.

### [ExpressionFunction(s)](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)

- [basic](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/BasicExpressionFunction.java)
- [eval](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/TreeExpressionFunctionEval.java)
- [expressionNumberFunction](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionNumberFunctionExpressionFunction.java)
- [fake](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/FakeExpressionFunction.java)
- [lambda](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunctionNull.java)
- [node](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunctionNode.java)
- [nodeName](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunctionNodeName.java)
- [nullFunction](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunctionNull.java)
- [typeName](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunctionTypeName.java)

The accompanying
[ExpressionEvaluationContext](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/ExpressionEvaluationContext.java)
when executing a function uses the function metadata to convert values to the target type as necessary, supporting a
type-safe but dynamic conversions as necessary.

### [Projects](https://github.com/mP1?tab=repositories&q=function&type=&language=&sort=)

The following function projects hold many functions, and can be used in any `Expression` context including Spreadsheets.

- [boolean](https://github.com/mP1/walkingkooka-tree-expression-function-boolean)
- [datetime](https://github.com/mP1/walkingkooka-tree-expression-function-datetime)
- [engineering](https://github.com/mP1/walkingkooka-tree-expression-function-engineering)
- [net](https://github.com/mP1/walkingkooka-tree-expression-function-net)
- [number](https://github.com/mP1/walkingkooka-tree-expression-function-number)
- [number-trigonometry](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry)
- [spreadsheet](https://github.com/mP1/walkingkooka-spreadsheet-expression-function)
- [stat](https://github.com/mP1/walkingkooka-tree-expression-function-stat)
- [string](https://github.com/mP1/walkingkooka-tree-expression-function-string)
- [walkingkooka-spreadsheet-expression-function](https://github.com/mP1/walkingkooka-spreadsheet-expression-function)
  Contains additional functions that can be executed within a spreadsheet expression context.

# [Node](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/Node.java)

A `Node` is a unit within a tree, coming in two forms, leaves and parent nodes with the former never having children
while the later possibly containing zero or more children. Numerous `Node` implementations include:

- [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression)
- [File](https://github.com/mP1/walkingkooka-tree-file/tree/master/src/main/java/walkingkooka/tree/file) A file system
  abstraction, execute xpath over directories and files.
- [Json](https://github.com/mP1/walkingkooka-tree-json/tree/master/src/main/java/walkingkooka/tree/json) Json objects.
- [Patch](https://github.com/mP1/walkingkooka-tree-patch/tree/master/src/main/java/walkingkooka/tree/patch) JSON Patch
- [Pojo](https://github.com/mP1/walkingkooka-tree-pojo/tree/master/src/main/java/walkingkooka/tree/pojo) Java POJOs with
  highlight support
- [Xml](https://github.com/mP1/walkingkooka-tree-xml/tree/master/src/main/java/walkingkooka/tree/xml) Xml documents

## [Select](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select)

A selector represents a query which may be used to match `Node` within a node graph, equivalent to
[XPATH](https://en.wikipedia.org/wiki/XPath)

- Almost identical in syntax and effective functionality to that of XPATH.
- May be executed using any of the many `Node` implementations.

## [NodePointer](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/pointer/NodePointer.java)

A `NodePointer` supports path navigation/selection over a `Node` and is similar in syntax and features
to [JSON POINTER](https://datatracker.ietf.org/doc/html/rfc6901). Like `Select` is can operate against any of the above
mentioned `Node` implementations.

## [NodePatch](https://github.com/mP1/walkingkooka-tree-patch/blob/master/src/main/java/walkingkooka/tree/patch/NodePatch.java)

A `NodePatch` is an equivalent technology similar to [json-patch](http://jsonpatch.com) but supports all `Node`
technologies.
