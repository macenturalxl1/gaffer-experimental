/*
 * Copyright 2020 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.gaffer.gaas.client;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.gov.gchq.gaffer.gaas.exception.GaaSRestApiException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static uk.gov.gchq.gaffer.gaas.utilities.ApiExceptionTestFactory.makeApiException_timeout;

@SpringBootTest
public class CRDClientTest {

    @MockBean
    private CoreV1Api coreV1Api;

    @Autowired
    private CRDClient crdClient;

    @Test
    public void getAllNameSpaces_ShouldThrowGaaSRestApiException_WhenCustomApiThrowsApiEx() throws ApiException, GaaSRestApiException {
        final ApiException apiException = makeApiException_timeout();
        when(coreV1Api.listNamespace("true", null, null, null, null, 0, null, null, Integer.MAX_VALUE, Boolean.FALSE))
                .thenThrow(apiException);

        final GaaSRestApiException exception = assertThrows(GaaSRestApiException.class, () -> crdClient.getAllNameSpaces());

        assertEquals("java.net.SocketTimeoutException: connect timed out", exception.getMessage());
    }
}