package com.bookinggo.RESTfulDemo.util;

import com.bookinggo.RESTfulDemo.dto.ErrorDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.bookinggo.RESTfulDemo.util.RestfulDemoUtil.badRequestResponse;
import static org.assertj.core.api.Assertions.assertThat;

public class RestfulDemoUtilTest {
    @Test
    public void badRequestResponseIsValid() {
        String testMessage = "test";
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        ResponseEntity<ErrorDto> responseEntity = badRequestResponse(testMessage);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getBody().getStatus()).isEqualTo(400);
        assertThat(responseEntity.getBody().getError()).isEqualTo("Bad Request");
        assertThat(responseEntity.getBody().getMessage()).isEqualTo(testMessage);
    }
}
