package net.salah.locate;

import android.support.annotation.NonNull;

/**
 * Created by Salah Nahed  on 4/13/17.
 */
interface LocationBuilderBase {

    Locate.LocationBuilder debuggingMode(boolean debug);
    Locate.LocationBuilder setPriority(Locate.Priority priority);
    Locate.LocationBuilder setInterval(long interval);
    Locate.LocationBuilder setFastInterval(long fastInterval);
    Locate.LocationBuilder addLocationResultListener(@NonNull Locate.LocationResult listener);
    Locate build();

}
