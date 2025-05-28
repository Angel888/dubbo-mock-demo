package org.example.mockdemo;

public class PH3636MockMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Mock mock = invocation.getMethod().getAnnotation(Mock.class);
        if (mock == null) {
            return invocation.proceed();
        }
        Pair pair = MockContext.getMockConfigDO(invocation.getThis().getClass().getName(), invocation.getMethod().getName(), invocation.getMethod().getParameterTypes());
        if (MockContext.notContent(pair.getRight())) {
            return invocation.proceed();
        }
        Class > returnType = invocation.getMethod().getReturnType();
        if (MockContext.notReturnType(returnType)) {
            return invocation.proceed();
        }
        return JsonUtil.fromJsonToType(pair.getRight().getContent(), invocation.getMethod().getGenericReturnType());
    }
}
