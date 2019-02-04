"""
Script creates status_s index for testing.
"""
import argparse
import json
import os

import requests

script_dir = os.path.dirname(os.path.realpath(__file__))

parser = argparse.ArgumentParser()
parser.add_argument('--attributes', nargs='+', default=[])
args = parser.parse_args()

index_name = 'status_s'
f_mappings = 'active_mappings.json'
f_docs = 'wifiAccessPoint.json'
elastic_url = 'http://localhost:9200'
auth = ('elastic', 'changeme')

version = float(json.loads(requests.get('http://localhost:9200', auth=auth).text)['version']['number'].split('.')[0])
type_name = 'active' if version < 7 else '_doc'

requests.delete(f'{elastic_url}/{index_name}', auth=auth)
requests.put(f'{elastic_url}/{index_name}', auth=auth)

with open(f'{script_dir}/{f_mappings}') as f:
    mappings = json.loads(f.read())

if args.attributes:
    [mappings['properties'].pop(key) for key in list(mappings['properties'].keys()) if key not in args.attributes]

requests.put(f'{elastic_url}/{index_name}/_mapping/{type_name if version < 7 else ""}', json=mappings, auth=auth)

with open(f'{script_dir}/{f_docs}') as f:
    features = json.load(f)['features']

for item in [item for item in features if item['status_s'] == 'active']:
    if args.attributes:
        [item.pop(key) for key in list(item.keys()) if key not in args.attributes]

    requests.put(f'{elastic_url}/{index_name}/{type_name}/{item["id"]}', json=item, auth=auth)

requests.put(f'{elastic_url}//_xpack/security/role/status_admin', json={'indices': [{'privileges': ['all'], 'names': ['status*']}]}, auth=auth)
requests.put(f'{elastic_url}/_xpack/security/user/admin', json={'password': 'statusadmin', 'roles': ['status_admin']}, auth=auth)
