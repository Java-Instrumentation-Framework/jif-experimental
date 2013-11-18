package tests.DynProxy.ProxyStub;

import java.util.Collection;

public interface MyDAO {
    public Collection<MyVO> findSomeStuff();
    public Collection<MyVO> findSomeOtherStuff();
}
