package cs2110;

import java.util.Set;

public class Variable implements Expression{

    /**
     * The name of the variable.
     */
    private final String name;

    /**
     * Constructs a Variable with the given name.
     */
    public Variable(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Variable name must not be null or empty");
        }
        this.name = name;
    }

    /**
     * Return the value of this variable using given VarTable.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        if (vars == null || !vars.contains(name)) {
            throw new UnboundVariableException("Variable '" + name + "' is not defined.");
        }
        return vars.get(name);
    }

    /**
     * No operations are required to evaluate a variable.
     */
    @Override
    public int opCount() {
        return 0;
    }

    /**
     * Returns the infix representation of this variable.
     */
    @Override
    public String infixString() {
        return name;
    }

    /**
     * Returns the postfix representation of this variable.
     */
    @Override
    public String postfixString() {
        return name;
    }

    /**
     * Return whether `other` is a Variable of the same class with the same name.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        Variable v = (Variable) other;
        return name.equals(v.name);
    }

    @Override
    public Expression optimize(VarTable vars) {
        if (vars.contains(name)) {
            try {
                return new Constant(vars.get(name));
            } catch (UnboundVariableException e) {
                // Should not happen
                throw new RuntimeException("Unexpected unbound variable during optimization", e);
            }
        }

        return this;
    }

    @Override
    public Set<String> dependencies() {
        return Set.of(name);
}
}
