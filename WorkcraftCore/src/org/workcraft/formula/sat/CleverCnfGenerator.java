package org.workcraft.formula.sat;

import static org.workcraft.formula.encoding.CnfOperations.literal;
import static org.workcraft.formula.encoding.CnfOperations.not;
import static org.workcraft.formula.encoding.CnfOperations.or;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.workcraft.formula.And;
import org.workcraft.formula.BinaryBooleanFormula;
import org.workcraft.formula.BooleanFormula;
import org.workcraft.formula.BooleanVariable;
import org.workcraft.formula.BooleanVisitor;
import org.workcraft.formula.Iff;
import org.workcraft.formula.Imply;
import org.workcraft.formula.Literal;
import org.workcraft.formula.Not;
import org.workcraft.formula.One;
import org.workcraft.formula.Or;
import org.workcraft.formula.RecursiveBooleanVisitor;
import org.workcraft.formula.Xor;
import org.workcraft.formula.Zero;
import org.workcraft.formula.cnf.Cnf;
import org.workcraft.formula.cnf.CnfClause;
import org.workcraft.formula.cnf.RawCnfGenerator;
//import org.workcraft.formula.sat.CnfTask;
import org.workcraft.formula.utils.FormulaToString;

public class CleverCnfGenerator implements RawCnfGenerator<BooleanFormula>, BooleanVisitor<Literal> {
    Cnf result = new Cnf();

    private static final class Void {
        private Void() { }
    }

    static class ConstantExpectingCnfGenerator implements BooleanVisitor<Void> {
        private static boolean cleverOptimiseAnd = true;

        private final BooleanVisitor<Literal> dumbGenerator;
        private final Cnf result;

        ConstantExpectingCnfGenerator(Cnf result, BooleanVisitor<Literal> dumbGenerator) {
            this.result = result;
            this.dumbGenerator = dumbGenerator;
        }

        boolean currentResult = true;

        @Override
        public Void visit(And and) {
            if (currentResult) {
                and.getX().accept(this);
                and.getY().accept(this);
            } else {
                if (!cleverOptimiseAnd) {
                    Literal x = and.getX().accept(dumbGenerator);
                    Literal y = and.getY().accept(dumbGenerator);
                    result.add(or(not(x), not(y)));
                } else {
                    Literal[][] side1 = getBiClause(and.getX());
                    Literal[][] side2 = getBiClause(and.getY());
                    for (int i = 0; i < side1.length; i++) {
                        for (int j = 0; j < side2.length; j++) {
                            List<Literal> list = new ArrayList<>();
                            for (int k = 0; k < side1[i].length; k++) {
                                list.add(not(side1[i][k]));
                            }
                            for (int k = 0; k < side2[j].length; k++) {
                                list.add(not(side2[j][k]));
                            }
                            result.add(new CnfClause(list));
                        }
                    }
                }

            }
            return null;
        }

        static class BiClauseGenerator implements BooleanVisitor<Literal[][]> {
            BooleanVisitor<Literal> dumbGenerator;
            BiClauseGenerator(BooleanVisitor<Literal> dumbGenerator) {
                this.dumbGenerator = dumbGenerator;
            }
            @Override
            public Literal[][] visit(And node) {
                Literal[][] result = new Literal[1][];
                result[0] = new Literal[2];
                result[0][0] = node.getX().accept(dumbGenerator);
                result[0][1] = node.getY().accept(dumbGenerator);
                return result;
            }
            @Override
            public Literal[][] visit(Iff node) {
                throw new RuntimeException();
            }
            @Override
            public Literal[][] visit(Zero node) {
                throw new RuntimeException();
            }
            @Override
            public Literal[][] visit(One node) {
                throw new RuntimeException();
            }
            @Override
            public Literal[][] visit(Not node) {
                Literal[][] preres = node.getX().accept(this);
                if (preres.length != 1) {
                    throw new RuntimeException("something wrong...");
                }
                Literal[][]res = new Literal[preres[0].length][];
                for (int i = 0; i < res.length; i++) {
                    res[i] = new Literal[1];
                    res[i][0] = not(preres[0][i]);
                }
                return res;
            }
            @Override
            public Literal[][] visit(Imply node) {
                throw new RuntimeException();
            }
            @Override
            public Literal[][] visit(BooleanVariable variable) {
                Literal[][]result = new Literal[1][];
                result[0] = new Literal[1];
                result[0][0] = literal(variable);
                return result;
            }
            @Override
            public Literal[][] visit(Or node) {
                throw new RuntimeException();
            }
            @Override
            public Literal[][] visit(Xor node) {
                throw new RuntimeException();
            }
        }

        Literal[][] getBiClause(BooleanFormula formula) {
            return formula.accept(new BiClauseGenerator(dumbGenerator));
        }

        @Override
        public Void visit(Iff iff) {
            Literal x = iff.getX().accept(dumbGenerator);
            Literal y = iff.getY().accept(dumbGenerator);
            if (currentResult) {
                result.add(or(x, not(y)));
                result.add(or(not(x), y));
            } else {
                result.add(or(x, y));
                result.add(or(not(x), not(y)));
            }
            return null;
        }

