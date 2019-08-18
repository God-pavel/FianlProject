<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>


<@c.page>
    <div class="container mt-4">
    <h3><@spring.message "page.report"/></h3>
    <#if message != "">
        <div class="alert alert-danger" role="alert">${message}</div>
    </#if>

    <div class="container">
    <div class="row">
    <div class="col-sm">
    <form action="/report/createXReport" method="post">
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
<button class="btn btn-primary" type="submit"><@spring.message "report.createX"/></button>
    </form>
    </div>
    <div class="col-sm">
    <form action="/report/createZReport" method="post">
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-primary" type="submit"><@spring.message "report.createZ"/></button>
    </form>
    </div>
    </div>
    </div>
    </div>
    <div class="card-columns">
    <#list reports as report>
        <div class="card my-3" style="width: 18rem;">
        <b><@spring.message "report.report"/> ${report.id}</b>
        <div class="m-2">
        <p><@spring.message "report.type"/> ${report.reportType}</p>
        <p><@spring.message "report.date"/> ${report.date}</p>
        <p><@spring.message "report.creator"/> ${report.user.username}</p>
        </div>
        <div class="card-footer text-muted">
        <b><@spring.message "report.total"/> ${report.total}</b>
        </div>
        </div>
    </#list>
    </div>



</@c.page>