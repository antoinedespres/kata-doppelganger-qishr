package info.dmerej;

import info.dmerej.mailprovider.SendMailRequest;
import info.dmerej.mailprovider.SendMailResponse;
import jdk.jfr.Frequency;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

class MockHttpClient implements HttpClient{
    int code;
    private Object sendMailRequest;

    public MockHttpClient(int code) {
        this.code = code;
    }

    public SendMailResponse post(String url, Object request) {
        sendMailRequest = request;
        return new SendMailResponse(this.code, "OK");
    }

    public Object getSendMailRequest(){
        return sendMailRequest;
    }
}


public class MailSenderTest {

    @Test
    void should_make_a_valid_http_request() {

        User user = new User("michel", "michel@efrei.net");
        MockHttpClient mockHttpClient = new MockHttpClient(200);
        MailSender mailSender = new MailSender(mockHttpClient);
        SendMailRequest sendMailRequest = new SendMailRequest("michel@efrei.net","New notification","message1");

        mailSender.sendV1(user,"message1");

        assertEquals(sendMailRequest, mockHttpClient.getSendMailRequest());
    }

    @Test
    void should_retry_when_getting_a_503_error() {
        User user = new User("michel", "michel@efrei.net");
        MockHttpClient mockHttpClient = new MockHttpClient(503);
        MailSender mailSender = new MailSender(mockHttpClient);
        SendMailRequest sendMailRequest = new SendMailRequest("michel@efrei.net","New notification","message1");

        mailSender.sendV2(user,"message1");

        assertEquals(sendMailRequest, mockHttpClient.getSendMailRequest());
    }

    @Test
    void should_make_a_valid_http_request_mockito() {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.post(Mockito.anyString(), Mockito.any())).thenReturn(new SendMailResponse(200, "OK"));
        MailSender mailSender = new MailSender(mockHttpClient);
        SendMailRequest sendMailRequest = new SendMailRequest("michel@efrei.net","New notification","message1");
        User user = new User("michel", "michel@efrei.net");

        List<Object> listRequest = new ArrayList<>();
        List<Object> spyRequest = spy(listRequest);
        doAnswer(invocationOnMock -> {
            spyRequest.add(invocationOnMock.getArgument(1));
            return null;
        }).when(mockHttpClient).post(any(String.class),any(Object.class));

        mailSender.sendV1(user,"message1");

        assertEquals(sendMailRequest, spyRequest.get(0));
    }

    @Test
    void should_retry_when_getting_a_503_error_mockito() {
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.post(Mockito.anyString(), Mockito.any())).thenReturn(new SendMailResponse(503, "Error"));
        MailSender mailSender = new MailSender(mockHttpClient);
        SendMailRequest sendMailRequest = new SendMailRequest("michel@efrei.net","New notification","message1");
        User user = new User("michel", "michel@efrei.net");

        //assertEquals(sendMailRequest, mockHttpClient.getSendMailRequest());

        List<Object> listRequest = new ArrayList<>();
        List<Object> spyRequest = spy(listRequest);
        doAnswer(invocationOnMock -> {
            spyRequest.add(invocationOnMock.getArgument(1));
            return null;
        }).when(mockHttpClient).post(any(String.class),any(Object.class));

        mailSender.sendV2(user,"message1");

        assertEquals(sendMailRequest, spyRequest.get(0));
    }
}
