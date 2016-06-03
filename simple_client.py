#!/usr/bin/env python

from __future__ import print_function

import json
import requests

headers={'content-type': 'application/json'}

base_url = "http://localhost:9090"

def do_request(method, url):
    response = requests.get(url)
    response_json = response.json()

    print('{}: {}'.format(method, url))
    for link in response_json['_schema']['links']:
        print("    {}: {} {} {}".format(link['rel'], link['method'], link['href'], link['schema'] if 'schema' in link else ''))

    return response_json

print("*** get base")
do_request('GET', base_url)

print()
print("*** create station")
response = requests.post(base_url + "/stations", data=json.dumps({'name': 'test', 'longitude': 11.0, 'latitude': 49.0}), headers=headers).json()
station_id = response['id']

print()
print("*** get stations")
stations = do_request('GET', base_url + '/stations')
for station in stations['members']:
  print("   ", station['name'], station['longitude'], station['latitude'], station['id'])

do_request('GET', base_url + '/stations/{}'.format(station_id))


