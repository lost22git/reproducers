package me.kec.se.quickstart.gh5273;

import java.io.IOException;
import java.io.OutputStream;

import io.helidon.common.GenericType;
import io.helidon.common.http.Headers;
import io.helidon.common.http.WritableHeaders;
import io.helidon.nima.http.media.EntityWriter;
import io.helidon.nima.http.media.spi.MediaSupportProvider;

import static io.helidon.common.media.type.MediaTypes.APPLICATION_JSON;
import static io.helidon.nima.http.media.spi.MediaSupportProvider.SupportLevel.COMPATIBLE;
import static java.lang.System.Logger.Level.TRACE;

public class JacksonSupportProvider implements MediaSupportProvider {

    private static final System.Logger LOGGER = System.getLogger(JacksonSupportProvider.class.getName());

    <T> boolean checkSupported(GenericType<T> type,
                               Headers requestHeaders,
                               WritableHeaders<?> responseHeaders) {
        if (responseHeaders != null
                && responseHeaders.contentType().filter(v -> v.test(APPLICATION_JSON)).isPresent()) {
            return true;
        }
        return requestHeaders != null && requestHeaders.acceptedTypes().contains(APPLICATION_JSON);
    }

    @Override
    public <T> WriterResponse<T> writer(GenericType<T> type,
                                        WritableHeaders<?> requestHeaders) {

        return writer(type, requestHeaders, null);
    }

    @Override
    public <T> WriterResponse<T> writer(GenericType<T> type,
                                        Headers requestHeaders,
                                        WritableHeaders<?> responseHeaders) {
        LOGGER.log(TRACE, " writer for type=" + type.getTypeName());

        if (checkSupported(type, requestHeaders, responseHeaders)) {
            //noinspection unchecked
            return new WriterResponse<>(COMPATIBLE, () -> (EntityWriter<T>) JacksonEntityWriter.INSTANCE);
        }
        return WriterResponse.unsupported();
    }

    static class JacksonEntityWriter<T> implements EntityWriter<T> {

        private static final JacksonEntityWriter<?> INSTANCE = new JacksonEntityWriter<>();

        @Override
        public void write(GenericType<T> type,
                          T object,
                          OutputStream outputStream,
                          Headers requestHeaders,
                          WritableHeaders<?> responseHeaders) {
            try {
                Main.JSON.writeValue(outputStream, object);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void write(GenericType<T> type,
                          T object,
                          OutputStream outputStream,
                          WritableHeaders<?> headers) {
            write(type, object, outputStream, headers, null);
        }
    }
}