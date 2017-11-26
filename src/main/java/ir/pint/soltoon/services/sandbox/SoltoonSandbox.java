package ir.pint.soltoon.services.sandbox;

import ir.pint.soltoon.services.soltoonServices.JobInfo;
import ir.pint.soltoon.services.soltoonServices.JobOptions;

import java.io.InputStream;

public interface SoltoonSandbox {
    SourceInfo addSource(InputStream code, SourceOptions options);
    boolean sourceExists(String id);
    JobInfo startJob(String service, JobOptions options);
    boolean jobExists(String id);
    JobInfo getJob(String id);
}
