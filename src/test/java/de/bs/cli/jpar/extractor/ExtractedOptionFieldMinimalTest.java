package de.bs.cli.jpar.extractor;

import java.lang.reflect.Field;

import org.junit.Before;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.internal.Defaults;

public class ExtractedOptionFieldMinimalTest extends ExtractedOptionBaseTest {
	public static final String OPT_NAME = "all";
	public static final String OPT_DESCRIPTION = "example description test abc";
	public static final String OPT_FIELD_NAME = "testFieldMinimal";
	public Field optField;
	
	@Option(name=OPT_NAME, description=OPT_DESCRIPTION)
	private String testFieldMinimal;

	private Field getField() throws NoSuchFieldException, SecurityException {
		return getClass().getDeclaredField(OPT_FIELD_NAME);
	}
	
	private Option getOptionAnnotation(final Field field) {
		return field.getAnnotation(Option.class);
	}
	
	@Before
	public void setupTest() throws NoSuchFieldException, SecurityException {
		optField = getField();
		this.option = getOptionAnnotation(optField);
		this.testee = new ExtractedOptionField(optField, option, null);
	}
	
	@Override
	protected String getName() {
		return OPT_NAME;
	}

	@Override
	protected Class<?> getTargetType() {
		return optField.getType();
	}

	@Override
	protected String getTargetName() {
		return OPT_FIELD_NAME;
	}

	@Override
	protected Class<?> getSourceType() {
		return Defaults.SOURCE_TYPE;
	}

	@Override
	protected String getManualDecription() {
		return OPT_DESCRIPTION;
	}

	@Override
	protected boolean getRequired() {
		return Defaults.REQUIRED;
	}

}
