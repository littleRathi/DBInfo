package de.bs.cli.jpar.extractor.type;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.process.Parameters;

@SuppressWarnings("rawtypes")
public abstract class CollectionTypeTestBase<T extends Collection> {
	private CollectionType testee;
	private static final Class SOURCE_TYPE = String.class;
	
	private ExtractedOption option;
	private ExtractedArguments arguments;
	
	private static final String EXTRACTED_ARGUMENT_ARG_NAME = "-test:";
	
	private static final String ARGUMENT_2A = "a00;a02";
	private static final String ARGUMENT_AB = "a00;b00";
	private static final String ARGUMENT_B = "b00";
	
	private static final String DELIMITER = ";";
	private static final String ALLOWED_VALUE_A0 = "a00";
	private static final String ALLOWED_VALUE_A1 = "a01";
	private static final String ALLOWED_VALUE_A2 = "a02";
	private static final String ALLOWED_VALUE_B0 = "b00";
	private static final String[][] ALLOWED_VALUES = new String[][]{
		{ALLOWED_VALUE_A0, ALLOWED_VALUE_A1, ALLOWED_VALUE_A2},{ALLOWED_VALUE_B0}
	};
	
	protected abstract Class<T> getCollectionType();
	
	protected abstract Collection getCollectionInstance();
	
	protected abstract Collection getWrongCollectionInstance();
		
	private ExtractedArguments mockExtractedArgumentsDelimiter() {
		arguments = mock(ExtractedArguments.class);
		when(arguments.getDelimiter()).thenReturn(DELIMITER);
		when(arguments.getValues()).thenReturn(ALLOWED_VALUES);
		
		return arguments;
	}
	
	private ExtractedArguments mockExtractedArgumentsDelimiterOnly() {
		arguments = mock(ExtractedArguments.class);
		when(arguments.getDelimiter()).thenReturn(DELIMITER);
		
		return arguments;
	}
	
	@SuppressWarnings("unchecked")
	private ExtractedOption mockExtractedOption() {
		option = mock(ExtractedOption.class);
		when(option.getOptionName()).thenReturn(EXTRACTED_ARGUMENT_ARG_NAME);
		when(option.getManualDescription()).thenReturn("Some meaningless text");
		when(option.getSourceType()).thenReturn(SOURCE_TYPE);
		
		return option;
	}
	
	@Before
	public void setupTest() {
		testee = new CollectionType(getCollectionType(), mockExtractedOption(), mockExtractedArgumentsDelimiter());
	}
	
	// type check test
	@Test
	public void testCollectionTestClass() {
		Class<T> collectionClass = getCollectionType();
		
		assertThat("Class '" + collectionClass + "' is not a subclass of '" + Collection.class + "'.", 
				Collection.class.isAssignableFrom(collectionClass), equalTo(true));
		
	}
	
	// testcases:Type (superclass)
	@Test
	public void testGetOption() {
		ExtractedOption returned = testee.getOption();
		
		assertThat(returned, equalTo(option));
	}
	
	@SuppressWarnings({ "unchecked" })
	@Test
	public void testGetTargetType() {
		Class<T> returned = (Class<T>)testee.getTargetType();
		
		assertThat(returned, equalTo((Class)getCollectionType()));
	}
	
	@Test
	public void testWithSourceType() {
		Class returned = (Class)testee.getOption().getSourceType();
		
		assertThat(returned, equalTo((Class)SOURCE_TYPE));
	}
	
	@SuppressWarnings({ "unchecked", })
	@Test(expected=JParException.class)
	public void testWithoutSourceType() {
		option = mockExtractedOption();
		when(option.getSourceType()).thenReturn((Class)Void.class);
		
		testee = new CollectionType(getCollectionType(), option, null);
		
		fail();
	}
	
	// testcases:CollectionType
	@Test
	public void testUsageDescription() {
		StringBuilder sb = new StringBuilder();
		testee.getManualDescription(sb);
		String description = sb.toString();
		
		assertThat(description, containsString(EXTRACTED_ARGUMENT_ARG_NAME));
		assertThat(description, containsString(SOURCE_TYPE.getSimpleName()));
		assertThat(description, containsString(DELIMITER));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgs2AWithValues() {
		when(arguments.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_2A});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_2A, args);

		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((Collection<?>)result), hasSize(2));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_A2));
	}
	
	@Test(expected=JParException.class)
	public void testProcessArgsABWithValues() {
		when(arguments.validValues((String[])isNotNull())).thenReturn(false);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_AB});
		testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_AB, args);
		
		fail();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsBWithValues() {
		when(arguments.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_B});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_B, args);
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((Collection<?>)result), hasSize(1));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_B0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgs2AWithoutValuesProperty() {
		testee = new CollectionType(getCollectionType(), mockExtractedOption(), mockExtractedArgumentsDelimiterOnly());
		when(arguments.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_2A});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_2A, args);
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((Collection<?>)result), hasSize(2));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_A2));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsABWithoutValuesProperty() {
		testee = new CollectionType(getCollectionType(), mockExtractedOption(), mockExtractedArgumentsDelimiterOnly());
		when(arguments.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_AB});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ARGUMENT_AB, args);
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((Collection<?>)result), hasSize(2));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_A0, ALLOWED_VALUE_B0));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testProcessArgsBWithoutValuesProperty() {
		testee = new CollectionType(getCollectionType(), mockExtractedOption(), mockExtractedArgumentsDelimiterOnly());
		when(arguments.validValues((String[])isNotNull())).thenReturn(true);
		
		Parameters args = new Parameters(new String[]{EXTRACTED_ARGUMENT_ARG_NAME + ARGUMENT_B});
		Object result = testee.processArgs(EXTRACTED_ARGUMENT_ARG_NAME, ALLOWED_VALUE_B0, args);
		
		assertThat(result, instanceOf(getCollectionType()));
		assertThat(((Collection<?>)result), hasSize(1));
		assertThat(((Collection<String>)result), hasItems(ALLOWED_VALUE_B0));
	}
}
