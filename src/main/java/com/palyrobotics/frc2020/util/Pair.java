package com.palyrobotics.frc2020.util;

public class Pair<K, E> {
    private K value1;
    private E value2;

    public Pair(K value1, E value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public K getValue1() {
        return value1;
    }

    public E getValue2() {
        return value2;
    }

    public void setValue1(K value1) {
        this.value1 = value1;
    }

    public void setValue2(E value2) {
        this.value2 = value2;
    }
}
