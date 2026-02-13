package util;
public interface Constants {
    public static final String CURRENT_DIR = System.getProperty("user.dir");
    public static final String CONFIG_DIR=Constants.CURRENT_DIR+ "/config";
    public static final String REPLICA="replica";
    public static final String PARTITIONS="partition";
    public static final String PAYLOAD_TYPE_STRING="String";
    public static final String PAYLOAD_TYPE_BYTE="Byte";
    public static final String PAYLOAD_TYPE_BYTEARRAY ="ByteArray";
}
