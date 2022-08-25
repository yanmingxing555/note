package com.fomp.note.springcloud;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
/**
 * 通过 RestTemplate，我们可以非常方便的进行 Rest API 调用。
 * 但是在 Spring 5 中已经不再建议使用 RestTemplate，而是建议使用 WebClient。
 * WebClient 是一个支持异步调用的 Client。
 */
/**
 * RestTemplate是Spring提供的用于访问Rest服务的客户端，是 Spring 框架提供的一个工具类，RestTemplate 是一个同步的 Rest API 客户端
 * RestTemplate提供了多种便捷访问远程Http服务的方法,能够大大提高客户端的编写效率。
 * 之前的HTTP开发是用apache的HttpClient开发，代码复杂，还得操心资源回收等。代码很复杂，冗余代码多。
 */
@Service
public class RestTemplateServiceTest {
    RestTemplate restTemplate = new RestTemplate();

    /**
     * RestTemplate大致可以分为三组：
     *  1、getForObject --- optionsForAllow 分为一组，这类方法是常规的 Rest API（GET、POST、DELETE 等）方法调用；
     *  2、exchange：接收一个 RequestEntity 参数，可以自己设置 HTTP method，URL，header 和 body，返回 ResponseEntity；
     *  3、execute：通过 callback 接口，可以对请求和返回做更加全面的自定义控制。
     */

    /**
     * 1.直接请求，无法设置Header中的头部信息
     */
    public void addGoods(){
        /**
         *  1.getForEntity方法的返回值是一个ResponseEntity<T>，ResponseEntity<T>是Spring对HTTP请求响应的封装，
         *   包括了几个重要的元素，如响应码、contentType、contentLength、响应消息体等,(即http的header和body)
         *  2.getForObject函数实际上是对getForEntity函数的进一步封装，如果你只关注返回的消息体的内容，
         *   对其他信息都不关注，此时可以使用getForObject
         */
        Goods goods = new Goods(2,"手机","手机",5000.0);
        String url = "http://localhost:9081/goods";
        //第一个参数：url,第二个参数：数据,第三个参数：返回值类型
        ResponseEntity<String> entity = restTemplate.postForEntity(url,goods,String.class);
        System.out.println("StatusCode===>"+entity.getStatusCode());
        System.out.println("StatusCodeValue===>"+entity.getStatusCodeValue());
        System.out.println("Headers===>"+entity.getHeaders());
        System.out.println("Body===>"+entity.getBody());

        String res = restTemplate.postForObject(url,goods,String.class);
        System.out.println("res===>"+res);
    }

    /**
     * 2.exchange：可以设置Header中的信息
     */
    public void withHeader(){
        Goods goods = new Goods(2,"手机","手机",5000.0);
        String url = "http://localhost:9081";
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(url).path("/goods").build(true);
        URI uri = uriComponents.toUri();
        RequestEntity<Object> requestEntity = RequestEntity.post(uri).
                header(HttpHeaders.COOKIE,"key1=value1").//添加cookie
                header("MyRequestHeader","MyValue").//添加header
                accept(MediaType.ALL).
                contentType(MediaType.APPLICATION_JSON).
                body(goods);
        ResponseEntity<String> entity = restTemplate.exchange(requestEntity,String.class);
        // 响应结果
        System.out.println("StatusCode===>"+entity.getStatusCode());
        System.out.println("StatusCodeValue===>"+entity.getStatusCodeValue());
        System.out.println("Headers===>"+entity.getHeaders());
        System.out.println("Body===>"+entity.getBody());
    }

    public static void main(String[] args) {
        RestTemplateServiceTest test = new RestTemplateServiceTest();
        test.addGoods();
        test.withHeader();
    }
    /**
     * postForLocation 方法的返回值是一个 Uri 对象，因为 POST 请求一般用来添加数据，
     * 有的时候需要将刚刚添加成功的数据的 URL 返回来，此时就可以使用这个方法，一个常见的使用场景如用户注册功能，
     * 用户注册成功之后，可能就自动跳转到登录页面了，此时就可以使用该方法
     *
     * 注意：postForLocation 方法返回的 Uri 实际上是指响应头的 Location 字段，
     *      所以，provider 中 register 接口的响应头必须要有 Location 字段（即请求的接口实际上是一个重定向的接口），
     *      否则 postForLocation 方法的返回值为null
     */
    public void location(){
        Goods goods = new Goods(2,"手机","手机",5000.0);
        String url = "http://localhost:9081";
        URI uri = restTemplate.postForLocation(url, goods);
        String s = restTemplate.getForObject(uri, String.class);
        System.out.println(s);
    }
}
