package de.bs.dbinfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallUtil {
	public static String callMethod(final String methodName, final Object instance) {
		try {
			return callMethod(instance.getClass().getMethod(methodName), instance);
		} catch (NoSuchMethodException e) {
			return "<exc:No such Method>";
		} catch (SecurityException e) {
			return "<exc:Security>";
		}
	}
	
	public static String callMethod(final Method methodToCall, final Object instanc) {
		String result = "<no result>";
		
		try {
			result = String.valueOf(methodToCall.invoke(instanc));
		} catch (IllegalAccessException e) {
			result = "<exc:IllegalAccess=" + e.getMessage() + ">";
		} catch (IllegalArgumentException e) {
			result = "<exc:IllegalArgument=" + e.getMessage() + ">";
		} catch (InvocationTargetException e) {
			result = "<exc:Invocation=" + e.getMessage() + ">";
		}
		
		return result;
	}
}
