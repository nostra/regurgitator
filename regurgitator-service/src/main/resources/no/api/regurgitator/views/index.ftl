<#-- @ftlvariable name="" type="no.api.regurgitator.views.IndexView" -->
<!DOCTYPE html>
<html>
<head>
    <title>Proxy recorder</title>
</head>
<#setting url_escaping_charset='UTF-8'>
<body>
<section>
    <article>
        <header>
            <h1>Press to toggle record mode</h1>
        </header>
        <section>
            Recording: <mark>${toRecord?c}</mark>.
            <form action="" method="post" name="toggle">
                <button type="submit">toggle</button>
            </form>
        </section>
        <section>
            <hr/>
            Used size: ${storage.sizeAsKb} KB
            <pre>
                <#list storage.keys as key>
<#if ! key?ends_with("_headers")>
<a href="/read/${key?url}">${key}</a>
</#if>
                </#list>
            </pre>
        </section>
    </article>
</section>
</body>
</html>
