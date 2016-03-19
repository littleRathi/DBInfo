package de.bs.cli.jpar.extractor;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.Option;

public class ExtractedArgumentsFieldTest {
	private Field field;
	private Arguments arguments;
	private Option option;
	
	private static final String NAME = "test";
	private static final String OPTION_NAME = ExtractedOption.asOptionName(NAME);
	
	private static Arguments getArgumentsAnnotation(final Field field) {
		return field.getAnnotation(Arguments.class);
	}
	
	// TODO
	// base test first on one valid and then fail test by removing (or if relevant add) allowed elements
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createOption() {
		option = mock(Option.class);
		when(option.annotationType()).thenReturn((Class)Option.class);
		when(option.sourceType()).thenReturn((Class)Void.class);
		when(option.name()).thenReturn(NAME);
		when(option.description()).thenReturn("Some meaningless text...");
	}
	
	private void setTest(final String fieldName) throws NoSuchFieldException, SecurityException {
		field = ExtractedArgumentsFieldTest.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		arguments = getArgumentsAnnotation(field);
	}
	
	@Before
	public void setupTest() {
		createOption();
	}
	
	@After
	public void stopTest() {
		field = null;
		arguments = null;
		option = null;
	}
	
	
	@Arguments
	private String onlyArguments;
	
	@Test(expected=JParException.class)
	public void testOnlyArgumentsWithoutOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyArguments");
		
		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testOnlyArgumentsWithOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyArguments");
		
		ExtractedArguments.getAnnotationOnField(arguments, option, OPTION_NAME, field);
		
		fail();
	}
	
	
	
	@Arguments(name="-test")
	private String onlyName;
	
	@Test(expected=JParException.class)
	public void testOnlyNameWithoutOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyName");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testOnlyNameWithOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyName");

		ExtractedArguments.getAnnotationOnField(arguments, option, OPTION_NAME, field);
		
		fail();
	}
	
	
	
	@Arguments(delimiter="")
	private String onlyDelimiter;
	
	@Test(expected=JParException.class)
	public void testOnlyDelimiterWithoutOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyDelimiter");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testOnlyDelimiterWithOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyDelimiter");

		ExtractedArguments.getAnnotationOnField(arguments, option, OPTION_NAME, field);
		
		fail();
	}
	
	
	
	@Arguments(values={"abc"})
	private String onlyValues;
	
	@Test(expected=JParException.class)
	public void testOnlyValuesWithoutOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyValues");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	@Test
	public void testOnlyValuesWithOption() throws NoSuchFieldException, SecurityException {
		setTest("onlyValues");

		ExtractedArguments result = ExtractedArguments.getAnnotationOnField(arguments, option, OPTION_NAME, field);
		
		assertThat(result, notNullValue());
	}
	
	
	
	@Arguments(name="-test", values={"def"})
	private String nameValues;
	
	@Test(expected=JParException.class)
	public void testNameValuesWithoutOption() throws NoSuchFieldException, SecurityException {
		setTest("nameValues");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testNameValuesWithOption() throws NoSuchFieldException, SecurityException {
		setTest("nameValues");

		ExtractedArguments.getAnnotationOnField(arguments, option, OPTION_NAME, field);
		
		fail();
	}
	
	
	
	@Arguments(name="-test", delimiter="")
	private String nameDelimiter;
	
	@Test(expected=JParException.class)
	public void testNameDelimiterWithoutOption() throws NoSuchFieldException, SecurityException {
		setTest("nameDelimiter");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	@Test(expected=JParException.class)
	public void testNameDelimiterWithOption() throws NoSuchFieldException, SecurityException {
		setTest("nameDelimiter");

		ExtractedArguments.getAnnotationOnField(arguments, option, OPTION_NAME, field);
		
		fail();
	}
	
	@Arguments(values={"test"},delimiter="")
	private String valuesDelimiter;
	
	@Test(expected=JParException.class)
	public void testValuesDelimiterWithoutOption() throws NoSuchFieldException, SecurityException {
		setTest("valuesDelimiter");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	@Test
	// Allowed: it provides ONLY information for ExtractedOption -> if delimiter is needed, the ExtractedOption 
	// will enforce itself.
	public void testValuesDelimiterWithOption() throws NoSuchFieldException, SecurityException {
		setTest("valuesDelimiter");

		ExtractedArguments args = ExtractedArguments.getAnnotationOnField(arguments, option, OPTION_NAME, field);
		
		assertThat(args, notNullValue());
	}
	
	
	// check for field contains values
	@Arguments(name="-test")
	public static String[][] withoutOptionButNull;
	
	@Test(expected=JParException.class)
	public void testWithoutOptionButNull() throws NoSuchFieldException, SecurityException {
		setTest("withoutOptionButNull");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	
	
	@Arguments(name="-test")
	public static String[][] firstDimension = new String[][]{};
	
	@Test(expected=JParException.class)
	public void testFirstDimension() throws NoSuchFieldException, SecurityException {
		setTest("firstDimension");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	
	
	@Arguments(name="-test")
	public static String[][] secondDimension = new String[][]{{}};
	
	@Test(expected=JParException.class)
	public void testSecondDimension() throws NoSuchFieldException, SecurityException {
		setTest("secondDimension");

		ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		fail();
	}
	
	
	
	@Arguments(name="-test")
	public static String[][] correctField = new String[][]{{"fine"}};
	
	@Test
	public void testCorrectField() throws NoSuchFieldException, SecurityException {
		setTest("correctField");
		
		ExtractedArguments args = ExtractedArguments.getAnnotationOnField(arguments, null, OPTION_NAME, field);
		
		assertThat(args, notNullValue());
		assertThat(args.getValues(), notNullValue());
		assertThat(args.getValues().length, greaterThan(0));
		assertThat(args.getValues()[0].length, greaterThan(0));
	}
}
