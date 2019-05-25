package com.ysl.kappak.request;

import com.ysl.kappak.util.HttpHelper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author ：youngsapling
 * @date ：Created in 2019/5/23 20:58
 * @modifyTime :
 * @description : 对HttpServletRequest进行封装, 获取其body.
 */
public class RequestBodyHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 将原始请求体读取出来存下来.
     */
    private final byte[] body;

    public RequestBodyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String stringBody = HttpHelper.getRequestBodyString(request);
        body = stringBody.getBytes(Charset.forName("utf-8"));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
