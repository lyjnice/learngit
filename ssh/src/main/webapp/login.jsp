<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri ="/struts-tags" prefix="s"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
  <h1>保存商品页面</h1>
    <s:form action="sso_doLogin" method="post" namespace="/" theme="simple"> 
        <table>
           <tr>
	           <td>用户名</td>
	           <td> <s:textfield name="user.pname" ></s:textfield></td>
           </tr>
           <tr>
	           <td>密码</td>
	           <td> <s:textfield name="user.password"></s:textfield></td>
           </tr>
           <tr>
	           <td>暂存url</td>
	           <td> <s:textfield name="gotoUrl"  value=${gotoUrl}></s:textfield></td>
           </tr>
           <tr>
	           <td><input type="submit" value="添加"></td>
           </tr>
        </table>
    </s:form>
</body>
</html>