package com.thomax.ast.model2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferTableInfo {

    private static final String PROPERTY_TOPIC_TABLE = "PROPERTY_TOPIC";

    private static final String EVENT_TOPIC_TABLE = "EVENT_TOPIC";

    private static final String DEVICE_DETAIL_TABLE = "DEVICE_DETAIL";

    public static final String DEVICE_ID_COLUMN = "deviceId";
    public static final String GROUP_NAME_COLUMN = "groupName";

    private static final Map<String, List<String>> TABLE_INFO;

    static {
        TABLE_INFO = new HashMap<>();

        List<String> topicProp = new ArrayList<>();
        topicProp.add("dataTimeStamp");
        topicProp.add("tslType");
        topicProp.add("macAddress");
        topicProp.add("deviceId");
        topicProp.add("productId");
        topicProp.add("command");
        TABLE_INFO.put(PROPERTY_TOPIC_TABLE, topicProp);
        TABLE_INFO.put(EVENT_TOPIC_TABLE, topicProp);

        List<String> deviceDetailProp = new ArrayList<>();
        deviceDetailProp.add(DEVICE_ID_COLUMN);
        deviceDetailProp.add(GROUP_NAME_COLUMN);
        TABLE_INFO.put(DEVICE_DETAIL_TABLE, deviceDetailProp);
    }

    public static Map<String, List<String>> getTableInfo() {
        return TABLE_INFO;
    }

    public static boolean isTopicTable(String table) {
        return PROPERTY_TOPIC_TABLE.equals(table) || EVENT_TOPIC_TABLE.equals(table);
    }

    public static boolean isDeviceDetailTable(String table) {
        return DEVICE_DETAIL_TABLE.equals(table);
    }

    public static boolean isDeviceDetailOfDeviceId(String column) {
        return DEVICE_ID_COLUMN.equals(column);
    }

}
