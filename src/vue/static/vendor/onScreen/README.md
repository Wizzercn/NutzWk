onScreen
========

A jQuery plugin that does stuff when the matched elements are visible (as inside the viewport)

The plugin works something like this:
```JavaScript
$('elements').onScreen({
   container: window,
   direction: 'vertical',
   doIn: function() {
     // Do something to the matched elements as they come in
   },
   doOut: function() {
     // Do something to the matched elements as they get off scren
   },
   tolerance: 0,
   throttle: 50,
   toggleClass: 'onScreen',
   lazyAttr: null,
   lazyPlaceholder: 'someImage.jpg',
   debug: false
});
```
---

Download
--------

[Download the compressed (production) version](https://raw.github.com/silvestreh/onScreen/master/jquery.onscreen.min.js).

[Download the uncompressed (development) version](https://raw.github.com/silvestreh/onScreen/master/jquery.onscreen.js).

You can checkout the demos [here](http://silvestreh.github.io/onScreen/). And you can download them [here](https://github.com/silvestreh/onScreen/archive/gh-pages.zip).

Bower
-----
onScreen is available as a bower package. Just run `bower install onScreen` and you're set.

---

Options
-------

####**container**: `string`
Tells onScreen() to track elements inside a scrollable element.<br>
_default_: `window` (without quotes)<br>

####**direction**: `string`
Tells the plugin to work in `horizontal` or `vertical` mode.<br>
_default_: `vertical`<br>

####**doIn**: `function`
Is executed whenever the matched elements enter the viewport.<br>
_default_: `null`<br>

####**doOut**: `function`
Is executed whenever the matched elements leave the viewport.<br>
_default_: `null`<br>

####**tolerance**: `integer`
The `doIn()` method will be executed when the matched element is `N` pixels inside the viewport.<br>
_default_: `0`<br>

####**throttle**: `integer`
Throttle delay. Throttles calculation callback, so it will executed no more than specified delay ms.<br>
_default_: `null`<br>

####**toggleClass**: `string`
Tells the plugin to add a specified class when the elements enter the viewport and remove it when they leave.<br>
_default_: `null`<br>

####**lazyAttr**: `string`
onScreen will look for this attribute on `<img>` tags and replace the `src` attribute with this one's.<br>
_default_: `null`<br>

####**lazyPlaceholder**: `string`
Image to display while loading. This is applied through CSS as the background of the matched elements.<br>
_default_: A base64 encoded gif file.<br>

####**debug**: `boolean`
Spams your console with information about the matched elements and the scroll container.<br>
_default_: `false`

####**remove**: `string`
Detach the event listener. You have to use this on the container element onScreen is attached to. Eg: If onScreen is attached to the window (the default behavior) you should use `$(window).onScreen('remove')` and onScreen will be removed.
