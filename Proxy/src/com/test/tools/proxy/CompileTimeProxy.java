package com.test.tools.proxy;
public class CompileTimeProxy implements com.test.tools.proxy.Moveable{
Moveable t;
public CompileTimeProxy(com.test.tools.proxy.Moveable t) {
  this.t = t;
}
@Override
public void stop() {
  System.out.println("Tank moving................");
  Long start = System.currentTimeMillis();
  t.stop();
  Long end = System.currentTimeMillis();
  System.out.println("ºÄÊ±£º "+(end - start));
}@Override
public void move() {
  System.out.println("Tank moving................");
  Long start = System.currentTimeMillis();
  t.move();
  Long end = System.currentTimeMillis();
  System.out.println("ºÄÊ±£º "+(end - start));
}}