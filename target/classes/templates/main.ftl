<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>
<#import "parts/pager.ftl" as p>
<#include "parts/security.ftl">
<@c.page>
    <h3><@spring.message "page.main"/></h3>
    <#if message != "">
        <div class="alert alert-danger" role="alert">${message}</div>
    </#if>
    <form action="/main/createCheck" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-primary" type="submit">Create check</button>
    </form>

    <@p.pager url page/>

        <div class="card-columns">
        <#list page.content as check>
            <div class="card my-3" style="width: 18rem;">
            <b>Check: ${check.id}</b>
            <div class="m-2">
            <p>Time: ${check.time}</p>
            <p>Check creator: ${check.user.username}</p>
            <#list check.productAmount as key, value>
                <div style="float: left;">${key.name}  Amount: ${value}</div>
                <#if isSeniorCashier>
                    <form action="/main/deleteProduct" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <input type="hidden" name="checkId" value="${check.id}"/>
                <input type="hidden" name="productName" value="${key.name}"/>
                    <button type="submit" class="btn ml-2 btn-outline-secondary btn-sm">Delete product</button>
                    </form>
                </#if>
            </#list>
            </div>
            <div class="card-footer text-muted">
            <b style="float: left;">Total: ${check.total}</b>
            <#if isSeniorCashier>
                <form action="/main/deleteCheck" method="post">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" name="checkId" value="${check.id}"/>
                <button type="submit" class="btn ml-4 btn-secondary">Delete check</button>
                </form>
            </#if>
            </div>
            </div>
        </#list>
        </div>

</@c.page>