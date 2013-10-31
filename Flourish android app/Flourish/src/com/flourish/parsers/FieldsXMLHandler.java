package com.flourish.parsers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FieldsXMLHandler extends DefaultHandler{

	public String CPId;
	String elementValues = null;
	Boolean elementOnCat = false;
	Boolean Mcatid=false;
	FieldsGetterSetters Cat;

	public List<FieldsGetterSetters> mcatlist=new ArrayList<FieldsGetterSetters>();

	public List<FieldsGetterSetters> getCategories() {
		return mcatlist;
	}
	/** 
	 * This will be called when the tags of the XML starts.
	 **/
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {

		//		elementOn = true;

		if (localName.equals("title"))
		{
			this.elementOnCat=true;
		} /*else if (localName.equals("category-id")) {
			this.elementOnCat=true;

		}*/
		else if (localName.equals("id")) {
			this.Mcatid = true;
		}
	}
	/** 
	 * This is called to get the tags value
	 **/
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if(this.Mcatid){

			CPId=new String(ch, start, length);
			//item.add(new String(ch, start, length));
		}

		//		if (elementOn) {
		else if(this.elementOnCat)
		{
			elementValues = new String(ch, start, length);
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

// here we can directly give title
		if (localName.equals("title"))
		{
			this.elementOnCat=false;

			Cat = new FieldsGetterSetters(CPId, elementValues);

			mcatlist.add(Cat);
		} 
		/*else if (localName.equals("category-id")) {
			this.elementOnCat=false;

		}*/
		else if (localName.equals("id")) {
			this.Mcatid = false;


		}
	}




}
