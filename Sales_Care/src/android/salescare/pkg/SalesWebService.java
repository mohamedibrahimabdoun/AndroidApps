package android.salescare.pkg;

public class SalesWebService {

	/******** get all MSISDNs by passing IMSI ********/
	
	/********   SIM Activation parameters *********/
	public  Object A_response = null;
	private String A_IMSI = null;
	private String A_SUBNO = null;
	private String A_PUK = null;
	
	/********   Data Registration parameters *********/
	public Object response;
	public String MSISDN;
	public String NAME;
	public String ADDRESS;
	public String SERAIL;
	public String IMSI;
	public String FrontImageName;
	public String BackImageName;
	public String idType;
	public byte[] FrontImage;
	public byte[] BackImage;
	
	/********   VTU Transfer parameters *********/
	
	public Object V_response = null;
	private String V_IMSI = null;
	private String MSGSTR = null;
	private String V_MSISDN = null;
	private String DATE = null;
	private String TOP = null;
	
	/********   Electricity parameters  *********/
	
	public Object E_response = null;
	private String E_IMSI = null;
	
}
