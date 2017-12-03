package ir.pint.soltoon.container.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ReentrantLock lock = new ReentrantLock();

        return null;
    }
}
