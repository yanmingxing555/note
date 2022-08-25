package com.fomp.note.util.myutil.httpclientutil;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * RESTFUL客户端工具类：发送GET、POST、PUT、DELETE请求
 * @author YSS-YMX
 */
@Slf4j
public class HttpClientUtils {


	/*****************传参格式*******************/
	//请求头信息封装：Map<String, String> headers
	//doGet参数封装：Map<String, String> params 和 JSONObject params
	//其他方法参数封装：JSONObject params
	/*****************传参格式*******************/

	// 编码格式：统一用utf-8
	private static final String ENCODING = "utf-8";
	// 设置连接超时时间，单位毫秒。
	private static final int CONNECT_TIMEOUT = 15000;
	// 请求获取数据的超时时间(即响应时间)，单位毫秒。
	private static final int SOCKET_TIMEOUT = 15000;

	/**
	 * 发送get请求；无请求参数
	 */
	public static HttpClientResult doGet(String url) throws Exception{
		return doGet1(url, null, null);
	}
	/**
	 * 发送get请求；带请求参数封装到地址上
	 */
	public static HttpClientResult doGet(String url, Map<String, String> params) throws Exception {
		return doGet1(url, null, params);
	}
	/**
	 * 发送get请求；带请求头和请求参数封装到地址上
	 */
	public static HttpClientResult doGet1(String url,Map<String, String> headers, Map<String, String> params) throws Exception {
		CloseableHttpClient httpClient = null;// 创建httpClient对象
		CloseableHttpResponse httpResponse = null;//创建httpResponse对象
		HttpClientResult httpClientResult = null;//接收封装返回结果
		try {
			httpClient = HttpClients.createDefault();
			//创建访问的地址
			URIBuilder uriBuilder = new URIBuilder(url);
			if (params != null) {
				Set<Entry<String, String>> entrySet = params.entrySet();
				for (Entry<String, String> entry : entrySet) {
					uriBuilder.setParameter(entry.getKey(), entry.getValue());
				}
			}
			//创建http对象
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			setHttpHeader(httpGet);
			packageHeader(headers, httpGet);
			//执行请求并获得响应结果
			httpClientResult = getHttpClientResult(httpResponse, httpClient, httpGet);
			return httpClientResult;
		} catch (Exception e) {
			throw new Exception("发送GET请求信息出错："+e.getMessage());
		}finally {
			//释放资源
			release(httpResponse, httpClient);
		}
	}
	/**
	 * 发送get请求；带请求参数
	 */
	public static HttpClientResult doGet(String url, JSONObject params) throws Exception {
		return doGet2(url, null, params);
	}
	/**
	 * 发送get请求；带请求头和请求参数
	 */
	public static HttpClientResult doGet2(String url,Map<String, String> headers, JSONObject params) throws Exception {
		CloseableHttpClient httpClient = null;// 创建httpClient对象
		CloseableHttpResponse httpResponse = null;//创建httpResponse对象
		HttpClientResult httpClientResult = null;//接收封装返回结果
		try {
			httpClient = HttpClients.createDefault();
			//创建访问的地址
			URIBuilder uriBuilder = new URIBuilder(url);
			//创建http对象
			HttpGetWithBody httpGet = new HttpGetWithBody(uriBuilder.build());
			setHttpHeader(httpGet);
			packageHeader(headers, httpGet);
			packageParam(params, httpGet);
			//执行请求并获得响应结果
			httpClientResult = getHttpClientResult(httpResponse, httpClient, httpGet);
			return httpClientResult;
		} catch (Exception e) {
			throw new Exception("发送GET请求信息出错："+e.getMessage());
		}finally {
			//释放资源
			release(httpResponse, httpClient);
		}
	}

	/**
	 * 发送post请求；不带请求头和请求参数
	 * @param url 请求地址
	 */
	public static HttpClientResult doPost(String url) throws Exception {
		return doPost(url, null, null);
	}

	/**
	 * 发送post请求；带请求参数
	 * @param url 请求地址
	 * @param params 参数集合
	 */
	public static HttpClientResult doPost(String url, JSONObject params)
			throws Exception {
		return doPost(url, null, params);
	}

	/**
	 * 发送post请求；带请求头和请求参数
	 * @param url 请求地址
	 * @param headers 请求头集合
	 * @param params 请求参数集合
	 */
	public static HttpClientResult doPost(String url,Map<String, String> headers, JSONObject params) throws Exception {
		CloseableHttpClient httpClient = null;// 创建httpClient对象
		CloseableHttpResponse httpResponse = null;//创建httpResponse对象
		HttpClientResult httpClientResult = null;//接收封装返回结果
		try {
			httpClient = HttpClients.createDefault();
			//创建HttpPost对象
			HttpPost httpPost = new HttpPost(url);
			//设置请求头
			setHttpHeader(httpPost);
			packageHeader(headers, httpPost);
			//封装请求参数
			packageParam(params, httpPost);
			//执行请求并获得响应结果
			httpClientResult = getHttpClientResult(httpResponse, httpClient, httpPost);
			return httpClientResult;
		} catch (Exception e) {
			throw new Exception("发送POST请求信息出错："+e.getMessage());
		} finally {
			//释放资源
			release(httpResponse, httpClient);
		}
	}

