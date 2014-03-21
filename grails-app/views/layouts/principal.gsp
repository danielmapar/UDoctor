<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><g:layoutTitle default="UDOCTOR" /></title>
<r:layoutResources />
</head>
<body>
<g:render template="/templatesLayout/header" model='["searchbarAtivo": "${pageProperty(name: 'page.searchbarAtivo')}"]'/> 
<g:layoutBody />
<g:render template="/templatesLayout/footer"/> 
<r:layoutResources />
</body>
</html>