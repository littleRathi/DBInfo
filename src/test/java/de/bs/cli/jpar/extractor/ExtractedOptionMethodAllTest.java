package de.bs.cli.jpar.extractor;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Before;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.config.Consts;
import de.bs.cli.jpar.config.Defaults;

public class ExtractedOptionMethodAllTest extends ExtractedOptionBaseTest {
	private static final Class<?> OPT_SOURCE_TYPE = String.class;
	public static final String OPT_NAME = "all";
	public static final String OPT_DESCRIPTION = "example description for method test all...";
	public static final boolean OPT_REQUIRED = !Consts.REQUIRED;
	public static final String OPT_METHOD_NAME = "testMethodAll";
	public static final String[][] ARG_VALID = {{"a", "b"}};
	public static final String ARG_SUCCESS = "a" + Defaults.getListDelimiter() + "b";
	public static final String ARG_FAIL = "b" + Defaults.getListDelimiter() + "c";
	public Method optMethod;
	
	@Option(name=OPT_NAME, description=OPT_DESCRIPTION, required=OPT_REQUIRED, sourceType=String.class)
	public void testMethodAll(final Collection<String> collection) {
		container = collection;
	}
	private Collection<String> container;
	
	private Method getMethod() throws NoSuchMethodException, SecurityException {
		return getClass().getDeclaredMethod(OPT_METHOD_NAME, new Class<?>[]{Collection.class});
	}

	private Option getOptionAnnotation(final Method method) {
		return method.getAnnotation(Option.class);
	}
	
	private ExtractedArguments mockArguments() {
		ExtractedArguments args = mock(ExtractedArguments.class);
		when(args.getDelimiter()).thenReturn(Defaults.getListDelimiter());
		when(args.getValues()).thenReturn(ARG_VALID);
		when(args.validValues(ARG_VALID[0])).thenReturn(true);
		return args;
	}
	
	@Before
	public void setupTest() throws NoSuchMethodException, SecurityException {
		optMethod = getMethod();
		this.option = getOptionAnnotation(optMethod);
		this.testee = new ExtractedOptionMethod(optMethod, option, mockArguments());
	}
	
	@Override
	protected String getName() {
		return OPT_NAME;
	}

	@Override
	protected Class<?> getTargetType() {
		Class<?>[] params = optMethod.getParameterTypes();
		return params[0];
	}

	@Override
	protected String getTargetName() {
		return OPT_METHOD_NAME;
	}

	@Override
	protected Class<?> getSourceType() {
		return OPT_SOURCE_TYPE;
	}

	@Override
	protected String getManualDecription() {
		return OPT_DESCRIPTION;
	}

	@Override
	protected boolean getRequired() {
		return OPT_REQUIRED;
	}
	
	@Override
	protected String successArgument() {
		return ARG_SUCCESS;
	}
	
	@Override
	protected void successCheck() {
		assertThat(container, notNullValue());
		assertThat(container, hasSize(2));
		assertThat(container, hasItems("a", "b"));
	}
	
	@Override
	protected String failArgument()  {
		return ARG_FAIL;
	}
}
