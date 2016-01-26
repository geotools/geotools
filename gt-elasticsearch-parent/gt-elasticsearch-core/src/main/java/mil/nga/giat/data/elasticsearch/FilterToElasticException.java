/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

public class FilterToElasticException extends RuntimeException {

    private static final long serialVersionUID = 1819999351118120451L;

    public FilterToElasticException(String message) {
        super(message);
    }

    public FilterToElasticException(String msg, Throwable exp) {
        super(msg, exp);
    }
}
