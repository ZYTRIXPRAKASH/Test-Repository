package com.flourish.parsers;

public class FieldsGetterSetters {


	public String catid;
	public String catname;




	public String getCatid() {
		return catid;
	}


	public String getCatname() {
		return catname;
	}



	public  FieldsGetterSetters(String CatId,String CatName){
		catid=CatId;
		catname=CatName;

	}
}