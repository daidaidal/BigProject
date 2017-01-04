package model;

public class FileData {
	private String filename;
	private long filelen;
	private boolean created;
	private String belong;
	private String path;
	public FileData(String filename, long filelen, boolean created, String belong, String path) {
		super();
		this.filename = filename;
		this.filelen = filelen;
		this.created = created;
		this.belong = belong;
		this.path = path;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFilelen() {
		return filelen;
	}
	public void setFilelen(long filelen) {
		this.filelen = filelen;
	}
	public boolean isCreated() {
		return created;
	}
	public void setCreated(boolean created) {
		this.created = created;
	}
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
