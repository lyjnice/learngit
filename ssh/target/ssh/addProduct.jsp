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
    <s:form action="product_save" method="post" namespace="/" theme="simple"> 
        <table>
           <tr>
	           <td>商品名称</td>
	           <td> <s:textfield name="product.pname" ></s:textfield></td>
           </tr>
           <tr>
	           <td>价格</td>
	           <td> <s:textfield name="product.price"></s:textfield></td>
           </tr>
           <tr>
	           <td><input type="submit" value="添加"></td>
           </tr>
        </table>
    </s:form>
</body>
</html>