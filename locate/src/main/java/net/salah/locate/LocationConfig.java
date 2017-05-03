package net.salah.locate;

import java.io.Serializable;

/**
 * Created by Salah Nahed  on 4/13/17.
 */

public class LocationConfig implements Serializable {
     boolean debug = false;
     Locate.Priority priority = Locate.Priority.PRIORITY_BALANCED_POWER_ACCURACY;
     long interval = 2000;
     long fastestInterval = interval / 2;



}
