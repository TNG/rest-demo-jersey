[![Build Status](https://travis-ci.org/TNG/rest-demo-jersey.svg?branch=master)](https://travis-ci.org/TNG/rest-demo-jersey)
[![Coverage Status](https://coveralls.io/repos/github/TNG/rest-demo-jersey/badge.svg?branch=master)](https://coveralls.io/github/TNG/rest-demo-jersey?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/578db6313e6a8b00457f8f82/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/578db6313e6a8b00457f8f82)

# rest-demo-jersey
Spring-boot and Jersey based REST service showing the integration of [rest-schemagen](http://github.com/Mercateo/rest-schemagen).

<img src="https://rawgit.com/TNG/rest-demo-jersey/master/doc/service.svg" alt="service schematics">

## Example usage

### get base info
GET: http://localhost:9090

* stations: GET /stations 
* statistics: GET /weather/statistics 
* query: GET /weather/49.0/11.0 

### get stations
GET: http://localhost:9090/stations

* create: POST /stations {u'type': u'object', u'properties': {u'latitude': {u'type': u'number'}, u'name': {u'type': u'string'}, u'longitude': {u'type': u'number'}}}
* self: GET /stations?offset=0&limit=100 

### create station
POST: http://localhost:9090/stations

* self: GET /stations/d6b4cf17-144c-443d-8ca3-2ed2bc371ef3 
* delete: DELETE /stations/d6b4cf17-144c-443d-8ca3-2ed2bc371ef3 
    
