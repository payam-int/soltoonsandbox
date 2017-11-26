package ir.pint.soltoon.services.docker.dockerTask;

public class DockerBind {
    private String from;
    private String to;

    public DockerBind() {
    }

    public DockerBind(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String toBindString() {
        return String.format("%s:%s", getFrom(), getTo());
    }
}
