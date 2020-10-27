package com.example.aii.controller.interfacemanagement;

import com.example.aii.entity.BaseInterface;
import com.example.aii.exception.AiiException;
import com.example.aii.util.unpack.HTTPUnpack;
import com.example.aii.util.LineFeedUtils;
import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class InterfaceDTO {

    private String interfaceName;
    private Integer timeout;
    private String requestParams;
    private String responseParams;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(String responseParams) {
        this.responseParams = responseParams;
    }

    public BaseInterface toInterface() {
        return new Convert().doForward(this);
    }

    private static class Convert extends Converter<InterfaceDTO, BaseInterface> {

        @Override
        protected BaseInterface doForward(@Nonnull InterfaceDTO interfaceDTO) {
//            BaseInterface res = new BaseInterface();
//            BeanUtils.copyProperties(interfaceDTO, res);
//            String requestParams = interfaceDTO.getRequestParams();
//            String responseParams = interfaceDTO.getResponseParams();
//            // 拆解requestParams
//            requestParams = LineFeedUtils.toCRLF(requestParams);
//            String[] reqPackets = requestParams.split("\r\n\r\n");
//            String reqHeader = reqPackets[0];
//            String reqBody = reqPackets.length > 2 ? reqPackets[1] : null;
//            // request general
//            HTTPUnpack.General general = HTTPUnpack.handleGeneral(reqHeader.split("\r\n")[0]);
//            BeanUtils.copyProperties(general, res);
//            // request headers
//            LinkedMultiValueMap<String, String> reqHeaderMultiMp = HTTPUnpack.handleHeaders(reqHeader);
//            res.setRequestHeaders(reqHeaderMultiMp);
//            // request parameters
//            if (!StringUtils.isEmpty(reqBody)) {
//                // skip GET HEAD OPTIONS
//                HttpMethod method = general.getMethod();
//                if (!(method.equals(HttpMethod.GET)
//                        || method.equals(HttpMethod.HEAD)
//                        || method.equals(HttpMethod.OPTIONS))) {
//                    res.setRequestData(parametersFormat(reqHeaderMultiMp, reqBody));
//                }
//            }
//            // 拆解responseParams
//            if (!StringUtils.isEmpty(responseParams)) {
//                responseParams = LineFeedUtils.toCRLF(responseParams);
//                String[] resPackets = responseParams.split("\r\n\r\n");
//                String resHeader = resPackets[0];
//                String resBody = resPackets.length > 2 ? resPackets[1] : null;
//                LinkedMultiValueMap<String, String> resHeaderMultiMp = HTTPUnpack.handleHeaders(resHeader);
//                res.setResponseHeaders(reqHeaderMultiMp);
//                if (!StringUtils.isEmpty(resBody)) {
//                    res.setResponseBody(parametersFormat(resHeaderMultiMp, resBody));
//                }
//            }
            return null;
        }

        @Override
        protected InterfaceDTO doBackward(@Nonnull BaseInterface anBaseInterface) {
            return null;
        }

        private Object parametersFormat(LinkedMultiValueMap<String, String> multiHeader, String requestParameters) {
            MediaType contentType = new HttpHeaders(multiHeader).getContentType();
            Function<String, Object> bodyHandler;
            String baseType = "x-www-form-urlencoded";
            Charset charset = StandardCharsets.UTF_8;
            if (contentType != null) {
                baseType = contentType.getSubtype();
                charset = contentType.getCharset() == null ? charset : contentType.getCharset();
            }
            try {
                requestParameters = URLDecoder.decode(requestParameters, charset.toString());
            } catch (UnsupportedEncodingException e) {
                throw new AiiException("格式化异常，参数无法进行转码");
            }
            switch (baseType) {
                case "x-www-form-urlencoded": {
                    bodyHandler = HTTPUnpack.handleFormBody;
                    break;
                }
                case "json": {
                    bodyHandler = HTTPUnpack.handleJSONBody;
                    break;
                }
                default:
                    throw new AiiException("还没有该类型的请求体处理器");
            }
            return bodyHandler.apply(requestParameters);
        }
    }
}
