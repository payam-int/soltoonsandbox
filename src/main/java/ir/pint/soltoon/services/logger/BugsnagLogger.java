package ir.pint.soltoon.services.logger;

import com.bugsnag.Bugsnag;
import org.springframework.stereotype.Service;

@Service
public class BugsnagLogger implements ExceptionLogger {

    Bugsnag bugsnag = new Bugsnag("211d3337ac1232df46778db557537339");


    @Override
    public void log(Throwable throwable) {
        bugsnag.notify(throwable);
    }
}
