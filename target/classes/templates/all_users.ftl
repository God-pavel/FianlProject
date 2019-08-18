<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>



<@c.page>
    <div class="row justify-content-center">
    <div class="col-md-12">
    <h3><@spring.message "page.users"/></h3>
    <table class="table table-striped">
    <thead>
<tr>
    <th><@spring.message "users.id"/></th>
    <th><@spring.message "users.username"/></th>
    <th><@spring.message "users.password"/></th>
    <th><@spring.message "users.role"/></th>
    <th><@spring.message "users.edit"/></th>
</tr>
    </thead>
    <tbody>
    <#list users as user>
        <tr>
        <td>${user.id}</td>
        <td>${user.username}</td>
        <td>${user.password}</td>
        <td><#list user.roles as role>${role}<#sep>, </#list></td>
        <td><a href="/user/${user.id}"><@spring.message "users.edit"/></a> </td>
        </tr>
    </#list>
    </tbody>
    </table>
    </div>
    </div>
</@c.page>