package kcs.funding.fundingboost.domain.security.resolver;

import kcs.funding.fundingboost.domain.security.CustomUserDetails;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        log.info("JwtAuthorizationArgumentResolver called");
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext()
                .getAuthentication();
        if (authentication == null) {
            return null;
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getMemberId();
    }
}
