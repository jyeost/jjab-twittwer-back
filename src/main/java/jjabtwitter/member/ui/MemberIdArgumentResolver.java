package jjabtwitter.member.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jjabtwitter.global.exception.ClientException;
import jjabtwitter.member.application.dto.MemberId;
import jjabtwitter.member.domain.LoginInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

import static jjabtwitter.global.exception.ExceptionInformation.AUTHORIZATION_EMPTY;
import static jjabtwitter.member.ui.SessionConst.SESSION;

@Component
public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private static HttpSession getSession(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);

        if (Objects.isNull(session)) {
            throw new ClientException(AUTHORIZATION_EMPTY);
        }
        return session;
    }

    private static LoginInfo getAttribute(final HttpSession session) {
        final Object attribute = session.getAttribute(SESSION.getKey());
        if (Objects.isNull(attribute)) {
            throw new ClientException(AUTHORIZATION_EMPTY);
        }
        return (LoginInfo) attribute;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberId.class)
                && parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final HttpSession session = getSession(request);
        final Long userId = getUserId(session);

        session.setMaxInactiveInterval(SESSION.getValidatedTime());
        return new MemberId(userId);
    }

    private Long getUserId(final HttpSession session) {
        try {
            final LoginInfo loginInfo = getAttribute(session);
            return loginInfo.getRepresentativeId();
        } catch (IllegalStateException e) {
            throw new ClientException(AUTHORIZATION_EMPTY);
        }
    }
}
