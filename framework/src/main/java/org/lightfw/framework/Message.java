package org.lightfw.framework;

import lombok.Data;

import java.io.Serializable;

/**
 * Main runtime class.
 */
@Data
public class Message implements Serializable {

    private final String id;
    private final String body;
}
