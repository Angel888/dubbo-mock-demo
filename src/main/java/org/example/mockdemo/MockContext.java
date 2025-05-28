package org.example.mockdemo;

@Configurationpublic
class MockContext implements ApplicationContextAware {
    private static final boolean online = !EnvUtils.isOffline();
    private static FastThreadLocal fastThreadLocal = new FastThreadLocal<>();
    private static ApplicationContext instance;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance = applicationContext;
    }

    public static ApplicationContext getInstance() {
        return instance;
    }

    /**
     * 是否是线上环境
     */
    public static boolean online() {
        return online;
    }

    public static void set(LoginUser loginUser) {
        fastThreadLocal.set(loginUser);
    }

    public static LoginUser get() {
        return fastThreadLocal.get();
    }

    public static void remove() {
        fastThreadLocal.remove();
    }

    /**
     * 唯一url(类，方法，参数拼接)
     */
    public static String getUrl(String className, String methodName, Class>[]parameterTypes) {
        StringBuilder sb = new StringBuilder(className);
        sb.append(".").append(methodName);
        StringJoiner stringJoiner = new StringJoiner(",", "(", ")");
        if (parameterTypes != null) {
            for (Class > parameterType :parameterTypes){
                if (parameterType != null) {
                    stringJoiner.add(parameterType.getName());
                }
            }
        } return sb.append(stringJoiner.toString()).toString();
    }

    /**
     * 数据库配置     * 可以替换为http形式等等
     */
    public static MockDO getMockConfigDO(String url) {
        LoginUser loginUser = MockContext.get();
        if (loginUser == null) {
            return null;
        }
        return MockContext.getInstance().getBean(MockService.class).loadByUserIdUrl(loginUser.getUserId(), url);
    }

    public static PairgetMockConfigDO(String className, String methodName, Class>[]parameterTypes) {
        String url = getUrl(className, methodName, parameterTypes);
        return Pair.of(url, getMockConfigDO(url));
    }

    public static boolean notContent(MockDO mockDO) {
        return mockDO == null;
    }

    public static boolean notReturnType(Class>returnType) {
        return returnType == null || returnType == void.class || returnType == Void.class;
    }

    /**
     * 获取唯一key     * 第一个参数为类的全限定名(必填)，第二个参数为该类的方法名(选填)     * 控制台方式：用英文逗号分割
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String val;
        do {
            System.out.print("请输入：");
            val = input.next();
            print(val.split(","));
        } while (!"exit".equals(val));
        System.out.println("程序退出！");
        input.close();
    }

    public static void print(String[] args) {
        if (args == null || args.length < 1 || "exit".equals(args[0])) {
            return;
        }
        Class > c;
        try {
            c = Class.forName(args[0]);
        } catch (ClassNotFoundException ignore) {
            System.out.println("类不存在！" + ignore.fillInStackTrace());
            return;
        }
        Method[] methods = c.getMethods();
        String m = args.length > 1 ? args[1] : null;
        for (Method method : methods) {
            if (StringUtils.isBlank(m)) {
                System.out.println(getUrl(c.getName(), method.getName(), method.getParameterTypes()));
            } else if (method.getName().equals(m)) {
                System.out.println(getUrl(c.getName(), method.getName(), method.getParameterTypes()));
            }
        }
    }
}
