package net.salah.locate;

import java.io.Serializable;

/**
 * Created by Salah Nahed  on 4/13/17.
 */

class LocationConfig implements Serializable {
     boolean debug = false;
     Locate.Priority priority = Locate.Priority.PRIORITY_HIGH_ACCURACY;
     long interval = 2000;
     long fastestInterval = interval / 2;



}
