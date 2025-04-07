package jewellery;

/**
 *
 * @author AR-LABS
 */
public class DatabaseCredentials {

    public static final String DB_ADDRESS = "jdbc:h2:./company/"+GLOBAL_VARS.SELECTED_COMPANY+"/"+GLOBAL_VARS.SELECTED_COMPANY_FYYEAR+"_db;DATABASE_TO_UPPER=false;IGNORECASE=TRUE";
    public static final String DB_USERNAME = "sa";
    public static final String DB_PASSWORD = "";
    
    public static final String RECEIPT_TABLE = "receipt";
    public static final String COMPANY_TABLE = "company";
    public static final String ACCOUNT_TABLE = "account";
    public static final String ACCOUNT_GROUP_TABLE = "accountgroup";
    public static final String DAILY_UPDATE_TABLE = "dailyupdate";
    public static final String ENTRY_ITEM_TABLE = "entryitem";
    public static final String ITEMS_LIST = "itemslist";
    public static final String LEDGER_TABLE = "ledger";
    public static final String OUTSTANDING_ANALYSIS_TABLE = "outstandinganalysis";
    public static final String PURCHASE_HISTORY_TABLE = "purchasehistory";
    public static final String PURCHASE_BILL_TABLE = "purchasebill";
    public static final String SALES_TABLE = "sales";
    public static final String SALES_BILL_TABLE = "salesbill";
    public static final String TAG_PRINTING_TABLE = "tagprinting";
    public static final String STATES_TABLE = "states";
    public static final String TAG_COUNTER_TABLE = "tagcounter";
    public static final String CASH_PURCHASE_DETAILS_TABLE = "cashpurchasedetails";
    public static final String BILL_NO_COUNTER_TABLE = "billnocounter";
    public static final String SALE_BILL_NO_COUNTER_TABLE = "salebillnocounter";
    public static final String PAYMENT_TABLE = "payments";
    
    public static final String EXCHANGE_TABLE = "exchange";
}
