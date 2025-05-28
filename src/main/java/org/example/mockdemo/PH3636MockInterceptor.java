package org.example.mockdemo;

@Configurationpublic
class PH3636MockInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (MockContext.online()) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        LoginUser loginUser = (LoginUser) request.getAttribute(Constants.LOGIN_USER);
        if (loginUser == null) {
            return true;
        }
        MockContext.set(loginUser);
        String uri = request.getRequestURI();
        MockDO mockDO = MockContext.getMockConfigDO(uri);
        if (MockContext.notContent(mockDO)) {
            return true;
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        try {
            pw.write(mockDO.getContent());
        } finally {
            pw.flush();
            pw.close();
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        if (MockContext.online()) {
            return;
        }
        MockContext.remove();
    }
}
