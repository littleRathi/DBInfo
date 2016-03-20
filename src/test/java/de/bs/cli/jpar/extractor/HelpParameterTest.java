package de.bs.cli.jpar.extractor;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import static de.bs.cli.jpar.extractor.ExtractedOption.asElName;
import static de.bs.cli.jpar.extractor.ExtractedOption.asOptionName;

import org.junit.Before;
import org.junit.Test;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.config.Consts;
import de.bs.cli.jpar.config.Defaults;
import de.bs.cli.jpar.extractor.type.Type;
import de.bs.cli.jpar.extractor.type.VoidType;
import de.bs.cli.jpar.process.Parameters;


public class HelpParameterTest {
	private HelpParameter testee;
	
	private static final String OPTION_NAME = asOptionName(Consts.NAME_HELP);
	private static final String ARGUMENT_ILLEGAL_STRING = "abc";
	
	@Before
	public void setupTest() {
		testee = new HelpParameter();
	}
	
	@Test
	public void testGetELName() {
		String result = testee.getElName();

		assertThat(result, equalTo(asElName(Consts.NAME_HELP)));
	}
	
	@Test
	public void testGetName() {
		String result = testee.getName();
		
		assertThat(result, equalTo(Consts.NAME_HELP));
	}
	
	@Test
	public void testGetOptionName() {
		String result = testee.getOptionName();
		
		assertThat(result, equalTo(asOptionName(Consts.NAME_HELP)));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetTargetType() {
		Class result = testee.getTargetType();
		
		assertThat(result, equalTo((Class)Void.class));
	}
	
	@Test
	public void testGetTargetName() {
		String result = testee.getTargetName();
		
		assertThat(result, equalTo(Consts.PHANTOM_TARGET_NAME_PREFIX + Consts.NAME_HELP));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetSourceType() {
		Class result = testee.getSourceType();
		
		assertThat(result, equalTo((Class)Void.class));
	}
	
	@Test
	public void testGetManualDescription() {
		String result = testee.getManualDescription();
		
		assertThat(result, notNullValue());
		assertThat(result, not(isEmptyOrNullString()));
	}
	
	@Test
	public void testGetRequired() {
		boolean result = testee.isRequired();
		
		assertThat(result, equalTo(Boolean.FALSE));
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetType() {
		Type result = testee.getType();
		
		assertThat(result, notNullValue());
		assertThat(result.getClass(), equalTo((Class)VoidType.class));
	}
	
	@Test(expected=JParException.class)
	public void testProcessArgWithArgument() {
		Parameters parameters = new Parameters(new String[]{OPTION_NAME + Defaults.getOptionDelimiter() + ARGUMENT_ILLEGAL_STRING});
		testee.processArg(this, OPTION_NAME, ARGUMENT_ILLEGAL_STRING, parameters);

		fail();
	}
}
