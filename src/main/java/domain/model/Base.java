package domain.model;

import java.util.Arrays;

public class Base {
    private int a;
    private int b;

    public Base(){}

    public Base(int a, int b){
        this.a = a;
        this.b =b;
    }

    @Override
    public String toString() {
        return "Base{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
