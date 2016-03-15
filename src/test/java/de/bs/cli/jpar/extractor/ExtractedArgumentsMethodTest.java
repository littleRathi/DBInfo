package de.bs.cli.jpar.extractor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.Option;

public class ExtractedArgumentsMethodTest {
	private Method method;
	private Arguments arguments;
	private Option option;
	
	private static String NAME = "test";
	private static String OPTION_NAME = "-" + NAME;
	
	private static String VALUE_A00 = "a00";
	private static String VALUE_A01 = "a01";
	private static String VALUE_B00 = "b00";
	private static String VALUE_B01 = "b01";
	private static String[][] VALUES = {{VALUE_A00, VALUE_A01},{VALUE_B00, VALUE_B01}};
	private static String[][] VALUES_INVALID_00 = null;
	private static String[][] VALUES_INVALID_01 = {};
	private static String[][] VALUES_INVALID_02 = {{}};
	
	private static Arguments getArgumentsAnnotation(final Method method) {
		return method.getAnnotation(Arguments.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createOption() {
		option = mock(Option.class);
		when(option.annotationType()).thenReturn((Class)Option.class);
		when(option.sourceType()).thenReturn((Class)Void.class);
		when(option.name()).thenReturn(NAME);
		when(option.description()).thenReturn("Some meaningless text...");
	}
	
	private void setTest(final String methodName, final Class<?>... parameters) throws NoSuchMethodException, SecurityException {
		method = getMethod(methodName, parameters);
		arguments = getArgumentsAnnotation(method);
	}
	private static Method getMethod(final String methodName, final Class<?>... parameters) throws NoSuchMethodException, SecurityException {
		Class<?> thisClass = ExtractedArgumentsMethodTest.class;
		
		return thisClass.getMethod(methodName, parameters);
	}
	
	@Before
	public void setupTest() {
		createOption();
	}
	
	@After
	public void stopTest() {
		method = null;
		arguments = null;
		option = null;
	}
	
	// double tests: without and with @Option (mocked)
	@Arguments(name="-test")
	public static String[][] valuesCorrect() {
		return VALUES;
	}
	
	@Test
	public void testValuesCorrect() throws NoSuchMethodException, SecurityException {
		setTest("valuesCorrect");
		
		ExtractedArguments args = ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		assertThat(args, notNullValue());
	}
	
	@Test(expected=JParException.class)
	public void testValuesCorrectWithOption() throws NoSuchMethodException, SecurityException {
		setTest("valuesCorrect");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
		
		fail();
	}
	
	
	
	@Arguments(name="-test")
	public static void wrongReturnType() {
	}
	
	@Test(expected=JParException.class)
	public void testWrongReturnType() throws NoSuchMethodException, SecurityException {
		setTest("wrongReturnType");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testWrongReturnTypeWithOption() throws NoSuchMethodException, SecurityException {
		setTest("wrongReturnType");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
		
		fail();
	}
	
	
	
	@Arguments(name="-test")
	public static String[][] withParameters(final int number) {
		return null;
	}
	
	@Test(expected=JParException.class)
	public void testWithParameters() throws NoSuchMethodException, SecurityException {
		setTest("withParameters", int.class);
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testWithParametersWithOption() throws NoSuchMethodException, SecurityException {
		setTest("withParameters", int.class);
		
		ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
		
		fail();
	}
	
	
	
	@Arguments(name="-test", values={"a00", "a01"})
	public static String[][] alsoWithValidValues() {
		return null;
	}
	
	@Test(expected=JParException.class)
	public void testAlsoWithValidValues() throws NoSuchMethodException, SecurityException {
		setTest("alsoWithValidValues");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testAlsoWithValidValuesWithOption() throws NoSuchMethodException, SecurityException {
		setTest("alsoWithValidValues");
		
		ExtractedArguments args = ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
		
		assertThat(args, notNullValue());
	}
	
	
	
	@Arguments(name="-test", delimiter="")
	public static String[][] invalidDelimiter() {
		return null;
	}
	
	@Test(expected=JParException.class)
	public void testInvalidDelimiter() throws NoSuchMethodException, SecurityException {
		setTest("invalidDelimiter");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testInvalidDelimiterWithOption() throws NoSuchMethodException, SecurityException {
		setTest("invalidDelimiter");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		fail();
	}
	
	
	
	@Arguments(name="-test")
	public String[][] notStatic() {
		return null;
	}
	
	@Test(expected=JParException.class)
	public void testNotStatic() throws NoSuchMethodException, SecurityException {
		setTest("notStatic");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testNotStaticWithOption() throws NoSuchMethodException, SecurityException {
		setTest("notStatic");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
		
		fail();
	}
	
	
	
	@Arguments
	public static String[][] missingOptionName() {
		return null;
	}
	
	@Test(expected=JParException.class)
	public void testMissingOptionName() throws NoSuchMethodException, SecurityException {
		setTest("missingOptionName");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testMissingOptionNameWithOption() throws NoSuchMethodException, SecurityException {
		setTest("missingOptionName");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
		
		fail();
	}
	
	
	
	@Arguments(values={"abc", "def"})
	public static void argumentsCorrectWithOptionAnnotation() {
	}
	
	@Test(expected=JParException.class)
	public void testArgumentsCorrectWithOptionAnnotation() throws NoSuchMethodException, SecurityException {
		setTest("argumentsCorrectWithOptionAnnotation");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
	
		fail();
	}
	
	@Test
	public void testArgumentsCorrectWithOptionAnnotationWithOption() throws NoSuchMethodException, SecurityException {
		setTest("argumentsCorrectWithOptionAnnotation");
		
		ExtractedArguments args = ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
	
		assertThat(args, notNullValue());
	}
	
	
	
	// test Arguments.values (with option mockup)
	@Arguments(values={})
	public static void valuesEmptyOption() {
	}
	
	@Test(expected=JParException.class)
	public void testValuesEmptyOption() throws NoSuchMethodException, SecurityException {
		setTest("valuesEmptyOption");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, option, OPTION_NAME, method);
	
		fail();
	}
	
	
	
	// test return values of method (without option annotation)
	@Arguments(name="test")
	public static String[][] returnsNull() {
		return VALUES_INVALID_00;
	}
	
	@Test(expected=JParException.class)
	public void testReturnsNull() throws NoSuchMethodException, SecurityException {
		setTest("returnsNull");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
	
		fail();
	}
	
	
	
	@Arguments(name="test")
	public static String[][] returnsEmptyArray() {
		return VALUES_INVALID_01;
	}
	
	@Test(expected=JParException.class)
	public void testReturnsEmptyArray() throws NoSuchMethodException, SecurityException {
		setTest("returnsEmptyArray");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
	
		fail();
	}
	
	
	
	@Arguments(name="test")
	public static String[][] returnsEmptyEmptyArray() {
		return VALUES_INVALID_02;
	}
	
	@Test(expected=JParException.class)
	public void testReturnsEmptyEmptyArray() throws NoSuchMethodException, SecurityException {
		setTest("returnsEmptyEmptyArray");
		
		ExtractedArguments.getAnnotationOnMethod(arguments, null, OPTION_NAME, method);
	
		fail();
	}
}
