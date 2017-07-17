package userAction;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.PrepareInterceptor;

import sun.org.mozilla.javascript.internal.regexp.SubString;
import util.DBHelper;

public class action extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest request=ServletActionContext.getRequest();
	private String name;
	private String password;	
	private String token;
	private HttpServletResponse response =ServletActionContext.getResponse();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	//����ͨ��token��¼
	public void olderLoginByToken() throws SQLException, IOException{
			PrintWriter writer=response.getWriter();			
			System.out.println(token);
			String sql="select * from table_older where token = '"+token+"'";
			String json=getOlder(sql);
			json=json.substring(1, json.length()-1);
			System.out.println("aaaaaaaaaaaaaaaaaaa"+json);
			if(json!=null){
				writer.write("SUCCESS"); 
				writer.write("^"+token+"^"+json);				
			}			
			else
			{
				writer.write("FALSE");
			}
		
			writer.flush();
	}
	
	//�����˺������¼
	public String olderdologin() throws ServletException, IOException, SQLException, PushClientException, PushServerException
	{
		

		Object[] params={};
		DBHelper mydb=new DBHelper();
		PrintWriter writer=response.getWriter();
		String loginsql="select * from table_older where id="+name+" and password="+password;
		String json =getOlder(loginsql);
		json=json.substring(1, json.length()-1);
		System.out.println(json);
		if(json!=null){
			String token=request.getSession().getId();
			
			System.out.println(json);
			System.out.println(token);
			String sql="update table_older set token = '"+token+"' where id="+name;
			mydb.doPstm(sql, params);
			writer.write("SUCCESS");
			writer.write("^"+token+"^"+json);
		}		
		else{
			writer.write("FALSE");
		}
	
		writer.flush();
		
		
//		try{
//			
//		name=req.getParameter("name");
//		
//		password=req.getParameter("password");
//		
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		name="1";
//		password="1";
//		if(name.equals("123")&&password.equals("123")){
//		
//		return "SUCCESS";
//		}
//		else {
//			System.out.println("false");
//			return "FALSE";
//		}
	return null;
	}
	
	//ͨ��token��¼
	public void loginByToken() throws SQLException, IOException{
			PrintWriter writer=response.getWriter();			
			System.out.println(token);
			String sql="select * from table_nurse where token = '"+token+"'";
			String json="";
			Object[] params={};
			DBHelper mydb=new DBHelper();
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			if(rs!=null){
				while(rs.next()){
				json=json+"{'id':'"+rs.getString("id")+"',";
				json=json+"'name':'"+rs.getString("name")+"',";
				json=json+"'sex':'"+rs.getString("sex")+"',";
				json=json+"'age':'"+rs.getInt("age")+"',";
				json=json+"'image':'"+rs.getString("image")+"'}";	
				System.out.println(json);
				writer.write("SUCCESS"); 
				writer.write("^"+token+"^"+json);
				}
			}
			
			else
			{
				writer.write("FALSE");
			}
		
			writer.flush();
	}
	
	//�˺������¼
	public String dologin() throws ServletException, IOException, SQLException, PushClientException, PushServerException
	{
		

		Object[] params={};
		DBHelper mydb=new DBHelper();
		PrintWriter writer=response.getWriter();
		String loginsql="select * from table_nurse where id="+name+" and password="+password;
		mydb.doPstm(loginsql, params);
		ResultSet rs=mydb.getRs();
		String json="";
		if(rs!=null){
		 //����response����		
		 while(rs.next()){
			 System.out.println(rs.getString("name"));
		 json="[";
			
				json=json+"{'id':'"+rs.getInt("id")+"',";
				json=json+"'name':'"+rs.getString("name")+"',";
				json=json+"'sex':'"+rs.getString("sex")+"',";
				json=json+"'age':'"+rs.getString("age")+"',";	
				json=json+"'image':'"+rs.getString("image")+"'},";							
				System.out.println(rs.getInt("id"));	 
		}		    
		json=json.substring(0,json.length()-1);
		json=json+"]";		
		String token=request.getSession().getId();
		System.out.println(json);
		System.out.println(token);
		String sql="update table_nurse set token = '"+token+"' where id="+name;
		mydb.doPstm(sql, params);
		writer.write("SUCCESS");
		writer.write("^"+token+"^"+json);
		}
		else{
			return "FALSE";
		}
		 rs.close();
	
		writer.flush();
		
		
//		try{
//			
//		name=req.getParameter("name");
//		
//		password=req.getParameter("password");
//		
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		name="1";
//		password="1";
//		if(name.equals("123")&&password.equals("123")){
//		
//		return "SUCCESS";
//		}
//		else {
//			System.out.println("false");
//			return "FALSE";
//		}
	return null;
	}
	
	
	//sql->json->client
	public void SendList(){
	String sql="select * from table_older";	                                                                          
	Object[] params={};
	DBHelper mydb=new DBHelper();
	try
	{
		mydb.doPstm(sql, params);
		ResultSet rs=mydb.getRs();
		
		//�����ݿ��ѯ�õ�������ת��Ϊjson�ַ���
		String json="[";
		while(rs.next())
		{
			json=json+"{'id':'"+rs.getInt("id")+"',";
			json=json+"'name':'"+rs.getString("name")+"',";

			json=json+"'image':'"+rs.getString("image")+"'},";			
	    }
	json=json.substring(0,json.length()-1);
	json=json+"]";
	rs.close();
	System.out.println(json);
	PrintWriter writer=response.getWriter();
	writer.write(json);
	
	writer.flush();
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	}
	
	
	public void sendAllInfo() throws IOException{
			String sql="select * from table_older";	                                                                          
		                                                                     
			String json=null;
			json=getOlder(sql);
			
			System.out.println(json);
			PrintWriter writer=response.getWriter();
			writer.write(json);
			
			writer.flush();
			}
			
	
	
	
	public String getOlder(String sql){
		Object[] params={};
		Object[] params2={};
		Object[] params3={};
		Object[] params4={};
		DBHelper mydb=new DBHelper();
		String json=null;
		try
		{
			mydb.doPstm(sql, params);
			ResultSet rs=mydb.getRs();
			
			//�����ݿ��ѯ�õ�������ת��Ϊjson�ַ���
			 json="[";
			 String sql2;
			while(rs.next())
			{
				String id=rs.getString("id");
				sql2="select heart  from table_heart where olderid="+id;	   
				mydb.doPstm(sql2, params2);
				ResultSet rs2=mydb.getRs();
				
				json=json+"{'id':'"+id+"',";
				json=json+"'name':'"+rs.getString("name")+"',";
				json=json+"'image':'"+rs.getString("image")+"',";		
				json=json+"'heart':[";
				while(rs2.next()){
					json=json+"'"+rs2.getInt("heart")+"',";
				}
				if(rs2!=null){
					json=json.substring(0,json.length()-1);
				}
				json=json+"],";
				
				
				
				
				sql2="select blood from table_blood where olderid="+id;	  
				mydb.doPstm(sql2, params4);
				ResultSet rs4=mydb.getRs();
				json=json+"'blood':[";
				while(rs4.next()){
					json=json+"'"+rs4.getInt("blood")+"',";
				}
				if(rs4!=null){
					json=json.substring(0,json.length()-1);
				}
				json=json+"]},";
				rs2.close();
				
				rs4.close();
		    }
		json=json.substring(0,json.length()-1);
		json=json+"]";
		System.out.println(json);
		rs.close();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return json;
	}
		
	
	//��������
	public void sendMessage() throws PushClientException, PushServerException{
		sendSingleMessage("4115927218546716715","guangzhou");
	}
	
	public void sendSingleMessage(String ChannelId,String location) throws PushClientException, PushServerException{
		String apiKey="hOZgDzCrxGLMoUhIneQCjRj6";
		String secretKey="5GTQ5Tlkcvy6LbE3EKG4afmCOh3FvCfW";
		PushKeyPair pair = new PushKeyPair(apiKey,secretKey);
		BaiduPushClient pushClient = new BaiduPushClient(pair,
				BaiduPushConstants.CHANNEL_REST_URL);
		pushClient.setChannelLogHandler (new YunLogHandler () {
		    @Override
		    public void onHandle (YunLogEvent event) {
		        System.out.println(event.getMessage());
		    }
		}); 
		
	try{
	    //���������������������ʵ��
		PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
			    addChannelId(ChannelId).
			    addMsgExpires(new Integer(3600)).   //������Ϣ����Чʱ��,��λ��,Ĭ��3600 x 5.
			    addMessageType(1).                  //������Ϣ����,0��ʾ��Ϣ,1��ʾ֪ͨ,Ĭ��Ϊ0.
			    addMessage("{\"title\":\"����\",\"description\":\"γ��\"}").
			    addDeviceType(3);          //�����豸���ͣ�3 for android, 4 for ios.
	    //ִ��Http���󣬻�ȡ���ؽ��
		PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
	    //Http��������ӡ
		System.out.println("msgId: " + response.getMsgId()+ ",sendTime: " + response.getSendTime());
	} catch (PushClientException e) {
	    /*ERROROPTTYPE ���������쳣�Ĵ���ʽ -- �׳��쳣�Ͳ����쳣,
	    *'true' ��ʾ�׳�, 'false' ��ʾ����
	    */
	    if (BaiduPushConstants.ERROROPTTYPE) { 
	        throw e;
	    } else {
	        e.printStackTrace();
	    }
	} catch (PushServerException e) {
	    if (BaiduPushConstants.ERROROPTTYPE) {
	        throw e;
	    } else {
	        System.out.println(String.format(
	                "requestId: %d, errorCode: %d, errorMessage: %s",e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
	    }
	}
	}
}
