package com.foxlink.realtime.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foxlink.realtime.model.IOWorkShopPW;
import com.foxlink.realtime.model.Page;
import com.foxlink.realtime.service.IOSpecialWSEmpService;
import com.foxlink.realtime.service.IOWorkShopPowerService;
import com.foxlink.realtime.service.WorkshopNoRestService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/IOSpecialWSEmp")
public class IOSpecialWSEmpController {
	private static Logger logger = Logger.getLogger(IOSpecialWSEmpController.class);
	private IOSpecialWSEmpService iOSpecialWSEmpService;

	@RequestMapping(value="/ShowIOSpecialWSEmp",method=RequestMethod.GET)
	public String ShowWorkShopNoRestInfo(){
		return "IOSpecialWSEmp";
	}
	
	@RequestMapping(value="/ShowAllIOSpecialWSEmp",method=RequestMethod.POST,produces="application/json;charset=utf-8")
	@ResponseBody
	public String ShowAllIOSpecialWSEmp(HttpSession session,@RequestParam("curPage")String curPage,@RequestParam("queryCritirea")String queryCritirea,@RequestParam("queryParam")String queryParam){
		String DisableResult=null;
		try{
			int currentPage=1;
			if(curPage=="" || curPage==null)
			     currentPage=1;
			else
				currentPage=Integer.parseInt(curPage);;
			    if(queryParam=="" || queryParam==null)
			    	queryCritirea="";
			ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
			String updateUser=(String) session.getAttribute("username");
			String userDataCostId=(String) session.getAttribute("userDataCostId");
			String accessRole=(String) session.getAttribute("accessRole");
			iOSpecialWSEmpService = (IOSpecialWSEmpService) context.getBean("iOSpecialWSEmpService");
			Gson gson = new GsonBuilder().serializeNulls().create();
			Page page = iOSpecialWSEmpService.getworkshopNoRestPage(currentPage,queryCritirea, queryParam,updateUser,userDataCostId,accessRole);
			page.setList(iOSpecialWSEmpService.FindQueryRecord(updateUser, currentPage, queryCritirea,queryParam,userDataCostId,accessRole));
			DisableResult = gson.toJson(page);
		}catch(Exception ex){
			logger.error(ex);
			JsonObject exception=new JsonObject();
			exception.addProperty("StatusCode", "500");
			exception.addProperty("ErrorMessage", "取得保密車間進出權限列表失敗，原因："+ex.toString());
			DisableResult=exception.toString();
		}		
		System.out.println(DisableResult);
		return DisableResult;
	}
	
