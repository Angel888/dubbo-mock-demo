package org.example.mockdemo;

@Configurationpublic
class PH3636MockBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (MockContext.online()) {
            return bean;
        }
        Mock mock = null;
        for (Method method : bean.getClass().getMethods()) {
            mock = method.getAnnotation(Mock.class);
            if (mock != null) {
                break;
            }
        }
        if (mock == null) {
            return bean;
        }
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.setExposeProxy(true);
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvisor(new DefaultPointcutAdvisor(new PH3636MockMethodInterceptor()));
        return proxyFactory.getProxy();
    }
}
