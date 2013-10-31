package com.flourish.baseapplication;

import android.app.Application;

public class FlourishBaseApplication extends Application {
	
	public String mFlurishBaseUrl="http://apidev.verticaloffice.com/";//base url of all service
	
  public String  mLoginUrl_get="User.svc/json/Login/ioCodeFA/drinkredbull/";
  public String  inventory_product_list_url_Get ="/Inventory.svc/json/Products/Search/ioCodeFA/drinkredbull/";
  public String  create_user_url= "User.svc/json/User/ioCodeCO/drinkredbull";
                                
  public String  login_url= "User.svc/json/Login/ioCodeFA/drinkredbull/";
  
  public String  get_contacts_url= "/Contact.svc/json/Contacts/GetActive/ioCodeFA/drinkredbull/";
  public String  get_contacts_address_url= "Contact.svc/json/Contact/Addresses/ioCodeFA/drinkredbull/";
  public String  get_contacts_phone_url= "Contact.svc/json/Contact/Phones/ioCodeFA/drinkredbull/";
  public String  get_contacts_email_url= "Contact.svc/json/Contact/InternetAddresses/ioCodeFA/drinkredbull/";
  public String  add_contact_phone_number_url= "Contact.svc/json/Contact/Phone/ioCodeFA/drinkredbull/";
  public String  add_contact_email_address_url= "Contact.svc/json/Contact/InternetAddress/ioCodeFA/drinkredbull/";
  public String  add_contact_address_url= "Contact.svc/json/Contact/Address/ioCodeFA/drinkredbull/";
  public String  update_contact_url= "/Contact.svc/json/Contact/ioCodeFA/drinkredbull/";
  public String  create_contact_url="/Contact.svc/json/Contact/ioCodeFA/drinkredbull/";
  public String  delete_contact_url= "Contact.svc/json/Contact/ioCodeFA/drinkredbull/";
  public String  inventory_url= "Inventory.svc/json/Product/ioCodeFA/drinkredbull/";
  public String  inventory_get_favorite_products_url= "Inventory.svc/json/Products/Favorites/ioCodeFA/drinkredbull/";
  public String  sales_by_contact_totals_url= "Invoice.svc/json/SalesByContact/Totals/ioCodeFA/drinkredbull/";
  public String  create_invoice_url= "Invoice.svc/json/Invoice/Create/ioCodeFA/drinkredbull/";
  public String  create_line_item_for_invoice_url= "Invoice.svc/json/Invoice/Item/ioCodeFA/drinkredbull/";
  public String  get_sales_list_url= "Invoice.svc/json/Sales/Paged/ioCodeFA/drinkredbull/";
  public String  get_product_list_url= "Inventory.svc/json/Products/Search/ioCodeFA/drinkredbull/";
  public String  get_sales_invoice_items_url= "Invoice.svc/json/Sales/Invoice/Items/ioCodeFA/drinkredbull/";
  public String  get_flourish_articles_list_url="http://flourish.freshdesk.com/solution/categories/15664.xml";
  public String  delete_invoice_url= "Invoice.svc/json/Sales/Invoice/ioCodeFA/drinkredbull/";
  public String  get_flourish_articles_list_json_url="http://flourish.freshdesk.com/solution/categories/15664/folders/24967/articles/8852-what-is-flourish-.json";
  public String  get_expenses_list= "Money.svc/json/Ledger/Expense/Active/ioCodeFA/drinkredbull/";
  public String  get_mileage_list= "Money.svc/json/Mileage/List/ioCodeFA/drinkredbull/";
  public String  get_income_list= "Money.svc/json/Ledger/Income/Active/ioCodeFA/drinkredbull/";
  public String  add_mileage= "Money.svc/json/Mileage/Add/ioCodeFA/drinkredbull/";
  public String  update_expenses= "/Money.svc/json/Journal/Expense/ioCodeFA/drinkredbull/";
  public String  update_mileage= "/Money.svc/json/Mileage/ioCodeFA/drinkredbull/";
  public String  update_income= "/Money.svc/json/Journal/Income/ioCodeFA/drinkredbull/";
  public String  get_vehicles= "Money.svc/json/Vehicles/GetActive/ioCodeFA/drinkredbull/";
  public String  get_expenses= "Money.svc/json/Accounts/Expense/Active/ioCodeFA/drinkredbull/";
  public String  get_income= "Money.svc/json/Accounts/Income/Active/ioCodeFA/drinkredbull/";
  public String  delete_expenses= "/Money.svc/json/Journal/ioCodeFA/drinkredbull/";
 
 
  public String  get_all_undelivered_items =  "Invoice.svc/json/Invoice/Undelivered/ioCodeFA/drinkredbull/";
  public String  add_a_delivery_to_an_invoice ="/Invoice.svc/json/Invoice/Delivery/ioCodeFA/drinkredbull/";
  public String  add_an_item_to_a_delivery="/Invoice.svc/json/Invoice/Delivery/Item/ioCodeFA/drinkredbull/";
  public String  saves_an_item_on_a_delivery="/Invoice.svc/json/Invoice/Delivery/Item/ioCodeFA/drinkredbull/";
                                     
