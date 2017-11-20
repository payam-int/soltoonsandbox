package ir.pint.soltoon.services.security;

public interface SecurityService {
    boolean isRequestAllowed(String request, int scale);

    boolean isRequestAllowed(String request);

    boolean processRequest(String request, int scale);

    boolean processRequest(String request);


}
