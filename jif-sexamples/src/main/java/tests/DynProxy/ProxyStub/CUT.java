package tests.DynProxy.ProxyStub;

public class CUT {
    private MyDAO dao;
    public void doSomething() {
        dao.findSomeStuff();
    }
    public void setDao(MyDAO dao) {
        this.dao = dao;
    }
}