	/**
	 * 发送put请求；不带请求参数
	 */
	public static HttpClientResult doPut(String url) throws Exception {
		return doPut(url,null, null);
	}

	/**
	 * 发送put请求；带参数
	 * @param url 请求地址
	 * @param params 参数集合
	 */
	public static HttpClientResult doPut(String url, JSONObject params)throws Exception {
		return doPut(url,null, params);
	}

	/**
	 * 发送put请求；带请求头和参数
	 * @param url 请求地址
	 * @param params 参数集合
	 */
	public static HttpClientResult doPut(String url,Map<String, String> headers, JSONObject params)throws Exception {
		CloseableHttpClient httpClient = null;// 创建httpClient对象
		CloseableHttpResponse httpResponse = null;//创建httpResponse对象
		HttpClientResult httpClientResult = null;//接收封装返回结果
		try {
			httpClient = HttpClients.createDefault();
			HttpPut httpPut = new HttpPut(url);
			setHttpHeader(httpPut);
			packageHeader(headers, httpPut);
			packageParam(params, httpPut);
			//执行请求并获得响应结果
			httpClientResult = getHttpClientResult(httpResponse, httpClient, httpPut);
			return httpClientResult;
		} catch (Exception e) {
			throw new Exception("发送PUT请求信息出错："+e.getMessage());
		}finally {
			//释放资源
			release(httpResponse, httpClient);
		}
	}

	/**
	 * 发送patch请求；不带请求参数
	 */
	public static HttpClientResult doPatch(String url) throws Exception {
		return doPatch(url,null, null);
	}
	/**
	 * 发送patch请求；带请求参数
	 */
	public static HttpClientResult doPatch(String url, JSONObject params) throws Exception {
		return doPatch(url,null, params);
	}

	/**
	 * 发送patch请求；带请求头和参数
	 * @param url 请求地址
	 * @param params 参数集合
	 */
	public static HttpClientResult doPatch(String url,Map<String, String> headers, JSONObject params) throws Exception {
		CloseableHttpClient httpClient = null;// 创建httpClient对象
		CloseableHttpResponse httpResponse = null;//创建httpResponse对象
		HttpClientResult httpClientResult = null;//接收封装返回结果
		try {
			httpClient = HttpClients.createDefault();
			HttpPatch httpPatch = new HttpPatch(url);
			setHttpHeader(httpPatch);
			packageHeader(headers, httpPatch);
			packageParam(params, httpPatch);
			//执行请求并获得响应结果
			httpClientResult = getHttpClientResult(httpResponse, httpClient, httpPatch);
			return httpClientResult;
		} catch (Exception e) {
			throw new Exception("发送PATCH请求信息出错："+e.getMessage());
		}finally {
			//释放资源
			release(httpResponse, httpClient);
		}
	}
	/**
	 * 发送delete请求；不带请求参数
	 */
	public static HttpClientResult doDelete(String url) throws Exception {
		CloseableHttpClient httpClient = null;// 创建httpClient对象
		CloseableHttpResponse httpResponse = null;//创建httpResponse对象
		HttpClientResult httpClientResult = null;//接收封装返回结果
		try {
			httpClient = HttpClients.createDefault();
			HttpDelete httpDelete = new HttpDelete(url);
			setHttpHeader(httpDelete);
			//执行请求并获得响应结果
			httpClientResult = getHttpClientResult(httpResponse, httpClient, httpDelete);
			return httpClientResult;
		} catch (Exception e) {
			throw new Exception("发送DELETE请求信息出错："+e.getMessage());
		}finally {
			//释放资源
			release(httpResponse, httpClient);
		}
	}
	/**
	 * 发送delete请求；带请求参数
	 */
	public static HttpClientResult doDelete(String url,JSONObject params) throws Exception {
		return doDelete(url,null,params);
	}
	/**
	 * 发送delete请求；带请求头和参数
	 */
	public static HttpClientResult doDelete(String url,Map<String, String> headers, JSONObject params)throws Exception {
		CloseableHttpClient httpClient = null;// 创建httpClient对象
		CloseableHttpResponse httpResponse = null;//创建httpResponse对象
		HttpClientResult httpClientResult = null;//接收封装返回结果
		try {
			httpClient = HttpClients.createDefault();
			HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
			setHttpHeader(httpDelete);
			packageHeader(headers, httpDelete);
			packageParam(params, httpDelete);
			//执行请求并获得响应结果
			httpClientResult = getHttpClientResult(httpResponse, httpClient, httpDelete);
			return httpClientResult;
		} catch (Exception e) {
			throw new Exception("发送DELETE请求信息出错："+e.getMessage());
		}finally {
			//释放资源
			release(httpResponse, httpClient);
		}
	}
	/**
	 * 封装个性化请求头
	 */
	private static void packageHeader(Map<String, String> params,HttpRequestBase httpMethod) throws Exception{
		//封装请求头
		try {
			if (params != null) {
				Set<Entry<String, String>> entrySet = params.entrySet();
				for (Entry<String, String> entry : entrySet) {
					// 设置到请求头到HttpRequestBase对象中
					httpMethod.setHeader(entry.getKey(), entry.getValue());
				}
			}
		} catch (RuntimeException e) {
			throw new Exception("封装个性化请求头信息出错"+e.getMessage());
		}
	}

