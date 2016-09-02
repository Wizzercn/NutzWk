# Bootstrap Rating Input in 2Kb

This is another plugin that eases the generation of rating stars for jQuery and Bootstrap.

It generates widgets like this:

![Rating example](http://curso-rails-mini-blog.s3.amazonaws.com/rating.png)

## But, why another damn rating plugin???

After searching for existing widgets, I found three categories of them:

  - The ones that depends on PNG images.
  - The ones that adds A LOT of JavaScript and CSS code to my project.
  - The ones that adds A LOT of JavaScript and CSS code and depends on PNG images.

I don't want to add a whole multipurpose library just to put a few stars in my interface, I want my rating stars to look awesome in retina screens without worrying about image versions and dynamically replacing them, and Bootstrap already includes a set of beautifully designed vectorial icons by Glyphicons, so I thought I could create something simpler.

## Ok, enough talking, tell me how this thing works!

If you're using bower to manage your frontend dependencies you can install this plugin by just issuing this command:

    bower install bootstrap-rating-input --save

Else you can just download `build/bootstrap-rating-input.min.js`, put it wherever you usually put JavaScripts in your project and include it on pages where you want to have forms with ratings:

    <script src="path/to/javascripts/bootstrap-rating-input.min.js" type="text/javascript"></script>

Now add a input of type *number* to your forms and add the class `rating` to it:

    <input type="number" name="your_awesome_parameter" id="some_id" class="rating" />

That's all! When page loads, you'll find a few stars where you'd expect to find the input. It works just like most of rating plugins, but you don't need to learn anything about options or initializations, it just works out of the box.

### Wait, where has my input gone?

The plugin transforms your number input into a hidden field and wraps it inside a div with the interactive stars that will catch your clicks and save the selected values into the hidden field. In this way the form can be submitted or value readed by jQuery normally.

### Nice, but I want to use a different number of stars

Sure! You can set min and max values adding `data-min` and `data-max`:

    <input class="rating" data-max="5" data-min="1" id="some_id" name="your_awesome_parameter" type="number" />

### Can I set a special value for empty ratings?

You can add the attribute `data-empty-value` to indicate which value should send the form when it have an empty rating. This can be used, for example, to have an special value indicating the user didn't seleted anything:

    <input class="rating" data-max="5" data-min="1" id="some_id" name="your_awesome_parameter" type="number" data-empty-value="0"/>

By default empty ratings will behave like a regular empty field.

### And what about clearing the stars?

By default once you set a value it remains set and you can only change it by another, but you can add a clear link by just defining the `data-clearable` attribute:

    <input class="rating" data-clearable="remove" id="some_id" name="your_awesome_parameter" type="number" />

The content of `data-clearable` will appear as label for the link. You can set a space or a &amp;nbsp; to make it appear as a naked close icon.

### Can I use custom icon classes?

Now you can use custom icons thanks to the awesome contribution by [johncadigan](https://github.com/johncadigan). You can set different icon classes from gliphicons or even load icons from other libraries you're using. For instance here is how you generate a heart rating input with font awesome (You can see it working in the `demo.html` file):

    <input type="number" name="your_awesome_parameter" id="some_id" class="rating" data-clearable="remove" data-icon-lib="fa" data-active-icon="fa-heart" data-inactive-icon="fa-heart-o" data-clearable-icon="fa-trash-o"/>

If you want to use [FontAwesome](http://fontawesome.io/), remember to include the library in your header:

    <header>
      ...
      <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
      ...
    </header>

### I don't want to be forced to add the `rating` class to the inputs

The `rating` class is used in combination with `input[type=number]` to let you autoload the rating plugin without coding anything, but you can apply this plugin to a input of any type by executing the method `rating` on a jQuery selection:

    $('input.my_class').rating();

## Requirements

You know... [Twitter Bootstrap](http://getbootstrap.com) and [jQuery](http://jquery.com)!

## Can I generate read-only stars for displaying?

If you think about it you don't want to use a plugin to generate static HTML code that is as simple as this:

    <i class='glyphicon glyphicon-star'></i><i class='glyphicon glyphicon-star'></i><i class='glyphicon glyphicon-star'></i><i class='glyphicon glyphicon-star-empty'></i><i class='glyphicon glyphicon-star-empty'></i>

You can easily generate such code with your favourite template engine and a loop. With Ruby and HAML it could look like this:

    / Given a variable val with the value you want to represent and a variable max that contains the maximum number of stars:
    - max.times do |i|
      %i{class: "glyphicon glyphicon-star#{'-empty' if i>val}"}

Well, HAML is awesome, but you are a programmer, so you'll be able to addapt this to your favorite language...

## It looks nice, but I want to complain because it doesn't fit my favorite use case

I have implemented this for my project in my environment and sharing it for free. Leave me an issue with your suggestions and I'll eventually push a fix, but this is MIT licensed, so you're welcome to fork this project, do pull requests with fixes and improvements, reimplement better versions of it for your own or do whatever you want, I'll be happy if it becomes useful or inspires at least one more person.

## Ok, I want to contribute

Nice! You're awesome, fork the project, and do whatever changes you want into `src/bootstrap-rating-input.js`. If you're kind enough I'll appreciate that you maintain the minified version updated and to ease this step I've automated minification with grunt, so if you have npm installed you can issue following command to update the minified version:

    $ npm install && grunt

Thanks!!