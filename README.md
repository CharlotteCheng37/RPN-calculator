# CS2110 Assignment 4: RPN Expression Evaluator & CSV Spreadsheet Engine

This project implements an interpreter for evaluating mathematical expressions written in **Reverse Polish Notation (RPN)**. It supports variables, constants, operations, conditionals, and unary functions. It also includes a CSV-based spreadsheet evaluator that processes RPN formulas within CSV cells, similar to a simplified version of Excel.

---

## 🧠 Features

### ✅ Expression System
- `Expression` interface with subclasses:
  - `Constant`, `Variable`, `Operation`, `Conditional`, `Application`
- Recursive evaluation with variable lookup via `VarTable`
- `optimize()` method to apply constant folding
- Supports:
  - Binary operators: `+ - * / ^`
  - Conditional expressions using `?:`
  - Unary functions: `sqrt()`, `log()`, `sin()`, etc.
  - Infix and postfix string representations

### ✅ RPN Parser (`RpnParser`)
- Converts RPN strings into expression trees
- Uses stack-based parsing
- Supports variables, numbers, operators, conditionals, functions

### ✅ CSV Evaluator (`CsvEvaluator`)
- Parses CSV files with formulas starting with `=`
- Computes each cell’s value based on row-major evaluation
- Supports cross-cell references (e.g., `=A1 B2 +`)
- Outputs evaluated spreadsheet to standard output
- Graceful handling of errors with `#N/A`
