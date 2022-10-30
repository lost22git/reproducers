package me.kec.se.quickstart.gh5273;

import io.helidon.common.Reflected;

@Reflected
public class SystemInfo {
    private String info;

    public SystemInfo(String info) {
        this.info = info;
    }

    static SystemInfo create() {
        return new SystemInfo("This is info!");
    }

    public String getInfo() {
        return info;
    }
}
