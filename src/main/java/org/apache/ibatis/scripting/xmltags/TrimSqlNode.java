package org.apache.ibatis.scripting.xmltags;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * @author furious 2024/4/16
 */
public class TrimSqlNode implements SqlNode {

    private final SqlNode contents;
    private final String prefix;
    private final List<String> prefixesToOverride;

    public TrimSqlNode(SqlNode contents, String prefix, String prefixesToOverride) {
        this.contents = contents;
        this.prefix = prefix;
        this.prefixesToOverride = parseOverrides(prefixesToOverride);
    }

    @Override
    public boolean apply(DynamicContext context) {
        FilteredDynamicContext filteredDynamicContext = new FilteredDynamicContext(context);
        boolean result = contents.apply(filteredDynamicContext);
        filteredDynamicContext.applyAll();
        return result;
    }

    private static List<String> parseOverrides(String overrides) {
        if (StringUtils.isEmpty(overrides)) {
            return Collections.emptyList();
        }
        return Arrays.stream(overrides.split("\\|")).collect(Collectors.toList());
    }

    private class FilteredDynamicContext extends DynamicContext {
        private final DynamicContext delegate;
        private StringBuilder sqlBuffer = new StringBuilder();

        public FilteredDynamicContext(DynamicContext delegate) {
            this.delegate = delegate;
        }

        public void applyAll() {
            sqlBuffer = new StringBuilder(sqlBuffer.toString().trim());
            String trimmedUppercaseSql = sqlBuffer.toString().toUpperCase();
            if (trimmedUppercaseSql.length() > 0) {
                applyPrefix(sqlBuffer, trimmedUppercaseSql);
            }
            delegate.appendSql(sqlBuffer.toString());
        }

        @Override
        public void appendSql(String sql) {
            sqlBuffer.append(sql);
        }

        @Override
        public String getSql() {
            return delegate.getSql();
        }

        @Override
        public Object getParameterObject() {
            return delegate.getParameterObject();
        }

        private void applyPrefix(StringBuilder sql, String trimmedUppercaseSql) {
            if (Objects.nonNull(prefixesToOverride)) {
                for (String remove : prefixesToOverride) {
                    if (trimmedUppercaseSql.startsWith(remove)) {
                        sql.delete(0, remove.trim().length());
                        break;
                    }
                }
            }
            if (prefix != null) {
                sql.insert(0, " ");
                sql.insert(0, prefix);
            }
        }
    }

}