 // public String  invoice_Delivery_Item="/Invoice.svc/json/Invoice/Delivery/Item/{AffiliateUser}/{AffiliatePass}/{LoginKey}/{InvoiceId}"
  public String  save_changes_to_a_delivery="Invoice.svc/json/Invoice/Delivery/";

  public String  invoice_delivery_execute = "Invoice.svc/json/Invoice/Delivery/Execute/ioCodeFA/drinkredbull/";
  public String  get_all_delivered_items = "Invoice.svc/json/Invoice/Deliveries/ioCodeFA/drinkredbull/";
  public String  get_delivery_types="Invoice.svc/json/Invoice/Delivery/Types/ioCodeFA/drinkredbull/";
  public String  updates_an_invoices="/Invoice.svc/json/Sales/Invoice/ioCodeFA/drinkredbull/";//70a1233a-a1e7-4b54-ac27-836f6ed01866/375678
  public String  Finalizes_a_delivery="Invoice.svc/json/Invoice/Delivery/Execute/ioCodeFA/drinkredbull/";
//  /Invoice.svc/json/Invoice/Delivery/Execute/{AffiliateUser}/{AffiliatePass}/{LoginKey}/{DeliveryId}
	   
  
  public String  update_expenses_photo="Money.svc/json/Journal/Receipt/ioCodeFA/drinkredbull/";
  public String  get_all_payments="Invoice.svc/json/Invoice/Payments/ioCodeFA/drinkredbull/";
  public String  add_payment_request="Invoice.svc/json/Invoice/Payment/ioCodeFA/drinkredbull/";
  public String  get_user_settings="/User.svc/json/Settings/ioCodeFA/drinkredbull/";//affiliateUser/affiliatePW/LoginKey";
  public String  save_user_setting="/User.svc/json/Address/ioCodeFA/drinkredbull/";//affiliateUser/affiliatePW/LoginKey"
	   
  public String bookingList="/Calendar.svc/json/Bookings/Active/ioCodeFA/drinkredbull/";	    
  public String updatesAlineItemOnInvoice="/Invoice.svc/json/Invoice/Item//ioCodeFA/drinkredbull/";
  public String Add_a_payment_to_an_invoice ="/Invoice.svc/json/Invoice/Payment/ioCodeFA/drinkredbull/";
  public String Sales_Invoice_TaxAfterDisc="/Invoice.svc/json/Sales/Invoice/TaxAfterDisc/ioCodeFA/drinkredbull/";
  //chage the invoice draf mode to active mode
  public String Sales_Invoice_Finalize="/Invoice.svc/json/Sales/Invoice/Finalize/ioCodeFA/drinkredbull/";//{LoginKey}/{SalesInvoiceId} ";
  public String Sales_Invoice_TaxShipping="/Invoice.svc/json/Sales/Invoice/TaxShipping/ioCodeFA/drinkredbull/";//{LoginKey}/{SalesInvoiceId}/{TaxShipping}";
  public String Sales_Invoice_SetTaxRate="/Invoice.svc/json/Sales/Invoice/SetTaxRate/ioCodeFA/drinkredbull/";//{LoginKey}/{SalesInvoiceId}/{TaxRate}"
  public String Sales_Invoice_SetShipping="/Invoice.svc/json/Sales/Invoice/SetShipping/ioCodeFA/drinkredbull/"; //{LoginKey}/{SalesInvoiceId}/{Shipping}";
  public String Sales_Invoice_Apply_DollarDiscount="/Invoice.svc/json/Sales/Invoice/ApplyDollarDiscount/ioCodeFA/drinkredbull/";//{LoginKey}/{SalesInvoiceId}/{Discount}"
  public String Sales_Invoice_ApplyPercentDiscount="/Invoice.svc/json/Sales/Invoice/ApplyPercentDiscount/ioCodeFA/drinkredbull/";//{LoginKey}/{SalesInvoiceId}/{Shipping}";
  //delivery item quantity

   public String invoice_Items_DeliveredQty="/Invoice.svc/json/Invoice/Items/DeliveredQty/ioCodeFA/drinkredbull/";///{LoginKey}/{InvoiceId}
  //delivery all items ,Delivers all undelivered items on an invoice
   public String  invoice_Deliver_All="/Invoice.svc/json/Invoice/Deliver/All/ioCodeFA/drinkredbull/";//{LoginKey}
   public String invoice_Payment_Propay="/Invoice.svc/json/Invoice/Payment/Propay/ioCodeFA/drinkredbull/";//{LoginKey}

  
  
  
   
   
   
   
   
   
 public  int mSelectedUserTag;

public int getmSelectedUserTag() {
	return mSelectedUserTag;
}


public void setmSelectedUserTag(int mSelectedUserTag) {
	this.mSelectedUserTag = mSelectedUserTag;
}
 
 
  public int mPersonNameFromContact;
 
  
 public int mPaymentsTab;

public int getmPaymentsTab() {
	return mPaymentsTab;
}


public void setmPaymentsTab(int mPaymentsTab) {
	this.mPaymentsTab = mPaymentsTab;
} 
  
  
  
  
	

}
