<#import "/spring.ftl" as spring/>

<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    curUser = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = curUser.getUsername()
    isAdmin = curUser.isAdmin()
    isMerchandiser = curUser.isMerchandiser()
    isSeniorCashier = curUser.isSeniorCashier()
    isUser = curUser.isUser()
    >
<#else>
    <#assign
    name = "unknown"
    isAdmin = false
    isMerchandiser = false
    isSeniorCashier = false
    isUser = false
    >
</#if>