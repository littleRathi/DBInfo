package de.bs.dbinfo.exporter;

public interface DataBlock {
	String getHeader();
	void setHeader(final String header);
	void addHeaderRow(final String prefix, final String[] data);
	void addRow(final String prefix, final String[] data);
}
