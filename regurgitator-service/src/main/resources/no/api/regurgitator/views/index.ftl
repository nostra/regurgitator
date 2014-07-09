<#-- @ftlvariable name="" type="no.api.regurgitator.views.IndexView" -->
<#setting url_escaping_charset='UTF-8'>
<#assign symbolactive="">
<#assign onactive="">
<#assign offactive="">

<#if toRecord?c == "true">
	<#assign onactive="active">
	<#assign offactive="">
	<#assign symbolactive="active">
<#else>
	<#assign onactive="">
	<#assign offactive="active">
	<#assign symbolactive="">
</#if>

<!DOCTYPE html>
<html>
<head>
    <title>Regurgitator Proxy recorder</title>
    <meta charset="utf-8" />
    <link rel="stylesheet" type='text/css' href="/asset/stylesheets/reset.css">
    <link rel="stylesheet" type='text/css' href="/asset/stylesheets/styles.css">
    <link href='http://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>  
</head>
<body>
<div id="header">
	<div id="header-wrapper">
	    <img src="/asset/images/regurgitator-logo.png" alt="Regurgitator Logo"/>
	    <div id="header-container" class="clearfix">
	        <div id="header-container-inner" class="clearfix">
	            <div class="circleSymbol ${symbolactive}"></div>
	            <div id="top-right-text">RECORD PROXY:</div>
	            <div id="button-container" class="clearfix">
	            	<form action="." method="post" name="toggle" id="toggleForm">		            	           
			            <div class="proxyButton ${onactive}" id="on" onclick="javascript:document.getElementById('toggleForm').submit();">ON</div>
			            <div class="proxyButton ${offactive}" id="off" onclick="javascript:document.getElementById('toggleForm').submit();">OFF</div>
			       </form>
		        </div>	           
	        </div>
	    </div>
	</div>
</div>
<div id="content">
    <div id="content-container">
        <div id="content-top">USED SIZE:  ${storage.sizeAsKb} KB</div>
        <div id="content-bottom">
            <ul>
            	<#list storage.keys as key>
            		<#if !(key?ends_with("_headers"))>
            			<li><a href="/read/${key?url}">${key}</a></li>
            		</#if>
            	</#list>
            </ul>
            <script type="text/javascript">
			    //<![CDATA[ 
		            function resize()
		            {
		                var heights = window.innerHeight;
		                if(window.innerHeight >= 800)
		                {
		                	heights = 640;                	
		                }
		                else
		            	{
		                	heights = heights - 200;
		            	}
		                document.getElementById("content-bottom").style.height = heights + "px";
		            }
		            resize();
		            window.onresize = function() {resize();};
			     //]]>  
       		 </script> 
        </div>  
         <div id="footer">MODIFY BY AMEDIA</div>    
    </div>     
</div>
</body>
</html>
