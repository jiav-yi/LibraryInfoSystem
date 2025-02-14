package com.jnu.utils;

import cn.hutool.core.util.StrUtil;
import com.jnu.commmon.dto.MsgDTO;
import com.jnu.component.BigModelStreamClient;
import com.jnu.config.BigModelConfig;
import com.jnu.listener.BigModelWebSocketListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class BigModelUtil {
    @Resource
    private BigModelStreamClient bigModelStreamClient;

    @Resource
    private BigModelConfig bigModelConfig;

    /**
     * 发送问题
     * @param question 问题
     * @return 星火大模型的回答
     */
    public String sendQuestion(String question) {
        // 如果是无效字符串，则不对大模型进行请求
        if (StrUtil.isBlank(question)) {
            return "无效问题，请重新输入";
        }
        // 获取连接令牌
        if (!bigModelStreamClient.operateToken(BigModelStreamClient.GET_TOKEN_STATUS)) {
            return "当前大模型连接数过多，请稍后再试";
        }

        // 创建消息对象
        MsgDTO msgDTO = MsgDTO.createUserMsg(question);
        // 创建监听器
        BigModelWebSocketListener listener = new BigModelWebSocketListener();
        // 发送问题给大模型，生成 websocket 连接
        WebSocket webSocket = bigModelStreamClient.sendMsg(UUID.randomUUID().toString().substring(0, 10), Collections.singletonList(msgDTO), listener);
        if (webSocket == null) {
            // 归还令牌
            bigModelStreamClient.operateToken(BigModelStreamClient.BACK_TOKEN_STATUS);
            return "系统内部错误，请联系管理员";
        }
        try {
            int count = 0;
            // 为了避免死循环，设置循环次数来定义超时时长
            int maxCount = bigModelConfig.getMaxResponseTime() * 5;
            while (count <= maxCount) {
                Thread.sleep(200);
                if (listener.isWsCloseFlag()) {
                    break;
                }
                count++;
            }
            if (count > maxCount) {
                return "大模型响应超时，请联系管理员";
            }
            // 响应大模型的答案
            return listener.getAnswer().toString();
        } catch (InterruptedException e) {
            log.error("错误：" + e.getMessage());
            return "系统内部错误，请联系管理员";
        } finally {
            // 关闭 websocket 连接
            webSocket.close(1000, "");
            // 归还令牌
            bigModelStreamClient.operateToken(BigModelStreamClient.BACK_TOKEN_STATUS);
        }
    }

    /**
     * 清洗大模型返回内容
     */
    public String cleanAiResponse(String rawResponse) {
        // 去除Markdown代码块标识
        String cleaned = rawResponse.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        return cleaned;
    }
}