	/**
	 *封装请求参数
	 */
	private static void packageParam(JSONObject params,HttpEntityEnclosingRequestBase httpMethod)throws Exception {
		// 封装请求参数
		/*if (params != null) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<Entry<String, String>> entrySet = params.entrySet();
			for (Entry<String, String> entry : entrySet) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			// 设置到请求的http对象中
			httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
		}*/
		try {
			if (params!=null) {
				StringEntity entity = new StringEntity(params.toString(), ENCODING);
				httpMethod.setEntity(entity);
			}
		} catch (Exception e) {
			throw new Exception("封装请求参数信息出错："+e.getMessage());
		}
	}

	/**
	 * 执行发送并获得响应结果
	 */
	private static HttpClientResult getHttpClientResult(CloseableHttpResponse httpResponse,
														CloseableHttpClient httpClient,
														HttpRequestBase httpMethod) throws Exception {
		try {
			//执行请求
			httpResponse = httpClient.execute(httpMethod);
			log.info("------发送报文正常完成-------");
			//获取返回结果
			if (httpResponse != null && httpResponse.getStatusLine() != null) {
				HttpEntity repEntity = httpResponse.getEntity();
				String content = "";
				String header = "";
				if (repEntity != null) {
					//获取请求头
					Header[] allHeaders = httpResponse.getAllHeaders();
					for (Header header2 : allHeaders) {
						/*if ("ApiVersion".equals(header2.getName())) {
						}*/
						header = header2.getName() + ":" + header2.getValue();
					}
					//读取报文体的内容
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(repEntity.getContent(),ENCODING));
					StringBuilder builder = new StringBuilder();
					String line = "";
					while ((line = bufferedReader.readLine()) != null) {
						builder.append(line);
					}
					EntityUtils.consume(repEntity);
					content = builder.toString();
					//content = EntityUtils.toString(httpResponse.getEntity(),ENCODING);
				}else{
					log.info("-----------------响应报文体为空------------------");
					throw new Exception("响应报文体为空");
				}
				return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content, header);
			}else{
				log.info("-----------------响应报文为空------------------");
				throw new Exception("响应报文为空");
			}
		} catch (Exception e) {
			throw new Exception("发送请求信息并接收响应结果出错："+e.getMessage());
		}
	}
	/**
	 * 设置超时配置信息
	 */
	private static RequestConfig getReqConfig(){
		/**
		 * setConnectTimeout：设置连接超时时间，单位毫秒。
		 * setConnectionRequestTimeout：设置从connect Manager(连接池)获取Connection超时时间，单位毫秒。
		 * setSocketTimeout：请求获取数据的超时时间(即响应时间)，单位毫秒。
		 * 如果访问一个接口，多少时间内无法返回数据，就直接放弃此次调用。
		 */
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		return requestConfig;
	}
	/**
	 * 设置超时信息和基本请求头设置
	 */
	private static void setHttpHeader(HttpRequestBase httpMethod){
		//设置配置信息
		httpMethod.setConfig(getReqConfig());
		//设置请求头
		httpMethod.setHeader("content-type", "application/json;charset=utf-8");
		httpMethod.setHeader("Accept", "application/json");
		/*httpPost.setHeader("Cookie", "");
		httpPost.setHeader("Connection", "keep-alive");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
		httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");*/
	}
	/**
	 * 释放资源
	 */
	private static void release(CloseableHttpResponse httpResponse,CloseableHttpClient httpClient) throws Exception {
		try {
			if (httpResponse != null) {
				httpResponse.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (Exception e) {
		}
	}
}