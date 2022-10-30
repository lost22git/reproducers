package me.kec.se.quickstart.gh5273;

import io.helidon.common.http.HttpMediaType;
import io.helidon.nima.webserver.http.HttpRules;
import io.helidon.nima.webserver.http.HttpService;
import io.helidon.nima.webserver.http.ServerRequest;
import io.helidon.nima.webserver.http.ServerResponse;

public class InfoRoute implements HttpService {

    @Override
    public void routing(HttpRules rules) {
        rules.get("", this::getInfo);
    }

    void getInfo(ServerRequest req,
                 ServerResponse res) {
        var info = SystemInfo.create();
        res.headers().contentType(HttpMediaType.APPLICATION_JSON);
        res.send(info);
    }
}
