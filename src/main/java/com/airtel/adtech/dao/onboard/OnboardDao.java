package com.airtel.adtech.dao.onboard;

import java.sql.Connection;

public interface OnboardDao {
	
	   public int updateEmailVerified(Connection connection,String userUuid);
	   public void deleteCompanyDataFromDb(Connection connection, String mobileNumber, String emailId);
	   boolean deleteFromAdvertiser(Connection connection, String mobile, String email);
	   public boolean deleteFromUserIdentity(Connection connection, String mobile, String email) ;
	   public boolean deleteFromUserProfile(Connection connection, String mobile, String email);
}
