package uj.java.w3;

import java.util.*;

public interface JsonMapper {
    String toJson(Map<String, ?> map);
    static JsonMapper defaultInstance() {
        return new JsonMapperClass();
    }
}

class JsonMapperClass implements JsonMapper{
    @SuppressWarnings("unchecked")
    public String listToString(List<Object> list){
        StringBuilder res = new StringBuilder("[");
        for (Object o : list) {
            if (o instanceof List l) {
                res.append(listToString(l));
            } else if (o instanceof Map m) {
                res.append(mapToString(m));
                res.append(",");
            } else if (o instanceof String s) {
                String tmp = s.replace("\"", "\\\"");
                res.append("\"").append(tmp).append("\"");
                res.append(",");
            } else {
                res.append(o);
                res.append(",");
            }
        }
        if (res.length() > 1) {
            res.setLength(res.length() - 1);
        }

        res.append("]");
        return res.toString();
    }
    @SuppressWarnings("unchecked")
    public String mapToString(Map<Object, Object> map){
        StringBuilder res = new StringBuilder("{");

        for(Object key: (map.keySet())){
            if(map.get(key) instanceof Map m){
                res.append("\"").append(key).append("\"").append(":").append(mapToString(m)).append(",");
            } else if (map.get(key) instanceof List l){
                res.append("\"").append(key).append("\"").append(":").append(listToString(l)).append(",");
            } else if (map.get(key) instanceof String s){
                String tmp = s.replace("\"", "\\\"");
                res.append("\"").append(key).append("\"").append(":").append("\"").append(tmp).append("\"").append(",");
            } else {
                res.append("\"").append(key).append("\"").append(":").append(map.get(key)).append(",");
            }
        }
        if (res.length() > 1) {
            res.setLength(res.length() - 1);
        }
        res.append("}");
        return res.toString();
    }
    @SuppressWarnings("unchecked")
    public String toJson(Map<String, ?> map){
        StringBuilder res = new StringBuilder("{");
        if (map == null || map.isEmpty()){
            return "{}";
        } else {
            for (String key : map.keySet()) {
                res.append("\"").append(key).append("\"").append(":");
                if (map.get(key) instanceof List l){
                    String tmp = listToString(l);
                    res.append(tmp).append(",");
                } else if (map.get(key) instanceof Map m){
                    String tmp = mapToString(m);
                    res.append(tmp).append(",");
                } else {
                    res.append(map.get(key)).append(",");
                }
            }
        }
        if (res.length() > 1) {
            res.setLength(res.length() - 1);
        }
        res.append("}");
        return res.toString();
    }
}