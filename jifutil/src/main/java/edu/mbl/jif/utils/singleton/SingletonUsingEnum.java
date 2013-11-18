package edu.mbl.jif.utils.singleton;

/**
 *
 * @author GBH
 */
public enum SingletonUsingEnum {

    INSTANCE;

    public void doStuff(String stuff) {
        System.out.println("Doing " + stuff);
    }

    public static void main(String[] args) {
        SingletonUsingEnum.INSTANCE.doStuff("some stuff");
    }

}

/*
public class SingletonSample {
public enum ServiceLocator {      
SERVICE1 ("controlService"), SERVICE2("loginService");    
Service service;       
private ServiceLocator(String serviceName) {
service = ServiceFactory.createService(serviceName);
}
public Service getService() {
return service;
}
}

interface Service {
void getWhatEvEr();
}

public static void main(String[] args) {
Service s1 = ServiceLocator.SERVICE1.getService();
Service s2 = ServiceLocator.SERVICE2.getService();
}

}
 */

