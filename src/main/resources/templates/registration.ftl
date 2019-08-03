<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>


<@c.page>
    <h3><@spring.message "page.registration"/></h3>

    <#if message??>
        <div class="alert alert-danger" role="alert">${message}</div>
    </#if>

    <form action="/registration" method="post">

    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "registration.firstname"/></label>
<div class="col-sm-6">
    <input type="text" class="form-control" name="firstName" required/>
</div>
    </div>

    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "registration.lastname"/></label>
<div class="col-sm-6">
    <input type="text" class="form-control" name="lastName" required/>
</div>
    </div>
    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "registration.email"/></label>
<div class="col-sm-6">
    <input type="text" class="form-control" name="email" required/>
</div>
    </div>
    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "registration.username"/></label>
<div class="col-sm-6">
    <input type="text" class="form-control" name="username" required/>
</div>
    </div>
    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "registration.password"/></label>
<div class="col-sm-6">
    <input type="text" class="form-control" name="password" required/>
</div>
    </div>


<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-primary" type="submit"><@spring.message "registration.register"/></button>
    </form>
</@c.page>