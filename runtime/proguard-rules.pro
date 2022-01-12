-keep class com.edwin.annotations.*

-keep @com.edwin.annotations.Generated public class ** {
    public static void inject(android.app.Activity, android.os.Bundle);
    public static void saveState(android.app.Activity, android.os.Bundle);
}
