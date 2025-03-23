package cs2110;

import java.util.Set;
import java.util.HashSet;

public class Operation implements Expression{

    /**
     * The mathematical operation of this expression.
     */
    private final Operator op;

    /**
     * The first expression in the operation (e.g., in "a + b", "a" is the left operand).
     */
    private final Expression leftOperand;

    /**
     * The second expression in the operation (e.g., in "a + b", "b" is the right operand).
     */
    private final Expression rightOperand;

    /**
     * Create an operation node.
     */
    public Operation(Operator op, Expression leftOperand, Expression rightOperand) {
        if (op == null || leftOperand == null || rightOperand == null) {
            throw new IllegalArgumentException("Operator and operands must not be null.");
        }
        this.op = op;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * Evaluate the operation by recursively evaluating both operands and applying the operator.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        if (vars == null) {
            throw new IllegalArgumentException("VarTable must not be null.");
        }
        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    /**
     * One operation is required to evaluate an operator.
     */
    @Override
    public int opCount() {
        return 1 + leftOperand.opCount() + rightOperand.opCount();
    }

    /**
     * Return the infix notation of the expression, enclosed in parentheses.
     * Example: "(((2.0 * y) + 1.0) ^ 3.0)"
     */
    @Override
    public String infixString() {
        return "(" + leftOperand.infixString() + " " + op.symbol() + " " + rightOperand.infixString() + ")";
    }

    /**
     * Return the postfix notation of the expression.
     * Example: "2.0 y * 1.0 + 3.0 ^"
     */
    @Override
    public String postfixString() {
        return leftOperand.postfixString() + " " + rightOperand.postfixString() + " " + op.symbol();
    }

    /**
     * Return whether `other` is an Operation of the same class with the same operator and operand.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Operation)) {
            return false;
        }
        Operation o = (Operation) other;
        return op.equals(o.op) &&
                leftOperand.equals(o.leftOperand) &&
                rightOperand.equals(o.rightOperand);
    }

    @Override
    public Expression optimize(VarTable vars) {
        Expression leftOpt = leftOperand.optimize(vars);
        Expression rightOpt = rightOperand.optimize(vars);
        if (leftOpt instanceof Constant && rightOpt instanceof Constant) {
            Constant l = (Constant) leftOpt;
            Constant r = (Constant) rightOpt;
            return new Constant(op.operate(l.eval(vars), r.eval(vars)));
        }
        return new Operation(op, leftOpt, rightOpt);
    }

    @Override
    public Set<String> dependencies() {
        Set<String> deps = new HashSet<>(leftOperand.dependencies());
        deps.addAll(rightOperand.dependencies());
        return deps;
    }
}
