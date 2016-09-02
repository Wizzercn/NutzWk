# jQuery Stepy - A Wizard Plugin - [wbotelhos.com/stepy](http://wbotelhos.com/stepy)

jQuery Stepy is a plugin based on FormToWizard that generates a customizable wizard.

## Version

```
@version        1.1.0
@since          2010-07-03
@author         Washington Botelho
@documentation  wbotelhos.com/stepy
@twitter        twitter.com/wbotelhos
```

## Required Files

+ jquery.stepy.min.js
+ jquery.stepy.css

## Options

```js
back         : undefined  // Callback before the backward action.
backLabel    : '< Back'   // Change the back button label.
block        : false      // Block the next step if the current is invalid.
description  : false      // Choose if the descriptions of the titles will be showed.
duration     : 0          // Duration of the transition between steps in ms.
enter        : true       // Enables the enter key to change to the next step.
errorImage   : false      // If an error occurs, a image is showed in the title of the corresponding step.
finish       : undefined  // Callback before the finish action.
finishButton : true       // Include the button with class called '.finish' into the last step.
ignore       : ''         // Choose the fields to be ignored on validation.
legend       : false      // Choose if the legends of the steps will be showed.
next         : undefined  // Callback before the forward action.
nextLabel    : 'Next >'   // Change the next button label.
select       : undefined  // Callback executed when the step is shown.
titleClick   : true       // Active the back and next action in the titles.
titleTarget  : undefined  // Choose the place where titles will be placed.
transition   : 'hide'     // Use transition between steps ('hide', 'fade' or 'slide').
validate     : false      // Active the jQuery Validation for each step.
```

## Usage

```html
<form>
  <fieldset title="Step 1">
    <legend>description one</legend>
    <!-- inputs -->
  </fieldset>

  <fieldset title="Step 2">
    <legend>description two</legend>
    <!-- inputs -->
  </fieldset>

  <input type="submit" />
</form>
```

```js
$('form').stepy();
```

## Functions

```js
$('form').stepy('step', 2); // Changes the form to the second step.

$('form').stepy('destroy'); // Destroy the Stepy's bind and gives you the raw element.
```

## Contributors

+ Almir Mendes
+ Andrey Fedoseev
+ Igor Tamashchuk
+ Rafael Machado

## Licence

The MIT License

## Donate

You can do it via [PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=X8HEP2878NDEG&item_name=jQuery%20Stepy). Thanks! (:
