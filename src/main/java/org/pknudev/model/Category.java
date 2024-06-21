package org.pknudev.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Category implements Serializable {
    private int id;
    private String name;
    private int order;
}
