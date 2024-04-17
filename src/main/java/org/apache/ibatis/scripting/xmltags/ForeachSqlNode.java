package org.apache.ibatis.scripting.xmltags;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.parsing.GenericTokenParser;

import ognl.Ognl;

/**
 * @author furious 2024/4/17
 */
public class ForeachSqlNode implements SqlNode {

    public static final String ITEM_PREFIX = ForeachSqlNode.class.getName();

    private final SqlNode contents;
    private final String collection;
    private final String item;
    private final String open;
    private final String close;
    private final String separator;

    public ForeachSqlNode(SqlNode contents, String collection, String item, String open, String close, String separator) {
        this.contents = contents;
        this.collection = collection;
        this.item = item;
        this.open = open;
        this.close = close;
        this.separator = separator;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean apply(DynamicContext context) {
        Iterable<?> iterable = null;
        try {
            Object value = Ognl.getValue(Ognl.parseExpression(collection), context.getParameterObject());
            if (value instanceof Iterable && ((Iterable) value).iterator().hasNext()) {
                iterable = (Iterable) value;
            }
        } catch (Exception e) {
            return false;
        }
        if (Objects.isNull(iterable)) {
            return true;
        }

        applyOpen(context);
        int index = 0;
        for (Object o : iterable) {
            DynamicContext oldContext = context;
            if (index == 0) {
                context = new SeparatorContext(context, "");
            } else {
                context = new SeparatorContext(context, separator);
            }
            applyItem(context, o, index);
            contents.apply(new FilteredDynamicContext(context, item, index));
            context = oldContext;
            index++;
        }
        applyClose(context);
        return true;
    }

    private void applyOpen(DynamicContext context) {
        if (Objects.nonNull(open)) {
            context.appendSql(open);
        }
    }

    private void applyItem(DynamicContext context, Object o, int i) {
        if (Objects.nonNull(item)) {
            context.bind(item, o);
            context.bind(itemizeItem(item, i), o);
        }
    }

    private void applyClose(DynamicContext context) {
        if (Objects.nonNull(close)) {
            context.appendSql(close);
        }
    }

    private static String itemizeItem(String item, int i) {
        return ITEM_PREFIX + item + "_" + i;
    }

    private static class SeparatorContext extends DynamicContext {

        private final DynamicContext delegate;
        private final String separator;

        public SeparatorContext(DynamicContext delegate, String separator) {
            this.delegate = delegate;
            this.separator = separator;
        }

        @Override
        public Map<String, Object> getBindings() {
            return delegate.getBindings();
        }

        @Override
        public void bind(String name, Object value) {
            delegate.bind(name, value);
        }

        @Override
        public void appendSql(String sql) {
            if (StringUtils.isNoneBlank(sql)) {
                delegate.appendSql(separator);
            }
            delegate.appendSql(sql);
        }

        @Override
        public String getSql() {
            return delegate.getSql();
        }
    }

    private static class FilteredDynamicContext extends DynamicContext {
        private final DynamicContext delegate;
        private final String item;
        private final int index;

        public FilteredDynamicContext(DynamicContext delegate, String item, int index) {
            this.delegate = delegate;
            this.item = item;
            this.index = index;
        }

        @Override
        public Map<String, Object> getBindings() {
            return delegate.getBindings();
        }

        @Override
        public void bind(String name, Object value) {
            delegate.bind(name, value);
        }

        @Override
        public String getSql() {
            return delegate.getSql();
        }

        @Override
        public void appendSql(String sql) {
            GenericTokenParser parser = new GenericTokenParser("#{", "}", content -> "#{" + itemizeItem(item, index) + "}");
            delegate.appendSql(parser.parse(sql));
        }
    }
}
