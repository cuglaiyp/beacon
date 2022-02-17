package com.onebit.beacon.support.util;

import org.springframework.util.PropertyPlaceholderHelper;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @Author: Onebit
 * @Date: 2022/1/13
 */
public class ContentHolderUtil {
    // 占位符前缀
    private static final String PLACE_HOLDER_PREFIX = "{$";
    // 占位符后缀
    private static final String PLACE_HOLDER_SUFFIX = "}";

    private static PropertyPlaceholderHelper propertyPlaceholderHelper =
            new PropertyPlaceholderHelper(PLACE_HOLDER_PREFIX, PLACE_HOLDER_SUFFIX);

    /*private static final StandardEvaluationContext evaluationContext;

    static {
        evaluationContext = new StandardEvaluationContext();
        evaluationContext.addPropertyAccessor(new MapAccessor());
    }*/

    public static String replacePlaceHolder(final String template, final Map<String, String> paramMap) {
        String replacedContent = propertyPlaceholderHelper.replacePlaceholders(template,
                new CustomPlaceholderResolver(paramMap));
        return replacedContent;
    }

    private static class CustomPlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {
        private Map<String, String> paramMap;

        public CustomPlaceholderResolver(Map<String, String> paramMap) {
            super();
            this.paramMap = paramMap;
        }

        @Override
        public String resolvePlaceholder(String placeholderName) {
            String value = paramMap.get(placeholderName);
            if (null == value) {
                String errorStr = MessageFormat.format("template:{} require param:{},but not exist! paramMap:{}",
                        placeholderName, paramMap.toString());
                throw new IllegalArgumentException(errorStr);
            }
            return value;
        }
    }
}
