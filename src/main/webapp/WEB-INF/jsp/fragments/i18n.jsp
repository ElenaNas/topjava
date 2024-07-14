<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<script type="text/javascript">
    const i18n = {};

    i18n["addTitle"] = '<spring:message code="${param.page}.add"/>';
    i18n["editTitle"] = '<spring:message code="${param.page}.edit"/>';

    <c:forEach var='key' items='${["common.errorStatus", "common.confirm", "common.deleted", "common.saved", "common.enabled", "common.disabled"]}'>
    i18n['${key}'] = '<spring:message code="${key}"/>';
    </c:forEach>
</script>