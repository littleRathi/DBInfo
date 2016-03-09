package de.bs.program.argument.extractor;

import java.lang.annotation.Annotation;

import de.bs.program.argument.Argument;
import de.bs.program.argument.process.Arguments;

public class HelpArgument extends ExtractedArgument {
	public HelpArgument() {
		super(new Argument() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return Argument.class;
			}

			@Override
			public String name() {
				return "help";
			}

			@Override
			public String description() {
				return "show this help information.";
			}

			@Override
			public boolean required() {
				return false;
			}

			@Override
			public Class<?> sourceType() {
				return Void.class;
			}
		});
		setTargetType(Void.class);
	}

	@Override
	public String getTargetName() {
		return ":help";
	}

	@Override
	public void prozessArg(Object program, String argumentName, String argumentValue, Arguments args) {
		System.out.println(de.bs.program.argument.Arguments.getHelpDescription(program.getClass()));
		System.exit(0);
	}

}
