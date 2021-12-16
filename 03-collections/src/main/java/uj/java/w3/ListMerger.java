package uj.java.w3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListMerger {
    public static List<Object> mergeLists(List<?> l1, List<?> l2) {
        List<Object> result = new ArrayList<>();
        if (l1 != null && l2 != null) {
            int len = Math.min(l1.size(), l2.size());

            for (int i = 0; i < len; i++){
                result.add(l1.get(i));
                result.add(l2.get(i));
            }

            if (l1.size() > len) {
                for (int i = len; i < l1.size(); i++) {
                    result.add(l1.get(i));
                }
            } else if (l2.size() > len){
                for (int i = len; i < l2.size(); i++) {
                    result.add(l2.get(i));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }
}
