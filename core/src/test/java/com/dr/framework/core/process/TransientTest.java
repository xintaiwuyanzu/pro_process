package com.dr.framework.core.process;

import com.dr.framework.core.process.bo.ProcessTypeProviderWrapper;
import com.dr.framework.core.process.service.impl.DefaultProcessTypeProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 测试transient关键字序列化
 *
 * @author dr
 */
public class TransientTest {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ProcessTypeProviderWrapper wrapper = new ProcessTypeProviderWrapper(new DefaultProcessTypeProvider());
        System.out.println(objectMapper.writeValueAsString(wrapper));
    }
}
