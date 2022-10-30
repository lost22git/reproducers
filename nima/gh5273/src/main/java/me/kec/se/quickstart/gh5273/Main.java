package me.kec.se.quickstart.gh5273;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.helidon.logging.common.LogConfig;
import io.helidon.nima.webserver.WebServer;
import io.helidon.nima.webserver.http.HttpRouting;

public class Main {
    public static void main(String[] args) {

        // 启动完毕后退出 主要用来查看启动耗时
//    System.setProperty("exit.on.started", "!");

        // SE 下需要在运行入口处初始化 logging
        LogConfig.configureRuntime();

        configureJackson();

        var server = WebServer.builder()
                .socket("h1", (sb, rb) -> {
        sb.port(8080); // overrides the config from app.properties
                    var routing = HttpRouting.builder()
                            .update(Main::configureRouting)
                            .build();
                    rb.addRouting(routing);
                })
                .start();
    }

    static ObjectMapper JSON;

    static void configureJackson() {
        // TODO: What this does?
//        JSON = ObjectMapperCustomFn.createDefault().apply(new ObjectMapper());
        JSON = new ObjectMapper();
    }

    static void configureRouting(HttpRouting.Builder routing) {
        // noinspection unchecked
        routing.register("/info", InfoRoute::new);
    }
}
