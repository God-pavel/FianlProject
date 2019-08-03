<#import "/spring.ftl" as spring/>

<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>
<#include "parts/security.ftl">
<@c.page>
    <div class="row justify-content-center">
    <div class="col-md-12">
    <table class="table table-striped">

    <h3><@spring.message "page.merchandise"/></h3>
    <#if message??>
        <div class="alert alert-danger" role="alert">${message}</div>
    </#if>

    <form action="/merchandise" method="post">
    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "merchandise.product"/></label>
<div class="col-sm-4">
    <input type="text" class="form-control" name="name" required/>
</div>
    </div>
<#--TODO correct productType-->
    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "merchandise.price"/></label>
<div class="col-sm-4">
    <input type="number" class="form-control" name="price" step="any" required/>
</div>
    </div>
    <div class="form-group row">
    <label class="col-sm-2 col-form-label"> <@spring.message "merchandise.amount"/></label>
<div class="col-sm-4">
    <input type="number" name="amount" class="form-control" required/>
</div>
    </div>
<#--TODO Make correct checkbox-->
    <#list types as type>
        <div>
        <label><input type="checkbox" name="${type}">${type}</label>
        </div>
    </#list>
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button class="btn btn-primary" type="submit"><@spring.message "merchandise.add"/></button>

    </form>
    </table>
    </div>
    </div>
</@c.page>