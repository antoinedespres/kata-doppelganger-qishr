package info.dmerej;

import info.dmerej.mailprovider.SendMailRequest;
import info.dmerej.mailprovider.SendMailResponse;
import jdk.jfr.Frequency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        // TODO: write a test to demonstrate the bug in MailSender.sendV2()
    }
}
