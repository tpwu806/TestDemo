package com.code2016.imooc.reflect;

import java.lang.reflect.Method;

public class JActiveFramework {
	/**
	 * 根据标准javaBean对象的属性名获取其属性值
	 * 
	 * @param obj
	 * @param propertyName
	 * @return
	 */
	public static Object InvokeObjectMethod(Object obj, String propertyName) {
		// 1.根据属性名称就可以获取其get方法
		String getMethodName = "get"
				+ propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
		//2.获取方法对象
		Class<? extends Object> c = obj.getClass();
		try {
			//get方法都是public的且无参数
			Method m= c.getMethod(getMethodName);
			//3 通过方法的反射操作方法
			Object value = m.invoke(obj);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}