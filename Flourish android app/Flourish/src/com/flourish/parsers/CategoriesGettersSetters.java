package com.flourish.parsers;


/**
 *  This class contains all getter and setter methods to set and retrieve data.
 *  
 **/
public class CategoriesGettersSetters {
	
	
	String name;
	public String id;
	
	
	public String getId() {
		return id;
	}


	public String getName(){
		return name;
	}
	
	
	public CategoriesGettersSetters(String Mname,String Mid) {
		// TODO Auto-generated constructor stub
		
		name=Mname;
		id=Mid;
	}
	

}
