package kr.co.sampler.crawling.common.util;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NullCheckUtil {
    private NullCheckUtil(){}

    public static boolean isNull(Object ... objects){
        for(Object object : objects){
            if(Objects.isNull(object)) return true;
        }
        return  false;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) { return true; }
        if ((obj instanceof String) && (((String)obj).trim().length() == 0)) { return true; }
        if (obj instanceof Map) { return ((Map<?, ?>)obj).isEmpty(); }
        if (obj instanceof List) { return ((List<?>)obj).isEmpty(); }
        if (obj instanceof Object[]) { return (((Object[])obj).length == 0); }

        return false;
    }
}
