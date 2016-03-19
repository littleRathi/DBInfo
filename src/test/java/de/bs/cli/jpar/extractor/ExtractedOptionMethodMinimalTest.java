package de.bs.cli.jpar.extractor;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Before;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.config.Consts;
import de.bs.cli.jpar.extractor.type.BooleanType;

public class ExtractedOptionMethodMinimalTest extends ExtractedOptionBaseTest {
	public static final String OPT_NAME = "all";
	public static final String OPT_DESCRIPTION = "example description for method test all...";
	public static final String OPT_METHOD_NAME = "testMethodMinimal";
	public static final String ARG_FAIL = "fail";
	public Method optMethod;
	
	@Option(name=OPT_NAME, description=OPT_DESCRIPTION)
	public void testMethodMinimal(final boolean bool) {
		container = bool;
	}
	public boolean container;
	
	private Method getMethod() throws NoSuchMethodException, SecurityException {
		return getClass().getDeclaredMethod(OPT_METHOD_NAME, new Class<?>[]{boolean.class});
	}

	private Option getOptionAnnotation(final Method method) {
		return method.getAnnotation(Option.class);
	}
	
	@Before
	public void setupTest() throws NoSuchMethodException, SecurityException {
		optMethod = getMethod();
		this.option = getOptionAnnotation(optMethod);
		this.testee = new ExtractedOptionMethod(optMethod, option, null);
	}

	@Override
	protected String getName() {
		return OPT_NAME;
	}

	@Override
	protected Class<?> getTargetType() {
		return Boolean.class;
	}

	@Override
	protected String getTargetName() {
		return OPT_METHOD_NAME;
	}

	@Override
	protected Class<?> getSourceType() {
		return Consts.SOURCE_TYPE;
	}

	@Override
	protected String getManualDecription() {
		return OPT_DESCRIPTION;
	}

	@Override
	protected boolean getRequired() {
		return Consts.REQUIRED;
	}
	
	@Override
	protected String successArgument() {
		return BooleanType.TRUE;
	}
	
	@Override
	protected void successCheck() {
		assertThat(container, equalTo(Boolean.TRUE));
	}
	
	@Override
	protected String failArgument()  {
		return ARG_FAIL;
	}
}
