package com.flourish.contacts;


public class Items implements Item, Comparable<Items> {

	private String name;
	private String id;
	private String birthDate;
	private String email;
	private String phone;
	private String street;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private String street2;
	private String city;
	private String state;
	private String zipcode;
	private String country;

	

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSectionItem() {
		return false;
	}

	@Override
	public int compareTo(Items another) 
	{
		String mThisName = this.getName();
		String mAnotherName = another.getName();
		String[] mThisNameSplittedStr = mThisName.split(" ", mThisName.length());
		if(mThisNameSplittedStr.length>1)
		{
			mThisName = mThisNameSplittedStr[1]+" "+mThisNameSplittedStr[0];
		}
		else
		{
			mThisName = this.getName();
		}
		String[] mAnotherNameSplittedStr = mAnotherName.split(" ", mAnotherName.length());
		if(mAnotherNameSplittedStr.length>1)
		{
			mAnotherName = mAnotherNameSplittedStr[1]+" "+mAnotherNameSplittedStr[0];
		}
		else
		{
			mAnotherName = another.getName();
		}
		return mThisName.compareTo(mAnotherName);
	}

}