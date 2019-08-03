<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>


<@c.page>
    <div class="container mt-4">
    <h3><@spring.message "page.report"/></h3>
    <#if message != "">
        <div>${message}</div>
    </#if>

    <div class="container">
    <div class="row">
    <div class="col-sm">
    <form action="/report/createXReport" method="post">
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
<button class="btn btn-primary" type="submit">Create X-Report</button>
    </form>
    </div>
    <div class="col-sm">
    <form action="/report/createZReport" method="post">
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-primary" type="submit">Create Z-Report</button>
    </form>
    </div>
    </div>
    </div>
    </div>
    <div class="card-columns">
    <#list reports as report>
        <div class="card my-3" style="width: 18rem;">
        <b>Report: ${report.id}</b>
        <div class="m-2">
        <p>Type: ${report.reportType}</p>
        <p>Date: ${report.date}</p>
        <p>Creator: ${report.user.username}</p>
        </div>
        <div class="card-footer text-muted">
        <b>Total: ${report.total}</b>
        </div>
        </div>
    </#list>
    </div>



</@c.page>