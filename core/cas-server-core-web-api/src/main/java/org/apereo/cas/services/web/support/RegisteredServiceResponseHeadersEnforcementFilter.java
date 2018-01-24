package org.apereo.cas.services.web.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apereo.cas.authentication.principal.WebApplicationService;
import org.apereo.cas.security.ResponseHeadersEnforcementFilter;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.RegisteredServiceProperty;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.web.support.ArgumentExtractor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * This is {@link RegisteredServiceResponseHeadersEnforcementFilter}. A filter extension that looks at the properties of a
 * registered service to determine if headers should be injected into the response.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@Slf4j
@RequiredArgsConstructor
public class RegisteredServiceResponseHeadersEnforcementFilter extends ResponseHeadersEnforcementFilter {
    private final ServicesManager servicesManager;
    private final ArgumentExtractor argumentExtractor;

    @Override
    protected void insertContentSecurityPolicyHeader(final HttpServletResponse httpServletResponse, final HttpServletRequest httpServletRequest) {
        if (shouldHttpHeaderBeInjectedIntoResponse(httpServletRequest,
            RegisteredServiceProperty.RegisteredServiceProperties.HTTP_HEADER_ENABLE_CONTENT_SECURITY_POLICY)) {
            super.insertContentSecurityPolicyHeader(httpServletResponse, httpServletRequest);
        }
    }

    @Override
    protected void insertXSSProtectionHeader(final HttpServletResponse httpServletResponse, final HttpServletRequest httpServletRequest) {
        if (shouldHttpHeaderBeInjectedIntoResponse(httpServletRequest,
            RegisteredServiceProperty.RegisteredServiceProperties.HTTP_HEADER_ENABLE_XSS_PROTECTION)) {
            super.insertXSSProtectionHeader(httpServletResponse, httpServletRequest);
        }
    }

    @Override
    protected void insertXFrameOptionsHeader(final HttpServletResponse httpServletResponse, final HttpServletRequest httpServletRequest) {
        if (shouldHttpHeaderBeInjectedIntoResponse(httpServletRequest,
            RegisteredServiceProperty.RegisteredServiceProperties.HTTP_HEADER_ENABLE_XFRAME_OPTIONS)) {
            super.insertXFrameOptionsHeader(httpServletResponse, httpServletRequest);
        }
    }

    @Override
    protected void insertXContentTypeOptionsHeader(final HttpServletResponse httpServletResponse, final HttpServletRequest httpServletRequest) {
        if (shouldHttpHeaderBeInjectedIntoResponse(httpServletRequest,
            RegisteredServiceProperty.RegisteredServiceProperties.HTTP_HEADER_ENABLE_XCONTENT_OPTIONS)) {
            super.insertXContentTypeOptionsHeader(httpServletResponse, httpServletRequest);
        }
    }

    @Override
    protected void insertCacheControlHeader(final HttpServletResponse httpServletResponse, final HttpServletRequest httpServletRequest) {
        if (shouldHttpHeaderBeInjectedIntoResponse(httpServletRequest,
            RegisteredServiceProperty.RegisteredServiceProperties.HTTP_HEADER_ENABLE_CACHE_CONTROL)) {
            super.insertCacheControlHeader(httpServletResponse, httpServletRequest);
        }
    }

    @Override
    protected void insertStrictTransportSecurityHeader(final HttpServletResponse httpServletResponse, final HttpServletRequest httpServletRequest) {
        if (shouldHttpHeaderBeInjectedIntoResponse(httpServletRequest,
            RegisteredServiceProperty.RegisteredServiceProperties.HTTP_HEADER_ENABLE_STRICT_TRANSPORT_SECURITY)) {
            super.insertStrictTransportSecurityHeader(httpServletResponse, httpServletRequest);
        }
    }

    private boolean shouldHttpHeaderBeInjectedIntoResponse(final HttpServletRequest request,
                                                           final RegisteredServiceProperty.RegisteredServiceProperties property) {
        final Optional<RegisteredService> result = getRegisteredServiceFromRequest(request);
        if (result.isPresent()) {
            final Map<String, RegisteredServiceProperty> properties = result.get().getProperties();
            if (properties.containsKey(property.getPropertyName())) {
                final RegisteredServiceProperty prop = properties.get(property.getPropertyName());
                return BooleanUtils.toBoolean(prop.getValue());
            }
        }
        return false;
    }

    private Optional<RegisteredService> getRegisteredServiceFromRequest(final HttpServletRequest request) {
        final WebApplicationService service = this.argumentExtractor.extractService(request);
        if (service != null) {
            return Optional.of(this.servicesManager.findServiceBy(service));
        }
        return Optional.empty();
    }
}
