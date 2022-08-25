package com.gateway.gateway;

/**
 * Spring Cloud Gateway 是 Spring 官方基于 Spring 5.0，Spring Boot 2.0 和 Project Reactor 等技术开发的网关，
 * 旨在为微服务架构提供一种简单而有效的统一的 API 路由管理方式，统一访问接口。
 *
 * Spring Cloud Gateway是Spring Cloud官方推出的第二代网关框架，取代Zuul 网关。
 * 网关作为流量的，在微服务系统中有着非常作用。据说性能是第一代网关 zuul的1.5倍。(基于Netty,WebFlux)
 * 注意点:由于不是Servlet容器，所以他不能打成war包, 只支持SpringBoot2.X不 支持1.x
 * 网关作用: 网关常见的功能有路由转发、权限校验、限流控制等作用。
 */
/**
 * Gateway核心概念
 *
 * 1.路由（Route）： 路由是网关的基本组成部分，路由信息由ID、目标URL、一组断言和一组过滤器组成，如果断言为真，则说明请求的URL和配置匹配。
 * 2.断言（Predicate）： Java8中的断言函数，Spring Cloud Gateway中的断言函数输入类型是Spring5.0框架中的ServerWebExchange。
 *                  Spring Cloud Gateway中的断言函数允许开发者自定义匹配来自于Http Request中的任何信息，比如请求头和参数等
 * 3.过滤器（Flute）： 一个标准的SpringWebFilter，Spring Cloud Gateway中的Filter分为两种类型，
 *                  分别是Gateway Filter和GlobalFilter，过滤器将会对请求和响应进行处理。
 */

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义全局过滤器：此种方式无需yml中配置
 */
@Component
public class MyDefineGatewayGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 全局过滤器：
     *   方式一：实现 GlobalFilter 接口
     *      不需要在配置文件中配置，作用在所有的路由上。
     *   方式二：配置spring.cloud.gateway.default-filters
     *      如果配置spring.cloud.gateway.default-filters 上会对所有路由生效也算是全局的过滤器，
     *      但是这些过滤器的实现上和局部过滤器是一样的，都是要实现GatewayFilterFactory接口。
     */

    private final int ORDERED = 1;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //例如，在编写的全局过滤器中，检查请求中是否携带token请求头。如果token请求头存在则放行；
        //如果token为空或者不存在则设置返回的状态码为401，即未授权，也不再执行下去。
        System.out.println("--------------全局过滤器MyDefineGatewayGlobalFilter------------------");
        String token = exchange.getRequest().getHeaders().getFirst("token");
        if (StringUtils.isBlank(token)) {
            //设置响应状态码为未授权
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
    /**
     * 值越小越先执行
     * @return 排序值
     */
    @Override
    public int getOrder() {
        return ORDERED;
    }
}
/**
 * Gateway的高可用（了解）
 *
 * 启动多个Gateway服务，自动注册到Eureka，形成集群。如果是服务内部访问，访问Gateway，自动负载均衡，没问题。
 * 但是，Gateway更多是外部访问，PC端、移动端等。它们无法通过Eureka进行负载均衡，那么该怎么办？
 * 此时，可以使用其它的服务网关，来对Gateway进行代理。比如：Nginx 。
 */
/**
 * Gateway与Feign的区别
 *
 * Gateway 作为整个应用的流量入口，接收所有的请求，如PC、移动端等，并且将不同的请求转发至不同的处理微服务模块，其作用可视为Nginx；
 *          大部分情况下用作权限鉴定、服务端流量控制等等。
 * Feign 则是将当前微服务的部分服务接口暴露出来，并且主要用于各个微服务之间的服务调用。
 */
/**
 * 命名分组：(?<name>capturing text)
 * 将匹配的子字符串捕获到一个组名称或编号名称中，在获得匹配结果时，可通过分组名进行获取。例如这里的示例，就是将 “capturing text” 捕获到名称为 “name” 的组中
 *
 * 引用捕获文本：${name}
 * 将名称为name的命名分组所匹配到的文本内容替换到此处
 *
 * 那么就很好解释官网的这个例子了，
 * 对于配置文件中的： - RewritePath=/red(?<segment>/?.*), $\{segment}详解：
 * (?<segment>/?.*)：
 *
 *     1.?<segment>
 *     名称为 segment 的组
 *     2./?
 *     /出现0次或1次
 *     3..*
 *     任意字符出现0次或多次
 *
 * 合起来就是：将 /?.*匹配到的结果捕获到名称为segment的组中
 * $\{segment}：
 *
 * 将名称为 segment 的分组捕获到的文本置换到此处。
 * 注意，\的出现是由于避免 yaml 语法认为这是一个变量（因为在 yaml 中变量的表示法为 ${variable}，而这里我们想表达的是字面含义），在 gateway 进行解析时，会被替换为 ${segment}
 */
