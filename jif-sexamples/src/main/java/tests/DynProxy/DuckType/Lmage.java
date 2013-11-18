
package tests.DynProxy.DuckType;

public class Lmage {

    boolean disposed;

    public boolean isDisposed() {
        return disposed;
    }

    public void dispose() {
        System.out.println("Lmage disposed");
    }
}
