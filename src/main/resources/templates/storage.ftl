<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<#import "/spring.ftl" as spring/>


<@c.page>
    <div class="row justify-content-center">
    <div class="col-md-12">
    <table class="table table-striped">
    <h3><@spring.message "page.storage"/></h3>
    <thead>
<tr>
    <th><@spring.message "users.id"/></th>
    <th><@spring.message "storage.product"/></th>
    <th><@spring.message "storage.amount"/></th>
    <th><@spring.message "storage.price"/></th>
    <th><@spring.message "storage.type"/></th>
    <#if isMerchandiser>
        <th><@spring.message "users.edit"/></th>
    </#if>

</tr>
    </thead>
    <tbody>
    <#list products as product>
        <tr>
        <td>${product.id}</td>
        <td>${product.name}</td>
        <td>${product.amount}</td>
        <td>${product.price}</td>
        <td>${product.productType}</td>
        <#if isMerchandiser>
            <td><a href="/storage/${product.id}"><@spring.message "users.edit"/></a> </td>
        </#if>
        </tr>
    </#list>
    </tbody>
    </table>

    </div>
    </div>
    <#import "parts/common.ftl" as c>
    <#import "/spring.ftl" as spring/>
</@c.page>

