[![Build Status](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-tree/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree/badge.svg)](https://coveralls.io/github/mP1/walkingkooka-tree)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka-tree)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

## Background

Trees are everywhere in computing, representing more complex data structures than a list or sequence with examples being

- JSON
- XML documents
- Computer languages

Before expanding on their utility consider the following key classes/interfaces.

## [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression/Expression.java)

An agnostic AST that supports complex expressions, including computation, logical operands and user defined functions.

- Immutable
- Functional
- Fully traversable
- May be evaluated - see below

## [ExpressionNumber](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/ExpressionNumber.java)

An abstraction that supports two number types

- `BigDecimal` slower but more precision and various `Rounding` options.
- `double` faster but limited 64-bit precision
- `float` fastest but limited 32-bit precision [TODO](https://github.com/mP1/walkingkooka-tree/issues/722)

Either may be used within an expression and during computations with the answer matching the form provided by the
accompanying `ExpressionEvaluationContext`.

## [ExpressionEvaluationContext](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression/ExpressionEvaluationContext.java)

A context accompanies the evaluation of an `Expression` and provides context aware values and behaviours.

- A powerful construct that supports many values and behaviours that modify evaluation of an expression.
- Provides the preferred `ExpressionNumber` to use at evaluation time.
- The context can provide for example locale aware values such as decimal point, current symbol and more.
- Mapping
  of [function names](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression/ExpressionFunctionName.java)
  to
  a [function](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)
- Switching locales or function mappings and more make it possible to re-evaluate an `Expression` simply providing a
  different `ExpressionEvaluationContext`.
- XPath expressions and spreadsheet formula expressions both use `Expression` to compute their results.

## [ExpressionFunction](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)

This interface makes it easy to insert a function located by name into an `Expression` AST. Numerous methods are
provided to support meta programming and have a familiar feel to java reflection.

- returnType()
- parameters() provides a meta view of each individual parameter name, cardinality (including optional support), type
  and other `Expression` execution semantics.
- purity Useful to decide whether an expression or function needs re-evaluation, useful for big spreadsheets with lots
  of cells to avoid a lot of costly and slow re-evaluation.

Numerous projects are available focusing on grouping functions that perform a similar or related task:

The following function project hold many generic functions.

- [boolean](https://github.com/mP1/walkingkooka-tree-expression-function-boolean)
- [datetime](https://github.com/mP1/walkingkooka-tree-expression-function-datetime)
- [engineering](https://github.com/mP1/walkingkooka-tree-expression-function-engineering)
- [net](https://github.com/mP1/walkingkooka-tree-expression-function-net)
- [number](https://github.com/mP1/walkingkooka-tree-expression-function-number)
- [number-trigonometry](https://github.com/mP1/walkingkooka-tree-expression-function-number-trigonometry)
- [spreadsheet](https://github.com/mP1/walkingkooka-spreadsheet-expression-function)
- [stat](https://github.com/mP1/walkingkooka-tree-expression-function-stat)
- [string](https://github.com/mP1/walkingkooka-tree-expression-function-string)

These project below require a `SpreadsheetExpressionEvaluationContext` which helps support numerous spreadsheet type
requirements, like locating cell/label references, parsing spreadsheet formula expressions and more.

- [walkingkooka-spreadsheet-expression-function](https://github.com/mP1/walkingkooka-spreadsheet-expression-function)

## [Node](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/Node.java)

- A node is the smaller unit within a tree with two basic forms leaves and parent nodes.
- This provides the base interface for all Tree like representations in all my repos from parsing grammars, expressions
  and more.

## [Select](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/select)

- An almost identical in representation supporting constructs and functionality found in XPATH.
- Language is almost identical to XPATH expressions both in syntax and textual representation and execution form.
- Other represent
- General purpose XPATH like execution system that works over many tree systems, unlike XPATH which only works with XML.
- XPath expressions can work unaltered over the following tree abstractions.

  - [Expression](https://github.com/mP1/walkingkooka-tree/tree/master/src/main/java/walkingkooka/tree/expression)
    Described above
  - [File](https://github.com/mP1/walkingkooka-tree-file/tree/master/src/main/java/walkingkooka/tree/file) A file system
    abstraction, execute xpath over directories and files.
  - [Json](https://github.com/mP1/walkingkooka-tree-json/tree/master/src/main/java/walkingkooka/tree/json) Json objects.
  - [Patch](https://github.com/mP1/walkingkooka-tree-patch/tree/master/src/main/java/walkingkooka/tree/patch) JSON Patch
  - [Pojo](https://github.com/mP1/walkingkooka-tree-pojo/tree/master/src/main/java/walkingkooka/tree/pojo) Java POJOs
    with highlight support
  - [Xml](https://github.com/mP1/walkingkooka-tree-xml/tree/master/src/main/java/walkingkooka/tree/xml) Xml documents

### Additional Tree Navigation Technologies.

Additional standard tree navigation approaches are also implemented supporting `Node` are also provided in other repos.

- [JSON POINTER](https://datatracker.ietf.org/doc/html/rfc6901) Another navigation system that may be used to identify a
  value within a JSON document.
- [json-patch](http://jsonpatch.com) only works with JSON and not XML.
- [XPATH](https://en.wikipedia.org/wiki/XPath) only works with XML and not JSON.

## Visitors

Lists have powerful APIs such as `java.util.stream.Stream` and besides the navigation technologies briefly mentioned
above it is often necessary to find and change an AST in some way or perhaps convert from/to another AST.

- Visitors are provided for `Expression` and all `Node` implementations used in numerous interesting ways.
- The [spreadsheet project](https://github.com/mP1/walkingkooka-spreadsheet) uses visitors in many use cases, to support
  writing formulas when a column/row is moved.
- An `ExpressionVisitor` can be used to transform an Expression back to the `ParserToken` representation, allowing a
  roundtrip from parsing text into an expression and back.
