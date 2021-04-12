package felix;

import org.apache.commons.lang.StringUtils;


public class A {
    public double sum(double a, double b){
        return a + b;
    }

    public String commaSeparated(String... elements){
        return StringUtils.join(elements, ", ");
    }

}
