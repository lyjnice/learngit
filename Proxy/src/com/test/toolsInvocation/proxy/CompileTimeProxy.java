package com.test.toolsInvocation.proxy;
import java.lang.reflect.Method;
public class CompileTimeProxy implements com.test.toolsInvocation.proxy.Moveable{
com.test.toolsInvocation.proxy.InvocationHandler h;
public CompileTimeProxy(InvocationHandler h) {
  this.h = h;
}
@Override
public void move() {
try {
 Method md = com.test.toolsInvocation.proxy.Moveable.class.getMethod("move");
     h.invoke(this,md);
} catch (Exception e) {
    e.printStackTrace();
}
}}