# btcclock [![Build Status](https://travis-ci.org/lvaccaro/btcclock.svg?branch=master)](https://travis-ci.org/lvaccaro/btcclock)
[![download](http://style.anu.edu.au/_anu/images/icons/icon-google-play.png)](https://play.google.com/store/apps/details?id=com.eternitywall.btcclock)

Bitcoin modular Android Widget Clock.

## Define your new clock
* Go inside the package
```
com.eternitywall.bbtcclock.clocks
```
* Create a new Class file that extends Clock.
```
public class StandardClock extends Clock {
```
* Build the constructor with follows params: unique id, description, image resource file.
```
    public StandardClock() {
        super(0, "Standard time", R.drawable.clock);
    }
```
* Run the clock behaviour and callback the parent method this.updateListener.callback() with the follows params: context, big widget text line, bottom widget text line, image resource file.
```
    public void run(final Context context, final int appWidgetId) {
        new Runnable() {
            @Override
            public void run() {
                Calendar mCalendar = Calendar.getInstance();
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                CharSequence time = DateFormat.format("HH:mm", mCalendar);
                CharSequence description = DateFormat.format("d MMM yyyy", mCalendar);
                StandardClock.this.updateListener.callback(context, appWidgetId, time.toString(), description.toString(), StandardClock.this.resource);
            }
        }.run();
    }
}
```
* Add the new clock file inside the list of clocks : com.eternitywall.btcclock.clocks
```
private static Clock[] clocks = new Clock[]{
            new BitcoinClock(),
            new BitcoinGithubClock(),
            new StandardClock(),
            new NasaClock(),
            new TimestampClock(),
            new SpaceXClock()
    };
```

## App Screenshots
![App Screenshot](https://raw.githubusercontent.com/eternitywall/btcclock/master/resources/Screenshot_1507475354.png)
