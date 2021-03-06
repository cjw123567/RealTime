package com.foxlink.realtime.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.foxlink.realtime.DAO.AccountDAO;
import com.foxlink.realtime.model.Page;
import com.foxlink.realtime.model.QueryStatus;
import com.foxlink.realtime.model.User;

public class AccountService extends Service<User> {

	/***
     * 獲取所有賬號列表
     * 
     * @Author Yaru Song 
     * @return
    */
    private static Logger logger=Logger.getLogger(AccountService.class);  
		
	public AccountService() {
		super();
			// TODO Auto-generated constructor stub
	}

	private AccountDAO accountDAO;
	//accountDAO=(UserDAO) super.context.getBean("accountDAO");
	public void setAccountDAO(AccountDAO accountDAO ) {
	      this.accountDAO = accountDAO;
	      }
	
	@Override
	public  List<User> FindAllRecords(int currentPage,String queryCritirea,String queryParam){
		// TODO Auto-generated method stub
		List<User> AllUsers=null;
		try {
			int totalRecord = accountDAO.getTotalRecord(queryCritirea,queryParam);	     
			AllUsers = accountDAO.FindAllRecords(currentPage,totalRecord, queryCritirea, queryParam);					  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Find User Record is failed ",e);
		}
		return AllUsers;
	}
	
	 public Page getAccountPage(int pageNum, String queryCritirea,String queryParam) {
	      
	        int totalRecord = accountDAO.getTotalRecord( queryCritirea, queryParam);	      
	        Page page = new Page(pageNum, totalRecord);
	        //Page page = accountDAO.getPage(pageNum, User.class, totalRecord);
	        return page;
	    }

	@Override
	public boolean AddNewRecord(User newRecord) {
		// TODO Auto-generated method stub
		return accountDAO.AddNewRecord(newRecord);
	}

	@Override
	public boolean UpdateRecord(User updateRecord) {
		// TODO Auto-generated method stub
		return accountDAO.UpdateRecord(updateRecord);
	}

	@Override
	public boolean DeleteRecord(String recordID,String updateUser) {
		// TODO Auto-generated method stub
		return accountDAO.DeleteRecord(recordID,updateUser);
	}

	@Override
	public List<User> FindAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<User> FindRecord(User changePasswordUser) {
		// TODO Auto-generated method stub
		return accountDAO.FindRecords(changePasswordUser);
	}
	
	public boolean checkUserNameDuplicate(String userName) {
		// TODO Auto-generated method stub
		return accountDAO.checkUserNameDuplicate(userName);
	}

	


	
	public Map<String, Object> GetLoginInfo(String userName) {
		return accountDAO.GetLoginInfo(userName);
	}
	
	public String GetLoginRole(String userName) {
		List<String> roleList = accountDAO.GetLoginRole(userName);
		String Role = null;
		if(roleList != null){
			for (String string : roleList) {
				if(string.equals("ROLE_ADMIN")){
					Role = "ALL";
					break;
				}else if(string.equals("ROLE_TXADLOW")&&Role==null){
					Role = "TX";
				}else if(string.equals("ROLE_TXACCESSRECORD")&&Role==null){
					Role = "TX";
				}else if(string.equals("ROLE_ZJADLOW")&&Role==null){
					Role = "ZJ";
				}else if(string.equals("ROLE_LJADLOW")&&Role==null){
					Role = "LJ";
				}else if(string.equals("ROLE_VIC_LineLeader")&&Role==null){
					Role = "ROLE_VIC_LineLeader";
				}else if(string.equals("ROLE_VIC_ASSISTANT")&&Role==null){
					Role = "ROLE_VIC_ASSISTANT";
				}else if(string.equals("ROLE_VIC_ADMIN")&&Role==null){
					Role = "ROLE_VIC_ADMIN";
				}else if(string.equals("ROLE_VIC_ASSISTANT_O")&&Role==null){
					Role = "ROLE_VIC_ASSISTANT_O";
				}
			}
		}
		if(Role==null||Role.equals("")){
			Role="NOACCESS";
		}
		return Role;
	}
	
	public boolean UpdateAccountPassWord(User updateRecord) {
		// TODO Auto-generated method stub
		return accountDAO.UpdateAccountPassWord(updateRecord);
	}

	@Override
	public List<User> FindQueryRecord(String userDataCostId, int currentPage, User t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> FindQueryRecords(String userDataCostId, User t) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean DeletePowerRecord(String recordID,String role) {
		// TODO Auto-generated method stub
		return accountDAO.DeletePowerRecord(recordID,role);
	}

	public boolean NewPowerRecord(String userName, String role, String updateUser) {
		// TODO Auto-generated method stub
		return accountDAO.NewPowerRecord(userName,role,updateUser);
	}

	public boolean checkRole(String userName, String role) {
		// TODO Auto-generated method stub
		return accountDAO.checkRole(userName,role);
	}

}
