package com.das.helper;

public class UploadData<R, T>{
    private R column;
    private T value;

    public T getValue(){
        return value;
    }
    public String getColumnName(){
        return ((Column) column).getColumn();
    }
    public UploadData(R column,T value){
        this.column=column;
        this.value=value;
    }
    
}