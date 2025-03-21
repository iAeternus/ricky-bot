package org.ricky.common.context;

import static org.ricky.core.common.utils.ValidationUtil.isNull;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className ThreadLocalContext
 * @desc
 */
public class ThreadLocalContext {

    private static final ThreadLocal<GroupMsgContext> CONTEXT = new ThreadLocal<>();

    public static void setContext(GroupMsgContext context) {
        if (isNull(context)) {
            throw new IllegalArgumentException("GroupMsgContext cannot be null");
        }
        CONTEXT.set(context);
    }

    public static GroupMsgContext getContext() {
        return CONTEXT.get();
    }

    public static void removeContext() {
        CONTEXT.remove();
    }

}
