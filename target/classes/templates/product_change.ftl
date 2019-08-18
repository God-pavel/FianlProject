<#import "parts/common.ftl" as c>
<#import "/spring.ftl" as spring/>
<#include "parts/security.ftl">
<@c.page>
    <div class="container mt-4">

    <form action="/storage" method="post">
    <h3><@spring.message "page.product"/> ${product.name}</h3>
    <p>
    <@spring.message "merchandise.price"/> <input type="number" step="any" name="price" value="${product.price}">
    </p>
    <p>
    <@spring.message "merchandise.amount"/> <input type="number" step="any" name="amount" value="${product.amount}">
    </p>

<input type="hidden" name="productId" value="${product.id}">
<input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button type="submit"><@spring.message "merchandise.save"/></button>
    </form>
    </div>
</@c.page>