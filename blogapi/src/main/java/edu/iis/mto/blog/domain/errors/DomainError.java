package edu.iis.mto.blog.domain.errors;

public class DomainError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String USER_NOT_FOUND = "unknown user";
    public static final String POST_NOT_FOUND = "unknown post";
    public static final String SELF_LIKE = "cannot like own post";
    public static final String USER_NOT_CONFIRMED = "user status has to be confirmed in order to like post";
    public static final String USER_STATUS_REMOVED = "User has been removed";

    public DomainError(String msg) {
        super(msg);
    }

}
