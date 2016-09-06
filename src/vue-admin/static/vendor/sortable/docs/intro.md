## Sortable

Sortable is an open-source javascript and CSS library which adds sorting functionality to tables. It is tiny (<code>&lt;2kb</code>) and has no dependencies.

#### Demo

Here is a demo of the "Bootstrap" theme.

<p style="display: none"></p>
<table class="sortable-theme-bootstrap" data-sortable> <thead> <tr> <th data-sortable="false">Browser</th> <th data-sorted="true" data-sorted-direction="descending">Usage</th> <th>Initial release</th> <th>Stable version</th> </tr> </thead> <tbody> <tr> <td>Chrome</td> <td>42.68%</td> <td data-value="2008">September 2, 2008</td> <td>31.0.1650.57</td> </tr> <tr> <td>Internet Explorer</td> <td>25.44%</td> <td data-value="1995">August 16, 1995</td> <td>11.0.1</td> </tr> <tr> <td>Firefox</td> <td>20.01%</td> <td data-value="2002">September 23, 2002</td> <td>25.0.1</td> </tr> <tr> <td>Safari</td> <td>8.39%</td> <td data-value="2003">January 7, 2003</td> <td>7.0</td> </tr> <tr> <td>Opera</td> <td>1.03%</td> <td data-value="1994">Late 1994</td> <td>18.0.1284.49</td> </tr> <tr> <td>Other</td> <td data-value="0">2.44%</td> <td data-value="-1"></td> <td></td> </tr> </tbody> </table>
<p style="display: none"></p>

#### Features

- Drop-in script and styles
- 6 beautiful CSS themes
- Tiny footprint (<code>&lt;2kb</code>) and no dependencies
- Looks and behaves great on mobile devices

#### Requirements

- None

#### Browser Support

- IE8+
- Firefox 4+
- Current WebKit (Chrome, Safari)
- Opera

#### Including

For the most common usage of sortable, you'll want to include following:

```html
<script src="sortable.min.js"></script>
<link rel="stylesheet" href="sortable-theme-bootstrap.css" />
```

Now any table with the attribute `sortable` will be made sortable. To get the styling, you'll also need to add a class name to the table to match the theme you chose:

```html
<table class="sortable-theme-bootstrap" data-sortable>
    <!-- ... -->
</table>
```

This example uses the "bootstrap" theme. For a full list of supported themes, check out [Themes](/sortable/api/themes).

#### Learn More

To learn more about how to use sortable, visit our API pages.

- [Options](http://github.hubspot.com/sortable/api/options)
- [Themes](http://github.hubspot.com/sortable/api/themes)

#### Credits

Sortable is free and open-source, released on the [MIT License](https://github.com/HubSpot/sortable/blob/master/LICENSE).

#### Credits

Sortable was built by [Adam Schwartz](http://twitter.com/adamfschwartz)

<!-- Resources for the demos -->
<p style="-webkit-transform: translateZ(0)"></p>
<script src="/sortable/js/sortable.js"></script>
<style>
.hs-doc-content table {
    border: 0;
}
.hs-doc-content table th {
    background: initial;
}
</style>
<link rel="stylesheet" href="/sortable/css/sortable-theme-bootstrap.css">
