package me.tastycake.hackathon_2.mysql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Types;

@Getter
@AllArgsConstructor
public class ResultObject {
    private Object value;
    private String columnName;
    private int columnIndex;
    private int type;
    private boolean isSerializable;
    private boolean isList;
}
