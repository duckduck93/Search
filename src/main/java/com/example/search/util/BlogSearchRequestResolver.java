package com.example.search.util;

import com.example.search.blog.exchange.BlogSearchRequest;
import com.example.search.errors.RequiredParameterNotExistsException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

public class BlogSearchRequestResolver extends PageableHandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return BlogSearchRequest.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Optional<String> query = Optional.ofNullable(webRequest.getParameter("query"));
        Optional<String> sort = Optional.ofNullable(webRequest.getParameter("sort"));
        Integer page = Integer.parseInt(Optional.ofNullable(webRequest.getParameter("page")).orElse("1"));
        Integer size = Integer.parseInt(Optional.ofNullable(webRequest.getParameter("size")).orElse("10"));

        return new BlogSearchRequest(
                query.orElseThrow(RequiredParameterNotExistsException::new),
                page,
                size,
                SortType.valueOf(sort.orElse(SortType.ACCURACY.name()))
        );
    }
}
