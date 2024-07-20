package com.carol.remote.transport.socket;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MyMessage implements Serializable {
    private String content;
}
