package com.airtel.adtech.dao.onboard;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import com.airtel.helper.report.ReportHelper;
import com.airtel.teams.common.CommonApi;

public class OnboardDaoImpl extends CommonApi implements OnboardDao {

	@Override
	public int updateEmailVerified(Connection connection, String userUuid) {
		String updateEmailVerifiedQuery = "update TEAM_USER set IS_EMAIL_VERIFIED=1 where UUID='" + userUuid + "'";

		int result = updateDeleteDataFromDb(connection, updateEmailVerifiedQuery, false);

		if (result == -1) {
			ReportHelper.logValidationFailure("Couldn't get db connection", "DB connection", "error in db connection",
					"DB connection failure");
			Assert.assertTrue(false);
		}

		return result;
	}
	
	  public void deleteCompanyDataFromDb(Connection connection, String mobileNumber, String emailId)
	    {

	        String query="select COMPANY_ID from TEAM_USER where EMAIL='"+emailId+"' OR MOBILE='"+mobileNumber+"'";

	        List<Map<String,String>> queryData = fetchDatafromDb(connection, query);
	        List<String> companyIds=null;

	        if(queryData.size()!=0)
	        {
	            companyIds=new ArrayList<>();
	            for (Map<String,String> map:queryData) {
	                String companyId=map.get("COMPANY_ID");
	                if(companyId!=null)
	                {
	                    companyIds.add(companyId);
	                }
	            }

	            for (String companyId:
	                    companyIds) {

	                String bankid=null;
	                String bankidquery="select ID from TEAM_COMPANY_BANK_DETAIL where COMPANY_ID="+companyId;
	                List<Map<String,String>> bankqueryData = fetchDatafromDb(connection, bankidquery);
	                if(bankqueryData!=null && bankqueryData.size()!=0)
	                    bankid=bankqueryData.get(0).get("ID");

	                String erpcompanyid=null;
	                String erpcompanyidquery="select ID from ERP_COMPANY where COMPANY_ID="+companyId;
	                List<Map<String,String>> erpcompanyData = fetchDatafromDb(connection, erpcompanyidquery);
	                if(erpcompanyData!=null && erpcompanyData.size()!=0)
	                    erpcompanyid=erpcompanyData.get(0).get("ID");

	                String erpaddressid=null;
	                String erpaddressidquery="select ID from TEAM_COMPANY_ADDRESS where COMPANY_ID="+companyId;
	                List<Map<String,String>> erpaddressData = fetchDatafromDb(connection, erpaddressidquery);
	                if(erpaddressData!=null && erpaddressData.size()!=0)
	                    erpaddressid=erpaddressData.get(0).get("ID");


	                String ERPcompanysite1="delete from ERP_COMPANY_SITE where ADDRESS_ID="+erpaddressid;
	                String ERPcompanysiteloc="delete from ERP_COMPANY_SITE_LOCATION where ADDRESS_ID="+erpaddressid;

	                String companyAddressQuery="delete from TEAM_COMPANY_ADDRESS where COMPANY_ID="+companyId;
	                String companyKycQuery="delete from TEAM_COMPANY_KYC where COMPANY_ID="+companyId;
	                String pennydropQuery="delete from TEAM_PENNY_DROP_TRANSACTION where COMPANY_BANK_DETAIL_ID="+bankid;
	                String companyBankQuery="delete from TEAM_COMPANY_BANK_DETAIL where COMPANY_ID="+companyId;

	                String companyERPQuery="delete from TEAM_ERP_EVENT where COMPANY_ID="+companyId;
	                String companySubscribedQuery="delete from TEAM_COMPANY_SUBSCRIBED_SERVICES where COMPANY_ID="+companyId;

	                String ERPcompany="delete from ERP_COMPANY where COMPANY_ID="+companyId;
	                String ERPcompanysite="delete from ERP_COMPANY_SITE where ERP_COMPANY_ID= "+ erpcompanyid;

	                String companyQuery="delete from TEAM_COMPANY where ID="+companyId;

	                int result2=updateDeleteDataFromDb(connection,ERPcompanysite1,false);
	                int result3=updateDeleteDataFromDb(connection,ERPcompanysite,false);
	                int result4=updateDeleteDataFromDb(connection,ERPcompanysiteloc,false);
	                int result5=updateDeleteDataFromDb(connection,companyAddressQuery,false);
	                int result6=updateDeleteDataFromDb(connection,companyKycQuery,false);
	                int result7=updateDeleteDataFromDb(connection,pennydropQuery,false);
	                int result8=updateDeleteDataFromDb(connection,companyBankQuery,false);

	                int result9=updateDeleteDataFromDb(connection,ERPcompany,false);
	                int result10=updateDeleteDataFromDb(connection,companyERPQuery,false);
	                int result11=updateDeleteDataFromDb(connection,companySubscribedQuery,false);

	                int result1=updateDeleteDataFromDb(connection,companyQuery,false);


	                if(result1==-1 || result2==-1 || result3==-1 || result4==-1 || result5==-1 || result6==-1 || result7==-1 || result8==-1 || result9==-1 || result10==-1 || result11==-1)
	                {
	                    ReportHelper.logValidationFailure("Error while deleting company data", "DB connection","error in db connection", "error while deleting company data");
	                    Assert.assertTrue(false);
	                }
	            }
	        }
	    }

	@Override
	public boolean deleteFromAdvertiser(Connection connection, String mobile, String email) {
	      String queryInvitedBy="delete from ADVERTISER_DETAIL where USER_ID IN (select ID from TEAM_USER where EMAIL='"+email+"' OR MOBILE="+mobile+")";

	        
	        int result= updateDeleteDataFromDb(connection, queryInvitedBy, false);

	        if (result == -1 ) {
	            ReportHelper.logValidationFailure("error while deleting advertiser data", "DB connection", "error in db connection",
	                    "error while deleting invitee data");
	            Assert.assertTrue(false);
	            return false;
	        }

	        return true;

	    }

	@Override
	public boolean deleteFromUserIdentity(Connection connection, String mobile, String email) {
		String query = "delete from TEAM_USER_IDENTITY where (EMAIL_ID='" + email + "' or MOBILE_NUMBER='" + mobile
				+ "') and is_enabled=1";

		int result = updateDeleteDataFromDb(connection, query, false);

		if (result == -1) {
			ReportHelper.logValidationFailure("Couldn't get db connection", "DB connection", "error in db connection",
					"DB connection failure");
			Assert.assertTrue(false);
			return false;
		}

		return true;
	}

	@Override
	public boolean deleteFromUserProfile(Connection connection, String mobile, String email) {
		String query = "delete from TEAM_USER where (EMAIL='" + email + "' or MOBILE=" + mobile + ") and IS_ACTIVE=1";

		int result = updateDeleteDataFromDb(connection, query, false);

		if (result == -1) {
			ReportHelper.logValidationFailure("Couldn't get db connection", "DB connection", "error in db connection",
					"DB connection failure");
			Assert.assertTrue(false);
			return false;
		}

		return true;
	}
	

}
