package model;

import java.io.Serializable;
import java.util.ArrayList;

public class DataPack implements Serializable {
	private static final long serialVersionUID = 1L;
	/*
	 * 0:heart-beat pack
	 * 1:message to person
	 * 2:message to group
	 * 3:drawing board to person
	 * 4:drawing board to group
	 */
	private int mode;
	private String msg;
	private ArrayList<Double> x;
	private ArrayList<Double> y;
	public DataPack(){}
	public DataPack(int mode, String msg, ArrayList<Double> x, ArrayList<Double> y) {
		super();
		this.mode = mode;
		this.msg = msg;
		this.x = x;
		this.y = y;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ArrayList<Double> getX() {
		return x;
	}
	public void setX(ArrayList<Double> x) {
		this.x = x;
	}
	public ArrayList<Double> getY() {
		return y;
	}
	public void setY(ArrayList<Double> y) {
		this.y = y;
	}
}
