package com.das.dao;

import java.util.ArrayList;

import com.das.db.Db;
import com.das.helper.UploadData;
import com.das.util.Provider;

public abstract  class Dao<R, T>{
	protected Db db;
	protected String table;
	
	public Dao(String table) {
		db = new Db(); 
		this.table = table;
	}
	
	public abstract int insert(T obj);
	public abstract ArrayList<T> get(int id);
	public abstract ArrayList<T> get(ArrayList<UploadData<R, ?>> set);
	public abstract ArrayList<T> get();
	public abstract int update(T obj);
	public abstract int update(ArrayList<UploadData<R, ?>> set, int id);
	public abstract int delete(int id);
	public abstract int delete(ArrayList<UploadData<R, ?>> set);
	public abstract long count();
}
