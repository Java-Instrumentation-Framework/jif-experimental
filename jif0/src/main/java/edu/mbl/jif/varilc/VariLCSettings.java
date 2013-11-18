package edu.mbl.jif.varilc;

public class VariLCSettings {

    double wavelength;
    double swing;
    double a0;
    double a1;
    double a2;
    double a3;
    double a4;
    double b0;
    double b1;
    double b2;
    double b3;
    double b4;

    void getCurrent(VariLCModel vlcModel) {
        wavelength = vlcModel.getWavelength();
        swing = vlcModel.getSwing();
        a0 = vlcModel.getA0();
        a1 = vlcModel.getA1();
        a2 = vlcModel.getA2();
        a3 = vlcModel.getA3();
        a4 = vlcModel.getA4();
        b0 = vlcModel.getB0();
        b1 = vlcModel.getB1();
        b2 = vlcModel.getB2();
        b3 = vlcModel.getB3();
        b4 = vlcModel.getB4();
    }

    public void setCurrent(VariLCModel vlcModel) {
        vlcModel.setWavelength(wavelength);
        vlcModel.setSwing(swing);
        vlcModel.setA0(a0);
        vlcModel.setA1(a1);
        vlcModel.setA2(a2);
        vlcModel.setA3(a3);
        vlcModel.setA4(a4);
        vlcModel.setB0(b0);
        vlcModel.setB1(b1);
        vlcModel.setB2(b2);
        vlcModel.setB3(b3);
        vlcModel.setB4(b4);
    }

}

