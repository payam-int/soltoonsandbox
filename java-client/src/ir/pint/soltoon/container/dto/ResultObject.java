package ir.pint.soltoon.container.dto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ResultObject {
    private Map<String, String> states = new ConcurrentHashMap<>();
    private ResultInfo resultInfo;
}
