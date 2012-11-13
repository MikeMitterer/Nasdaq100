Nasdaq100
=========
This App shows you the top Nasdaq100 companies with their logo's. If you select one of these companies it
shows you it's Wikipedia entry. Another option is to display a chart image of the last 3 month.

![Screenshot](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/screenshots/images/screenshots-collage.png)

Donate
======
If you like my work, and if you want me to write a complete tutorial for this App, please
support my work by donating some money.
 
<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_s-xclick">
<input type="hidden" name="hosted_button_id" value="2ZXA2DPD8XQYE">
<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG_global.gif" border="0" name="submit" alt="PayPal — The safer, easier way to pay online.">
<img alt="" border="0" src="https://www.paypalobjects.com/de_DE/i/scr/pixel.gif" width="1" height="1">
</form>

*Thank you!*

Intention / OpenSource
======================
On reason to OpenSource this App was to show a real live Application with Fragments, ActionBarSherlock,
Roboguice, targeting different Android versions, different devices (N1, Tablet aso.) and AdMob in one place. 
It's also a tribute to all the OpenSource stuff I use.

Development / Settings
============
It should be easy to setup for you. The only tricky part is to add ActionBarSherlock.
Under [doc/dependency/][13] I have added the ActionBarSherlock version I use.
These screenshots should help to setup the project for you:

In Eclipse:<br>
![In Eclipse](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/screenshots/images/eclipse.png)

On Harddisk:<br>
![On Harddisk](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/screenshots/images/onhd.png)

Settings for ActionBarSherlock:<br>
![Settings for ActionBarSherlock](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/screenshots/images/settings-actionbarsherlock.png)

Settings for Nasdaq100:<br>
![Settings for Nasdaq100](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/screenshots/images/settings-nasdaq100.png)

Dependencies
============
- [ActionBarSherlock][1]
- [Android-Universal-Image-Loader][2] 
- [Roboguice-Sherlock][3]
- [Roboguice][4]
- [Roboguice][5] (GitHub)

License
========
- All dependencies are under the licence set by their respective owners
- The project makes use of icons released in the [AndroidIcons project][6], and are released under their respective licenses
- The [application icon][7] and the [background image][8] are based on files from Fotolia and protected by their respective licenses
- The project makes use of icons released in the [www.defaulticon.com project][9], and are released under their respective licenses

    Copyright 2012 Michael Mitterer, IT-Consulting and Development Limited,
    Austrian Branch

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Scripts
========
#### genAndroidIcons
If you use [www.defaulticon.com][9] icons in your Android project it's very easy to generate your own iconsets for Android.
(At least on Linux or Mac OS)
	
check out the [script][11] in doc/bin/genAndroidIcons. (Don't forget to chmod 700 doc/bin/genAndroidIcons)
(Tested on Mac OSX 10.8.2, should also work on Linux. Sorry Windows-User, no script for you)
[Here][12] you can see one of the preview-files the script generates.
The preview.png was generated with a border and a shadow so that it's easier do see the thumbnail dimensions.
	
A possible file structure the script generates:

![Screenshot](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/defaulticon/my-structure.png)

#### tagdev
If you have more than one physical device connected to your PC and if you want
to use the command-line with ant you have to specify the serial number of your target device to help ant (+ adb)
to copy the apk to the right location.
[tagdev][14] should help.

Sample:
- $ doc/bin/tagdev --list
- $ . doc/bin/tagdev --alias device1
- $ ant installr

And agin - sorry dear Windows-User. Maybe [Cygwin][15] can help...

[1]: https://github.com/JakeWharton/ActionBarSherlock
[2]: https://github.com/nostra13/Android-Universal-Image-Loader
[3]: https://github.com/rtyley/roboguice-sherlock
[4]: http://code.google.com/p/roboguice/
[5]: https://github.com/emmby/roboguice
[6]: http://www.androidicons.com/
[7]: http://de.fotolia.com/id/38423697
[8]: http://de.fotolia.com/id/40304666
[9]: http://www.defaulticon.com/
[10]: http://developer.android.com/guide/practices/ui_guidelines/icon_design_menu.html
[11]: https://github.com/MikeMitterer/Nasdaq100/tree/master/doc/bin/genAndroidIcons
[12]: https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/defaulticon/preview.png
[13]: https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/dependency
[14]: https://github.com/MikeMitterer/Nasdaq100/tree/master/doc/bin/tagdev
[15]: http://www.cygwin.com/
