package org.example.mockdemo;

public class PH3636MockClusterInvoker<T> implements Invoker<T> {
    private final Directory directory;
    private final Invoker invoker;

    public PH3636MockClusterInvoker(Directory directory, Invoker invoker) {
        this.directory = directory;
        this.invoker = invoker;
    }

    @Override
    public URL getUrl() {
        return directory.getUrl();
    }

    @Override
    public boolean isAvailable() {
        return directory.isAvailable();
    }

    @Override
    public void destroy() {
        this.invoker.destroy();
    }

    @Override
    public ClassgetInterface() {
        return directory.getInterface();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        if (MockContext.online()) {
            return invoker.invoke(invocation);
        }
        Pair pair = MockContext.getMockConfigDO(invoker.getInterface().getName(), invocation.getMethodName(), invocation.getParameterTypes());
        if (MockContext.notContent(pair.getRight())) {
            return invoker.invoke(invocation);
        }
        Method method;
        try {
            method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
        } catch (NoSuchMethodException e) {
            return invoker.invoke(invocation);
        }
        if (method == null) {
            return invoker.invoke(invocation);
        }
        Class > returnType = method.getReturnType();
        if (MockContext.notReturnType(returnType)) {
            return invoker.invoke(invocation);
        }
        return new RpcResult((Object) JsonUtil.fromJsonToType(pair.getRight().getContent(), method.getGenericReturnType()));
    }
}
