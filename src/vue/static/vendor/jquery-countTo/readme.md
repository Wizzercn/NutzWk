jQuery countTo Plugin
=====================
[jQuery countTo](https://github.com/mhuggins/jquery-countTo) is a
[jQuery](http://jquery.com) plugin that will count up (or down) to a target
number at a specified speed, rendered within an HTML DOM element.

Requirements:
-------------
[jQuery countTo](https://github.com/mhuggins/jquery-countTo) requires the
latest version of [jQuery](http://jquery.com).

Usage:
------
There are two main approaches to using this plugin: through data attributes on
DOM nodes, and through JS options explicitly passed to the `countTo` function.

These two methods can be mixed and matched as well.  Data attributes takes
precedence over JS options.

### Data Attributes

This approach allows you to define `data-*` attributes on whatever DOM element
will act as a container for your counter.  This is useful when you already know
the values at the time that you are constructing the DOM.

    <span class="timer" data-from="25" data-to="75"></span>
    
    <script type="text/javascript"><!--
        $('.timer').countTo();
    //--></script>

A more detailed example that demonstrates all possible options being used is as
follows.

    <span class="timer" data-from="0" data-to="100"
          data-speed="5000" data-refresh-interval="50"></span>
    
    <script type="text/javascript"><!--
        $('.timer').countTo();
    //--></script>

Refer to the **Options** section below for more info on the various options
available.

### JavaScript Options

This approach allows you to pass values to the `countTo` function.  This is
useful when you don't know the values at the time the DOM is being rendered.

    <span class="timer"></span>
    
    <script type="text/javascript"><!--
        $('.timer').countTo({from: 0, to: 500});
    //--></script>

A more detailed example that demonstrates all possible options being used is as
follows.

    <span class="timer"></span>
    
    <script type="text/javascript"><!--
        $('.timer').countTo({
            from: 50,
            to: 2500,
            speed: 1000,
            refreshInterval: 50,
            formatter: function (value, options) {
                return value.toFixed(options.decimals);
            },
            onUpdate: function (value) {
                console.debug(this);
            },
            onComplete: function (value) {
                console.debug(this);
            }
        });
    //--></script>

Refer to the **Options** section below for more info on the various options
available.

Options:
--------
A complete listing of the options that can be passed to the `countTo` method is below.

<table>
  <tr>
    <th>Option</th>
    <th>Data Attribute</th>
    <th>Description</th>
  </tr>
  <tr>
    <td><i>from</i></td>
    <td><i>data-from</i></td>
    <td>The number to start counting from. <i>(default: 0)</i></td>
  </tr>
  <tr>
    <td><i>to</i></td>
    <td><i>data-to</i></td>
    <td>The number to stop counting at. <i>(default: 100)</i></td>
  </tr>
  <tr>
    <td><i>speed</i></td>
    <td><i>data-speed</i></td>
    <td>The number of milliseconds it should take to finish counting.
        <i>(default: 1000)</i></td>
  </tr>
  <tr>
    <td><i>refreshInterval</i></td>
    <td><i>data-refresh-interval</i></td>
    <td>The number of milliseconds to wait between refreshing the counter.
        <i>(default: 100)</i></td>
  </tr>
  <tr>
    <td colspan="2"><i>formatter (value, options)</i></td>
    <td>A handler that is used to format the current value before rendering to
        the DOM.  The true current value and options set is passed to the
        function, and it is run in the context of the DOM element.  It must
        return the formatted value. <i>(default:
        <code>value.toFixed(options.decimal)</code>)</i></td>
  </tr>
  <tr>
    <td colspan="2"><i>onUpdate (value)</i></td>
    <td>A callback function that is triggered for every iteration that the
        counter updates.  The currently rendered value is passed to the
        function, and it is called in the context of the DOM element.
        <i>(default: null)</i></td>
  </tr>
  <tr>
    <td colspan="2"><i>onComplete (value)</i></td>
    <td>A callback function that is triggered when counting finishes.  The
        final rendered value is passed to the function, and it is called in the
        context of the DOM element. <i>(default: null)</i></td>
  </tr>
</table>

Created By:
-----------
[Matt Huggins](http://www.matthuggins.com)

License:
--------
jQuery-countTo is released under the [MIT license](http://www.opensource.org/licenses/MIT).
