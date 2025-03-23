package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Conditional implements Expression{

    /**
     * The condition expression that determines which branch to evaluate.
     */
    private final Expression condition;

    /**
     * The branch evaluated when the condition is nonzero (true).
     */
    private final Expression  trueBranch;

    /**
     * The branch evaluated when the condition is zero (false).
     */
    private final Expression falseBranch;

    /**
     * Constructs a Conditional expression node.
     */
    public Conditional(Expression condition, Expression trueBranch, Expression falseBranch) {
        if (condition == null || trueBranch == null || falseBranch == null) {
            throw new IllegalArgumentException("Condition and Branches must not be null.");
        }
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
    }

    /**
     * Evaluates the conditional expression by checking the condition's value.
     * If the condition evaluates to `0.0`, returns the value of the false branch.
     * Otherwise, returns the value of the true branch.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        if (vars == null) {
            throw new IllegalArgumentException("VarTable must not be null.");
        }
        return (condition.eval(vars) == 0.0) ? falseBranch.eval(vars) : trueBranch.eval(vars);
    }

    /**
     * Counts the number of operations in this expression.
     * The worst-case count includes the condition evaluation, the more expensive branch,
     * plus 1 for the selection operation.
     */
    @Override
    public int opCount() {
        return condition.opCount() + Math.max(trueBranch.opCount(), falseBranch.opCount()) + 1;
    }

    /**
     * Return the infix notation of the expression, enclosed in parentheses.
     * Example: "((x + 3.0) ? (2.0 * y) : 7.0)"
     */
    @Override
    public String infixString() {
        return "(" + condition.infixString() + " ? " + trueBranch.infixString() + " : " + falseBranch.infixString() + ")";
    }

    /**
     * Return the postfix notation of the expression.
     * Example: "x 3.0 + 2.0 y * 7.0 ?:"
     */
    @Override
    public String postfixString() {
        return condition.postfixString() + " " + trueBranch.postfixString() + " " +
                falseBranch.postfixString() + " ?:";
    }

    /**
     * Checks if this conditional expression is equal to another object.
     * Two Conditional expressions are equal if their condition, true branch, and false branch are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Conditional)) return false;
        Conditional c = (Conditional) other;
        return condition.equals(c.condition) &&
                trueBranch.equals(c.trueBranch) &&
                falseBranch.equals(c.falseBranch);
    }

    @Override
    public Expression optimize(VarTable vars) {
        Expression condOpt = condition.optimize(vars);
        Expression trueOpt = trueBranch.optimize(vars);
        Expression falseOpt = falseBranch.optimize(vars);

        if (condOpt instanceof Constant) {
            Constant c = (Constant) condOpt;
            return c.eval(vars) == 0.0 ? falseOpt : trueOpt;
            }
        return new Conditional(condOpt, trueOpt, falseOpt);
    }

    @Override
    public Set<String> dependencies() {
        Set<String> deps = new HashSet<>(condition.dependencies());
        deps.addAll(trueBranch.dependencies());
        deps.addAll(falseBranch.dependencies());
        return deps;
    }
}
