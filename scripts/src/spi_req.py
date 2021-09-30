# coding=utf-8
import json
import time
import urllib
import uuid
from hashlib import sha256
import hmac

from urlparse import (parse_qs)
import requests
import base64


def _saas_spi_signature(app_key, payload, method="GET"):
    def _get_utf8_value(value):
        if not isinstance(value, str) and not isinstance(value, unicode):
            value = str(value)

        if isinstance(value, unicode):
            return value.encode('utf-8')
        return value

    keys = sorted(payload.keys())

    pairs = []
    for key in keys:
        _key = urllib.quote(key, safe='')
        val = payload[key]
        if isinstance(val, list):
            for v in val:
                _val = _get_utf8_value(v)
                pairs.append("%s=%s" % (_key, urllib.quote(_val, safe='-_~')))
        else:
            _val = _get_utf8_value(val)
            pairs.append("%s=%s" % (_key, urllib.quote(_val, safe='-_~')))

    string_to_sign = '%s\n%s' % (method, '&'.join(pairs))
    _hmac = hmac.new(app_key, digestmod=sha256).copy()
    _hmac.update(string_to_sign.encode())
    return base64.b64encode(_hmac.digest()).strip()


ISO8601 = '%Y-%m-%dT%H:%M:%SZ'

HEAD_SIGNATURE_KEY = 'app_signature'
QUERY_SIGNATURE_KEY = 'signature'
QUERY_TIME_STAMP_KEY = 'time_stamp'


def _get_ts(ts=None):
    ''' get formatted UTC time '''
    if not ts:
        ts = time.gmtime()

    return time.strftime(ISO8601, ts)


def __spi_proxy():
    return None


def __check_spi_response(resp, test_conn=False):
    if resp is None:
        return False, 'Saas spi failed. [%s]' % resp

    if test_conn:
        # Test connection, the network is ok, return true
        return True, {'test_conn': 'ok'}

    if resp.status_code != 200 or 'application/json' != resp.headers.get('Content-Type'):
        return False, 'SPI接口响应不合法, status_code:[%s]  Content-Type:[%s]' % \
               (resp.status_code, resp.headers.get('Content-Type'))

    body = resp.json()
    if not body.get('success', False):
        return False, 'Saas spi operation failed, response body [%s]' % body

    return True, body


def __request(url, app_key, payload, test_conn=False):
    """
    Spi request
    :param url:  app message url
    :param app_key: app key
    :param payload: spi request params
    :return: (True, body) or (False, error msg)
    """

    url_parse = url.split("?")
    call_url = url_parse[0]
    query_params = {}
    # add message url raw query params
    query_params.update(parse_qs(url_parse[1]) if len(url_parse) == 2 else {})
    query_params.update(payload)
    # add time_stamp
    query_params[QUERY_TIME_STAMP_KEY] = _get_ts()
    _signature = _saas_spi_signature(app_key, query_params)
    # add signature

    try:
        print '[DEBUG] url:[%s]  params: %s' % (call_url, query_params)
        print ''
        resp = requests.request(method="GET", url=call_url, params=query_params, timeout=(2, 6), proxies=__spi_proxy(),
                                headers={HEAD_SIGNATURE_KEY: _signature})
    except Exception as e:
        print "[ERROR] ", e.message
        return False, e.message

    print "[INFO] Saas spi status_code:[%s] response [%s]." % (resp.status_code, resp.content if resp else "")
    status, body = __check_spi_response(resp, test_conn)
    if not status:
        print "[ERROR] " + body

    return status, body

APP_ID = "app-rxm3bnm8"
APP_KEY = "Qa6Y3LaG8e6cW/RmR9mbw/XsSTt1zyVmeqJcVq4N5Y+Y5MHtPwNYX/T1wgK2dR0C"
# URL = "http://139.198.181.88/api/spi"
URL = "http://127.0.0.1:8080/api/spi"

mock = {
    'CreateAppInstance': {
        "action": "CreateAppInstance",
        "user_id": "usr-rymOTvEl",
        "app_id": APP_ID,
        "spec": base64.b64encode("alex 套餐"),
        "period": '1_day',
        "cloud_info": base64.b64encode(str(json.dumps({
            "sso_server": "http://sso.qingcloud.com",
            "api_server": "http://api.qingcloud.com:7777"
        }))),
        "debug": True
    },
    'RenewAppInstance': {
        "action": "RenewAppInstance",
        "spec": base64.b64encode("alex 高性能"),
        "period": '10_day',
        "instance_id": 's-i-1kpw50kg7vqm',
    },
    'UpgradeAppInstance': {
        "action": "UpgradeAppInstance",
        "instance_id": 's-i-i1dwrj3ize5q',
        "spec": base64.b64encode("alex618促销适用"),
        "period": '999_day',
    },
    'ExpireAppInstance': {
        "action": "ExpireAppInstance",
        "instance_id": 's-i-i1dwrj3ize5q',
    },
    'DeleteAppInstance': {
        "action": "DeleteAppInstance",
        "instance_id": 's-i-d6ezbarpe8sa',
    },
    'TestConnection': {
        "action": "TestConnection",
    },
}


if __name__ == '__main__':
    __request(URL, APP_KEY, mock['CreateAppInstance'], False)
