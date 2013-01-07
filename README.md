Nasdaq100
=========
This app displays the top Nasdaq100 companies with their accompanying logo's. 
Users can simply select one of these companies to be redirected to the organization’s Wikipedia entry. 
Additionaly they can opt to view the company’s chart over the last 3 months

![Screenshot](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/screenshots/images/screenshots-collage.png)

WebSite + Google Play
======================
<table border=0 style="border: none">
	<tr>
		<td>
			<a href="#" target="_blank">
				<img src="https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/images/icon.png">
			</a>
		</td>
		<td>
			<a href="#" target="_blank">
				<img src="https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/images/googleplay.png">
			</a>
		</td>
	</tr>
</table>

![Icon](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/images/icon.png)
[www.nasdaq100.at](http://www.nasdaq100.at/)<br /> 
German only, englisch will come in the next few weeks

![Google Play](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/images/googleplay.png)
[Google Play](https://play.google.com/store/apps/details?id=at.mikemitterer.nasdaq100)

Intention / OpenSource
======================
One reason to OpenSource this app was to demonstrate a real live application with Fragments, ActionBarSherlock and
Roboguice, that targets different versions of Android, different devices (N1, Tablet aso.) and AdMob, all in one place. 
The application was also developed as a tribute to all the OpenSource material that I have benefited from in the past.

Development / Settings
============
It should be easy to setup for you. The only tricky part is adding the ActionBarSherlock part. 
Under [doc/dependency/][13] I have added a link to the version of ActionBarSherlock that I use. 
The screenshots below should guide you through the rest of the setup process:

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
- All dependencies are under the license set by their respective owners
- The project makes use of icons released in the [AndroidIcons project][6], and are released under their respective licenses
- The [application icon][7] and the [background image][8] are based on files from Fotolia and are protected by their respective licenses
- The project makes use of icons that were released in the [www.defaulticon.com project][9], and are released under their respective licenses

    Copyright 2012 Michael Mitterer, IT-Consulting and Development Limited,
    Austrian Branch

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, 
    software distributed under the License is distributed on an 
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
    either express or implied. See the License for the specific language 
    governing permissions and limitations under the License.
    

Scripts
========
#### genAndroidIcons
If you use [www.defaulticon.com][9] icons in your Android project it's very easy to generate your own iconsets for Android
(At least on Linux or Mac OS).
	
Check out the [script][11] in doc/bin/genAndroidIcons. (Don't forget to chmod 700 doc/bin/genAndroidIcons)
(Tested on Mac OSX 10.8.2, should also work on Linux. Sorry Windows user, there is no script for you)
[Here][12] you can see one of the preview files that the script generates.
The preview.png was generated with a border and a shadow so that it's easier to see the thumbnail dimensions.
	
Below you can see a screenshot of a possible file structure the script generates:

![Screenshot](https://github.com/MikeMitterer/Nasdaq100/raw/master/doc/defaulticon/my-structure.png)

#### tagdev
If you have more than one physical device connected to your PC and if you want
to use the command-line with ant, you need to specify the serial number of your target device to help ant (+ adb)
to copy the apk to the correct location.
[tagdev][14] should help you to do this.

Sample:
- $ doc/bin/tagdev --list
- $ . doc/bin/tagdev --alias device1
- $ ant installr

And again - sorry dear Windows user. Maybe [Cygwin][15] can help...

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
