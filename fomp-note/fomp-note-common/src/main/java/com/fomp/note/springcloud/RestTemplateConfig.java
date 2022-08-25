package com.fomp.note.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate工具类，主要用来提供RestTemplate对象
 */
@Slf4j
@Configuration//加上这个注解作用，可以被Spring扫描
public class RestTemplateConfig {
    /**
     * 创建RestTemplate对象，将RestTemplate对象的生命周期的管理交给Spring
     */
    @Bean
    @LoadBalanced  //这个注解是让 RestTemplate 开启负载均衡的能力
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
       RestTemplate restTemplate = new RestTemplate(factory);//1.设置超时时间
        //2.设置中文乱码问题方式一
        //restTemplate.getMessageConverters().add(1,new StringHttpMessageConverter(Charset.forName("UTF-8")));
        // 设置中文乱码问题方式二
        // restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
        //3.设置拦截器
        MyInterceptor myInterceptor = new MyInterceptor();
        List<ClientHttpRequestInterceptor> list = new ArrayList<>();
        list.add(myInterceptor);
        restTemplate.setInterceptors(list);
        //3.ErrorHandler 用来对调用错误对统一处理。
        MyResponseErrorHandler errorHandler = new MyResponseErrorHandler();
        restTemplate.setErrorHandler(errorHandler);
        //4.HttpMessageConverter 配置：解码器
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // 通过下面代码可以添加新的 HttpMessageConverter(转换器)
        //messageConverters.add(new );
        return restTemplate;
    }

    /**
     * 超时策略
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //设置超时时间
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        // 设置代理
        //factory.setProxy(null);
        return factory;
    }
    /**
     * 拦截器
     */
    class MyInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            log.info("enter interceptor...");
            return execution.execute(request,body);
        }
    }

    /**
     * 忽略Https证书认证RestTemplate
     * @return unSSLRestTemplate
     *//*
    @Bean("unSSLRestTemplate")
    public RestTemplate unSSLRestTemplate() throws UnSSLRestTemplateCreateException {
        try{
            RestTemplate restTemplate = new RestTemplate(RestTemplateConfig.generateHttpRequestFactory());
            return restTemplate;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new UnSSLRestTemplateCreateException("unSSLRestTemplate bean创建失败，原因为：" + e.getMessage());
        }
    }
    *//**
     * 通过该工厂类创建的RestTemplate发送请求时，可忽略https证书认证
     * @return 工厂
     *//*
    private static HttpComponentsClientHttpRequestFactory generateHttpRequestFactory() throws Exception{
        TrustStrategy acceptingTrustStrategy = ((x509Certificates, authType) -> true);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        return factory;
    }*/
}
