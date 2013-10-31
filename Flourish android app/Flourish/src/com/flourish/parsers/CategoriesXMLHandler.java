package com.flourish.parsers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CategoriesXMLHandler extends DefaultHandler 
{
	public String GPId;
	String elementValue = null;
	Boolean elementOn = false;
	Boolean Mcid=false;
	//public static XMLGettersSetters data = null;

	CategoriesGettersSetters Xml;


	/*	public static XMLGettersSetters getXMLData() {
		return data;
	}

	public static void setXMLData(XMLGettersSetters data) {
		XMLHandler.data = data;
	}*/


	public List<CategoriesGettersSetters> mlist=new ArrayList<CategoriesGettersSetters>();

	public List<CategoriesGettersSetters> getsDetails() {
		return mlist;
	}
	/** 
	 * This will be called when the tags of the XML starts.
	 **/
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {

		//		elementOn = true;

		/*if (localName.equals("solution-category"))
		{
			this.elementOn=true;
		}*/ if (localName.equals("name")) {
			this.elementOn=true;

		}
		else if (localName.equals("id")) {
			this.Mcid = true;
		}
	}
	/** 
	 * This is called to get the tags value
	 **/
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if(this.Mcid){

			GPId=new String(ch, start, length);
			//item.add(new String(ch, start, length));
		}

		//		if (elementOn) {
		else if(this.elementOn)
		{
			elementValue = new String(ch, start, length);
		}
		//			elementOn = false;
		//		}

	}
	/** 
	 * This will be called when the tags of the XML end.
	 **/
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		
		
		
		 if (localName.equals("name")) {

			Xml=new CategoriesGettersSetters(elementValue,GPId);

			mlist.add(Xml);
			this.elementOn=false;

		}
		else if (localName.equals("id")) {
			this.Mcid = false;


		}
		 
		 
		 
	
	}



}
