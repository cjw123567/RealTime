$(document).ready(function(){
	var curPage=1,queryCritirea=null,queryParam=null,isUserNameValid=false;
	var reg ="^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	var re = new RegExp(reg);
	ShowAllIOCardMaIPList();
	ShowWorkShop();
	
	 var CLICKTAG = 0;
     function button_onclick(pElement){
         if (CLICKTAG == 0) {  
             CLICKTAG = 1;  
             pElement.disabled=true;
             // 等待3s后重置按钮可用
             setTimeout(function () { CLICKTAG = 0 ; pElement.disabled=false;}, 2000);  
         }
     }
	
	$('#resetSubmit').click(function(){
	    $('#inputCardMachineIP').val('');
    	$('#message-text').val('');
	});
	
	$('#searchIOCardMaIP').click(function(){
		var queryCritirea=$('#queryCritirea option:selected').val();
		var queryParam=$('#queryParam').val();
		if(queryParam==""){
			ShowAllIOCardMaIPList();
		}else{
			getPersonList(curPage,queryCritirea,queryParam)	
		}
	});
	
	$('#setSecrecyWS').click(function(){
		var secrecyWS = $('#workShopSecrecy option:selected').val();
		var status = $('#secrecyStatus option:selected').val();
		if(secrecyWS==""||secrecyWS=="null"){
			alert("車間號不能爲空！")
		}else{
			setWorkShopSecrecy(secrecyWS,status);
		}
	})
	
	$('.reset').on('click',()=>{
		$('#deleteId .dlTable').find('tr').remove();
	})
	
	$('.deleteIp').on('click',()=>{
		var size = $('#deleteId .dlTable').children().length;
		if($('#deleteId .dlTable').children().length==0){
			alert("無數據可刪除!");
		}else{
			var relist =[];
			$('#deleteId .dlTable').find('tr').each(function(i,e){
				//				console.log(i);
								var dltr = {};
								var child =$(this).children();
								dltr.Deviceip = child.eq(0).text();
								dltr.WorkShopNo = child.eq(1).text();
								relist.push(dltr);
			})
			console.log(relist);
			var results=confirm("確定刪除表格内的"+size+"條綁定訊息 ?");
			if(results==true){
				$.ajax({
					type:'POST',
					contentType: "application/json",
					url:'../IOCardBdIP/deleteIOCardMaIP.do',
					data:JSON.stringify(relist),
					dataType:'json',
					error:function(e){
						alert(e);
					},
					success:function(data){
						 if(data!=null && data!=''){
							 if(data.StatusCode=="200"){
								 alert(data.Message);
								 /*
								var parentElement=$(this).parent().parent();
								//刪除，所以將此列從畫面移除
								parentElement.remove();
								  */
								 ShowAllIOCardMaIPList();
								 $('#deleteId .dlTable').empty();
							 }
							 else{
								 alert(data.Message);
							 }
						 }else{
							 alert('操作失敗!')
						 }
					}
				});
			}
		}
	})
	
	$('#setIOCardMaIP').click(function(){
		button_onclick($('#setIOCardMaIP')[0]);
		var machine={},errorMessage='';
		machine["Deviceip"]=$('#inputCardMachineIP').val();
		machine["WorkShopNo"]=$('#workShop option:selected').val();
		machine["WorkShop_Desc"]=$('#message-text').val();
		machine["Direction"]=$('#machineState option:selected').val();
		console.log(machine);
		
		if(machine["Deviceip"]==="null" || machine["Deviceip"]=='')
			errorMessage+='工號未填寫\n';
		if(!re.test(machine["Deviceip"])){
			errorMessage+='卡机IP不符合规范\n';
		}		
		/*if(machine["WorkShop_Desc"]=='' || machine["WorkShop_Desc"]==null){
			errorMessage+='未填寫卡機描述 \n';
		}*/
		if(machine["WorkShopNo"]==="null" || machine["WorkShopNo"]=='')
			errorMessage+='未選擇使用的車間\n';
		
		if(machine["Direction"]==="null" || machine["Direction"]=='')
			errorMessage+='未選擇卡機狀態\n';
		
		checkDeviceipDuplicate(machine["Deviceip"],machine["WorkShopNo"]);
		
		if(errorMessage==''&&isUserNameValid){
			//新增綁定賬號
			$.ajax({
				type:'POST',
				contentType: "application/json",
				url:'../IOCardBdIP/AddIOCardMaIP.do',
				data:JSON.stringify(machine),
				dataType:'json',
				success:function(data){
					$('#setIOCardMaIP').prop("disabled",false);
					 if(data!=null && data!=''){
						 if(data.StatusCode=="200"){
							 $('#inputCardMachineIP').val('');
							 $('#message-text').val('');
							 alert(data.Message);
							 ShowAllIOCardMaIPList();
							/* alert(data.Message);			
							 $('#inputUserName').val('');
							 $('#inputChineseName').val('');
							 $('#inputDepID').val('');
							 $('#inputCostID').val(null);
							 $("#inputAssistantId").val('');
							 $('#inputPhoneTel').val('');
							 $('#inputRole').val('');
							 $('#insertAccountDialog').modal('hide');
							 ShowAllAccountList();*/						 
						 }
						 else{
							 alert(data.Message);
						 }
					 }else{
						 alert('卡機IP狀態設置失敗!');
					 }
				},
				error:function(e){
					alert('卡機IP狀態設置發生錯誤');
				}
			});
		}
	    else{
	    	if(errorMessage.length>0 ||errorMessage!='' ){
		    alert(errorMessage);		
			event.preventDefault(); //preventDefault() 方法阻止元素发生默认的行为（例如，当点击提交按钮时阻止对表单的提交）。
	    	}
	    }	
	})
	
	function ShowAllIOCardMaIPList(){
		$.ajax({
			type:'POST',
			url:'../IOCardBdIP/ShowIOCardMachineList',
			data:{curPage:curPage,queryCritirea:queryCritirea,queryParam:queryParam},
			error:function(e){
//				alert('找不到資料');
				alert(0);
			},
			success:function(rawData){	
				if (rawData != null && rawData != "") {
					var executeResult=rawData["list"];
					var errorResponse=executeResult.ErrorMessage;
					if(errorResponse!=null){
						alert('找不到資料');
					}
					else{
						var numOfRecords=executeResult.length;
						if(numOfRecords>0)	
							ShowAllIOCardMaIPListTable(rawData);
						else{
							/*$('.left').css('height','727px');*/
							alert('暫無卡機信息資料');
						}
					}
				}
			}
		});	
	}
	
	function ShowAllIOCardMaIPListTable(rawData){
		$('#IOCardMaIPTable tbody').empty();
		var currentPage=rawData.currentPage;
		var totalRecord=rawData.totalRecord;
		var totalPage=rawData.totalPage;
		var pageSize=rawData.pageSize;
		var executeResult=rawData["list"];
		for(var i=0;i<executeResult.length;i++){
			var	tableContents='<tr><td class="touch">'+executeResult[i]["Deviceip"]+'</td>'+
					'<td>'+executeResult[i]["WorkShopNo"]+'</td>'+
					'<td style="white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">'+executeResult[i]["WorkShop_Desc"]+'</td>'
					if(executeResult[i]["Direction"]=="I"){
						tableContents+='<td>進</td>'
					}else if(executeResult[i]["Direction"]=="O"){
						tableContents+='<td>出</td>'
					}
//					'<td>'+executeResult[i]["Direction"]+'</td>'
//					'<td>'++'</td>'+
					var iS_SPECIAL =executeResult[i].IS_SPECIAL=="Y"?'保密車間':'非保密車間';
					if(iS_SPECIAL=="保密車間"){
						tableContents+='<td style="color:red;">'+iS_SPECIAL+'</td>';
					}else{
						tableContents+='<td>'+iS_SPECIAL+'</td>';
					}
					tableContents+='<td><input type="button" value="編輯" class="editBtn btn btn-xs btn-link"></td>';
				tableContents+='</tr>';
					/*tableContents+='<td><input type="button" value="編輯" class="editBtn btn btn-xs btn-link">';*/
					$('#IOCardMaIPTable tbody').append(tableContents);
		}
		refreshUserInfoPagination(currentPage,totalRecord,totalPage,pageSize);
	
		$('.touch').click(function(){	
			$('.cancelBtn').click();
			var a = $(this).text();
			var b = $(this).next().text();
	//		console.log(a,b);
			var list =[];
			if($('#deleteId .dlTable').children().length==0){
				$('#deleteId .dlTable').append('<tr><td>'+a+'</td><td>'+b+'</td></tr>');
			}else{
				$('#deleteId .dlTable').find('tr').each(function(i,e){
	//				console.log(i);
					var dltr = {};
					var child =$(this).children();
					dltr.c = child.eq(0).text();
					dltr.d = child.eq(1).text();
					list.push(dltr);
	//				console.log(list);
					
				})
				var count=0;
				for(var i=0;i<list.length;i++){
					if((list[i].c==a)&&(list[i].d==b)){
						count++;
					}
				}
				if(count==0){
					$('#deleteId .dlTable').append('<tr><td>'+a+'</td><td>'+b+'</td></tr>')
				}
			}
			$('#deleteId .dlTable').find('tr').each(function(i,e){
				$(this).click(function(){
					$(e).remove();
				})
			})
		})
		
		$(".editBtn").click(function(){
			var parentElement = $(this).parent().parent();
			var WorkShopNo=$(parentElement).find('td').eq(1).text();
			$(parentElement).find('td').eq(1).html('<select class="changeWorkShopNo input-small"></select>');
			
			ShowWorkShopNo('changeWorkShopNo');
			
			$(parentElement).find('td .changeWorkShopNo option').each(function(){
				if($(this).val()==WorkShopNo){
					$(this).prop('selected',true);
				}
			});
			
			var WorkShop_Desc=$(parentElement).find('td').eq(2).text();
			$(parentElement).find('td').eq(2).html('<input type="text" class="changeWorkShop_Desc input-small" maxlength="60" value="'+WorkShop_Desc+'">');
//			$(parentElement).find('td').eq(2).html('<textarea class="input-small changeWorkShop_Desc" id="message-text" value="'+WorkShop_Desc+'"></textarea>');
//			
			var Direction=$(parentElement).find('td').eq(3).text();
			$(parentElement).find('td').eq(3).html('<select id="machineState" name="changeStatus" class="input-small changeStatus"><option value="I">進</option><option value="O">出</option></select> ');
			$(parentElement).find('td .changeStatus option').each(function(){
				if($(this).text()==Direction){
					$(this).prop('selected',true);
				}
			});
			
//			$(parentElement).children().find('.editBtn .deleteBtn').hide();
			$(parentElement).find('td').eq(5).append('<a class="confirmBtn btn btn-xs btn-link" role="button">確認</a>'+
	        		'<a class="cancelBtn btn btn-xs btn-link" role="button">取消</a>');
			$(parentElement).find('.editBtn,.deleteBtn').hide();
			
			$('.confirmBtn').click(function(){
				var parentElement=$(this).parent().parent();
				var User=new Object(),errorMessage='';
				var Direction=$(parentElement).find('.changeStatus option:selected').eq(0).text();
				User.Deviceip=$(parentElement).find('td').eq(0).text();
				User.WorkShopNo=$(parentElement).find('option:selected').eq(0).val();
				User.WorkShop_Desc=$(parentElement).find('td input:text').eq(0).val();
				User.Direction=$(parentElement).find('.changeStatus option:selected').eq(0).val();
					
				if(User.WorkShopNo==="null" || User.WorkShopNo=='')
					errorMessage+='車間號未填寫\n';
				if(User.Direction==="null" || User.Direction=='')
					errorMessage+='卡機狀態未填寫\n';
				
				console.log(User);
				if(errorMessage==''){	
					$.ajax({
						type:'POST',
						contentType: "application/json",
						url:'../IOCardBdIP/UpdateIOCardMaIP.do',
						data:JSON.stringify(User),
						dataType:'json',
						error:function(e){
							alert(e);
							},
						success:function(data){
							  if(data!=null && data!=''){
								  if(data.StatusCode=="200"){
									  alert(data.Message);
									  $(parentElement).find('.editBtn').show();
									  $(parentElement).find('td').eq(0).html(User.Deviceip);
									  $(parentElement).find('td').eq(1).html(User.WorkShopNo);
									  $(parentElement).find('td').eq(2).html(User.WorkShop_Desc);
									  $(parentElement).find('td').eq(3).html(Direction);
//									  $(parentElement).find('td').eq(6).html(User.ROLE);
									  $(parentElement).find('.confirmBtn,.cancelBtn').remove();
								  }
								  else{
									  alert(data.Message);
								  }
							  }else{
								  alert('操作失敗！')
							  }
							}
							});
					}
				  else{
				    	if(errorMessage.length>0 ||errorMessage!='' ){
					    alert(errorMessage);		
						event.preventDefault(); //preventDefault() 方法阻止元素发生默认的行为（例如，当点击提交按钮时阻止对表单的提交）。
					}
				  }
				});
			
			$('.cancelBtn').click(function(){
				var parentElement=$(this).parent().parent();
				$(parentElement).find('.editBtn').show();
				$(parentElement).find('td').eq(1).html(WorkShopNo);
				$(parentElement).find('td').eq(2).html(WorkShop_Desc);
				$(parentElement).find('td').eq(3).html(Direction);
				$(this).parent().find('.confirmBtn,.cancelBtn').remove();
			})					
		})
		
		/*$('.deleteBtn').click(function(){
			var parentElement=$(this).parent().parent();
			var deleteDeviceip=$(parentElement).find('td').eq(0).text();
			var results=confirm("確定刪除卡機IP為 "+deleteDeviceip+" 的狀態 ?");
			if(results==true){
				$.ajax({
					type:'GET',
					url:'../IOCardBdIP/deleteIOCardMaIP.do',
					data:{Deviceip:deleteDeviceip},
					error:function(e){
						alert(e);
					},
					success:function(data){
						 if(data!=null && data!=''){
							 if(data.StatusCode=="200"){
								 alert(data.Message);
								 
								var parentElement=$(this).parent().parent();
								//刪除，所以將此列從畫面移除
								parentElement.remove();
								  
								 ShowAllIOCardMaIPList();
							 }
							 else{
								 alert(data.Message);
							 }
						 }else{
							 alert('操作失敗!')
						 }
					}
				});
			}
		});*/
	}
	
	
	
	function refreshUserInfoPagination(currentPage,totalRecord,totalPage,pageSize){
		$('#IOCardMaIPListPagination').empty();
		var paginationElement='頁次：'+currentPage+'/'+totalPage +'&nbsp;每页:&nbsp;'+pageSize+'&nbsp;共&nbsp;'+totalRecord+'&nbsp;條&nbsp;';
		if(currentPage==1)
			paginationElement+='<a href ="javascript:return false;">首页</a>';		  
		else
			paginationElement+='<a class="firstPage">首页</a>';

		if(currentPage>1)
			paginationElement+='&nbsp;<a class="previousPage">上一頁</a>';
		else
			paginationElement+='&nbsp;<a href ="javascript:return false;">上一頁</a>';
		
	   /* for(var i=1;i <= totalPage;i++){
	    	paginationElement+='&nbsp;<a class="numPage">' + i +'</a>&nbsp;';	    	
	    }*/
		if(currentPage<totalPage)
			paginationElement+='<a class="nextPage">下一頁</a>';
		else
			paginationElement+='<a href ="javascript:return false;">下一頁</a>';
		
		$('#IOCardMaIPListPagination').append(paginationElement);
		
		$('.firstPage').click(function(){
			curPage=1;
			if($('#queryParam').val()!=null){
				var text =$('#queryParam').val();
				var select = $('#queryCritirea option:selected').val();
				getPersonList(curPage,select,text);
			}else{
				getPersonList(curPage,queryCritirea,queryParam);				
			}	
		});
		
		$('.previousPage').click(function(){
			curPage=currentPage-1;
			if($('#queryParam').val()!=null){
				var text =$('#queryParam').val();
				var select = $('#queryCritirea option:selected').val();
				getPersonList(curPage,select,text);
			}else{
				getPersonList(curPage,queryCritirea,queryParam);				
			}		
		});	
		
		$('.nextPage').click(function(){
			curPage=currentPage+1;
			if($('#queryParam').val()!=null){
				var text =$('#queryParam').val();
				var select = $('#queryCritirea option:selected').val();
				getPersonList(curPage,select,text);
			}else{
				getPersonList(curPage,queryCritirea,queryParam);				
			}				
		});	
		
		$('.numPage').click(function(){
			var curPage=$(this).text();
			if($('#queryParam').val()!=null){
				var text =$('#queryParam').val();
				var select = $('#queryCritirea option:selected').val();
				getPersonList(curPage,select,text);
			}else{
				getPersonList(curPage,queryCritirea,queryParam);				
			}		
    	});
		
	}
	
	function getPersonList(curPage,queryCritirea,queryParam){
		$.ajax({
			type:'POST',
			url:'../IOCardBdIP/ShowIOCardMachineList',
			data:{curPage:curPage,queryCritirea:queryCritirea,queryParam:queryParam},
			error:function(e){
				alert('找不到資料');
			},
			success:function(rawData){	
				if (rawData != null && rawData != "") {
					var executeResult=rawData["list"];
					var errorResponse=executeResult.ErrorMessage;
					if(errorResponse!=null){
						alert('找不到資料');
					}
					else{
						var numOfRecords=executeResult.length;
						if(numOfRecords>0){
							ShowAllIOCardMaIPListTable(rawData);
							//$('#queryParam').val('');
						}
						else{
						/*	var currentPage=rawData.currentPage;
							console.log();
							var totalRecord=rawData.totalRecord;
							console.log(totalRecord);
							var totalPage=rawData.totalPage;
							var pageSize=rawData.pageSize;
							$('#FLinePersonMtY tbody').empty()
							$('.left').css('height','727px');
							refreshUserInfoPaginationY(currentPage,totalRecord,totalPage,pageSize);*/
							alert('找不到資料');
						}	
					}
				}
			}
		});
	}
	
	//顯示所有車間
	function ShowWorkShop(){
		$.ajax({
			type:'GET',
			url:'../Utils/WorkshopNo.show',
			data:{},
			async:false,
			success:function(data){
				var htmlAppender='';
			 if(data!=null && data!=''){	
				if(data.length>0 && data.StatusCode == null){
					for(var i=0;i<data.length;i++){
						htmlAppender+='<option value="'+data[i]+'">'+data[i]+'</option>';
					}
					 $('#workShop').append(htmlAppender);
					 $('#workShopSecrecy').append(htmlAppender);
				/*	 $('#ChangeWorkShop').append(htmlAppender);*/
				}
				else{
					alert('無車間資料');
				}
			 }else{
				alert('無車間資料');
			 }
			}
		});   
	}	
	
	function ShowWorkShopNo(selectClass){
		$.ajax({
			type:'GET',
			url:'../Utils/WorkshopNo.show',
			data:{},
			async:false,
			success:function(data){
				var htmlAppender='';
			 if(data!=null && data!=''){	
				if(data.length>0 && data.StatusCode == null){
					for(var i=0;i<data.length;i++){
						htmlAppender+='<option value="'+data[i]+'">'+data[i]+'</option>';
					}
					 $('.'+selectClass).append(htmlAppender);
				/*	 $('#ChangeWorkShop').append(htmlAppender);*/
				}
				else{
					alert('無車間資料');
				}
			 }else{
				alert('無車間資料');
			 }
			}
		});   
	}	
	
	 function checkDeviceipDuplicate(Deviceip,WorkShopNo){
			if(Deviceip!=""){
				$.ajax({
					type:'POST',
					url:'../IOCardBdIP/checkDeviceip.do',
					data:{
						Deviceip:Deviceip,
						WorkShopNo:WorkShopNo
					},
					async:false,
					error:function(e){
						alert(e);
					},
					success:function(data){	
						 if(data!=null && data!=''){
							 if(data.StatusCode==500){
								/* if(Deviceip)*/
								 alert(data.Message);
								 isUserNameValid=false;
							 }
							 else
								{
								 isUserNameValid=true;
								}
					}else{
						 isUserNameValid=false;
						}
					}
				});
			}
		}
	 
	 //設置保密車間ajax請求
	 function setWorkShopSecrecy(secrecyWS,status){
		 $.ajax({
			 type:'POST',
				url:'../IOCardBdIP/setSecrecyWS.do',
				data:{
					SecrecyWS:secrecyWS,
					Status:status
				},
				async:false,
				error:function(e){
					alert(e);
				},
				success:function(data){	
					 if(data!=null && data!=''){
						 if(data.StatusCode=="200"){
							 ShowAllIOCardMaIPList();
							 alert(data.Message);
						/*	 $('#IOCardMaIPTable tbody').empty();*/
							 $('#workShopSecrecy').val('');
							 $('#secrecyStatus').val('');
						 }
						 else
							{
							  alert(data.Message);
							}
					}else{
						  alert("操作失敗！");
					}
				}
		 })
	 }
})