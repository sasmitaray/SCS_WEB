package com.sales.crm.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sales.crm.model.Role;
import com.sales.crm.model.User;
import com.sales.crm.service.UserService;

public class LoginFilter implements Filter {
	
	
	UserService userService = null;
	
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException { 
    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath()+"/web/userWeb/login";
        String logoutURI = request.getContextPath()+"/logout";
        String contextPath = request.getContextPath();

        boolean loggedIn = false;
        boolean loginRequest = false;
        boolean restRequest = false;
        boolean createResellerForm = false;
        boolean saveReseller = false;
        boolean restValidateUser = false;
        boolean selfRegistration = false;
        boolean resources = false;
        boolean createTenantForm = false;
        boolean selfRegistrationTenant = false;
        
        
        if(request.getRequestURI().equals(logoutURI)){
        	if(session != null){
        		session.invalidate();
        	}
		}else{
			loggedIn = session != null && session.getAttribute("user") != null;
			loginRequest = request.getRequestURI().equals(loginURI);
			restRequest = request.getRequestURI().contains(contextPath + "/rest/") ? true : false;
			createResellerForm = request.getRequestURI().contains(contextPath + "/web/resellerWeb/selfRegisterResellerForm") ? true : false;
			saveReseller = request.getRequestURI().contains(contextPath + "/web/resellerWeb/saveReseller") ? true : false;
			restValidateUser = request.getRequestURI().contains(contextPath + "/rest/userReST/validateUser") ? true : false;
			selfRegistration = request.getRequestURI().contains(contextPath + "web/resellerWeb/selfRegisterReseller") ? true : false;
			resources = request.getRequestURI().contains(contextPath+ "/resources/") ? true : false;
			createTenantForm = request.getRequestURI().contains(contextPath + "/web/tenantWeb/selfRegisterTenantForm") ? true : false;
			selfRegistrationTenant = request.getRequestURI().contains(contextPath + "web/tenantWeb/selfRegisterTenant") ? true : false;
			
		}
		
		if (createResellerForm || saveReseller || loginRequest || loggedIn || restValidateUser || selfRegistration
				|| resources || createTenantForm || selfRegistrationTenant) {
		 	chain.doFilter(request, response);
        }else if(restRequest){
        	if(validateRESTCredential(request, response)){
        		chain.doFilter(request, response);
        	}else{
        		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	}
        }else {
        	RequestDispatcher rd=req.getRequestDispatcher("/index.jsp");  
        	rd.include(req, res); 
        }
    }
    
    private boolean isAdminUser(User user){
		List<Role> roles = user.getRoles();
		if(roles == null){
			return false;
		}
		for(Role role : roles){
			if(role.getRoleID() == 1){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		userService = 
  			  (UserService)WebApplicationContextUtils.
  			    getRequiredWebApplicationContext(filterConfig.getServletContext()).
  			    getBean("userService");
	
		
	}

    boolean validateRESTCredential(HttpServletRequest request, HttpServletResponse response){
    	String authCredentials = request.getHeader("Authorization");
    	if (null == authCredentials)
			return false;
		// header value format will be "Basic encodedstring" for Basic
		// authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = authCredentials.replaceFirst("Basic"
				+ " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(
					encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String userName = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		if(userService.validateUserCredential(userName, password)){
			User user;
			try{
				user = userService.getUser(userName);
			}catch(Exception exception){
				return false;
			}
			HttpSession session = request.getSession(true);
			session.setAttribute("user", user);
			session.setAttribute("tenantID", user.getTenantID());
			return true;
		}
		return false;	
    }
}