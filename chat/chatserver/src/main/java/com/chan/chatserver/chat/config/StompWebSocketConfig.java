package com.chan.chatserver.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
//이 어노테이션이 STOMP를 사용한다는 것을 명시
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    public StompWebSocketConfig(StompHandler stompHandler) {
        this.stompHandler = stompHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOriginPatterns("*")
                //ws:://가 아니라 http:// 엔트포인트를 사용할 수 있게 해주는
                //sockJs라이브러리를 통한 요청을 허용하는 설정.
                .withSockJS();

        registry.addEndpoint("/connect")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.configureMessageBroker(registry);
        // /publish/1 형태로 메시지를 발행해야 함을 설정
        // /publics로 시작하는 url 패턴으로 메시지가 발해외면 @Controller 객체의 @MessageMapping 메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/publish");

        // /topic/1 형태로 메시지를 수신(subsribe)해야 함을 설정
        registry.enableSimpleBroker("/topic");
    }

    //웹 소켓 요청(connect, subscribe, disconnect) 등의 요청시에는 http header등 http메시지를 넣어올 수 있고,
    // 이를 interceptor를 통해 가로채 토큰등을 검증할 수 있다.
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
        registration.interceptors(stompHandler);
    }
}