	@RequestMapping(value="/checkUserName.do",method=RequestMethod.POST,produces="Application/json;charset=utf-8")
	@ResponseBody 
	public String checkUserNameDuplicate(HttpSession session,@RequestParam("Emp_id")String Emp_id,@RequestParam("WorkShopNo")String WorkShopNo){
		JsonObject checkResult=new JsonObject();	
		String userDataCostId=(String) session.getAttribute("userDataCostId");
		String accessRole=(String) session.getAttribute("accessRole");
		try{
			ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
			iOSpecialWSEmpService = (IOSpecialWSEmpService) context.getBean("iOSpecialWSEmpService");
			if(iOSpecialWSEmpService.checkEmpIdExistence(Emp_id)) {
				if(iOSpecialWSEmpService.checkUserNameDuplicate(Emp_id,WorkShopNo,accessRole)){
					checkResult.addProperty("StatusCode", "200");
					checkResult.addProperty("Message", "此工號未設置臨時權限，可以新增此賬號!");
				}else{
					checkResult.addProperty("StatusCode", "500");
					checkResult.addProperty("Message","工號"+Emp_id+ "已設置車間"+WorkShopNo+"的臨時權限，請更改賬號！");
				}	
			}else {
				checkResult.addProperty("StatusCode", "500");
				checkResult.addProperty("Message", "無工號為:"+Emp_id+"的信息，請更改賬號！");
			}
			
		}
		catch(Exception ex){
			logger.error("Check new Account info is failed, due to: ",ex);
			checkResult.addProperty("StatusCode", "500");
			checkResult.addProperty("Message", "檢查工號是否置臨時權限，原因："+ex.toString());
		}
		/*System.out.println(checkResult.toString());*/
		return checkResult.toString();
	}
	//判斷同一卡號和車間是否有數據
			@RequestMapping(value="/checkCardId.do",method=RequestMethod.POST,produces="Application/json;charset=utf-8")
			@ResponseBody 
			public String checkCardIdDuplicate(HttpSession session,@RequestParam("CardId")String CardId,@RequestParam("WorkshopNo")String WorkshopNo){
				JsonObject checkResult=new JsonObject();	
				String userDataCostId=(String) session.getAttribute("userDataCostId");
				String accessRole=(String) session.getAttribute("accessRole");
				try{
					ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
					iOSpecialWSEmpService = (IOSpecialWSEmpService) context.getBean("iOSpecialWSEmpService");
					System.out.println("進入查詢同一卡號=======================>>>>>>>>>>");
						if(iOSpecialWSEmpService.checkCardIdDuplicate(CardId,WorkshopNo,accessRole)){
							checkResult.addProperty("StatusCode", "200");
							checkResult.addProperty("Message", "此卡號未設置臨時權限，可以新增此賬號!");
						}else{
							checkResult.addProperty("StatusCode", "500");
							checkResult.addProperty("Message","卡號"+CardId+ "已設置車間"+WorkshopNo+"的臨時權限，請更改賬號！");
						}	
				
					
				}
				catch(Exception ex){
					logger.error("Check new Account info is failed, due to: ",ex);
					checkResult.addProperty("StatusCode", "500");
					checkResult.addProperty("Message", "檢查卡號是否置臨時權限，原因："+ex.toString());
				}
				/*System.out.println(checkResult.toString());*/
				return checkResult.toString();
			}
	//設置員工保密車間臨時權限
	@RequestMapping(value="/AddIOSpecialWSEmp.do",method=RequestMethod.POST,produces="Application/json;charset=utf-8")
	@ResponseBody 
	public String ADDIOSpecialWSEmp(HttpSession session,@RequestBody IOWorkShopPW[] ioWorkShopPW){
		JsonObject AddResult=new JsonObject();	

		try{
			String updateUser=(String) session.getAttribute("username");
			String accessRole=(String) session.getAttribute("accessRole");
			ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
			iOSpecialWSEmpService = (IOSpecialWSEmpService) context.getBean("iOSpecialWSEmpService");
			if (iOSpecialWSEmpService.addIOSpecialWSEmp(ioWorkShopPW,updateUser,accessRole)) {
				AddResult.addProperty("StatusCode", "200");
				AddResult.addProperty("Message", "保密車間臨時權限設置成功");
			} else {
				AddResult.addProperty("StatusCode", "500");
				AddResult.addProperty("Message", "保密車間臨時權限設置失敗");
			}

		}
		catch(Exception ex){
			logger.error("Adding the new IOWsPw info is failed, due to: ",ex);
			AddResult.addProperty("StatusCode", "500");
			AddResult.addProperty("Message", "保密車間進出臨時權限設置發生錯誤，原因："+ex.toString());
		}
		System.out.println(AddResult.toString());
		return AddResult.toString();
	}
	//設置臺干和廠商保密車間臨時權限
		@RequestMapping(value="/AddIOSpecialWSEmpOther.do",method=RequestMethod.POST,produces="Application/json;charset=utf-8")
		@ResponseBody 
		public String ADDIOSpecialWSEmpOther(HttpSession session,@RequestBody IOWorkShopPW[] ioWorkShopPW){
			JsonObject AddResult=new JsonObject();	

			try{
				String updateUser=(String) session.getAttribute("username");
				String accessRole=(String) session.getAttribute("accessRole");
				ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
				iOSpecialWSEmpService = (IOSpecialWSEmpService) context.getBean("iOSpecialWSEmpService");
				if (iOSpecialWSEmpService.addIOSpecialWSEmpOther(ioWorkShopPW,updateUser,accessRole)) {
					AddResult.addProperty("StatusCode", "200");
					AddResult.addProperty("Message", "保密車間臨時權限設置成功");
				} else {
					AddResult.addProperty("StatusCode", "500");
					AddResult.addProperty("Message", "保密車間臨時權限設置失敗");
				}

			}
			catch(Exception ex){
				logger.error("Adding the new IOWsPw info is failed, due to: ",ex);
				AddResult.addProperty("StatusCode", "500");
				AddResult.addProperty("Message", "保密車間進出臨時權限設置發生錯誤，原因："+ex.toString());
			}
			System.out.println(AddResult.toString());
			return AddResult.toString();
		}
	@RequestMapping(value="/UpdateIOSpecialWSEmp.do",method=RequestMethod.POST,produces="Application/json;charset=utf-8")
	@ResponseBody
	public String UpdateIOSpecialWSEmp(HttpSession session,@RequestBody IOWorkShopPW ioWorkShopPW){
		JsonObject UpdateResult=new JsonObject();		
		try{
			ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
			iOSpecialWSEmpService = (IOSpecialWSEmpService) context.getBean("iOSpecialWSEmpService");
			String updateUser=(String) session.getAttribute("username");
			String accessRole=(String) session.getAttribute("accessRole");
			if(iOSpecialWSEmpService.UpdateRecord(ioWorkShopPW,updateUser,accessRole)){
				UpdateResult.addProperty("StatusCode", "200");
				UpdateResult.addProperty("Message", "更新保密車間臨時權限成功");
			}
			else{
				UpdateResult.addProperty("StatusCode", "500");
				UpdateResult.addProperty("Message", "更新保密車間臨時權限失敗");
			}
		}
		catch(Exception ex){
			logger.error("Updating the Account info is failed, due to: ",ex);
			UpdateResult.addProperty("StatusCode", "500");
			UpdateResult.addProperty("Message", "更新保密車間臨時權限發生錯誤，原因："+ex.toString());
		}
		return UpdateResult.toString();
	}
	
	@RequestMapping(value="/deleteIOSpecialWSEmp.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public String DeleteIOSpecialWSEmp(HttpSession session,@RequestParam("Emp_id")String Emp_id,@RequestParam("WorkShopNo")String WorkShopNo,@RequestParam("CardID")String CardID){
		JsonObject DisableResult=new JsonObject();
		try{
			ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
			iOSpecialWSEmpService = (IOSpecialWSEmpService) context.getBean("iOSpecialWSEmpService");
			String updateUser=(String) session.getAttribute("username");
			String accessRole=(String) session.getAttribute("accessRole");
			System.out.println("卡號========="+CardID);
			if(iOSpecialWSEmpService.DeleteIOWorkShopPW(Emp_id,WorkShopNo, updateUser,CardID,accessRole)){
				DisableResult.addProperty("StatusCode", "200");
				DisableResult.addProperty("Message", "保密車間進出臨時權限已失效");
			}
			else{
				DisableResult.addProperty("StatusCode", "500");
				DisableResult.addProperty("Message", "保密車間進出臨時權限發生錯誤");
			}
		}
		catch(Exception ex){
			logger.error("Disable the IOCardMaIP info is failed, due to:",ex);
			DisableResult.addProperty("StatusCode", "500");
			DisableResult.addProperty("Message", "保密車間進出臨時權限發生錯誤，原因:"+ex.toString());
		}		
		return DisableResult.toString();
	}
}
