package de.bs.cli.jpar.extractor;

import java.lang.annotation.Annotation;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.process.Parameters;

public class HelpParameter extends ExtractedOption {
	public HelpParameter() {
		super(new Option() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return Option.class;
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
		}, null, Void.class);
	}

	@Override
	public String getTargetName() {
		return ":help";
	}

	@Override
	public void prozessArg(Object program, String option, String argument, Parameters args) {
		System.out.println(de.bs.cli.jpar.JPar.manual(program.getClass()));
		System.exit(0);
	}

}
