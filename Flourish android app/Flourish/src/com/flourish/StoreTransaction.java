package com.flourish;

public class StoreTransaction 
{
	private static final StoreTransaction INSTANCE = new StoreTransaction();
	public String mSessionId = null;
	public String mPersonId = null;
	public String mPersonName = null;

	public String getPersonId() {
		return mPersonId;
	}

	public void setPersonId(String mPersonId) {
		this.mPersonId = mPersonId;
	}

	public String getPersonName() {
		return mPersonName;
	}

	public void setPersonName(String mPersonName) {
		this.mPersonName = mPersonName;
	}

	// Private constructor prevents instantiation from other classes
	private StoreTransaction() {}

	public static StoreTransaction getInstance() 
	{
		return INSTANCE;
	}
}