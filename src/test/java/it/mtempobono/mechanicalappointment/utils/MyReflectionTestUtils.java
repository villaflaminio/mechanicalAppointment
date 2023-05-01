package it.mtempobono.mechanicalappointment.utils;

import org.apache.commons.lang.builder.EqualsBuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyReflectionTestUtils {
	
	private static final String NOT_EQUALS = "%s: [ %s ] != [ %s ]";
	private static final String REFLECT_ERROR = "%s: ERROR -> %s";

	/**
	 * Uses reflection to get a list of getters from <b>base</b> and invokes
	 * that method on <b>base</b> and <b>compareTo</b>. If the results of the
	 * getters !=, or the compareTo class doesn't have that getter, then that
	 * method name would be returned in the list
	 * 
	 * @param base 
	 *            object to get the list of getters
	 * @param compareTo
	 *            object that the list of getters is compared to
	 * @return list of methods that weren't equal
	 */
	public static List<String> compareGetters(Object base, Object compareTo, List<String> compareFields) {
		List<String> notEquals = new ArrayList<String>();
		
		for(Method method : getGetters(base.getClass(), compareFields)) {
			try {
				Method compareMethod = compareTo.getClass().getMethod(method.getName());
				
				Object baseResult = method.invoke(base);
				Object compareResult = compareMethod.invoke(compareTo);
				
				if( !new EqualsBuilder().append(baseResult, compareResult).isEquals() ) {
					notEquals.add(String.format(NOT_EQUALS, method.getName(), baseResult, compareResult));
				}
			} catch (NullPointerException e) {
				throw e;
			} catch (Exception e) {
				notEquals.add(String.format(REFLECT_ERROR, method.getName(), e.getMessage()));
			}
		}
		
		return notEquals;
	}

	/**
	 * finds the getters of a class 
	 * 
	 * @param clazz
	 * @return list of Method
	 */
	@SuppressWarnings("rawtypes")
	public static List<Method> getGetters(Class clazz, List<String> compareFields) {

		if(compareFields == null) {
			compareFields = new ArrayList<String>();
		}

		List<Method> getters = new ArrayList<Method>();

		// get getters
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (isGetter(method) && listContainsString(compareFields, method.getName()) ) {
				getters.add(method);
			}
		}

		return getters;
	}
	
	/**
	 * Runs an upper case contains for each element of <b>list</b> for <b>string</b>
	 * i.e.: <code>item.toUpperCase().contains(string.toUpperCase())</code>
	 * 
	 * @param list
	 * @param string
	 * @return true or false
	 */
	private static boolean listContainsString(List<String> list, String string) {
		boolean contains = false;
		for(String item: list) {
			item = "get"+item;
			if(string.toUpperCase().equals(item.toUpperCase())) {
				contains = true;
				break;
			}
		}
		
		return contains;
	}

	/**
	 * @param method
	 * @return true or false if the method is a getter
	 */
	public static boolean isGetter(Method method) {
		if (!method.getName().startsWith("get")) {
			return false;
		}
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		if (void.class.equals(method.getReturnType())) {
			return false;
		}
		return true;
	}
}