package com.code2017.proxy.jdk.StaticProxy;
/** 
 *测试Count类 
 *  
 * @author Administrator 
 *  
 */ 
public class TestCount {
	public static void main(String[] args) {
		CountImpl countImpl = new CountImpl();
		CountProxy countProxy = new CountProxy(countImpl);
		countProxy.updateCount();
		countProxy.queryCount();

	}
}
