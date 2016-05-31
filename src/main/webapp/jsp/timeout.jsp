<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
  var url = '${param.url}';
  if(window.parent){
      window.parent.location = url;  
  } else {
      window.location = url;  
 }   
</script>
</head>
</html>