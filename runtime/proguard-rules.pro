-keep class com.edwin.annotations.*

-keep @com.edwin.annotations.Generated public class ** {
    public static void inject(android.app.Activity, android.os.Bundle);
    public static void saveState(android.app.Activity, android.os.Bundle);
}

-keep @com.edinw.annotations.Generated public class ** {
      public static void inject(android.support.v4.app.Fragment, android.os.Bundle);
      public static void saveState(android.support.v4.app.Fragment, android.os.Bundle);
}

-keep public class android.support.v4.app.Fragment {
    java.lang.String mWho;
}