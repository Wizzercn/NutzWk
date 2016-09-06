## API Options

#### Initializing sortable

To be properly initialized and pick up the default styling, your table must add the attribute `data-sortable`.

Example:

```html
<table data-sortable>
    <!-- ... -->
</table>
```

##### `init`

All tables on the page will be automatically initted when the page is loaded.

If you add tables with javascript, call `init` after they are added to the page:

```coffeescript
Sortable.init()
```

##### `initTable`

To initialize an individual table, call `initTable`.

```coffeescript
exampleTable = document.querySelector('#exampleTable')
Sortable.initTable(exampleTable)
```

#### Sorting options

##### `data-value`

By default, sortable will automatically detect whether a column contains alpha or numeric data. If you'd rather have a particular column sort on custom data, set the attribute `data-value` on a `<td>`.

```html
<table data-sortable>
    <thead><!-- ... --></thead>
    <tbody>
        <tr>
            <td>Adam</td>
            <td data-value="1384904153699">3 hours ago</td>
            <td><a href="#">New</a></td>
        </tr>
        <!-- ... -->
    </tbody>
</table>
```

##### `th` `data-sortable="false"`

To disable sorting on a particular column, add `data-sortable="false"` to the `<th>` for that column.

```html
<table data-sortable>
    <thead>
        <tr>
            <th>Name</th>
            <th>Date</th>
            <th data-sortable="false">Actions</th>
        </tr>
    </thead>
    <tbody><!-- ... --></tbody>
</table>
```

<!-- Resources for the demos -->
<p style="-webkit-transform: translateZ(0)"></p>
<script src="/sortable/js/sortable.js"></script>
<link rel="stylesheet" href="/sortable/css/sortable-theme-light.css" />
