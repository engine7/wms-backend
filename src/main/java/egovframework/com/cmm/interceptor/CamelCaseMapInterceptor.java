package egovframework.com.cmm.interceptor;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.*;

@Intercepts({
    @Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class}
    )
})
public class CamelCaseMapInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();

        if (result instanceof List<?>) {
            List<?> list = (List<?>) result;

            if (!list.isEmpty() && list.get(0) instanceof Map) {
                for (Map<String, Object> row : (List<Map<String, Object>>) list) {
                    List<String> keys = new ArrayList<>(row.keySet());
                    for (String key : keys) {
                        String camelKey = underscoreToCamel(key);
                        if (!camelKey.equals(key)) {
                            row.put(camelKey, row.remove(key));
                        }
                    }
                }
            }
        }
        return result;
    }

    private String underscoreToCamel(String input) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;
        for (char c : input.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                sb.append(nextUpper ? Character.toUpperCase(c) : Character.toLowerCase(c));
                nextUpper = false;
            }
        }
        return sb.toString();
    }
}
