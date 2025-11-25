package com.example.order.exception;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.common.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse response, String s, BlockException e) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        R error = R.error(500, e.getMessage());
        String json = objectMapper.writeValueAsString(error);
        response.getWriter().write(json);
    }
}
