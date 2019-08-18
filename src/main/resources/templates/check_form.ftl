<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>


<@c.page>
    <h3><@spring.message "page.check"/></h3>
    <#if message != "">
        <div class="alert alert-danger" role="alert">${message}</div>
    </#if>

    <p><@spring.message "check.creator"/> ${check.userName}</p>
    <p><@spring.message "check.time"/> ${check.time}</p>

    <#list check.productAmount as key, value>
        <p>${key.name}  <@spring.message "check.amount"/> ${value}</p>

    </#list>
    <p><@spring.message "check.total"/> ${check.total}</p>

    <form action="/createCheck/closeCheck/${check.id}" method="post">

<input type="hidden" name="checkId" value="${check.id}"/>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-primary m-3" type="submit"><@spring.message "check.close"/></button>
    </form>


    <div class="container">
    <div class="row">
    <div class="col-sm">
    <form action="/createCheck/addByName/${check.id}" method="post">
<div class="form-group row">
    <label class="col-sm-3 col-form-label"><@spring.message "check.productName"/> </label>
    <div class="col-sm-5">
        <input type="text" class="form-control" name="name" required/>
    </div>
</div>

<div class="form-group row">
    <label class="col-sm-3 col-form-label"><@spring.message "check.amount"/></label>
    <div class="col-sm-5">
        <input type="number" step="any" class="form-control" name="amount" required/>
    </div>
</div>

<input type="hidden" name="checkId" value="${check.id}"/>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
<button class="btn btn-outline-secondary btn-sm" type="submit"><@spring.message "check.addProductByName"/></button>
    </form>
    </div>
    <div class="col-sm">
<form action="/createCheck/addById/${check.id}" method="post">
<div class="form-group row">
    <label class="col-sm-3 col-form-label"><@spring.message "check.productId"/> </label>
    <div class="col-sm-5">
        <input type="number" class="form-control" name="id" required/>
    </div>
</div>

<div class="form-group row">
    <label class="col-sm-3 col-form-label"><@spring.message "check.amount"/></label>
    <div class="col-sm-5">
        <input type="number" step="any" class="form-control" name="amount" required/>
    </div>
</div>

<input type="hidden" name="checkId" value="${check.id}"/>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-outline-secondary btn-sm" type="submit"><@spring.message "check.addProductById"/></button>
    </form>
    </div>

    </div>
    </div>







</@c.page>