        @Override
        public Void visit(Not not) {
            boolean store = currentResult;
            currentResult = !currentResult;
            not.getX().accept(this);
            currentResult = store;
            return null;
        }

        @Override
        public Void visit(BooleanVariable node) {
            if (currentResult) {
                result.add(or(literal(node)));
            } else {
                result.add(or(not(node)));
            }
            return null;
        }
        @Override
        public Void visit(Zero node) {
            throw new RuntimeException("not implemented");
        }
        @Override
        public Void visit(One node) {
            throw new RuntimeException("not implemented");
        }
        @Override
        public Void visit(Imply node) {
            throw new RuntimeException("not implemented");
        }
        @Override
        public Void visit(Or node) {
            throw new RuntimeException("not implemented");
        }

        @Override
        public Void visit(Xor node) {
            throw new RuntimeException("not implemented");
        }
    }

    public CnfTask getCnf(BooleanFormula formula) {

        Cnf cnf = generateCnf(formula);
        return new SimpleCnfTaskProvider().getCnf(cnf);
    }

    class FormulaCounter extends RecursiveBooleanVisitor<Object> {
        int count = 0;

        Map<BooleanFormula, Integer> met = new HashMap<>();

        /*@Override
        protected Object visitDefault(BooleanFormula node) {
        }*/

        @Override
        protected Object visitBinary(BinaryBooleanFormula node) {
            count++;
            Integer m = met.get(node);
            if (m == null) {
                met.put(node, 1);
            } else {
                met.put(node, m + 1);
            }
            return super.visitBinary(node);
        }

        public void printReport() {
            for (Entry<BooleanFormula, Integer> entry : met.entrySet()) {
                if (entry.getValue() > 100) {
                    System.out.println(">100: " + entry.getValue() + ": " + FormulaToString.toString(entry.getKey()));
                }
            }
        }

        public int getCount() {
            return count;
        }

        public int getUniques() {
            return met.size();
        }
    }

    public Cnf generateCnf(BooleanFormula formula) {
        //FormulaCounter counter = new FormulaCounter();
        //formula.accept(counter);
        //System.out.println("total visits: " + counter.getCount());
        //System.out.println("unique visits: " + counter.getUniques());
        //counter.printReport();
        //System.out.println("formula: " + FormulaToString.toString(formula));

        formula.accept(new ConstantExpectingCnfGenerator(result, this));
        //CnfLiteral res = formula.accept(this); result.add(or(res));

        return result;
    }

    Map<BooleanFormula, Literal> cache = new HashMap<>();
    //int varCount = 0;

    Literal newVar(BooleanFormula node) {
        Literal res = new Literal(""); //"tmp_" + varCount++
        cache.put(node, res);
        return res;
    }

    interface BinaryGateImplementer {
        void implement(Literal res, Literal x, Literal y);
    }

    public Literal visit(BinaryBooleanFormula node, BinaryGateImplementer impl) {
        Literal res = cache.get(node);
        if (res == null) {
            res = newVar(node);
            Literal x = node.getX().accept(this);
            Literal y = node.getY().accept(this);
            impl.implement(res, x, y);
        }
        return res;
    }

    @Override
    public Literal visit(And node) {
        return visit(node,
            new BinaryGateImplementer() {
                @Override
                public void implement(Literal res, Literal x, Literal y) {
                    result.add(or(res, not(x), not(y)));
                    result.add(or(not(res), x));
                    result.add(or(not(res), y));
                }
            }
        );
    }

    @Override
    public Literal visit(Iff node) {
        return visit(node,
                new BinaryGateImplementer() {
                    @Override
                    public void implement(Literal res, Literal x, Literal y) {
                        result.add(or(not(res), not(x), y));
                        result.add(or(not(res), x, not(y)));
                        result.add(or(res, not(x), not(y)));
                        result.add(or(res, x, y));
                    }
                }
            );
    }

    @Override
    public Literal visit(Zero node) {
        return Literal.ZERO;
    }

    @Override
    public Literal visit(One node) {
        return Literal.ONE;
    }

    @Override
    public Literal visit(Not node) {
        return not(node.getX().accept(this));
    }

    @Override
    public Literal visit(Imply node) {
        return visit(node,
                new BinaryGateImplementer() {
                    @Override
                    public void implement(Literal res, Literal x, Literal y) {
                        result.add(or(not(res), not(x), y));
                        result.add(or(res, not(y)));
                        result.add(or(res, x));
                    }
                }
            );
    }

    @Override
    public Literal visit(BooleanVariable variable) {
        return literal(variable);
    }

    @Override
    public Literal visit(Or node) {
        return visit(node,
                new BinaryGateImplementer() {
                    @Override
                    public void implement(Literal res, Literal x, Literal y) {
                        result.add(or(not(res), x, y));
                        result.add(or(res, not(y)));
                        result.add(or(res, not(x)));
                    }
                }
            );
    }

    @Override
    public Literal visit(Xor node) {
        return visit(node,
                new BinaryGateImplementer() {
                    @Override
                    public void implement(Literal res, Literal x, Literal y) {
                        result.add(or(res, not(x), y));
                        result.add(or(res, x, not(y)));
                        result.add(or(not(res), not(x), not(y)));
                        result.add(or(not(res), x, y));
                    }
                }
            );
    }

}
