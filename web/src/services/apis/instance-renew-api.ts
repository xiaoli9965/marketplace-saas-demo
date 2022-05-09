import {request} from "@@/plugin-request/request";

const BASE_URI = '/api/instanceRenew';

export async function instanceRenewPage(params?: Record<string, any>) {
  return request<R.Ret>(`${BASE_URI}/list`, {
    method: 'POST',
    data:params,
  });
}
