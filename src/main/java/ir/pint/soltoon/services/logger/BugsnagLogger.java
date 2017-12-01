package ir.pint.soltoon.services.logger;

import com.bugsnag.Bugsnag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BugsnagLogger implements ExternalExceptionLogger {

    @Value("${bugsnag.apiKey}")
    private String apiKey;

    Bugsnag bugsnag;

    @PostConstruct
    private void init() {
        bugsnag  = new Bugsnag(apiKey);
    }


    @Override
    public void log(Throwable throwable) {
        bugsnag.notify(throwable);
    }
}
