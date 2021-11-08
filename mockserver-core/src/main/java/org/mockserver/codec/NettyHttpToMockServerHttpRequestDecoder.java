package org.mockserver.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.mappers.FullHttpRequestToMockServerHttpRequest;
import org.mockserver.model.HttpRequest;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author jamesdbloom
 */
public class NettyHttpToMockServerHttpRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {

    private final FullHttpRequestToMockServerHttpRequest fullHttpRequestToMockServerRequest;

    public NettyHttpToMockServerHttpRequestDecoder(MockServerLogger mockServerLogger, boolean isSecure, Integer port) {
        fullHttpRequestToMockServerRequest = new FullHttpRequestToMockServerHttpRequest(mockServerLogger, isSecure, port);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, List<Object> out) {
        final HttpRequest httpRequest = fullHttpRequestToMockServerRequest.mapFullHttpRequestToMockServerRequest(fullHttpRequest);
        if (ctx != null && ctx.channel() != null && ctx.channel().remoteAddress() != null
            && ctx.channel().remoteAddress() instanceof InetSocketAddress) {
            httpRequest.withClientAddress(org.mockserver.model.SocketAddress.socketAddress()
                .withHost(((InetSocketAddress) ctx.channel().remoteAddress()).getHostName())
                .withPort(((InetSocketAddress) ctx.channel().remoteAddress()).getPort()));
        }
        out.add(httpRequest);
    }

}
