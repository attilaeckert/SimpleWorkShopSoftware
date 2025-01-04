package com.simpleworkshopsoftware.base;
/**
 * This interface defines a generic contract for classes that hold an identifier (ID).
 * Besides it used as a wrapper class, to make the repository implementations easier.
 * @author Attila Eckert
 * @date 12/27/2024
 * @version 1.0
 */
public interface IdHolder<ID> {
    ID getId();
}
