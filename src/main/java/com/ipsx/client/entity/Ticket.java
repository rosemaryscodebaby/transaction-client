package com.ipsx.client.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    private String id;
    private String groupId;
    private String value;

    public String formattedTicket(){
        return String.format("groupId:%s\nticketId:%s\n%s\n----------------\n", groupId, id, value);
    }
}

