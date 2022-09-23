/*
package com.fomp.note.util.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yss.htf.newecc.entity.FundNode;
import com.yss.htf.newecc.entity.RequestInfo;
import com.yss.htf.newecc.util.otherutil.FastJsonUtil;

public class TestRestfulClient {

	public static void main(String[] args) {
		try {
			System.out.println("**********��ʼ*********");
			//testDoGet();
			testDoPost();
			//testDoPut();
			//testDoDelete();
			
			
			
			
			*/
/*System.out.println(UUID.generate());
			System.out.println(com.wm.util.text.UUID.generate());*//*

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void testDoGet() throws Exception{
		String url = "http://10.10.20.137:8080/YssTaQs/fundeccrest/fundservice";
		Map<String, String> params = new HashMap<String, String>();
		params.put("FFUNDCODE", "000099");
		params.put("FFUNDSTATUS", "1");
		params.put("FFUNDTYPE", "2");
		params.put("FZDYFUNDTYPE", "3");
		HttpClientResult result = 
			HttpClientUtils.doGet(url, params);
		System.out.println(
				result.getCode()+"****"+
				result.getHeader()+"****"+
				result.getContent()
				);
	}
	public static  void testDoPost() throws Exception{
		String url = "http://10.10.20.137:8080/YssTaQs/tacsrest/accountrestcontroller/accountservice";
		ArrayList<FundNode> list = new ArrayList<FundNode>();
		RequestInfo commonRequest = new RequestInfo();
		FundNode rBean1= new FundNode();
		rBean1.setFundCode("000001");
		rBean1.setDesc("����1");
		FundNode rBean2= new FundNode();
		rBean2.setFundCode("000002");
		rBean2.setDesc("����2");
		list.add(rBean1);
		list.add(rBean2);
		commonRequest.setMsgId("1001");
		commonRequest.setFrom("ecc");
		commonRequest.setTo("yss");
		commonRequest.setTimestamp("123546");
		commonRequest.setData(FastJsonUtil.listToJson(list));
		//System.out.println(JsonUtils.object2JsonString(commonRequest));
		System.out.println(FastJsonUtil.getBeanToJson(commonRequest));
		//String reString = HttpClientUtil.sendPostInfo(url, JsonUtils.object2JsonString(commonRequest));
		HttpClientResult result = HttpClientUtils.doPost(url, JSONObject.parseObject(FastJsonUtil.getBeanToJson(commonRequest)));
		System.out.println(
				result.getCode()+"****"+
				result.getHeader()+"****"+
				result.getContent()
				);
	}

	public static  void testDoPut() throws Exception{
		String url = "http://10.10.20.137:8080/YssTaQs/fundeccrest/fundservice/detail/000001";
		ArrayList<FundNode> list = new ArrayList<FundNode>();
		RequestInfo commonRequest = new RequestInfo();
		FundNode rBean1= new FundNode();
		rBean1.setFundCode("000001");
		rBean1.setDesc("����1");
		*/
/*RequestInsertFundBean rBean2= new RequestInsertFundBean();
		rBean2.setFundcode("000002");
		rBean2.setFdesc("����2");*//*

		list.add(rBean1);
		//list.add(rBean2);
		commonRequest.setMsgId("10000001");
		commonRequest.setFrom("ecc");
		commonRequest.setTo("yss");
		commonRequest.setTimestamp("123546");
		commonRequest.setData(FastJsonUtil.listToJson(list));
		System.out.println(FastJsonUtil.getBeanToJson(commonRequest));
		HttpClientResult result = HttpClientUtils.doPut(url, JSONObject.parseObject(FastJsonUtil.getBeanToJson(commonRequest)));
		System.out.println(
				result.getCode()+"****"+
				result.getHeader()+"****"+
				result.getContent()
				);
	}

	public static  void testDoDelete() throws Exception{
		String url = "http://10.10.20.137:8080/YssTaQs/fundeccrest/fundservice/detail/000001";
		HttpClientResult result = HttpClientUtils.doDelete(url);
		System.out.println(
				result.getCode()+"****"+
				result.getHeader()+"****"+
				result.getContent()
				);

	}
	
	
	
	
}
*/
