#Client
This file contains a very basic implementation of a client-side ```enhance``` function, that takes an API response and adds
some logic based on the supplied JSON Hyperschema links. This custom logic will allow you to query the REST API in a proper way.

Example response from this REST API:
```json
{
  "_schema":
    [
      {
        "rel": "create",
        "method": "POST",
        "href": "/stations"
      }
      ],
  "members":
    [
      {"_schema":
        [
          {
            "rel": "delete",
            "method": "DELETE",
            "href": "/stations/1"
          }
        ],
        "id": "1",
        "name": "The Westin Grand München",
        "latitude": 11.6198367,
        "longitude": 48.1503496
      }
    ]
}
```

```javascript
function addFunctions(schema, target) {
    var o = {};

    schema.forEach(function(link) {
        o['$' + link.rel] = function(options) {
            return fetch(link.href, Object.assign({
                method: link.method
            }, options));
        }
    });

    Object.assign(target, o);
}

function enhance(input) {
    // response contains schema
    if(input._schema != null) {
        addFunctions(input._schema, input);
    }

    // response is a collection
    if(input.members != null) {
        input.members.forEach(function(member) {
            addFunctions(member._schema, member);
        });
    }

    return input;
}
```

When calling ```enhance(JSON.parse(response))``` the output will looks like:

```javascript
{ _schema: [ { rel: 'create', method: 'POST', href: '/stations' } ],
  members: [
      {
        _schema: [ { rel: 'delete', method: 'DELETE', href: '/stations/1' } ],
        id: '1',
        name: 'The Westin Grand München',
        latitude: 11.6198367,
        longitude: 48.1503496,
        '$delete': [Function]
      }
  ],
  '$create': [Function]
}
```

This enables us to call ```$create()``` on the root object and ```$delete()``` on all members.



