package assessment.manager.opt.mailRequest;

import assessment.manager.opt.mailRequest.mail.Recipient;
import assessment.manager.opt.mailRequest.mail.SendEmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
public class MailServiceImpl implements MailService {
//    @Value( "${mail.api.key}" )
//    private String apiKey;
//    @Value( "${brevo.mail.url}" )
//    private String mailUrl;

    @Override
    public String sendMail( String email, String subject, String htmlContent) {
//        SendEmailRequest sendEmailRequest = new SendEmailRequest();
//        Recipient recipient = new Recipient( email );
//        sendEmailRequest.setSubject( subject );
//        sendEmailRequest.setContent( htmlContent );
//        sendEmailRequest.getRecipients().add( recipient );
//
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.set( "api-key", apiKey );
//            httpHeaders.set( "accept", APPLICATION_JSON_VALUE );
//            RequestEntity<SendEmailRequest> requestEntity = new RequestEntity<>( sendEmailRequest, httpHeaders, HttpMethod.POST, URI.create( mailUrl ) );
//            log.info( ":::::::: EMAIL SENT SUCCESSFULLY ::::::::::::" );
//        } catch (Exception e) {
//            throw new RuntimeException( "Error sending email" );
//        }
        return null;
    }
}
