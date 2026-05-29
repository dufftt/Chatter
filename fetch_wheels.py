import urllib.request, re
url = 'https://mlc.ai/wheels'
req = urllib.request.Request(url)
with urllib.request.urlopen(req) as response:
    html = response.read().decode('utf-8')

for match in set(re.findall(r'href="(.*?[wW]in_amd64\.whl)"', html)):
    print(match)
