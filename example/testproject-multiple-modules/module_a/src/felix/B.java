package felix;

public class B {
    final A adder;

    public B(A a) {
        this.adder = a;
    }

    public double product(double a, double b){
        return a*b;
    }


    public double squareOfSum(double a, double b){
        return Math.pow(adder.sum(a,b), 2);
    }
}
