package com.foxlink.realtime.controller;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.foxlink.realtime.model.LineNoByDepid;
import com.foxlink.realtime.model.Page;
import com.foxlink.realtime.model.WorkShopStatus;
import com.foxlink.realtime.service.AccessGoodsService;
import com.foxlink.realtime.service.LineNoByDepidService;
import com.foxlink.realtime.service.WorkShopStatusService;
import com.foxlink.realtime.util.NullStringToEmptyAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Controller
@RequestMapping("/LineNoByDepid")
public class LineNoByDepidController {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private LineNoByDepidService lineNoByDepidService;
	
	@RequestMapping(value="/ShowLineNoByDepid",method=RequestMethod.GET)
	public String ShowAccessGoodsInfo(){
		return "LineNoByDepid";
	}
	
	@RequestMapping(value="/LineNo.show",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	public @ResponseBody String ShowLineNo(@RequestParam("WorkShopNo")String workShopNo) {
		String jsonResults="";
		System.out.print(workShopNo);
		try{
			Gson gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
			ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
			lineNoByDepidService=(LineNoByDepidService) context.getBean("lineNoByDepidService");
			jsonResults=gson.toJson(lineNoByDepidService.FindLineNo(workShopNo));
		}catch(Exception ex){
			JsonObject exception=new JsonObject();
			exception.addProperty("StatusCode", "500");
			exception.addProperty("ErrorMessage", "取得LineNo失敗，原因："+ex.toString());
			jsonResults=exception.toString();
	}
		System.out.print(jsonResults);
		return jsonResults;
	}
	@RequestMapping(value="/checkWorkShopStatud.do",method=RequestMethod.POST,produces="Application/json;charset=utf-8")
	@ResponseBody 
	public String checkWorkShopCost(HttpSession session,@RequestParam("LineNo")String LineNo,@RequestParam("WorkShopNo")String WorkShopNo){
		JsonObject checkResult=new JsonObject();	
		
		try{
			if(lineNoByDepidService.checkWorkShopStatud(LineNo,WorkShopNo)){
				checkResult.addProperty("StatusCode", "200");
				checkResult.addProperty("Message", "此車間綫體綁定綫組別代碼已存在！");
			}
			else{
				checkResult.addProperty("StatusCode", "500");
				checkResult.addProperty("Message", "可以設置此車間綫體綁定綫組別代碼!");
			}
		}
		catch(Exception ex){
			logger.error("Check new WorkShopStatud info is failed, due to: ",ex);
			checkResult.addProperty("StatusCode", "500");
			checkResult.addProperty("Message", "檢查此車間綫體綁定綫組別代碼是否存在發生錯誤，原因："+ex.toString());
		}
		return checkResult.toString();
	}
	@RequestMapping(value="/AddLineNoByDepid.do",method=RequestMethod.POST,produces="Application/json;charset=utf-8")
	@ResponseBody 
	public String AddExceptionCost(HttpSession session,@RequestBody LineNoByDepid[] lineNoByDepid){
//		JsonObject AddResult=new JsonObject();		

			String updateUser=(String) session.getAttribute("username");
//			otCardbd.setUpdate_UserId(updateUser);			
			return lineNoByDepidService.addLineNoByDepid(lineNoByDepid, updateUser);
	}
	
	@RequestMapping(value = "/LineNoByDepid.show", method = RequestMethod.GET,produces="Application/json;charset=utf-8")
	public @ResponseBody String WorkShopStatusList(@RequestParam("curPage")String curPage,@RequestParam("queryCritirea")String queryCritirea,@RequestParam("queryParam")String queryParam){
		String jsonResults="";
		try{
			int currentPage=1;
			if(curPage=="" || curPage==null)
			     currentPage=1;
			else
				currentPage=Integer.parseInt(curPage);
			 if(queryParam=="" || queryParam==null)
			    	queryCritirea="";	
				Gson gson = new GsonBuilder().serializeNulls().create();
				
				Page page=lineNoByDepidService.getLineNoByDepidPage(currentPage,queryCritirea, queryParam);
				page.setList(lineNoByDepidService.FindAllRecords(currentPage,queryCritirea, queryParam));
				jsonResults=gson.toJson(page);
				System.out.println(queryCritirea+"-----"+queryParam);
				//System.out.println(jsonResults);
				//jsonResults=gson.toJson(workShopService.FindAllRecords());
			}catch(Exception ex){
				logger.error(ex);
				JsonObject exception=new JsonObject();
				exception.addProperty("StatusCode", "500");
				exception.addProperty("ErrorMessage", "取得車間綫體綁定綫組別代碼列表失敗，原因："+ex.toString());
				jsonResults=exception.toString();
		}
		
		return jsonResults;
	}
	
	@RequestMapping(value="/deleteLineNoByDepid.do",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	@ResponseBody
	public String DeletedeleteAccessGoods(HttpSession session,@RequestParam("WorkShopNo")String workShopNo
			,@RequestParam("LineNo")String lineNo,@RequestParam("Depid")String depid){
		JsonObject DisableResult=new JsonObject();
		try{
			String updateUser=(String) session.getAttribute("username");		
			if(lineNoByDepidService.DeleteLineNoByDepid(workShopNo,lineNo,depid,updateUser)){
				DisableResult.addProperty("StatusCode", "200");
				DisableResult.addProperty("Message", "車間綫體綁定綫組別代碼已失效");
			}
			else{
				DisableResult.addProperty("StatusCode", "500");
				DisableResult.addProperty("Message", "刪除車間綫體綁定綫組別代碼發生錯誤");
			}
		}
		catch(Exception ex){
			logger.error("Disable the LineNoByDepid info is failed, due to:",ex);
			DisableResult.addProperty("StatusCode", "500");
			DisableResult.addProperty("Message", "刪除車間綫體綁定綫組別代碼發生錯誤，原因:"+ex.toString());
		}		
		return DisableResult.toString();
	}
}
