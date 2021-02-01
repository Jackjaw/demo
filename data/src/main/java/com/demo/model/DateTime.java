package com.demo.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data
public class  DateTime implements Serializable {
    String timezone;
    String datetime;
}
