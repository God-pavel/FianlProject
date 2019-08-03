<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>


<@c.page>
    <h3><@spring.message "page.check"/></h3>
    <#if message != "">
        <div class="alert alert-danger" role="alert">${message}</div>
    </#if>

    <p>Check creator: ${check.userName}</p>
    <p>Time: ${check.time}</p>

    <#list check.productAmount as key, value>
        <p>${key.name}  Amount: ${value}</p>

    </#list>
    <p>Total: ${check.total}</p>

    <form action="/createCheck/closeCheck/${check.id}" method="post">

<input type="hidden" name="checkId" value="${check.id}"/>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-primary m-3" type="submit">Close check</button>
    </form>


    <div class="container">
    <div class="row">
    <div class="col-sm">
    <form action="/createCheck/addByName/${check.id}" method="post">
<div class="form-group row">
    <label class="col-sm-3 col-form-label">Product name: </label>
    <div class="col-sm-5">
        <input type="text" class="form-control" name="name" required/>
    </div>
</div>

<div class="form-group row">
    <label class="col-sm-3 col-form-label">Amount</label>
    <div class="col-sm-5">
        <input type="number" step="any" class="form-control" name="amount" required/>
    </div>
</div>

<input type="hidden" name="checkId" value="${check.id}"/>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
<button class="btn btn-outline-secondary btn-sm" type="submit">Add product by name</button>
    </form>
    </div>
    <div class="col-sm">
<form action="/createCheck/addById/${check.id}" method="post">
<div class="form-group row">
    <label class="col-sm-3 col-form-label">Product id: </label>
    <div class="col-sm-5">
        <input type="number" class="form-control" name="id" required/>
    </div>
</div>

<div class="form-group row">
    <label class="col-sm-3 col-form-label">Amount</label>
    <div class="col-sm-5">
        <input type="number" step="any" class="form-control" name="amount" required/>
    </div>
</div>

<input type="hidden" name="checkId" value="${check.id}"/>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-outline-secondary btn-sm" type="submit">Add product by id</button>
    </form>
    </div>

    </div>
    </div>







</@c.page>