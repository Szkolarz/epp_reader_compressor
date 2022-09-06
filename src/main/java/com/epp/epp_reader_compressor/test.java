package com.epp.epp_reader_compressor;

import java.io.BufferedWriter;

public class test  {

    public static Integer i;
    public static BufferedWriter bw;

    public test() {

    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public BufferedWriter getBw() {
        return bw;
    }

    public void setBw(BufferedWriter bw) {
        this.bw = bw;
    }

    public test(Integer i, BufferedWriter bw) {
        this.i = i;
        this.bw = bw;
    }

}
