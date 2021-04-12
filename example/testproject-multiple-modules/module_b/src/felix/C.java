package felix;

import felix.A;

public class C {
    public double delgatingSum(double a, double b){
        return new A().sum(a,b);
    }
}
