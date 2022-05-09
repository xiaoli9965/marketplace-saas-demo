import {request} from "@@/plugin-request/request";

const BASE_URI = '/api/';

export async function authUrlLong(data?: Record<string, any>) {
  return request<R.Ret>(`${BASE_URI}/authUrl`, {
    method: 'POST',
    data,
  });
}


export async function requestQingcCloudSso(path, opt: any) {
  return request<R.Ret>(path, {
    method: 'POST',
    ...opt,
  });
}
