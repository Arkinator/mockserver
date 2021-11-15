package org.mockserver.socket.tls;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.AfterClass;
import org.junit.Test;
import org.mockserver.logging.MockServerLogger;
import org.mockserver.socket.tls.bouncycastle.BCKeyAndCertificateFactory;

public class CustomKeyAndCertificateFactorySupplierTest {

    @Test
    public void setSupplier_shouldUseSupplier() {
        KeyAndCertificateFactory factoryInstance = new BCKeyAndCertificateFactory(null);
        KeyAndCertificateFactoryFactory.setCustomKeyAndCertificateFactorySupplier((logger, isServer) -> factoryInstance);

        assertTrue("Should give exact instance",
            KeyAndCertificateFactoryFactory.createKeyAndCertificateFactory(null)
                == factoryInstance);
    }

    @Test
    public void setServerModifier_shouldBeCalled() {
        final AtomicBoolean customizerCallFlag = new AtomicBoolean(false);
        NettySslContextFactory.sslServerContextBuilderCustomizer = builder -> {
            customizerCallFlag.set(true);
            return builder;
        };

        new NettySslContextFactory(mock(MockServerLogger.class), true)
            .createServerSslContext();

        assertTrue(customizerCallFlag.get());
    }

    @Test
    public void setClientModifier_shouldBeCalled() {
        final AtomicBoolean customizerCallFlag = new AtomicBoolean(false);
        NettySslContextFactory.sslClientContextBuilderCustomizer = builder -> {
            customizerCallFlag.set(true);
            return builder;
        };

        new NettySslContextFactory(mock(MockServerLogger.class), false)
            .createClientSslContext(false);

        assertTrue(customizerCallFlag.get());
    }

    @AfterClass
    public static void resetSupplier() {
        KeyAndCertificateFactoryFactory.setCustomKeyAndCertificateFactorySupplier(null);
    }
}
