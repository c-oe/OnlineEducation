package com.learn.coe.infrastructure.apigaeway.config.filter;

import com.google.gson.JsonObject;
import com.learn.coe.common.base.util.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author coffee
 * @since 2021-05-29 12:55
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        //判断路径中含有 /api/**/auth/** 需要鉴权
        if (antPathMatcher.match("/api/**/auth/**",path)){

            //获取token
            List<String> token = request.getHeaders().get("token");
            if (token == null){
                //没有token
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            }

            //token校验失败
            boolean checkJwtTToken = JwtUtils.checkJwtTToken(token.get(0));
            if (!checkJwtTToken){
                ServerHttpResponse response = exchange.getResponse();
                return out(response);
            }
        }
        //放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    //使用webflux输入请求信息
    private Mono<Void> out(ServerHttpResponse response) {

        JsonObject message = new JsonObject();
        message.addProperty("success", false);
        message.addProperty("code", 28004);
        message.addProperty("data", "");
        message.addProperty("message", "鉴权失败");
        byte[] bytes = message.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        //输出http响应
        return response.writeWith(Mono.just(buffer));
    }
}
