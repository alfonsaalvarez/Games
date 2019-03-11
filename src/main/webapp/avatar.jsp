<%@page import="edu.uclm.esi.tysweb.laoca.dominio.*"%>
<%@page import="org.json.JSONObject"%>
<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String p=request.getParameter("p");
	JSONObject jso=new JSONObject(p);
	
	try {
		String email=jso.optString("email");
		String avatar = Manager.get().avatar(email);
		
		JSONObject respuesta = new JSONObject ();
		respuesta.put("avatar",avatar);
		respuesta.put("result","OK");
		
		out.println(respuesta.toString());	
	} catch (Exception e) {
		jso.put("result","ERROR");
		jso.put("mensaje",e.getMessage());
	}	
%>









