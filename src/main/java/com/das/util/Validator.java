package com.das.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	 public static boolean isNameValid(String name){
	        String allowedChars = "^[a-z-0-9- _ ( ) /]+$";
	        Pattern pattern = Pattern.compile(allowedChars, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(name);

	        if(!matcher.matches()){
	            return false;
	        }

	        return true;
	 }
	 
	 public static final boolean isPhoneNumberValid(String target){
	        return Pattern.compile("^\\+[0-9]{2}[0-9]{10}$").matcher(target).matches();
	    }

	    public static final boolean isEmailValid(String target){
	        return Pattern.compile("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$").matcher(target).matches();
	    }

	    public static final boolean isPasswordValid(String target){
	        return Pattern.compile("^[A-Z](?=.*\\d)(?=.*[a-z])[\\w~@#$%^&*+=`|{}:;!.?\"()\\[\\]]{8,25}$").matcher(target).matches();
	    }
	    
	    public static final boolean isEmpty(String target) {
	    	return !(target != null && target.length() > 0);
	    }
}

