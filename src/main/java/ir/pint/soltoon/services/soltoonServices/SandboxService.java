package ir.pint.soltoon.services.soltoonServices;

public interface SandboxService {
    String getServiceName();

    JobInfo startJob(JobOptions options);

    boolean jobExists(String id);

    JobInfo getJob(String id);
}
