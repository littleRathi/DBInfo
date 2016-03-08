package de.bs.dbinfo.exporter;

public interface Exporter {
	String getIdentifier();
	void setIdentifier(final String identifier);
	DataBlock createDataBlock();
	void done();
}
