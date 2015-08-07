<!DOCTYPE html>
<html>
<head>
    <title>Index page</title>
    <link rel="stylesheet" href="css/ex.css" media="screen" type="text/css">
    <!-- HTML basis taken from
         http://samples.galenframework.com/tutorial1/tutorial1.html
     -->
</head>
<body>
<div id="header">
    <h1>Header</h1>
</div>
<div id="menu">
    <ul>
        <li><a href="#">Item 1</a></li>
        <li><a href="#">Item 2</a></li>
        <li><a href="#">Item 3</a></li>
        <li><a href="#">Item 4</a></li>
    </ul>
</div>
<div id="middle">
    <div id="content-container">
        <div id="content-box">
            <div id="content">
                <ul>
                <#list content as c>
                <li>${c}</li>
                </#list>
                </ul>
            </div>
        </div>
    </div>
    <div id="side-panel">
        Side panel
    </div>
</div>
<div id="footer">
    Footer
</div>
</body>
</html>
