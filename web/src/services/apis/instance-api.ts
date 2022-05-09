import {request} from "@@/plugin-request/request";

const BASE_URI = '/api/instance';

export async function instancePage(params?: Record<string, any>) {
  return request<R.Ret>(`${BASE_URI}/list`, {
    method: 'POST',
    data:params,
  });
}
